package exceptions;

public class InvalidPopulation extends RuntimeException {

    public InvalidPopulation(){
        super("La population que vous venez de passer est invalide");
    }

    
}
