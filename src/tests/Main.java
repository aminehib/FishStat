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
import traitements.KmeansCompletion;
import traitements.LinearRegressionCompletion;
import traitements.MeanValueCompletion;

import traitements.*;
import tools.*;


public class Main {

    public static void main(String[] args){
        
        
        DataFrame<Fish> df  = new DataFrame<>();
        LinkedHashMap<String , String > header = new LinkedHashMap<>() ;

        LinkedHashMap<String , String > head = new LinkedHashMap<>() ;

        //head.put("N", "Total");
    


        header.put("espece", "Species");
        //header.put("NParasitesTotal", "Total_parasites");
        header.put("longueur_cm","Length") ;
        header.put("taille_cm","Size");
        header.put("poids_kg","Weight");
        header.put("taux_infestation","InfestationRate");
        header.put("nombre_parasites", "Total_parasites");
        

        try{
            df.setData(CsvReader.readCsv("src/tests.csv" ,",",";",1 , header, Fish.class)) ;
            System.out.println(df);

        }catch(Exception e){
            e.printStackTrace();
        }
       
        for(Fish p : df.getData()){
            p.setSpecies("mackerel");
        }
        System.out.println(df);


        Double[] errors = {0.0,0.1,0.0,0.1,0.1};
        
        Traitement t = new RegressionCompletion();

        t.clean(df ,errors );
        System.out.println("completion");
        t.complete(df);
        System.out.println(df);

        Population pop = new Population(df);
        System.out.println(pop);

        //new LinearRegressionCompletion().Complete(df, "InfestationRate", "Parasites");

       System.out.println(df);

        


        SvgGenerator.GenerateSVG(df,"Size","InfestationRate",1600,1600,50,100,100,2);

        LinearRegression model = new LinearRegression(df.getSizes(), df.getInfestationRates());
        System.out.println(model.getCoeff()+ " " + model.getIntercept() + " "+ df.getSpecies().size()); 
        
        

       
        
    }

    
}
