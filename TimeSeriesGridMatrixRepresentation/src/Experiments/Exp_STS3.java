package Experiments;

import GridBasedTimeSeries.Grid;
import struct.PointTra;
import struct.SetTS;
import utils.Tool;
import utils.Validation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jun on 2019-01-09.
 */
public class Exp_STS3 {
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
            //if (!file.getName().equals("OliveOil")) continue;
            System.out.println(file.getName());

            fileName = dirName + "/" + file.getName() + "/" + file.getName();
            File fileTRAIN =new File(fileName + "_TRAIN.csv");
            File fileTest=new File(fileName	 + "_TEST.csv");

            if (!(fileTRAIN.exists() && fileTest.exists())) continue;
            HashMap<String,Object> trainData= Tool.readData(fileName + "_TRAIN.csv");
            HashMap<String,Object> testData = Tool.readData(fileName + "_TEST.csv");
            int CLASS_NUM=(int) trainData.get("CLASS_NUM");//number of classes
            ArrayList<PointTra> trainOrg=(ArrayList<PointTra>) trainData.get("traData");//read original trainForMatrix dataset
            ArrayList<PointTra> testOrg=(ArrayList<PointTra>) testData.get("traData");	//read original test dataset

            trainOrg = Tool.minmaxNormalize(trainOrg); //conduct min_max normalization
            testOrg = Tool.minmaxNormalize(testOrg); //conduct min_max normalization

            Grid gm = new Grid();
            gm.trainForSet(trainOrg);
            System.out.println("m : " + gm.m + " n : " + gm.n);
            SetTS[] trainSets = gm.dataset2Set(trainOrg);
            SetTS[] testSets= gm.dataset2Set(testOrg);
            double errorRate = Validation.LOOCV_Set(trainSets);
            System.out.println("STS3 Train Error_Rate : " + errorRate);
            errorRate = Validation.oneNNClassificationErrorRate_Set(trainSets, testSets);
            System.out.println("STS3 Test Error_Rate : " + errorRate);
        }
    }
}
