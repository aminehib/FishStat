package exceptions;

/**
 * Exception levée lorsque le format d'un fichier d'entrée
 * n'est pas reconnu (format autre que CSV ou contenu invalide).
 */
public class InvalidFileFormat  extends Exception{

    /**
     * Construit l'exception avec un message par défaut indiquant
     * que le fichier doit être au format CSV.
     */
    public InvalidFileFormat(){
        super("Le fichier entree doit etre du format : file.csv") ;
    }

    /**
     * Construit l'exception avec un message personnalisé.
     *
     * @param message le message décrivant l'erreur
     */
    public InvalidFileFormat(String message){
        super(message);
    }

}
