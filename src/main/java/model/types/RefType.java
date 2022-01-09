package model.types;

import model.values.RefValue;
import model.values.Value;

public class RefType implements Type {
    Type inner;

    public RefType(Type inner) {
        this.inner = inner;
    }

    public Type getInner() {
        return inner;
    }

    @Override
    public Value getDefaultValue() {
        return new RefValue(0, inner);
    }

    @Override
    public Type deepCopy() {
        return new RefType(inner.deepCopy());
    }

    @Override
    public String toString() {
        return "Ref(" + inner.toString() + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof RefType)
            return inner.equals(((RefType)other).inner);
        return false;
    }
}
