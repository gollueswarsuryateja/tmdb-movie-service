package com.softwarebhayya.tmbd.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class MovieControllerIntegTest {

    @Autowired
    MockMvc mockMvc;

    // ─── Helper ────────────────────────────────────────────────────────────────

    /** Creates a movie and returns its generated id. */
    private int createMovie(String name, String director, String actors) throws Exception {
        String json = """
                {
                    "name": "%s",
                    "director": "%s",
                    "actors": [%s]
                }
                """.formatted(name, director, actors);

        MvcResult result = mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn();

        return JsonPath.read(result.getResponse().getContentAsString(), "$.id");
    }

    // ─── CREATE ────────────────────────────────────────────────────────────────

    @Test
    void givenMovie_whenCreateMovie_thenReturnSavedMovie() throws Exception {
        String movieJson = """
                {
                    "name": "RRR",
                    "director": "SS Rajamouli",
                    "actors": ["NTR", "Ram Charan", "Alia Bhatt"]
                }
                """;

        mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(movieJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("RRR"))
                .andExpect(jsonPath("$.director").value("SS Rajamouli"))
                .andExpect(jsonPath("$.actors[0]").value("NTR"))
                .andExpect(jsonPath("$.actors[1]").value("Ram Charan"))
                .andExpect(jsonPath("$.actors[2]").value("Alia Bhatt"));
    }

    // ─── READ BY ID ────────────────────────────────────────────────────────────

    @Test
    void givenExistingId_whenGetMovieById_thenReturnMovie() throws Exception {
        int id = createMovie("Baahubali", "SS Rajamouli", "\"Prabhas\", \"Anushka\"");

        mockMvc.perform(get("/movies/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Baahubali"))
                .andExpect(jsonPath("$.director").value("SS Rajamouli"))
                .andExpect(jsonPath("$.actors[0]").value("Prabhas"))
                .andExpect(jsonPath("$.actors[1]").value("Anushka"));
    }

    @Test
    void givenNonExistingId_whenGetMovieById_thenReturn404() throws Exception {
        mockMvc.perform(get("/movies/{id}", 9999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Movie with id 9999 not found"));
    }

    // ─── READ ALL ──────────────────────────────────────────────────────────────

    @Test
    void givenMoviesExist_whenGetAllMovies_thenReturnList() throws Exception {
        createMovie("Movie A", "Director A", "\"Actor A\"");
        createMovie("Movie B", "Director B", "\"Actor B\"");

        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").isNotEmpty());
    }

    // ─── UPDATE ────────────────────────────────────────────────────────────────

    @Test
    void givenExistingId_whenUpdateMovie_thenReturnUpdatedMovie() throws Exception {
        int id = createMovie("Old Name", "Old Director", "\"Old Actor\"");

        String updatedJson = """
                {
                    "name": "New Name",
                    "director": "New Director",
                    "actors": ["New Actor 1", "New Actor 2"]
                }
                """;

        mockMvc.perform(put("/movies/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("New Name"))
                .andExpect(jsonPath("$.director").value("New Director"))
                .andExpect(jsonPath("$.actors[0]").value("New Actor 1"))
                .andExpect(jsonPath("$.actors[1]").value("New Actor 2"));
    }

    @Test
    void givenNonExistingId_whenUpdateMovie_thenReturn404() throws Exception {
        String updatedJson = """
                {
                    "name": "Ghost Movie",
                    "director": "Ghost Director",
                    "actors": ["Ghost Actor"]
                }
                """;

        mockMvc.perform(put("/movies/{id}", 9999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Movie with id 9999 not found"));
    }

    // ─── DELETE ────────────────────────────────────────────────────────────────

    @Test
    void givenExistingId_whenDeleteMovie_thenReturn204AndMovieGone() throws Exception {
        int id = createMovie("To Be Deleted", "Some Director", "\"Some Actor\"");

        // Delete the movie
        mockMvc.perform(delete("/movies/{id}", id))
                .andExpect(status().isNoContent());

        // Verify it is gone
        mockMvc.perform(get("/movies/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Movie with id " + id + " not found"));
    }

    @Test
    void givenNonExistingId_whenDeleteMovie_thenReturn404() throws Exception {
        mockMvc.perform(delete("/movies/{id}", 9999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Movie with id 9999 not found"));
    }

}