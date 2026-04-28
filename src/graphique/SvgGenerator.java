package graphique;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import model.DataFrame;
import model.Fish;
import tools.LinearRegression;

import java.nio.file.Path;

public class SvgGenerator {

    public static void GenerateSVG(DataFrame fish,String X ,String Y , int height , int width , String title ,int MARGE , int nbl , int nbc , int echelle ){


        double CELL_SIZE_X =  (width-2*MARGE) / nbc ;
        double CELL_SIZE_Y = (height-2*MARGE) / nbl ;
        
        ArrayList<Double> x0 = fish.getColumn(X) ;
        ArrayList<Double> y0 = fish.getColumn(Y) ;

        for(int i = x0.size() - 1; i >= 0; i--){
             if(x0.get(i) == null || y0.get(i) == null){
                x0.remove(i);
                y0.remove(i);
            }
        }

        if(x0.size() == 0){
            System.out.println("Données manquantes");
            return ;
        }

        x0.sort( (f1,f2) ->{
            // tri croissant
            return f1.compareTo(f2);
        });

       


        OutputStream out  = null ;


        Path p = Paths.get("graphique.svg").toAbsolutePath();
        try{
        out = Files.newOutputStream(p, StandardOpenOption.CREATE , StandardOpenOption.TRUNCATE_EXISTING );
        String SVG  = initialiserSVG(width,height ) ;
        
        String points = "<polyline points = '" ;
        double first_X = x0.get(0) ;
        double last_X = x0.get(x0.size()-1) ;
    
           
        LinearRegression model = new LinearRegression(fish.getColumn(X), fish.getColumn(Y));
       

           
        SVG += dessinerGrilleEtMarge(nbc, nbl, CELL_SIZE_X, CELL_SIZE_Y, MARGE ,first_X ,  last_X -first_X ,0 , echelle );
        
        for(int i = 0 ; i < x0.size() ; i++){
            double x = MARGE + ( ( x0.get(i) - first_X) / (last_X - first_X) ) *nbc*CELL_SIZE_X ;
            double y = MARGE + (1 - ( ( model.predict(x0.get(i) )  / echelle  ) ))  *nbl*CELL_SIZE_Y;
            points  += String.format("%f,%f ",x,y);
            SVG += String.format("<circle cx ='%f' cy ='%f' r='4' fill ='blue' />\n",x , y) ;
        }
        
        points += "' stroke='red' stroke-width='2' fill='none'/>\n";
        SVG += text( (width-2*MARGE)/2 , MARGE /2  , MARGE , title, "green",0);
        SVG += text( (width-2*MARGE)/2 , height -  MARGE / 3  , MARGE/3 , String.format("x( %s )",X ), "blue",0);
        SVG += text(width - MARGE /3  , (height-2*MARGE)/2 , MARGE / 3 , String.format("y( %s )",Y ), "blue",90);
        
        SVG += points + "</svg>" ;
        
        out.write(SVG.getBytes());
        out.close();
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        
    }

    private static String initialiserSVG(int largeur, int hauteur){
        String SVG ="" ;
        SVG += "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" ;
        SVG += "<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" ";
        SVG +="width=\"" + largeur + "\" height=\"" + hauteur + "\">\n" ;
        return SVG ;
    }




    private static String dessinerGrilleEtMarge(int nbc, int nbl ,double SVG_CELLSIZE_X , double SVG_CELLSIZE_Y ,  int SVG_MARGE ,double first_X , double ecart_X , double first_Y , double ecart_Y){
        String SVG ="" ;
        for(int i = 0 ; i <= nbl ; i++){
            SVG += ligne(SVG_MARGE , SVG_MARGE + i * SVG_CELLSIZE_Y, SVG_MARGE + nbc * SVG_CELLSIZE_X, SVG_MARGE + i * SVG_CELLSIZE_Y ,"black",1);
            SVG += text(SVG_MARGE / 2.0 , SVG_MARGE + i * SVG_CELLSIZE_Y , SVG_MARGE / 5.0  ,String.format("%.2f",first_Y +ecart_Y *(nbl - i) / nbl ) , "black" , -45 );
        }

    
        for(int i = 0 ; i <= nbc ; i++){
            SVG += ligne(SVG_MARGE + i * SVG_CELLSIZE_X ,  SVG_MARGE , SVG_MARGE + i * SVG_CELLSIZE_X , SVG_MARGE + nbl * SVG_CELLSIZE_Y , "black", 1);
            SVG += text(SVG_MARGE + i * SVG_CELLSIZE_X  , SVG_MARGE + nbl * SVG_CELLSIZE_Y  +SVG_MARGE /3.0 , SVG_MARGE / 5.0 ,String.format("%.2f",first_X +  ecart_X * i /nbc) , "black" , -60);
        }

        return SVG ;

    }


    private static String ligne(double x1, double y1, double x2, double y2, String color, int width){
        String SVG = "" ;
        SVG +=  "<line x1=\"" + x1+ "\" y1=\"" + y1 + "\"";
        SVG += " x2=\"" + x2+ "\" y2=\"" + y2;
        SVG += "\" stroke=\"" + color + "\"";
        SVG += " stroke-width=\"" + width + "\"" + " />\n" ;
        return SVG ;
    }

    

    private static String text(double x, double y, double size, String txt, String color , double rotation){
        String SVG = "" ;
        SVG += "<text x=\"" + x + "\" y=\"" + y + "\"" ;
        SVG += " font-family=\"Verdana\" font-size=\"" + size + "\"";
        SVG += " text-anchor=\"middle\" " ;
        SVG += " dominant-baseline=\"middle\" ";
        SVG += String.format("transform=\"rotate(%.1f %.1f %.1f)\" ", rotation , x ,y);
        SVG += "fill=\"" + color + "\" >\n" ;
        SVG += txt + "\n"; 
        SVG += "</text>\n" ;
        return SVG ;
}



    
}