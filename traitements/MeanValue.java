package traitements;
import model.Fish;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MeanValue extends Traitement {

    @Override
    public void complete(ArrayList<Fish> poissons){
        LinkedHashMap<String , ArrayList<Fish> > Species = getSpecies(poissons);
        for(String species : Species.keySet()){
            ArrayList<Fish> Known = getKnownValues(Species.get(species)) ;
            double moyenne ;
            if(Known.size() != 0){
                moyenne = meanValue(Known);
                ArrayList<Fish> Unknown = getUnknownValues(Species.get(species));
                for(Fish unknown : Unknown)
                    unknown.setInfestationRate(moyenne);
             }
        }
    }

    private double meanValue(ArrayList<Fish> poissons){
        double sum = 0 ;
        for(Fish poisson : poissons){
            sum += poisson.getInfestationRate();
        }
        return sum / poissons.size() ;
    }
    
}
