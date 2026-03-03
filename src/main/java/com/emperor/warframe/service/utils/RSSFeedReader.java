package com.emperor.warframe.service.utils;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

public class RSSFeedReader {
    private static final Logger logger = LoggerFactory.getLogger(RSSFeedReader.class);

    public static List<SyndEntry> readFeed(String url) {
        List<SyndEntry> entryList = new ArrayList<>();
        try (var stream = URI.create(url).toURL().openStream()) {
            SyndFeed feed = new SyndFeedInput().build(new XmlReader(stream));
            entryList = feed.getEntries();
        } catch (IOException e) {
            logger.error("Error occured with I/O: {}", e);
        } catch (FeedException e) {
            logger.error("Error occured with feed: {}", e);
        }
        return entryList;
    }
}
