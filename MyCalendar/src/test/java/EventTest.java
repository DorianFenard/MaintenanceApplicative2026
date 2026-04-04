import event.EvenementPeriodique;
import event.RDVPerso;
import event.Reunion;
import org.junit.jupiter.api.Test;
import valueobject.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EventTest {

    // ========================================================================
    // RDVPerso
    // ========================================================================

    @Test
    public void testRdvDescription() {
        RDVPerso rdv = createRdv("Dentiste", 2026, 4, 10, 10, 0, 30);
        assertEquals("RDV : Dentiste à 2026-04-10T10:00", rdv.description());
    }

    @Test
    public void testRdvEstDansPeriode() {
        RDVPerso rdv = createRdv("Test", 2026, 4, 10, 10, 0, 30);
        assertTrue(rdv.estDansPeriode(periodeAvril()));
    }

    @Test
    public void testRdvHorsPeriode() {
        RDVPerso rdv = createRdv("Test", 2026, 5, 10, 10, 0, 30);
        assertFalse(rdv.estDansPeriode(periodeAvril()));
    }

    @Test
    public void testRdvOccupation() {
        RDVPerso rdv = createRdv("Test", 2026, 4, 10, 10, 0, 60);
        Periode occ = rdv.occupation();
        assertEquals(new DateEvenement(2026, 4, 10, 10, 0), occ.debut());
        assertEquals(new DateEvenement(2026, 4, 10, 11, 0), occ.fin());
    }

    @Test
    public void testRdvAccessors() {
        RDVPerso rdv = createRdv("Test", 2026, 4, 10, 10, 0, 45);
        assertEquals(new TitreEvenement("Test"), rdv.titre());
        assertEquals(new Proprietaire("User"), rdv.proprietaire());
        assertEquals(new DureeEvenement(45), rdv.duree());
        assertNotNull(rdv.id());
        assertEquals(new DateEvenement(2026, 4, 10, 10, 0), rdv.dateDebut());
    }

    // ========================================================================
    // Reunion
    // ========================================================================

    @Test
    public void testReunionDescription() {
        Reunion r = createReunion("Standup", 2026, 4, 10, 9, 0, 30, "Salle B", "Alice", "Bob");
        assertEquals("Réunion : Standup à Salle B avec Alice, Bob", r.description());
    }

    @Test
    public void testReunionEstDansPeriode() {
        Reunion r = createReunion("Test", 2026, 4, 15, 10, 0, 60, "Salle A", "X");
        assertTrue(r.estDansPeriode(periodeAvril()));
    }

    @Test
    public void testReunionHorsPeriode() {
        Reunion r = createReunion("Test", 2026, 6, 15, 10, 0, 60, "Salle A", "X");
        assertFalse(r.estDansPeriode(periodeAvril()));
    }

    @Test
    public void testReunionOccupation() {
        Reunion r = createReunion("Test", 2026, 4, 10, 10, 0, 60, "Salle A", "X");
        Periode occ = r.occupation();
        assertEquals(new DateEvenement(2026, 4, 10, 10, 0), occ.debut());
        assertEquals(new DateEvenement(2026, 4, 10, 11, 0), occ.fin());
    }

    @Test
    public void testReunionAccessors() {
        Reunion r = createReunion("Standup", 2026, 4, 10, 9, 0, 30, "Salle B", "Alice", "Bob");
        assertEquals(new Lieu("Salle B"), r.lieu());
        assertEquals(new Participants(List.of("Alice", "Bob")), r.participants());
        assertEquals(new DureeEvenement(30), r.duree());
    }

    // ========================================================================
    // EvenementPeriodique
    // ========================================================================

    @Test
    public void testPeriodiqueDescription() {
        EvenementPeriodique ep = createPeriodique("Sport", 2026, 4, 1, 18, 0, 7);
        assertEquals("Événement périodique : Sport tous les 7 jours", ep.description());
    }

    @Test
    public void testPeriodiqueEstDansPeriodeDirectHit() {
        EvenementPeriodique ep = createPeriodique("Sport", 2026, 4, 1, 18, 0, 7);
        assertTrue(ep.estDansPeriode(periodeAvril()));
    }

    @Test
    public void testPeriodiqueEstDansPeriodeAfterIterations() {
        // Starts March 1st, every 10 days: Mar1, Mar11, Mar21, Mar31, Apr10
        EvenementPeriodique ep = createPeriodique("Checkup", 2026, 3, 1, 10, 0, 10);
        assertTrue(ep.estDansPeriode(periodeAvril()));
    }

    @Test
    public void testPeriodiqueHorsPeriode() {
        EvenementPeriodique ep = createPeriodique("Future", 2026, 6, 1, 10, 0, 7);
        assertFalse(ep.estDansPeriode(periodeAvril()));
    }

    @Test
    public void testPeriodiqueNoOccurrenceReachesPeriod() {
        EvenementPeriodique ep = createPeriodique("Monthly", 2026, 1, 1, 10, 0, 30);
        Periode midApril = new Periode(
                new DateEvenement(2026, 4, 2, 0, 0),
                new DateEvenement(2026, 4, 15, 0, 0));
        assertFalse(ep.estDansPeriode(midApril));
    }

    @Test
    public void testPeriodiqueOccupationIsPunctual() {
        EvenementPeriodique ep = createPeriodique("Sport", 2026, 4, 1, 18, 0, 7);
        Periode occ = ep.occupation();
        assertEquals(occ.debut(), occ.fin());
    }

    @Test
    public void testPeriodiqueAccessors() {
        EvenementPeriodique ep = createPeriodique("Sport", 2026, 4, 1, 18, 0, 7);
        assertEquals(new FrequenceJours(7), ep.frequence());
    }

    // ========================================================================
    // Conflict detection (polymorphic — via Event.estEnConflitAvec)
    // ========================================================================

    @Test
    public void testConflitEntreDeuxRdv() {
        RDVPerso rdv1 = createRdv("A", 2026, 4, 10, 10, 0, 60);
        RDVPerso rdv2 = createRdv("B", 2026, 4, 10, 10, 30, 60);
        assertTrue(rdv1.estEnConflitAvec(rdv2));
    }

    @Test
    public void testPasDeConflitExactBoundary() {
        RDVPerso rdv1 = createRdv("A", 2026, 4, 10, 10, 0, 60);
        RDVPerso rdv2 = createRdv("B", 2026, 4, 10, 11, 0, 60);
        assertFalse(rdv1.estEnConflitAvec(rdv2));
    }

    @Test
    public void testConflitSymetrique() {
        RDVPerso rdv1 = createRdv("A", 2026, 4, 10, 10, 0, 60);
        RDVPerso rdv2 = createRdv("B", 2026, 4, 10, 10, 30, 60);
        assertTrue(rdv1.estEnConflitAvec(rdv2));
        assertTrue(rdv2.estEnConflitAvec(rdv1));
    }

    @Test
    public void testConflitEntreReunionEtRdv() {
        Reunion reunion = createReunion("Meeting", 2026, 4, 10, 10, 0, 60, "Salle", "Alice");
        RDVPerso rdv = createRdv("Dentiste", 2026, 4, 10, 10, 30, 30);
        assertTrue(reunion.estEnConflitAvec(rdv));
        assertTrue(rdv.estEnConflitAvec(reunion));
    }

    @Test
    public void testPasDeConflitEntreDeuxReunionsSeparees() {
        Reunion r1 = createReunion("A", 2026, 4, 10, 10, 0, 60, "S1", "Alice");
        Reunion r2 = createReunion("B", 2026, 4, 10, 11, 0, 30, "S2", "Bob");
        assertFalse(r1.estEnConflitAvec(r2));
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
