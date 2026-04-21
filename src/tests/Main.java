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
        
        
        DataFrame<Population> df  = new DataFrame<>();
        LinkedHashMap<String , String > header = new LinkedHashMap<>() ;

        LinkedHashMap<String , String > head = new LinkedHashMap<>() ;

        //head.put("N", "Total");
    


        header.put("TrueHost", "Species");
        header.put("Total_Fish_Examined", "Total");
        header.put("Standard_LengthCalc","MeanLength") ;
        //header.put("Taille","Size");
        //header.put("Poids","Weight");
        //header.put("TI","InfestationRate");
        header.put("Portion_of_Body", "Content");


       

        try{
            df.setData(CsvReader.readCsv("/home/anis/Downloads/Données collectées DRYAD-20260318/processed_data_anisakis.csv" ,",",";",1 , header, Population.class)) ;
            System.out.print(df);
        }catch(Exception e){
            e.printStackTrace();
        }
       
        
        
        Double[] errors = {0.0,0.1,0.0,0.1};
        System.out.println(df.toString());
        LinearRegressionCompletion t = new LinearRegressionCompletion();
        /*t.clean(df ,errors );
        System.out.println("completion");
        t.Complete(df , "Size" , "InfestationRate");*/

        System.out.println(df);


        

        SvgGenerator.GenerateSVG(df);

        LinearRegression model = new LinearRegression(df.getSizes(), df.getInfestationRates());
        System.out.println(model.getCoeff()+ " " + model.getIntercept() + " "+ df.getSpecies().size());    
        
        
        
    }


    
}
