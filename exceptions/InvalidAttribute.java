package exceptions;

public class InvalidAttribute extends Exception {
    
    public InvalidAttribute(String attribute){
        super("L'attribut " +"\"" +  attribute + "\""+  " est invalide\nVoici la liste des attributs valides : \"Species\" , \"Length\" , \"Weight\" , \"Size\"  , \"InfestationRate\" , \"Content\"");
    }
    
}
