package event;

import valueobject.*;

public final class Reunion extends Event {
    private final DureeEvenement duree;
    private final Lieu lieu;
    private final Participants participants;

    public Reunion(EventId id, TitreEvenement titre, Proprietaire proprietaire,
                   DateEvenement dateDebut, DureeEvenement duree, Lieu lieu, Participants participants) {
        super(id, titre, proprietaire, dateDebut);
        this.duree = duree;
        this.lieu = lieu;
        this.participants = participants;
    }

    public DureeEvenement duree() {
        return duree;
    }

    public Lieu lieu() {
        return lieu;
    }

    public Participants participants() {
        return participants;
    }

    @Override
    public String description() {
        return "Réunion : " + titre() + " à " + lieu + " avec " + participants;
    }

    @Override
    public boolean estDansPeriode(Periode periode) {
        return periode.contient(dateDebut());
    }

    @Override
    public Periode occupation() {
        return new Periode(dateDebut(), duree.ajouterA(dateDebut()));
    }
}
