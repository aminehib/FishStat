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

    public static void GenerateSVG(DataFrame fish, int height , int width , int MARGE , int nbl , int nbc , int echelle){


        double CELL_SIZE_X =  (width-2*MARGE) / nbc ;
        double CELL_SIZE_Y = (height-2*MARGE) / nbl ;
        ArrayList<Fish> poissons = new ArrayList<>(fish.getData());
        poissons.removeIf(arg->{
            return arg.getSize() == null || arg.getInfestationRate() == null;
        });
        poissons.sort( (f1,f2) ->{
            Double t1 = f1.getSize() ;
            Double t2 = f2.getSize() ;
            // tri croissant
            return t1.compareTo(t2);
        });

        if(poissons.size() == 0){
            System.out.println("Données manquantes");
            return ;
        }


        OutputStream out  = null ;


        Path p = Paths.get("graphique.svg").toAbsolutePath();
        try{
        out = Files.newOutputStream(p, StandardOpenOption.CREATE , StandardOpenOption.TRUNCATE_EXISTING );
        String SVG  = initialiserSVG(width,height ) ;
        
        String points = "<polyline points = '" ;
        double first_X = poissons.get(0).getSize() ;
        double last_X = poissons.get(poissons.size()-1).getSize() ;
        LinearRegression model = new LinearRegression(fish.getSizes(), fish.getInfestationRates());
       

           
        SVG += dessinerGrilleEtMarge(nbc, nbl, CELL_SIZE_X, CELL_SIZE_Y, MARGE ,first_X ,  last_X -first_X ,0 , echelle );

        for(Fish poisson : poissons){
            double x = MARGE + ( (poisson.getSize() - first_X) / (last_X - first_X) ) *nbc*CELL_SIZE_X ;
            double y = MARGE + (1 - ( (model.predict(poisson.getSize()) ) / (echelle ) ) )*nbl*CELL_SIZE_Y;
            points  += String.format("%f,%f ",x,y);
            SVG += String.format("<circle cx ='%f' cy ='%f' r='4' fill ='blue' />\n",x , y) ;
        }
        
        points += "' stroke='red' stroke-width='2' fill='none'/>\n";
        SVG += text( (width-2*MARGE)/2 , MARGE /2  , MARGE , "Graphe de la régression", "green");
        SVG += text( (width-2*MARGE)/2 , width -  MARGE / 3  , MARGE/3 , "x(....)", "blue");
        SVG += text(MARGE /3  , (height-2*MARGE)/2 , MARGE / 3 , "y(.....)", "blue");
        
        SVG += points + "</svg>" ;
        
        out.write(SVG.getBytes());
        out.close();
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        
    }

    private static String grille(int length , int width ,double start, double end){
        String svg = "" ;
        for(int i = 0 ; i <= 10 ; i++){
            svg += String.format("<line x1='%d' y1='%d' x2='%d' y2='%d' stroke='black' stroke-width='1' />", 0 , i*100 , width , i*100);
            svg += String.format("<text x='%d' y='%d' >%d %%</text>" , width , i*100 , 100-i*10) ;
        }
        for(int i = 0 ; i <= 10 ; i++){
            svg += String.format("<line x1='%d' y1='%d' x2='%d' y2='%d' stroke='black' stroke-width='1' />", i*(width/10) , 0 , i*(width/10), length);
            svg += String.format("<text x='%d' y='%d'>%.1f </text>" , i*(width/10) , length +20 , start +i* ( (end -start)/ ((width/100))) );
        }
        return svg ;
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
            SVG += ligne(SVG_MARGE , SVG_MARGE + i * SVG_CELLSIZE_Y, SVG_MARGE + nbc * SVG_CELLSIZE_Y, SVG_MARGE + i * SVG_CELLSIZE_Y ,"black",1);
            SVG += text(SVG_MARGE / 2.0 , SVG_MARGE + i * SVG_CELLSIZE_Y , SVG_CELLSIZE_Y / 10 ,String.format("%.2f",first_Y +ecart_Y *(nbl - i) / nbl ) , "black" );
        }

    
        for(int i = 0 ; i <= nbc ; i++){
            SVG += ligne(SVG_MARGE + i * SVG_CELLSIZE_X ,  SVG_MARGE , SVG_MARGE + i * SVG_CELLSIZE_X , SVG_MARGE + nbl * SVG_CELLSIZE_X , "black", 1);
            SVG += text(SVG_MARGE + i * SVG_CELLSIZE_X  , SVG_MARGE + nbl * SVG_CELLSIZE_X  +SVG_MARGE /3.0 , SVG_CELLSIZE_X / 10 ,String.format("%.2f",first_X +  ecart_X * i /nbc) , "black");
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

    

    private static String text(double x, double y, double size, String txt, String color){
        String SVG = "" ;
        SVG += "<text x=\"" + x + "\" y=\"" + y + "\"" ;
        SVG += " font-family=\"Verdana\" font-size=\"" + size + "\"";
        SVG += " text-anchor=\"middle\" " ;
        SVG += " dominant-baseline=\"middle\" ";
        SVG += "fill=\"" + color + "\" >\n" ;
        SVG += txt + "\n"; 
        SVG += "</text>\n" ;
        return SVG ;
}



    
}