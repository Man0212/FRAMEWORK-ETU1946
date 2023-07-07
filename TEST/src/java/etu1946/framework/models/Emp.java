package etu1946.framework.models;
import etu1946.framework.annotation.Url;
import etu1946.framework.annotation.Scope;
import etu1946.framework.annotation.RestAPI;
import etu1946.framework.view.ModelView;
import jakarta.servlet.http.HttpServletRequest;

@Scope("singleton")
public class Emp {
  String Nom;
Integer Age;

public String getNom() {
    return Nom;
}
public void setNom(String nom) {
    Nom = nom;
}
public Integer getAge() {
    return Age;
}
public void setAge(Integer age) {
    Age = age;
}

    @Url(value="hello",params = {"age","nom"})
    public ModelView welcome(int age,String nom) {
        ModelView vue = new ModelView();
        try{
        vue.setView("list.jsp");
        Emp user = new Emp();
        user.setNom(nom);
        user.setAge(age);
        vue.setJson(true);
        vue.addItem("user",user);
        
         }catch(Exception e){
                
         }
        return vue;
    }

    @RestAPI("json")
    @Url(value="tableau",params = {"nom"})
    public Emp[] tableau(String nom) {
        Emp[] tab = new Emp[2];
        tab[0]= new Emp();
        tab[1]= new Emp();
        tab[0].setNom(nom);
        tab[0].setAge(19);
        tab[1].setNom("Alain");
        tab[1].setAge(20);
        return tab;
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