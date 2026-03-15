package model;

import java.io.IOException;
import java.util.ArrayList;
import exceptions.InvalidFileFormat;
import files.Files;


public class DataFrame {

    private ArrayList<Fish> poissons ;

    public DataFrame(ArrayList<Fish> poissons){
        this.poissons = poissons ;
    }

    public DataFrame(){

    }

    public void readcsv(String fileName) throws InvalidFileFormat{

        if(!Files.getExtension(fileName).equals("csv")) throw new InvalidFileFormat();

        ArrayList<String> lines = null ;
        try{
            lines = Files.getFile(fileName) ;
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        if(lines == null){
            // Pourquoi: éviter un NullPointerException si la lecture échoue.
            throw new InvalidFileFormat("Impossible de lire le fichier CSV : " + fileName);
        }
        int i = 0 ;
        poissons = new ArrayList<>() ;
        for(String line : lines){
            poissons.add(new Fish(null,null , null , null )) ;
            String[] data = line.split(",", -1) ;
            if(data.length!= 6){
                // Pourquoi: une ligne mal formée ne doit pas être acceptée silencieusement.
                throw new InvalidFileFormat("Ligne invalide (6 champs attendus) : " + line);
            }
            Fish poisson = poissons.get(i);
            if(data[0].equals(""))poisson.setSpecies(null);
            else poisson.setSpecies(data[0]);
            poisson.setLength(parseDouble(data[1], line));
            poisson.setWeight(parseDouble(data[2], line));
            poisson.setSize(parseDouble(data[3], line));
            poisson.setInfestationRate(parseDouble(data[4], line));
            String[] contenu = data[5].split(";;");
            for(String cont : contenu){
                if(!cont.equals("")) poisson.addContent(cont);
            }
            i++ ;
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
        return Total ;
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








}           
