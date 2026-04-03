package valueobject;

import java.util.Objects;

public final class Periode {
    private final DateEvenement debut;
    private final DateEvenement fin;

    public Periode(DateEvenement debut, DateEvenement fin) {
        Objects.requireNonNull(debut);
        Objects.requireNonNull(fin);
        this.debut = debut;
        this.fin = fin;
    }

    public DateEvenement debut() {
        return debut;
    }

    public DateEvenement fin() {
        return fin;
    }

    public boolean contient(DateEvenement date) {
        return date.estApresOuEgal(debut) && date.estAvantOuEgal(fin);
    }

    public boolean chevauche(Periode other) {
        return debut.estAvant(other.fin) && other.debut.estAvant(fin);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Periode other && debut.equals(other.debut) && fin.equals(other.fin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(debut, fin);
    }

    @Override
    public String toString() {
        return debut + " → " + fin;
    }
}
