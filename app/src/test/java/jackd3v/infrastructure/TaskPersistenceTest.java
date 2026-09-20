package jackd3v.infrastructure;

import static org.junit.jupiter.api.Assertions.*;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jackd3v.domain.Status;
import jackd3v.domain.Task;

public class TaskPersistenceTest {

    private static SessionFactory sessionFactory;

    @BeforeAll
    static void before() {
        sessionFactory = new org.hibernate.cfg.Configuration().addAnnotatedClass(Task.class)
                .buildSessionFactory();
    }

    @AfterAll
    static void after() {
        sessionFactory.close();
    }

    @Test
    void savedTaskPersists() {
        Task task = new Task("foo bar");

        sessionFactory.inTransaction(session -> session.persist(task));

        Task loaded = sessionFactory.fromTransaction(session -> session.find(Task.class, task.getID()));

        assertEquals("foo bar", loaded.getContent());
        assertEquals(Status.ACTIVE, loaded.getStatus());
        assertNull(loaded.getClosedAt());
    }
}
