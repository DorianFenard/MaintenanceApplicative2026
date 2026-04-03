package valueobject;

import java.util.Objects;

public final class Proprietaire {
    private final String value;

    public Proprietaire(String value) {
        Objects.requireNonNull(value, "Le propriétaire ne peut pas être null");
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Proprietaire other && value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}
