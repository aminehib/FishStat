package files;

import java.util.ArrayList;

import java.io.BufferedReader ;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

import exceptions.InvalidFileFormat;

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

    public static String[] splitLine(String line, String delimiter){
        return line.split(Pattern.quote(delimiter), -1);
    }

    public static int indexOfColumn(String headerLine, String delimiter, String columnName) throws InvalidFileFormat{
        String[] headers = splitLine(headerLine, delimiter);
        for(int i = 0; i < headers.length; i++){
            String h = headers[i].trim();
            if(i == 0){
                h = h.replace("\uFEFF", "");
            }
            if(h.equals(columnName)){
                return i;
            }
        }
        throw new InvalidFileFormat("Colonne manquante: " + columnName);
    }

    public static String getExtension(String name){
        if(name.lastIndexOf(".") == -1 || name.lastIndexOf(".") == name.length() - 1 )return "" ;
        String extension  =  name.substring(name.lastIndexOf(".") + 1);
        
        return extension ;
    }
    
}
