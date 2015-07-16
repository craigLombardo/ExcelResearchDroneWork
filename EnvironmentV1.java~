import acm.program.*;
import java.awt.event.*;
import java.lang.Exception;
import javax.swing.*;
//import java.util.Scanner;

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
      Thread.sleep(100); 
    }
    catch(java.lang.InterruptedException e){
      
    } 
    calc.startFlight();
    
  }
  
  /*public void read(){
   System.out.println("What angle?");
   Scanner sc = new Scanner(System.in);
   double val = Double.parseDouble(sc.next());
   sc.close();
   System.out.println("done");
   }*/
  
  public void keyPressed(KeyEvent e){
    //if(e.getKeyCode()==38) read();//calc.setAltitude(1/0.95);
    //if(e.getKeyCode()==40) calc.setAltitude(0.95);
    //if(e.getKeyCode()==32) calc.reset();
    //System.out.println(e.getKeyCode());
  }
  
  public void mousePressed(MouseEvent e){
    calc.reset(e.getX(),e.getY());
  }
  
}