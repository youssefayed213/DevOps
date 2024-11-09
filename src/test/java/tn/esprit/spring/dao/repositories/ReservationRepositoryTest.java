package tn.esprit.spring.dao.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.spring.dao.entities.Etudiant;
import tn.esprit.spring.dao.entities.Reservation;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ReservationRepositoryTest {


    private final ReservationRepository reservationRepository;

    private final EtudiantRepository etudiantRepository;


    @Autowired
    public ReservationRepositoryTest(ReservationRepository reservationRepository, EtudiantRepository etudiantRepository) {
        this.reservationRepository = reservationRepository;
        this.etudiantRepository = etudiantRepository;
    }

    @BeforeEach
    void setUp() {
        // Create an Etudiant
        Etudiant etudiant = new Etudiant();
        etudiant.setCin(123456L);
        etudiantRepository.save(etudiant);

        // Create and save a Reservation with the Etudiant
        Reservation reservation = new Reservation();
        reservation.setIdReservation("res1");
        reservation.setAnneeUniversitaire(LocalDate.of(2023, 1, 1));
        reservation.setEstValide(true);
        reservation.getEtudiants().add(etudiant);
        reservationRepository.save(reservation);
    }

    @Test
    public void testCountByAnneeUniversitaireBetween() {
        int count = reservationRepository.countByAnneeUniversitaireBetween(LocalDate.of(2022, 1, 1), LocalDate.of(2024, 1, 1));
        assertEquals(1, count);
    }

    @Test
    public void testFindByEtudiantsCinAndEstValide() {
        Reservation foundReservation = reservationRepository.findByEtudiantsCinAndEstValide(123456L, true);
        assertNotNull(foundReservation);
        assertEquals("res1", foundReservation.getIdReservation());
    }

    @Test
    public void testFindByEstValideAndAnneeUniversitaireBetween() {
        List<Reservation> reservations = reservationRepository.findByEstValideAndAnneeUniversitaireBetween(true, LocalDate.of(2022, 1, 1), LocalDate.of(2024, 1, 1));
        assertFalse(reservations.isEmpty());
        assertEquals(1, reservations.size());
    }
}
