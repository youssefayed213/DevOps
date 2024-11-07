package tn.esprit.spring.RestControllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.DAO.Entities.Reservation;
import tn.esprit.spring.Services.Reservation.IReservationService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(ReservationRestController.class)
public class ReservationRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IReservationService reservationService;

    @Autowired
    private ObjectMapper objectMapper;

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        reservation = new Reservation();
        reservation.setIdReservation("1");
        reservation.setAnneeUniversitaire(LocalDate.of(2024, 9, 1));
        reservation.setEstValide(true);
    }

    @Test
    void addOrUpdateReservationTest() throws Exception {
        // Arrange
        when(reservationService.addOrUpdate(any(Reservation.class))).thenReturn(reservation);

        // Act & Assert
        mockMvc.perform(post("/reservation/addOrUpdate")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(reservation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idReservation").value("1"))
                .andExpect(jsonPath("$.anneeUniversitaire").value("2024-09-01"))
                .andExpect(jsonPath("$.estValide").value(true));

        verify(reservationService, times(1)).addOrUpdate(any(Reservation.class));
    }

    @Test
    void findAllReservationsTest() throws Exception {
        // Arrange
        List<Reservation> reservations = Arrays.asList(reservation);
        when(reservationService.findAll()).thenReturn(reservations);

        // Act & Assert
        mockMvc.perform(get("/reservation/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idReservation").value("1"))
                .andExpect(jsonPath("$[0].anneeUniversitaire").value("2024-09-01"))
                .andExpect(jsonPath("$[0].estValide").value(true));

        verify(reservationService, times(1)).findAll();
    }

    @Test
    void findReservationByIdTest() throws Exception {
        // Arrange
        when(reservationService.findById("1")).thenReturn(reservation);

        // Act & Assert
        mockMvc.perform(get("/reservation/findById")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idReservation").value("1"))
                .andExpect(jsonPath("$.anneeUniversitaire").value("2024-09-01"))
                .andExpect(jsonPath("$.estValide").value(true));

        verify(reservationService, times(1)).findById("1");
    }

    @Test
    void deleteReservationTest() throws Exception {
        // Act
        mockMvc.perform(delete("/reservation/delete")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(reservation)))
                .andExpect(status().isOk());

        // Assert
        verify(reservationService, times(1)).delete(any(Reservation.class));
    }

    @Test
    void deleteReservationByIdTest() throws Exception {
        // Act
        mockMvc.perform(delete("/reservation/deleteById")
                        .param("id", "1"))
                .andExpect(status().isOk());

        // Assert
        verify(reservationService, times(1)).deleteById("1");
    }

    @Test
    void ajouterReservationEtAssignerAChambreEtAEtudiantTest() throws Exception {
        // Arrange
        long numChambre = 101L;
        long cin = 123456L;
        when(reservationService.ajouterReservationEtAssignerAChambreEtAEtudiant(numChambre, cin))
                .thenReturn(reservation);

        // Act & Assert
        mockMvc.perform(post("/reservation/ajouterReservationEtAssignerAChambreEtAEtudiant")
                        .param("numChambre", String.valueOf(numChambre))
                        .param("cin", String.valueOf(cin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idReservation").value("1"));

        verify(reservationService, times(1)).ajouterReservationEtAssignerAChambreEtAEtudiant(numChambre, cin);
    }

    @Test
    void getReservationParAnneeUniversitaireTest() throws Exception {
        // Arrange
        LocalDate debutAnnee = LocalDate.of(2024, 9, 1);
        LocalDate finAnnee = LocalDate.of(2025, 6, 30);
        long expectedCount = 5L;
        when(reservationService.getReservationParAnneeUniversitaire(debutAnnee, finAnnee))
                .thenReturn(expectedCount);

        // Act & Assert
        mockMvc.perform(get("/reservation/getReservationParAnneeUniversitaire")
                        .param("debutAnnee", debutAnnee.toString())
                        .param("finAnnee", finAnnee.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(expectedCount)));

        verify(reservationService, times(1)).getReservationParAnneeUniversitaire(debutAnnee, finAnnee);
    }

    @Test
    void annulerReservationTest() throws Exception {
        // Arrange
        long cinEtudiant = 123456L;
        String expectedResponse = "Reservation cancelled successfully";
        when(reservationService.annulerReservation(cinEtudiant)).thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(delete("/reservation/annulerReservation")
                        .param("cinEtudiant", String.valueOf(cinEtudiant)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));

        verify(reservationService, times(1)).annulerReservation(cinEtudiant);
    }
}
