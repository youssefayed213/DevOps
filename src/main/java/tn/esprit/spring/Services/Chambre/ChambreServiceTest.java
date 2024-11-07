package tn.esprit.spring.Services.Chambre;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.DAO.Entities.*;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ChambreServiceTest {

    @Mock
    private ChambreRepository chambreRepository;

    @Mock
    private BlocRepository blocRepository;

    @InjectMocks
    private ChambreService chambreService;

    @Test
    void testAddOrUpdate() {
        // Arrange
        Chambre chambre = new Chambre();
        when(chambreRepository.save(chambre)).thenReturn(chambre);

        // Act
        Chambre result = chambreService.addOrUpdate(chambre);

        // Assert
        assertNotNull(result);
        verify(chambreRepository, times(1)).save(chambre);
    }

    @Test
    void testFindAll() {
        // Arrange
        Chambre chambre1 = new Chambre();
        Chambre chambre2 = new Chambre();
        when(chambreRepository.findAll()).thenReturn(Arrays.asList(chambre1, chambre2));

        // Act
        List<Chambre> result = chambreService.findAll();

        // Assert
        assertEquals(2, result.size());
        verify(chambreRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        // Arrange
        Chambre chambre = new Chambre();
        when(chambreRepository.findById(1L)).thenReturn(Optional.of(chambre));

        // Act
        Chambre result = chambreService.findById(1L);

        // Assert
        assertNotNull(result);
        verify(chambreRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteById() {
        // Act
        chambreService.deleteById(1L);

        // Assert
        verify(chambreRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete() {
        // Arrange
        Chambre chambre = new Chambre();

        // Act
        chambreService.delete(chambre);

        // Assert
        verify(chambreRepository, times(1)).delete(chambre);
    }

    @Test
    void testGetChambresParNomBloc() {
        // Arrange
        Chambre chambre = new Chambre();
        when(chambreRepository.findByBlocNomBloc("BlocA")).thenReturn(Arrays.asList(chambre));

        // Act
        List<Chambre> result = chambreService.getChambresParNomBloc("BlocA");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(chambreRepository, times(1)).findByBlocNomBloc("BlocA");
    }

    @Test
    void testNbChambreParTypeEtBloc() {
        // Arrange
        when(chambreRepository.countByTypeCAndBlocIdBloc(TypeChambre.SIMPLE, 1L)).thenReturn(5);

        // Act
        long result = chambreService.nbChambreParTypeEtBloc(TypeChambre.SIMPLE, 1L);

        // Assert
        assertEquals(5L, result);
        verify(chambreRepository, times(1)).countByTypeCAndBlocIdBloc(TypeChambre.SIMPLE, 1L);
    }

    @Test
    void testGetChambresNonReserveParNomFoyerEtTypeChambre_EmptyResult() {
        // Setup test case where no non-reserved chambres match
        List<Chambre> result = chambreService.getChambresNonReserveParNomFoyerEtTypeChambre("NonExistentFoyer", TypeChambre.SIMPLE);
        assertTrue(result.isEmpty());
    }

    @Test
    void testListeChambresParBloc() {
        // Arrange
        Bloc bloc = new Bloc();
        bloc.setNomBloc("BlocA");
        bloc.setCapaciteBloc(100);
        Chambre chambre1 = new Chambre();
        chambre1.setNumeroChambre(1L);
        chambre1.setTypeC(TypeChambre.SIMPLE);
        bloc.setChambres(Arrays.asList(chambre1));
        when(blocRepository.findAll()).thenReturn(Arrays.asList(bloc));

        // Act
        chambreService.listeChambresParBloc();

        // Assert
        verify(blocRepository, times(1)).findAll();  // Verify that findAll was called once
        // Optionally verify other side effects here (e.g., check if certain methods were called)
    }


    @Test
    void testPourcentageChambreParTypeChambre() {
        // Arrange
        when(chambreRepository.count()).thenReturn(100L);
        when(chambreRepository.countChambreByTypeC(TypeChambre.SIMPLE)).thenReturn(30L);
        when(chambreRepository.countChambreByTypeC(TypeChambre.DOUBLE)).thenReturn(50L);
        when(chambreRepository.countChambreByTypeC(TypeChambre.TRIPLE)).thenReturn(20L);

        // Act
        chambreService.pourcentageChambreParTypeChambre();

        // Assert
        verify(chambreRepository, times(1)).count();
        verify(chambreRepository, times(1)).countChambreByTypeC(TypeChambre.SIMPLE);
        verify(chambreRepository, times(1)).countChambreByTypeC(TypeChambre.DOUBLE);
        verify(chambreRepository, times(1)).countChambreByTypeC(TypeChambre.TRIPLE);
    }

    @Test
    void testNbPlacesDisponibleParChambreAnneeEnCours() {
        // Arrange: Stub the repository to return some data for the chambres
        Chambre chambre1 = new Chambre();
        chambre1.setIdChambre(1L);
        chambre1.setTypeC(TypeChambre.SIMPLE);
        when(chambreRepository.findAll()).thenReturn(Arrays.asList(chambre1));

        // Mock the countReservationsByIdChambreAndReservationsEstValideAndReservationsAnneeUniversitaireBetween method
        when(chambreRepository.countReservationsByIdChambreAndReservationsEstValideAndReservationsAnneeUniversitaireBetween(
                1L, true, LocalDate.of(2024, 9, 15), LocalDate.of(2025, 6, 30)
        )).thenReturn(0L);  // Assume no reservations for this chambre

        // Act: Call the method
        chambreService.nbPlacesDisponibleParChambreAnneeEnCours();

        // Assert: Verify that the correct repository methods were called
        verify(chambreRepository, times(1)).findAll();
        verify(chambreRepository, times(1)).countReservationsByIdChambreAndReservationsEstValideAndReservationsAnneeUniversitaireBetween(
                1L, true, LocalDate.of(2024, 9, 15), LocalDate.of(2025, 6, 30)
        );
    }




}
