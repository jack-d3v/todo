package jackd3v.domain;

import java.time.Instant;
import java.util.UUID;

public class Task {
    private UUID id;
    private String content;
    private Status status;
    private Instant closedAt;

    public Task(String content) {
        if (content == null)
            throw new NullPointerException();
        if (content.isBlank())
            throw new IllegalArgumentException();
        this.id = UUID.randomUUID();
        this.content = content;
        this.status = Status.ACTIVE;
    }

    public Status getStatus() {
        return this.status;
    }

    public Instant getClosedAt() {
        return this.closedAt;
    }

    private void setClosedAt(Instant now) {
        this.closedAt = now;
    }

    private void setStatus(Status status) {
        this.status = status;
    }

    public void close() {
        this.setStatus(Status.CLOSED);
        this.setClosedAt(Instant.now());
    }

    public void activate() {
        this.setStatus(Status.ACTIVE);
        this.setClosedAt(null);
    }

    public void park() {
        this.setStatus(Status.PARKED);
        this.setClosedAt(null);
    }

}
