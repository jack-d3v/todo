package jackd3v.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;

import org.junit.jupiter.api.Test;

class TaskTest {

    @Test
    void nullContentThrows() {
        assertThrows(NullPointerException.class, () -> new Task(null));
    }

    @Test
    void emptyContentThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Task(""));
    }

    @Test
    void whitespaceContentThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Task("    "));
    }

    @Test
    void closeSetsStatusAndClosedAt() {
        Task task = new Task("foo");
        task.close();
        assertNotNull(task.getClosedAt());
        assertEquals(Status.CLOSED, task.getStatus());
    }

    @Test
    void activateAfterCloseClearsClosedAt() {
        Task task = new Task("foo");
        task.close();
        task.activate();
        assertNull(task.getClosedAt());
        assertEquals(Status.ACTIVE, task.getStatus());
    }

    @Test
    void parkAfterCloseClearsClosedAt() {
        Task task = new Task("Foo");
        task.close();
        task.park();
        assertNull(task.getClosedAt());
        assertEquals(Status.PARKED, task.getStatus());
    }

}
