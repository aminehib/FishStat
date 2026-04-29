package tests;

import java.util.LinkedHashMap;

import files.CsvReader;
import graphique.SvgGenerator;
import tools.LinearRegression;
import traitements.KmeansCompletion;
import traitements.Traitement;
import model.* ;

public class TestPopulation1 {

    public static void main(String[] args){
        
        
        DataFrame<Population> df  = new DataFrame<>();
        LinkedHashMap<String , String > header = new LinkedHashMap<>() ;

        LinkedHashMap<String , String > head = new LinkedHashMap<>() ;

        head.put("N", "Total");
        head.put("Poids moyen ± SD (g)","MeanWeight");
        head.put("Longueur moyenne ± SD (cm)","MeanLength");
        head.put("Prévalence (%)","InfestationRate") ;
        head.put("Intensité moyenne (étendue)","Intensity") ;


        header.put("Espèce", "Species");
        //header.put("NParasitesTotal", "Total_parasites");
        header.put("Paramètre","Parameter") ;
        header.put("Total","Value");
        

        try{
            df.setData(CsvReader.readCsv("src/v.csv" ,",",";",100 , header,head,7, Population.class)) ;
            System.out.println(df);

        }catch(Exception e){
            e.printStackTrace();
        }
       
        SvgGenerator.GenerateSVG(df,"Length","InfestationRate",1600,1600,"Droite de régression",50,50,50,1);

        LinearRegression model = new LinearRegression(df.getSizes(), df.getInfestationRates());
        System.out.println(model.getCoeff()+ " " + model.getIntercept() + " "+ df.getSpecies().size()); 
        
        

       
        
    }
    
}
