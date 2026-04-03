package valueobject;

import java.util.List;
import java.util.Objects;

public final class Participants {
    private final List<String> noms;

    public Participants(List<String> noms) {
        Objects.requireNonNull(noms, "La liste de participants ne peut pas être null");
        this.noms = List.copyOf(noms);
    }

    public List<String> noms() {
        return noms;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Participants other && noms.equals(other.noms);
    }

    @Override
    public int hashCode() {
        return noms.hashCode();
    }

    @Override
    public String toString() {
        return String.join(", ", noms);
    }
}
