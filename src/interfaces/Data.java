package interfaces;

public interface Data {
    public  Double getInfestationRate() ;
    public  Double getSize() ;
    public  Double getLength() ;
    public  Double getWeight() ;
    public  Integer getParasites() ;
    public  String getSpecies() ;
    public  java.util.HashSet<String> getContent() ;
    public  void addContent(String content) ;
}
