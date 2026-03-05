import WorldState from "warframe-worldstate-parser";

async function getAndParse() {
  const timeout = new Promise((_, reject) =>
    setTimeout(
      () => reject(new Error("Parser Timeout: Check if CDN is up.")),
      5000, // 5 seconds
    ),
  );

  try {
    const response = await fetch(
      "https://api.warframe.com/cdn/worldState.php",
      {
        headers: {
          "User-Agent":
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
          Accept: "text/plain, */*",
          "Accept-Language": "en-US,en;q=0.9",
        },
      },
    );

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const rawData = await response.text();

    const ws = await Promise.race([WorldState.build(rawData), timeout]);

    process.stdout.write(JSON.stringify(ws));
    process.exit(0);
  } catch (e) {
    process.stderr.write("Node Logic Error: " + e.message);
    process.exit(1);
  }
}

getAndParse().catch((err) => {
  console.error("Fatal Crash:", err);
  process.exit(1);
});
