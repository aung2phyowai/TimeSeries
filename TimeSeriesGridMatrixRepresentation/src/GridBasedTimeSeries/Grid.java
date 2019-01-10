package GridBasedTimeSeries;

import struct.GridMatrix;
import struct.PointTra;
import struct.SetTS;
import utils.Similarity;
import utils.Validation;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jun on 2019-01-04.
 */
public class Grid {
    public int m; //number of rows
    public int n; //number of columns
    private GridMatrix[] trainMatrices;
    private SetTS[] trainSets;

    public Grid(int m, int n)
    {
        this.m = m;
        this.n = n;
    }

    public Grid()
    {
        Random random = new Random();
        this.m = 1+random.nextInt(39);
        this.n = 1+random.nextInt(34);

        //this.m = 1+random.nextInt(79);
        //this.n = 1+random.nextInt(69);
    }

    public void trainForSet(ArrayList<PointTra> trainOrg) //find the best parameters(m,n) for 'set' representation using train datasets
    {
        int[] size = step1(trainOrg, Similarity.JACCARD_DISTANCE);
        size = step2(trainOrg, size[0], size[1], Similarity.JACCARD_DISTANCE);
        this.m = size[0];
        this.n = size[1];
    }

    public void trainForMatrix(ArrayList<PointTra> trainOrg) //find the best parameters(m,n) for 'matrix' representation using train datasets
    {
        int[] size = step1(trainOrg, Similarity.GMED);
        size = step2(trainOrg, size[0], size[1], Similarity.GMED);
        this.m = size[0];
        this.n = size[1];
    }

    private int[] step1(ArrayList<PointTra> trainOrg, String distMeasure)
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
                double errorRate = 1;
                if(distMeasure.equals(Similarity.GMED))
                {
                    GridMatrix[] trainMatrices = this.dataset2Matrices(trainOrg);
                    errorRate = Validation.LOOCV_Matrix(trainMatrices);
                }
                else if(distMeasure.equals(Similarity.JACCARD_DISTANCE))
                {
                    SetTS[] trainSets = this.dataset2Set(trainOrg);
                    errorRate = Validation.LOOCV_Set(trainSets);
                }

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

    private int[] step2(ArrayList<PointTra> trainOrg, int m, int n, String distMeasure)
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
                double errorRate = 1;
                if(distMeasure.equals(Similarity.GMED))
                {
                    GridMatrix[] trainMatrices = this.dataset2Matrices(trainOrg);
                    errorRate = Validation.LOOCV_Matrix(trainMatrices);
                }
                else if(distMeasure.equals(Similarity.JACCARD_DISTANCE))
                {
                    SetTS[] trainSets = this.dataset2Set(trainOrg);
                    errorRate = Validation.LOOCV_Set(trainSets);
                }

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

    public void setTrainSets(SetTS[] trainSets){
        this.trainSets = trainSets;
    }

    public SetTS[] getTrainSets(){
        return this.trainSets;
    }

    public GridMatrix[] dataset2Matrices(ArrayList<PointTra> datasets)
    {
        GridMatrix[] matrixDatasets= new GridMatrix[datasets.size()];
        for(int i=0; i<datasets.size(); i++)
            matrixDatasets[i] = ts2Matrix(datasets.get(i));

        return matrixDatasets;
    }

    public GridMatrix ts2Matrix(PointTra ts)
    {
        GridMatrix gm = new GridMatrix(this.m, this.n, ts.getCla());
        gm.setTimeSeries(ts);
        return gm;
    }

    public SetTS[] dataset2Set(ArrayList<PointTra> datasets)
    {
        SetTS[] setDatasets = new SetTS[datasets.size()];
        for(int i=0; i<datasets.size(); i++)
            setDatasets[i] = ts2Set(datasets.get(i));

        return setDatasets;
    }

    public SetTS ts2Set(PointTra ts)
    {
        SetTS set = new SetTS(this.m, this.n, ts.getCla());
        set.setTimeSeries(ts);
        return set;
    }
}
