package tn.esprit.spring.Services.Bloc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;
import tn.esprit.spring.DAO.Repositories.FoyerRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class BlocServiceTest {

    @Mock
    private BlocRepository blocRepository;

    @Mock
    private ChambreRepository chambreRepository;

    @Mock
    private FoyerRepository foyerRepository;

    @InjectMocks
    private BlocService blocService;

    @Test
    void testAddOrUpdate2() {
        // Arrange
        Bloc bloc = new Bloc();
        Chambre chambre1 = new Chambre();
        Chambre chambre2 = new Chambre();
        bloc.setChambres(Arrays.asList(chambre1, chambre2));

        // Act
        blocService.addOrUpdate2(bloc);

        // Assert
        verify(chambreRepository, times(2)).save(any(Chambre.class));
    }

    @Test
    void testFindAll() {
        // Arrange
        Bloc bloc1 = new Bloc();
        Bloc bloc2 = new Bloc();
        when(blocRepository.findAll()).thenReturn(Arrays.asList(bloc1, bloc2));

        // Act
        List<Bloc> result = blocService.findAll();

        // Assert
        assertEquals(2, result.size());
        verify(blocRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        // Arrange
        Bloc bloc = new Bloc();
        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc));

        // Act
        Bloc result = blocService.findById(1L);

        // Assert
        assertNotNull(result);
        verify(blocRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteById() {
        // Act
        blocService.deleteById(1L);

        // Assert
        verify(blocRepository, times(1)).deleteById(1L);
    }

    @Test
    void testAffecterChambresABloc() {
        // Arrange
        Bloc bloc = new Bloc();
        when(blocRepository.findByNomBloc("BlocA")).thenReturn(bloc);
        Chambre chambre = new Chambre();
        when(chambreRepository.findByNumeroChambre(1L)).thenReturn(chambre);

        // Act
        blocService.affecterChambresABloc(Arrays.asList(1L), "BlocA");

        // Assert
        verify(chambreRepository, times(1)).save(chambre);
    }

    @Test
    void testAffecterBlocAFoyer() {
        // Arrange
        Bloc bloc = new Bloc();
        Foyer foyer = new Foyer();
        when(blocRepository.findByNomBloc("BlocA")).thenReturn(bloc);
        when(foyerRepository.findByNomFoyer("FoyerA")).thenReturn(foyer);

        // Act
        blocService.affecterBlocAFoyer("BlocA", "FoyerA");

        // Assert
        assertEquals(foyer, bloc.getFoyer());
        verify(blocRepository, times(1)).save(bloc);
    }
}

