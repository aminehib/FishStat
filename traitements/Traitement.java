package traitements;
import  interfaces.Cleanable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import model.DataFrame;
import model.Fish;
import tools.BoiteAMoustaches;

public abstract class Traitement implements Cleanable {

    protected ArrayList<Fish> getKnownValues(ArrayList<Fish> poissons){
        ArrayList<Fish> Known  = new ArrayList<>();
        for(Fish poisson : poissons){
            if(poisson.getInfestationRate() != null)Known.add(poisson);
        }
        return Known ;
    }

    protected ArrayList<Fish> getUnknownValues(ArrayList<Fish> poissons){
        ArrayList<Fish> Unknown  = new ArrayList<>();
        for(Fish poisson : poissons){
            if(poisson.getInfestationRate() == null)Unknown.add(poisson);
        }
        return Unknown;
    }

    protected LinkedHashMap<String ,ArrayList<Fish>> getSpecies(ArrayList<Fish> poissons){
        LinkedHashMap<String ,ArrayList<Fish>> Species = new LinkedHashMap<>() ;
        for(Fish poisson : poissons){
            if(!Species.containsKey(poisson.getSpecies()) )Species.put(poisson.getSpecies(), new ArrayList<>());
            Species.get(poisson.getSpecies()).add(poisson);
        }
        return Species ;
    }

    @Override
    public void clean(DataFrame Fish ){
        ArrayList<Fish> poissons = new ArrayList<>(Fish.getData());
        LinkedHashMap<String,ArrayList<Double> > colonnes = new LinkedHashMap<>() ;
        colonnes.put("Length", new ArrayList<>());
        colonnes.put("Size", new ArrayList<>());
        colonnes.put("Weight", new ArrayList<>());
        colonnes.put("Infestation", new ArrayList<>());
        for(Fish poisson : poissons){
            colonnes.get("Length").add(poisson.getLength());
            colonnes.get("Size").add(poisson.getSize());
            colonnes.get("Weight").add(poisson.getWeight());
            colonnes.get("Infestation").add(poisson.getInfestationRate());
        }
        BoiteAMoustaches boiteL = new BoiteAMoustaches(colonnes.get("Length"));
        BoiteAMoustaches boiteS = new BoiteAMoustaches(colonnes.get("Size"));
        BoiteAMoustaches boiteW = new BoiteAMoustaches(colonnes.get("Weight"));
        BoiteAMoustaches boiteI = new BoiteAMoustaches(colonnes.get("Infestation"));
        
        for(Fish poisson : poissons){

            Double rate = poisson.getInfestationRate();
            Double weight = poisson.getWeight();
            Double size = poisson.getSize();
            Double length = poisson.getLength();

            if( (rate != null && boiteI.getMoustacheSup() != null) &&(rate > boiteI.getMoustacheSup()|| rate > 1.0 || rate <0.0 || rate < boiteI.getMoustacheInf()))
                poisson.setInfestationRate(null);

            if((rate != null && boiteW.getMoustacheSup() != null) &&(weight > boiteW.getMoustacheSup()||  weight < boiteW.getMoustacheInf()))
                poisson.setWeight(null);

            if((rate != null && boiteS.getMoustacheSup() != null)&&(size > boiteS.getMoustacheSup()||  size < boiteS.getMoustacheInf()))
                poisson.setSize(null);

            if((rate != null && boiteL.getMoustacheSup() != null) &&( length  > boiteL.getMoustacheSup()||  length  < boiteL.getMoustacheInf()))
                poisson.setLength(null);
           
        }

    }

}
