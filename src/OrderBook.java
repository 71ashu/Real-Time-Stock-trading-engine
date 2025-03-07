import java.util.concurrent.atomic.AtomicReferenceArray;

public class OrderBook {
    private static final int MAX_TICKERS = 1024;
    private static final int MAX_ORDERS_PER_TICKER = 10000;

    // Using AtomicReferenceArray for lock-free operations
    private final AtomicReferenceArray<TickerOrderBook> orderBooks;

    public OrderBook() {
        orderBooks = new AtomicReferenceArray<>(MAX_TICKERS);
        for (int i = 0; i < MAX_TICKERS; i++) {
            orderBooks.set(i, new TickerOrderBook());
        }
    }

    public void addOrder(Order order) {
        int tickerIndex = Math.abs(order.getTickerSymbol().hashCode() % MAX_TICKERS);
        TickerOrderBook tickerBook = orderBooks.get(tickerIndex);
        tickerBook.addOrder(order);
        matchOrders(tickerIndex);
    }

    private void matchOrders(int tickerIndex) {
        TickerOrderBook tickerBook = orderBooks.get(tickerIndex);
        tickerBook.matchOrders();
    }

    // Inner class to handle orders for a specific ticker
    private static class TickerOrderBook {
        private final AtomicReferenceArray<Order> buyOrders;
        private final AtomicReferenceArray<Order> sellOrders;
        private volatile int buyIndex = 0;
        private volatile int sellIndex = 0;

        public TickerOrderBook() {
            buyOrders = new AtomicReferenceArray<>(MAX_ORDERS_PER_TICKER);
            sellOrders = new AtomicReferenceArray<>(MAX_ORDERS_PER_TICKER);
        }

        public void addOrder(Order order) {
            AtomicReferenceArray<Order> orders = (order.getType() == Order.OrderType.BUY) ? buyOrders : sellOrders;
            int index = (order.getType() == Order.OrderType.BUY) ? buyIndex++ : sellIndex++;
            
            if (index < MAX_ORDERS_PER_TICKER) {
                orders.set(index % MAX_ORDERS_PER_TICKER, order);
            } else {
                // Reset index if we reach the maximum
                if (order.getType() == Order.OrderType.BUY) {
                    buyIndex = 0;
                } else {
                    sellIndex = 0;
                }
                orders.set(0, order);
            }
        }

        public void matchOrders() {
            // O(n) matching algorithm
            for (int i = 0; i < buyIndex; i++) {
                Order buyOrder = buyOrders.get(i);
                if (buyOrder != null && buyOrder.isActive()) {
                    for (int j = 0; j < sellIndex; j++) {
                        Order sellOrder = sellOrders.get(j);
                        if (sellOrder != null && sellOrder.isActive() && 
                            buyOrder.getPrice() >= sellOrder.getPrice() &&
                            buyOrder.getTickerSymbol().equals(sellOrder.getTickerSymbol())) {
                            
                            // Match found - mark both orders as inactive
                            buyOrder.setInactive();
                            sellOrder.setInactive();
                            
                            System.out.println("Matched: " + buyOrder.getTickerSymbol() + 
                                             " Buy@" + buyOrder.getPrice() + 
                                             " with Sell@" + sellOrder.getPrice());
                            break;
                        }
                    }
                }
            }
        }
    }
} 