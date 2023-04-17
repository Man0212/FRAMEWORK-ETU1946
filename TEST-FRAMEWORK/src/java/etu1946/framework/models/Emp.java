package etu1946.framework.models;
import etu1946.framework.annotation.Url;
import etu1946.framework.view.ModelView;


public class Emp {

    @Url("TEST/hello")
    public ModelView welcome() {
        ModelView vue = new ModelView();
        try{
        vue.setView("list.jsp");
        String value = "Tsy Hello World";
        vue.addItem("test",value);
         }catch(Exception e){
                
         }
        return vue;
    }
}