package event;

public class EventSerializer {

    public String toJson(Event event) {
        return event.accept(new JsonVisitor());
    }

    private static class JsonVisitor implements EventVisitor<String> {

        @Override
        public String visitRendezVous(RendezVousPersonnel rdv) {
            return "{"
                    + jsonField("type", "RDV_PERSONNEL") + ", "
                    + jsonField("id", rdv.id().value()) + ", "
                    + jsonField("titre", rdv.titre().value()) + ", "
                    + jsonField("proprietaire", rdv.proprietaire().value()) + ", "
                    + jsonField("dateDebut", rdv.dateDebut().toString()) + ", "
                    + jsonInt("dureeMinutes", rdv.duree().minutes())
                    + "}";
        }

        @Override
        public String visitReunion(Reunion reunion) {
            return "{"
                    + jsonField("type", "REUNION") + ", "
                    + jsonField("id", reunion.id().value()) + ", "
                    + jsonField("titre", reunion.titre().value()) + ", "
                    + jsonField("proprietaire", reunion.proprietaire().value()) + ", "
                    + jsonField("dateDebut", reunion.dateDebut().toString()) + ", "
                    + jsonInt("dureeMinutes", reunion.duree().minutes()) + ", "
                    + jsonField("lieu", reunion.lieu().value()) + ", "
                    + jsonArray("participants", reunion.participants().noms())
                    + "}";
        }

        @Override
        public String visitPeriodique(EvenementPeriodique periodique) {
            return "{"
                    + jsonField("type", "PERIODIQUE") + ", "
                    + jsonField("id", periodique.id().value()) + ", "
                    + jsonField("titre", periodique.titre().value()) + ", "
                    + jsonField("proprietaire", periodique.proprietaire().value()) + ", "
                    + jsonField("dateDebut", periodique.dateDebut().toString()) + ", "
                    + jsonInt("frequenceJours", periodique.frequence().jours())
                    + "}";
        }

        private String jsonField(String key, String value) {
            return "\"" + key + "\": \"" + value + "\"";
        }

        private String jsonInt(String key, int value) {
            return "\"" + key + "\": " + value;
        }

        private String jsonArray(String key, java.util.List<String> values) {
            StringBuilder sb = new StringBuilder("\"" + key + "\": [");
            for (int i = 0; i < values.size(); i++) {
                sb.append("\"").append(values.get(i)).append("\"");
                boolean isLast = (i == values.size() - 1);
                sb.append(isLast ? "" : ", ");
            }
            sb.append("]");
            return sb.toString();
        }
    }
}
