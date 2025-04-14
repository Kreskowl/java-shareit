package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepositoryImpl;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepositoryImpl userRepository;
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
    public UserDto createUser(UserDto dto) {
        User user = userRepository.save(mapper.createDtoToUser(dto));
        return mapper.userToDto(user);
    }

    @Override
    public UserDto findById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));
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
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id " + id + " not found"));
        mapper.updateDtoToUser(dto, existing);
        return mapper.userToDto(existing);
    }
}
