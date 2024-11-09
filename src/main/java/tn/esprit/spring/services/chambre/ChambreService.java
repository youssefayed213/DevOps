package tn.esprit.spring.services.chambre;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.spring.dao.entities.Bloc;
import tn.esprit.spring.dao.entities.Chambre;
import tn.esprit.spring.dao.entities.Reservation;
import tn.esprit.spring.dao.entities.TypeChambre;
import tn.esprit.spring.dao.repositories.BlocRepository;
import tn.esprit.spring.dao.repositories.ChambreRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ChambreService implements IChambreService {
    ChambreRepository repo;
    BlocRepository blocRepository;
    private static final  String sentence = "Le nombre de place disponible pour la chambre";

    private static final  String sentence2 = "est complete";

    private static final  String sentence3 = "La chambre";

    @Override
    public Chambre addOrUpdate(Chambre c) {
        return repo.save(c);
    }

    @Override
    public List<Chambre> findAll() {
        return repo.findAll();
    }

    @Override
    public Chambre findById(long id) {
        return repo.findById(id).get();
    }

    @Override
    public void deleteById(long id) {
        repo.deleteById(id);
    }

    @Override
    public void delete(Chambre c) {
        repo.delete(c);
    }

    @Override
    public List<Chambre> getChambresParNomBloc(String nomBloc) {
        return repo.findByBlocNomBloc(nomBloc);
    }

    @Override
    public long nbChambreParTypeEtBloc(TypeChambre type, long idBloc) {
        return repo.countByTypeCAndBlocIdBloc(type, idBloc);
    }

    @Override
    public List<Chambre> getChambresNonReserveParNomFoyerEtTypeChambre(String nomFoyer, TypeChambre type) {
        LocalDate[] academicYearDates = getAcademicYearDates();
        LocalDate dateDebutAU = academicYearDates[0];
        LocalDate dateFinAU = academicYearDates[1];

        List<Chambre> listChambreDispo = new ArrayList<>();

        for (Chambre chambre : repo.findAll()) {
            if (isChambreEligible(chambre, nomFoyer, type, dateDebutAU, dateFinAU)) {
                listChambreDispo.add(chambre);
            }
        }
        return listChambreDispo;
    }

    // Helper method to calculate the start and end dates of the current academic year
    private LocalDate[] getAcademicYearDates() {
        int year = LocalDate.now().getYear() % 100;
        LocalDate dateDebutAU;
        LocalDate dateFinAU;

        if (LocalDate.now().getMonthValue() <= 7) {
            dateDebutAU = LocalDate.of(Integer.parseInt("20" + (year - 1)), 9, 15);
            dateFinAU = LocalDate.of(Integer.parseInt("20" + year), 6, 30);
        } else {
            dateDebutAU = LocalDate.of(Integer.parseInt("20" + year), 9, 15);
            dateFinAU = LocalDate.of(Integer.parseInt("20" + (year + 1)), 6, 30);
        }
        return new LocalDate[]{dateDebutAU, dateFinAU};
    }

    // Helper method to check if a chambre is eligible to be added to the available list
    private boolean isChambreEligible(Chambre chambre, String nomFoyer, TypeChambre type, LocalDate dateDebutAU, LocalDate dateFinAU) {
        if (!chambre.getTypeC().equals(type) || !chambre.getBloc().getFoyer().getNomFoyer().equals(nomFoyer)) {
            return false;
        }

        int numReservations = countValidReservations(chambre, dateDebutAU, dateFinAU);
        return isCapacityAvailable(type, numReservations);
    }

    // Helper method to count valid reservations for a chambre within the academic year
    private int countValidReservations(Chambre chambre, LocalDate dateDebutAU, LocalDate dateFinAU) {
        int numReservations = 0;
        for (Reservation reservation : chambre.getReservations()) {
            if (reservation.getAnneeUniversitaire().isBefore(dateFinAU) && reservation.getAnneeUniversitaire().isAfter(dateDebutAU)) {
                numReservations++;
            }
        }
        return numReservations;
    }

    // Helper method to check if the chambre capacity is available based on the number of reservations
    private boolean isCapacityAvailable(TypeChambre type, int numReservations) {
        switch (type) {
            case SIMPLE:
                return numReservations == 0;
            case DOUBLE:
                return numReservations < 2;
            case TRIPLE:
                return numReservations < 3;
            default:
                return false;
        }
    }


    @Override
    public void listeChambresParBloc() {
        for (Bloc b : blocRepository.findAll()) {
            log.info("Bloc => " + b.getNomBloc() + " ayant une capacité " + b.getCapaciteBloc());
            if (b.getChambres().size() != 0) {
                log.info("La liste des chambres pour ce bloc: ");
                for (Chambre c : b.getChambres()) {
                    log.info("NumChambre: " + c.getNumeroChambre() + " type: " + c.getTypeC());
                }
            } else {
                log.info("Pas de chambre disponible dans ce bloc");
            }
            log.info("********************");
        }
    }

    @Override
    public void pourcentageChambreParTypeChambre() {
        long totalChambre = repo.count();
        double pSimple = (repo.countChambreByTypeC(TypeChambre.SIMPLE) * 100) / totalChambre;
        double pDouble = (repo.countChambreByTypeC(TypeChambre.DOUBLE) * 100) / totalChambre;
        double pTriple = (repo.countChambreByTypeC(TypeChambre.TRIPLE) * 100) / totalChambre;
        log.info("Nombre total des chambre: " + totalChambre);
        log.info("Le pourcentage des chambres pour le type SIMPLE est égale à " + pSimple);
        log.info("Le pourcentage des chambres pour le type DOUBLE est égale à " + pDouble);
        log.info("Le pourcentage des chambres pour le type TRIPLE est égale à " + pTriple);

    }

    @Override
    public void nbPlacesDisponibleParChambreAnneeEnCours() {
        // Début "récuperer l'année universitaire actuelle"
        LocalDate dateDebutAU;
        LocalDate dateFinAU;
        int year = LocalDate.now().getYear() % 100;
        if (LocalDate.now().getMonthValue() <= 7) {
            dateDebutAU = LocalDate.of(Integer.parseInt("20" + (year - 1)), 9, 15);
            dateFinAU = LocalDate.of(Integer.parseInt("20" + year), 6, 30);
        } else {
            dateDebutAU = LocalDate.of(Integer.parseInt("20" + year), 9, 15);
            dateFinAU = LocalDate.of(Integer.parseInt("20" + (year + 1)), 6, 30);
        }
        // Fin "récuperer l'année universitaire actuelle"
        for (Chambre c : repo.findAll()) {
            long nbReservation = repo.countReservationsByIdChambreAndReservationsEstValideAndReservationsAnneeUniversitaireBetween(c.getIdChambre(),true, dateDebutAU, dateFinAU);
            switch (c.getTypeC()) {
                case SIMPLE:
                    if (nbReservation == 0) {
                        log.info(sentence + c.getTypeC() + " " + c.getNumeroChambre() + " est 1 ");
                    } else {
                        log.info(sentence3+ c.getTypeC() + " " + c.getNumeroChambre() + sentence2);
                    }
                    break;
                case DOUBLE:
                    if (nbReservation < 2) {
                        log.info(sentence + c.getTypeC() + " " + c.getNumeroChambre() + " est " + (2 - nbReservation));
                    } else {
                        log.info(sentence3 + c.getTypeC() + " " + c.getNumeroChambre() + sentence2);
                    }
                    break;
                case TRIPLE:
                    if (nbReservation < 3) {
                        log.info(sentence + c.getTypeC() + " " + c.getNumeroChambre() + " est " + (3 - nbReservation));
                    } else {
                        log.info(sentence3 + c.getTypeC() + " " + c.getNumeroChambre() + sentence2);
                    }
            }
        }
    }
}
