package tools;

import java.util.ArrayList;

public class Coords {

    private Double x = null ;
    private Double y = null ;

    public Coords(Double x , Double y){
        this.x = x ;
        this.y = y ;
    }

    public static Coords[] init_Coords(ArrayList<Double> X , ArrayList<Double> Y ){

        if(X == null || Y == null || X.size() != Y.size())return null ;

        Coords[] coords = new Coords[X.size()] ;

        for(int i = 0 ;  i <  X.size() ; i++){
            coords[i] = new Coords(X.get(i), Y.get(i)) ;
        }
        return coords ;
    }


    public static Double distance(Coords x1 , Coords x2){
        if(x1 == null || x2 ==null) return Double.MAX_VALUE ;
        return Math.sqrt(Math.pow(x1.getX() - x2.getX() , 2) + Math.pow(x1.getY() - x2.getY(), 2));
    }


    public Double getX(){
        return this.x ;
    }
    public Double getY(){
        return this.y ;
    }

    public void setX(Double x){
        this.x  = x;
    }
    public void setY(Double y){
        this.y = x ;
    }
    
}
