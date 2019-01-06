package struct;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jun on 2019-01-04.
 */
public class Grid {
    public int m; //number of rows
    public int n; //number of columns

    public Grid(int m, int n)
    {
        this.m = m;
        this.n = n;
    }

    public Grid()
    {
        Random random = new Random();
        this.m = 5+random.nextInt(35);
        this.n = 5+random.nextInt(30);
    }

    public GridMatrix[] dataset2Matrices(ArrayList<PointTra> trainOrg)
    {
        GridMatrix[] matrixDatasets= new GridMatrix[trainOrg.size()];
        for(int i=0; i<trainOrg.size(); i++)
            matrixDatasets[i] = ts2Matrix(trainOrg.get(i));

        return matrixDatasets;
    }

    public GridMatrix ts2Matrix(PointTra ts)
    {
        GridMatrix gm = new GridMatrix(m, n, ts.getCla());
        gm.setTimeSeries(ts);
        return gm;
    }
}
