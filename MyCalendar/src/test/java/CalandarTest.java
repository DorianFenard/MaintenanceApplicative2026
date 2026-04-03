import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class CalandarTest {

    @Test
    public void testEventDescriptions() {
        LocalDateTime date = LocalDateTime.of(2026, 4, 3, 10, 0);

        Event rdv = new Event("RDV_PERSONNEL", "Dentiste", "Alice", date, 30, "", "", 0);
        assertTrue(rdv.description().contains("RDV : Dentiste à"));

        Event reunion = new Event("REUNION", "Point Projet", "Bob", date, 60, "Salle 1", "Alice, Bob", 0);
        assertEquals("Réunion : Point Projet à Salle 1 avec Alice, Bob", reunion.description());

        Event periodique = new Event("PERIODIQUE", "Sport", "Charlie", date, 0, "", "", 7);
        assertEquals("Événement périodique : Sport tous les 7 jours", periodique.description());
    }

    @Test
    public void testCalendarManagerAjoutEtPeriode() {
        CalendarManager manager = new CalendarManager();
        LocalDateTime debut = LocalDateTime.of(2026, 4, 1, 0, 0);
        LocalDateTime fin = LocalDateTime.of(2026, 4, 30, 23, 59);

        manager.ajouterEvent("RDV_PERSONNEL", "Test1", "User1", LocalDateTime.of(2026, 4, 10, 10, 0), 30, "", "", 0);
        manager.ajouterEvent("PERIODIQUE", "Test2", "User1", LocalDateTime.of(2026, 3, 20, 10, 0), 0, "", "", 10);
        manager.ajouterEvent("RDV_PERSONNEL", "Hors limite", "User1", LocalDateTime.of(2026, 5, 10, 10, 0), 30, "", "", 0);

        assertEquals(3, manager.events.size());

        List<Event> dansPeriode = manager.eventsDansPeriode(debut, fin);
        assertEquals(2, dansPeriode.size());
    }

    @Test
    public void testCalendarManagerConflits() {
        CalendarManager manager = new CalendarManager();

        Event e1 = new Event("RDV_PERSONNEL", "A", "U", LocalDateTime.of(2026, 4, 10, 10, 0), 60, "", "", 0);
        Event e2 = new Event("RDV_PERSONNEL", "B", "U", LocalDateTime.of(2026, 4, 10, 10, 30), 60, "", "", 0);
        Event e3 = new Event("RDV_PERSONNEL", "C", "U", LocalDateTime.of(2026, 4, 10, 11, 30), 60, "", "", 0);
        Event ePerio = new Event("PERIODIQUE", "P", "U", LocalDateTime.of(2026, 4, 10, 10, 30), 60, "", "", 0);

        assertTrue(manager.conflit(e1, e2));

        assertFalse(manager.conflit(e1, e3));

        assertFalse(manager.conflit(e1, ePerio));
    }

    @Test
    public void testAfficherEvenements() {
        java.io.ByteArrayOutputStream consoleSortie = new java.io.ByteArrayOutputStream();
        java.io.PrintStream consoleOriginale = System.out;
        System.setOut(new java.io.PrintStream(consoleSortie));

        try {
            CalendarManager manager = new CalendarManager();
            manager.ajouterEvent("RDV_PERSONNEL", "Dentiste", "User1", LocalDateTime.of(2026, 4, 10, 10, 0), 30, "", "", 0);
            manager.ajouterEvent("PERIODIQUE", "Sport", "User1", LocalDateTime.of(2026, 4, 12, 18, 0), 0, "", "", 7);

            manager.afficherEvenements();

            String affichage = consoleSortie.toString();
            assertTrue(affichage.contains("RDV : Dentiste à"));
            assertTrue(affichage.contains("Événement périodique : Sport tous les 7 jours"));

        } finally {
            System.setOut(consoleOriginale);
        }
    }
}