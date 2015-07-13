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
  
  public void makeUnitVector(){
    double mag = Math.sqrt(Math.pow(xVal,2)+Math.pow(yVal,2));
    xVal /= mag;
    yVal /= mag;
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
  
  public void makeOrthogonal(double uAngle, double vAngle){
    double tmpx = xVal;
    xVal = yVal;
    yVal = tmpx;
    
    double newVAngle = vAngle;
    double newUAngle = uAngle;
    
    if(vAngle < 90 && uAngle > 270) newVAngle += 360;
    else if(uAngle < 90 && vAngle > 270) newUAngle += 360;
    
    if(newVAngle <= newUAngle) yVal = -2*yVal;
    else xVal = -2*xVal;
    
    
  }
  
}