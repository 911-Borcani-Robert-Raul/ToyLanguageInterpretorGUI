package model.values;

import model.types.BoolType;
import model.types.Type;

import java.util.Objects;

public class BoolValue implements Value {
    boolean value;

    public BoolValue(boolean b) {
        value = b;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public Type getType() {
        return new BoolType();
    }

    @Override
    public Value deepCopy() {
        return new BoolValue(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof BoolValue)
            return Objects.equals(value, ((BoolValue) other).value);
        return false;
    }
}
