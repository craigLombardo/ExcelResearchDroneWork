import acm.graphics.GMath;
import java.lang.Exception;
import javax.swing.JTextArea;

public class Calculator implements Runnable{
  
  private Drone myDrone;
  public static final double MASS = 10;      //mass
  public static final double SPEED_INC=0.09; 
  private static final int MAX_FORCE = 1;     //the max force exerted per step by the drone
  private static final double MAX_YAW_SPEED = 0.05;
  private static final double MAX_TOTAL_FORCE = 50;
  private static final double MIN_SIZE = 0.25;
  private static final double MAX_SIZE = 1.9;
  private static final double ALT = 1000;
  private static final double D_ALT = 0.999;
  private static final double SHELL_BUFF=50d;
  private static final double MIN_ABS_VEL= 0.05;
  private double xForce=0, yForce=0;
  private double pXForce = 0, pYForce = 0;
  private double rXForce = 0, rYForce = 0;
  private double xVel=0, yVel=0, xPos=0, yPos=0;
  private double timeStep;
  private double drag=0.999;
  
  private static final double MAX_VEL = 125;
  
  private static final int countVal = 50;
  private int autoCount = countVal;
  
  private double altitude=1;
  
  private boolean fly=false;
  
  private ControllerInfo cont;
  
  private Objects[] objs;
  
  private static final int 
    MAX_THROTTLE = 23000,
    MAX_YAW=26000,
    MAX_PITCH=23000,
    MAX_ROLL=25000;
  
  private JTextArea info;
  
  public Calculator(double x, double y, double time, Drone drone, JTextArea text, EnvironmentV1 en) throws Exception{
    myDrone = drone;
    timeStep = time;
    
    xPos = x;
    yPos = y;
    
    try{
      cont = new ControllerInfo();
    }
    catch(Exception e){
      throw new Exception();
    }
    
    info = text;
    
    objs = new Objects[1];
    objs[0] = new Objects(800,200,50,en);
    //objs[1] = new Objects(600,200,50,en);
    //objs[2] = new Objects(700,200,50,en);
    //objs[3] = new Objects(500,200,50,en);
  }
  
  public void run(){
    while(true){
      
      if(cont.update()<0){
        new PopUpBox("Lost connection to the controller,\nexiting the program!","Connection Error!");
        System.exit(-1);
      }
      for(Objects obj : objs){
        double prevDist = obj.distance;
        double myDist = obj.getDistanceFrom(xPos,yPos);
        double stayAwayFromMe = SHELL_BUFF+Math.abs((MAX_VEL*sqrt(xVel,yVel))/(2.5*MAX_TOTAL_FORCE/MASS));
        updateShells(prevDist > myDist ? stayAwayFromMe : 0, obj);
        update();
        if(myDist < stayAwayFromMe && prevDist > myDist) obj.close(true);
        if( obj.close() && ((autoCount!=countVal) || (myDist < stayAwayFromMe && prevDist > myDist) || (myDist < SHELL_BUFF))){
          //System.out.println(stayAwayFromMe);
          pXForce = 0;
          pYForce = 0;
          rXForce = 0;
          rYForce = 0;
          flyDrone(false, obj);
          myDrone.auto();
          if(autoCount == 0){
            obj.close(false);
            autoCount = countVal;
          }
          else autoCount--;
        }
      }
      if(fly && autoCount == countVal){
        myDrone.man();
        flyDrone(true, null);
      }
    }
  }
  
  public void flyDrone(boolean manual, Objects obj){
    double cAngle = myDrone.getCAngle();
    double pForce=0;
    double rForce=0;
    
    if(manual){
      pForce = MAX_FORCE*getPitchPercent();
      rForce = MAX_FORCE*getRollPercent();
      
      pXForce += (pForce*GMath.cosDegrees(cAngle));
      pYForce += (pForce*GMath.sinDegrees(cAngle));
      
      rXForce += (rForce*GMath.cosDegrees(cAngle-90.0));
      rYForce += (rForce*GMath.sinDegrees(cAngle-90.0));
      
      xForce = pXForce + rXForce;
      yForce = pYForce + rYForce;
    }
    
    else{
      //if unaltered then there is only repulsion as the vector is from the object pointing in the direction of the drone
      Vector unitV = getUnitVectorFrom(obj);
      /*
       * This method style allows for a smoother avoidance rather than just straight repulsion
       */
      /*
       * //start
       */
      
      double an1 = 180-getAngle(unitV.x(),unitV.y());
      double an2 = getAngle(xVel,yVel);
      unitV.makeOrthogonal(an1 < 0 ? an1 + 360 : an1, an2 < 0 ? an2 + 360 : an2);
      //end
      //*/
      
      if(sqrt(xVel,yVel) > 0.5) unitV.mulFactor(sqrt(xVel,yVel));
      else unitV.mulFactor(2);
      xForce += unitV.x();
      yForce -= unitV.y();
    }
    
    xForce = xForce > MAX_TOTAL_FORCE ? MAX_TOTAL_FORCE : xForce < -MAX_TOTAL_FORCE ? -MAX_TOTAL_FORCE : xForce;
    yForce = yForce > MAX_TOTAL_FORCE ? MAX_TOTAL_FORCE : yForce < -MAX_TOTAL_FORCE ? -MAX_TOTAL_FORCE : yForce;
    
    if(pForce==0){
      pXForce = 0;
      pYForce = 0;
    } 
    
    if(rForce == 0){
      rXForce = 0;
      rYForce = 0;
    }
    
    analyze(xForce, yForce);
    setYaw();
    //setAltitude();
    myDrone.pause();
  }
  
