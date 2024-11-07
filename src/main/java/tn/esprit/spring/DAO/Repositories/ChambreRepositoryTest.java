package tn.esprit.spring.DAO.Repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.Reservation;
import tn.esprit.spring.DAO.Entities.TypeChambre;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ChambreRepositoryTest {

    @Autowired
    private ChambreRepository chambreRepository;

    @Autowired
    private BlocRepository blocRepository; // Assuming you have a BlocRepository for setting up related data

    private Bloc testBloc;
    private Chambre testChambre;

    @BeforeEach
    void setUp() {
        // Set up a Bloc entity
        testBloc = new Bloc();
        testBloc.setNomBloc("TestBloc");
        testBloc.setCapaciteBloc(15);
        testBloc = blocRepository.save(testBloc);

        // Set up a Chambre entity
        testChambre = new Chambre();
        testChambre.setNumeroChambre(101);
        testChambre.setBloc(testBloc);
        testChambre.setTypeC(TypeChambre.SIMPLE);
        chambreRepository.save(testChambre);
    }

    @Test
    public void testFindByNumeroChambre() {
        Chambre chambre = chambreRepository.findByNumeroChambre(101);
        assertNotNull(chambre);
        assertEquals(101, chambre.getNumeroChambre());
    }

    @Test
    public void testFindByBlocNomBloc() {
        List<Chambre> chambres = chambreRepository.findByBlocNomBloc("TestBloc");
        assertFalse(chambres.isEmpty());
        assertEquals("TestBloc", chambres.get(0).getBloc().getNomBloc());
    }

    @Test
    public void testCountByTypeCAndBlocIdBloc() {
        int count = chambreRepository.countByTypeCAndBlocIdBloc(TypeChambre.SIMPLE, testBloc.getIdBloc());
        assertEquals(1, count);
    }

    @Test
    public void testListerReservationPourUneChambre() {
        // Set up a reservation with dates in the range
        LocalDate start = LocalDate.of(2023, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 1);
        int reservationCount = chambreRepository.listerReservationPourUneChambre(testChambre.getIdChambre(), start, end);
        assertEquals(0, reservationCount); // Assuming no reservations are present initially
    }

    @Test
    public void testCountReservationsByIdChambreAndReservationsAnneeUniversitaireBetween() {
        LocalDate start = LocalDate.of(2023, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 1);
        int reservationCount = chambreRepository.countReservationsByIdChambreAndReservationsAnneeUniversitaireBetween(testChambre.getIdChambre(), start, end);
        assertEquals(0, reservationCount); // Assuming no reservations are present initially
    }


    @Test
    public void testCount() {
        long count = chambreRepository.count();
        assertEquals(1, count); // Assuming only one chambre set up in @BeforeEach
    }

    @Test
    public void testCountChambreByTypeC() {
        long count = chambreRepository.countChambreByTypeC(TypeChambre.SIMPLE);
        assertEquals(1, count); // Assuming one chambre with type SINGLE in the setup
    }

    @Test
    public void testCountReservationsByIdChambreAndReservationsEstValideAndReservationsAnneeUniversitaireBetween() {
        LocalDate start = LocalDate.of(2023, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 1);
        int reservationCount = (int) chambreRepository.countReservationsByIdChambreAndReservationsEstValideAndReservationsAnneeUniversitaireBetween(testChambre.getIdChambre(), true, start, end);
        assertEquals(0, reservationCount); // Assuming no reservations are present initially
    }
}
