package tests;


import java.util.ArrayList;
import exceptions.InvalidFileFormat;
import model.DataFrame;
import model.Fish;
import tools.BoiteAMoustaches;
import traitements.LinearRegression;
import traitements.MeanValue;
import traitements.Traitement;

public class Main {

    public static void main(String[] args){
        DataFrame df  = new DataFrame();
        try{
            df.readcsv("anis.csv"); 
        }catch( InvalidFileFormat e){
            System.out.println(e.getMessage());
        }

        System.out.println(df);
        Traitement t = new MeanValue();
        t.complete(df.getData());
        
        /*try{
            t.complete(df.getData());
        }catch(InvalidLinearRegression e){
            System.out.print(e);
        }*/
        System.out.println(df);
        

    }
    
}
