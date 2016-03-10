package me.nemanjamiljkovic.mist.semantic;

public class Type {
    private String name;
    private Type parent;

    public Type(String name) {
        this(name, null);
    }

    public Type(String name, Type parent) {
        this.name = name;
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Type type = (Type) o;

        if (!name.equals(type.name)) {
            return false;
        }

        //noinspection SimplifiableIfStatement
        if (parent != null && type.parent != null && !parent.equals(type.parent)) {
            return false;
        }

        return parent != type.parent;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        return result;
    }

    public String getName() {
        return name;
    }

    public Type getParent() {
        return parent;
    }

    @Override
    public String toString() {
        String output = name;

        if (parent != null) {
            output += parent.toString();
        }

        return output;
    }
}
