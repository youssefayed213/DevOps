package tn.esprit.spring.RestControllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.Services.Universite.IUniversiteService;

import java.util.Arrays;
import java.util.List;

@WebMvcTest(UniversiteRestController.class)
public class UniversiteRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUniversiteService universiteService;

    @Autowired
    private ObjectMapper objectMapper;

    private Universite universite;

    @BeforeEach
    void setUp() {
        universite = new Universite();
        universite.setIdUniversite(1L);
        universite.setNomUniversite("Université de Test");
    }

    @Test
    void addOrUpdateUniversiteTest() throws Exception {
        // Arrange
        when(universiteService.addOrUpdate(any(Universite.class))).thenReturn(universite);

        // Act & Assert
        mockMvc.perform(post("/universite/addOrUpdate")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(universite)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUniversite").value(1))
                .andExpect(jsonPath("$.nomUniversite").value("Université de Test"));

        verify(universiteService, times(1)).addOrUpdate(any(Universite.class));
    }

    @Test
    void findAllUniversitesTest() throws Exception {
        // Arrange
        List<Universite> universites = Arrays.asList(universite);
        when(universiteService.findAll()).thenReturn(universites);

        // Act & Assert
        mockMvc.perform(get("/universite/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idUniversite").value(1))
                .andExpect(jsonPath("$[0].nomUniversite").value("Université de Test"));

        verify(universiteService, times(1)).findAll();
    }

    @Test
    void findUniversiteByIdTest() throws Exception {
        // Arrange
        when(universiteService.findById(1L)).thenReturn(universite);

        // Act & Assert
        mockMvc.perform(get("/universite/findById")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUniversite").value(1))
                .andExpect(jsonPath("$.nomUniversite").value("Université de Test"));

        verify(universiteService, times(1)).findById(1L);
    }

    @Test
    void deleteUniversiteTest() throws Exception {
        // Act
        mockMvc.perform(delete("/universite/delete")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(universite)))
                .andExpect(status().isOk());

        // Assert
        verify(universiteService, times(1)).delete(any(Universite.class));
    }

    @Test
    void deleteUniversiteByIdTest() throws Exception {
        // Act
        mockMvc.perform(delete("/universite/deleteById")
                        .param("id", "1"))
                .andExpect(status().isOk());

        // Assert
        verify(universiteService, times(1)).deleteById(1L);
    }
}
