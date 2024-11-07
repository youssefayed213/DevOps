package tn.esprit.spring.RestControllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.Services.Bloc.IBlocService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BlocRestController.class)
class BlocRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IBlocService service;

    private Bloc bloc;

    @BeforeEach
    void setUp() {
        bloc = new Bloc();
        bloc.setNomBloc("Bloc1");
        bloc.setIdBloc(1L);
    }

    @Test
    void testAddOrUpdate() throws Exception {
        when(service.addOrUpdate(any(Bloc.class))).thenReturn(bloc);

        mockMvc.perform(post("/bloc/addOrUpdate")
                        .contentType("application/json")
                        .content("{\"nom\":\"Bloc1\",\"id\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomBloc").value("Bloc1"))
                .andExpect(jsonPath("$.idBloc").value(1));

        verify(service, times(1)).addOrUpdate(any(Bloc.class));
    }

    @Test
    void testFindAll() throws Exception {
        List<Bloc> blocs = Arrays.asList(bloc);
        when(service.findAll()).thenReturn(blocs);

        mockMvc.perform(get("/bloc/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomBloc").value("Bloc1"))
                .andExpect(jsonPath("$[0].idBloc").value(1));

        verify(service, times(1)).findAll();
    }

    @Test
    void testFindById() throws Exception {
        when(service.findById(1L)).thenReturn(bloc);

        mockMvc.perform(get("/bloc/findById")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomBloc").value("Bloc1"))
                .andExpect(jsonPath("$.idBloc").value(1));

        verify(service, times(1)).findById(1L);
    }

    @Test
    void testDelete() throws Exception {
        doNothing().when(service).delete(any(Bloc.class));

        mockMvc.perform(delete("/bloc/delete")
                        .contentType("application/json")
                        .content("{\"nom\":\"Bloc1\",\"id\":1}"))
                .andExpect(status().isOk());

        verify(service, times(1)).delete(any(Bloc.class));
    }

    @Test
    void testDeleteById() throws Exception {
        doNothing().when(service).deleteById(1L);

        mockMvc.perform(delete("/bloc/deleteById")
                        .param("id", "1"))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteById(1L);
    }

    @Test
    void testAffecterChambresABloc() throws Exception {
        List<Long> numChambre = Arrays.asList(101L, 102L);
        when(service.affecterChambresABloc(eq(numChambre), eq("Bloc1"))).thenReturn(bloc);

        mockMvc.perform(put("/bloc/affecterChambresABloc")
                        .param("nomBloc", "Bloc1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[101, 102]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idBloc").exists())
                .andExpect(jsonPath("$.nomBloc").value("Bloc1"))
                .andExpect(jsonPath("$.capaciteBloc").exists());

               // .andExpect(jsonPath("$.nom").value("Bloc1"));

        verify(service, times(1)).affecterChambresABloc(eq(numChambre), eq("Bloc1"));
    }

    @Test
    void testAffecterBlocAFoyer() throws Exception {
        // Mock the returned bloc to have the expected "nom" field
        Bloc bloc = new Bloc();
        bloc.setNomBloc("Bloc1"); // Set the "nom" field so it matches the expectation

        // Define behavior for the service method
        when(service.affecterBlocAFoyer(eq("Bloc1"), eq("Foyer1"))).thenReturn(bloc);

        // Perform the request and assert the response
        mockMvc.perform(put("/bloc/affecterBlocAFoyer")
                        .param("nomBloc", "Bloc1")
                        .param("nomFoyer", "Foyer1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomBloc").value("Bloc1"));

        // Verify that the service method was called exactly once
        verify(service, times(1)).affecterBlocAFoyer(eq("Bloc1"), eq("Foyer1"));
    }

}
