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


        header.put("espece", "Species");



        header.put("longueur", "Length");
        header.put("length","Lenght");
        header.put("Poids", "Weight");
        header.put("Taille", "Size");
        header.put("TI", "InfestationRate");
        header.put("contenu", "Content");


        try{
            df.setData(CsvReader.readCsv("src/anis.csv",";" , header, Fish.class)) ;
            System.out.print(df);
        }catch(Exception e){
            System.out.print(e);
        }
        
        
        
        Double[] errors = {0.0,0.1,0.0,0.1};
        System.out.println(df.toString());
        Traitement t = new MeanValueCompletion();
        t.clean(df ,errors );
        t.complete(df);

        System.out.println(df);

        SvgGenerator.GenerateSVG(df);

        LinearRegression model = new LinearRegression(df.getSizes(), df.getInfestationRates());
        System.out.println(model.getCoeff()+ " " + model.getIntercept() + " "+ df.getSpecies().size());    
        
    }


    
}
