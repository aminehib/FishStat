package traitements;
import model.DataFrame;
import model.Fish;
import tools.MeanValue;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import tools.MeanValue;


public class MeanValueCompletion extends Traitement {

    @Override
    public void complete(DataFrame<Fish> fish){
        ArrayList<Fish> poissons = new ArrayList<>(fish.getData());
        LinkedHashMap<String , ArrayList<Fish> > Species = getSpecies(poissons);
        for(String species : Species.keySet()){
            DataFrame Known = new DataFrame(getKnownValues(Species.get(species)) );
            LinkedHashMap<String, ArrayList<Double> > KnownValues = new LinkedHashMap<>();
            KnownValues.put("InfestationRates", Known.getInfestationRates());
            KnownValues.put("Weights", Known.getWeights());
            KnownValues.put("Sizes", Known.getSizes());
            KnownValues.put("Lengths", Known.getLengths());
            Double moyenne ;

            for( String type : KnownValues.keySet()){
                ArrayList<Double> values = KnownValues.get(type);
                if(values.isEmpty())continue ;
                moyenne = new MeanValue(values).getMean();
                if(moyenne == null)continue ;
                ArrayList<Fish> Unknown = getUnknownValues(Species.get(species));
                for(Fish unknown : Unknown)
                    switch (type) {
                        case "InfestationRates":
                            if(unknown.getInfestationRate() == null)
                                unknown.setInfestationRate(moyenne);
                            break;
                        case "Weights":
                            if(unknown.getWeight() == null)
                            unknown.setWeight(moyenne);
                            break;
                        case "Sizes":
                            if(unknown.getSize() == null)
                                unknown.setSize(moyenne);
                            break;
                        case "Lengths":
                            if(unknown.getLength() == null)
                            unknown.setLength(moyenne);
                            break;
                        default:
                            break;
                    }
            }
            
        }
    }

  
    
}
