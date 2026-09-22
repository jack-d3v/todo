package jackd3v.infrastructure;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.SessionFactory;

import jackd3v.domain.Task;
import jackd3v.domain.TaskRepository;

public class HibernateTaskRepository implements TaskRepository {
    private final SessionFactory sessionFactory;

    public HibernateTaskRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Task task) {
        sessionFactory.inTransaction(session -> session.persist(task));
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return Optional.ofNullable(sessionFactory
                .fromTransaction(session -> session.find(Task.class, id)));
    }

    @Override
    public List<Task> findAll() {
        return sessionFactory.fromTransaction(session -> session.createQuery("from Task", Task.class).getResultList());
    }
}
