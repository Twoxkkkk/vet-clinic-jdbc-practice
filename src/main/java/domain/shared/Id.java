package domain.shared;

import domain.shared.exceptions.NullOrBlankException;

import java.util.UUID;

public record Id<T>(UUID value) {
    public Id {
        if (value == null) {
            throw new NullOrBlankException("ID cannot be null!");
        }
    }

    public static <T> Id<T> generate() {
        return new Id<>(UUID.randomUUID());
    }
}