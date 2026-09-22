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

    @Test
    void rewordNullThrows() {
        Task task = new Task("foo");
        assertThrows(NullPointerException.class, () -> task.reword(null));
    }

    @Test
    void rewordEmptyThrows() {
        Task task = new Task("foo");
        assertThrows(IllegalArgumentException.class, () -> task.reword(""));
    }

    @Test
    void rewordWhitespaceThrows() {
        Task task = new Task("foo");
        assertThrows(IllegalArgumentException.class, () -> task.reword("   "));
    }

    @Test
    void rewordChangesContent() {
        Task task = new Task("foo");
        task.reword("bar");
        assertEquals("bar", task.getContent());
    }
}
