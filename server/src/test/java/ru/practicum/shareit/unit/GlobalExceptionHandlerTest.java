package ru.practicum.shareit.unit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.exception.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import({GlobalExceptionHandler.class, GlobalExceptionHandlerTest.SomeDummyController.class})
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @RestController
    static class SomeDummyController {

        @GetMapping("/notfound")
        public void notFound() {
            throw new NotFoundException("Resource not found");
        }

        @GetMapping("/validation")
        public void validation() {
            throw new ValidationException("Invalid input");
        }

        @GetMapping("/conflict")
        public void conflict() {
            throw new ConflictException("Already exists");
        }

        @GetMapping("/forbidden")
        public void forbidden() {
            throw new ForbiddenException("You are not the owner");
        }

        @GetMapping("/throwable")
        public void unexpected() {
            throw new RuntimeException("Boom!");
        }
    }

    @Test
    void shouldHandleNotFoundException() throws Exception {
        mockMvc.perform(get("/notfound"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Resource not found"));
    }

    @Test
    void shouldHandleValidationException() throws Exception {
        mockMvc.perform(get("/validation"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Invalid input"));
    }

    @Test
    void shouldHandleConflictException() throws Exception {
        mockMvc.perform(get("/conflict"))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.message").value("Already exists"));
    }

    @Test
    void shouldHandleForbiddenException() throws Exception {
        mockMvc.perform(get("/forbidden"))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.message").value("You are not the owner"));
    }

    @Test
    void shouldHandleThrowable() throws Exception {
        mockMvc.perform(get("/throwable"))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.message").value("Boom!"));
    }
}
