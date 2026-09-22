package jackd3v.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository {
    void save(Task task);

    Optional<Task> findById(UUID id);

    List<Task> findAll();
}
