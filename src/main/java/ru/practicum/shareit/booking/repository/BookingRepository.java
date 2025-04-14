package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByRenterIdOrderByStartTimeDesc(Long userId);

    List<Booking> findByRenterIdAndStatusOrderByStartTimeDesc(Long userId, Status status);

    List<Booking> findByRenterIdAndStartTimeBeforeAndEndTimeAfterOrderByStartTimeDesc(
            Long userId, LocalDateTime now1, LocalDateTime now2);

    List<Booking> findByRenterIdAndEndTimeBeforeOrderByStartTimeDesc(Long userId, LocalDateTime now);

    List<Booking> findByRenterIdAndStartTimeAfterOrderByStartTimeDesc(Long userId, LocalDateTime now);

    List<Booking> findAllByItemIdInOrderByStartTimeDesc(List<Long> itemIds);


    List<Booking> findAllByItemIdInAndStatusOrderByStartTimeDesc(List<Long> itemIds, Status status);

    List<Booking> findAllByItemIdInAndStartTimeBeforeAndEndTimeAfterOrderByStartTimeDesc(
            List<Long> itemIds, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByItemIdInAndEndTimeBeforeOrderByStartTimeDesc(
            List<Long> itemIds, LocalDateTime end);

    List<Booking> findAllByItemIdInAndStartTimeAfterOrderByStartTimeDesc(
            List<Long> itemIds, LocalDateTime start);

    List<Booking> findByItemIdAndRenterIdAndEndTimeBefore(Long itemId, Long renterId, LocalDateTime endTime);

    @Query("SELECT b FROM Booking b JOIN Item i ON b.itemId = i.id WHERE i.ownerId = :ownerId ORDER BY b.startTime DESC")
    List<Booking> findAllByOwner(@Param("ownerId") Long ownerId);

    @Query("SELECT b FROM Booking b JOIN Item i ON b.itemId = i.id WHERE i.ownerId = :ownerId AND b.status = :status ORDER BY b.startTime DESC")
    List<Booking> findAllByOwnerAndStatus(@Param("ownerId") Long ownerId, @Param("status") Status status);

    @Query("SELECT b FROM Booking b JOIN Item i ON b.itemId = i.id WHERE i.ownerId = :ownerId AND b.startTime <= CURRENT_TIMESTAMP AND b.endTime >= CURRENT_TIMESTAMP ORDER BY b.startTime DESC")
    List<Booking> findAllCurrentByOwner(@Param("ownerId") Long ownerId);

    @Query("SELECT b FROM Booking b JOIN Item i ON b.itemId = i.id WHERE i.ownerId = :ownerId AND b.endTime < CURRENT_TIMESTAMP ORDER BY b.startTime DESC")
    List<Booking> findAllPastByOwner(@Param("ownerId") Long ownerId);

    @Query("SELECT b FROM Booking b JOIN Item i ON b.itemId = i.id WHERE i.ownerId = :ownerId AND b.startTime > CURRENT_TIMESTAMP ORDER BY b.startTime DESC")
    List<Booking> findAllFutureByOwner(@Param("ownerId") Long ownerId);

    @Query("SELECT b FROM Booking b WHERE b.itemId IN :itemIds AND b.endTime < CURRENT_TIMESTAMP AND b.status = 'APPROVED' " +
            "ORDER BY b.endTime DESC")
    List<Booking> findLastBookingsForItems(@Param("itemIds") List<Long> itemIds);

    @Query("SELECT b FROM Booking b WHERE b.itemId IN :itemIds AND b.startTime > CURRENT_TIMESTAMP AND b.status = 'APPROVED' " +
            "ORDER BY b.startTime ASC")
    List<Booking> findNextBookingsForItems(@Param("itemIds") List<Long> itemIds);

}
