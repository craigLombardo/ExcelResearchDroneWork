/* ControllerInfo.java */
import java.lang.Exception;

/*
 * This class is used for the controller and gets the necessary controller readout from the implemented C Code
 */
public class ControllerInfo{
  
  /*
   * Self explanatory methods
   * native means they are from outside code (i.e. C Code in this case)
   */
  native int getRoll();
  native int getPitch();
  native int getThrottle();
  native int getYaw();
  native int connect();
  native int update();
  
  /*
   * The constructor method creates an instance of the ControllerInfo and throws an
   * exception if it cannot connect.
   * The exception is caught in the Calculator constructor
   */
  public ControllerInfo() throws Exception{
    if(connect() != 0){
      System.out.println("A Connection could not be made");
      throw new Exception();
    }
  }
  
  static{
    System.loadLibrary("Controls");
  }
  
  /*public static void main(String[] args){
    try{
      ControllerInfo dr = new ControllerInfo();
      dr.connect();
      while(true){
        
        System.out.printf("Throttle: %d Yaw: %d Roll: %d Pitch: %d\r",dr.getThrottle(),dr.getYaw(),dr.getRoll(),dr.getPitch());
      }
    }
    catch(Exception e){
      
    }
  }*/
}