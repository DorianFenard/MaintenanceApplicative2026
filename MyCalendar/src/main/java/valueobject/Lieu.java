package valueobject;

import java.util.Objects;

public final class Lieu {
    private final String value;

    public Lieu(String value) {
        Objects.requireNonNull(value, "Le lieu ne peut pas être null");
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Lieu other && value.equals(other.value);
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
