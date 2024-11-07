package tn.esprit.spring.Services.Foyer;

import tn.esprit.spring.DAO.Entities.*;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.DAO.Repositories.EtudiantRepository;
import tn.esprit.spring.DAO.Repositories.FoyerRepository;
import tn.esprit.spring.DAO.Repositories.UniversiteRepository;
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
        Foyer foyer = new Foyer();
        Universite universite = new Universite();
        universite.setNomUniversite("Test Universite");

        when(foyerRepository.findById(1L)).thenReturn(Optional.of(foyer));
        when(universiteRepository.findByNomUniversite("Test Universite")).thenReturn(universite);
        when(universiteRepository.save(universite)).thenReturn(universite);

        Universite result = foyerService.affecterFoyerAUniversite(1L, "Test Universite");

        assertNotNull(result);
        assertEquals(foyer, result.getFoyer());  // Check if the Foyer is linked to the Universite
        verify(universiteRepository).save(universite);  // Ensure the save was called
    }

    @Test
    void testDesaffecterFoyerAUniversite() {
        Universite universite = new Universite();
        Foyer foyer = new Foyer();
        universite.setFoyer(foyer);  // Set a foyer to the universite

        when(universiteRepository.findById(1L)).thenReturn(Optional.of(universite));
        when(universiteRepository.save(universite)).thenReturn(universite);

        Universite result = foyerService.desaffecterFoyerAUniversite(1L);

        assertNotNull(result);
        assertNull(result.getFoyer());  // Check if the Foyer has been detached from the Universite
        verify(universiteRepository).save(universite);  // Ensure the save was called
    }

    @Test
    void testAjouterFoyerEtAffecterAUniversite() {
        Foyer foyer = new Foyer();
        Universite universite = new Universite();
        Bloc bloc = new Bloc();
        foyer.setBlocs(List.of(bloc));  // Associate the bloc with the foyer

        when(foyerRepository.save(foyer)).thenReturn(foyer);
        when(universiteRepository.findById(1L)).thenReturn(Optional.of(universite));
        when(universiteRepository.save(universite)).thenReturn(universite);

        Foyer result = foyerService.ajouterFoyerEtAffecterAUniversite(foyer, 1L);

        assertNotNull(result);
        verify(blocRepository).save(bloc);  // Verify that the bloc is saved with the foyer
        verify(universiteRepository).save(universite);  // Verify that the universite is saved
    }

    @Test
    void testAjoutFoyerEtBlocs() {
        Foyer foyer = new Foyer();
        Bloc bloc1 = new Bloc();
        Bloc bloc2 = new Bloc();
        foyer.setBlocs(List.of(bloc1, bloc2));  // Associate two blocs with the foyer

        when(foyerRepository.save(foyer)).thenReturn(foyer);
        when(blocRepository.save(any(Bloc.class))).thenReturn(new Bloc());  // Mock saving blocs

        Foyer result = foyerService.ajoutFoyerEtBlocs(foyer);

        assertNotNull(result);
        verify(blocRepository, times(2)).save(any(Bloc.class));  // Verify both blocs are saved
    }
}
