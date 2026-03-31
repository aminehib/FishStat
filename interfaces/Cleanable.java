package interfaces;
import java.util.ArrayList;


import model.DataFrame;
import model.Fish;
import exceptions.InvalidParametreLength;


public interface Cleanable {

    public void clean(DataFrame<Fish> fish, Double[] errors) throws InvalidParametreLength;
    public void complete(DataFrame<Fish> fish);
    
}
