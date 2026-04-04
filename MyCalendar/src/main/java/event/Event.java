package event;

import valueobject.*;

public abstract class Event {
    private final EventId id;
    private final TitreEvenement titre;
    private final Proprietaire proprietaire;
    private final DateEvenement dateDebut;

    protected Event(EventId id, TitreEvenement titre, Proprietaire proprietaire, DateEvenement dateDebut) {
        this.id = id;
        this.titre = titre;
        this.proprietaire = proprietaire;
        this.dateDebut = dateDebut;
    }

    public EventId id() {
        return id;
    }

    public TitreEvenement titre() {
        return titre;
    }

    public Proprietaire proprietaire() {
        return proprietaire;
    }

    public DateEvenement dateDebut() {
        return dateDebut;
    }

    public abstract String description();

    public abstract boolean estDansPeriode(Periode periode);

    public abstract Periode occupation();

    public boolean estEnConflitAvec(Event autre) {
        return occupation().chevauche(autre.occupation());
    }
}
