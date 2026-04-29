package model;

import java.util.ArrayList;
import java.util.HashSet;

import exceptions.InvalidPopulation;
import tools.MeanValue;
import interfaces.*;

/**
 * Représente une population (un ensemble de poissons d'une même
 * espèce) avec ses statistiques agrégées : moyennes, taux
 * d'infestation, intensité, abondance et contenu testé.
 */
public class Population implements Data{

    private Integer total  ;
    private Integer parasites ;
    private String name ;
    private HashSet<String> contentTested ;
    private Double infestationRate ;
    private Double intensity ;
    private Double meanLength ;
    private Double meanSize ;
    private Double meanWeight ;

    /**
     * Construit une population avec son nom, son effectif et le
     * contenu testé. Les statistiques sont initialisées via les setters.
     *
     * @param name           le nom de l'espèce
     * @param total          l'effectif total
     * @param contentTested  les contenus testés
     */
    public Population(String name , Integer total , HashSet<String> contentTested){
        this.name = name ;
        this.total = total ;
        this.contentTested = contentTested ;
    }

    /**
     * Construit une population à partir d'un {@code DataFrame<Fish>}
     * monoespèce en calculant les statistiques agrégées.
     *
     * @param fish le DataFrame source (une seule espèce)
     * @throws InvalidPopulation si plusieurs espèces sont présentes
     */
    public Population(DataFrame<Fish> fish) throws InvalidPopulation{

        if(fish.getSpecies().size() != 1)throw new InvalidPopulation();

        ArrayList<Fish> poissons = new ArrayList<>(fish.getData());
        name = poissons.get(0).getSpecies();
        this.total = poissons.size();
        MeanValue mean = new MeanValue(fish.getLengths());
        meanLength = mean.getMean();
        mean = new MeanValue(fish.getInfestationRates());
        infestationRate = mean.getMean();
        if(infestationRate == null){
            double n =  0 ;
            boolean c = false;
            for(Fish poisson : poissons){
                if( poisson.getParasites() != null ){
                    c = true ;
                    if(poisson.getParasites() > 0 )
                        n++ ;
                }
            }
            if(c == true)
                infestationRate = n / total ;
        }
        mean = new MeanValue(fish.getWeights());
        meanWeight = mean.getMean();
        mean = new MeanValue(fish.getSizes());
        meanSize = mean.getMean();
        ArrayList<Double> par = fish.getParasites() ;
        for(Double v : par){
            if(v != null)
                if(parasites == null ){
                    parasites = v.intValue() ;
                }
                else parasites += v.intValue() ;
        }
        contentTested = fish.getContents();
    }

    /** @return le nom de l'espèce */
    public String getSpecies(){return this.name;}

    /** @return le nombre total de parasites */
    public Integer getParasites(){return this.parasites ;}

    /** @return l'effectif de la population */
    public Integer  getNumber(){ return this.total;}

    /** @return l'ensemble des contenus testés */
    public HashSet<String> getContentTested(){ return this.contentTested ;}

    /** @return le taux d'infestation moyen */
    public Double getInfestationRate(){
        return this.infestationRate ;
    }

    /** @return l'intensité parasitaire moyenne */
    public Double getIntensity(){
        return this.intensity ;
    }

    /** @return la taille moyenne */
    public Double getSize(){
        return this.meanSize ;
    }

    /** @return la longueur moyenne */
    public Double getLength(){return this.meanLength ;}

    /** @return le poids moyen */
    public Double getWeight(){return this.meanWeight ;}

    /** @param name le nom de l'espèce */
    public void setName(String name){
        this.name = name ;
    }

    /** @param parasites le nombre total de parasites */
    public void setParasites(Integer parasites){
        this.parasites = parasites ;
    }

    /**
     * Ajoute un élément à l'ensemble des contenus testés.
     *
     * @param content l'élément observé
     */
    public void addContent(String content){
        if(contentTested== null) contentTested = new HashSet<>() ;
        this.contentTested.add(content) ;
    }

    /** @return l'ensemble des contenus testés */
    public HashSet<String> getContent(){
        return this.contentTested ;
    }

    /**
     * Calcule l'abondance (intensité * taux d'infestation).
     *
     * @return l'abondance, ou {@code null} si une donnée manque
     */
    public Double getAbondance(){
        if(intensity == null || infestationRate== null)return null ;
        return intensity *infestationRate;
    }

    /** @param meanLength la longueur moyenne */
    public void setMeanLength(Double meanLength){this.meanLength = meanLength ;}
    /** @param meanSize la taille moyenne */
    public void setMeanSize(Double meanSize){this.meanSize = meanSize ;}
    /** @param meanWeight le poids moyen */
    public void setMeanWeight(Double meanWeight){this.meanWeight = meanWeight ;}
    /** @param prevalence le taux d'infestation (prévalence) */
    public void setInfestationRate(Double prevalence){this.infestationRate = prevalence ;}
    /** @param intensity l'intensité parasitaire */
    public void setIntensity(Double intensity){this.intensity = intensity ;}
    /** @param total l'effectif total */
    public void setTotal(Integer total){this.total = total ;}

    /** @return une représentation texte tabulaire de la population */
    @Override
    public String toString(){
        String contenu = (contentTested == null ||contentTested.isEmpty() )? "vide" : "{" + String.join(",", contentTested) + "}";
        return String.format("%-30sTotal:%-10dLongueur_Moyenne:%-10.4fPoids_Moyen:%-10.4fTaille_Moyenne:%-10.4fNombre de parasites :%-10d Taux d'infestation:%-10.4fIntensité:%-10.4fAbondance:%-10.4fContenu:%-10s\n","[" + getSpecies() + "]" ,getNumber() ,getLength() , getWeight(),getSize() ,getParasites() , getInfestationRate(),getIntensity(),getAbondance() , contenu ) ;
    }

}
