import struct.GridMatrix;
import struct.PointTra;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static sun.misc.Version.print;

/**
 * Created by jun on 2019-01-04.
 */
public class Exp_MultiGridClassification {
    public static void run() throws IOException
    {
        //String dirName = "../Datasets/UCR_Sample";
        String dirName = "../Datasets/UCR";
        String fileName;
        File dir = new File(dirName);
        File[] files = dir.listFiles();

        for (File file : files) {
            //System.out.println(file.getName()); //print file name
            if (!file.getName().equals("Lighting2")) continue;

            fileName = dirName + "/" + file.getName() + "/" + file.getName();
            File fileTRAIN =new File(fileName + "_TRAIN.csv");
            File fileTest=new File(fileName	 + "_TEST.csv");

            if (!(fileTRAIN.exists() && fileTest.exists())) continue;
            HashMap<String,Object> trainData=Tool.readData(fileName + "_TRAIN.csv");
            HashMap<String,Object> testData = Tool.readData(fileName + "_TEST.csv");
            int CLASS_NUM=(int) trainData.get("CLASS_NUM");//number of classes
            ArrayList<PointTra> trainOrg=(ArrayList<PointTra>) trainData.get("traData");//read original train dataset
            ArrayList<PointTra> testOrg=(ArrayList<PointTra>) testData.get("traData");	//read original test dataset

            trainOrg = Tool.minmaxNormalize(trainOrg); //conduct min_max normalization
            testOrg = Tool.minmaxNormalize(testOrg); //conduct min_max normalization

            int m = 30; //number of grids
            Grid[] grids = new Grid[m];
            //double[][][] point_table = new double[testOrg.size()][m][CLASS_NUM];

            for (int g_idx=0; g_idx<m; g_idx++)
            {
                Grid grid = new Grid();
                grid.setTrainMatrices(grid.dataset2Matrices(trainOrg));
                grids[g_idx] = grid ;
            }

            //conduct 1-NN classification and get error rate

            int predictCount = 0;
            int errorCount = 0;
            for(int q_idx=0; q_idx<testOrg.size(); q_idx++)
            {
                Map<Integer, Double> pointDict = new HashMap<Integer, Double>();

                for(int g_idx=0; g_idx<m; g_idx++)
                {
                    Grid grid = grids[g_idx];
                    GridMatrix queryGridMatrix = grid.ts2Matrix(testOrg.get(q_idx));

                    ArrayList<Integer> predictLabel = Validation.searchListByGMED(grid.getTrainMatrices(), queryGridMatrix);

                    for(int label: predictLabel)
                    {
                        double point = 1.0/predictLabel.size();
                        pointDict.put(label, pointDict.getOrDefault(label, 0d) + point);
                    }
                }

                double maxPoint = Double.MIN_VALUE;
                int predictLabel = -1;
                for(Integer label: pointDict.keySet())
                {
                    double point = pointDict.getOrDefault(label, 0d);
                    if(maxPoint < point)
                    {
                        maxPoint = point;
                        predictLabel = label;
                    }
                }
                predictCount++;
                if(predictLabel != testOrg.get(q_idx).getCla())
                    errorCount++;
            }
            System.out.println("Error rate : " + (double)errorCount/predictCount);
        }
    }
}
