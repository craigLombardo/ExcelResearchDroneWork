import acm.program.*;
import java.awt.event.*;
import java.lang.Exception;
import javax.swing.*;

/*
 * This class serves as the graphical environment for the simulation.
 */
public class EnvironmentV1 extends GraphicsProgram{
  
  private Drone drone;
  public static final int APPLICATION_WIDTH=1400, APPLICATION_HEIGHT=1000;
  private Calculator calc;
  
  public void init(){
    double startX = 200, startY = 600;
    
    JFrame frame = new JFrame("Drone Readout");
    frame.setVisible(true);
    frame.setSize(300,200);
    
    JTextArea readout = new JTextArea();
    frame.add(readout);
    readout.setEditable(false);
    
    drone = new Drone(startX,startY,0.1,this);
    try{
      calc = new Calculator(startX,startY,0.033333,drone,readout,this);
    }
    catch(Exception e){
      new PopUpBox("Could not connect to a\ncontroller... exiting!","Connection Error!");
      System.exit(1);
    }
    (new Thread(calc)).start();
    (new Thread(drone)).start();
    
    addKeyListeners();
    addMouseListeners();
    
    try{
      //We wait just to ensure the controller is properly mounted before beginning, if there is an error it will become
      //apparent before the computer awakens, hopefully....
      Thread.sleep(100); 
    }
    catch(java.lang.InterruptedException e){
      
    } 
    calc.startFlight();
    
  }
  
  public void mousePressed(MouseEvent e){
    calc.reset(e.getX(),e.getY());
  }
  
}