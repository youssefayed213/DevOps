package tn.esprit.spring.restcontrollers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.dao.entities.Etudiant;
import tn.esprit.spring.services.etudiant.IEtudiantService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(EtudiantRestController.class)
@ExtendWith(SpringExtension.class)
class EtudiantRestControllerTest {


    private final MockMvc mockMvc;

    @MockBean
    private IEtudiantService service;

    private Etudiant etudiant1;
    private Etudiant etudiant2;

    private static final String TEST_UNIVERSITY_NAME = "XYZ University";

    @Autowired
    public EtudiantRestControllerTest(MockMvc mockMvc){
        this.mockMvc = mockMvc;
    }

    @BeforeEach
    void setUp() {
        etudiant1 = new Etudiant();
        etudiant1.setIdEtudiant(1L);
        etudiant1.setNomEt("John");
        etudiant1.setPrenomEt("Doe");
        etudiant1.setCin(123456789);
        etudiant1.setEcole(TEST_UNIVERSITY_NAME);
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
                .andExpect(jsonPath("$[0].ecole").value(TEST_UNIVERSITY_NAME))
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
                .andExpect(jsonPath("$.ecole").value(TEST_UNIVERSITY_NAME))
                .andExpect(jsonPath("$.dateNaissance").value("1999-05-12"));

        // Verify that the service method was called
        verify(service, times(1)).findById(1L);
    }


}
