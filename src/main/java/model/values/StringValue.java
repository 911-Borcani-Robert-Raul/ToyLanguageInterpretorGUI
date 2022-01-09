package model.values;

import model.types.StringType;
import model.types.Type;

import java.util.Objects;

public class StringValue implements Value {
    String val;
    public StringValue(String v) {
        val=v;
    }
    public String getValue() {
        return val;
    }
    public String toString() {
        return val;
    }

    public Type getType() {
        return new StringType();
    }

    @Override
    public Value deepCopy() {
        return new StringValue(val);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof StringValue)
            return Objects.equals(val, ((StringValue) other).val);
        return false;
    }
}
