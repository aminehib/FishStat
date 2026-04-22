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
    


        header.put("Sample_code", "Species");
        header.put("NParasitesTotal", "Total_parasites");
        //header.put("Standard_LengthCalc","MeanLength") ;
        //header.put("Taille","Size");
        //header.put("Poids","Weight");
        //header.put("TI","InfestationRate");
        //header.put("Portion_of_Body", "Content");
        

        try{
            df.setData(CsvReader.readCsv("src/mackerel.97442.csv" ,";",",",1 , header, Fish.class)) ;
            System.out.println(df);

        }catch(Exception e){
            e.printStackTrace();
        }
       
        for(Fish p : df.getData()){
            p.setSpecies("mackerel");
        }
         System.out.println(df);

               
        Double[] errors = {0.0,0.1,0.0,0.1};
        
        Traitement t = new MeanValueCompletion();

        t.clean(df ,errors );
        System.out.println("completion");
        t.complete(df);
        System.out.println(df);
       

        


        SvgGenerator.GenerateSVG(df);

        LinearRegression model = new LinearRegression(df.getSizes(), df.getInfestationRates());
        System.out.println(model.getCoeff()+ " " + model.getIntercept() + " "+ df.getSpecies().size());    
        
    }

    
}
