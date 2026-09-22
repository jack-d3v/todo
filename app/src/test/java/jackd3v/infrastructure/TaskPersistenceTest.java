package jackd3v.infrastructure;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jackd3v.domain.Status;
import jackd3v.domain.Task;
import jackd3v.domain.TaskRepository;

public class TaskPersistenceTest {

    private static SessionFactory sessionFactory;
    private static TaskRepository repository;

    @BeforeAll
    static void before() {
        sessionFactory = new org.hibernate.cfg.Configuration().addAnnotatedClass(Task.class)
                .buildSessionFactory();
        repository = new HibernateTaskRepository(sessionFactory);
    }

    @AfterAll
    static void after() {
        sessionFactory.close();
    }

    @Test
    void savedTaskPersists() {
        Task task = new Task("foo bar");

        repository.save(task);

        Task loaded = repository.findById(task.getID()).orElseThrow();

        assertEquals("foo bar", loaded.getContent());
        assertEquals(Status.ACTIVE, loaded.getStatus());
        assertNull(loaded.getClosedAt());
    }

    @Test
    void findByIdMissingReturnsEmpty() {
        Optional<Task> result = repository.findById(UUID.randomUUID());
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllReturnsSavedTasks() {
        Task task1 = new Task("foo");
        Task task2 = new Task("bar");
        repository.save(task1);
        repository.save(task2);

        List<UUID> ids = repository.findAll().stream().map(Task::getID).toList();

        assertTrue(ids.contains(task1.getID()));
        assertTrue(ids.contains(task2.getID()));
    }
}
