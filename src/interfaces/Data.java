package interfaces;

/**
 * Contrat commun aux entités manipulées dans un {@code DataFrame}
 * (poissons, populations). Fournit les accesseurs aux mesures
 * biologiques et au contenu observé.
 */
public interface Data {
    /** @return le taux d'infestation, ou {@code null} si inconnu */
    public  Double getInfestationRate() ;
    /** @return la taille (épaisseur) du poisson, ou {@code null} si inconnue */
    public  Double getSize() ;
    /** @return la longueur, ou {@code null} si inconnue */
    public  Double getLength() ;
    /** @return le poids, ou {@code null} si inconnu */
    public  Double getWeight() ;
    /** @return le nombre de parasites, ou {@code null} si inconnu */
    public  Integer getParasites() ;
    /** @return le nom de l'espèce */
    public  String getSpecies() ;
    /** @return l'ensemble des éléments contenus (estomac/contenu testé) */
    public  java.util.HashSet<String> getContent() ;
    /**
     * Ajoute un élément au contenu de l'entité.
     *
     * @param content un élément observé
     */
    public  void addContent(String content) ;
}
