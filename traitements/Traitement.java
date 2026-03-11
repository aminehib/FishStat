package traitements;
import  interfaces.Cleanable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

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
    public void clean(ArrayList<Fish> poissons){
        LinkedHashMap<String , ArrayList<Fish>> species = getSpecies(poissons) ;
        for( String espece : species ){
        BoiteAMoustaches boite = new BoiteAMoustaches(species.get(espece));
            for(Fish poisson : poissons){
                if(poisson.getInfestationRate() != null &&( poisson.getInfestationRate() > boite.getMoustacheSup() || poisson.getInfestationRate() < boite.getMoustacheInf()) )
                    poisson.setInfestationRate(null);
            }
        }
    }


    
    
}
