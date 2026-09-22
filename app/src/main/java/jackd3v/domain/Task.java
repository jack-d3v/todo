package jackd3v.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

@Entity
public class Task {
    @Id
    private UUID id;
    private String content;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Instant closedAt;

    public Task(String content) {
        requireValidContent(content);
        this.id = UUID.randomUUID();
        this.content = content;
        this.status = Status.ACTIVE;
    }

    protected Task() {
    }

    public Status getStatus() {
        return this.status;
    }

    public Instant getClosedAt() {
        return this.closedAt;
    }

    public UUID getID() {
        return this.id;
    }

    public String getContent() {
        return this.content;
    }

    private void setClosedAt(Instant now) {
        this.closedAt = now;
    }

    private void setStatus(Status status) {
        this.status = status;
    }

    private static void requireValidContent(String content) {
        Objects.requireNonNull(content);
        if (content.isBlank())
            throw new IllegalArgumentException();
    }

    public void reword(String content) {
        requireValidContent(content);
        this.content = content;
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
