# Java JDBC Multithreading Demo: CSV to PostgreSQL

This project is a hands-on demonstration of using pure Java concurrency and JDBC to perform high-speed, multithreaded data insertion into a PostgreSQL database.

It's designed as a learning exercise to understand the fundamentals of manual thread management and batch JDBC operations, without relying on higher-level abstractions like connection pools (`HikariCP`) or thread pools (`ExecutorService`).

## 🎓 Learning Objectives

The primary goal of this project is to learn:

- How to implement manual thread management using the `Thread` and `Runnable` classes.
- The most important concurrency-safety principle for databases: **one connection per thread**.
- How to wait for all worker threads to complete using `thread.join()`.
- How to perform high-performance batch inserts using JDBC's `PreparedStatement.addBatch()` and `executeBatch()`.
- How to read and parse a large CSV file efficiently using `BufferedReader`.
- How to divide a large dataset into smaller, equal-sized chunks for parallel processing.

## ✨ Features

- **Pure Java Concurrency:** Uses only the built-in `java.lang.Thread` and `java.lang.Runnable`.
- **Pure JDBC:** Uses only the standard `java.sql` package and the PostgreSQL JDBC driver. No external connection pooling libraries.
- **Batch Inserts:** Each thread inserts data in batches (e.g., 1,000 rows at a time) within a single transaction for massive performance gains.
- **Real-World Data:** Designed to parse and load a real CSV dataset (music track data).
- **Gradle Build:** A minimal `build.gradle` file to manage the single JDBC dependency.

## Prerequisites

Before you begin, ensure you have the following installed:

1.  **Java JDK:** Version 11 or higher.
2.  **PostgreSQL:** A running PostgreSQL server.
3.  **Your Data:** The music CSV file this program was built for.

## 🚀 How to Set Up and Run

### 1. Set Up the Database

Connect to your PostgreSQL instance (using `psql`, DBeaver, or another client) and run the `setup.sql` script. This will create the `music_tracks` table with the correct schema for the CSV data.

```sql
-- From setup.sql
DROP TABLE IF EXISTS music_tracks;

CREATE TABLE music_tracks (
    id SERIAL PRIMARY KEY,
    artist_name TEXT,
    track_name TEXT,
    track_id VARCHAR(100) UNIQUE,
    popularity INTEGER,
    year INTEGER,
    genre VARCHAR(100),
    danceability REAL,
    energy REAL,
    key INTEGER,
    loudness REAL,
    mode INTEGER,
    speechiness REAL,
    acousticness REAL,
    instrumentalness REAL,
    liveness REAL,
    valence REAL,
    tempo REAL,
    duration_ms INTEGER,
    time_signature INTEGER
);
```

### 2. Change the database configuration in App.java

Change with your database credential.

```java
private static final String JDBC_URL = "jdbc:postgresql://172.30.0.2:5432/OneMilInsert";
    private static final String USERNAME = "psql_user";
    private static final String PASSWORD = "password";
```

### 3. run the program

Open your terminal in the root of the project directory (where build.gradle is) and run the application using the Gradle wrapper.

```bash
./gradlew run
```
