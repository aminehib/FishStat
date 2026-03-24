package tests;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import exceptions.InvalidAttribute;
import exceptions.InvalidFileFormat;
import graphique.SvgGenerator;
import model.DataFrame;
import model.Fish;
import tools.BoiteAMoustaches;
import tools.LinearRegression;
import traitements.LinearRegressionCompletion;
import traitements.MeanValueCompletion;


import traitements.Traitement;

public class Main {

    public static void main(String[] args){
        
        
        DataFrame df  = new DataFrame();
        LinkedHashMap<String , String > header = new LinkedHashMap<>() ;
        header.put("Sample_code", " Species");
        /*header.put("longueur", "Length");
        header.put("Poids", "Weight");
        header.put("Taille", "Size");
        header.put("TI", "InfestationRate");
        header.put("contenu", "Content");*/
        
        
        try{
            df.readcsv("src/mackerel.97442.csv" ,header); 
        }catch( InvalidFileFormat | InvalidAttribute e){
            System.out.println(e.getMessage());
        }
        
        System.out.println(df);
        Traitement t = new MeanValueCompletion();
        //t.clean(df);
        t.complete(df);

        System.out.println(df);

        SvgGenerator.GenerateSVG(df);

        LinearRegression model = new LinearRegression(df.getSizes(), df.getInfestationRates());
        System.out.println(model.getCoeff()+ " " + model.getIntercept() + " "+ df.getSpecies().size());    

    }
    
}
