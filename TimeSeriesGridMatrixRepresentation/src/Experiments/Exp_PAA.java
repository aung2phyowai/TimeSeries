package Experiments;

import struct.PAA;
import struct.PointTra;
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
public class Exp_PAA {
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

            //trainOrg = Tool.featureScaling(trainOrg); //conduct min_max normalization
            //testOrg = Tool.featureScaling(testOrg); //conduct min_max normalization

            int n = PAA.train(trainOrg);
            System.out.println("n :" + n);
            PAA[] trainPAAs = PAA.dataset2PAAs(trainOrg, n);
            PAA[] testPAAS = PAA.dataset2PAAs(testOrg, n);
            //double errorRate = PAA.LOOCV(trainPAAs);
            //System.out.println("PAA Train Error_Rate : " + errorRate);
            double errorRate = PAA.oneNNClassificationErrorRate(trainPAAs, testPAAS);
            System.out.println("PAA Test Error_Rate : " + errorRate);
        }
    }
}
