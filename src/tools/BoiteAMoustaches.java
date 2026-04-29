package tools;

import java.util.ArrayList;

/**
 * Calcule les statistiques d'une boîte à moustaches :
 * quartiles Q1, Q2 (médiane), Q3 et bornes des moustaches
 * basées sur l'écart interquartile (1.5 * IQR).
 */
public class BoiteAMoustaches {

    private Double Q1;
    private Double Q2;
    private Double Q3;
    private Double moustacheInf;
    private Double moustacheSup;

    /**
     * Construit la boîte à moustaches à partir d'une liste de valeurs
     * (les {@code null} sont ignorés ; la liste passée n'est pas modifiée).
     *
     * @param valeurs les valeurs à analyser
     */
    public BoiteAMoustaches(ArrayList<Double> valeurs ){

        ArrayList<Double> filtered = new ArrayList<>(valeurs);
        filtered.removeIf( arg->{
            return (arg == null ) ;
        });

        filtered.sort((f1, f2) -> {
        Double t1 = f1 ;
        Double t2 = f2 ;
        return t1.compareTo(t2);
        });

        Q2 = median(filtered);
        int n = filtered.size() ;
        int milieu = n / 2;

        ArrayList<Double> lower;
        ArrayList<Double> upper;

        if(n % 2 == 0){
            lower = new ArrayList<>(filtered.subList(0, milieu));
            upper = new ArrayList<>(filtered.subList(milieu, n));
        }else{
            lower = new ArrayList<>(filtered.subList(0, milieu));
            upper = new ArrayList<>(filtered.subList(milieu + 1, n));
        }

        Q1 = median(lower);
        Q3 = median(upper);

        if(Q1 == null || Q2 == null ){
            moustacheInf = null ;
            moustacheSup = null ;
            return ;
        }

        double ecartInterquantile = Q3 - Q1 ;

        moustacheInf =  Q1 -  (1.5 * ecartInterquantile) ;
        moustacheSup =  Q3 +  (1.5 * ecartInterquantile ) ;
    }

    private Double median(ArrayList<Double> list){

        int n = list.size();
        if(n == 0)return null ;
        int m = n / 2;

        if(n % 2 == 0 ){
            return (list.get(m - 1) + list.get(m)) / 2.0;
        }else{
            return list.get(m);
        }
    }

    /** @return le premier quartile (Q1) */
    public Double getPremierQuantile(){
        return Q1;
    }

    /** @return la médiane (Q2) */
    public Double getMediane(){
        return Q2;
    }

    /** @return le troisième quartile (Q3) */
    public Double getDernierQuantile(){
        return Q3;
    }

    /** @return la moustache inférieure {@code Q1 - 1.5 * IQR} */
    public Double getMoustacheInf(){
        return moustacheInf;
    }

    /** @return la moustache supérieure {@code Q3 + 1.5 * IQR} */
    public Double getMoustacheSup(){
        return moustacheSup;
    }
}
