import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class StockTradingSimulation {
    private static final String[] SAMPLE_TICKERS = {
        "AAPL", "GOOGL", "MSFT", "AMZN", "META", "TSLA", "NVDA", "JPM", "BAC", "WMT"
    };
    
    private static final Random random = new Random();
    private static final OrderBook orderBook = new OrderBook();

    public static void main(String[] args) {
        int numThreads = 4; // Simulate 4 concurrent traders
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        // Submit trading tasks
        for (int i = 0; i < numThreads; i++) {
            executorService.submit(StockTradingSimulation::simulateTrading);
        }

        // Run for 30 seconds
        try {
            Thread.sleep(30000);
            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void simulateTrading() {
        while (!Thread.currentThread().isInterrupted()) {
            Order order = generateRandomOrder();
            orderBook.addOrder(order);
            
            try {
                // Simulate some delay between orders
                Thread.sleep(random.nextInt(100));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private static Order generateRandomOrder() {
        Order.OrderType type = random.nextBoolean() ? Order.OrderType.BUY : Order.OrderType.SELL;
        String ticker = SAMPLE_TICKERS[random.nextInt(SAMPLE_TICKERS.length)];
        int quantity = random.nextInt(90) + 10; // Random quantity between 10 and 100
        double price = 50.0 + random.nextDouble() * 950.0; // Random price between 50.0 and 1000.0
        price = Math.round(price * 100.0) / 100.0; // Round to 2 decimal places

        return new Order(type, ticker, quantity, price);
    }
} 