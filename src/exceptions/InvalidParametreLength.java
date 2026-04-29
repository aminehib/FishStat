package exceptions;

/**
 * Exception levée lorsqu'un paramètre passé à une méthode
 * n'a pas la longueur attendue (par exemple un tableau d'erreurs).
 */
public class InvalidParametreLength extends RuntimeException {

    /**
     * Construit l'exception avec le nom du paramètre fautif
     * et la longueur attendue.
     *
     * @param param        le nom du paramètre invalide
     * @param validLength  la longueur valide attendue
     */
    public InvalidParametreLength(String param , int validLength){
        super(String.format("La longueur de \"%s\" doit etre %d",param , validLength)) ;
    }

}
