package tn.esprit.spring.restcontrollers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.dao.entities.Bloc;
import tn.esprit.spring.services.bloc.IBlocService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BlocRestController.class)
class BlocRestControllerTest {

    private final MockMvc mockMvc;

    @MockBean
    private IBlocService service;

    private Bloc bloc;

    private static final String TEST_BLOC_NAME = "Bloc1";
    private static final String TEST_FOYER_NAME = "Foyer1";

    @Autowired
    public BlocRestControllerTest(MockMvc mockMvc){
        this.mockMvc = mockMvc;
    }

    @BeforeEach
    void setUp() {
        bloc = new Bloc();
        bloc.setNomBloc(TEST_BLOC_NAME);
        bloc.setIdBloc(1L);
    }

    @Test
    void testAddOrUpdate() throws Exception {
        when(service.addOrUpdate(any(Bloc.class))).thenReturn(bloc);

        mockMvc.perform(post("/bloc/addOrUpdate")
                        .contentType("application/json")
                        .content("{\"nom\":\"Bloc1\",\"id\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomBloc").value(TEST_BLOC_NAME))
                .andExpect(jsonPath("$.idBloc").value(1));

        verify(service, times(1)).addOrUpdate(any(Bloc.class));
    }

    @Test
    void testFindAll() throws Exception {
        List<Bloc> blocs = Arrays.asList(bloc);
        when(service.findAll()).thenReturn(blocs);

        mockMvc.perform(get("/bloc/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomBloc").value(TEST_BLOC_NAME))
                .andExpect(jsonPath("$[0].idBloc").value(1));

        verify(service, times(1)).findAll();
    }

    @Test
    void testFindById() throws Exception {
        when(service.findById(1L)).thenReturn(bloc);

        mockMvc.perform(get("/bloc/findById")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomBloc").value(TEST_BLOC_NAME))
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
        when(service.affecterChambresABloc(eq(numChambre), eq(TEST_BLOC_NAME))).thenReturn(bloc);

        mockMvc.perform(put("/bloc/affecterChambresABloc")
                        .param("nomBloc", TEST_BLOC_NAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[101, 102]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idBloc").exists())
                .andExpect(jsonPath("$.nomBloc").value(TEST_BLOC_NAME))
                .andExpect(jsonPath("$.capaciteBloc").exists());



        verify(service, times(1)).affecterChambresABloc(eq(numChambre), eq(TEST_BLOC_NAME));
    }

    @Test
    void testAffecterBlocAFoyer() throws Exception {
        // Mock the returned bloc to have the expected "nom" field
        Bloc blocs = new Bloc();
        blocs.setNomBloc(TEST_BLOC_NAME); // Set the "nom" field so it matches the expectation

        // Define behavior for the service method
        when(service.affecterBlocAFoyer(eq(TEST_BLOC_NAME), eq(TEST_FOYER_NAME))).thenReturn(blocs);

        // Perform the request and assert the response
        mockMvc.perform(put("/bloc/affecterBlocAFoyer")
                        .param("nomBloc", TEST_BLOC_NAME)
                        .param("nomFoyer", TEST_FOYER_NAME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomBloc").value(TEST_BLOC_NAME));

        // Verify that the service method was called exactly once
        verify(service, times(1)).affecterBlocAFoyer(eq(TEST_BLOC_NAME), eq(TEST_FOYER_NAME));
    }

}
