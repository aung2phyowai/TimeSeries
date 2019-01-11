package Experiments;

import struct.PointTra;
import utils.Similarity;
import utils.Tool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jun on 2019-01-10.
 */
public class Exp_DTW {
    public static void run() throws IOException
    {
        //String dirName = "../Datasets/UCR_Sample";
        String dirName = "../Datasets/UCR";
        String fileName;
        File dir = new File(dirName);
        File[] files = dir.listFiles();

        for (File file : files) {
            String[] fileList = {"synthetic_control", "Gun_Point", "CBF", "FaceAll", "OSULeaf", "SwedishLeaf", "50words", "Trace", "Two_Patterns", "wafer", "FaceFour", "Lighting2", "Lighting7", "ECG200", "Adiac", "yoga", "FISH", "Plane", "Car", "Beef", "Coffee", "OliveOil"};
            List<String> list = Arrays.asList(fileList);
            if(!list.contains(file.getName())) continue;
            //if (!file.getName().equals("Trace")) continue;
            System.out.println(file.getName());

            fileName = dirName + "/" + file.getName() + "/" + file.getName();
            File fileTRAIN =new File(fileName + "_TRAIN.csv");
            File fileTest=new File(fileName	 + "_TEST.csv");

            if (!(fileTRAIN.exists() && fileTest.exists())) continue;
            HashMap<String,Object> trainData= Tool.readData(fileName + "_TRAIN.csv");
            HashMap<String,Object> testData = Tool.readData(fileName + "_TEST.csv");
            ArrayList<PointTra> trainOrg=(ArrayList<PointTra>) trainData.get("traData");//read original trainForMatrix dataset
            ArrayList<PointTra> testOrg=(ArrayList<PointTra>) testData.get("traData");	//read original test dataset

            trainOrg = Tool.featureScaling(trainOrg); //conduct min_max normalization
            testOrg = Tool.featureScaling(testOrg); //conduct min_max normalization

            double queryCnt = 0;
            double errorCnt = 0;

            for(int qIdx=0; qIdx<testOrg.size(); qIdx++)
            {
                queryCnt++;
                PointTra query = testOrg.get(qIdx);
                double minDist = Double.MAX_VALUE;

                int predictLabel = -1;
                for(int idx=0; idx<trainOrg.size(); idx++)
                {
                    PointTra base = trainOrg.get(idx);

                    double sim = Similarity.DTW(query, base);
                    if(sim < minDist)
                    {
                        minDist = sim;
                        predictLabel = base.getCla();
                    }
                }

                if(predictLabel != query.getCla())
                    errorCnt++;
            }

            double errorRate = errorCnt/queryCnt;
            System.out.println("DTW Test Error_Rate : " + errorRate);
        }
    }
}
