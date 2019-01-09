import struct.GridMatrix;

import java.util.ArrayList;

/**
 * Created by jun on 2019-01-06.
 */
public class Validation {

    public static double oneNNClassificationErrorRate(GridMatrix[] trainMatrices, GridMatrix[] testMatrices, String distanceMeasure)
    {
        double queryCnt = 0;
        double errorCnt = 0;

        for(int qIdx=0; qIdx<testMatrices.length; qIdx++)
        {
            queryCnt++;
            GridMatrix query = testMatrices[qIdx];
            int predictLabel = -1;
            if (distanceMeasure.equals("GMED"))
                predictLabel = searchByGMED(trainMatrices, query);
            else if(distanceMeasure.equals("GMDTW"))
                predictLabel = searchByGMDTW(trainMatrices, query);

            //System.out.println("predictLabel : " + predictLabel + " realLabel:" + query.getLabel());
            if(predictLabel != query.getLabel())
                errorCnt++;
        }

        return errorCnt/queryCnt;
    }

    /*
    The leave-one-out cross-validation(LOOCV) method is used to search for the best parameters m and n
    only the GMED measure is used to search suitable parameters(m,n) -> GMED measure is faster than the GMDTW
     */
    public static double LOOCV(GridMatrix[] trainMatrices)
    {
        double queryCnt = 0;
        double errorCnt = 0;

        for(int qIdx=0; qIdx<trainMatrices.length; qIdx++)
        {
            GridMatrix query = trainMatrices[qIdx];
            int predictLabel = -1;
            double minDist = Double.MAX_VALUE;

            for(int idx=0; idx<trainMatrices.length; idx++)
            {
                if(qIdx == idx) //for leave-one-out cross validation
                    continue;

                GridMatrix base = trainMatrices[idx];

                double sim = Similarity.GMED(base, query);
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

    public static int searchByGMED(GridMatrix[] databases, GridMatrix query)
    {
        double minDist = Double.MAX_VALUE;
        int predictLabel = -1;
        for(int idx=0; idx<databases.length; idx++)
        {
            double sim = Similarity.GMED(databases[idx], query);
            if(sim < minDist)
            {
                minDist = sim;
                predictLabel = databases[idx].getLabel();
            }
        }
        return predictLabel;
    }

    public static int searchByGMDTW(GridMatrix[] databases, GridMatrix query)
    {
        double minDist = Double.MAX_VALUE;
        int predictLabel = -1;
        for(int idx=0; idx<databases.length; idx++)
        {
            double sim = Similarity.GMDTW(databases[idx], query);
            if(sim < minDist)
            {
                minDist = sim;
                predictLabel = databases[idx].getLabel();
            }
        }
        return predictLabel;
    }

    public static ArrayList<Integer> searchListByGMED(GridMatrix[] databases, GridMatrix query)
    {
        double minDist = Double.MAX_VALUE;
        ArrayList<Integer> predictLabelList = new ArrayList<>();
        for(int idx=0; idx<databases.length; idx++)
        {
            double sim = Similarity.GMED(databases[idx], query);
            if(sim < minDist)
            {
                minDist = sim;
                predictLabelList.clear();
                predictLabelList.add(databases[idx].getLabel());
            }
            else if(sim == minDist)
                predictLabelList.add(databases[idx].getLabel());
        }

        return predictLabelList;
    }

    public static ArrayList<Integer> searchListByGMDTW(GridMatrix[] databases, GridMatrix query)
    {
        double minDist = Double.MAX_VALUE;
        ArrayList<Integer> predictLabelList = new ArrayList<>();
        for(int idx=0; idx<databases.length; idx++)
        {
            double sim = Similarity.GMDTW(databases[idx], query);
            if(sim < minDist)
            {
                minDist = sim;
                predictLabelList.clear();
                predictLabelList.add(databases[idx].getLabel());
            }
            else if(sim == minDist)
                predictLabelList.add(databases[idx].getLabel());
        }

        return predictLabelList;
    }

}
