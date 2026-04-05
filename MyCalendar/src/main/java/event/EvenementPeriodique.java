package event;

import valueobject.*;

public final class EvenementPeriodique extends Event {
    private final FrequenceJours frequence;

    public EvenementPeriodique(EventId id, TitreEvenement titre, Proprietaire proprietaire,
                                DateEvenement dateDebut, FrequenceJours frequence) {
        super(id, titre, proprietaire, dateDebut);
        this.frequence = frequence;
    }

    public FrequenceJours frequence() {
        return frequence;
    }

    @Override
    public String description() {
        return "Événement périodique : " + titre() + " tous les " + frequence;
    }

    @Override
    public boolean estDansPeriode(Periode periode) {
        return occurrenceDansPeriode(dateDebut(), periode);
    }

    private boolean occurrenceDansPeriode(DateEvenement occurrence, Periode periode) {
        return occurrence.estAvantOuEgal(periode.fin())
                && (periode.contient(occurrence)
                    || occurrenceDansPeriode(frequence.prochaineOccurrence(occurrence), periode));
    }

    @Override
    public Periode occupation() {
        return new Periode(dateDebut(), dateDebut());
    }

    @Override
    public <T> T accept(EventVisitor<T> visitor) {
        return visitor.visitPeriodique(this);
    }
}
