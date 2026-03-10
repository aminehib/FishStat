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
        int i = 0 ;
        poissons = new ArrayList<>() ;
        for(String line : lines){
            poissons.add(new Fish(null,null , null , null )) ;
            String[] data = line.split(",") ;
            if(data.length!= 6) ;
            Fish poisson = poissons.get(i);
            if(data[0].equals(""))poisson.setSpecies(null);
            else poisson.setSpecies(data[0]);
            if(data[1].equals(""))poisson.setLength(null);
            else poisson.setLength(Double.parseDouble(data[1]));
            if(data[2].equals(""))poisson.setWeight(null);
            else poisson.setWeight(Double.parseDouble(data[2]));
            if(data[3].equals(""))poisson.setSize(null);
            else poisson.setSize(Double.parseDouble(data[3]));
            if(data[4].equals(""))poisson.setInfestationRate(null);
            else poisson.setInfestationRate(Double.parseDouble(data[4]));
            String[] contenu = data[5].split(";;");
            for(String cont : contenu){
                poisson.addContent(cont); ;
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








}           