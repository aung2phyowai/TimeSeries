package PLA;

/**
 * Created by jun on 2019-01-11.
 */
public class Representation {

    public static double[][] PLA(double[] ts)
    {
        int n = ts.length;
        int k = n/3;
        int remainder = n%3;

        double[] balanceMemory = new double[k];
        double[][][] plaMemory = new double[k][][];

        double minBalnace = Double.MAX_VALUE;
        int optimizedK = k;

        //initialize
        double[][] pla = new double[k][];
        for(int t=0; t<ts.length; t++)
        {
            int s = t/3;
            int idx = t%3;

            if(idx == 0)
            {
                if(s < k-1)
                    pla[s] = new double[3];
                else
                    pla[s] = new double[3+remainder];
            }

            if(s < k)
                pla[s][idx] = ts[t];
            else
                pla[s-1][idx+3] = ts[t];
        }

        while(k > 1)
        {
            double[] errors = new double[k];

            for(int i=0; i<k; i++)
            {
                double[] segment = pla[i];
                double[] coeff = linearRegression(segment);
                errors[i] = getResidualError(segment, coeff);
            }

            balanceMemory[k] = getBalanceOfError(errors);
            plaMemory[k] = pla;

            if(balanceMemory[k] < minBalnace)
            {
                minBalnace = balanceMemory[k];
                optimizedK = k;
            }

            if(k == 2)
                break;

            pla = merge(pla, errors);
            k = pla.length;
        }

        return plaMemory[optimizedK];
    }

    private static double[][] merge(double[][] pla, double[] currentErrors)
    {
        double[][] mergedPLA = null;
        int k = pla.length;

        double minBalance = Double.MAX_VALUE;

        for(int i=0; i<k-1; i++)
        {
            int length1 = pla[i].length;
            int length2 = pla[i+1].length;

            double[] mergedSegment = new double[length1+length2];
            for(int j=0; j<mergedSegment.length; j++)
            {
                if(j < length1)
                    mergedSegment[j] = pla[i][j];
                else
                    mergedSegment[j] = pla[i+1][j-length1];
            }

            double[] coeff = linearRegression(mergedSegment);
            double err = getResidualError(mergedSegment, coeff);

            double[] newErrors = new double[k-1];

            for(int j=0, pointer=0; j<k-1; j++, pointer++)
            {
                if(j == i)
                {
                    newErrors[j] = err;
                    pointer++;
                }
                else
                    newErrors[j] = currentErrors[pointer];
            }

            double balanceOfError = getBalanceOfError(newErrors);

            if(balanceOfError < minBalance)
            {
                minBalance = balanceOfError;

                mergedPLA = new double[k-1][];
                for(int j=0, pointer=0; j<k-1; j++, pointer++)
                {
                    if(j == i)
                    {
                        mergedPLA[j] = mergedSegment;
                        pointer++;
                    }
                    else
                        mergedPLA[j] = pla[pointer];
                }
            }
        }

        return mergedPLA;
    }

    public static double[] linearRegression(double[] points)
    {
        double[] params = new double[2]; //a, b  a: gradient, b: bias
        double n = points.length;
        double x_sum = 0;
        double x_square_sum = 0;
        double xy_sum = 0;
        double y_sum = 0;

        for(int i=0; i<n; i++)
        {
            x_sum += i;
            x_square_sum += i*i;
            y_sum += points[i];
            xy_sum += i*points[i];
        }

        double det = (x_square_sum*n)-(x_sum*x_sum); //ad-bc
        params[0] = (n*xy_sum - x_sum*y_sum)/det;
        params[1] = (-x_sum*xy_sum + x_square_sum*y_sum)/det;

        return params;
    }

    private static double getResidualError(double[] segment, double[] coeff)
    {
        double err = 0;
        int n = segment.length;
        for(int x=0; x<n; x++)
        {
            double d = segment[x]-(coeff[0]*x+coeff[1]);
            err += d*d;
        }
        err = err/n;
        return err;
    }

    private static double getBalanceOfError(double[] errors)
    {
        double N = errors.length;
        double avg = getAverage(errors);

        double errorSquareSum = 0;
        for(int i=0; i<N; i++)
            errorSquareSum += Math.pow(errors[i]-avg, 2);

        return Math.sqrt(errorSquareSum/(N-1));
    }

    private static double getAverage(double[] errors)
    {
        double N = errors.length;

        double sum = 0;
        for(int i=0; i<N; i++)
            sum += errors[i];

       return sum/N;
    }

}
