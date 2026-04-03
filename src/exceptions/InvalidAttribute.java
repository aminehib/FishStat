package exceptions;

public class InvalidAttribute extends Exception {
    
    public InvalidAttribute(String attribute , String[] valid){
        super("L'attribut " +"\"" +  attribute + "\""+  " est invalide\nVoici la liste des attributs valides : \"" + String.join("\" , \"",valid) +"\"\n" );
    }
    
}
