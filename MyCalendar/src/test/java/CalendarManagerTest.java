import event.Event;
import event.RDVPerso;
import event.Reunion;
import event.EvenementPeriodique;

import manager.CalendarManager;
import valueobject.*;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CalendarManagerTest {


    @Test
    public void testManagerInitiallyEmpty() {
        CalendarManager manager = new CalendarManager();
        assertEquals(0, manager.nombreEvenements());
        assertTrue(manager.tousLesEvenements().isEmpty());
    }



    @Test
    public void testAjouterRdv() {
        CalendarManager manager = new CalendarManager();
        manager.ajouter(createRdv("Dentiste", 2026, 4, 10, 10, 0, 30));
        assertEquals(1, manager.nombreEvenements());
    }

    @Test
    public void testAjouterPlusieursTypes() {
        CalendarManager manager = new CalendarManager();
        manager.ajouter(createRdv("A", 2026, 4, 10, 10, 0, 30));
        manager.ajouter(createReunion("B", 2026, 4, 11, 10, 0, 60, "Salle", "Bob"));
        manager.ajouter(createPeriodique("C", 2026, 4, 1, 8, 0, 7));
        assertEquals(3, manager.nombreEvenements());
    }

    // ========================================================================
    // Listing events (immutable)
    // ========================================================================

    @Test
    public void testTousLesEvenementsIsUnmodifiable() {
        CalendarManager manager = new CalendarManager();
        manager.ajouter(createRdv("Test", 2026, 4, 10, 10, 0, 30));
        assertThrows(UnsupportedOperationException.class, () ->
                manager.tousLesEvenements().add(createRdv("Hack", 2026, 1, 1, 0, 0, 1)));
    }

    @Test
    public void testTousLesEvenementsReturnsAll() {
        CalendarManager manager = new CalendarManager();
        manager.ajouter(createRdv("A", 2026, 4, 10, 10, 0, 30));
        manager.ajouter(createReunion("B", 2026, 4, 11, 10, 0, 60, "Salle", "X"));
        assertEquals(2, manager.tousLesEvenements().size());
    }

    // ========================================================================
    // Events in period
    // ========================================================================

    @Test
    public void testEvenementsDansPeriodeRdvInside() {
        CalendarManager manager = new CalendarManager();
        manager.ajouter(createRdv("InApril", 2026, 4, 10, 10, 0, 30));
        manager.ajouter(createRdv("InMay", 2026, 5, 10, 10, 0, 30));

        List<Event> result = manager.evenementsDansPeriode(periodeAvril());
        assertEquals(1, result.size());
        assertEquals(new TitreEvenement("InApril"), result.get(0).titre());
    }

    @Test
    public void testEvenementsDansPeriodeReunion() {
        CalendarManager manager = new CalendarManager();
        manager.ajouter(createReunion("April Meeting", 2026, 4, 20, 14, 0, 60, "Salle", "Bob"));
        manager.ajouter(createReunion("June Meeting", 2026, 6, 20, 14, 0, 60, "Salle", "Bob"));

        List<Event> result = manager.evenementsDansPeriode(periodeAvril());
        assertEquals(1, result.size());
    }

    @Test
    public void testEvenementsDansPeriodePeriodique() {
        CalendarManager manager = new CalendarManager();
        // Starts March 1st, every 7 days → has occurrences in April
        manager.ajouter(createPeriodique("Weekly", 2026, 3, 1, 10, 0, 7));

        List<Event> result = manager.evenementsDansPeriode(periodeAvril());
        assertEquals(1, result.size());
    }

    @Test
    public void testEvenementsDansPeriodePeriodiqueHorsPeriode() {
        CalendarManager manager = new CalendarManager();
        manager.ajouter(createPeriodique("Future", 2026, 6, 1, 10, 0, 7));

        List<Event> result = manager.evenementsDansPeriode(periodeAvril());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testEvenementsDansPeriodeMixed() {
        CalendarManager manager = new CalendarManager();
        manager.ajouter(createRdv("InApril", 2026, 4, 10, 10, 0, 30));
        manager.ajouter(createRdv("InMay", 2026, 5, 10, 10, 0, 30));
        manager.ajouter(createReunion("AprilMeeting", 2026, 4, 20, 14, 0, 60, "Salle", "Bob"));
        manager.ajouter(createPeriodique("Weekly", 2026, 3, 1, 10, 0, 7));

        List<Event> result = manager.evenementsDansPeriode(periodeAvril());
        assertEquals(3, result.size());
    }

    @Test
    public void testEvenementsDansPeriodeEmpty() {
        CalendarManager manager = new CalendarManager();
        List<Event> result = manager.evenementsDansPeriode(periodeAvril());
        assertTrue(result.isEmpty());
    }

    // ========================================================================
    // Conflict detection
    // ========================================================================

    @Test
    public void testDetecterConflitsOverlapping() {
        CalendarManager manager = new CalendarManager();
        RDVPerso rdv1 = createRdv("A", 2026, 4, 10, 10, 0, 60);
        RDVPerso rdv2 = createRdv("B", 2026, 4, 10, 10, 30, 60);
        RDVPerso rdv3 = createRdv("C", 2026, 4, 10, 12, 0, 60);

        manager.ajouter(rdv1);
        manager.ajouter(rdv2);
        manager.ajouter(rdv3);

        List<Event> conflits = manager.detecterConflits(rdv1);
        assertEquals(1, conflits.size());
        assertEquals(rdv2.id(), conflits.get(0).id());
    }

    @Test
    public void testDetecterConflitsNone() {
        CalendarManager manager = new CalendarManager();
        RDVPerso rdv1 = createRdv("A", 2026, 4, 10, 10, 0, 60);
        RDVPerso rdv2 = createRdv("B", 2026, 4, 10, 11, 0, 60);

        manager.ajouter(rdv1);
        manager.ajouter(rdv2);

        assertTrue(manager.detecterConflits(rdv1).isEmpty());
    }

    @Test
    public void testDetecterConflitsCrossType() {
        CalendarManager manager = new CalendarManager();
        Reunion reunion = createReunion("Meeting", 2026, 4, 10, 10, 0, 60, "Salle", "Alice");
        RDVPerso rdv = createRdv("Dentiste", 2026, 4, 10, 10, 30, 30);

        manager.ajouter(reunion);
        manager.ajouter(rdv);

        List<Event> conflits = manager.detecterConflits(reunion);
        assertEquals(1, conflits.size());
        assertEquals(rdv.id(), conflits.get(0).id());
    }

    @Test
    public void testDetecterConflitsDoesNotIncludeSelf() {
        CalendarManager manager = new CalendarManager();
        RDVPerso rdv = createRdv("A", 2026, 4, 10, 10, 0, 60);
        manager.ajouter(rdv);

        assertTrue(manager.detecterConflits(rdv).isEmpty());
    }

    // ========================================================================
    // Delete by EventId
    // ========================================================================

    @Test
    public void testSupprimerExistingEvent() {
        CalendarManager manager = new CalendarManager();
        RDVPerso rdv = createRdv("Test", 2026, 4, 10, 10, 0, 30);
        manager.ajouter(rdv);

        assertTrue(manager.supprimer(rdv.id()));
        assertEquals(0, manager.nombreEvenements());
    }

    @Test
    public void testSupprimerNonExistingEvent() {
        CalendarManager manager = new CalendarManager();
        manager.ajouter(createRdv("Test", 2026, 4, 10, 10, 0, 30));

        assertFalse(manager.supprimer(new EventId("inexistant")));
        assertEquals(1, manager.nombreEvenements());
    }

    @Test
    public void testSupprimerOnlyTargetedEvent() {
        CalendarManager manager = new CalendarManager();
        RDVPerso rdv1 = createRdv("A", 2026, 4, 10, 10, 0, 30);
        RDVPerso rdv2 = createRdv("B", 2026, 4, 11, 10, 0, 30);
        manager.ajouter(rdv1);
        manager.ajouter(rdv2);

        manager.supprimer(rdv1.id());
        assertEquals(1, manager.nombreEvenements());
        assertEquals(rdv2.id(), manager.tousLesEvenements().get(0).id());
    }

    @Test
    public void testSupprimerReunion() {
        CalendarManager manager = new CalendarManager();
        Reunion r = createReunion("Meeting", 2026, 4, 10, 10, 0, 60, "Salle", "Alice");
        manager.ajouter(r);

        assertTrue(manager.supprimer(r.id()));
        assertEquals(0, manager.nombreEvenements());
    }

    @Test
    public void testSupprimerPeriodique() {
        CalendarManager manager = new CalendarManager();
        EvenementPeriodique ep = createPeriodique("Sport", 2026, 4, 1, 18, 0, 7);
        manager.ajouter(ep);

        assertTrue(manager.supprimer(ep.id()));
        assertEquals(0, manager.nombreEvenements());
    }

    // ========================================================================
    // Helpers
    // ========================================================================

    private RDVPerso createRdv(String titre, int y, int m, int d, int h, int min, int duree) {
        return new RDVPerso(
                EventId.generate(),
                new TitreEvenement(titre),
                new Proprietaire("User"),
                new DateEvenement(y, m, d, h, min),
                new DureeEvenement(duree));
    }

    private Reunion createReunion(String titre, int y, int m, int d, int h, int min, int duree,
                                   String lieu, String... participants) {
        return new Reunion(
                EventId.generate(),
                new TitreEvenement(titre),
                new Proprietaire("User"),
                new DateEvenement(y, m, d, h, min),
                new DureeEvenement(duree),
                new Lieu(lieu),
                new Participants(List.of(participants)));
    }

    private EvenementPeriodique createPeriodique(String titre, int y, int m, int d, int h, int min, int freq) {
        return new EvenementPeriodique(
                EventId.generate(),
                new TitreEvenement(titre),
                new Proprietaire("User"),
                new DateEvenement(y, m, d, h, min),
                new FrequenceJours(freq));
    }

    private Periode periodeAvril() {
        return new Periode(
                new DateEvenement(2026, 4, 1, 0, 0),
                new DateEvenement(2026, 4, 30, 23, 59));
    }
}
