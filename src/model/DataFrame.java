package model;

import java.util.ArrayList;
import java.util.HashSet;

import exceptions.InvalidAttribute;
import exceptions.InvalidFileFormat;
import interfaces.*;

/**
 * Conteneur générique d'entités {@link Data} (poissons ou
 * populations). Offre des accesseurs orientés colonnes pour
 * exposer les mesures biologiques sous forme de listes.
 *
 * @param <T> type d'entité contenue, doit implémenter {@link Data}
 */
public class DataFrame<T extends Data>  {

    private ArrayList<T> poissons ;

    /**
     * Construit le DataFrame avec une liste initiale.
     *
     * @param poissons la liste d'entités à contenir
     */
    public DataFrame(ArrayList<T> poissons){
        this.poissons = poissons ;
    }

    /** Construit un DataFrame vide. */
    public DataFrame(){
        this.poissons = new ArrayList<>();
    }

    /** @param poissons la nouvelle liste d'entités */
    public void setData(ArrayList<T> poissons){
        this.poissons = poissons ;
    }

    /** @return la liste d'entités contenues */
    public ArrayList<T> getData(){return this.poissons ;}

    /** @return une représentation texte (une ligne par entité) */
    public String toString(){
        String Total =  "Liste des poissons :\n" ;
        if(poissons == null)return Total +"empty\n" ;
       for(T poisson : poissons)
            if(poisson != null)
                Total += poisson.toString() ;
        return Total.equals("Liste des poissons :\n")? Total +"empty\n":Total ;
    }

    private Double parseDouble(String value, String line) throws InvalidFileFormat{
        if(value == null || value.equals("")) return null;
        try{
            return Double.parseDouble(value);
        }catch(NumberFormatException e){
            throw new InvalidFileFormat("Valeur numérique invalide: \"" + value + "\" dans la ligne: " + line);
        }
    }

    /** @return la colonne des taux d'infestation */
    public ArrayList<Double> getInfestationRates(){

        ArrayList<Double> InfestationRate = new ArrayList<>() ;
        for(T poisson : poissons){
            InfestationRate.add(poisson.getInfestationRate());
        }
        return InfestationRate ;
    }

    /** @return la colonne des tailles */
    public ArrayList<Double> getSizes(){
        ArrayList<Double> Size = new ArrayList<>() ;
        for(T poisson : poissons){
            Size.add(poisson.getSize());
        }
        return Size ;
    }

    /** @return la colonne des longueurs */
    public ArrayList<Double> getLengths(){
        ArrayList<Double> length = new ArrayList<>() ;
        for(T poisson : poissons){
            length.add(poisson.getLength());
        }
        return  length ;
    }

    /** @return la colonne des poids */
     public ArrayList<Double> getWeights(){
        ArrayList<Double> weigth = new ArrayList<>() ;
        for(T poisson : poissons){
            weigth.add(poisson.getWeight());
        }
        return  weigth;
    }

    /** @return l'ensemble des espèces présentes */
     public HashSet<String> getSpecies(){
        HashSet<String> species = new HashSet<>() ;
        for(T poisson : poissons){
            species.add(poisson.getSpecies());
        }
        return species;
    }

    /** @return l'union des contenus stomacaux observés */
    public HashSet<String> getContents(){
        HashSet<String> content = new  HashSet<>() ;
        for(T poisson : poissons){

                HashSet<String> fishContent = poisson.getContent() ;
                for(String fishCont : fishContent){
                    content.add(fishCont);
                }

        }
        return  content;
    }

    /**
     * Filtre les entités d'une espèce donnée.
     *
     * @param espece nom de l'espèce
     * @return les entités appartenant à cette espèce
     */
    public ArrayList<T> getSpecies(String espece){
        ArrayList<T> res = new ArrayList<>() ;
        for(T poisson : poissons){

            if(poisson.getSpecies().equals(espece))res.add(poisson);

        }
        return res ;
    }

    /** @return la colonne des nombres de parasites convertis en Double */
    public ArrayList<Double> getParasites(){
        ArrayList<Double> parasites = new ArrayList<>() ;
        for(T poisson : poissons){
            parasites.add((poisson.getParasites() == null)? null : poisson.getParasites().doubleValue());
        }
        return  parasites;

    }

    /**
     * Accesseur générique à une colonne par son nom.
     *
     * @param column un nom parmi : Length, Weight, Size, Parasites, InfestationRate
     * @return la liste des valeurs de la colonne
     * @throws InvalidAttribute si {@code column} n'est pas reconnu
     */
    public ArrayList<Double> getColumn(String column ){
        ArrayList<Double> x = null ;
            String[] cols = {"Length","Weight","Size","Parasites" ,"InfestationRate"};
             switch(column){
            case "Length":
                x = getLengths() ;
                break ;

             case "Weight":
                x = getWeights() ;
                break ;

             case "Size":
                x = getSizes() ;
                break ;

            case "Parasites":
                x = getParasites();
                break ;

             case "InfestationRate":
                x = getInfestationRates() ;
                break ;

            default:throw new InvalidAttribute(column, cols) ;
        }
        return x ;
    }

}
