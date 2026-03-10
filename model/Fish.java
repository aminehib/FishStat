package model;
import java.util.ArrayList;

public class Fish{

    private String spieces;
    private Double length ;
    private Double weight ;
    private Double size ;
    private Double infestationRate ;
    private ArrayList<String> content ;

    public Fish(String spieces , Double length , Double size , Double infestationRate){
        this.spieces = spieces ;
        this.length = length ;
        this.size = size ;
        this.infestationRate = infestationRate ;
        content = new ArrayList<>();
    }


    public void setSpecies(String spieces){
        this.spieces = spieces ;
    }

    public void setLength(Double length){
        this.length = length ;
    }

    public void setWeight(Double weight){
        this.weight = weight ;
    }

    public void setSize(Double size){
        this.size = size ;
    }

    public void setInfestationRate(Double infestationRate){
        this.infestationRate = infestationRate ;
    }

    public String getSpecies(){return this.spieces;}

    public Double getLength(){return this.length;}

    public Double getWeight(){return this.weight ;}

    public Double getSize(){return this.size ;}

    public Double getInfestationRate(){return this.infestationRate;}

    public void addContent(String contenu){
        content.add(contenu);
    }
    
    public ArrayList<String> getContent(){return this.content ;}

    public boolean removeContent(int slot){
        if(slot < 0 || slot > content.size() - 1 )return false ;
        content.remove(slot);
        return true ;
    }

    @Override
    public String toString(){
        String contenu = (content.isEmpty())? "vide" : "{" + String.join(",", content) + "}";
        return String.format("%-10sLongueur:%-10sPoids:%-10sTaille:%-10sTaux d'infestation:%-20sContenu:%-10s\n","[" + getSpecies() + "]" ,getLength() , getWeight(),getSize() , getInfestationRate() , contenu ) ;
    }






}