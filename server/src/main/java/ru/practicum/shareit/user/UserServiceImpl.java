package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.dto.user.UserCreateDto;
import ru.practicum.shareit.dto.user.UserDto;
import ru.practicum.shareit.dto.user.UserUpdateDto;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper mapper;

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(mapper::userToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto createUser(UserCreateDto dto) {
        checkEmailDuplicate(dto.getEmail());
        User user = userRepository.save(mapper.createDtoToUser(dto));
        return mapper.userToDto(user);
    }

    @Override
    public UserDto findById(long id) {
        User user = ifUserExists(id);
        return mapper.userToDto(user);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public UserDto update(long id, UserUpdateDto dto) {
        User existing = ifUserExists(id);
        checkEmailDuplicate(dto.getEmail());
        mapper.updateDtoToUser(dto, existing);
        return mapper.userToDto(existing);
    }

    private User ifUserExists(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));
    }

    private void checkEmailDuplicate(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ConflictException("Email already exists: " + email);
        }
    }
}
