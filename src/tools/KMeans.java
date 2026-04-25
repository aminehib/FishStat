package tools;

import java.util.*;

import model.*;

public class KMeans {

    public static ArrayList<Integer> Kmeans(Coords[] init_centers, int max_iter , double epsilon ,DataFrame<Fish> df , String X , String Y){

        if(init_centers.length == 0 )return null  ;

        ArrayList<Double> x = null ;
        ArrayList<Double> y = null ;
        ArrayList<Integer> labels = new ArrayList<>() ;
        ArrayList<Double> dist = new ArrayList<>() ;
        

        Coords[] previous_centers = new Coords[init_centers.length]  ;



        switch(X){
            case "Length":
                x = df.getLengths() ;
                break ;
            
             case "Weight":
                x = df.getWeights() ;
                break ;

             case "Size":
                x = df.getSizes() ;
                break ;

            case "Parasites":
                x = df.getParasites();
                break ;
            
             case "InfestationRate":
                x = df.getInfestationRates() ;
                break ;
            
        }

        switch(Y){
            case "Length":
                y = df.getLengths() ;
                break ;
            
             case "Weight":
                y = df.getWeights() ;
                break ;

             case "Size":
                y = df.getSizes() ;
                break ;
            
            case  "Parasites":
                y = df.getParasites() ;
            
             case "InfestationRate":
                y = df.getInfestationRates() ;
                break ;
            
        }
        for(int i = 0 ; i < x.size() ; i++){
            labels.add(-1);
            dist.add(Double.MAX_VALUE) ;
        }

        Coords[] coords = Coords.init_Coords(x, y) ;

        int iter = 0 ;

        
        while(iter < max_iter && !stop(init_centers , previous_centers , epsilon)){
            iter ++ ;
            
            int K = 0 ;
            for(Coords center : init_centers){
                System.out.println("pour : " + K);

                previous_centers[K] = center ;
            
                for(int i = 0 ; i < coords.length ; i++){
                    if(dist.get(i) > Coords.distance(center,coords[i])){
                        dist.set(i , Coords.distance(center,coords[i]));
                        labels.set(i,K) ;
                    }
                }
                
                K++ ;
            }

            K =  0 ;

            for(Coords center : init_centers){
                System.out.println("pour : " + K);

                previous_centers[K] = center ;
                int n  = 0 ;
                double mx = 0 ;
                double my = 0  ;
                for(int i = 0 ; i < coords.length ; i++){
                   
                    if(labels.get(i) == K){
                        n++ ;
                        if(coords[i].getX() != null){
                            mx += coords[i].getX() ;
                        }
                        if(coords[i].getY() != null){
                            my += coords[i].getY() ;
                        }
                    }
                }
                mx = mx / n ;
                my = my / n ;
                center.setX(mx);
                center.setY(my);
                K++ ;
            }
            
        
        }

        return labels ;

    }

    private static boolean stop(Coords[] x , Coords[] y , double critere){
        for(int i = 0 ; i < x.length ; i++){
            if(Coords.distance(x[i],y[i] ) > critere )return false ;
        }
        return true ;
    }
    
}
