package model;



public abstract class Data{

    public abstract Double getInfestationRate() ;
    public abstract Double getSize() ;
    public abstract Double getLength() ;
    public abstract Double getWeight() ;
    public abstract Integer getParasites() ;
    public abstract String getSpecies() ;

    public abstract java.util.HashSet<String> getContent() ;
    public abstract void addContent(String content) ;
    
}

