package struct;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Created by jun on 2019-01-09.
 * GridBasedTimeSeries.Grid based set representation of Time-series
 */
public class SetTS extends ArrayList<Integer> {
    int label; // label of original time series
    int m; //number of rows
    int n; //number of columns

    public SetTS(int m, int n, int label)
    {
        this.m = m;
        this.n = n;
        this.label = label;
    }

    public void setTimeSeries(PointTra ts)
    {
        int T = ts.size();

        double height = 1.0/m;
        double width = (double)T/n;
        //double height = Math.round((1.0/m)*100)/100.0;
        //double width =  Math.round(((double)T/n)*100)/100.0;

        TreeSet<Integer> trajectorySet=new TreeSet<Integer>();

        for(int idx=0; idx<ts.size(); idx++)
        {
            double x = ts.get(idx).getX();
            double t = ts.get(idx).getT()+1;

            int i = (int)((1-x)/height);
            if (i == m)
                i = m-1;

            int j;
            if((int)(t/width) == Math.round((t/width)*1000000)/1000000.0)
                j = (int)(t/width)-1;
            else
                j = (int)(t/width);

            trajectorySet.add(i*n+j+1);
        }

        for(Iterator<Integer> it = trajectorySet.iterator(); it.hasNext();)
        {
            this.add(it.next());
        }
    }

    public int getLabel()
    {
        return this.label;
    }

    public void print() //print set
    {
        for(int s : this)
            System.out.print(s + " ");
        System.out.println();
    }
}
