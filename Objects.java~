import acm.graphics.*;
import java.awt.*;

public class Objects{
  
  private GOval obj;
  private GOval shell;
  private EnvironmentV1 myEn;
  private double initRadius;
  private double myX, myY;
  public double distance;
  private boolean imClose = false;
  
  public Objects(double x, double y,double d, EnvironmentV1 en){
    obj = new GOval(x-d/2, y-d/2, d, d);
    obj.setFilled(true);
    obj.setFillColor(Color.RED);
    
    shell = new GOval(x-d, y-d, 0, 0);
    //shell.setFilled(false);
    //shell.setColor(Color.BLACK);
    
    en.add(obj);
    en.add(shell);
    myEn = en;
    
    myX = x;
    myY = y;
    initRadius = d;
  }
  
  public double getDistanceFrom(double xPos, double yPos){
    distance = Math.sqrt(Math.pow(xPos-myX,2)+Math.pow(yPos-myY,2))-initRadius;
    return distance;
  }
  
  public Vector getPosition(){
    return new Vector(myX,myY);
  }
  
  public void updateMyShell(double dist){
    myEn.remove(shell);
    shell = new GOval(myX-dist-initRadius/2, myY-dist-initRadius/2, 2*dist+initRadius, 2*dist+initRadius);
    shell.setFilled(false);
    shell.setColor(Color.BLACK);
    myEn.add(shell);
  }
  
  public boolean close(){
    return imClose;
  }
  
  public void close(boolean bool){
    imClose = bool;
  }
  
}