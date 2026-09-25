# todo

A command-line to-do list in Java, backed by Hibernate and H2.

The point of this project is the persistence layer and the domain model, not the
interface. The front end is a plain REPL: it prints the list, reads a command,
and redraws.

## Running

```
./gradlew run
```

```
./gradlew test
```

Requires JDK 25. Gradle's toolchain will resolve one if it isn't the default.

## Commands

| Command | Effect |
|---|---|
| `add <text>` | Create a task |
| `edit <n> <text>` | Replace the text of task `n` |
| `close <n>` | Mark task `n` closed |
| `park <n>` | Mark task `n` parked |
| `activate <n>` | Return task `n` to active |
| `filter active\|parked\|closed\|all` | Limit the list to one status |
| `quit` | Exit |

Task numbers refer to the rows currently on screen, so they follow the filter.

Each status renders as a symbol: active is blank, parked is `-`, closed is `x`.

```
to-do list  (filter: all)

1. [ ] write the readme
2. [-] wire up arrow keys
3. [x] pick a library

>
```

## Storage

H2 runs in memory with `MODE=PostgreSQL`, so Hibernate talks to it as it would
to Postgres without needing a server. **Tasks do not survive exit.** That is
deliberate — the database is here to exercise the mapping and the repository.

Switching to a file-backed database is a one-line change to the JDBC URL in
`app/src/main/resources/hibernate.properties`.

## Layout

```
jackd3v.domain          Task, Status, TaskRepository
jackd3v.infrastructure  HibernateTaskRepository
jackd3v.App             wiring and the REPL
```

`domain` has no Hibernate imports except the JPA annotations on the entity, and
knows nothing about the terminal. `App` is the only place that names
`HibernateTaskRepository`; everything else depends on the `TaskRepository`
interface.

`save` uses `persist` and `update` uses `merge`. They are separate methods
rather than one upsert because the id is assigned in the constructor, so a
single `merge`-everything `save` would issue a pointless `SELECT` before every
insert.
