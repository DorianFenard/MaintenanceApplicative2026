import valueobject.*;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ValueObjectTest {

    // ========================================================================
    // EventId
    // ========================================================================

    @Test
    public void testEventIdStoresValue() {
        EventId id = new EventId("abc-123");
        assertEquals("abc-123", id.value());
    }

    @Test
    public void testEventIdGenerateCreatesUniqueIds() {
        EventId id1 = EventId.generate();
        EventId id2 = EventId.generate();
        assertNotEquals(id1, id2);
    }

    @Test
    public void testEventIdEqualityWhenSameValue() {
        EventId id1 = new EventId("same");
        EventId id2 = new EventId("same");
        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    public void testEventIdInequalityWhenDifferentValue() {
        assertNotEquals(new EventId("a"), new EventId("b"));
    }

    @Test
    public void testEventIdToString() {
        assertEquals("test-id", new EventId("test-id").toString());
    }

    @Test
    public void testEventIdNullThrows() {
        assertThrows(NullPointerException.class, () -> new EventId(null));
    }

    // ========================================================================
    // TitreEvenement
    // ========================================================================

    @Test
    public void testTitreStoresValue() {
        TitreEvenement titre = new TitreEvenement("Dentiste");
        assertEquals("Dentiste", titre.value());
    }

    @Test
    public void testTitreEquality() {
        assertEquals(new TitreEvenement("A"), new TitreEvenement("A"));
        assertNotEquals(new TitreEvenement("A"), new TitreEvenement("B"));
    }

    @Test
    public void testTitreToString() {
        assertEquals("Dentiste", new TitreEvenement("Dentiste").toString());
    }

    @Test
    public void testTitreNullThrows() {
        assertThrows(NullPointerException.class, () -> new TitreEvenement(null));
    }

    // ========================================================================
    // Proprietaire
    // ========================================================================

    @Test
    public void testProprietaireStoresValue() {
        Proprietaire p = new Proprietaire("Alice");
        assertEquals("Alice", p.value());
    }

    @Test
    public void testProprietaireEquality() {
        assertEquals(new Proprietaire("A"), new Proprietaire("A"));
        assertNotEquals(new Proprietaire("A"), new Proprietaire("B"));
    }

    @Test
    public void testProprietaireToString() {
        assertEquals("Alice", new Proprietaire("Alice").toString());
    }

    @Test
    public void testProprietaireNullThrows() {
        assertThrows(NullPointerException.class, () -> new Proprietaire(null));
    }

    // ========================================================================
    // DateEvenement
    // ========================================================================

    @Test
    public void testDateFromLocalDateTime() {
        LocalDateTime ldt = LocalDateTime.of(2026, 4, 10, 14, 30);
        DateEvenement d = new DateEvenement(ldt);
        assertEquals(ldt, d.value());
    }

    @Test
    public void testDateFromComponents() {
        DateEvenement d = new DateEvenement(2026, 4, 10, 14, 30);
        assertEquals(LocalDateTime.of(2026, 4, 10, 14, 30), d.value());
    }

    @Test
    public void testDateEstAvant() {
        DateEvenement early = new DateEvenement(2026, 4, 10, 10, 0);
        DateEvenement late = new DateEvenement(2026, 4, 10, 14, 0);
        assertTrue(early.estAvant(late));
        assertFalse(late.estAvant(early));
        assertFalse(early.estAvant(early));
    }

    @Test
    public void testDateEstApres() {
        DateEvenement early = new DateEvenement(2026, 4, 10, 10, 0);
        DateEvenement late = new DateEvenement(2026, 4, 10, 14, 0);
        assertTrue(late.estApres(early));
        assertFalse(early.estApres(late));
        assertFalse(early.estApres(early));
    }

    @Test
    public void testDateEstAvantOuEgal() {
        DateEvenement early = new DateEvenement(2026, 4, 10, 10, 0);
        DateEvenement late = new DateEvenement(2026, 4, 10, 14, 0);
        assertTrue(early.estAvantOuEgal(late));
        assertTrue(early.estAvantOuEgal(early));
        assertFalse(late.estAvantOuEgal(early));
    }

    @Test
    public void testDateEstApresOuEgal() {
        DateEvenement early = new DateEvenement(2026, 4, 10, 10, 0);
        DateEvenement late = new DateEvenement(2026, 4, 10, 14, 0);
        assertTrue(late.estApresOuEgal(early));
        assertTrue(late.estApresOuEgal(late));
        assertFalse(early.estApresOuEgal(late));
    }

    @Test
    public void testDatePlusMinutes() {
        DateEvenement d = new DateEvenement(2026, 4, 10, 10, 0);
        assertEquals(new DateEvenement(2026, 4, 10, 11, 30), d.plusMinutes(90));
    }

    @Test
    public void testDatePlusJours() {
        DateEvenement d = new DateEvenement(2026, 4, 10, 10, 0);
        assertEquals(new DateEvenement(2026, 4, 15, 10, 0), d.plusJours(5));
    }

    @Test
    public void testDateEquality() {
        DateEvenement d1 = new DateEvenement(2026, 4, 10, 10, 0);
        DateEvenement d2 = new DateEvenement(2026, 4, 10, 10, 0);
        assertEquals(d1, d2);
        assertEquals(d1.hashCode(), d2.hashCode());
    }

    @Test
    public void testDateInequality() {
        assertNotEquals(
                new DateEvenement(2026, 4, 10, 10, 0),
                new DateEvenement(2026, 4, 10, 11, 0));
    }

    @Test
    public void testDateToString() {
        assertEquals("2026-04-10T10:00", new DateEvenement(2026, 4, 10, 10, 0).toString());
    }

    @Test
    public void testDateNullThrows() {
        assertThrows(NullPointerException.class, () -> new DateEvenement(null));
    }

    // ========================================================================
    // DureeEvenement
    // ========================================================================

    @Test
    public void testDureeMinutes() {
        assertEquals(45, new DureeEvenement(45).minutes());
    }

    @Test
    public void testDureeAjouterA() {
        DureeEvenement duree = new DureeEvenement(60);
        DateEvenement date = new DateEvenement(2026, 4, 10, 10, 0);
        assertEquals(new DateEvenement(2026, 4, 10, 11, 0), duree.ajouterA(date));
    }

    @Test
    public void testDureeEquality() {
        assertEquals(new DureeEvenement(30), new DureeEvenement(30));
        assertNotEquals(new DureeEvenement(30), new DureeEvenement(60));
    }

    @Test
    public void testDureeToString() {
        assertEquals("45 min", new DureeEvenement(45).toString());
    }

    // ========================================================================
    // Lieu
    // ========================================================================

    @Test
    public void testLieuStoresValue() {
        assertEquals("Salle A", new Lieu("Salle A").value());
    }

    @Test
    public void testLieuEquality() {
        assertEquals(new Lieu("A"), new Lieu("A"));
        assertNotEquals(new Lieu("A"), new Lieu("B"));
    }

    @Test
    public void testLieuToString() {
        assertEquals("Salle A", new Lieu("Salle A").toString());
    }

    @Test
    public void testLieuNullThrows() {
        assertThrows(NullPointerException.class, () -> new Lieu(null));
    }

    // ========================================================================
    // Participants
    // ========================================================================

    @Test
    public void testParticipantsStoresNames() {
        Participants p = new Participants(List.of("Alice", "Bob"));
        assertEquals(List.of("Alice", "Bob"), p.noms());
    }

    @Test
    public void testParticipantsIsImmutable() {
        java.util.ArrayList<String> original = new java.util.ArrayList<>(List.of("Alice"));
        Participants p = new Participants(original);
        original.add("Bob");
        assertEquals(1, p.noms().size());
    }

    @Test
    public void testParticipantsEquality() {
        assertEquals(
                new Participants(List.of("A", "B")),
                new Participants(List.of("A", "B")));
        assertNotEquals(
                new Participants(List.of("A")),
                new Participants(List.of("B")));
    }

    @Test
    public void testParticipantsToString() {
        assertEquals("Alice, Bob", new Participants(List.of("Alice", "Bob")).toString());
    }

    @Test
    public void testParticipantsNullThrows() {
        assertThrows(NullPointerException.class, () -> new Participants(null));
    }

    // ========================================================================
    // FrequenceJours
    // ========================================================================

    @Test
    public void testFrequenceJoursValue() {
        assertEquals(7, new FrequenceJours(7).jours());
    }

    @Test
    public void testFrequenceProchaineOccurrence() {
        FrequenceJours f = new FrequenceJours(10);
        DateEvenement d = new DateEvenement(2026, 4, 1, 10, 0);
        assertEquals(new DateEvenement(2026, 4, 11, 10, 0), f.prochaineOccurrence(d));
    }

    @Test
    public void testFrequenceEquality() {
        assertEquals(new FrequenceJours(7), new FrequenceJours(7));
        assertNotEquals(new FrequenceJours(7), new FrequenceJours(14));
    }

    @Test
    public void testFrequenceToString() {
        assertEquals("7 jours", new FrequenceJours(7).toString());
    }

    // ========================================================================
    // Periode
    // ========================================================================

    @Test
    public void testPeriodeAccessors() {
        DateEvenement debut = new DateEvenement(2026, 4, 1, 0, 0);
        DateEvenement fin = new DateEvenement(2026, 4, 30, 0, 0);
        Periode p = new Periode(debut, fin);
        assertEquals(debut, p.debut());
        assertEquals(fin, p.fin());
    }

    @Test
    public void testPeriodeContientDateInside() {
        Periode p = periode(2026, 4, 1, 2026, 4, 30);
        assertTrue(p.contient(new DateEvenement(2026, 4, 15, 12, 0)));
    }

    @Test
    public void testPeriodeContientDateAtBoundaries() {
        Periode p = periode(2026, 4, 1, 2026, 4, 30);
        assertTrue(p.contient(new DateEvenement(2026, 4, 1, 0, 0)));
        assertTrue(p.contient(new DateEvenement(2026, 4, 30, 0, 0)));
    }

    @Test
    public void testPeriodeNeContientPasDateOutside() {
        Periode p = periode(2026, 4, 1, 2026, 4, 30);
        assertFalse(p.contient(new DateEvenement(2026, 3, 31, 23, 59)));
        assertFalse(p.contient(new DateEvenement(2026, 4, 30, 0, 1)));
    }

    @Test
    public void testPeriodeChevauchement() {
        Periode p1 = periode(2026, 4, 10, 2026, 4, 20);
        Periode p2 = periode(2026, 4, 15, 2026, 4, 25);
        assertTrue(p1.chevauche(p2));
        assertTrue(p2.chevauche(p1));
    }

    @Test
    public void testPeriodePasDeChevauchementExactBoundary() {
        Periode p1 = new Periode(
                new DateEvenement(2026, 4, 10, 10, 0),
                new DateEvenement(2026, 4, 10, 11, 0));
        Periode p2 = new Periode(
                new DateEvenement(2026, 4, 10, 11, 0),
                new DateEvenement(2026, 4, 10, 12, 0));
        assertFalse(p1.chevauche(p2));
    }

    @Test
    public void testPeriodeEquality() {
        Periode p1 = periode(2026, 4, 1, 2026, 4, 30);
        Periode p2 = periode(2026, 4, 1, 2026, 4, 30);
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    public void testPeriodeInequality() {
        assertNotEquals(
                periode(2026, 4, 1, 2026, 4, 30),
                periode(2026, 4, 1, 2026, 5, 30));
    }

    @Test
    public void testPeriodeToString() {
        Periode p = new Periode(
                new DateEvenement(2026, 4, 1, 0, 0),
                new DateEvenement(2026, 4, 2, 0, 0));
        assertEquals("2026-04-01T00:00 → 2026-04-02T00:00", p.toString());
    }

    // ========================================================================
    // Helper
    // ========================================================================

    private Periode periode(int y1, int m1, int d1, int y2, int m2, int d2) {
        return new Periode(
                new DateEvenement(y1, m1, d1, 0, 0),
                new DateEvenement(y2, m2, d2, 0, 0));
    }
}
