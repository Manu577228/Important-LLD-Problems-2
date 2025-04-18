//6. Problem Statement: Rate Limiter (Token Bucket)
//
//We need to limit the number of requests that can be processed per client (or user or system) in a given time window.
//
//This is often used to protect services from abuse or accidental overload.
//
//* Allow up to a certain number of requests per second (rate).
//
//* Additional requests beyond the allowed rate are rejected.
//
//* The implementation must be thread-safe.
//
//* The system should gradually refill tokens over time (i.e., refill mechanism).
//
//* Each client/requestor can have their own bucket (optionally, but we'll start with a single bucket).

public class RateLimiter {
    private final int capacity;
    private double tokens;
    private final double refillRatePerSecond;
    private long lastRefillTimestamp;

    public RateLimiter(int capacity, double refillRatePerSecond) {
        this.capacity = capacity;
        this.tokens = capacity;
        this.refillRatePerSecond = refillRatePerSecond;
        this.lastRefillTimestamp = System.nanoTime();
    }

    public synchronized boolean allowRequest() {
        refill();

        if (tokens >= 1.0) {
            tokens -= 1.0;
            return true;
        }
        return false;
    }

    private void refill() {
        long now = System.nanoTime();
        double secondSinceLastRefill = (now - lastRefillTimestamp) / 1_000_000_000.0;

        double tokensToAdd = secondSinceLastRefill * refillRatePerSecond;
        tokens = Math.min(capacity, tokens + tokensToAdd);

        lastRefillTimestamp = now;
    }

    public static void main(String[] args) throws InterruptedException {
        RateLimiter r1 = new RateLimiter(5, 2);

        for (int i = 0; i < 10; i++) {
            if (r1.allowRequest()) {
                System.out.println("Request" + i + "allowed");
            } else {
                System.out.println("Request" + i + "rejected.");
            }
            Thread.sleep(300);
        }
    }
}