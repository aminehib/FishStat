package model;
import java.util.HashSet;
import interfaces.*;

/**
 * Représente un poisson individuel avec ses mesures biologiques :
 * espèce, longueur, poids, taille, taux d'infestation, nombre de
 * parasites et contenu stomacal observé.
 */
public class Fish implements Data{

    private String species;
    private Double length ;
    private Double weight ;
    private Double size ;
    private Double infestationRate ;
    private Integer parasites_number ;
    private HashSet<String> content ;

    /**
     * Construit un poisson avec les mesures biologiques de base.
     *
     * @param species          le nom de l'espèce
     * @param length           la longueur en cm
     * @param weight           le poids en g
     * @param size             la taille (épaisseur) en cm
     * @param infestationRate  le taux d'infestation entre 0 et 1
     */
    public Fish(String species , Double length ,Double weight , Double size , Double infestationRate){
        this.species = species ;
        this.length = length ;
        this.weight = weight ;
        this.size = size ;
        this.infestationRate = infestationRate ;
        content = new HashSet<>();
    }


    /** @param species le nom de l'espèce */
    public void setSpecies(String species){
        this.species = species ;
    }

    /** @param length la longueur du poisson */
    public void setLength(Double length){
        this.length = length ;
    }

    /** @param weight le poids du poisson */
    public void setWeight(Double weight){
        this.weight = weight ;
    }

    /** @param size la taille (épaisseur) du poisson */
    public void setSize(Double size){
        this.size = size ;
    }

    /** @param parasites le nombre de parasites */
    public void setParasites(Integer parasites){
        this.parasites_number = parasites ;
    }

    /** @param infestationRate le taux d'infestation entre 0 et 1 */
    public void setInfestationRate(Double infestationRate){
        this.infestationRate = infestationRate ;
    }

    /** @return le nom de l'espèce */
    public String getSpecies(){return this.species;}

    /** @return la longueur */
    public Double getLength(){return this.length;}

    /** @return le poids */
    public Double getWeight(){return this.weight ;}

    /** @return la taille */
    public Double getSize(){return this.size ;}

    /** @return le taux d'infestation */
    public Double getInfestationRate(){return this.infestationRate;}

    /** @return le nombre de parasites */
    public Integer getParasites(){
        return this.parasites_number ;
    }

    /**
     * Ajoute un élément au contenu stomacal.
     * Initialise l'ensemble si besoin.
     *
     * @param contenu l'élément observé
     */
    public void addContent(String contenu){
        if(content == null)content = new HashSet<>() ;
        content.add(contenu);
    }

    /** @return une copie de l'ensemble des contenus observés */
    public HashSet<String> getContent(){return new HashSet<>(content) ;}

    /**
     * Retire un élément du contenu stomacal.
     *
     * @param content l'élément à retirer
     * @return {@code true} si l'élément était présent
     */
    public boolean removeContent(String content){
        return this.content.remove(content);
    }

    /** @return une représentation texte tabulaire du poisson */
    @Override
    public String toString(){
        String contenu = (content == null || content.isEmpty())? "vide" : "{" + String.join(",", content) + "}";
        return String.format("%-30sLongueur:%-10.4fPoids:%-10.4fTaille:%-10.4fNombre de parasites:%-10dTaux d'infestation:%-20.4fContenu:%-10s\n","[" + getSpecies() + "]" ,getLength() , getWeight(),getSize() ,getParasites() , getInfestationRate() , contenu ) ;
    }

}
