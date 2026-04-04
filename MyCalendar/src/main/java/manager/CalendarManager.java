package manager;

import event.Event;
import valueobject.EventId;
import valueobject.Periode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CalendarManager {
    private final List<Event> events;

    public CalendarManager() {
        this.events = new ArrayList<>();
    }

    public void ajouter(Event event) {
        events.add(event);
    }

    public List<Event> tousLesEvenements() {
        return Collections.unmodifiableList(events);
    }

    public List<Event> evenementsDansPeriode(Periode periode) {
        return events.stream()
                .filter(e -> e.estDansPeriode(periode))
                .collect(Collectors.toList());
    }

    public List<Event> detecterConflits(Event cible) {
        return events.stream()
                .filter(e -> !e.id().equals(cible.id()))
                .filter(e -> e.estEnConflitAvec(cible))
                .collect(Collectors.toList());
    }

    public boolean supprimer(EventId id) {
        return events.removeIf(e -> e.id().equals(id));
    }

    public int nombreEvenements() {
        return events.size();
    }
}
