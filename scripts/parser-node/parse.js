import fs from "fs";
import WorldState from "warframe-worldstate-parser";

async function getAndParse() {
  const timeout = new Promise((_, reject) =>
    setTimeout(
      () => reject(new Error("Parser Timeout: Check if CDN is up.")),
      5000, // 5 seconds
    ),
  );

  const rawData = fs.readFileSync(0, "utf-8");

  if (!rawData) {
    throw new Error("No data received from Java stdin");
  }

  try {
    const ws = await Promise.race([WorldState.build(rawData), timeout]);

    process.stdout.write(JSON.stringify(ws));
    process.exit(0);
  } catch (e) {
    process.stderr.write("Node Logic Error: " + e.message);
    process.exit(1);
  }
}

getAndParse();
