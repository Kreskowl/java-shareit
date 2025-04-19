package ru.practicum.shareit.controllers.bookingMockTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.dto.ShareConstants;
import ru.practicum.shareit.dto.booking.BookingCreateDto;
import ru.practicum.shareit.dto.booking.BookingDto;
import ru.practicum.shareit.dto.booking.Status;
import ru.practicum.shareit.dto.item.ItemBookDto;
import ru.practicum.shareit.dto.user.UserBookDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BookingController.class)
public class BookingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingClient client;

    @Autowired
    private ObjectMapper objectMapper;

    private BookingCreateDto createDto;
    private BookingDto responseDto;

    private static final long USER_ID = 1;
    private static final long ITEM_ID = 1;
    private static final long BOOKING_ID = 1;
    private static final LocalDateTime START = LocalDateTime.of(2030, 10, 10, 10, 30);
    private static final LocalDateTime END = START.plusMinutes(1);

    @BeforeEach
    void setUp() {
        createDto = new BookingCreateDto(ITEM_ID, START, END);

        responseDto = BookingDto.builder()
                .id(BOOKING_ID)
                .item(new ItemBookDto(ITEM_ID, "test"))
                .booker(new UserBookDto(USER_ID))
                .start(START)
                .end(END)
                .status(Status.APPROVED)
                .build();
    }

    @Test
    void shouldCreateBooking() throws Exception {

        when(client.createBooking(eq(1L), any())).thenReturn(ResponseEntity.ok(responseDto));

        mockMvc.perform(post("/bookings")
                        .header(ShareConstants.USER_ID_HEADER, USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(BOOKING_ID))
                .andExpect(jsonPath("$.item.id").value(ITEM_ID))
                .andExpect(jsonPath("$.item.name").value("test"))
                .andExpect(jsonPath("$.booker.id").value(USER_ID))
                .andExpect(jsonPath("$.status").value("APPROVED"))
                .andExpect(jsonPath("$.start").exists())
                .andExpect(jsonPath("$.end").exists());
    }

    @Test
    void shouldGetBookingById() throws Exception {
        when(client.findBookingById(any())).thenReturn(ResponseEntity.ok(responseDto));

        mockMvc.perform(get("/bookings/{bookingId}", BOOKING_ID)
                        .header(ShareConstants.USER_ID_HEADER, USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(BOOKING_ID))
                .andExpect(jsonPath("$.item.id").value(ITEM_ID))
                .andExpect(jsonPath("$.item.name").value("test"))
                .andExpect(jsonPath("$.booker.id").value(USER_ID))
                .andExpect(jsonPath("$.status").value("APPROVED"))
                .andExpect(jsonPath("$.start").exists())
                .andExpect(jsonPath("$.end").exists());
    }

    @Test
    void shouldGetAllOwnerBookings() throws Exception {
        when(client.getBookingsByOwner(eq(1L), any())).thenReturn(ResponseEntity.ok(List.of(responseDto)));

        mockMvc.perform(get("/bookings/owner")
                        .header(ShareConstants.USER_ID_HEADER, USER_ID)
                        .param("state", "FUTURE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(BOOKING_ID))
                .andExpect(jsonPath("$[0].status").value("APPROVED"));
    }

    @Test
    void shouldGetAllCurrentBookings() throws Exception {
        when(client.getBookings(eq(1L), any())).thenReturn(ResponseEntity.ok(List.of(responseDto)));

        mockMvc.perform(get("/bookings")
                        .header(ShareConstants.USER_ID_HEADER, USER_ID)
                        .param("state", "CURRENT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(BOOKING_ID))
                .andExpect(jsonPath("$[0].status").value("APPROVED"));
    }

    @Test
    void shouldUpdateBookingStatus() throws Exception {
        when(client.updateStatus(1L, 1L, true)).thenReturn(ResponseEntity.ok(responseDto));

        mockMvc.perform(patch("/bookings/{bookingId}?approved=true", BOOKING_ID)
                        .header(ShareConstants.USER_ID_HEADER, USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void shouldCallClientWhenUpdatingStatus() throws Exception {
        when(client.updateStatus(USER_ID, BOOKING_ID, true)).thenReturn(ResponseEntity.ok(responseDto));

        mockMvc.perform(patch("/bookings/{bookingId}?approved=true", BOOKING_ID)
                        .header(ShareConstants.USER_ID_HEADER, USER_ID))
                .andExpect(status().isOk());

        verify(client).updateStatus(USER_ID, BOOKING_ID, true);
    }

    @Test
    void shouldReturnBadRequestIfHeaderMissing() throws Exception {
        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestIfStartTimeMissing() throws Exception {
        BookingCreateDto wrong = new BookingCreateDto(ITEM_ID, null, END);
        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(wrong)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestIfEndTimeMissing() throws Exception {
        BookingCreateDto wrong = new BookingCreateDto(ITEM_ID, START, null);
        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(wrong)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnNotFoundIfItemDoesNotExist() throws Exception {
        long bookingId = 999L;

        when(client.findBookingById(bookingId))
                .thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found"));

        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header(ShareConstants.USER_ID_HEADER, USER_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Booking not found"));
    }


    private String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }
}
