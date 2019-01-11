package struct;

import utils.Representation;
import utils.Similarity;
import utils.Validation;

import java.util.ArrayList;

/**
 * Created by jun on 2019-01-10.
 */
public class PAA{
    int label; // label of original time series
    int m; //original time series length
    int n; //number of segment
    double[] paa;

    public PAA(int n, int label)
    {
        this.n = n;
        this.label = label;
    }

    public void setTimeSeries(PointTra ts)
    {
        this.m = ts.size();
        this.paa = Representation.PAA(ts, n);
    }

    public int getLabel(){
        return this.label;
    }

    //static functions
    public static int train(ArrayList<PointTra> trainOrg) //find the best parameters(m,n) for 'matrix' representation using train datasets
    {
        int n = step1(trainOrg);
        n = step2(trainOrg, n);
        return n;
    }

    private static int step1(ArrayList<PointTra> trainOrg)
    {
        int result_n = 5;
        int stepSize = 5;

        double minErrorRate = Double.MAX_VALUE;
        for(int n=5; n<=30; n+=stepSize)
        {
            PAA[] trainPAAs = dataset2PAAs(trainOrg, n);
            double errorRate = LOOCV(trainPAAs);

            if(errorRate < minErrorRate)
            {
                minErrorRate = errorRate;
                result_n = n;
            }
        }

        return result_n;
    }

    private static int step2(ArrayList<PointTra> trainOrg, int n)
    {
        int result_n = n;
        double minErrorRate = Double.MAX_VALUE;

        int nRange = n+4;

        for(n=n-4; n<=nRange; n++)
        {
            PAA[] trainPAAs = dataset2PAAs(trainOrg, n);
            double errorRate = LOOCV(trainPAAs);

            if(errorRate < minErrorRate)
            {
                minErrorRate = errorRate;
                result_n = n;
            }
        }

        return result_n;
    }

    public static PAA[] dataset2PAAs(ArrayList<PointTra> trainOrg, int n)
    {
        PAA[] trainPAAs = new PAA[trainOrg.size()];
        for(int i=0; i<trainOrg.size(); i++)
        {
            PAA paa = new PAA(n, trainOrg.get(i).getCla());
            paa.setTimeSeries(trainOrg.get(i));
            trainPAAs[i] = paa;
        }
        return trainPAAs;
    }

    public static double oneNNClassificationErrorRate(PAA[] trainPAAs, PAA[] testPAAs)
    {
        double queryCnt = 0;
        double errorCnt = 0;

        for(int qIdx=0; qIdx<testPAAs.length; qIdx++)
        {
            queryCnt++;
            PAA query = testPAAs[qIdx];
            double minDist = Double.MAX_VALUE;

            int predictLabel = -1;
            for(int idx=0; idx<trainPAAs.length; idx++)
            {
                PAA base = trainPAAs[idx];

                double sim = Similarity.WeightedED(base.paa, query.paa, query.m, query.n);
                if(sim < minDist)
                {
                    minDist = sim;
                    predictLabel = base.getLabel();
                }
            }

            if(predictLabel != query.getLabel())
                errorCnt++;
        }

        return errorCnt/queryCnt;
    }

    public static double LOOCV(PAA[] trainPAAs)
    {
        double queryCnt = 0;
        double errorCnt = 0;

        for(int qIdx=0; qIdx<trainPAAs.length; qIdx++)
        {
            PAA query = trainPAAs[qIdx];
            int predictLabel = -1;
            double minDist = Double.MAX_VALUE;

            for(int idx=0; idx<trainPAAs.length; idx++)
            {
                if(qIdx == idx) //for leave-one-out cross validation
                    continue;

                PAA base = trainPAAs[idx];

                double sim = Similarity.WeightedED(base.paa, query.paa, query.m, query.n);
                if(sim < minDist)
                {
                    minDist = sim;
                    predictLabel = base.getLabel();
                }
            }

            queryCnt++;
            if(predictLabel != query.getLabel())
                errorCnt++;
        }

        return errorCnt/queryCnt;
    }
}
