package etu1946.framework.models;
import etu1946.framework.annotation.Url;
import etu1946.framework.view.ModelView;
import jakarta.servlet.http.HttpServletRequest;


public class Emp {

    @Url("hello")
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
    
      @Url("formulaire")
    public ModelView formulaire() {
        ModelView vue = new ModelView();
        vue.setView("formulaire.jsp");
        return vue;
    }
    
    @Url("affiche")
    public ModelView affiche() {
    ModelView vue = new ModelView();
    vue.setView("ValeurFormulaire.jsp");
    return vue;
    }
}