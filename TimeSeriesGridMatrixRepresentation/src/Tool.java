import struct.Point;
import struct.PointTra;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jun on 2019-01-04.
 */
public class Tool {
    public static HashMap<String,Object> readData(String fileName)
            throws NumberFormatException, IOException {
        ArrayList<PointTra> traData=new ArrayList<PointTra>();
        PointTra oneTra;
        Point p;
        String str;
        String[] str1;
        boolean flag;
        FileInputStream f = new FileInputStream(fileName);
        BufferedReader dr = new BufferedReader(new InputStreamReader(f));
        int minClaID=Integer.MAX_VALUE;
        while ((str = dr.readLine()) != null) {
            if (str.isEmpty())
                continue;
            oneTra = new PointTra();
            str1 = str.split(",");
            flag = true;
            for (int i = 0; i < str1.length; i++) {
                if (!str1[i].isEmpty()) {
                    if (flag == true) {
                        oneTra.setCla(Float.valueOf(str1[i]).intValue());
                        if (oneTra.getCla()<minClaID) minClaID=oneTra.getCla(); //find the min classID
                        flag = false;
                        continue;
                    }
                    p = new Point();
                    p.setX(Double.valueOf(str1[i]));
                    p.setT(oneTra.size());
                    oneTra.add(p);
                }
            }
            if (oneTra.size() != 0) traData.add(oneTra);
        }
        dr.close();

        //deal with the CLASS ID,let it number from 0 to CLASS_NUM-1
        //and find the maxID of class
        int maxID=0;
        for (int i=0;i<traData.size();i++)
        {
            oneTra=traData.get(i);
            int nowClaID=oneTra.getCla()-minClaID;
            if (nowClaID>maxID) maxID=nowClaID;
            oneTra.setCla(nowClaID);
        }

        HashMap<String,Object> ans=new HashMap<String,Object>();
        ans.put("traData", traData);
        ans.put("CLASS_NUM", maxID+1);
        return ans;
    }

    public static  ArrayList<PointTra> minmaxNormalize(ArrayList<PointTra> datasets)
    {
        for(int i=0; i<datasets.size(); i++)
        {
            double max = Double.MIN_VALUE;
            double min = Double.MAX_VALUE;

            PointTra ts = datasets.get(i);
            for(int j=0; j<ts.size(); j++)
            {
                double x = ts.get(j).getX();
                if (max < x)
                    max = x;
                if (min > x)
                    min = x;
            }

            double range = max-min;
            for(int j=0; j<ts.size(); j++)
            {
                double x = ts.get(j).getX();
                ts.get(j).setX((x-min)/range);
            }
        }
        return datasets;
    }
}
