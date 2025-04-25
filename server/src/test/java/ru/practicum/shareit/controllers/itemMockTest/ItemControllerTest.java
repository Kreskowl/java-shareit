package ru.practicum.shareit.controllers.itemMockTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.dto.ShareConstants;
import ru.practicum.shareit.dto.item.CommentCreateDto;
import ru.practicum.shareit.dto.item.CommentDto;
import ru.practicum.shareit.dto.item.ItemCreateDto;
import ru.practicum.shareit.dto.item.ItemResponseDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
public class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService service;

    @Autowired
    private ObjectMapper objectMapper;

    private ItemCreateDto createDto;
    private ItemResponseDto responseDto;
    private static final long USER_ID = 1;
    private static final long ITEM_ID = 1;
    private static final long REQUEST_ID = 1;
    private static final long COMMENT_ID = 1;

    @BeforeEach
    void setUp() {
        createDto = new ItemCreateDto("Test", "Just testing", USER_ID, true, REQUEST_ID);

        responseDto = ItemResponseDto.builder()
                .id(ITEM_ID)
                .name("Test")
                .description("Just testing")
                .ownerId(USER_ID)
                .available(true)
                .requestId(null)
                .build();
    }

    @Test
    void shouldCreateItem() throws Exception {

        when(service.createItem(any())).thenReturn(responseDto);

        mockMvc.perform(post("/items")
                        .header(ShareConstants.USER_ID_HEADER, USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ITEM_ID))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.description").value("Just testing"))
                .andExpect(jsonPath("$.ownerId").value(USER_ID))
                .andExpect(jsonPath("$.available").value("true"))
                .andExpect(jsonPath("$.requestId").doesNotExist());
    }

    @Test
    void shouldCreateItemForRequest() throws Exception {
        responseDto.setRequestId(REQUEST_ID);
        when(service.createItem(any())).thenReturn(responseDto);

        mockMvc.perform(post("/items")
                        .header(ShareConstants.USER_ID_HEADER, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ITEM_ID))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.description").value("Just testing"))
                .andExpect(jsonPath("$.ownerId").value(USER_ID))
                .andExpect(jsonPath("$.available").value("true"))
                .andExpect(jsonPath("$.requestId").value(REQUEST_ID));
    }

    @Test
    void shouldGetItemById() throws Exception {
        when(service.findById(eq(USER_ID), eq(ITEM_ID))).thenReturn(responseDto);

        mockMvc.perform(get("/items/{itemId}", ITEM_ID)
                        .header(ShareConstants.USER_ID_HEADER, USER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ITEM_ID))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.description").value("Just testing"))
                .andExpect(jsonPath("$.ownerId").value(USER_ID))
                .andExpect(jsonPath("$.available").value("true"))
                .andExpect(jsonPath("$.requestId").doesNotExist());
    }

    @Test
    void shouldUpdateItemById() throws Exception {
        ItemResponseDto updatedResponse = ItemResponseDto.builder()
                .id(ITEM_ID)
                .name("change")
                .description("for test")
                .ownerId(USER_ID)
                .available(false)
                .requestId(null)
                .build();


        when(service.update(eq(USER_ID), eq(ITEM_ID), any())).thenReturn(updatedResponse);

        mockMvc.perform(patch("/items/{itemId}", ITEM_ID)
                        .header(ShareConstants.USER_ID_HEADER, USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ITEM_ID))
                .andExpect(jsonPath("$.name").value("change"))
                .andExpect(jsonPath("$.description").value("for test"))
                .andExpect(jsonPath("$.ownerId").value(USER_ID))
                .andExpect(jsonPath("$.available").value("false"))
                .andExpect(jsonPath("$.requestId").doesNotExist());
    }

    @Test
    void shouldGetAllUserItems() throws Exception {
        when(service.findAllByOwnerId(1L)).thenReturn(List.of());

        mockMvc.perform(get("/items")
                        .header(ShareConstants.USER_ID_HEADER, USER_ID))
                .andExpect(status().isOk());
    }

    @Test
    void shouldSearchItems() throws Exception {
        when(service.search("hammer")).thenReturn(List.of());

        mockMvc.perform(get("/items/search")
                        .param("text", "hammer"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAddComment() throws Exception {
        CommentCreateDto dto = new CommentCreateDto("Nice one!");
        CommentDto response = CommentDto.builder()
                .id(1L)
                .text("Nice one!")
                .authorName("User")
                .created(LocalDateTime.now())
                .build();

        when(service.addComment(eq(1L), eq(1L), any()))
                .thenReturn(response);

        mockMvc.perform(post("/items/{itemId}/comment", COMMENT_ID)
                        .header(ShareConstants.USER_ID_HEADER, USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(COMMENT_ID))
                .andExpect(jsonPath("$.text").value("Nice one!"));
    }

    @Test
    void shouldReturnBadRequestIfHeaderMissing() throws Exception {
        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestIfNameMissing() throws Exception {
        ItemCreateDto wrong = new ItemCreateDto("", "bug", USER_ID, true, null);

        mockMvc.perform(post("/items")
                        .header(ShareConstants.USER_ID_HEADER, COMMENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(wrong)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestIfDescriptionMissing() throws Exception {
        ItemCreateDto wrong = new ItemCreateDto("one more", "", USER_ID, true, null);

        mockMvc.perform(post("/items")
                        .header(ShareConstants.USER_ID_HEADER, USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(wrong)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestIfAvailableMissing() throws Exception {
        ItemCreateDto wrong = new ItemCreateDto("attempt 2", "bug 2", USER_ID, null, null);

        mockMvc.perform(post("/items")
                        .header(ShareConstants.USER_ID_HEADER, USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(wrong)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestIfCommentWithoutText() throws Exception {
        CommentCreateDto dto = new CommentCreateDto("");

        mockMvc.perform(post("/items/{itemId}/comment", ITEM_ID)
                        .header(ShareConstants.USER_ID_HEADER, USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnNotFoundIfItemDoesNotExist() throws Exception {
        long itemId = 999L;

        when(service.findById(itemId, USER_ID))
                .thenThrow(new NotFoundException("Item not found"));

        mockMvc.perform(get("/items/{itemId}", itemId)
                        .header(ShareConstants.USER_ID_HEADER, USER_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Item not found")));
    }


    private String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }
}
