package Core;

public class Variable {
    public enum Type {
        INT, FLOAT
    }

    private String name;
    private boolean mutable;
    private Type type;

    public Variable(final String name, final boolean mutable, final Type type) {
        this.name = name;
        this.mutable = mutable;
        this.type = type;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setMutable(final boolean mutable) {
        this.mutable = mutable;
    }

    public void setType(final Type type) {
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public boolean isMutable() {
        return this.mutable;
    }

    public Type getType() {
        return this.type;
    }
}
