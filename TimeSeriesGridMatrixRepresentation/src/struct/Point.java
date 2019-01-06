package struct;
public class Point implements Comparable{
    private double x,t;
    public Point()
    {
        super();
    }
    public Point(double t,double x)
    {
        this.t=t;
        this.x=x;
    }
    public void setX(double x)
    {
        this.x=x;
    }
    public void setT(double t)
    {
        this.t=t;
    }
    public double getX()
    {
        return x;
    }
    public double getT()
    {
        return t;
    }
    public boolean equals(Object obj)
    {
//	 System.out.println("yerw");
        if(!(obj instanceof Point))
            return false;
        Point p = (Point)obj;
        return (Math.abs(this.t-p.getT())<=0.0000001 && Math.abs(this.x-p.getX())<=0.000000001)?true:false;

    }
    public int compareTo(Object obj)
    {
        Point p=(Point) obj;
        if (Math.abs(this.t-p.getT())<=0.0000001 && Math.abs(this.x-p.getX())<=0.000000001) return 0;
        if (this.t<p.getT()) return -1;
        if (Math.abs(this.t-p.getT())>0.0000001) return 1;
        if (this.x<p.getX()) return -1;
        return 1;
    }
    public int hashCode()
    {
        return ((int)(1001*this.x)+3977*(int)this.t)%10000913;
    }
}