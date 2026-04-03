package valueobject;

import java.time.LocalDateTime;
import java.util.Objects;

public final class DateEvenement {
    private final LocalDateTime value;

    public DateEvenement(LocalDateTime value) {
        Objects.requireNonNull(value, "La date ne peut pas être null");
        this.value = value;
    }

    public DateEvenement(int annee, int mois, int jour, int heure, int minute) {
        this(LocalDateTime.of(annee, mois, jour, heure, minute));
    }

    public LocalDateTime value() {
        return value;
    }

    public boolean estAvant(DateEvenement other) {
        return value.isBefore(other.value);
    }

    public boolean estApres(DateEvenement other) {
        return value.isAfter(other.value);
    }

    public boolean estAvantOuEgal(DateEvenement other) {
        return !value.isAfter(other.value);
    }

    public boolean estApresOuEgal(DateEvenement other) {
        return !value.isBefore(other.value);
    }

    public DateEvenement plusMinutes(int minutes) {
        return new DateEvenement(value.plusMinutes(minutes));
    }

    public DateEvenement plusJours(int jours) {
        return new DateEvenement(value.plusDays(jours));
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof DateEvenement other && value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
