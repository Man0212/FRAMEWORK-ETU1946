package etu1946.framework.models;
import etu1946.framework.annotation.Url;
import etu1946.framework.view.ModelView;
import jakarta.servlet.http.HttpServletRequest;


public class Emp {

    @Url(value="hello",params = {"age","nom"})
    public ModelView welcome(int age,String nom) {
        ModelView vue = new ModelView();
        try{
        vue.setView("list.jsp");
        vue.addItem("test",age*2);
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