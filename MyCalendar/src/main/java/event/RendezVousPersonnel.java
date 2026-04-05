package event;

import valueobject.*;

public final class RendezVousPersonnel extends Event {
    private final DureeEvenement duree;

    public RendezVousPersonnel(EventId id, TitreEvenement titre, Proprietaire proprietaire,
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

    @Override
    public <T> T accept(EventVisitor<T> visitor) {
        return visitor.visitRendezVous(this);
    }
}
