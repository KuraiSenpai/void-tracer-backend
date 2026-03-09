package com.emperor.warframe.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tukaani.xz.LZMAInputStream;
import org.tukaani.xz.MemoryLimitException;

import com.emperor.warframe.service.DownloadService;
import com.emperor.warframe.service.IndexService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class IndexServiceImpl implements IndexService {
    private static final Logger logger = LoggerFactory.getLogger(IndexServiceImpl.class);

    @Value("${warframe.index.url}")
    private String warframeIndexUrl;

    @Value("${warframe.index.file.name}")
    private String warframeIndexFileName;

    @Value("${warframe.index.file.path}")
    private String warframeIndexFilePath;

    @Autowired
    DownloadService downloadService;

    @Override
    public boolean getIndex() {
        boolean downloadIsSuccess = downloadService.downloadIndex(warframeIndexUrl, warframeIndexFileName,
                warframeIndexFilePath);
        if (downloadIsSuccess) {
            return processIndex();
        }
        return false;
    }

    private boolean processIndex() {
        File inputFile = new File(warframeIndexFilePath + warframeIndexFileName);
        File outputFile = new File(warframeIndexFilePath + warframeIndexFileName.replace(".lzma", ""));

        try {
            decompressLZMA(inputFile, outputFile);
        } catch (MemoryLimitException e) {
            log.error("Failed: System ran out of memory during decompression.", e);
            return false;
        } catch (Exception e) {
            log.error("Failed:", e);
            return false;
        }
        return true;
    }

    public void decompressLZMA(File inputFile, File outputFile) throws IOException {
        try (InputStream fis = new FileInputStream(inputFile);
                BufferedInputStream bis = new BufferedInputStream(fis)) {
            int props = bis.read();
            if (props == -1)
                throw new EOFException();

            byte[] dictBytes = new byte[4];
            bis.read(dictBytes);
            int dictSize = 0;
            for (int i = 0; i < 4; i++) {
                dictSize |= ((dictBytes[i] & 0xFF) << (8 * i));
            }

            // Skip the 8 bytes of uncompressed size
            for (int i = 0; i < 8; i++) {
                bis.read();
            }

            // We use -1L for uncompressedSize to tell it to look for the End-of-Stream
            // marker.
            LZMAInputStream lzmaIn = new LZMAInputStream(bis, -1L, (byte) props, dictSize);

            try (OutputStream out = new FileOutputStream(outputFile);
                    BufferedOutputStream bOut = new BufferedOutputStream(out)) {

                byte[] buffer = new byte[16384];
                int n;
                while ((n = lzmaIn.read(buffer)) != -1) {
                    bOut.write(buffer, 0, n);
                }
                bOut.flush();
            }

            lzmaIn.close();
        }
    }

}
