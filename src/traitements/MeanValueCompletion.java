package traitements;
import model.DataFrame;
import model.Fish;
import tools.MeanValue;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import tools.MeanValue;


public class MeanValueCompletion extends Traitement {

    @Override
    public void complete(DataFrame<Fish> fish){
        ArrayList<Fish> poissons = new ArrayList<>(fish.getData());
        LinkedHashMap<String , ArrayList<Fish> > Species = getSpecies(poissons);
        for(String species : Species.keySet()){
            DataFrame Known = new DataFrame(getKnownValues(Species.get(species)) );
            ArrayList<Double> KnownValues = Known.getInfestationRates() ;
            Double moyenne ;
            if(Known.getData().size() != 0){
                moyenne = new MeanValue(KnownValues).getMean();
                if(moyenne == null)continue ;
                ArrayList<Fish> Unknown = getUnknownValues(Species.get(species));
                for(Fish unknown : Unknown)
                    unknown.setInfestationRate(moyenne);
             }
        }
    }

  
    
}
