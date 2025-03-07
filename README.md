# Real-Time Stock Trading Engine

A high-performance, lock-free stock trading engine implementation in Java that matches buy and sell orders in real-time.

## Overview

This project implements a real-time stock trading engine that efficiently matches buy and sell orders for multiple stock tickers. The system is designed to handle concurrent operations from multiple traders while maintaining data consistency without using traditional locks.

## Key Features

- Support for up to 1,024 different stock tickers
- Lock-free concurrent operations using `AtomicReferenceArray`
- O(n) time complexity for order matching
- Real-time order matching with price-based pairing
- Thread-safe implementation for multi-broker scenarios
- No usage of Maps/Dictionaries for pure algorithmic implementation

## Components

### 1. Order Class (`Order.java`)
- Represents individual buy/sell orders
- Contains order type, ticker symbol, quantity, price, and status
- Implements thread-safe status updates

### 2. OrderBook Class (`OrderBook.java`)
- Manages the entire trading system
- Handles multiple ticker books using `AtomicReferenceArray`
- Implements lock-free order addition and matching
- Contains inner class `TickerOrderBook` for per-ticker order management

### 3. Trading Simulation (`StockTradingSimulation.java`)
- Provides a simulation environment for testing
- Generates random orders across multiple threads
- Simulates concurrent trading scenarios

## Technical Implementation

### Order Matching Algorithm
- Time Complexity: O(n) where n is the number of orders
- Space Complexity: O(m) where m is the maximum number of orders per ticker
- Uses separate buy and sell order arrays for each ticker
- Matches orders based on price compatibility

### Concurrency Handling
- Utilizes `AtomicReferenceArray` for thread-safe operations
- Implements volatile variables for visibility across threads
- Avoids traditional locks for better performance
- Supports multiple concurrent traders

## Usage

To run the simulation:

```bash
javac src/*.java
java src/StockTradingSimulation
```

The simulation will:
1. Create 4 concurrent trading threads
2. Generate random buy/sell orders
3. Process order matching in real-time
4. Run for 30 seconds by default

## Performance Considerations

- Maximum supported tickers: 1,024
- Maximum orders per ticker: 10,000
- Thread-safe operations without blocking
- Efficient memory usage with array-based implementation

## Sample Output

The system will output matched orders in the following format:
```
Matched: TICKER Buy@PRICE with Sell@PRICE
```

## Limitations

- Fixed array sizes for tickers and orders
- Simple price-based matching (no complex order types)
- FIFO order processing within price levels
- No persistence layer

## Future Improvements

1. Add support for order cancellation
2. Implement more sophisticated matching algorithms
3. Add market data streaming
4. Include order history tracking
5. Support for different order types (market, limit, etc.) 