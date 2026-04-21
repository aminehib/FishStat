package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import exceptions.InvalidAttribute;
import exceptions.InvalidFileFormat;

import tools.MeanValue;


public class DataFrame<T extends Data>  {

    private ArrayList<T> poissons ;


    
    public DataFrame(ArrayList<T> poissons){
        this.poissons = poissons ;
    }

    public DataFrame(){
        this.poissons = new ArrayList<>();
    }

    

    public void setData(ArrayList<T> poissons){
        this.poissons = poissons ;
    }

    public ArrayList<T> getData(){return this.poissons ;}

    public String toString(){
        String Total =  "Liste des poissons :\n" ;
        if(poissons == null)return Total +"empty\n" ;
       for(T poisson : poissons)
            if(poisson != null)
                Total += poisson.toString() ;
        return Total.equals("Liste des poissons :\n")? Total +"empty\n":Total ;
    }

    private Double parseDouble(String value, String line) throws InvalidFileFormat{
        if(value == null || value.equals("")) return null;
        try{
            return Double.parseDouble(value);
        }catch(NumberFormatException e){
            // Pourquoi: signaler clairement une valeur numérique invalide dans le CSV.
            throw new InvalidFileFormat("Valeur numérique invalide: \"" + value + "\" dans la ligne: " + line);
        }
    }

    public ArrayList<Double> getInfestationRates(){

        ArrayList<Double> InfestationRate = new ArrayList<>() ;
        for(T poisson : poissons){
            InfestationRate.add(poisson.getInfestationRate());                
        }
        return InfestationRate ;
    }


    public ArrayList<Double> getSizes(){
        ArrayList<Double> Size = new ArrayList<>() ;
        for(T poisson : poissons){
            Size.add(poisson.getSize());                
        }
        return Size ;
    }


    public ArrayList<Double> getLengths(){
        ArrayList<Double> length = new ArrayList<>() ;
        for(T poisson : poissons){
            length.add(poisson.getLength());
        }
        return  length ;
    }

     public ArrayList<Double> getWeights(){
        ArrayList<Double> weigth = new ArrayList<>() ;
        for(T poisson : poissons){
            weigth.add(poisson.getWeight());
        }
        return  weigth;
    }


     public HashSet<String> getSpecies(){
        HashSet<String> species = new HashSet<>() ;
        for(T poisson : poissons){
            species.add(poisson.getSpecies());
        }
        return species;
    }

    public HashSet<String> getContents(){
        HashSet<String> content = new  HashSet<>() ;
        for(T poisson : poissons){
            
                HashSet<String> fishContent = poisson.getContent() ;
                for(String fishCont : fishContent){
                    content.add(fishCont); 
                }
           
        }
        return  content;
    }



    public ArrayList<T> getSpecies(String espece){
        ArrayList<T> res = new ArrayList<>() ;
        for(T poisson : poissons){
          
            if(poisson.getSpecies().equals(espece))res.add(poisson);
            
        }
        return res ;
    }




}           
