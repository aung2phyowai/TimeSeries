import struct.GridMatrix;
import struct.Point;
import struct.PointTra;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jun on 2019-01-07.
 */
public class Toy_Sample {
    public static void run() throws IOException
    {
        ArrayList<PointTra> SampleDataset = new ArrayList<PointTra>();
        double[] x = {2, 3, 5, 6, 8, 2, 3, 1, 5, 7, 3, 1, 10, 5};
        double[] y = {3, 5, 6, 7, 2, 1, 5, 6, 8, 6, 4, 2, 1};

        PointTra X = new PointTra();
        PointTra Y = new PointTra();

        for(int i=0; i<x.length; i++)
            X.add(new Point(i, x[i]));
        for(int i=0; i<y.length; i++)
            Y.add(new Point(i, y[i]));

        SampleDataset.add(X);
        SampleDataset.add(Y);

        SampleDataset = Tool.minmaxNormalize(SampleDataset); //conduct min_max normalization

        System.out.println("normalized X");
        for(int i=0; i<X.size(); i++)
            System.out.print(X.get(i).getX() + " ");
        System.out.println();

        System.out.println("normalized Y");
        for(int i=0; i<Y.size(); i++)
            System.out.print(Y.get(i).getX() + " ");
        System.out.println();

        Grid gm = new Grid(5, 7);

        System.out.println("m : " + gm.m + " n : " + gm.n);
        GridMatrix[] trainMatrices = gm.dataset2Matrices(SampleDataset);
        System.out.println("X Matrix :");
        trainMatrices[0].print();
        System.out.println("Y Matrix :");
        trainMatrices[1].print();

        System.out.println("GMED : "+ Similarity.GMED(trainMatrices[0], trainMatrices[1]));
        System.out.println("GMDTW : "+ Similarity.GMDTW(trainMatrices[0], trainMatrices[1]));

    }

}
