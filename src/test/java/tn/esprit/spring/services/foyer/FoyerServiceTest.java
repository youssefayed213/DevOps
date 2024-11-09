package tn.esprit.spring.services.foyer;

import tn.esprit.spring.dao.entities.*;
import tn.esprit.spring.dao.repositories.BlocRepository;
import tn.esprit.spring.dao.repositories.FoyerRepository;
import tn.esprit.spring.dao.repositories.UniversiteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Arrays;
import java.util.Optional;

public class FoyerServiceTest {

    @Mock
    private FoyerRepository foyerRepository;


    @Mock
    private UniversiteRepository universiteRepository;

    @Mock
    private BlocRepository blocRepository;

    @InjectMocks
    private FoyerService foyerService;

    private Foyer foyer;

    private static final String TEST_UNIVERSITY_NAME = "Test Universite";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        foyer = new Foyer();  // Initialize the Foyer object for testing
    }

    @Test
    void testAddOrUpdate() {
        when(foyerRepository.save(any(Foyer.class))).thenReturn(foyer);  // Mock the save method

        Foyer result = foyerService.addOrUpdate(foyer);  // Call the service method

        assertNotNull(result);
        verify(foyerRepository).save(foyer);  // Verify that save was called with the foyer object
    }

    @Test
    void testFindAll() {
        Foyer foyer1 = new Foyer();
        Foyer foyer2 = new Foyer();
        when(foyerRepository.findAll()).thenReturn(Arrays.asList(foyer1, foyer2));

        List<Foyer> result = foyerService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(foyerRepository).findAll();  // Verify that findAll was called
    }

    @Test
    void testFindById() {
        when(foyerRepository.findById(1L)).thenReturn(Optional.of(foyer));

        Foyer result = foyerService.findById(1L);

        assertNotNull(result);
        verify(foyerRepository).findById(1L);  // Verify that findById was called
    }

    @Test
    void testDeleteById() {
        doNothing().when(foyerRepository).deleteById(1L);  // Mock deleteById to do nothing

        foyerService.deleteById(1L);

        verify(foyerRepository).deleteById(1L);  // Verify that deleteById was called
    }

    @Test
    void testDelete() {
        Foyer foyerToDelete = new Foyer();
        doNothing().when(foyerRepository).delete(foyerToDelete);  // Mock delete

        foyerService.delete(foyerToDelete);

        verify(foyerRepository).delete(foyerToDelete);  // Verify that delete was called
    }

    @Test
    void testAffecterFoyerAUniversite() {
        Foyer foyer1 = new Foyer();
        Universite universite = new Universite();
        universite.setNomUniversite(TEST_UNIVERSITY_NAME);

        when(foyerRepository.findById(1L)).thenReturn(Optional.of(foyer1));
        when(universiteRepository.findByNomUniversite(TEST_UNIVERSITY_NAME)).thenReturn(universite);
        when(universiteRepository.save(universite)).thenReturn(universite);

        Universite result = foyerService.affecterFoyerAUniversite(1L, TEST_UNIVERSITY_NAME);

        assertNotNull(result);
        assertEquals(foyer1, result.getFoyer());  // Check if the Foyer is linked to the Universite
        verify(universiteRepository).save(universite);  // Ensure the save was called
    }

    @Test
    void testDesaffecterFoyerAUniversite() {
        Universite universite = new Universite();
        Foyer foyero = new Foyer();
        universite.setFoyer(foyero);  // Set a foyer to the universite

        when(universiteRepository.findById(1L)).thenReturn(Optional.of(universite));
        when(universiteRepository.save(universite)).thenReturn(universite);

        Universite result = foyerService.desaffecterFoyerAUniversite(1L);

        assertNotNull(result);
        assertNull(result.getFoyer());  // Check if the Foyer has been detached from the Universite
        verify(universiteRepository).save(universite);  // Ensure the save was called
    }

    @Test
    void testAjouterFoyerEtAffecterAUniversite() {
        Foyer foyer3 = new Foyer();
        Universite universite = new Universite();
        Bloc bloc = new Bloc();
        foyer3.setBlocs(List.of(bloc));  // Associate the bloc with the foyer

        when(foyerRepository.save(foyer3)).thenReturn(foyer3);
        when(universiteRepository.findById(1L)).thenReturn(Optional.of(universite));
        when(universiteRepository.save(universite)).thenReturn(universite);

        Foyer result = foyerService.ajouterFoyerEtAffecterAUniversite(foyer3, 1L);

        assertNotNull(result);
        verify(blocRepository).save(bloc);  // Verify that the bloc is saved with the foyer
        verify(universiteRepository).save(universite);  // Verify that the universite is saved
    }

    @Test
    void testAjoutFoyerEtBlocs() {
        Foyer foyer4 = new Foyer();
        Bloc bloc1 = new Bloc();
        Bloc bloc2 = new Bloc();
        foyer4.setBlocs(List.of(bloc1, bloc2));  // Associate two blocs with the foyer

        when(foyerRepository.save(foyer4)).thenReturn(foyer4);
        when(blocRepository.save(any(Bloc.class))).thenReturn(new Bloc());  // Mock saving blocs

        Foyer result = foyerService.ajoutFoyerEtBlocs(foyer4);

        assertNotNull(result);
        verify(blocRepository, times(2)).save(any(Bloc.class));  // Verify both blocs are saved
    }
}
