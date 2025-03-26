package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemUserRepository implements UserRepository {
    private final Map<Long, User> storage = new HashMap<>();
    private final AtomicLong generator = new AtomicLong(0);

    @Override
    public User save(User user) {
        if (existsByEmail(user.getEmail())) {
            throw new ConflictException("email already used");
        }
        ;
        if (user.getId() == null) {
            user.setId(generator.incrementAndGet());
        }
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public User findById(Long id) {
        return ifExists(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void deleteById(Long id) {
        ifExists(id);
        storage.remove(id);
    }

    @Override
    public User update(long id, User updated) {
        User existing = ifExists(id);
        existing.setName(updated.getName());
        if (existsByEmail(updated.getEmail())) {
            throw new ConflictException("email already used");
        }
        existing.setEmail(updated.getEmail());
        storage.put(existing.getId(), existing);
        return existing;
    }

    private User ifExists(long id) {
        return Optional.ofNullable(storage.get(id))
                .orElseThrow(() -> new NotFoundException("user with id " + id + " not found"));
    }

    public boolean existsByEmail(String email) {
        if (email == null) {
            return false;
        }
        return storage.values().stream()
                .filter(u -> u.getEmail() != null)
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }
}
