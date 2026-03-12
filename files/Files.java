package files;

import java.util.ArrayList;

import java.io.BufferedReader ;
import java.io.FileReader;
import java.io.IOException;

public class Files {


    public static ArrayList<String> getFile(String name) throws IOException {
        FileReader fileReader = null  ;
        
        fileReader = new FileReader(name)  ;
        
        BufferedReader buffer  = new BufferedReader(fileReader) ;
        String line ;
        ArrayList<String> lines  = new ArrayList<>() ;
        
        while( (line = buffer.readLine() ) != null){
            lines.add(line);
        }
        fileReader.close();
        buffer.close();
        return lines ;
    }


    public static String getExtension(String name){
        if(name.lastIndexOf(".") == -1 || name.lastIndexOf(".") == name.length() - 1 )return "" ;
        String extension  =  name.substring(name.lastIndexOf(".") + 1);
        
        return extension ;
    }
    
}
