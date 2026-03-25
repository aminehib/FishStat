package traitements;
import model.DataFrame;
import model.Fish;
import tools.MeanValue;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.HashMap;
import tools.MeanValue;
import exceptions.InvalidFileFormat;


public class MeanValueCompletion extends Traitement {

    @Override
    public void complete(DataFrame fish){
        ArrayList<Fish> poissons = new ArrayList<>(fish.getData());
        LinkedHashMap<String , ArrayList<Fish> > Species = getSpecies(poissons);
        for(String species : Species.keySet()){
            DataFrame Known = new DataFrame(getKnownValues(Species.get(species)) );
            ArrayList<Double> KnownValues = Known.getInfestationRates() ;
            Double moyenne ;
            moyenne = new MeanValue(KnownValues).getMean();
            if(moyenne == null) continue;
            ArrayList<Fish> Unknown = getUnknownValues(Species.get(species));
            for(Fish unknown : Unknown)
                unknown.setInfestationRate(moyenne);
        }
    }

    // Complétion par moyenne en utilisant un dataset de référence (par espèce).
    public void completeWithReference(DataFrame fish, String referenceCsv) throws InvalidFileFormat {
        DataFrame reference = new DataFrame();
        reference.readProcessedAnisakis(referenceCsv);

        // moyenne par espèce depuis le dataset de référence
        HashMap<String, Double> meanBySpecies = new HashMap<>();
        LinkedHashMap<String , ArrayList<Fish> > refSpecies = getSpecies(reference.getData());
        for(String species : refSpecies.keySet()){
            DataFrame known = new DataFrame(getKnownValues(refSpecies.get(species)));
            Double mean = new MeanValue(known.getInfestationRates()).getMean();
            if(mean != null){
                meanBySpecies.put(species, mean);
            }
        }

        // applique uniquement si la même espèce existe dans la référence
        LinkedHashMap<String , ArrayList<Fish> > targetSpecies = getSpecies(fish.getData());
        for(String species : targetSpecies.keySet()){
            Double mean = meanBySpecies.get(species);
            if(mean == null) continue;
            ArrayList<Fish> unknown = getUnknownValues(targetSpecies.get(species));
            for(Fish f : unknown){
                f.setInfestationRate(mean);
            }
        }
    }

  
    
}
