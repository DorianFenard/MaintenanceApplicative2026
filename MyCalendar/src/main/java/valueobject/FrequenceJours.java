package valueobject;

public final class FrequenceJours {
    private final int jours;

    public FrequenceJours(int jours) {
        this.jours = jours;
    }

    public int jours() {
        return jours;
    }

    public DateEvenement prochaineOccurrence(DateEvenement depuis) {
        return depuis.plusJours(jours);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof FrequenceJours other && jours == other.jours;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(jours);
    }

    @Override
    public String toString() {
        return jours + " jours";
    }
}
