package tests;


import java.util.ArrayList;
import exceptions.InvalidFileFormat;
import graphique.SvgGenerator;
import model.DataFrame;
import model.Fish;
import tools.BoiteAMoustaches;
import tools.LinearRegression;
import traitements.LinearRegressionCompletion;

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
        Traitement t = new LinearRegressionCompletion();
        t.clean(df);
        t.complete(df);


        
        
        System.out.println(df);

        SvgGenerator.GenerateSVG(df);

        LinearRegression model = new LinearRegression(df.getSizes(), df.getInfestationRates());
        System.out.println(model.getCoeff()+ " " + model.getIntercept());
        

    }
    
}
