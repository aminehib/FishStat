package tools;

import java.util.ArrayList;

public class RegressionPoly2 {

    private Double a  = null ;
    private Double b  = null ;
    private Double c  = null ;

    public Double[] getCoeffs(){
        Double[] coeffs  = {a,b};
        return coeffs;
    }

    public Double getIntercept(){
        return c ;
    }



    public  RegressionPoly2(ArrayList<Double> x , ArrayList<Double> y){

        if(x.size() != y.size())return ;

        int n = x.size();

        double Sx = 0, Sx2 = 0, Sx3 = 0, Sx4 = 0;
        double Sy = 0, Sxy = 0, Sx2y = 0;

        for (int i = 0; i < n; i++) {
            Double xi = x.get(i);
            Double yi = y.get(i);
            if(xi == null || yi == null) continue;

            double x2 = xi * xi;
            double x3 = x2 * xi;
            double x4 = x2 * x2;

            Sx += xi;
            Sx2 += x2;
            Sx3 += x3;
            Sx4 += x4;

            Sy += yi;
            Sxy += xi * yi;
            Sx2y += x2 * yi;
        }

        double[][] A = {
            {n,   Sx,  Sx2},
            {Sx,  Sx2, Sx3},
            {Sx2, Sx3, Sx4}
        };

        double[] B = {Sy, Sxy, Sx2y};

        double[] res = solve3x3(A, B);

        a = res[2] ;
        b = res[1] ;
        c = res[0] ;
    }

    // Gauss 3x3 simple
    static double[] solve3x3(double[][] A, double[] B) {
        int n = 3;

        for (int i = 0; i < n; i++) {
            double pivot = A[i][i];

            for (int j = 0; j < n; j++) {
                A[i][j] /= pivot;
            }
            B[i] /= pivot;

            for (int k = 0; k < n; k++) {
                if (k != i) {
                    double factor = A[k][i];
                    for (int j = 0; j < n; j++) {
                        A[k][j] -= factor * A[i][j];
                    }
                    B[k] -= factor * B[i];
                }
            }
        }
        return B;
    }

    public  Double predict(Double x){
        if(a == null || b == null || c == null )return null ;
        if(x != null)
            return a*x*x + b*x + c ;
        return null ;
    }

    public ArrayList<Double> predict(ArrayList<Double> x){
        if(a == null || b == null || c == null || x.size() == 0 )return null ;
        ArrayList<Double> res = new ArrayList<>() ;
        for(Double v : x){
            if(v != null)
                res.add(a*v*v + b*v + c ) ;
        }
        return res ;
    }
}
       

