import com.sun.xml.internal.ws.developer.MemberSubmissionAddressing;
import struct.GridMatrix;
import struct.PointTra;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jun on 2019-01-04.
 */
public class Grid {
    public int m; //number of rows
    public int n; //number of columns
    private GridMatrix[] trainMatrices;

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

    public void train(ArrayList<PointTra> trainOrg)
    {
        int[] size = step1(trainOrg);
        size = step2(trainOrg, size[0], size[1]);
        this.m = size[0];
        this.n = size[1];
    }

    private int[] step1(ArrayList<PointTra> trainOrg)
    {
        int[] size = new int[2]; //{m, n}
        int stepSize = 5;

        double minErrorRate = Double.MAX_VALUE;
        for(int m=5; m<=35; m+=stepSize)
        {
            for(int n=5; n<=30; n+=stepSize)
            {
                this.m=m;
                this.n=n;
                GridMatrix[] trainMatrices = this.dataset2Matrices(trainOrg);
                double errorRate = Validation.LOOCV(trainMatrices);
                //System.out.println("(m,n,errorRate)" + "(" + this.m + "," + this.n + "," + errorRate +")");
                if(errorRate < minErrorRate)
                {
                    minErrorRate = errorRate;
                    size[0] = m;
                    size[1] = n;
                }
            }
        }

        return size;
    }

    private int[] step2(ArrayList<PointTra> trainOrg, int m, int n)
    {
        int[] size = new int[2];
        double minErrorRate = Double.MAX_VALUE;

        int mRange = m+4;
        int nRange = n+4;

        for(m=m-4; m<=mRange; m++)
        {
            for(n=n-4; n<=nRange; n++)
            {
                this.m=m;
                this.n=n;
                GridMatrix[] trainMatrices = this.dataset2Matrices(trainOrg);
                double errorRate = Validation.LOOCV(trainMatrices);
                //System.out.println("(m,n,errorRate)" + "(" + this.m + "," + this.n + "," + errorRate +")");
                if(errorRate < minErrorRate)
                {
                    minErrorRate = errorRate;
                    size[0] = m;
                    size[1] = n;
                }
            }
        }
        return size;
    }

    public void setTrainMatrices(GridMatrix[] trainMatrices)
    {
        this.trainMatrices = trainMatrices;
    }

    public GridMatrix[] getTrainMatrices()
    {
        return trainMatrices;
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
