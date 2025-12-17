# Script MySQL Complet - Projet Spring Boot

## 📋 Description
Script SQL complet pour créer et peupler la base de données du projet Spring Boot avec des données de test réalistes.

```sql
-- =============================================
-- CRÉATION DE LA BASE DE DONNÉES ET DES TABLES
-- =============================================

-- Créer la base de données
CREATE DATABASE IF NOT EXISTS springbootdb;
USE springbootdb;

-- Table User
CREATE TABLE IF NOT EXISTS user (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(125) NOT NULL UNIQUE,
    password VARCHAR(125) NOT NULL,
    fullname VARCHAR(125) NOT NULL,
    role VARCHAR(125) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table BidList (avec relation vers User)
CREATE TABLE IF NOT EXISTS bidlist (
    bid_list_id INT PRIMARY KEY AUTO_INCREMENT,
    account VARCHAR(30) NOT NULL,
    type VARCHAR(30) NOT NULL,
    bid_quantity DOUBLE,
    ask_quantity DOUBLE,
    bid DOUBLE,
    ask DOUBLE,
    benchmark VARCHAR(125),
    bid_list_date TIMESTAMP,
    commentary VARCHAR(125),
    security VARCHAR(125),
    status VARCHAR(10),
    trader VARCHAR(125),
    book VARCHAR(125),
    creation_name VARCHAR(125),
    creation_date TIMESTAMP,
    revision_name VARCHAR(125),
    revision_date TIMESTAMP,
    deal_name VARCHAR(125),
    deal_type VARCHAR(125),
    source_list_id VARCHAR(125),
    side VARCHAR(125),
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE SET NULL
);

-- Table CurvePoint
CREATE TABLE IF NOT EXISTS curvepoint (
    id INT PRIMARY KEY AUTO_INCREMENT,
    curve_id INT,
    as_of_date TIMESTAMP,
    term DOUBLE,
    value DOUBLE,
    creation_date TIMESTAMP
);

-- Table Rating
CREATE TABLE IF NOT EXISTS rating (
    id INT PRIMARY KEY AUTO_INCREMENT,
    moodys_rating VARCHAR(125),
    sandp_rating VARCHAR(125),
    fitch_rating VARCHAR(125),
    order_number INT
);

-- Table Trade (avec relation vers User)
CREATE TABLE IF NOT EXISTS trade (
    trade_id INT PRIMARY KEY AUTO_INCREMENT,
    account VARCHAR(30) NOT NULL,
    type VARCHAR(30) NOT NULL,
    buy_quantity DOUBLE,
    sell_quantity DOUBLE,
    buy_price DOUBLE,
    sell_price DOUBLE,
    benchmark VARCHAR(125),
    trade_date TIMESTAMP,
    security VARCHAR(125),
    status VARCHAR(10),
    trader VARCHAR(125),
    book VARCHAR(125),
    creation_name VARCHAR(125),
    creation_date TIMESTAMP,
    revision_name VARCHAR(125),
    revision_date TIMESTAMP,
    deal_name VARCHAR(125),
    deal_type VARCHAR(125),
    source_list_id VARCHAR(125),
    side VARCHAR(125),
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE SET NULL
);

-- Table RuleName
CREATE TABLE IF NOT EXISTS rulename (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(125),
    description VARCHAR(125),
    json VARCHAR(125),
    template VARCHAR(512),
    sql_str VARCHAR(125),
    sql_part VARCHAR(125)
);

-- =============================================
-- INSERTION DES DONNÉES DE TEST
-- =============================================

-- 1. Utilisateurs
INSERT INTO user (username, password, fullname, role) VALUES
('admin', '$2a$10$NVM0n8ElaRgg7zWO1CxUdei7vWoPg91Lz2aYavh9.f9q0e4bRadue', 'Admin User', 'ADMIN'),
('user', '$2a$10$NVM0n8ElaRgg7zWO1CxUdei7vWoPg91Lz2aYavh9.f9q0e4bRadue', 'Regular User', 'USER'),
('trader', '$2a$10$NVM0n8ElaRgg7zWO1CxUdei7vWoPg91Lz2aYavh9.f9q0e4bRadue', 'Trader User', 'TRADER'),
('manager', '$2a$10$NVM0n8ElaRgg7zWO1CxUdei7vWoPg91Lz2aYavh9.f9q0e4bRadue', 'Manager User', 'MANAGER');

-- 2. Bids
INSERT INTO bidlist (account, type, bid_quantity, ask_quantity, bid, ask, benchmark, security, trader, creation_date, user_id) VALUES
('HSBC Investment', 'Corporate Bond', 1000.00, 950.00, 99.50, 100.50, 'EURIBOR 3M', 'FR0010754142', 'John Smith', '2025-01-10 09:30:00', 1),
('BNP Trading', 'Government Bond', 2000.00, 1950.00, 199.50, 200.50, 'EURIBOR 6M', 'DE0001102309', 'Maria Garcia', '2025-01-11 10:15:00', 2),
('Société Générale', 'Municipal Bond', 3000.00, 2950.00, 299.50, 300.50, 'SOFR 1M', 'XS2050623001', 'Robert Chen', '2025-01-12 11:45:00', 3),
('Credit Agricole', 'Convertible Bond', 4000.00, 3950.00, 399.50, 400.50, 'SONIA 3M', 'GB00B11JYZ78', 'Anna Kowalski', '2025-01-13 14:20:00', 1),
('Barclays Capital', 'High Yield Bond', 5000.00, 4950.00, 499.50, 500.50, 'ESTR O/N', 'US9128283R72', 'Thomas Müller', '2025-01-14 16:00:00', 2);

-- 3. Curve Points
INSERT INTO curvepoint (curve_id, term, value, as_of_date, creation_date) VALUES
(1, 0.25, 0.0150, '2025-01-01', '2025-01-01 08:00:00'),
(1, 0.50, 0.0165, '2025-01-01', '2025-01-01 08:00:00'),
(1, 1.00, 0.0180, '2025-01-01', '2025-01-01 08:00:00'),
(1, 2.00, 0.0195, '2025-01-01', '2025-01-01 08:00:00'),
(1, 5.00, 0.0220, '2025-01-01', '2025-01-01 08:00:00'),
(2, 0.25, 0.0160, '2025-01-01', '2025-01-01 09:00:00'),
(2, 1.00, 0.0190, '2025-01-01', '2025-01-01 09:00:00'),
(2, 5.00, 0.0240, '2025-01-01', '2025-01-01 09:00:00'),
(3, 1.00, 0.0200, '2025-01-01', '2025-01-01 10:00:00'),
(3, 10.00, 0.0300, '2025-01-01', '2025-01-01 10:00:00');

-- 4. Ratings
INSERT INTO rating (moodys_rating, sandp_rating, fitch_rating, order_number) VALUES
('Aaa', 'AAA', 'AAA', 1),
('Aa1', 'AA+', 'AA+', 2),
('Aa2', 'AA', 'AA', 3),
('Aa3', 'AA-', 'AA-', 4),
('A1', 'A+', 'A+', 5),
('A2', 'A', 'A', 6),
('A3', 'A-', 'A-', 7),
('Baa1', 'BBB+', 'BBB+', 8),
('Baa2', 'BBB', 'BBB', 9),
('Baa3', 'BBB-', 'BBB-', 10);

-- 5. Trades
INSERT INTO trade (account, type, buy_quantity, sell_quantity, buy_price, sell_price, benchmark, security, trader, trade_date, user_id) VALUES
('Goldman Sachs', 'FX Spot', 1000000.00, 0.00, 1.0850, 0.00, 'EUR/USD', 'EURUSD', 'David Lee', '2025-01-10 09:15:00', 1),
('Morgan Stanley', 'FX Forward', 0.00, 500000.00, 0.00, 1.0950, 'EUR/USD', 'EURUSD', 'Sarah Johnson', '2025-01-11 10:30:00', 2),
('JP Morgan', 'Equity', 10000.00, 0.00, 150.25, 0.00, 'S&P 500', 'AAPL', 'Michael Brown', '2025-01-12 11:45:00', 3),
('Citigroup', 'Commodity', 0.00, 5000.00, 0.00, 1850.75, 'Gold Spot', 'XAUUSD', 'Emma Wilson', '2025-01-13 14:20:00', 1),
('Deutsche Bank', 'Interest Rate Swap', 2500000.00, 0.00, 2.750, 0.00, 'EUR SWAP', 'IRS', 'James Taylor', '2025-01-14 16:00:00', 2);

-- 6. Rules
INSERT INTO rulename (name, description, json, template, sql_str, sql_part) VALUES
('Price Validation Rule', 'Validates price movements', '{"threshold": 0.05, "timeframe": "1h"}', 'price_template_v1', 'SELECT * FROM prices WHERE change > ?', 'change > ?'),
('Volume Alert Rule', 'Alerts on unusual volumes', '{"multiplier": 3.0, "period": "1d"}', 'volume_template_v2', 'SELECT * FROM trades WHERE volume > ?', 'volume > ?'),
('Risk Limit Rule', 'Checks risk exposure limits', '{"max_exposure": 1000000, "currency": "EUR"}', 'risk_template_v3', 'SELECT SUM(exposure) FROM positions', 'SUM(exposure)'),
('Compliance Rule', 'Ensures regulatory compliance', '{"regulation": "MIFID2", "country": "FR"}', 'compliance_template_v4', 'SELECT * FROM transactions WHERE date > ?', 'date > ?'),
('Reporting Rule', 'Generates daily reports', '{"format": "PDF", "frequency": "daily"}', 'report_template_v5', 'SELECT * FROM daily_summary', 'daily_summary');

-- =============================================
-- REQUÊTES DE TEST DES RELATIONS
-- =============================================

-- 1. Relations User ↔ BidList
SELECT 
    u.username,
    u.fullname,
    u.role,
    COUNT(b.bid_list_id) as total_bids,
    SUM(b.bid_quantity) as total_bid_quantity
FROM user u
LEFT JOIN bidlist b ON u.id = b.user_id
GROUP BY u.id
ORDER BY total_bids DESC;

-- 2. Relations User ↔ Trade
SELECT 
    u.username,
    u.fullname,
    u.role,
    COUNT(t.trade_id) as total_trades,
    SUM(t.buy_quantity) as total_buy_quantity,
    SUM(t.sell_quantity) as total_sell_quantity
FROM user u
LEFT JOIN trade t ON u.id = t.user_id
GROUP BY u.id
ORDER BY total_trades DESC;

-- 3. Vue complète des activités utilisateur
SELECT 
    u.username,
    u.fullname,
    u.role,
    (SELECT COUNT(*) FROM bidlist WHERE user_id = u.id) as bid_count,
    (SELECT COUNT(*) FROM trade WHERE user_id = u.id) as trade_count,
    (SELECT COUNT(*) FROM bidlist WHERE user_id = u.id) + 
    (SELECT COUNT(*) FROM trade WHERE user_id = u.id) as total_activities
FROM user u
ORDER BY total_activities DESC;

-- 4. Détails des bids avec informations utilisateur
SELECT 
    b.bid_list_id,
    b.account,
    b.type,
    b.bid_quantity,
    b.ask_quantity,
    b.bid,
    b.ask,
    b.trader,
    b.creation_date,
    u.username as created_by,
    u.fullname as creator_name,
    u.role as creator_role
FROM bidlist b
INNER JOIN user u ON b.user_id = u.id
ORDER BY b.creation_date DESC;

-- 5. Détails des trades avec informations utilisateur
SELECT 
    t.trade_id,
    t.account,
    t.type,
    t.buy_quantity,
    t.sell_quantity,
    t.buy_price,
    t.sell_price,
    t.trader,
    t.trade_date,
    u.username as executed_by,
    u.fullname as executor_name,
    u.role as executor_role
FROM trade t
INNER JOIN user u ON t.user_id = u.id
ORDER BY t.trade_date DESC;

-- =============================================
-- VÉRIFICATIONS FINALES
-- =============================================

-- Compter les enregistrements par table
SELECT 'User' as table_name, COUNT(*) as record_count FROM user
UNION ALL
SELECT 'BidList', COUNT(*) FROM bidlist
UNION ALL
SELECT 'CurvePoint', COUNT(*) FROM curvepoint
UNION ALL
SELECT 'Rating', COUNT(*) FROM rating
UNION ALL
SELECT 'Trade', COUNT(*) FROM trade
UNION ALL
SELECT 'RuleName', COUNT(*) FROM rulename;

-- Vérifier l'intégrité des relations
SELECT 
    'BidList sans User' as check_type,
    COUNT(*) as count
FROM bidlist 
WHERE user_id IS NOT NULL AND user_id NOT IN (SELECT id FROM user)
UNION ALL
SELECT 
    'Trade sans User',
    COUNT(*)
FROM trade 
WHERE user_id IS NOT NULL AND user_id NOT IN (SELECT id FROM user);