import WorldState from "warframe-worldstate-parser";

async function getAndParse() {
  const timeout = new Promise((_, reject) =>
    setTimeout(
      () => reject(new Error("Parser Timeout: Check if CDN is up.")),
      5000, // 5 seconds
    ),
  );

  try {
    const response = await fetch("https://api.warframe.com/cdn/worldState.php");
    if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);

    const rawData = await response.text();

    const ws = await Promise.race([WorldState.build(rawData), timeout]);

    process.stdout.write(JSON.stringify(ws));
    process.exit(0);
  } catch (e) {
    process.stderr.write("Node Logic Error: " + e.message);
    process.exit(1);
  }
}

getAndParse();
