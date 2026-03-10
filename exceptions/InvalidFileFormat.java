package exceptions;

public class InvalidFileFormat  extends Exception{

    public InvalidFileFormat(){
        super("Le fichier entré doit etre du format : file.csv") ;
    }
    
}
