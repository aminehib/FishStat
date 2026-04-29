package interfaces;

import model.DataFrame;
import model.Fish;
import exceptions.InvalidParametreLength;


/**
 * Définit les opérations de nettoyage et de complétion
 * appliquées à un {@code DataFrame<Fish>} avant analyse.
 */
public interface Cleanable {

    /**
     * Nettoie le DataFrame en mettant à {@code null} les valeurs
     * aberrantes (négatives, hors moustaches, hors plage).
     *
     * @param fish    le DataFrame à nettoyer
     * @param errors  les marges d'erreur autorisées sur les 5 colonnes
     * @throws InvalidParametreLength si {@code errors.length} != 5
     */
    public void clean(DataFrame<Fish> fish, Double[] errors) throws InvalidParametreLength;

    /**
     * Complète les valeurs manquantes du DataFrame
     * selon la stratégie de l'implémentation.
     *
     * @param fish le DataFrame à compléter
     */
    public void complete(DataFrame<Fish> fish);

}
