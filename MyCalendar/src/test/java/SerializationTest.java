import event.*;
import event.*;
import valueobject.*;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SerializationTest {

    private final EventSerializer serializer = new EventSerializer();
    private final EventDeserializer deserializer = new EventDeserializer();

    @Test
    public void testSerializeRdv() {
        RendezVousPersonnel rdv = createRdv("rdv-1", "Dentiste", "Alice", 2026, 4, 10, 10, 0, 30);
        String json = serializer.toJson(rdv);

        assertTrue(json.contains("\"type\": \"RDV_PERSONNEL\""));
        assertTrue(json.contains("\"id\": \"rdv-1\""));
        assertTrue(json.contains("\"titre\": \"Dentiste\""));
        assertTrue(json.contains("\"proprietaire\": \"Alice\""));
        assertTrue(json.contains("\"dateDebut\": \"2026-04-10T10:00\""));
        assertTrue(json.contains("\"dureeMinutes\": 30"));
    }


    @Test
    public void testSerializeReunion() {
        Reunion reunion = createReunion("reu-1", "Standup", "Bob", 2026, 4, 10, 9, 0, 30,
                "Salle B", "Alice", "Bob");
        String json = serializer.toJson(reunion);

        assertTrue(json.contains("\"type\": \"REUNION\""));
        assertTrue(json.contains("\"id\": \"reu-1\""));
        assertTrue(json.contains("\"titre\": \"Standup\""));
        assertTrue(json.contains("\"lieu\": \"Salle B\""));
        assertTrue(json.contains("\"participants\": [\"Alice\", \"Bob\"]"));
        assertTrue(json.contains("\"dureeMinutes\": 30"));
    }


    @Test
    public void testSerializePeriodique() {
        EvenementPeriodique ep = createPeriodique("per-1", "Sport", "Charlie", 2026, 4, 1, 18, 0, 7);
        String json = serializer.toJson(ep);

        assertTrue(json.contains("\"type\": \"PERIODIQUE\""));
        assertTrue(json.contains("\"id\": \"per-1\""));
        assertTrue(json.contains("\"titre\": \"Sport\""));
        assertTrue(json.contains("\"frequenceJours\": 7"));
    }


    @Test
    public void testDeserializeRdv() {
        RendezVousPersonnel original = createRdv("rdv-1", "Dentiste", "Alice", 2026, 4, 10, 10, 0, 30);
        String json = serializer.toJson(original);
        Event restored = deserializer.fromJson(json);

        assertTrue(restored instanceof RendezVousPersonnel);
        assertEquals(original.id(), restored.id());
        assertEquals(original.titre(), restored.titre());
        assertEquals(original.proprietaire(), restored.proprietaire());
        assertEquals(original.dateDebut(), restored.dateDebut());
        assertEquals(original.duree(), ((RendezVousPersonnel) restored).duree());
    }



    @Test
    public void testDeserializeReunion() {
        Reunion original = createReunion("reu-1", "Standup", "Bob", 2026, 4, 10, 9, 0, 30,
                "Salle B", "Alice", "Bob");
        String json = serializer.toJson(original);
        Event restored = deserializer.fromJson(json);

        assertTrue(restored instanceof Reunion);
        Reunion r = (Reunion) restored;
        assertEquals(original.id(), r.id());
        assertEquals(original.titre(), r.titre());
        assertEquals(original.lieu(), r.lieu());
        assertEquals(original.participants(), r.participants());
        assertEquals(original.duree(), r.duree());
    }


    @Test
    public void testDeserializePeriodique() {
        EvenementPeriodique original = createPeriodique("per-1", "Sport", "Charlie", 2026, 4, 1, 18, 0, 7);
        String json = serializer.toJson(original);
        Event restored = deserializer.fromJson(json);

        assertTrue(restored instanceof EvenementPeriodique);
        EvenementPeriodique ep = (EvenementPeriodique) restored;
        assertEquals(original.id(), ep.id());
        assertEquals(original.titre(), ep.titre());
        assertEquals(original.frequence(), ep.frequence());
    }



    @Test
    public void testRoundTripRdvDescription() {
        RendezVousPersonnel original = createRdv("rt-1", "Médecin", "Alice", 2026, 5, 20, 14, 30, 45);
        String json = serializer.toJson(original);
        Event restored = deserializer.fromJson(json);
        assertEquals(original.description(), restored.description());
    }

    @Test
    public void testRoundTripReunionDescription() {
        Reunion original = createReunion("rt-2", "Sprint Review", "Bob", 2026, 4, 15, 10, 0, 90,
                "Grande Salle", "Alice", "Bob", "Charlie");
        String json = serializer.toJson(original);
        Event restored = deserializer.fromJson(json);
        assertEquals(original.description(), restored.description());
    }

    @Test
    public void testRoundTripPeriodiqueDescription() {
        EvenementPeriodique original = createPeriodique("rt-3", "Yoga", "Diana", 2026, 1, 1, 7, 0, 14);
        String json = serializer.toJson(original);
        Event restored = deserializer.fromJson(json);
        assertEquals(original.description(), restored.description());
    }


    @Test
    public void testRoundTripRdvEstDansPeriode() {
        RendezVousPersonnel original = createRdv("b-1", "Test", "User", 2026, 4, 10, 10, 0, 30);
        Event restored = deserializer.fromJson(serializer.toJson(original));

        Periode avril = periodeAvril();
        assertEquals(original.estDansPeriode(avril), restored.estDansPeriode(avril));
    }

    @Test
    public void testRoundTripConflitPreserved() {
        RendezVousPersonnel rdv1 = createRdv("c-1", "A", "User", 2026, 4, 10, 10, 0, 60);
        RendezVousPersonnel rdv2 = createRdv("c-2", "B", "User", 2026, 4, 10, 10, 30, 60);

        Event restored1 = deserializer.fromJson(serializer.toJson(rdv1));
        Event restored2 = deserializer.fromJson(serializer.toJson(rdv2));

        assertEquals(rdv1.estEnConflitAvec(rdv2), restored1.estEnConflitAvec(restored2));
    }


    private RendezVousPersonnel createRdv(String id, String titre, String owner,
                                          int y, int m, int d, int h, int min, int duree) {
        return new RendezVousPersonnel(
                new EventId(id),
                new TitreEvenement(titre),
                new Proprietaire(owner),
                new DateEvenement(y, m, d, h, min),
                new DureeEvenement(duree));
    }

    private Reunion createReunion(String id, String titre, String owner,
                                   int y, int m, int d, int h, int min, int duree,
                                   String lieu, String... participants) {
        return new Reunion(
                new EventId(id),
                new TitreEvenement(titre),
                new Proprietaire(owner),
                new DateEvenement(y, m, d, h, min),
                new DureeEvenement(duree),
                new Lieu(lieu),
                new Participants(List.of(participants)));
    }

    private EvenementPeriodique createPeriodique(String id, String titre, String owner,
                                                  int y, int m, int d, int h, int min, int freq) {
        return new EvenementPeriodique(
                new EventId(id),
                new TitreEvenement(titre),
                new Proprietaire(owner),
                new DateEvenement(y, m, d, h, min),
                new FrequenceJours(freq));
    }

    private Periode periodeAvril() {
        return new Periode(
                new DateEvenement(2026, 4, 1, 0, 0),
                new DateEvenement(2026, 4, 30, 23, 59));
    }
}
