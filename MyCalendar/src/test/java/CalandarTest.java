import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class CalandarTest {

    @Test
    public void testDisplayEventsEmptyList() {
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        java.io.PrintStream original = System.out;
        System.setOut(new java.io.PrintStream(out));
        try {
            CalendarManager manager = new CalendarManager();
            manager.afficherEvenements();
            assertEquals("", out.toString());
        } finally {
            System.setOut(original);
        }
    }

// --- Main tests via System.in simulation ---

    private String runMainWithInput(String... lines) {
        String input = String.join("\n", lines) + "\n";
        java.io.ByteArrayInputStream in = new java.io.ByteArrayInputStream(input.getBytes());
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        java.io.InputStream originalIn = System.in;
        java.io.PrintStream originalOut = System.out;
        System.setIn(in);
        System.setOut(new java.io.PrintStream(out));
        try {
            Main.main(new String[]{});
        } catch (java.util.NoSuchElementException e) {
            // Expected: scanner runs out of input, loop stops
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
        return out.toString();
    }

    @Test
    public void testMainLoginRogerCorrectPassword() {
        String output = runMainWithInput("1", "Roger", "Chat", "5", "non");
        assertTrue(output.contains("Bonjour, Roger"));
    }

    @Test
    public void testMainLoginRogerWrongPassword() {
        String output = runMainWithInput("1", "Roger", "Wrong", "1", "Roger", "Chat", "5", "non");
        assertTrue(output.contains("Se connecter"));
    }

    @Test
    public void testMainLoginPierreCorrectPassword() {
        String output = runMainWithInput("1", "Pierre", "KiRouhl", "5", "non");
        assertTrue(output.contains("Bonjour, Pierre"));
    }

    @Test
    public void testMainLoginPierreWrongPassword() {
        String output = runMainWithInput("1", "Pierre", "Wrong");
        assertTrue(output.contains("Se connecter"));
    }

    @Test
    public void testMainCreateAccountSuccess() {
        String output = runMainWithInput("2", "Test", "abc", "abc", "5", "non");
        assertTrue(output.contains("Bonjour, Test"));
    }

    @Test
    public void testMainCreateAccountPasswordMismatch() {
        String output = runMainWithInput("2", "Test", "abc", "xyz");
        assertTrue(output.contains("Les mots de passes ne correspondent pas"));
    }

    @Test
    public void testMainCreateAccountThenReconnect() {
        String output = runMainWithInput(
                "2", "Alice", "pass", "pass",
                "5", "oui",
                "1", "Alice", "pass",
                "5", "non"
        );
        assertTrue(output.contains("Bonjour, Alice"));
    }

    @Test
    public void testMainLoginUnknownUserEntersMenu() {
        String output = runMainWithInput("1", "Unknown", "mdp");
        assertTrue(output.contains("Bonjour, Unknown"));
    }

    @Test
    public void testMainViewAllEvents() {
        String output = runMainWithInput(
                "1", "Roger", "Chat",
                "1", "1",
                "5", "non"
        );
        assertTrue(output.contains("Menu de visualisation"));
    }

    @Test
    public void testMainViewEventsByMonth() {
        String output = runMainWithInput(
                "1", "Roger", "Chat",
                "2", "Test", "2026", "4", "10", "10", "0", "30",
                "1", "2", "2026", "4",
                "5", "non"
        );
        assertTrue(output.contains("Événements trouvés"));
    }

    @Test
    public void testMainViewEventsByWeek() {
        String output = runMainWithInput(
                "1", "Roger", "Chat",
                "2", "Test", "2026", "4", "10", "10", "0", "30",
                "1", "3", "2026", "15",
                "5", "non"
        );
        assertTrue(output.contains("Menu de visualisation"));
    }

    @Test
    public void testMainViewEventsByDay() {
        String output = runMainWithInput(
                "1", "Roger", "Chat",
                "2", "Test", "2026", "4", "10", "10", "0", "30",
                "1", "4", "2026", "4", "10",
                "5", "non"
        );
        assertTrue(output.contains("Événements trouvés"));
    }

    @Test
    public void testMainViewEventsSubMenuReturn() {
        String output = runMainWithInput(
                "1", "Roger", "Chat",
                "1", "5",
                "5", "non"
        );
        assertTrue(output.contains("Menu de visualisation"));
    }

    @Test
    public void testMainAddPersonalAppointment() {
        String output = runMainWithInput(
                "1", "Roger", "Chat",
                "2", "Dentist", "2026", "4", "15", "14", "30", "45",
                "5", "non"
        );
        assertTrue(output.contains("Événement ajouté"));
    }

    @Test
    public void testMainAddMeetingWithExtraParticipant() {
        String output = runMainWithInput(
                "1", "Roger", "Chat",
                "3", "Standup", "2026", "4", "15", "9", "0", "30", "Room A",
                "oui", "Bob",
                "non",
                "5", "non"
        );
        assertTrue(output.contains("Événement ajouté"));
    }

    @Test
    public void testMainAddMeetingNoExtraParticipant() {
        String output = runMainWithInput(
                "1", "Roger", "Chat",
                "3", "Standup", "2026", "4", "15", "9", "0", "30", "Room A",
                "non",
                "5", "non"
        );
        assertTrue(output.contains("Événement ajouté"));
    }

    @Test
    public void testMainAddPeriodicEvent() {
        String output = runMainWithInput(
                "1", "Roger", "Chat",
                "4", "Sport", "2026", "4", "1", "18", "0", "7",
                "5", "non"
        );
        assertTrue(output.contains("Événement ajouté"));
    }

    @Test
    public void testMainDisconnectAndContinue() {
        String output = runMainWithInput(
                "1", "Roger", "Chat",
                "5", "oui",
                "1", "Pierre", "KiRouhl",
                "5", "non"
        );
        assertTrue(output.contains("Bonjour, Roger"));
        assertTrue(output.contains("Bonjour, Pierre"));
    }

    @Test
    public void testMainViewEventsByDayNoResults() {
        String output = runMainWithInput(
                "1", "Roger", "Chat",
                "1", "4", "2026", "12", "25",
                "5", "non"
        );
        assertTrue(output.contains("Aucun événement trouvé"));
    }

    @Test
    public void testMainViewEventsByMonthNoResults() {
        String output = runMainWithInput(
                "1", "Roger", "Chat",
                "1", "2", "2026", "12",
                "5", "non"
        );
        assertTrue(output.contains("Aucun événement trouvé"));
    }

    @Test
    public void testEventDescriptionUnknownType() {
        Event e = new Event("UNKNOWN", "X", "U", LocalDateTime.of(2026, 4, 3, 10, 0), 30, "", "", 0);
        assertEquals("", e.description());
    }

    @Test
    public void testPeriodicEventNoOccurrenceInPeriod() {
        CalendarManager manager = new CalendarManager();
        manager.ajouterEvent("PERIODIQUE", "Future", "U",
                LocalDateTime.of(2026, 6, 1, 10, 0), 0, "", "", 7);

        LocalDateTime start = LocalDateTime.of(2026, 4, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2026, 4, 30, 23, 59);

        List<Event> result = manager.eventsDansPeriode(start, end);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testConflictExactBoundary() {
        CalendarManager manager = new CalendarManager();
        Event e1 = new Event("RDV_PERSONNEL", "A", "U",
                LocalDateTime.of(2026, 4, 10, 10, 0), 60, "", "", 0);
        Event e2 = new Event("RDV_PERSONNEL", "B", "U",
                LocalDateTime.of(2026, 4, 10, 11, 0), 60, "", "", 0);

        assertFalse(manager.conflit(e1, e2));
    }

    @Test
    public void testConflictBothDirections() {
        CalendarManager manager = new CalendarManager();
        Event e1 = new Event("RDV_PERSONNEL", "A", "U",
                LocalDateTime.of(2026, 4, 10, 10, 0), 60, "", "", 0);
        Event ePeriodic = new Event("PERIODIQUE", "P", "U",
                LocalDateTime.of(2026, 4, 10, 10, 0), 60, "", "", 7);

        assertFalse(manager.conflit(e1, ePeriodic));
        assertFalse(manager.conflit(ePeriodic, e1));
    }

    @Test
    public void testCalendarManagerInitiallyEmpty() {
        CalendarManager manager = new CalendarManager();
        assertTrue(manager.events.isEmpty());
    }

    @Test
    public void testEventConstructorStoresAllFields() {
        LocalDateTime date = LocalDateTime.of(2026, 4, 3, 14, 30);
        Event e = new Event("REUNION", "Standup", "Alice", date, 15, "Room B", "Alice, Bob", 0);

        assertEquals("REUNION", e.type);
        assertEquals("Standup", e.title);
        assertEquals("Alice", e.proprietaire);
        assertEquals(date, e.dateDebut);
        assertEquals(15, e.dureeMinutes);
        assertEquals("Room B", e.lieu);
        assertEquals("Alice, Bob", e.participants);
        assertEquals(0, e.frequenceJours);
    }

    @Test
    public void testDisplayListEmpty() {
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        java.io.PrintStream original = System.out;
        System.setOut(new java.io.PrintStream(out));
        try {
            java.lang.reflect.Method m = Main.class.getDeclaredMethod("afficherListe", List.class);
            m.setAccessible(true);
            m.invoke(null, List.of());
            assertTrue(out.toString().contains("Aucun événement trouvé"));
        } catch (Exception e) {
            fail("Reflection failed: " + e.getMessage());
        } finally {
            System.setOut(original);
        }
    }

    @Test
    public void testDisplayListNonEmpty() {
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        java.io.PrintStream original = System.out;
        System.setOut(new java.io.PrintStream(out));
        try {
            java.lang.reflect.Method m = Main.class.getDeclaredMethod("afficherListe", List.class);
            m.setAccessible(true);

            Event e = new Event("RDV_PERSONNEL", "Test", "U",
                    LocalDateTime.of(2026, 4, 10, 10, 0), 30, "", "", 0);
            m.invoke(null, List.of(e));

            String result = out.toString();
            assertTrue(result.contains("Événements trouvés"));
            assertTrue(result.contains("RDV : Test"));
        } catch (Exception e) {
            fail("Reflection failed: " + e.getMessage());
        } finally {
            System.setOut(original);
        }
    }
    @Test
    public void testPeriodicEventDescriptionContent() {
        Event e = new Event("PERIODIQUE", "Yoga", "Alice",
                LocalDateTime.of(2026, 1, 1, 8, 0), 0, "", "", 3);
        assertEquals("Événement périodique : Yoga tous les 3 jours", e.description());
    }

    @Test
    public void testPeriodicEventFoundAfterMultipleIterations() {
        CalendarManager manager = new CalendarManager();
        manager.ajouterEvent("PERIODIQUE", "Checkup", "U",
                LocalDateTime.of(2026, 3, 1, 10, 0), 0, "", "", 10);

        LocalDateTime start = LocalDateTime.of(2026, 4, 5, 0, 0);
        LocalDateTime end = LocalDateTime.of(2026, 4, 15, 23, 59);

        List<Event> result = manager.eventsDansPeriode(start, end);
        assertEquals(1, result.size());
        assertEquals("Checkup", result.get(0).title);
    }
}