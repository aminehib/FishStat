package tools;

import java.util.ArrayList;

public class BoiteAMoustaches {

    private Double Q1;
    private Double Q2;
    private Double Q3;
    private Double moustacheInf;
    private Double moustacheSup;

    public BoiteAMoustaches(ArrayList<Double> valeurs){

        // Pourquoi: éviter de modifier la liste d'origine (effet de bord).
        ArrayList<Double> filtered = new ArrayList<>(valeurs);
        // garder les poissons avec un taux d'infestation connu
        filtered.removeIf( arg->{
            return (arg == null ) ;
        });

        filtered.sort((f1, f2) -> {
        Double t1 = f1 ;
        Double t2 = f2 ;
        // tri croissant
        return t1.compareTo(t2);
        });

        // médiane globale
        Q2 = median(filtered);
        int n = filtered.size() ;
        int milieu = n / 2;

        ArrayList<Double> lower;//liste des valeurs inferieures a la mediane
        ArrayList<Double> upper;//liste des valeurs superieures a la mediane

        if(n % 2 == 0){
            lower = new ArrayList<>(filtered.subList(0, milieu));
            upper = new ArrayList<>(filtered.subList(milieu, n));
        }else{
            lower = new ArrayList<>(filtered.subList(0, milieu));
            upper = new ArrayList<>(filtered.subList(milieu + 1, n));
        }

        // quartiles (mediane inferieure , superieure)
        Q1 = median(lower);
        Q3 = median(upper);

        if(Q1 == null || Q2 == null ){
            moustacheInf = null ;
            moustacheSup = null ;
            return ;
        }

        // écart interquartile
        double ecartInterquantile = Q3 - Q1 ;

        moustacheInf =  Q1 -  (1.5 * ecartInterquantile) ;
        moustacheSup =  Q3 +  (1.5 * ecartInterquantile );
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

    public Double getPremierQuantile(){
        return Q1;
    }

    public Double getMediane(){
        return Q2;
    }

    public Double getDernierQuantile(){
        return Q3;
    }

    public Double getMoustacheInf(){
        return moustacheInf;
    }

    public Double getMoustacheSup(){
        return moustacheSup;
    }
}