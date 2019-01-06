import struct.Grid;
import struct.GridMatrix;
import struct.PointTra;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jun on 2019-01-04.
 */
public class Exp_MultiGridClassification {
    public static void run() throws IOException
    {
        String dirName = "../Datasets/UCR_Sample";
        //String dirName = "../Datasets/UCR";
        String fileName;
        File dir = new File(dirName);
        File[] files = dir.listFiles();

        for (File file : files) {
            //System.out.println(file.getName()); //print file name
            if (!file.getName().equals("CBF")) continue;

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

            Grid gm = new Grid(8, 30);
            GridMatrix[] trainMatrices = gm.dataset2Matrices(trainOrg);
            GridMatrix[] testMatrices = gm.dataset2Matrices(testOrg);

            double sim = Similarity.GMED(trainMatrices[0], trainMatrices[1]);
            System.out.println("sim : " + sim);
            sim = Similarity.GMDTW(trainMatrices[0], trainMatrices[0]);
            System.out.println("sim : " + sim);
        }
    }
}
