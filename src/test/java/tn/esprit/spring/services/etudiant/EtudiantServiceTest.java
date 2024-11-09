package tn.esprit.spring.services.etudiant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.dao.entities.Etudiant;
import tn.esprit.spring.dao.repositories.EtudiantRepository;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EtudiantServiceTest {

    @Mock
    private EtudiantRepository repo;

    @InjectMocks
    private EtudiantService etudiantService;

    private Etudiant etudiant;

    @BeforeEach
    public void setUp() {
        etudiant = new Etudiant();
        etudiant.setIdEtudiant(1L);
        etudiant.setNomEt("John");
        etudiant.setPrenomEt("Doe");
    }

    @Test
    void testAddOrUpdate() {
        // Arrange: mock the save method
        when(repo.save(any(Etudiant.class))).thenReturn(etudiant);

        // Act: add or update the student
        Etudiant result = etudiantService.addOrUpdate(etudiant);

        // Assert: verify the result and interaction with the repository
        assertNotNull(result);
        assertEquals("John", result.getNomEt());
        verify(repo, times(1)).save(etudiant);
    }

    @Test
    void testFindById() {
        // Arrange: mock the findById method
        when(repo.findById(1L)).thenReturn(Optional.of(etudiant));

        // Act: find the student by id
        Etudiant result = etudiantService.findById(1L);

        // Assert: verify the result and interaction with the repository
        assertNotNull(result);
        assertEquals(1L, result.getIdEtudiant());
        verify(repo, times(1)).findById(1L);
    }



    @Test
    void testDeleteById() {
        // Act: delete student by id
        etudiantService.deleteById(1L);

        // Assert: verify that deleteById is called
        verify(repo, times(1)).deleteById(1L);
    }

    @Test
    void testDelete() {
        // Act: delete student
        etudiantService.delete(etudiant);

        // Assert: verify that delete is called
        verify(repo, times(1)).delete(etudiant);
    }
}
