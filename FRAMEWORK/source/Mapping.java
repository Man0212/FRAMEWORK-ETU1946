package etu1946.framework;

public class Mapping {

    private String className;
    private String method;

    public Mapping(String className, String method) {
        this.className = className;
        this.method = method;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getClassName() {
        return this.className;
    }

    public String getMethod() {
        return this.method;
    }
}