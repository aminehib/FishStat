package files;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import exceptions.InvalidAttribute;
import exceptions.InvalidFileFormat;
import model.Data;
import model.Fish;
import model.Population;

public class CsvReader {

    private static String[] fishAttributes = {"Species","Length","Weight","Size","Total_parasites" ,"InfestationRate","Content"};
    private static String[] populationAttributes = {"Name","Total","MeanLength","MeanWeight","MeanSize","Total_parasites" ,"InfestationRate","Intensity","Content"};

    
//utiliser equale 

    public static <T extends Data> ArrayList<T> readCsv(String name ,String split ,String multipleSplit ,int poucentage, LinkedHashMap<String , String > headers  , Class<T> type ) throws InvalidFileFormat, InvalidAttribute{

      if(headers == null || headers.size() == 0 ) return null ;
      ArrayList<T> result  = new ArrayList<>() ;
        
        
    try( FileReader fileReader = new FileReader(name)  ; BufferedReader buffer  = new BufferedReader(fileReader) ;){
        

        LinkedHashMap<String ,Integer > headerIndex = new LinkedHashMap<>() ;
        
        
        
        String line ;
        ArrayList<String> lines  = new ArrayList<>() ;

        line = buffer.readLine();

            String header[] = line.split(split);
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

            if(headerIndex.isEmpty()){
                System.out.println("Erreur:Aucune colonne ne correspond");
                return null;
            }

            
            while( (line = buffer.readLine() ) != null){
            if(line.equals(""))continue;
        
                Fish poisson = null;
                Population population = null;
                if(type.equals(Fish.class)) poisson = new Fish(null, null, null, null, null) ;
                else population = new Population(null,null,null) ;
                String[] ligne = line.split(split, -1);
                for(String key : headerIndex.keySet()){
                    if(type.equals(Fish.class)){
                    switch (key) {
                        case "Species":
                            poisson.setSpecies(ligne[headerIndex.get(key)]) ;
                            break;

                        case "Length":
                            if(ligne[headerIndex.get(key)].equals(""))poisson.setLength(null);
                            else poisson.setLength(Double.parseDouble(ligne[headerIndex.get(key)]));
                            break;

                        case "Weight":
                            if(ligne[headerIndex.get(key)].equals(""))poisson.setWeight(null);
                            else poisson.setWeight(Double.parseDouble(ligne[headerIndex.get(key)]));
                            break;
                    
                        case "Size":
                            if(ligne[headerIndex.get(key)].equals(""))poisson.setSize(null);
                            else poisson.setSize(Double.parseDouble(ligne[headerIndex.get(key)]));
                            break;

                        case "Total_parasites":
                            if(ligne[headerIndex.get(key)].equals(""))poisson.setParasites(null);
                            else poisson.setParasites((poisson.getParasites() == null )? (int) Double.parseDouble(ligne[headerIndex.get(key)]): (int)Double.parseDouble(ligne[headerIndex.get(key)]) + poisson.getParasites());
                            break;
                    
                        case "InfestationRate":
                            if(ligne[headerIndex.get(key)].equals(""))poisson.setInfestationRate(null);
                            else poisson.setInfestationRate(Double.parseDouble(ligne[headerIndex.get(key)])/poucentage);
                            break;
                        
                    
                        case "Content":
                            String[] contents = ligne[headerIndex.get(key)].split(multipleSplit);
                            for(String content : contents){
                                poisson.addContent(content);
                            }
                            break;
                    
                        default:
                            throw new InvalidAttribute(key,fishAttributes);
                   
                    }
                    
                }else{
                    
                     switch (key) {
                        case "Species":
                        
                            population.setName(ligne[headerIndex.get(key)]) ;
                            break;

                        case "MeanLength":
                            if(ligne[headerIndex.get(key)].equals(""))population.setMeanLength(null);
                            else population.setMeanLength(Double.parseDouble(ligne[headerIndex.get(key)]));
                            break;

                        case "MeanWeight":
                            if(ligne[headerIndex.get(key)].equals(""))population.setMeanWeight(null);
                            else population.setMeanWeight(Double.parseDouble(ligne[headerIndex.get(key)]));
                            break;
                    
                        case "MeanSize":
                            if(ligne[headerIndex.get(key)].equals(""))population.setMeanSize(null);
                            else population.setMeanSize(Double.parseDouble(ligne[headerIndex.get(key)]));
                            break;
                    
                        case "InfestationRate":
                            if(ligne[headerIndex.get(key)].equals(""))population.setInfestationRate(null);
                            else population.setInfestationRate(Double.parseDouble(ligne[headerIndex.get(key)])/poucentage);
                            break;
                        
                        case "Intensity":
                            if(ligne[headerIndex.get(key)].equals(""))population.setIntensity(null);
                            else population.setIntensity(Double.parseDouble(ligne[headerIndex.get(key)]));
                            break;
                        
                        case "Total":
                            if(ligne[headerIndex.get(key)].equals(""))population.setTotal(null);
                            else population.setTotal(Integer.parseInt(ligne[headerIndex.get(key)]));
                            break;   

                        case "Total_parasites":
                            if(ligne[headerIndex.get(key)].equals(""))population.setParasites(null);
                            else population.setParasites( (population.getParasites() == null)? (int)Double.parseDouble(ligne[headerIndex.get(key)]) : (int) Double.parseDouble(ligne[headerIndex.get(key)]) + poisson.getParasites());
                            break;  
                    
                        case "Content":
                            String[] contents = ligne[headerIndex.get(key)].split(multipleSplit);
                            for(String content : contents){
                                population.addContent(content);
                            }
                            break;
                    
                        default:
                            throw new InvalidAttribute(key,populationAttributes);
                   
                    }

                    
                }
                
            }
            result.add((poisson==null)?type.cast(population) :type.cast(poisson));
        }
        
           
    }catch(IOException e){
        System.out.println(e.getMessage());
    }
        
    return result ;

    
}


public static <T extends Data> ArrayList<T> readCsvFormat(String name ,String split ,String multipleSplit ,int poucentage, LinkedHashMap<String , String > headers ,LinkedHashMap<String,String> params,int N , Class<T> type ) throws InvalidFileFormat, InvalidAttribute{


      if(headers == null || headers.size() == 0 ) return null ;
      ArrayList<T> result  = new ArrayList<>() ;
      String param = null ;
        
        
    try( FileReader fileReader = new FileReader(name)  ; BufferedReader buffer  = new BufferedReader(fileReader) ;){
        

        LinkedHashMap<String ,Integer > headerIndex = new LinkedHashMap<>() ;
        
        
        String line ;
        ArrayList<String> lines  = new ArrayList<>() ;

        line = buffer.readLine();

            String header[] = line.split(split);
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

            if(headerIndex.isEmpty()){
                System.out.println("Erreur:Aucune colonne ne correspond");
                return null;
            }

            Fish poisson = null;
            Population population = null;
            int counter = 0 ;
            
             while( (line = buffer.readLine() ) != null){
                if(counter == N)counter = 0 ;
                
                if(counter == 0){
                if(type.equals(Fish.class)) poisson = new Fish(null, null, null, null, null) ;
                else population = new Population(null,null,null) ;
                }
                String[] ligne = line.split(split, -1);
                for(String key : headerIndex.keySet()){
                    if(type.equals(Fish.class)){
                    switch (key) {
                        case "Species":
                        
                            poisson.setSpecies(ligne[headerIndex.get(key)]) ;
                            break;

                        case "Parametre":
                            param  = ligne[headerIndex.get(key)] ;
                            break;
                        
                        case "Value":
                            
                            if(params.containsKey(param)){
                                switch (params.get(param)) {
                                   

                                 case "Length":
                                    if(ligne[headerIndex.get(key)].equals(""))poisson.setLength(null);
                                        else poisson.setLength(Double.parseDouble(ligne[headerIndex.get(key)]));
                                        break;

                        case "Weight":
                            if(ligne[headerIndex.get(key)].equals(""))poisson.setWeight(null);
                            else poisson.setWeight(Double.parseDouble(ligne[headerIndex.get(key)]));
                            break;
                    
                        case "Size":
                            if(ligne[headerIndex.get(key)].equals(""))poisson.setSize(null);
                            else poisson.setSize(Double.parseDouble(ligne[headerIndex.get(key)]));
                            break;
                        
                        case "Total_parasites":
                            if(ligne[headerIndex.get(key)].equals(""))poisson.setParasites(null);
                            else poisson.setParasites((poisson.getParasites() == null )? (int )Double.parseDouble(ligne[headerIndex.get(key)]) : (int) Double.parseDouble(ligne[headerIndex.get(key)]) + poisson.getParasites());
                            break;
                    
                        case "InfestationRate":
                            if(ligne[headerIndex.get(key)].equals(""))poisson.setInfestationRate(null);
                            else poisson.setInfestationRate(Double.parseDouble(ligne[headerIndex.get(key)])/poucentage);
                            break;
                        
                    
                        case "Content":
                            String[] contents = ligne[headerIndex.get(key)].split(multipleSplit);
                            for(String content : contents){
                                poisson.addContent(content);
                            }
                            break;
                    
                        default:
                            throw new InvalidAttribute(key,fishAttributes);      

                            }//switch
                        }//if

                       
                        default:
                            throw new InvalidAttribute(key,fishAttributes);
                   
                    }//switch
                }//fish
                else{
                    switch (key) {
                        case "Species":
                        
                            population.setName(ligne[headerIndex.get(key)]) ;
                            break;

                        case "Parameter":
                            param = ligne[headerIndex.get(key)] ;
                            break ;
                        
                        case "Value":
                            if(params.containsKey(param)){
                            String v = ligne[headerIndex.get(key)];

                            v = v.split(" ")[0];
                            switch(params.get(param)){

                        case "MeanLength":
                            if(ligne[headerIndex.get(key)].equals(""))population.setMeanLength(null);
                            else population.setMeanLength(Double.parseDouble(v));
                            break;

                        case "MeanWeight":
                            if(ligne[headerIndex.get(key)].equals(""))population.setMeanWeight(null);
                            else population.setMeanWeight(Double.parseDouble(v));
                            break;
                    
                        case "MeanSize":
                            if(ligne[headerIndex.get(key)].equals(""))population.setMeanSize(null);
                            else population.setMeanSize(Double.parseDouble(v));
                            break;
                    
                        case "InfestationRate":
                            if(ligne[headerIndex.get(key)].equals(""))population.setInfestationRate(null);
                            else population.setInfestationRate(Double.parseDouble(v)/poucentage);
                            break;
                        
                        case "Intensity":
                            if(ligne[headerIndex.get(key)].equals(""))population.setIntensity(null);
                            else population.setIntensity(Double.parseDouble(v));
                            break;
                        
                        case "Total":
                            if(ligne[headerIndex.get(key)].equals(""))population.setTotal(null);
                            else population.setTotal(Integer.parseInt(v));
                            break;   
                        
                        case "Total_parasites":
                            if(ligne[headerIndex.get(key)].equals(""))population.setParasites(null);
                            else population.setParasites( (population.getParasites() == null)?(int ) Double.parseDouble( ligne[headerIndex.get(key)]) : Integer.parseInt(ligne[headerIndex.get(key)]) + poisson.getParasites());
                            break; 
                    
                        case "Content":
                            String[] contents = ligne[headerIndex.get(key)].split(multipleSplit);
                            for(String content : contents){
                                population.addContent(content);
                            }
                            break;
                    
                        default:
                            System.out.print("ici");
                            throw new InvalidAttribute(params.get(param),populationAttributes);
                        
                        }
                        break;
                    }
                   
                    }

                }
            
            }
            if(counter ==0)result.add((poisson==null)?type.cast(population) :type.cast(poisson));
            counter ++ ;

        }
            

        }catch(IOException e){
            System.out.print(e);
        }
    return result;
}




}