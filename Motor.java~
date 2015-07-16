import acm.graphics.*;
import java.awt.*;

public class Motor{
  
  private GPolygon motor;
  private int rVal=100, gVal=100, bVal=100;
  
  public Motor(double x, double y, double xOff, double yOff, double radius, EnvironmentV1 en){
    motor = new GPolygon(x, y);
    motor.addVertex(xOff, yOff);
    motor.addArc(2*radius,2*radius,0,360);
    motor.setFilled(true);
    motor.setFillColor(new Color(rVal, gVal, bVal));
    
    en.add(motor);
  }
  
  public void activate(){
    //if(gVal<255){
    gVal=255;
    motor.setFillColor(new Color(rVal,gVal,bVal));
    //}
  }
  
  public void deactivate(){
    gVal=100;
    motor.setFillColor(new Color(rVal,gVal,bVal));
  }
  
  public void rotate(double theta){
    motor.rotate(theta);
  }
  
  public void move(double x, double y){
    motor.move(x,y);
  }
  
  public void setLocation(double x, double y){
    motor.setLocation(x,y);
  }
  
  public void scale(double sf){
    motor.scale(sf);
  }
}