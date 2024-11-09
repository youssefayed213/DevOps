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
public class EtudiantRepositoryTest {


    private final EtudiantRepository etudiantRepository;

    private final ReservationRepository reservationRepository;


    @Autowired
    public EtudiantRepositoryTest(EtudiantRepository etudiantRepository, ReservationRepository reservationRepository) {
        this.etudiantRepository = etudiantRepository;
        this.reservationRepository = reservationRepository;
    }

    @BeforeEach
    void setUp() {
        // Set up a new Etudiant entity
        Etudiant testEtudiant = new Etudiant();
        testEtudiant.setCin(123456789);
        testEtudiant.setNomEt("TestNom");
        testEtudiant.setPrenomEt("TestPrenom");
        testEtudiant.setEcole("TestEcole");
        testEtudiant.setDateNaissance(LocalDate.of(1999, 1, 1));
        etudiantRepository.save(testEtudiant);

        // Set up a Reservation entity (with estValide set to true)
        Reservation testReservation = new Reservation();
        testReservation.setIdReservation("R001");
        testReservation.setAnneeUniversitaire(LocalDate.of(2023, 9, 1));
        testReservation.setEstValide(true);
        testReservation.getEtudiants().add(testEtudiant);
        reservationRepository.save(testReservation);
    }



    @Test
    public void testFindByCin() {
        Etudiant etudiant = etudiantRepository.findByCin(123456789);
        assertNotNull(etudiant);
        assertEquals(123456789, etudiant.getCin());
    }

    @Test
    public void testFindByNomEtLike() {
        List<Etudiant> etudiants = etudiantRepository.findByNomEtLike("Test%");
        assertFalse(etudiants.isEmpty());
        assertEquals("TestNom", etudiants.get(0).getNomEt());
    }

    @Test
    public void testCount() {
        long count = etudiantRepository.count();
        assertEquals(1, count); // Assuming one Etudiant was created in @BeforeEach
    }
}
