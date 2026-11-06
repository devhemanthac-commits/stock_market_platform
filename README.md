# 📈 StockSim — Virtual Stock Market Platform

A full-featured stock market paper trading and simulation web application built with **Java Servlets (Jakarta EE 10)**, **Apache Tomcat 10**, and a modern, high-performance vanilla **HTML5 / CSS3 / JavaScript** frontend.

StockSim provides an interactive sandbox for users to learn trading, test investment strategies, execute buy/sell orders, and track portfolio performance in real time using virtual funds.

---

## 🚀 Key Features

- **💰 ₹10,00,000 Demo Capital**: Every registered user starts with virtual capital to practice risk-free trading.
- **⚡ Live Market Simulator**: In-memory scheduled ticker engine continuously simulates market volatility, updating stock prices, day highs/lows, and trading volume every 2 seconds.
- **💹 Interactive Trading Terminal**: Seamlessly execute Market Buy and Sell orders with dynamic quantity and cost calculations.
- **📊 Real-Time Portfolio Tracking**: Instant valuation of holdings, average buy prices, current profit & loss (P&L), and percentage returns.
- **⭐ Personalized Watchlist**: Pin favorite stocks for instant monitoring from both the dashboard and market view.
- **📜 Complete Transaction Ledger**: Detailed audit log of all executed trades with unique order IDs and timestamps.
- **🎨 Modern Dark Fintech UI**: Custom-designed dark aesthetic inspired by top institutional trading terminals, fully responsive and free of heavy UI framework dependencies.

---

## 🛠️ Technology Stack

| Layer | Technology |
|---|---|
| **Backend** | Java 17+, Jakarta Servlet 6.0, Google Gson 2.10.1 |
| **Server** | Apache Tomcat 10.1.x |
| **Frontend** | Semantic HTML5, Custom CSS3 (CSS Variables & Flexbox/Grid), Modern ES6+ JavaScript |
| **Build & Dependency Tool** | Apache Maven |
| **Data Persistence** | In-Memory thread-safe data store (`ConcurrentHashMap`) with background ticker service |

---

## 📁 Project Architecture

```
StockSim/
├── pom.xml                                   # Maven dependencies & build configuration
├── README.md                                 # Project documentation
├── .gitignore                                # Git ignore patterns
└── src/
    └── main/
        ├── java/com/stocksim/
        │   ├── model/
        │   │   ├── User.java                 # User account & cash balance
        │   │   ├── Stock.java                # Stock entity with live tick logic
        │   │   ├── Holding.java              # Portfolio stock holding & average cost basis
        │   │   └── Transaction.java          # Order receipt record
        │   ├── data/
        │   │   └── DataStore.java            # Thread-safe in-memory store & scheduled live ticker
        │   ├── filter/
        │   │   └── AuthFilter.java           # Session authentication & route protection
        │   └── servlet/
        │       ├── LoginServlet.java         # User authentication & session management
        │       ├── RegisterServlet.java      # New user onboarding
        │       ├── MarketDataServlet.java    # Live prices JSON API (/api/market/*)
        │       ├── TradeServlet.java         # Buy/Sell execution & balance verification
        │       ├── PortfolioServlet.java     # Holdings valuation & P&L calculator
        │       ├── HistoryServlet.java       # Transaction history endpoint
        │       ├── WatchlistServlet.java     # User watchlist operations
        │       └── UserServlet.java          # Authenticated user profile data
        └── webapp/
            ├── WEB-INF/
            │   └── web.xml                   # Deployment descriptor
            ├── css/
            │   └── style.css                 # Custom dark fintech styling
            ├── js/
            │   └── app.js                    # State management, AJAX polling & toast alerts
            ├── index.html                    # Platform landing page
            ├── login.html                    # User sign-in
            ├── register.html                 # User registration
            ├── dashboard.html                # Main trading dashboard & summary cards
            ├── markets.html                  # Searchable market watch table
            ├── trade.html                    # Order terminal with live price updates
            ├── portfolio.html                # Holdings management & P&L breakdown
            └── history.html                  # Executed trades log
```

---

## ⚙️ Setup & Running Locally

### Prerequisites
- **JDK 17** or newer
- **Apache Tomcat 10.1.x**
- **IntelliJ IDEA** (Ultimate or Community with Smart Tomcat plugin)

### Running with IntelliJ IDEA & Smart Tomcat:
1. Open IntelliJ IDEA and choose **File → Open...**, selecting the `StockSim` folder.
2. Ensure Maven syncs dependencies (`jakarta.servlet-api` and `gson`).
3. Open **Run → Edit Configurations...** and add a **Smart Tomcat** configuration:
   - **Tomcat server**: Select your Apache Tomcat 10.1.x installation path.
   - **Deployment directory**: `src/main/webapp`
   - **Context path**: `/StockSim`
   - **Server port**: `8080`
4. Click **Apply** and **Run**.
5. Navigate to:
   ```
   http://localhost:8080/StockSim/
   ```

---

## 🔒 Security & Data Integrity

- Route protection via `AuthFilter` ensuring unauthenticated users cannot invoke private trading or portfolio APIs.
- Concurrency-safe transactions utilizing `ConcurrentHashMap` and thread synchronization on balance and holding operations.
- Dynamic input validation preventing negative share orders and over-selling.

---

## 📄 License
This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.
