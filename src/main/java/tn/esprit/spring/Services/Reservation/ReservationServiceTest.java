package tn.esprit.spring.Services.Reservation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.DAO.Entities.Reservation;
import tn.esprit.spring.DAO.Entities.TypeChambre;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;
import tn.esprit.spring.DAO.Repositories.EtudiantRepository;
import tn.esprit.spring.DAO.Repositories.ReservationRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ChambreRepository chambreRepository;

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Chambre chambre;
    private Etudiant etudiant;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock Chambre
        chambre = new Chambre();
        chambre.setIdChambre(1L);
        chambre.setNumeroChambre(101);
        chambre.setTypeC(TypeChambre.SIMPLE);

        // Mock Etudiant
        etudiant = new Etudiant();
        etudiant.setCin(123456789);

        // Mock Reservation
        reservation = new Reservation();
        reservation.setIdReservation("2023/2024-Bloc A-101-123456789");
        reservation.setAnneeUniversitaire(LocalDate.now());
        reservation.setEstValide(false);
    }

    @Test
    void testAddOrUpdate() {
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation savedReservation = reservationService.addOrUpdate(reservation);

        assertNotNull(savedReservation);
        assertEquals(reservation.getIdReservation(), savedReservation.getIdReservation());
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void testFindAll() {
        when(reservationRepository.findAll()).thenReturn(List.of(reservation));

        var reservations = reservationService.findAll();

        assertNotNull(reservations);
        assertTrue(reservations.size() > 0);
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(reservationRepository.findById("2023/2024-Bloc A-101-123456789")).thenReturn(Optional.of(reservation));

        Reservation foundReservation = reservationService.findById("2023/2024-Bloc A-101-123456789");

        assertNotNull(foundReservation);
        assertEquals(reservation.getIdReservation(), foundReservation.getIdReservation());
        verify(reservationRepository, times(1)).findById("2023/2024-Bloc A-101-123456789");
    }

    @Test
    void testDeleteById() {
        doNothing().when(reservationRepository).deleteById(any());

        reservationService.deleteById("2023/2024-Bloc A-101-123456789");

        verify(reservationRepository, times(1)).deleteById("2023/2024-Bloc A-101-123456789");
    }

    @Test
    void testDelete() {
        doNothing().when(reservationRepository).delete(any(Reservation.class));

        reservationService.delete(reservation);

        verify(reservationRepository, times(1)).delete(reservation);
    }

    /*@Test
    void testAjouterReservationEtAssignerAChambreEtAEtudiant() {
        // Mock Chambre and Etudiant
        when(chambreRepository.findByNumeroChambre(101L)).thenReturn(chambre);
        when(etudiantRepository.findByCin(123456789)).thenReturn(etudiant);
        when(chambreRepository.countReservationsByIdChambreAndReservationsAnneeUniversitaireBetween(
                anyLong(), any(), any())).thenReturn(0); // Assume no reservations

        // Handle the case where the Bloc might be null
        if (chambre.getBloc() != null) {
            when(chambre.getBloc().getNomBloc()).thenReturn("BlocName");
        } else {
            // Mock or handle the case where Bloc is null
            // You can log a warning, use a default name, or perform other actions
            System.out.println("Bloc is null for this Chambre.");
        }

        // Call the method to test
        Reservation addedReservation = reservationService.ajouterReservationEtAssignerAChambreEtAEtudiant(101L, 123456789);

        // Assertions to check if the reservation was added correctly
        assertNotNull(addedReservation);
        assertTrue(addedReservation.getEtudiants().contains(etudiant));

        // Verify the interactions with the repositories
        verify(chambreRepository, times(1)).save(chambre);
        verify(reservationRepository, times(1)).save(addedReservation);
    }*/

    @Test
    void testAnnulerReservation() {
        when(reservationRepository.findByEtudiantsCinAndEstValide(123456789, true)).thenReturn(reservation);
        when(chambreRepository.findByReservationsIdReservation(reservation.getIdReservation())).thenReturn(chambre);

        String result = reservationService.annulerReservation(123456789);

        assertEquals("La réservation 2023/2024-Bloc A-101-123456789 est annulée avec succés", result);
        verify(reservationRepository, times(1)).delete(reservation);
        verify(chambreRepository, times(1)).save(chambre);
    }

    @Test
    void testAffectReservationAChambre() {
        when(reservationRepository.findById("2023/2024-Bloc A-101-123456789")).thenReturn(Optional.of(reservation));
        when(chambreRepository.findById(1L)).thenReturn(Optional.of(chambre));

        reservationService.affectReservationAChambre("2023/2024-Bloc A-101-123456789", 1L);

        verify(chambreRepository, times(1)).save(chambre);
        assertTrue(chambre.getReservations().contains(reservation));
    }

    @Test
    void testAnnulerReservations() {
        when(reservationRepository.findByEstValideAndAnneeUniversitaireBetween(
                eq(true), any(), any())).thenReturn(List.of(reservation));

        reservationService.annulerReservations();

        assertFalse(reservation.isEstValide());
        verify(reservationRepository, times(1)).save(reservation);
    }
}
