package model;

import java.util.ArrayList;
import java.util.HashSet;

import exceptions.InvalidPopulation;
import tools.MeanValue;

public class Population extends Data{

    private Integer total = 0 ;
    private Integer parasites = 0 ;
    private String name ;
    private HashSet<String> contentTested ;
    private Double inestationRate ;// infestedNumber / total ;
    private Double intensity ;
    private Double meanLength ;
    private Double meanSize ;
    private Double meanWeight ;


   

    public Population(String name , Integer total , HashSet<String> contentTested){
        this.name = name ;
        this.total = total ;
        this.contentTested = contentTested ;
    }

    public Population(DataFrame<Fish> fish) throws InvalidPopulation{

        if(fish.getSpecies().size() != 1)throw new InvalidPopulation();

        ArrayList<Fish> poissons = new ArrayList<>(fish.getData());
        name = poissons.get(0).getSpecies();
        this.total = poissons.size();
        MeanValue<Double> mean = new MeanValue<>(fish.getLengths());
        meanLength = mean.getMean();
        mean = new MeanValue<>(fish.getInfestationRates());
        inestationRate = mean.getMean();
        mean = new MeanValue<>(fish.getWeights());
        meanWeight = mean.getMean();
        mean = new MeanValue<>(fish.getSizes());
        meanSize = mean.getMean();
        ArrayList<Double> par = fish.getParasites() ;
        for(Double v : par){
            parasites += v.intValue() ;
        }
        StringBuilder content = new StringBuilder() ;
        contentTested = fish.getContents();
    }


    public String getSpecies(){return this.name;}

    public Integer getParasites(){return this.parasites ;}

    public Integer  getNumber(){ return this.total;}

    public HashSet<String> getContentTested(){ return this.contentTested ;}

    public Double getInfestationRate(){
        return this.inestationRate ;
    }
    public Double getIntensity(){
        return this.intensity ;
    }

    public Double getSize(){
        return this.meanSize ;
    }


    public Double getLength(){return this.meanLength ;}

    public Double getWeight(){return this.meanWeight ;}


    public void setName(String name){
        this.name = name ;
    }

    public void setParasites(Integer parasites){
        this.parasites = parasites ;
    }

    public void addContent(String content){
        if(contentTested== null) contentTested = new HashSet<>() ;
        this.contentTested.add(content) ;
    }

    public HashSet<String> getContent(){
        return this.contentTested ;
    }

    public Double getAbondance(){
        if(intensity == null || inestationRate== null)return null ;
        return intensity *inestationRate;
    }





    

    public void setMeanLength(Double meanLength){this.meanLength = meanLength ;}
    public void setMeanSize(Double meanSize){this.meanSize = meanSize ;}
    public void setMeanWeight(Double meanWeight){this.meanWeight = meanWeight ;}
    public void setInfestationRate(Double prevalence){this.inestationRate = prevalence ;}
    public void setIntensity(Double intensity){this.intensity = intensity ;}
    public void setTotal(Integer total){this.total = total ;}    

     @Override
    public String toString(){
        String contenu = (contentTested == null ||contentTested.isEmpty() )? "vide" : "{" + String.join(",", contentTested) + "}";
        return String.format("%-30sTotal:%-10dLongueur_Moyenne:%-10.4fPoids_Moyen:%-10.4fTaille_Moyenne:%-10.4fNombre de parasites :%010d Taux d'infestation:%-10.4fIntensité:%-10.4fAbondance:%-10.4fContenu:%-10s\n","[" + getSpecies() + "]" ,getNumber() ,getLength() , getWeight(),getSize() ,getParasites() , getInfestationRate(),getIntensity(),getAbondance() , contenu ) ;
    }





}
