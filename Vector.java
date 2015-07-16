public class Vector{
  
  private Double xVal, yVal;
  
  /*
   * This class was used because java does not support tuples, everything else can be pretty easily ported over 
   */
  
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
    
    double delt = 0.15;
    double um = 2.0;
    if(newVAngle <= newUAngle){
      if(Math.abs(xVal) < 0.1) xVal = yVal >= 0 ? um : -um;
      else xVal = yVal <= 0 ? (delt)*xVal : -(delt)*xVal;
      yVal = -yVal;
    }
    else{
      xVal = -xVal;
      if(Math.abs(yVal) < 0.1) yVal = xVal >= 0 ? um : -um;
      else yVal = xVal <= 0 ? (delt)*yVal : -(delt)*yVal;
    }
  }
  
}