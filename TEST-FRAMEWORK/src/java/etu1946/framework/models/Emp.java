package etu1946.framework.models;
import etu1946.framework.annotation.Url;

public class Emp {

    String name;

    @Url("add-emp")
    public void insert() {
        System.out.println("insertion");
    }
}