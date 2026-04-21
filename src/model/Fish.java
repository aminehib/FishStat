package model;
import java.util.ArrayList;
import java.util.HashSet;

public class Fish extends Data{

    // Correction de la faute de frappe : "species" (cohérent avec le sujet et le reste du code).
    private String species;
    private Double length ;
    private Double weight ;
    private Double size ;
    private Double infestationRate ;
    private Integer parasites_number ;
    private HashSet<String> content ;

    public Fish(String species , Double length ,Double weight , Double size , Double infestationRate){
        this.species = species ;
        this.length = length ;
        this.weight = weight ;
        this.size = size ;
        this.infestationRate = infestationRate ;
        content = new HashSet<>();
    }


    public void setSpecies(String species){
        this.species = species ;
    }
    /**
    @params length: la logueur du poisson
    @return : la logueur
     **/
    public void setLength(Double length){
        this.length = length ;
    }

    public void setWeight(Double weight){
        this.weight = weight ;
    }

    public void setSize(Double size){
        this.size = size ;
    }

    public void setParasites(Integer parasites){
        this.parasites_number = parasites ;
    }

    public void setInfestationRate(Double infestationRate){
        this.infestationRate = infestationRate ;
    }

    public String getSpecies(){return this.species;}

    public Double getLength(){return this.length;}

    public Double getWeight(){return this.weight ;}

    public Double getSize(){return this.size ;}

    public Double getInfestationRate(){return this.infestationRate;}

    public Integer getParasites(){
        return this.parasites_number ;
    }

    public void addContent(String contenu){
        if(content == null)content = new HashSet<>() ;
        content.add(contenu);
    }
    
    public HashSet<String> getContent(){return new HashSet<>(content) ;}

    public boolean removeContent(String content){
        return this.content.remove(content);
    }

    @Override
    public String toString(){
        String contenu = (content == null || content.isEmpty())? "vide" : "{" + String.join(",", content) + "}";
        return String.format("%-30sLongueur:%-10.4fPoids:%-10.4fTaille:%-10.4fNombre de parasites:%-10.4dTaux d'infestation:%-20.4fContenu:%-10s\n","[" + getSpecies() + "]" ,getLength() , getWeight(),getSize() ,getParasites() , getInfestationRate() , contenu ) ;
    }

}
