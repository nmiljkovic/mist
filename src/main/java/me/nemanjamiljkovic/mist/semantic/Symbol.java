package me.nemanjamiljkovic.mist.semantic;

public class Symbol {
    private String name;
    private Type type;
    private Kind kind;

    public enum Kind {
        Variable,
        Type
    }

    public Symbol(String name, Type type, Kind kind) {
        this.name = name;
        this.type = type;
        this.kind = kind;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public Kind getKind() {
        return kind;
    }
}
