package exceptions;

public class InvalidFileFormat  extends Exception{

    public InvalidFileFormat(){
        super("Le fichier entree doit etre du format : file.csv") ;
    }

    public InvalidFileFormat(String message){
        super(message);
    }
    
}
