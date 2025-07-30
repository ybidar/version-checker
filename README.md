# Version Checker

A Java-based web scraper that checks for the latest version updates of **iOS**, **iPadOS**, and **Android** operating systems and related beta releases. If any version changes are detected, the system sends an email notification to configured recipients.

---

## Features

- Scrapes official version update pages for iOS, iPadOS, and Android.
- Detects changes compared to previously stored versions.
- Sends email notifications to:
    - Primary recipients for version changes
    - Secondary recipients for debug/diagnostic messages
- Supports running both locally and via GitHub Actions.
- Stores version state in a local JSON file.
- Configurable logging with per-run log files.
- GitHub Actions integration:
    - Runs every 6 hours
    - Also triggers on every push to `main` branch

---

## Tech Stack

- Java 11+
- Maven
- Jsoup (HTML parsing)
- Jackson (JSON handling)
- JavaMail (email notifications)
- GitHub Actions (for CI and scheduled execution)

---

## Configuration

### Email Setup

- Email username and password are read from environment variables:
    - `EMAIL_USERNAME`
    - `EMAIL_PASSWORD`

- When run in GitHub Actions, use GitHub Secrets for these.

### How to Run

#### Locally

Add environment variables for your mail account (password can be a token as well)
export EMAIL_USERNAME=your_email@gmail.com
export EMAIL_PASSWORD=your_password

Run the following command

```mvn clean compile exec:java -Dexec.mainClass="com.yb.versionchecker.VersionChecker"```

### Output
- Version changes are logged in the console and a timestamped log file.
- The version_state.json is updated after every change.

### Extending the project
To add a new OS:

- Add the OS to the JSON structure in version_state.json.
- Update scraping logic in Scraper.java.
- No need to modify VersionInfo core logic thanks to dynamic OS handling.