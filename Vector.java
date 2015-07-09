public class Vector{
  
  private Double xVal, yVal;
  
  public Vector(Double x, Double y){
    xVal = x;
    yVal = y;
  }
  
  public Double x(){
    return xVal;
  }
  
  public Double y(){
    return yVal;
  }
  
  public void flip(){
    xVal = -xVal;
    yVal = -yVal;
  }
  
  public double getMag(){
    return Math.sqrt(Math.pow(xVal,2)+Math.pow(yVal,2));
  }
  
  public double getAngle(){
    return 180.0-Math.toDegrees(Math.atan2(-yVal,-xVal));
  }
  
  public void mulFactor(double val){
    xVal *= val;
    yVal *= val;
  }
  
  public void print(){
    System.out.println("x comp: " + xVal);
    System.out.println("y comp: " + yVal);
  }
  
}