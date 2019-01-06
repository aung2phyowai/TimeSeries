package struct;

/**
 * Created by jun on 2019-01-04.
 */
public class GridMatrix
{
    public int[][] matrix;
    int m;
    int n;
    int label;
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

        for(int idx=0; idx<ts.size(); idx++)
        {
            double x = ts.get(idx).getX();
            double t = ts.get(idx).getT();

            int i = (int)(x/height);
            if (i == m)
                i = m-1;
            int j = (int)(t/width);

            matrix[i][j]++;
        }
    }

    public void print()
    {
        for (int i=0; i<matrix.length; i++)
        {
            for(int j=matrix[i].length; j>0; j--)
                System.out.print(matrix[i][j-1]+ " ");
            System.out.println();
        }
    }
}