  private Vector getAccel(Vector comp){
    return new Vector(comp.x()/MASS,comp.y()/MASS);
  }
  
  private Vector getVel(Vector comp){
    Vector accelVector = getAccel(comp);
    double nXVel = (xVel + accelVector.x()*timeStep)*drag;
    double nYVel = (yVel + accelVector.y()*timeStep)*drag;
    xVel = nXVel <= -MAX_VEL ? -MAX_VEL : nXVel >= MAX_VEL ? MAX_VEL : Math.abs(nXVel)<MIN_ABS_VEL ? 0 : nXVel;
    yVel = nYVel <= -MAX_VEL ? -MAX_VEL : nYVel >= MAX_VEL ? MAX_VEL : Math.abs(nYVel)<MIN_ABS_VEL ? 0 : nYVel;
    return new Vector(xVel,yVel);
  }
  
  private Vector getPos(Vector comp){
    Vector velVector = getVel(comp);
    double nXPos = xPos + velVector.x()*timeStep;
    double nYPos = yPos - velVector.y()*timeStep;
    xPos = nXPos;
    yPos = nYPos;
    return new Vector(nXPos,nYPos); 
  }
  
  public void analyze(double xComp, double yComp){
    Vector output = getPos(new Vector(xComp,yComp));
    xComp*=drag;
    yComp*=drag;
    myDrone.setLocation(output.x(),output.y());
  }
  
  private double getThrottlePercent(){
    double throttle = cont.getThrottle();
    double val = throttle < -MAX_THROTTLE ? -MAX_THROTTLE : throttle > MAX_THROTTLE ? MAX_THROTTLE : throttle;
    return (val+MAX_THROTTLE)/(2*MAX_THROTTLE);
  }
  
  private double getYawPercent(){
    double yaw = -cont.getYaw();
    double yawPercent = yaw < -MAX_YAW ? -MAX_YAW : yaw > MAX_YAW ? MAX_YAW : yaw;
    return (yawPercent/MAX_YAW);
  }
  
  private double getRollPercent(){
    double roll = -cont.getRoll();
    double rollPercent = roll < -MAX_ROLL ? -MAX_ROLL : roll > MAX_ROLL ? MAX_ROLL : roll;
    return (rollPercent/MAX_ROLL);
  }
  
  private double getPitchPercent(){
    double pitch = cont.getPitch(); 
    double pitchPercent = pitch < -MAX_PITCH ? -MAX_PITCH : pitch > MAX_PITCH ? MAX_PITCH : pitch;
    return (pitchPercent/MAX_PITCH);
  }
  
  private void setYaw(){
    myDrone.setYawVal(-MAX_YAW_SPEED*getYawPercent(),Math.abs(getYawPercent()) < 0.01);
  }
  
  public void setAltitude(){
    double throttle = getThrottlePercent();    
    double sf = D_ALT;
    if(altitude*D_ALT > MIN_SIZE && altitude*D_ALT < MAX_SIZE){
      sf = (altitude > throttle*(MAX_SIZE-MIN_SIZE)+MIN_SIZE) ? sf : 1/sf;
      altitude*=sf;
      myDrone.scale(sf);
    }
  }
  
  public void reset(double tx, double ty){
    xVel=0;
    yVel=0;
    xPos=tx;
    yPos=ty;
  }
  
  public void startFlight(){
    fly = true;
  }
  
  public void update(){
    String text = String.format("  X: %.2f\n  Y: %.2f\n  Z: %.2f\n  dX: %.2f\n  dY: %.2f\n  dZ: %.2f\n  Throttle: %.3f%%\n  Yaw: %.3f\n  Roll: %.3f\n  Pitch: %.3f\n",
                                xPos,yPos,altitude*ALT,xVel,yVel,0.0,100.0*getThrottlePercent(),myDrone.getCAngle(),getRollPercent(),getPitchPercent());
    info.setText(text);
  }
  
  public Vector getUnitVectorFrom(Objects obj){
    if(obj == null) return new Vector(0d,0d);
    Vector objPos = obj.getPosition();
    double dx = xPos - objPos.x();
    double dy = yPos - objPos.y();
    double mag = sqrt(dx,dy);
    return new Vector(dx/mag,dy/mag);
  }
  
  public double getAngle(double x,double y){
    //Vector vel = new Vector(xVel,yVel);
    return Math.toDegrees(Math.atan2(y,x));
  }
  
  public void updateShells(double dist, Objects obj){
    obj.updateMyShell(dist);
  }
  
  public double sqrt(double x, double y){
    return Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
  }
  
}