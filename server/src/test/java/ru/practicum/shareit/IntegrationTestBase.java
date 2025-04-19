package ru.practicum.shareit;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.Getter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.dto.booking.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Getter
public abstract class IntegrationTestBase {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected ItemRepository itemRepository;

    @Autowired
    protected BookingRepository bookingRepository;

    @Autowired
    protected CommentRepository commentRepository;

    @Autowired
    protected ItemRequestRepository requestRepository;

    @PersistenceContext
    protected EntityManager entityManager;

    private User user;

    private User secondUser;

    private Item item;

    private Booking booking;

    private ItemRequest request;

    @BeforeEach
    void globalSetUp() {
        user = userRepository.save(User.builder().name("Test").email("test@example.com").build());
        secondUser = userRepository.save(User.builder().name("integrior").email("waffle@yandex.ru").build());

        item = itemRepository.save(Item.builder()
                .name("Hammer")
                .description("Just a red hammer")
                .available(true)
                .ownerId(user.getId())
                .build());

        booking = bookingRepository.save(Booking.builder()
                .itemId(item.getId())
                .renterId(secondUser.getId())
                .startTime(LocalDateTime.now().minusDays(5))
                .endTime(LocalDateTime.now().minusDays(2))
                .status(Status.APPROVED)
                .build());

        request = requestRepository.save(ItemRequest.builder()
                .description("looking for a gasmask")
                .requesterId(user.getId())
                .created(LocalDateTime.now())
                .build());
    }

    @AfterEach
    void cleanUp() {
        entityManager.createNativeQuery("DELETE FROM comments").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM bookings").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM items").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM requests").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM users").executeUpdate();

        entityManager.createNativeQuery("ALTER TABLE users ALTER COLUMN id RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE items ALTER COLUMN id RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE bookings ALTER COLUMN id RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE comments ALTER COLUMN id RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE requests ALTER COLUMN id RESTART WITH 1").executeUpdate();
    }

}
