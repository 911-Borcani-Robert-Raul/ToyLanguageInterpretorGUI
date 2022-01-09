package model.values;

import model.types.RefType;
import model.types.Type;

public class RefValue implements Value {
    int address;
    Type locationType;

    public RefValue(int address, Type locationType) {
        this.address = address;
        this.locationType = locationType;
    }

    public int getAddress() {
        return address;
    }

    public Type getLocationType() {
        return locationType;
    }

    @Override
    public Type getType() {
        return new RefType(locationType);
    }

    @Override
    public Value deepCopy() {
        return null;
    }

    @Override
    public String toString() {
        return "ref(" + address + ", " + locationType + ")";
    }

    public void setAdrress(int address) {
        this.address = address;
    }
}
