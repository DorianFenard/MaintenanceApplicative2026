package event;

import valueobject.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class EventDeserializer {

    private final Map<String, Function<Map<String, String>, Event>> factories;

    public EventDeserializer() {
        factories = new HashMap<>();
        factories.put("RDV_PERSONNEL", this::buildRendezVous);
        factories.put("REUNION", this::buildReunion);
        factories.put("PERIODIQUE", this::buildPeriodique);
    }

    public Event fromJson(String json) {
        Map<String, String> fields = parseJson(json);
        String type = fields.get("type");
        return factories.get(type).apply(fields);
    }

    private RendezVousPersonnel buildRendezVous(Map<String, String> fields) {
        return new RendezVousPersonnel(
                new EventId(fields.get("id")),
                new TitreEvenement(fields.get("titre")),
                new Proprietaire(fields.get("proprietaire")),
                new DateEvenement(java.time.LocalDateTime.parse(fields.get("dateDebut"))),
                new DureeEvenement(Integer.parseInt(fields.get("dureeMinutes"))));
    }

    private Reunion buildReunion(Map<String, String> fields) {
        return new Reunion(
                new EventId(fields.get("id")),
                new TitreEvenement(fields.get("titre")),
                new Proprietaire(fields.get("proprietaire")),
                new DateEvenement(java.time.LocalDateTime.parse(fields.get("dateDebut"))),
                new DureeEvenement(Integer.parseInt(fields.get("dureeMinutes"))),
                new Lieu(fields.get("lieu")),
                parseParticipants(fields.get("participants")));
    }

    private EvenementPeriodique buildPeriodique(Map<String, String> fields) {
        return new EvenementPeriodique(
                new EventId(fields.get("id")),
                new TitreEvenement(fields.get("titre")),
                new Proprietaire(fields.get("proprietaire")),
                new DateEvenement(java.time.LocalDateTime.parse(fields.get("dateDebut"))),
                new FrequenceJours(Integer.parseInt(fields.get("frequenceJours"))));
    }

    private Participants parseParticipants(String arrayStr) {
        // Input: "Alice", "Bob"  (already stripped of brackets)
        String[] parts = arrayStr.split(",");
        java.util.ArrayList<String> noms = new java.util.ArrayList<>();
        for (String part : parts) {
            noms.add(part.trim().replace("\"", ""));
        }
        return new Participants(noms);
    }

    private Map<String, String> parseJson(String json) {
        Map<String, String> fields = new HashMap<>();
        // Remove outer braces
        String content = json.trim();
        content = content.substring(1, content.length() - 1);

        // Split by top-level commas (not inside arrays/strings)
        List<String> pairs = splitTopLevel(content);

        for (String pair : pairs) {
            int colonIndex = pair.indexOf(':');
            String key = pair.substring(0, colonIndex).trim().replace("\"", "");
            String value = pair.substring(colonIndex + 1).trim();

            // Array value: keep raw content without brackets
            boolean isArray = value.startsWith("[");
            String cleanValue = isArray
                    ? value.substring(1, value.length() - 1).trim()
                    : value.replace("\"", "");

            fields.put(key, cleanValue);
        }
        return fields;
    }

    private List<String> splitTopLevel(String content) {
        java.util.ArrayList<String> parts = new java.util.ArrayList<>();
        int depth = 0;
        int start = 0;
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            depth += (c == '[') ? 1 : (c == ']') ? -1 : 0;
            boolean isSplitPoint = (c == ',' && depth == 0);
            // Avoid if: use the split point flag
            int end = isSplitPoint ? i : -1;
            parts.addAll(end >= 0
                    ? List.of(content.substring(start, end))
                    : List.of());
            start = isSplitPoint ? i + 1 : start;
        }
        parts.add(content.substring(start));
        return parts;
    }
}
