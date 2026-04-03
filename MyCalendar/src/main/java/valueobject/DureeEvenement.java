package valueobject;

public final class DureeEvenement {
    private final int minutes;

    public DureeEvenement(int minutes) {
        this.minutes = minutes;
    }

    public int minutes() {
        return minutes;
    }

    public DateEvenement ajouterA(DateEvenement date) {
        return date.plusMinutes(minutes);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof DureeEvenement other && minutes == other.minutes;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(minutes);
    }

    @Override
    public String toString() {
        return minutes + " min";
    }
}
