package Experiments;

import GridBasedTimeSeries.Grid;
import struct.GridMatrix;
import struct.PointTra;
import utils.Tool;
import utils.Validation;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by jun on 2019-01-16.
 */
public class Exp_MultiGridMatrixSimilarity {
    public static void run() throws IOException
    {
        //String dirName = "../Datasets/UCR_Sample";
        String dirName = "../Datasets/UCR";
        String fileName;
        File dir = new File(dirName);
        File[] files = dir.listFiles();

        for (File file : files) {
            String[] fileList = {"50words", "Adiac", "Cricket_X", "Cricket_Y", "Cricket_Z", "ECG200", "ECG5000", "ElectricDevices", "FaceAll", "FacesUCR", "FISH", "FordB", "HandOutlines", "NonInvasiveFatalECG_Thorax1", "OSULeaf", "ProximalPhalanxTW", "SmallKitchenAppliances", "StarLightCurves", "SwedishLeaf", "synthetic_control", "Trace", "Two_Patterns", "uWaveGestureLibrary_Z", "wafer", "yoga"};
            //String[] fileList = {"synthetic_control", "Gun_Point", "CBF", "FaceAll", "OSULeaf", "SwedishLeaf", "50words", "Trace", "Two_Patterns", "wafer", "FaceFour", "Lighting2", "Lighting7", "ECG200", "Adiac", "yoga", "FISH", "Plane", "Car", "Beef", "Coffee", "OliveOil"};
            List<String> list = Arrays.asList(fileList);
            if(!list.contains(file.getName())) continue;
            System.out.println(file.getName()); //print file name
            //if (!file.getName().equals("ECG200")) continue;

            fileName = dirName + "/" + file.getName() + "/" + file.getName();
            File fileTRAIN =new File(fileName + "_TRAIN.csv");
            File fileTest=new File(fileName	 + "_TEST.csv");

            if (!(fileTRAIN.exists() && fileTest.exists())) continue;
            HashMap<String,Object> trainData= Tool.readData(fileName + "_TRAIN.csv");
            HashMap<String,Object> testData = Tool.readData(fileName + "_TEST.csv");
            int CLASS_NUM=(int) trainData.get("CLASS_NUM");//number of classes
            ArrayList<PointTra> trainOrg=(ArrayList<PointTra>) trainData.get("traData");//read original trainForMatrix dataset
            ArrayList<PointTra> testOrg=(ArrayList<PointTra>) testData.get("traData");	//read original test dataset

            trainOrg = Tool.featureScaling(trainOrg); //conduct min_max normalization
            testOrg = Tool.featureScaling(testOrg); //conduct min_max normalization

            //generate trainForMatrix error rate threshold
            ArrayList<Double> thresholdList = new ArrayList<>();
            for(int i=0; i<3; i++)
            {
                Grid grid = new Grid();
                GridMatrix[] trainMatrices = grid.dataset2Matrices(trainOrg);
                thresholdList.add(Validation.LOOCV_Matrix(trainMatrices));
            }
            Collections.sort(thresholdList);
            double threshold = thresholdList.get(1);

            //generate multi grids
            int m = 10; //number of grids
            Grid[] grids = new Grid[m];

            for (int g_idx=0; g_idx<m; g_idx++)
            {
                Grid grid = new Grid();
                GridMatrix[] trainMatrices = grid.dataset2Matrices(trainOrg);
                double trainErrorRate = Validation.LOOCV_Matrix(trainMatrices);
                if(trainErrorRate > threshold)
                {
                    g_idx--;
                    continue;
                }
                grid.setTrainMatrices(trainMatrices);
                grids[g_idx] = grid ;
            }

            //conduct 1-NN classification and get error rate by GMED
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
            System.out.println("MultiGrid GMED Error rate : " + Math.round(((double)errorCount/predictCount)*1000)/1000.0);
        }
    }
}
