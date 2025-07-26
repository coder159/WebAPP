#!/usr/bin/env bash
set -e

echo "▶ Installing Java, Maven, Chrome (for Selenium)…"
apt-get update -qq
apt-get install -y --no-install-recommends curl gnupg ca-certificates default-jdk maven unzip
# Google Chrome
install -d -m 0755 /etc/apt/keyrings
curl -fsSL https://dl.google.com/linux/linux_signing_key.pub | gpg --dearmor -o /etc/apt/keyrings/google-chrome.gpg
echo "deb [arch=amd64 signed-by=/etc/apt/keyrings/google-chrome.gpg] http://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google-chrome.list
apt-get update -qq
apt-get install -y --no-install-recommends google-chrome-stable

echo "▶ Installing Node dependencies…"
cd server && npm ci
cd ../client && npm ci

echo "▶ Pre-building Java projects (skip tests)…"
cd ../tests/api && mvn -q -DskipTests clean install
cd ../ui && mvn -q -DskipTests clean install

echo "✅ Setup complete."
