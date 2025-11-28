# 📦 QKart E-Commerce Automation Test Suite

End-to-End UI automation framework for the **QKart E-Commerce Web Application**, built using **Selenium WebDriver, Java, TestNG & Gradle**.
The framework follows the **Page Object Model (POM)** design pattern — each UI page has a dedicated class with reusable interaction methods.
A **custom TestNG Listener** is integrated to capture screenshots automatically for every test event.

---

## 🚀 Features Automated

| Module Tested      | Coverage                                    |
| ------------------ | ------------------------------------------- |
| Registration       | New user signup, duplicate user handling    |
| Login              | Successful + negative authentication        |
| Product Search     | Keyword validation, invalid search handling |
| Cart Management    | Add/update/remove items, multi-item flow    |
| Checkout           | Address entry, cart total, order placement  |
| Payment Validation | Insufficient balance alert & error handling |
| Multi-Tab Session  | Cart persistence across browser tabs        |
| Advertisements     | iframe interactions + click validation      |
| Contact Form       | Input submission & popup validation         |

---

## 🧪 Technology Stack

| Component      | Used                                  |
| -------------- | ------------------------------------- |
| Language       | Java 21                               |
| Automation     | Selenium WebDriver 4.21.0             |
| Test Framework | TestNG                                |
| Build Tool     | Gradle                                |
| Design Pattern | Page Object Model (POM)               |
| Reporting      | Console + Screenshot capture          |
| Listener       | Custom `ITestListener` implementation |

---

## 📁 Project Structure

```
QKART_AUTOMATION/
 └── src/test/java/QKART_TESTNG/
      ├── pages/              # Page Object Model classes
      │    ├── Home.java
      │    ├── Login.java
      │    ├── Register.java
      │    ├── Checkout.java
      │    └── SearchResult.java
      ├── QKART_Tests.java    # End-to-end Test Scenarios
      ├── TestNGListener.java # Screenshot-capturing listener
      └── testng.xml          # Suite + parameter config
 └── build.gradle             # Dependencies + runner config
 └── screenshots/             # Execution screenshots auto-stored
```

---

## 🏁 How to Execute Tests

### 🔨 Build Project

```
./gradlew build
```

### ▶ Run Entire Test Suite

```
./gradlew test
```

### 🧪 Run via TestNG suite file

```
./gradlew test --tests QKART_TESTNG.QKART_Tests
```

---

## 📸 Screenshot Logging

* Screenshots captured automatically for every test
* Stored in `/screenshots/` with timestamp & test name
* Triggered via **custom TestNG listener**

```
onTestStart
onTestSuccess
onTestFailure
onTestFailedButWithinSuccessPercentage
onTestSkipped
```

---

## 📌 Future Enhancements

| Planned Upgrade                  | Benefit                         |
| -------------------------------- | ------------------------------- |
| Allure / Extent HTML Reporting   | Visual reports + trend tracking |
| Parallel execution               | Faster regression cycles        |
| CI/CD (GitHub Actions/Jenkins)   | Automated nightly builds        |
| Data-Driven Testing (Excel/JSON) | Wider input coverage            |
| Cloud Selenium Grid Integration  | Cross-browser cross-OS testing  |

---

### ⭐ If this project helped or interests you, consider giving the repo a star!

---
