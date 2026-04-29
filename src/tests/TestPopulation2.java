package tests;

import java.util.LinkedHashMap;

import files.CsvReader;
import graphique.SvgGenerator;
import model.DataFrame;
import model.Population;
import tools.LinearRegression;

public class TestPopulation2 {
     public static void main(String[] args){
        
        
        DataFrame<Population> df  = new DataFrame<>();
        LinkedHashMap<String , String > header = new LinkedHashMap<>() ;

        LinkedHashMap<String , String > head = new LinkedHashMap<>() ;

    


        header.put("Espèce", "Species");
        //header.put("NParasitesTotal", "Total_parasites");
        header.put("Prevalence","InfestationRate") ;
        header.put("LT mm","MeanLength");
        header.put("Masse g","MeanWeight");
        header.put("N", "Total") ;

        

        try{
            df.setData(CsvReader.readCsv("src/test3.csv" ,",",";",100 , header, Population.class)) ;
            System.out.println(df);

        }catch(Exception e){
            e.printStackTrace();
        }
       
        SvgGenerator.GenerateSVG(df,"Length","InfestationRate",1600,1600,"Droite de régression",50,50,50,1);
        LinearRegression model = new LinearRegression(df.getSizes(), df.getInfestationRates());
        System.out.println(model.getCoeff()+ " " + model.getIntercept() + " "+ df.getSpecies().size()); 
    }
}
