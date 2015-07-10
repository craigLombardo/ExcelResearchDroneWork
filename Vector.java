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
  
  public void makeOrthogonal(double uAngle, double vAngle){
    double tmpx = xVal;
    xVal = yVal;
    yVal = tmpx;
    
    int quad;
    if(uAngle >= 270) quad = 2;
    else if(uAngle >= 180) quad = 1;
    else if(uAngle >= 90) quad = 4;
    else quad = 3;
    //System.out.println(quad);
    if(quad == 1){
      if(vAngle >= 135) yVal = -yVal;
      else xVal = -xVal;
    }
    else if(quad == 2){
      if(vAngle <= 315) yVal = yVal;
      else xVal = -xVal;
    }
    else if(quad == 3){
      //System.out.println("here");
      if(vAngle <= 45) yVal = -yVal;
      else xVal = -xVal;
    }
    else{
      if(vAngle <= 225) yVal = yVal;
      else xVal = -xVal;
    }
    //if(vAngle <= uAngle) yVal = -yVal;
    //else xVal = -xVal;
    
    
  }
  
}