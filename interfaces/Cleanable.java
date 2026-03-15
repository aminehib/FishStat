package interfaces;
import java.util.ArrayList;


import model.DataFrame;
import model.Fish;


public interface Cleanable {

    public void clean(DataFrame fish);
    public void complete(DataFrame fish);
    
}
