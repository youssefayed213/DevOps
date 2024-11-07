package tn.esprit.spring.DAO.Repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.spring.DAO.Entities.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest

public class FoyerRepositoryTest {

    @Autowired
    private FoyerRepository foyerRepository;

    @Autowired
    private BlocRepository blocRepository;

    @Autowired
    private UniversiteRepository universiteRepository;

    private Foyer testFoyer;
    private Bloc testBloc;
    private Chambre testChambre;

    @BeforeEach
    void setUp() {
        // Create Universite
        Universite universite = new Universite();
        universite.setNomUniversite("TestUniversite");
        universite = universiteRepository.save(universite);

        // Set up a Foyer entity and associate it with Universite
        testFoyer = new Foyer();
        testFoyer.setNomFoyer("TestFoyer");
        testFoyer.setCapaciteFoyer(100);
        testFoyer.setUniversite(universite);
        testFoyer = foyerRepository.save(testFoyer);

        // Set up a Bloc entity and associate it with Foyer
        testBloc = new Bloc();
        testBloc.setNomBloc("TestBloc");
        testBloc.setCapaciteBloc(20);
        testBloc.setFoyer(testFoyer);
        testBloc = blocRepository.save(testBloc);

        // Set up and save a Chambre entity associated with Bloc
        testChambre = new Chambre();
        testChambre.setNumeroChambre(101);
        testChambre.setBloc(testBloc);
        testChambre.setTypeC(TypeChambre.SIMPLE);
        testBloc.getChambres().add(testChambre); // Add chambre to bloc
    }

    @Test
    public void testFindByNomFoyer() {
        Foyer foyer = foyerRepository.findByNomFoyer("TestFoyer");
        assertNotNull(foyer);
        assertEquals("TestFoyer", foyer.getNomFoyer());
    }

    @Test
    public void testFindByCapaciteFoyerGreaterThan() {
        List<Foyer> foyers = foyerRepository.findByCapaciteFoyerGreaterThan(50);
        assertFalse(foyers.isEmpty());
        assertTrue(foyers.stream().anyMatch(f -> f.getCapaciteFoyer() > 50));
    }

    @Test
    public void testFindByCapaciteFoyerLessThan() {
        List<Foyer> foyers = foyerRepository.findByCapaciteFoyerLessThan(150);
        assertFalse(foyers.isEmpty());
        assertTrue(foyers.stream().anyMatch(f -> f.getCapaciteFoyer() < 150));
    }

    @Test
    public void testFindByCapaciteFoyerBetween() {
        List<Foyer> foyers = foyerRepository.findByCapaciteFoyerBetween(50, 150);
        assertFalse(foyers.isEmpty());
        assertTrue(foyers.stream().allMatch(f -> f.getCapaciteFoyer() >= 50 && f.getCapaciteFoyer() <= 150));
    }

    @Test
    public void testFindByUniversiteNomUniversite() {
        Foyer foyer = foyerRepository.findByUniversiteNomUniversite("TestUniversite"); // Correct name
        assertNotNull(foyer);
        assertEquals("TestFoyer", foyer.getNomFoyer());
    }

    @Test
    public void testGetByBlocsChambresTypeC() {
        // Ensure chambres are correctly saved in relation with bloc and foyer
        List<Foyer> foyers = foyerRepository.getByBlocsChambresTypeC(TypeChambre.SIMPLE);
        assertFalse(foyers.isEmpty());
        assertTrue(foyers.stream().anyMatch(f -> f.getBlocs().stream()
                .anyMatch(b -> b.getChambres().stream()
                        .anyMatch(c -> c.getTypeC() == TypeChambre.SIMPLE))));
    }
}
