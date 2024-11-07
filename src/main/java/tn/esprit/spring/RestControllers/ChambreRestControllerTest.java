package tn.esprit.spring.RestControllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.TypeChambre;
import tn.esprit.spring.Services.Chambre.IChambreService;

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

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IChambreService service;

    private Chambre chambre;

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

/*    @Test
    void delete() throws Exception {
        // Prepare the Chambre object you want to delete
        Chambre chambreToDelete = new Chambre();
        chambreToDelete.setIdChambre(1L);  // Assuming 'idChambre' is the correct field name
        chambreToDelete.setTypeC(TypeChambre.SIMPLE);  // Assuming 'SIMPLE' is the correct enum value

        // Mock the service layer to do nothing on delete
        doNothing().when(service).delete(any(Chambre.class));

        // Perform the delete request with correct JSON payload representing a Chambre object
        mockMvc.perform(delete("/chambre/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idChambre\": 1, \"typeC\": \"SIMPLE\"}"))  // Adjusted to match Chambre fields
                .andExpect(status().isOk());

        // Verify that the service delete method was called once
        verify(service, times(1)).delete(any(Chambre.class));
    }





    @Test
    void deleteById() throws Exception {
        doNothing().when(service).deleteById(1L);

        mockMvc.perform(delete("/chambre/deleteById")
                        .param("id", "1"))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteById(1L);
    }*/

    @Test
    void getChambresParNomBloc() throws Exception {
        List<Chambre> chambres = Arrays.asList(chambre);
        when(service.getChambresParNomBloc("BlocA")).thenReturn(chambres);

        mockMvc.perform(get("/chambre/getChambresParNomBloc")
                        .param("nomBloc", "BlocA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(chambres.size()));

        verify(service, times(1)).getChambresParNomBloc("BlocA");
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
        when(service.getChambresNonReserveParNomFoyerEtTypeChambre("FoyerA", TypeChambre.SIMPLE))
                .thenReturn(chambres);

        mockMvc.perform(get("/chambre/getChambresNonReserveParNomFoyerEtTypeChambre")
                        .param("nomFoyer", "FoyerA")
                        .param("type", "SIMPLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(chambres.size()));

        verify(service, times(1)).getChambresNonReserveParNomFoyerEtTypeChambre("FoyerA", TypeChambre.SIMPLE);
    }
}
