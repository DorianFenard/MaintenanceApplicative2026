package valueobject;

import java.util.Objects;

public final class TitreEvenement {
    private final String value;

    public TitreEvenement(String value) {
        Objects.requireNonNull(value, "Le titre ne peut pas être null");
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof TitreEvenement other && value.equals(other.value);
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
