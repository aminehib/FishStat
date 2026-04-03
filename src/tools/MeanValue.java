package tools;

import java.util.ArrayList;

import model.Fish;

public class MeanValue {

    private Double mean = null;

    public MeanValue(ArrayList<Double> values){
        double sum = 0 ;
        int taille  = 0 ;
        for(Double v : values){
            if(v!=null){
                sum += v ;
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
