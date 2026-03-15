package tests;


import java.util.ArrayList;
import exceptions.InvalidFileFormat;
import graphique.SvgGenerator;
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
            df.readcsv("src/anis.csv"); 
        }catch( InvalidFileFormat e){
            System.out.println(e.getMessage());
        }
        
        System.out.println(df);
        Traitement t = new LinearRegression();
        t.clean(df.getData());
        t.complete(df.getData());
        
        
        System.out.println(df);

        SvgGenerator.GenerateSVG(df.getData());
        

    }
    
}
