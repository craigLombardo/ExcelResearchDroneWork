/* ControllerInfo.java */
import java.lang.Exception;

public class ControllerInfo{
  
  native int getRoll();
  native int getPitch();
  native int getThrottle();
  native int getYaw();
  native int connect();
  native int update();
  
  public ControllerInfo() throws Exception{
    if(connect() != 0){
      System.out.println("A Connection could not be made");
      throw new Exception();
    }
  }
  
  static{
    System.loadLibrary("Controls");
  }
  
  public static void main(String[] args){
    try{
      ControllerInfo dr = new ControllerInfo();
      dr.connect();
      while(true){
        
        System.out.printf("Throttle: %d Yaw: %d Roll: %d Pitch: %d\r",dr.getThrottle(),dr.getYaw(),dr.getRoll(),dr.getPitch());
      }
    }
    catch(Exception e){
      
    }
  }
}