package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@RequiredArgsConstructor
public class InMemUserRepository implements UserRepository {
    private final Map<Long, User> storage;
    private final Set<String> emailSet;
    private final UserMapper mapper;
    private final AtomicLong generator = new AtomicLong(0);

    @Override
    public User save(User user) {
        String email = user.getEmail();

        if (existsByEmail(email)) {
            throw new ConflictException("email already used");
        }

        if (user.getId() == null) {
            user.setId(generator.incrementAndGet());
        }
        storage.put(user.getId(), user);
        emailSet.add(email);
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
    public User update(long id, UserUpdateDto dto) {
        User existing = ifExists(id);
        String originalEmail = existing.getEmail();

        mapper.createUpdateDtoToUser(dto, existing);

        if (dto.getEmail() != null) {
            String newEmail = dto.getEmail();
            if (!originalEmail.equalsIgnoreCase(newEmail)) {
                if (existsByEmail(newEmail)) {
                    throw new ConflictException("Email already used");
                }
                emailSet.remove(existing.getEmail());
                emailSet.add(newEmail);
                existing.setEmail(newEmail);
            }
        }

        storage.put(existing.getId(), existing);
        return existing;
    }

    private User ifExists(long id) {
        return Optional.ofNullable(storage.get(id))
                .orElseThrow(() -> new NotFoundException("user with id " + id + " not found"));
    }

    private boolean existsByEmail(String email) {
        if (email == null) {
            return false;
        }
        return emailSet.stream().anyMatch(e -> e.equalsIgnoreCase(email));
    }
}
