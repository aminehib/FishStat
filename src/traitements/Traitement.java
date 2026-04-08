package traitements;
import  interfaces.Cleanable;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import exceptions.InvalidParametreLength;
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
            if(poisson.getInfestationRate() == null )Unknown.add(poisson);
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
    public void clean(DataFrame<Fish> Fish , Double[] errors)throws InvalidParametreLength{
        if(errors.length != 4)throw new InvalidParametreLength("errors", 4);
        ArrayList<Fish> fish = new ArrayList<>(Fish.getData());
        LinkedHashMap<String , ArrayList<Fish> > sepecies = getSpecies(fish) ;
        for(ArrayList<Fish> poissons : sepecies.values()){
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
                if(rate != null && (rate < 0.0 || rate > 1.0) ){
                    poisson.setInfestationRate(null);
                    rate = null ;
                }
                Double weight = poisson.getWeight();
                if(weight != null && weight < 0){
                    poisson.setWeight(null);
                    weight = null ;
                }

                Double size = poisson.getSize();
                if(size != null && size < 0){
                    poisson.setSize(null);
                    size = null ;
                }
                Double length = poisson.getLength();
                if(length != null && length < 0){
                    poisson.setLength(null);
                    length = null;
                }

                if( (rate != null && boiteI.getMoustacheSup() != null) &&(rate > boiteI.getMoustacheSup() +Math.abs(errors[0]) || rate < boiteI.getMoustacheInf() - Math.abs(errors[0])))
                    poisson.setInfestationRate(null);

                if((weight != null && boiteW.getMoustacheSup() != null) &&(weight > boiteW.getMoustacheSup()+ Math.abs(errors[1])||  weight < boiteW.getMoustacheInf()- Math.abs(errors[1])))
                    poisson.setWeight(null);

                if((size != null && boiteS.getMoustacheSup() != null)&&(size > boiteS.getMoustacheSup() + Math.abs(errors[2])||  size < boiteS.getMoustacheInf()- Math.abs(errors[2])))
                    poisson.setSize(null);

                if((length != null && boiteL.getMoustacheSup() != null) &&( length  > boiteL.getMoustacheSup() + Math.abs(errors[3])||  length  < boiteL.getMoustacheInf()- Math.abs(errors[3])))
                    poisson.setLength(null);
           
            }

        }
    }

}
