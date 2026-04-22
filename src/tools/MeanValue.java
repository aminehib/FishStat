package tools;

import java.util.ArrayList;

import model.Fish;

public class MeanValue<T extends Number> {

    private Double mean = null;

    public MeanValue(ArrayList<T> values){
        double sum = 0 ;
        int taille  = 0 ;
        for(T v : values){
            if(v!=null){
                sum += v.doubleValue() ;
                taille++ ;
            }
        }
        if(taille == 0)return ;
        mean =  sum / taille ;
    }

    public Double getMean(){
        return mean ;
    }
    
}
