package exceptions;

public class InvalidParametreLength extends RuntimeException {

    public InvalidParametreLength(String param , int validLength){
        super(String.format("La longueur de \"%s\" doit etre %d",param , validLength)) ;
    }
    
}
