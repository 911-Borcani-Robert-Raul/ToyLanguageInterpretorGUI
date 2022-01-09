package model.values;

import model.types.IntType;
import model.types.Type;

import java.util.Objects;

public class IntValue implements Value {
    int val;
    public IntValue(int v) {
        val=v;
    }
    public int getVal() {
        return val;
    }
    public String toString() {
        return String.valueOf(val);
    }

    public Type getType() {
        return new IntType();
    }

    @Override
    public Value deepCopy() {
        return new IntValue(val);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof IntValue)
            return Objects.equals(val, ((IntValue) other).val);
        return false;
    }
}
