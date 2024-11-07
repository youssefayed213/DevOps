package tn.esprit.spring.RestControllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.Services.Foyer.IFoyerService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class FoyerRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IFoyerService foyerService;

    @InjectMocks
    private FoyerRestController foyerRestController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(foyerRestController).build();
    }

    @Test
    public void testAddOrUpdate() throws Exception {
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(1L);
        foyer.setNomFoyer("Foyer A");

        when(foyerService.addOrUpdate(any(Foyer.class))).thenReturn(foyer);

        mockMvc.perform(post("/foyer/addOrUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"nom\": \"Foyer A\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idFoyer").value(1))
                .andExpect(jsonPath("$.nomFoyer").value("Foyer A"));

        verify(foyerService, times(1)).addOrUpdate(any(Foyer.class));
    }

    @Test
    public void testFindAll() throws Exception {
        Foyer foyer1 = new Foyer();
        foyer1.setIdFoyer(1L);
        foyer1.setNomFoyer("Foyer A");

        Foyer foyer2 = new Foyer();
        foyer2.setIdFoyer(2L);
        foyer2.setNomFoyer("Foyer B");

        when(foyerService.findAll()).thenReturn(List.of(foyer1, foyer2));

        mockMvc.perform(get("/foyer/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomFoyer").value("Foyer A"))
                .andExpect(jsonPath("$[1].nomFoyer").value("Foyer B"));

        verify(foyerService, times(1)).findAll();
    }

    @Test
    public void testFindById() throws Exception {
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(1L);
        foyer.setNomFoyer("Foyer A");

        when(foyerService.findById(1L)).thenReturn(foyer);

        mockMvc.perform(get("/foyer/findById?id=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idFoyer").value(1))
                .andExpect(jsonPath("$.nomFoyer").value("Foyer A"));

        verify(foyerService, times(1)).findById(1L);
    }

    @Test
    public void testDelete() throws Exception {
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(1L);

        // Use any() matcher to match the object type, not the exact instance
        doNothing().when(foyerService).delete(any(Foyer.class));

        mockMvc.perform(delete("/foyer/delete")
                        .contentType("application/json")
                        .content("{\"id\": 1}"))
                .andExpect(status().isOk());

        // Verify that delete was called once with the correct argument
        verify(foyerService, times(1)).delete(any(Foyer.class));
    }


    @Test
    public void testDeleteById() throws Exception {
        doNothing().when(foyerService).deleteById(1L);

        mockMvc.perform(delete("/foyer/deleteById?id=1"))
                .andExpect(status().isOk());

        verify(foyerService, times(1)).deleteById(1L);
    }

    @Test
    public void testAffecterFoyerAUniversite() throws Exception {
        Universite universite = new Universite();
        universite.setNomUniversite("Université A");

        when(foyerService.affecterFoyerAUniversite(1L, "Université A")).thenReturn(universite);

        mockMvc.perform(put("/foyer/affecterFoyerAUniversite?idFoyer=1&nomUniversite=Université A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomUniversite").value("Université A"));

        verify(foyerService, times(1)).affecterFoyerAUniversite(1L, "Université A");
    }

    @Test
    public void testDesaffecterFoyerAUniversite() throws Exception {
        Universite universite = new Universite();
        universite.setNomUniversite("Université A");

        when(foyerService.desaffecterFoyerAUniversite(1L)).thenReturn(universite);

        mockMvc.perform(put("/foyer/desaffecterFoyerAUniversite?idUniversite=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomUniversite").value("Université A"));

        verify(foyerService, times(1)).desaffecterFoyerAUniversite(1L);
    }

    @Test
    public void testAjouterFoyerEtAffecterAUniversite() throws Exception {
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(1L);
        foyer.setNomFoyer("Foyer A");

        Universite universite = new Universite();
        universite.setNomUniversite("Université A");

        when(foyerService.ajouterFoyerEtAffecterAUniversite(any(Foyer.class), eq(1L)))
                .thenReturn(foyer);

        mockMvc.perform(post("/foyer/ajouterFoyerEtAffecterAUniversite?idUniversite=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"nom\": \"Foyer A\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idFoyer").value(1))
                .andExpect(jsonPath("$.nomFoyer").value("Foyer A"));

        verify(foyerService, times(1)).ajouterFoyerEtAffecterAUniversite(any(Foyer.class), eq(1L));
    }
}
