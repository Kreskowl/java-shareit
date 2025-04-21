package ru.practicum.shareit.controllers.itemRequestMockTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.dto.ShareConstants;
import ru.practicum.shareit.dto.request.ItemRequestCreateDto;
import ru.practicum.shareit.dto.request.ItemRequestDto;
import ru.practicum.shareit.dto.user.UserBookDto;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.ItemRequestService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@ContextConfiguration(classes = ShareItServer.class)
public class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRequestService service;

    @Autowired
    private ObjectMapper objectMapper;

    private static final long USER_ID = 1;
    private static final long REQUEST_ID = 1;
    private ItemRequestCreateDto createDto;
    private ItemRequestDto responseDto;

    @BeforeEach
    void setUp() {
        createDto = new ItemRequestCreateDto("create");

        responseDto = ItemRequestDto.builder()
                .id(REQUEST_ID)
                .description("create")
                .requesterId(USER_ID)
                .created(LocalDateTime.now())
                .owner(new UserBookDto(2L))
                .items(List.of())
                .build();
    }

    @Test
    void shouldCreateRequest() throws Exception {
        when(service.createItemRequest(any(), eq(USER_ID)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/requests")
                        .header(ShareConstants.USER_ID_HEADER, USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(REQUEST_ID))
                .andExpect(jsonPath("$.description").value("create"))
                .andExpect(jsonPath("$.requesterId").value(USER_ID))
                .andExpect(jsonPath("$.created").exists())
                .andExpect(jsonPath("$.owner").exists())
                .andExpect(jsonPath("$.items").exists());

    }

    @Test
    void shouldGetAllUserRequests() throws Exception {
        when(service.findAllUserRequests(USER_ID))
                .thenReturn(List.of(responseDto));

        mockMvc.perform(get("/requests")
                        .header(ShareConstants.USER_ID_HEADER, USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(REQUEST_ID));
    }

    @Test
    void shouldGetAllOtherUsersRequests() throws Exception {
        when(service.findAllOtherRequests(USER_ID))
                .thenReturn(List.of(responseDto));

        mockMvc.perform(get("/requests/all")
                        .header(ShareConstants.USER_ID_HEADER, USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(REQUEST_ID));
    }

    @Test
    void shouldFindRequestById() throws Exception {
        when(service.findById(REQUEST_ID))
                .thenReturn(responseDto);

        mockMvc.perform(get("/requests/{requestId}", REQUEST_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(REQUEST_ID));
    }

    @Test
    void shouldReturnBadRequestIfHeaderMissing() throws Exception {
        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestIfDescriptionEmpty() throws Exception {
        ItemRequestCreateDto invalidDto = new ItemRequestCreateDto("");

        mockMvc.perform(post("/requests")
                        .header(ShareConstants.USER_ID_HEADER, USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    private String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }
}
