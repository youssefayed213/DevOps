package tn.esprit.spring.restcontrollers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.dao.entities.Chambre;
import tn.esprit.spring.dao.entities.TypeChambre;
import tn.esprit.spring.services.chambre.IChambreService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChambreRestController.class)
class ChambreRestControllerTest {


    private final MockMvc mockMvc;

    @MockBean
    private IChambreService service;

    private Chambre chambre;

    private static final String TEST_BLOC_NAME = "BlocA";
    private static final String TEST_FOYER_NAME = "FoyerA";
    @Autowired
    public ChambreRestControllerTest(MockMvc mockMvc){
        this.mockMvc = mockMvc;
    }
    @BeforeEach
    void setUp() {
        chambre = new Chambre();
        chambre.setIdChambre(1L);
        chambre.setTypeC(TypeChambre.SIMPLE);
        // Initialize other fields as necessary
    }

    @Test
    void addOrUpdate() throws Exception {
        when(service.addOrUpdate(any(Chambre.class))).thenReturn(chambre);

        mockMvc.perform(post("/chambre/addOrUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"type\": \"SIMPLE\"}")) // Adjust JSON according to Chambre fields
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idChambre").value(chambre.getIdChambre()));

        verify(service, times(1)).addOrUpdate(any(Chambre.class));
    }

    @Test
    void findAll() throws Exception {
        List<Chambre> chambres = Arrays.asList(chambre);
        when(service.findAll()).thenReturn(chambres);

        mockMvc.perform(get("/chambre/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(chambres.size()));

        verify(service, times(1)).findAll();
    }

    @Test
    void findById() throws Exception {
        when(service.findById(1L)).thenReturn(chambre);

        mockMvc.perform(get("/chambre/findById")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idChambre").value(chambre.getIdChambre()));

        verify(service, times(1)).findById(1L);
    }

    @Test
    void getChambresParNomBloc() throws Exception {
        List<Chambre> chambres = Arrays.asList(chambre);
        when(service.getChambresParNomBloc(TEST_BLOC_NAME)).thenReturn(chambres);

        mockMvc.perform(get("/chambre/getChambresParNomBloc")
                        .param("nomBloc", TEST_BLOC_NAME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(chambres.size()));

        verify(service, times(1)).getChambresParNomBloc(TEST_BLOC_NAME);
    }

    @Test
    void nbChambreParTypeEtBloc() throws Exception {
        when(service.nbChambreParTypeEtBloc(TypeChambre.SIMPLE, 1L)).thenReturn(5L);

        mockMvc.perform(get("/chambre/nbChambreParTypeEtBloc")
                        .param("type", "SIMPLE")
                        .param("idBloc", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));

        verify(service, times(1)).nbChambreParTypeEtBloc(TypeChambre.SIMPLE, 1L);
    }

    @Test
    void getChambresNonReserveParNomFoyerEtTypeChambre() throws Exception {
        List<Chambre> chambres = Arrays.asList(chambre);
        when(service.getChambresNonReserveParNomFoyerEtTypeChambre(TEST_FOYER_NAME, TypeChambre.SIMPLE))
                .thenReturn(chambres);

        mockMvc.perform(get("/chambre/getChambresNonReserveParNomFoyerEtTypeChambre")
                        .param("nomFoyer", TEST_FOYER_NAME)
                        .param("type", "SIMPLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(chambres.size()));

        verify(service, times(1)).getChambresNonReserveParNomFoyerEtTypeChambre(TEST_FOYER_NAME, TypeChambre.SIMPLE);
    }
}
