package valueobject;

import java.util.Objects;
import java.util.UUID;

public final class EventId {
    private final String value;

    public EventId(String value) {
        Objects.requireNonNull(value, "EventId cannot be null");
        this.value = value;
    }

    public static EventId generate() {
        return new EventId(UUID.randomUUID().toString());
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof EventId other && value.equals(other.value);
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
