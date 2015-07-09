import acm.graphics.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * This class constructs a drone
 */
public class Drone implements Runnable{
  
  private static final double MRADIUS=10;    //radius of motors (Dont change)
  
  private GPolygon body, pointer;            //the body and "pointer" to (show direction)
  private Motor m1, m2, m3, m4;              //four blades  
  private ArrayList<Motor> motors;           //the 4 motors of the drone
  private double cAngle=90;                  //current angle of the drone (starting pointing up)
  
  private double spinVal=0;
  private static final int MAX_SPIN_VAL=3;
  private static final double drag = 0.975;
  
  /**
   * This constructor creates a drone with a starting x,y a given time step and a given evironment
   */
  public Drone(double x, double y, double time, EnvironmentV1 en){
    double width = 20;
    double height = 40;
    
    body = new GPolygon(x, y);
    body.addVertex(-width/2,-height/2);
    body.addEdge(+width,0);
    body.addEdge(0,+height);
    body.addEdge(-width,0);
    body.addEdge(0,-height);
    body.setFilled(true);
    body.setFillColor(new Color(100,100,100));
    
    pointer = new GPolygon(x,y);
    pointer.addVertex(width/8,-height/5);
    pointer.addArc(width/4,width/4,0,360);
    pointer.setFilled(true);
    pointer.setFillColor(new Color(255,255,255));
    
    en.add(body);
    en.add(pointer);
    
    m1 = new Motor(x, y, 0, -height/2, MRADIUS, en);
    m2 = new Motor(x, y, width, -height/2, MRADIUS, en);
    m3 = new Motor(x, y, 0, +height/2, MRADIUS, en);
    m4 = new Motor(x, y, width, +height/2, MRADIUS, en);
    
    motors = new ArrayList<Motor>();
    motors.add(m1);
    motors.add(m2);
    motors.add(m3);
    motors.add(m4);
  }
  
  public void run(){
    while(true){
      oneTimeStep();
      body.pause(10);
    }
  }
  
  private void oneTimeStep(){
    if(spinVal!=0) spin(spinVal);
  }
  
  private void spin(double dAngle){
    for(Motor x : motors) x.rotate(dAngle);
    body.rotate(dAngle);
    pointer.rotate(dAngle);
    if(dAngle<0){
      m1.activate();
      m4.activate();
      m2.deactivate();
      m3.deactivate();
    }
    else if(dAngle>0){
      m2.activate();
      m3.activate();
      m1.deactivate();
      m4.deactivate();
    }
    else{
      for(Motor x : motors) x.deactivate();
    }
    cAngle+=dAngle;
    cAngle = cAngle < 0 ? cAngle + 360 : cAngle;
    cAngle = cAngle > 360 ? cAngle - 360 : cAngle;
  }
  
  public void move(double dx, double dy){
    body.move(dx,dy);
    pointer.move(dx,dy);
    for(Motor m : motors) m.move(dx,dy);
  }
  
  public void setLocation(double x, double y){
    body.setLocation(x,y);
    pointer.setLocation(x,y);
    for(Motor m : motors) m.setLocation(x,y);
  }
  
  public void pause(){
    body.pause(10);
  }
  
  public double getCAngle(){
    return cAngle;
  }
  
  public void setYawVal(double val, boolean dragOn){
    spinVal += val;
    spinVal = spinVal > MAX_SPIN_VAL ? MAX_SPIN_VAL : spinVal < -MAX_SPIN_VAL ? -MAX_SPIN_VAL : spinVal;
    if(dragOn) spinVal = spinVal*drag;
    if(Math.abs(spinVal) < 0.01) spinVal = 0;
  }
  
  public void scale(double sf){
    body.scale(sf);
    pointer.scale(sf);
    for(Motor x : motors) x.scale(sf);
  }
  
  public void auto(){
    for(Motor d : motors) d.activate();
  }
  
  public void man(){
    for(Motor d : motors) d.deactivate();
  }
  
}