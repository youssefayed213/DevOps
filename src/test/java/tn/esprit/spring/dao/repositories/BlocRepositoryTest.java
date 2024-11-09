package tn.esprit.spring.dao.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.spring.dao.entities.Bloc;
import tn.esprit.spring.dao.entities.TypeChambre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Transactional
public class BlocRepositoryTest {

    private final BlocRepository blocRepository;

    private static final String TEST_BLOC_NAME = "TestBloc";
    private static final String UPDATED_BLOC_NAME = "UpdatedBloc";

    private static final String UPDATED_BLOC_SQL = "UpdatedBlocSQL";
    // Constructor injection
    @Autowired
    public BlocRepositoryTest(BlocRepository blocRepository) {
        this.blocRepository = blocRepository;
    }



    @BeforeEach
    public void setUp() {
        // Initialize test data
        Bloc testBloc = new Bloc();
        testBloc.setNomBloc(TEST_BLOC_NAME);
        testBloc.setCapaciteBloc(5);
        blocRepository.save(testBloc);
    }

    @Test
    public void testFindByNomBloc() {
        Bloc foundBloc = blocRepository.findByNomBloc(TEST_BLOC_NAME);
        assertNotNull(foundBloc);
        assertEquals(TEST_BLOC_NAME, foundBloc.getNomBloc());
    }

    @Test
    public void testSelectByNomBJPQL1() {
        Bloc foundBloc = blocRepository.selectByNomBJPQL1(TEST_BLOC_NAME);
        assertNotNull(foundBloc);
        assertEquals(TEST_BLOC_NAME, foundBloc.getNomBloc());
    }

    @Test
    public void testSelectByNomBJPQL2() {
        Bloc foundBloc = blocRepository.selectByNomBJPQL2(TEST_BLOC_NAME);
        assertNotNull(foundBloc);
        assertEquals(TEST_BLOC_NAME, foundBloc.getNomBloc());
    }

    @Test
    public void testSelectByNomBSQL1() {
        Bloc foundBloc = blocRepository.selectByNomBSQL1(TEST_BLOC_NAME);
        assertNotNull(foundBloc);
        assertEquals(TEST_BLOC_NAME, foundBloc.getNomBloc());
    }

    @Test
    public void testSelectByNomBSQL2() {
        Bloc foundBloc = blocRepository.selectByNomBSQL2(TEST_BLOC_NAME);
        assertNotNull(foundBloc);
        assertEquals(TEST_BLOC_NAME, foundBloc.getNomBloc());
    }


}
