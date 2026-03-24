package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import exceptions.InvalidAttribute;
import exceptions.InvalidFileFormat;

import tools.MeanValue;


public class DataFrame {

    private ArrayList<Fish> poissons ;

    public DataFrame(ArrayList<Fish> poissons){
        this.poissons = poissons ;
    }

    public DataFrame(){
        this.poissons = new ArrayList<>();
    }

    public void readcsv(String name, LinkedHashMap<String , String > headers ) throws InvalidFileFormat, InvalidAttribute{

      if(headers == null || headers.size() == 0 ) return ;
        FileReader fileReader = null  ;
        
        try{
        fileReader = new FileReader(name)  ;

        LinkedHashMap<String ,Integer > headerIndex = new LinkedHashMap<>() ;
        
        BufferedReader buffer  = new BufferedReader(fileReader) ;
        String line ;
        ArrayList<String> lines  = new ArrayList<>() ;

        line = buffer.readLine();

        String header[] = line.split(";");
        lines.add(String.join(",", header));

        for(int i = 0 ; i < header.length; i++){
            if(headers.containsKey(header[i])){
                headerIndex.put( headers.get(header[i] ) , i);
                System.out.println(header[i]+ " trouvé");
            }
            else{
                System.out.println(header[i]+ " ignoré");
            }
        }
        if(headerIndex.size() ==0){
            System.out.println("Erreur:Aucune colonne ne correspond");
            return ;
        }
        poissons = new ArrayList<>() ;
        while( (line = buffer.readLine() ) != null){
            Fish poisson = new Fish(null, null, null, null, null) ;
            String[] ligne = line.split(";", -1);
            for(String key : headerIndex.keySet()){
                switch (key) {
                    case "Species":
                        
                        poisson.setSpecies(ligne[headerIndex.get(key)]) ;
                        break;

                    case "Length":
                        if(ligne[headerIndex.get(key)].equals(""))poisson.setLength(null);
                        else poisson.setLength(Double.parseDouble(ligne[headerIndex.get(key)]));
                        break;

                    case "Weight":
                        if(ligne[headerIndex.get(key)].equals(""))poisson.setLength(null);
                        else poisson.setWeight(Double.parseDouble(ligne[headerIndex.get(key)]));
                        break;
                    
                    case "Size":
                        if(ligne[headerIndex.get(key)].equals(""))poisson.setLength(null);
                        else poisson.setSize(Double.parseDouble(ligne[headerIndex.get(key)]));
                        break;
                    
                    case "InfestationRate":
                        if(ligne[headerIndex.get(key)].equals(""))poisson.setLength(null);
                        else poisson.setInfestationRate(Double.parseDouble(ligne[headerIndex.get(key)]));
                        break;
                    
                    case "Content":
                       poisson.addContent(ligne[headerIndex.get(key)]);
                        break;
                    
                    default:
                        throw new InvalidAttribute(key);
                   
                }
            }
           poissons.add(poisson);
        }


        fileReader.close();
        buffer.close();

    }catch(IOException e){
        System.out.println(e.getMessage());
    }
        
        
    }

    public void setData(ArrayList<Fish> poissons){
        this.poissons = poissons ;
    }

    public ArrayList<Fish> getData(){return this.poissons ;}

    public String toString(){
        String Total = "Liste des poissons :\n" ;
       for(Fish poisson : poissons)
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
        for(Fish poisson : poissons){
            InfestationRate.add(poisson.getInfestationRate());
        }
        return InfestationRate ;
    }


    public ArrayList<Double> getSizes(){
        ArrayList<Double> Size = new ArrayList<>() ;
        for(Fish poisson : poissons){
            Size.add(poisson.getSize());
        }
        return Size ;
    }


    public ArrayList<Double> getLengths(){
        ArrayList<Double> length = new ArrayList<>() ;
        for(Fish poisson : poissons){
            length.add(poisson.getLength());
        }
        return  length ;
    }

     public ArrayList<Double> getWeights(){
        ArrayList<Double> weigth = new ArrayList<>() ;
        for(Fish poisson : poissons){
            weigth.add(poisson.getWeight());
        }
        return  weigth;
    }


     public HashSet<String> getSpecies(){
        HashSet<String> species = new HashSet<>() ;
        for(Fish poisson : poissons){
            species.add(poisson.getSpecies());
        }
        return species;
    }

    public HashSet<String> getContents(){
        HashSet<String> content = new  HashSet<>() ;
        for(Fish poisson : poissons){
            HashSet<String> fishContent = poisson.getContent() ;
            for(String fishCont : fishContent){
                content.add(fishCont); 
            }
        }
        return  content;
    }



}           
