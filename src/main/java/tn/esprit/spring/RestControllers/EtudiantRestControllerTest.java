package tn.esprit.spring.RestControllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.Services.Etudiant.IEtudiantService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(EtudiantRestController.class)
@ExtendWith(SpringExtension.class)
class EtudiantRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IEtudiantService service;

    private Etudiant etudiant1;
    private Etudiant etudiant2;

    @BeforeEach
    void setUp() {
        etudiant1 = new Etudiant();
        etudiant1.setIdEtudiant(1L);
        etudiant1.setNomEt("John");
        etudiant1.setPrenomEt("Doe");
        etudiant1.setCin(123456789);
        etudiant1.setEcole("XYZ University");
        etudiant1.setDateNaissance(LocalDate.of(1999, 5, 12));

        etudiant2 = new Etudiant();
        etudiant2.setIdEtudiant(2L);
        etudiant2.setNomEt("Jane");
        etudiant2.setPrenomEt("Smith");
        etudiant2.setCin(987654321);
        etudiant2.setEcole("ABC College");
        etudiant2.setDateNaissance(LocalDate.of(2000, 8, 20));
    }

    @Test
    void addOrUpdate() throws Exception {
        // Mock the service layer to return the Etudiant when added or updated
        when(service.addOrUpdate(any(Etudiant.class))).thenReturn(etudiant1);

        // Perform a POST request to add or update an Etudiant
        mockMvc.perform(post("/etudiant/addOrUpdate")
                        .contentType("application/json")
                        .content("{\"idEtudiant\":1, \"nomEt\":\"John\", \"prenomEt\":\"Doe\", \"cin\":123456789, \"ecole\":\"XYZ University\", \"dateNaissance\":\"1999-05-12\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEtudiant").value(1))
                .andExpect(jsonPath("$.nomEt").value("John"))
                .andExpect(jsonPath("$.prenomEt").value("Doe"));
    }

    @Test
    void findAll() throws Exception {
        // Prepare a list of Etudiant
        List<Etudiant> etudiants = Arrays.asList(etudiant1, etudiant2);

        // Mock the service layer to return the list of Etudiants
        when(service.findAll()).thenReturn(etudiants);

        // Perform a GET request to find all Etudiants
        mockMvc.perform(get("/etudiant/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idEtudiant").value(1))
                .andExpect(jsonPath("$[0].nomEt").value("John"))
                .andExpect(jsonPath("$[0].prenomEt").value("Doe"))
                .andExpect(jsonPath("$[0].cin").value(123456789))
                .andExpect(jsonPath("$[0].ecole").value("XYZ University"))
                .andExpect(jsonPath("$[0].dateNaissance").value("1999-05-12"))
                .andExpect(jsonPath("$[1].idEtudiant").value(2))
                .andExpect(jsonPath("$[1].nomEt").value("Jane"))
                .andExpect(jsonPath("$[1].prenomEt").value("Smith"))
                .andExpect(jsonPath("$[1].cin").value(987654321))
                .andExpect(jsonPath("$[1].ecole").value("ABC College"))
                .andExpect(jsonPath("$[1].dateNaissance").value("2000-08-20"));

        // Verify that the service method was called
        verify(service, times(1)).findAll();
    }

    @Test
    void findById() throws Exception {
        // Mock the service layer to return the Etudiant with ID 1
        when(service.findById(1L)).thenReturn(etudiant1);

        // Perform a GET request to find an Etudiant by ID
        mockMvc.perform(get("/etudiant/findById")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEtudiant").value(1))
                .andExpect(jsonPath("$.nomEt").value("John"))
                .andExpect(jsonPath("$.prenomEt").value("Doe"))
                .andExpect(jsonPath("$.cin").value(123456789))
                .andExpect(jsonPath("$.ecole").value("XYZ University"))
                .andExpect(jsonPath("$.dateNaissance").value("1999-05-12"));

        // Verify that the service method was called
        verify(service, times(1)).findById(1L);
    }

  /*  @Test
    void delete() throws Exception {
        // Create an Etudiant object to send in the request body
        Etudiant etudiant = new Etudiant();
        etudiant.setIdEtudiant(1);
        etudiant.setNomEt("John");
        etudiant.setPrenomEt("Doe");
        etudiant.setCin(123456789);
        etudiant.setEcole("XYZ University");
        etudiant.setDateNaissance(LocalDate.of(1999, 5, 12));

        // Mock the service layer to do nothing on delete
        doNothing().when(service).delete(any(Etudiant.class));

        // Perform a DELETE request with the Etudiant object in the body
        mockMvc.perform(delete("/etudiant/delete")
                        .contentType("application/json")
                        .content("{\"nom\":\"Bloc1\",\"id\":1}"))
                .andExpect(status().isOk());

        // Verify that the service delete method was called once
        verify(service, times(1)).delete(any(Etudiant.class));
    }

    // Utility method to convert object to JSON string
    private String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }


 /*   @Test
    void deleteById() throws Exception {
        // Mock the service layer to do nothing on delete
        doNothing().when(service).deleteById(1L);

        // Perform a DELETE request to delete an Etudiant by ID
        mockMvc.perform(delete("/etudiant/deleteById")
                        .param("id", "1"))
                .andExpect(status().isOk());

        // Verify that the service deleteById method was called once
        verify(service, times(1)).deleteById(1L);
    }*/
}
