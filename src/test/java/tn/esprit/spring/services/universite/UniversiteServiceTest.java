package tn.esprit.spring.services.universite;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.dao.entities.Universite;
import tn.esprit.spring.dao.repositories.UniversiteRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UniversiteServiceTest {

    @Mock
    private UniversiteRepository universiteRepository;

    @InjectMocks
    private UniversiteService universiteService;

    private Universite universite;

    private static final String TEST_UNIVERSITY_NAME = "Test University";

    @BeforeEach
    void setUp() {
        // Initialize a Universite object for testing
        universite = new Universite();
        universite.setIdUniversite(1L);
        universite.setNomUniversite(TEST_UNIVERSITY_NAME);
    }

    @Test
    void testAddOrUpdate() {
        // Mocking the save method of the repository
        when(universiteRepository.save(universite)).thenReturn(universite);

        // Calling the service method
        Universite savedUniversite = universiteService.addOrUpdate(universite);

        // Verifying the interaction and asserting the result
        verify(universiteRepository, times(1)).save(universite);
        assertNotNull(savedUniversite);
        assertEquals(TEST_UNIVERSITY_NAME, savedUniversite.getNomUniversite());
    }

    @Test
    void testFindAll() {
        // Mocking the findAll method of the repository
        List<Universite> universites = Arrays.asList(universite, new Universite());
        when(universiteRepository.findAll()).thenReturn(universites);

        // Calling the service method
        List<Universite> foundUniversites = universiteService.findAll();

        // Verifying the interaction and asserting the result
        verify(universiteRepository, times(1)).findAll();
        assertNotNull(foundUniversites);
        assertEquals(2, foundUniversites.size());
    }

    @Test
    void testFindById() {
        // Mocking the findById method of the repository
        when(universiteRepository.findById(1L)).thenReturn(Optional.of(universite));

        // Calling the service method
        Universite foundUniversite = universiteService.findById(1L);

        // Verifying the interaction and asserting the result
        verify(universiteRepository, times(1)).findById(1L);
        assertNotNull(foundUniversite);
        assertEquals(TEST_UNIVERSITY_NAME, foundUniversite.getNomUniversite());
    }

    @Test
    void testFindByIdNotFound() {
        // Mocking the findById method of the repository to return an empty optional
        when(universiteRepository.findById(1L)).thenReturn(Optional.empty());

        // Calling the service method and expecting an exception
        assertThrows(EntityNotFoundException.class, () -> universiteService.findById(1L));

        // Verifying the interaction
        verify(universiteRepository, times(1)).findById(1L);
    }




    @Test
    void testDeleteById() {
        // Calling the service method
        universiteService.deleteById(1L);

        // Verifying the interaction
        verify(universiteRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete() {
        // Calling the service method
        universiteService.delete(universite);

        // Verifying the interaction
        verify(universiteRepository, times(1)).delete(universite);
    }
}
