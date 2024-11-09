package tn.esprit.spring.services.reservation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.dao.entities.Chambre;
import tn.esprit.spring.dao.entities.Etudiant;
import tn.esprit.spring.dao.entities.Reservation;
import tn.esprit.spring.dao.entities.TypeChambre;
import tn.esprit.spring.dao.repositories.ChambreRepository;
import tn.esprit.spring.dao.repositories.EtudiantRepository;
import tn.esprit.spring.dao.repositories.ReservationRepository;

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

    private Reservation reservation;

    private static final String IDRes = "2023/2024-Bloc A-101-123456789";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock Chambre
        chambre = new Chambre();
        chambre.setIdChambre(1L);
        chambre.setNumeroChambre(101);
        chambre.setTypeC(TypeChambre.SIMPLE);

        Etudiant etudiant = new Etudiant();
        etudiant.setCin(123456789);

        // Mock Reservation
        reservation = new Reservation();
        reservation.setIdReservation(IDRes);
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
        assertFalse(reservations.isEmpty());
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(reservationRepository.findById(IDRes)).thenReturn(Optional.of(reservation));

        Reservation foundReservation = reservationService.findById(IDRes);

        assertNotNull(foundReservation);
        assertEquals(reservation.getIdReservation(), foundReservation.getIdReservation());
        verify(reservationRepository, times(1)).findById(IDRes);
    }

    @Test
    void testDeleteById() {
        doNothing().when(reservationRepository).deleteById(any());

        reservationService.deleteById(IDRes);

        verify(reservationRepository, times(1)).deleteById(IDRes);
    }

    @Test
    void testDelete() {
        doNothing().when(reservationRepository).delete(any(Reservation.class));

        reservationService.delete(reservation);

        verify(reservationRepository, times(1)).delete(reservation);
    }


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
        when(reservationRepository.findById(IDRes)).thenReturn(Optional.of(reservation));
        when(chambreRepository.findById(1L)).thenReturn(Optional.of(chambre));

        reservationService.affectReservationAChambre(IDRes, 1L);

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
