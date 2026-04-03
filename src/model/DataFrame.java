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
        String Total = "Liste des poissons :\n" ;
       for(T poisson : poissons)
            Total += poisson.toString() ;
        return Total.equals("Liste des poissons :\n")? Total +"empty":Total ;
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
            if(poisson instanceof Fish){
                Fish fish = (Fish)poisson ;
                InfestationRate.add(fish.getInfestationRate());
            }else if(poisson instanceof Population){
                Population population = (Population)poisson ;
                InfestationRate.add(population.getInfestationRate());
            }
                
        }
        return InfestationRate ;
    }


    public ArrayList<Double> getSizes(){
        ArrayList<Double> Size = new ArrayList<>() ;
        for(T poisson : poissons){
             if(poisson instanceof Fish){
                Fish fish = (Fish)poisson ;
                Size.add(fish.getSize());
            }else if(poisson instanceof Population){
                Population population = (Population)poisson ;
                Size.add(population.getMeanSize());
            }
            
        }
        return Size ;
    }


    public ArrayList<Double> getLengths(){
        ArrayList<Double> length = new ArrayList<>() ;
        for(T poisson : poissons){
            if(poisson instanceof Fish){
                Fish fish = (Fish)poisson ;
                length.add(fish.getLength());
            }else if(poisson instanceof Population){
                Population population = (Population)poisson ;
                length.add(population.getMeanLength());
            }
        }
        return  length ;
    }

     public ArrayList<Double> getWeights(){
        ArrayList<Double> weigth = new ArrayList<>() ;
        for(T poisson : poissons){
             if(poisson instanceof Fish){
                Fish fish = (Fish)poisson ;
                weigth.add(fish.getInfestationRate());
            }else if(poisson instanceof Population){
                Population population = (Population)poisson ;
                weigth.add(population.getMeanWeight());
            }
        }
        return  weigth;
    }


     public HashSet<String> getSpecies(){
        HashSet<String> species = new HashSet<>() ;
        for(T poisson : poissons){
             if(poisson instanceof Fish){
                Fish fish = (Fish)poisson ;
                species.add(fish.getSpecies());
            }else if(poisson instanceof Population){
                Population population = (Population)poisson ;
                species.add(population.getName());
            }
        }
        return species;
    }

    public HashSet<String> getContents(){
        HashSet<String> content = new  HashSet<>() ;
        for(T poisson : poissons){
            if(poisson instanceof Fish){
                Fish fish = (Fish)poisson ;
                HashSet<String> fishContent = fish.getContent() ;
                for(String fishCont : fishContent){
                    content.add(fishCont); 
                }
            }else if(poisson instanceof Population){
                Population population = (Population)poisson ;
                String fishContent = population.getContent() ;
                content.add(fishContent);
            }
        }
        return  content;
    }



    public ArrayList<T> getSpecies(String espece){
        ArrayList<T> res = new ArrayList<>() ;
        for(T value : poissons){
            if(value instanceof Fish){
                Fish poisson = (Fish)value ;
                if(poisson.getSpecies().equals(espece))res.add(value);
            }else{
                Population pop = (Population)value ;
                if(pop.getName().equals(espece))res.add(value);

            }
        }
        return res ;
    }


}           
