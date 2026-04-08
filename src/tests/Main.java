package tests;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import exceptions.InvalidAttribute;
import exceptions.InvalidFileFormat;
import files.CsvReader;
import graphique.SvgGenerator;

import model.DataFrame;
import model.Fish;
import model.Population;
import tools.BoiteAMoustaches;
import tools.LinearRegression;
import traitements.LinearRegressionCompletion;
import traitements.MeanValueCompletion;

import traitements.Traitement;


public class Main {

    public static void main(String[] args){
        
        
        DataFrame<Fish> df  = new DataFrame<>();
        LinkedHashMap<String , String > header = new LinkedHashMap<>() ;

        LinkedHashMap<String , String > head = new LinkedHashMap<>() ;

        //head.put("N", "Total");
    


        header.put("espece", "Species");
        header.put("longueur","Length") ;
        header.put("Taille","Size");
        header.put("Poids","Weight");
        header.put("TI","InfestationRate");
        header.put("contenu", "Content");


       




        try{
            df.setData(CsvReader.readCsv("src/anis.csv",";","," , header, Fish.class)) ;
            System.out.print(df);
        }catch(Exception e){
            e.printStackTrace();
        }
       Population pop = new Population(new DataFrame<>(df.getSpecies("Thon")));
       System.out.println(pop);
        
        
        
        /*Double[] errors = {0.0,0.1,0.0,0.1};
        System.out.println(df.toString());
        Traitement t = new LinearRegressionCompletion();
        t.clean(df ,errors );
        t.complete(df);

        System.out.println(df);

        SvgGenerator.GenerateSVG(df);

        LinearRegression model = new LinearRegression(df.getSizes(), df.getInfestationRates());
        System.out.println(model.getCoeff()+ " " + model.getIntercept() + " "+ df.getSpecies().size());    */
        
        
    }


    
}
