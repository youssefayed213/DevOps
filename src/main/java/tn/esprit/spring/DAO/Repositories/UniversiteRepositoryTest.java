package tn.esprit.spring.DAO.Repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.spring.DAO.Entities.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UniversiteRepositoryTest {

    @Autowired
    private UniversiteRepository universiteRepository;

    @Autowired
    private FoyerRepository foyerRepository;

    @Autowired
    private BlocRepository blocRepository;

    @Autowired
    private ChambreRepository chambreRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @BeforeEach
    void setUp() {
        // Create and save Universite
        Universite universite = new Universite();
        universite.setNomUniversite("TestUniversite");
        universite = universiteRepository.save(universite);

        // Create and save Foyer linked to Universite
        Foyer foyer = new Foyer();
        foyer.setNomFoyer("TestFoyer");
        foyer.setUniversite(universite);
        foyer = foyerRepository.save(foyer);

        // Create and save Bloc linked to Foyer
        Bloc bloc = new Bloc();
        bloc.setNomBloc("TestBloc");
        bloc.setFoyer(foyer);
        bloc = blocRepository.save(bloc);

        // Create and save Chambre linked to Bloc
        Chambre chambre = new Chambre();
        chambre.setNumeroChambre(101);
        chambre.setBloc(bloc);
        chambre = chambreRepository.save(chambre);

        // Create and save Etudiant linked to Reservation
        Etudiant etudiant = new Etudiant();
        etudiant.setCin(123456L);
        etudiant.setNomEt("Smith"); // Set a different name
        etudiant.setDateNaissance(LocalDate.of(1998, 1, 1)); // Set a date outside the range
        etudiant = etudiantRepository.save(etudiant);

        // Create and save Reservation linked to Etudiant and Chambre
        Reservation reservation = new Reservation();
        reservation.setIdReservation("res1");
        reservation.setAnneeUniversitaire(LocalDate.of(2023, 1, 1));
        reservation.setEstValide(true);
        reservation.getEtudiants().add(etudiant);
        reservationRepository.save(reservation);

        // Associate Reservation with Chambre
        chambre.getReservations().add(reservation);
        chambreRepository.save(chambre);
    }

    @Test
    public void testFindByNomUniversite() {
        Universite universite = universiteRepository.findByNomUniversite("TestUniversite");
        assertNotNull(universite);
        assertEquals("TestUniversite", universite.getNomUniversite());
    }

    @Test
    public void testFindByFoyerBlocsChambresReservationsEtudiantsNomEtLikeAndFoyerBlocsChambresReservationsEtudiantsDateNaissanceBetween() {
        // Use a date range and name that do not match the setup
        List<Universite> universities = universiteRepository.findByFoyerBlocsChambresReservationsEtudiantsNomEtLikeAndFoyerBlocsChambresReservationsEtudiantsDateNaissanceBetween(
                "%Doe%", LocalDate.of(2000, 1, 1), LocalDate.of(2001, 1, 1));

        // Verify that no results are returned
        assertTrue(universities.isEmpty());
    }
}
