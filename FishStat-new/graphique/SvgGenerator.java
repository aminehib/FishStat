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

    public static void GenerateSVG(DataFrame fish){
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
        OutputStream out  = null ;
        Path p = Paths.get("graphique.svg").toAbsolutePath();
        try{
        out = Files.newOutputStream(p, StandardOpenOption.CREATE , StandardOpenOption.TRUNCATE_EXISTING );
        String SVG  = "<svg height='1100' width='1800' xmlns='http://www.w3.org/2000/svg'>\n";
        String points = "<polyline points = '" ;
        double start = poissons.get(0).getSize() ;
        double rate = 1700 / (poissons.get(poissons.size()-1).getSize() -start) ;
        LinearRegression model = new LinearRegression(fish.getSizes(), fish.getInfestationRates());
        for(Fish poisson : poissons){
            double x = (poisson.getSize()-start) *rate ;
            double y = 1000 - model.predict(poisson.getSize()) *1000;
            points  += String.format("%f,%f ",x,y);
            SVG += String.format("<circle cx ='%f' cy ='%f' r='4' fill ='blue' />\n",x , y);
        }
        points += "' stroke='red' stroke-width='2' fill='none'/>\n";
        SVG += grille(1000 ,1700,start , poissons.get(poissons.size()-1).getSize());
        SVG += "<text x='850' y='1050' color='white' font-size='20'>Taille du poisson</text>\n" ;
        SVG += "<text x='1760' y='450' color='white' font-size='20' transform='rotate(90 1800 400)'>Taux d'infestation</text>\n" ;
        SVG += "<text x='600' y='1080' color='white' font-size='30' font-weight='700'>Droite de regression</text>\n" ;
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
        for(int i = 0 ; i <= width/100 ; i++){
            svg += String.format("<line x1='%d' y1='%d' x2='%d' y2='%d' stroke='black' stroke-width='1' />", i*100 , 0 , i*100, length);
            svg += String.format("<text x='%d' y='%d'>%.1f </text>" , i*100 , length +20 , start +i* ( (end -start)/ ((width/100))) );
        }
        return svg ;
    }
    
}
