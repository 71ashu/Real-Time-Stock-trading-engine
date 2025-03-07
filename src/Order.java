public class Order {
    public enum OrderType {
        BUY,
        SELL
    }

    private final OrderType type;
    private final String tickerSymbol;
    private final int quantity;
    private final double price;
    private volatile boolean isActive;
    private final long timestamp;

    public Order(OrderType type, String tickerSymbol, int quantity, double price) {
        this.type = type;
        this.tickerSymbol = tickerSymbol;
        this.quantity = quantity;
        this.price = price;
        this.isActive = true;
        this.timestamp = System.nanoTime();
    }

    // Getters
    public OrderType getType() { return type; }
    public String getTickerSymbol() { return tickerSymbol; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public boolean isActive() { return isActive; }
    public long getTimestamp() { return timestamp; }

    // Setters
    public void setInactive() {
        this.isActive = false;
    }

    @Override
    public String toString() {
        return String.format("Order{type=%s, ticker=%s, quantity=%d, price=%.2f}",
                type, tickerSymbol, quantity, price);
    }
}