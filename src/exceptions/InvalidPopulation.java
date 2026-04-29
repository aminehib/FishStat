package exceptions;

/**
 * Exception levée lorsqu'une population de poissons construite
 * à partir d'un DataFrame est invalide (par exemple plusieurs espèces).
 */
public class InvalidPopulation extends RuntimeException {

    /**
     * Construit l'exception avec un message par défaut.
     */
    public InvalidPopulation(){
        super("La population que vous venez de passer est invalide");
    }


}
