package tn.esprit.spring.DAO.Repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.TypeChambre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Transactional
public class BlocRepositoryTest {

    @Autowired
    private BlocRepository blocRepository;

    private Bloc testBloc;

    @BeforeEach
    public void setUp() {
        // Initialize test data
        testBloc = new Bloc();
        testBloc.setNomBloc("TestBloc");
        testBloc.setCapaciteBloc(5);
        blocRepository.save(testBloc);
    }

    @Test
    public void testFindByNomBloc() {
        Bloc foundBloc = blocRepository.findByNomBloc("TestBloc");
        assertNotNull(foundBloc);
        assertEquals("TestBloc", foundBloc.getNomBloc());
    }

    @Test
    public void testSelectByNomBJPQL1() {
        Bloc foundBloc = blocRepository.selectByNomBJPQL1("TestBloc");
        assertNotNull(foundBloc);
        assertEquals("TestBloc", foundBloc.getNomBloc());
    }

    @Test
    public void testSelectByNomBJPQL2() {
        Bloc foundBloc = blocRepository.selectByNomBJPQL2("TestBloc");
        assertNotNull(foundBloc);
        assertEquals("TestBloc", foundBloc.getNomBloc());
    }

    @Test
    public void testSelectByNomBSQL1() {
        Bloc foundBloc = blocRepository.selectByNomBSQL1("TestBloc");
        assertNotNull(foundBloc);
        assertEquals("TestBloc", foundBloc.getNomBloc());
    }

    @Test
    public void testSelectByNomBSQL2() {
        Bloc foundBloc = blocRepository.selectByNomBSQL2("TestBloc");
        assertNotNull(foundBloc);
        assertEquals("TestBloc", foundBloc.getNomBloc());
    }

    @Test
    @Rollback
    public void testUpdateBlocJPQL() {
        blocRepository.updateBlocJPQL("UpdatedBloc");
        Bloc updatedBloc = blocRepository.findByNomBloc("UpdatedBloc");
        assertNotNull(updatedBloc);
        assertEquals("UpdatedBloc", updatedBloc.getNomBloc());
    }

    @Test
    @Rollback
    public void testUpdateBlocSQL() {
        blocRepository.updateBlocSQL("UpdatedBlocSQL");
        Bloc updatedBloc = blocRepository.findByNomBloc("UpdatedBlocSQL");
        assertNotNull(updatedBloc);
        assertEquals("UpdatedBlocSQL", updatedBloc.getNomBloc());
    }

    @Test
    public void testSelectBlocsByTypeChambreJPQL() {
        List<Bloc> blocs = blocRepository.selectBlocsByTypeChambreJPQL(TypeChambre.TRIPLE);
        assertNotNull(blocs);
        assertEquals(1, blocs.size());
        assertEquals("TestBloc", blocs.get(0).getNomBloc());
    }
}
