package utils;

import struct.GridMatrix;
import struct.SetTS;

import java.util.ArrayList;

/**
 * Created by jun on 2019-01-04.
 */
public class Similarity {
    public static final String JACCARD_DISTANCE = "Jaccard";
    public static final String GMED = "GMED";
    public static final String GMDTW = "GMDTW";

    //matrix-based Euclidean distance
    public static double GMED(GridMatrix m1, GridMatrix m2)
    {
        double sum = 0;

        int[][] matrix1 = m1.matrix;
        int[][] matrix2 = m2.matrix;

        for(int i=0; i<matrix1.length; i++)
        {
            for(int j=0; j<matrix1[i].length; j++)
            {
                sum += Math.pow(matrix1[i][j]-matrix2[i][j], 2);
            }
        }

        return Math.sqrt(sum);
    }

    //matrix-based Dynamic Time Warping
    public static double GMDTW(GridMatrix m1, GridMatrix m2)
    {
        int[][] matrix1 = m1.matrix;
        int[][] matrix2 = m2.matrix;

        double[][] distanceMarix = new double[matrix1.length][matrix2.length];

        for(int i=0; i<distanceMarix.length; i++)
        {
            for (int j=0; j<distanceMarix[i].length; j++)
            {
                double d = ED(matrix1[i], matrix2[j]);
                if(i==0 && j==0)
                    distanceMarix[i][j] = d;
                else if(i==0)
                    distanceMarix[i][j] = d + distanceMarix[i][j-1];
                else if(j==0)
                    distanceMarix[i][j] = d + distanceMarix[i-1][j];
                else
                    distanceMarix[i][j] = d + Math.min(Math.min(distanceMarix[i-1][j-1], distanceMarix[i-1][j]), distanceMarix[i][j-1]);
            }
        }

        return distanceMarix[matrix1.length-1][matrix2.length-1];
    }

    //standard Euclidean Distance
    public static double ED(int[] a, int[] b)
    {
        int sum = 0;
        for(int i=0; i<a.length; i++)
        {
            sum += Math.pow(a[i]-b[i], 2);
        }

        return Math.sqrt(sum);
    }

    public static double JaccardDist(SetTS t1, SetTS t2)
    {
        double dist = 2;

        double intersection = LinearIntersection(t1, t2, dist);
        dist = 1 - intersection/(t1.size() + t2.size() - intersection);

        return dist;
    }

    public static int LinearIntersection(ArrayList<Integer> a, ArrayList<Integer> b,
                                  double minDist) {
        int p = 0, q = 0, ans = 0;
        int sizeA = a.size();
        int sizeB = b.size();
        double len = sizeA + sizeB;
        double thre = len * (1.0 - minDist) / (2.0 - minDist);
        while (p < a.size() && q < b.size()) {
            if (a.get(p) < b.get(q))
                p++;
            else if (a.get(p) > b.get(q))
                q++;
            else {
                // ans.add(a.get(p));//
                ans += 1;
                p++;
                q++;
                if ((ans + sizeA - p <= thre) || (ans + sizeB - q <= thre)) {
                    return 0;
                }
            }
        }
        return ans;
    }
}
