package utils;

import struct.Point;
import struct.PointTra;

import java.util.Arrays;

/**
 * Created by jun on 2019-01-10.
 */
public class Representation {
    /*
        Piecewise Aggregate Approximation
        Eamonn Keogh, et al., A simple dimensionality reduction technique for fast similarity search in large time series databases, 2000.
     */
    public static double[] PAA(PointTra ts, int n)
    {
        int length = ts.size();
        if(length <= n)
        {
            double[] paa = new double[length];
            for(int i=0; i<length; i++)
                paa[i] = ts.get(i).getX();
            return paa;
        }
        else
        {
            if(length % n == 0){
                double[] paa = new double[n];
                int seg = length/n;
                for(int i=0; i<length; i++){
                    int idx = i/seg;
                    paa[idx] += ts.get(i).getX();
                }
                for(int i=0; i<n; i++){
                    paa[i] = paa[i]/(double)(seg);
                }
                return paa;
            }
            else
            {
                double[] paa = new double[n];
                for (int i = 0; i < length * n; i++) {
                    int idx = i / length; // the spot
                    int pos = i / n; // the col spot
                    paa[idx] = paa[idx] + ts.get(pos).getX();
                }
                for (int i = 0; i < n; i++) {
                    paa[i] = paa[i] / (double) length;
                }
                return paa;
            }
        }
    }

    /*
        Piecewise linear approximation
        Eamonn Keogh, Fast Similarity Search in the Presence of Longitudinal Scaling in Time Series Databases, 1997
     */
    public static double[] PLA(PointTra ts)
    {
        int n = ts.size();


        return null;
    }

    public static double[] LinearRegression(double[] points)
    {
        double[] params = new double[2]; //a, b  a: gradient, b: bias
        double n = points.length;
        double x_sum = 0;
        double x_square_sum = 0;
        double xy_sum = 0;
        double y_sum = 0;

        for(int i=0; i<n; i++)
        {
            x_sum += i;
            x_square_sum += i*i;
            y_sum += points[i];
            xy_sum += i*points[i];
        }

        double det = (x_square_sum*n)-(x_sum*x_sum); //ad-bc
        params[0] = (n*xy_sum - x_sum*y_sum)/det;
        params[1] = (-x_sum*xy_sum + x_square_sum*y_sum)/det;

        return params;
    }

    /*
        Symbolic Aggregate approXimation
        Jessical Lin, Eamonn Keogh, et al., A symbolic representation of time series, with implications for streaming algorithms, 2003.
     */
    public static String[] SAX()
    {

        return null;
    }


    public static void DDR()
    {

    }


}
