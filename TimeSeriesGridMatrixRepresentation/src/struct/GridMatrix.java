package struct;

/**
 * Created by jun on 2019-01-04.
 */
public class GridMatrix
{
    public int[][] matrix;
    int label;
    int m;
    int n;
    int T;

    public GridMatrix(int m, int n, int label)
    {
        this.m = m;
        this.n = n;
        this.matrix =new int[m][n];
        this.label = label;
    }

    public void setTimeSeries(PointTra ts)
    {
        int T = ts.size();

        double height = 1.0/m;
        double width = (double)T/n;
        //double height = Math.round((1.0/m)*100)/100.0;
        //double width =  Math.round(((double)T/n)*100)/100.0;

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


            matrix[i][j]++;
        }
    }

    /*
    public void setTimeSeries(PointTra ts)
    {
        int T = ts.size();

        double height = 1.0/m;
        double width = (double)T/n;

        for(int idx=0; idx<ts.size(); idx++)
        {
            double x = ts.get(idx).getX();
            double t = ts.get(idx).getT();

            if(x==0 || t==0)
                continue;

            int i = (int)(x/height);
            if (x != 0 && i*height == x)
                i--;

            int j = (int)(t/width);
            if (j != 0 && j*width == t)
                j--;

            matrix[i][j]++;
        }
    }
    */


    public int getLabel()
    {
        return this.label;
    }

    public void print()
    {
        for (int i=0; i<matrix.length; i++)
        {
            for(int j=0; j<matrix[i].length; j++)
                System.out.print(matrix[i][j]+ " ");
            System.out.println();
        }
    }
}
