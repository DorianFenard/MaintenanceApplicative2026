package event;

import valueobject.*;

public final class RDVPerso extends Event {
    private final DureeEvenement duree;

    public RDVPerso(EventId id, TitreEvenement titre, Proprietaire proprietaire,
                    DateEvenement dateDebut, DureeEvenement duree) {
        super(id, titre, proprietaire, dateDebut);
        this.duree = duree;
    }

    public DureeEvenement duree() {
        return duree;
    }

    @Override
    public String description() {
        return "RDV : " + titre() + " à " + dateDebut();
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
