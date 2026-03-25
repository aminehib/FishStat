package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import exceptions.InvalidFileFormat;
import files.Files;


public class DataFrame {

    private ArrayList<Fish> poissons ;

    public DataFrame(ArrayList<Fish> poissons){
        this.poissons = poissons ;
    }

    public DataFrame(){
        this.poissons = new ArrayList<>();
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
            poissons.add(new Fish(null,null ,null , null , null )) ;
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

    // ----------------------------
    // Chargements spécifiques datasets
    // ----------------------------

    public void readProcessedAnisakis(String fileName) throws InvalidFileFormat{
        if(!Files.getExtension(fileName).equals("csv")) throw new InvalidFileFormat();

        ArrayList<String> lines = null;
        try{
            lines = Files.getFile(fileName);
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        if(lines == null || lines.isEmpty()){
            throw new InvalidFileFormat("Fichier vide : " + fileName);
        }

        String header = lines.get(0);
        int idxSpecies = Files.indexOfColumn(header, ",", "TrueHost");
        int idxSize = Files.indexOfColumn(header, ",", "Standard_LengthCalc");
        int idxInfest = Files.indexOfColumn(header, ",", "Standard_Abundance");

        poissons = new ArrayList<>();
        for(int i = 1; i < lines.size(); i++){
            String line = lines.get(i);
            if(line == null || line.trim().isEmpty()) continue;
            String[] data = Files.splitLine(line, ",");

            String species = getCell(data, idxSpecies);
            if(species.equals("")) species = null;
            Double size = parseDouble(getCell(data, idxSize), line);
            Double infestation = parseDouble(getCell(data, idxInfest), line);
            if(infestation != null && infestation == 0.0){
                infestation = null;
            }

            Fish fish = new Fish(species, null, null, size, infestation);
            fish.addContent("unknown");
            poissons.add(fish);
        }
    }

    public void readMackerelCompagne(String fileName) throws InvalidFileFormat{
        if(!Files.getExtension(fileName).equals("csv")) throw new InvalidFileFormat();

        ArrayList<String> lines = null;
        try{
            lines = Files.getFile(fileName);
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        if(lines == null || lines.isEmpty()){
            throw new InvalidFileFormat("Fichier vide : " + fileName);
        }

        String header = lines.get(0);
        int idxSize = Files.indexOfColumn(header, ";", "StandardLength");
        int idxInfest = Files.indexOfColumn(header, ";", "NParasitesTotal");

        poissons = new ArrayList<>();
        for(int i = 1; i < lines.size(); i++){
            String line = lines.get(i);
            if(line == null || line.trim().isEmpty()) continue;
            String[] data = Files.splitLine(line, ";");

            Double size = parseDouble(getCell(data, idxSize), line);
            Double infestation = parseDouble(getCell(data, idxInfest), line);

            Fish fish = new Fish("mackerel", null, null, size, infestation);
            fish.addContent("unknown");
            poissons.add(fish);
        }
    }

    public void readMerluCompagne(String fileName) throws InvalidFileFormat{
        if(!Files.getExtension(fileName).equals("csv")) throw new InvalidFileFormat();

        ArrayList<String> lines = null;
        try{
            lines = Files.getFile(fileName);
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        if(lines == null || lines.isEmpty()){
            throw new InvalidFileFormat("Fichier vide : " + fileName);
        }

        String header = lines.get(0);
        int idxSize = Files.indexOfColumn(header, ";", "m.size (mm)");
        int idxInfest = Files.indexOfColumn(header, ";", "number of Anisakis sp L3");

        poissons = new ArrayList<>();
        for(int i = 1; i < lines.size(); i++){
            String line = lines.get(i);
            if(line == null || line.trim().isEmpty()) continue;
            String[] data = Files.splitLine(line, ";");

            Double size = parseDouble(getCell(data, idxSize), line);
            Double infestation = parseDouble(getCell(data, idxInfest), line);

            Fish fish = new Fish("merlu", null, null, size, infestation);
            fish.addContent("unknown");
            poissons.add(fish);
        }
    }

    private String getCell(String[] data, int idx){
        if(data == null || idx < 0 || idx >= data.length) return "";
        return data[idx].trim();
    }

    // Détecte automatiquement le dataset via les en-têtes et charge le bon format.
    public void readAuto(String fileName) throws InvalidFileFormat{
        if(!Files.getExtension(fileName).equals("csv")) throw new InvalidFileFormat();

        ArrayList<String> lines = null;
        try{
            lines = Files.getFile(fileName);
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        if(lines == null || lines.isEmpty()){
            throw new InvalidFileFormat("Fichier vide : " + fileName);
        }

        String header = lines.get(0);

        if(hasColumns(header, ",", new String[]{"TrueHost","Standard_LengthCalc","Standard_Abundance"})){
            readProcessedAnisakis(fileName);
            return;
        }

        if(hasColumns(header, ";", new String[]{"StandardLength","NParasitesTotal"})){
            readMackerelCompagne(fileName);
            return;
        }

        if(hasColumns(header, ";", new String[]{"m.size (mm)","number of Anisakis sp L3"})){
            readMerluCompagne(fileName);
            return;
        }

        throw new InvalidFileFormat("Dataset inconnu (en-têtes non reconnus): " + fileName);
    }

    private boolean hasColumns(String headerLine, String delimiter, String[] cols){
        String[] headers = Files.splitLine(headerLine, delimiter);
        for(String col : cols){
            boolean found = false;
            for(int i = 0; i < headers.length; i++){
                String h = headers[i].trim();
                if(i == 0){
                    h = h.replace("\uFEFF", "");
                }
                if(h.equals(col)){
                    found = true;
                    break;
                }
            }
            if(!found) return false;
        }
        return true;
    }



}           
