package etu1946.framework.models;
import etu1946.framework.annotation.Url;
import etu1946.framework.view.ModelView;


public class Emp {

    @Url("hello")
    public ModelView welcome() {
        ModelView vue = new ModelView();
        vue.setView("list.html");
        return vue;
    }
}