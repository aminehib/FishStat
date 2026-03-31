package model;

public class Population extends Data{

    private Integer total ;
    private String name ;
    private String contentTested ;
    private Double prevalence ;// infestedNumber / total ;
    private Double intensity ;
    private Double meanLength ;
    private Double meanSize ;
    private Double meanWeight ;


   

    public Population(String name , Integer total , String contentTested){
        this.name = name ;
        this.total = total ;
        this.contentTested = contentTested ;
    }


    public String getName(){return this.name;}

    public Integer  getNumber(){ return this.total;}

    public String getContentTested(){ return this.contentTested ;}

    public Double getPrevalence(){
        return this.prevalence ;
    }
    public Double getIntensity(){
        return this.intensity ;
    }

    public Double getMeanSize(){
        return this.meanSize ;
    }

    public Double getMeanLength(){return this.meanLength ;}

    public Double getMeanWeight(){return this.meanWeight ;}


    public void setName(String name){
        this.name = name ;
    }

    public void setContentTested(String content){
        this.contentTested = content ;
    }

    public String getContent(){
        return this.contentTested ;
    }

    public Double getAbondance(){
        if(intensity == null || prevalence == null)return null ;
        return intensity*prevalence ;
    }

    public Double getInfestationRate(){return prevalence ;}

    public void setMeanLength(Double meanLength){this.meanLength = meanLength ;}
    public void setMeanSize(Double meanSize){this.meanSize = meanSize ;}
    public void setMeanWeight(Double meanWeight){this.meanWeight = meanWeight ;}
    public void setInfestationRate(Double prevalence){this.prevalence = prevalence ;}
    public void setIntensity(Double intensity){this.intensity = intensity ;}
    public void setTotal(Integer total){this.total = total ;}    

     @Override
    public String toString(){
        
        return String.format("%-30sLongueur_Moyenne:%-10.4fPoids_Moyen:%-10.4fTaille_Moyenne:%-10.4fTaux d'infestation:%-10.4fIntensité:%-10.4fAbondance:%-10.4fContenu:%-10s\n","[" + getName() + "]" ,getMeanLength() , getMeanWeight(),getMeanSize() , getInfestationRate(),getIntensity(),getAbondance() , contentTested ) ;
    }





}
