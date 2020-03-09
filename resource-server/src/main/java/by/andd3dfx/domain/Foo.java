package by.andd3dfx.domain;

public class Foo {

    private final long id;
    private final String name;

    public Foo(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
