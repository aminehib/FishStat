package exceptions;

/**
 * Exception levée lorsqu'un attribut inconnu est rencontré
 * lors de la lecture d'un fichier ou de l'accès à une colonne.
 */
public class InvalidAttribute extends RuntimeException {

    /**
     * Construit une exception décrivant l'attribut invalide
     * et la liste des attributs autorisés.
     *
     * @param attribute le nom de l'attribut invalide
     * @param valid     la liste des attributs valides acceptés
     */
    public InvalidAttribute(String attribute , String[] valid){
        super("L'attribut " +"\"" +  attribute + "\""+  " est invalide\nVoici la liste des attributs valides : \"" + String.join("\" , \"",valid) +"\"\n" );
    }

}
