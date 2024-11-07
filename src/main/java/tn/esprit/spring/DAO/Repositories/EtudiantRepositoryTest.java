package tn.esprit.spring.DAO.Repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.DAO.Entities.Reservation;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class EtudiantRepositoryTest {

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private Etudiant testEtudiant;
    private Reservation testReservation;

    @BeforeEach
    void setUp() {
        // Set up a new Etudiant entity
        testEtudiant = new Etudiant();
        testEtudiant.setCin(123456789);
        testEtudiant.setNomEt("TestNom");
        testEtudiant.setPrenomEt("TestPrenom");
        testEtudiant.setEcole("TestEcole");
        testEtudiant.setDateNaissance(LocalDate.of(1999, 1, 1));
        etudiantRepository.save(testEtudiant);

        // Set up a Reservation entity (with estValide set to true)
        testReservation = new Reservation();
        testReservation.setIdReservation("R001");
        testReservation.setAnneeUniversitaire(LocalDate.of(2023, 9, 1));
        testReservation.setEstValide(true);
        testReservation.getEtudiants().add(testEtudiant);
        reservationRepository.save(testReservation);
    }

    /*@Test
    public void testE5erMethodeSQL() {
        // Use the custom query to get the list of students with valid reservations
        List<Etudiant> etudiants = etudiantRepository.e5erMethodeSQL(true);

        // Assert that the result is not empty and contains the test student
        assertFalse(etudiants.isEmpty());
        assertEquals(1, etudiants.size());
        assertEquals("TestNom", etudiants.get(0).getNomEt());
        assertTrue(etudiants.get(0).getReservations().get(0).isEstValide());
    }*/

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
