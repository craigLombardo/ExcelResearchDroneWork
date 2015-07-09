import acm.program.*;
import acm.graphics.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;

public class smart extends GraphicsProgram{
  
  public static int APPLICATION_WIDTH = 300;
  
  private GLabel label, label2;
  private double angle = 0.0;
  private static final double delt = .1;
  private boolean right, left, shift;
  private GPolygon line;
  
  public void init(){
    
    label = new GLabel("", 125, 100);
    label2 = new GLabel("", 125, 150);
    
    line = new GPolygon(150,300);
    line.addVertex(0,0);
    line.addEdge(100,0);
    line.addEdge(-10,-10);
    line.addEdge(0,20);
    line.addEdge(10,-10);
    line.setFilled(true);
    line.setFillColor(Color.BLACK);
    
    add(label);
    add(label2);
    add(line);
    addKeyListeners();
    
  }
  
  public void run(){
    while(true){
      oneTimeStep();
      pause(10);
    }
  }
  
  public void oneTimeStep(){
    double nAngle=angle;
    int mult = shift ? 5 : 1;
    if(left) nAngle+=delt*mult;
    if(right) nAngle-=delt*mult;
    if(nAngle < 0) nAngle += 360;
    else if(nAngle >= 360) nAngle -= 360;
    rotateTo(nAngle);
    label.setLabel(String.format("%.1f",angle));
    label2.setLabel(""+90*getQuad(angle));
  }
  
  public void keyPressed(KeyEvent e){
    if(e.getKeyCode()==37) left = true;
    else if(e.getKeyCode() == 39) right = true;
    else if(e.getKeyCode() == 32){
      Scanner tmp = new Scanner(System.in);
      System.out.println("What would you like to set the angle to?");
      double newAngle = Double.parseDouble(tmp.nextLine());
      //System.out.println(getQuad(newAngle));
      tmp.close();
      System.out.println(checkAngle(newAngle));
    }
    else if(e.getKeyCode() == 16) shift = true;
    /*37 left
     38 up
     39 right
     40 down*/
    //System.out.println(e.getKeyCode());
  }
  
  public void keyReleased(KeyEvent e){
    if(e.getKeyCode()==37) left = false;
    else if(e.getKeyCode() == 39) right = false;
    else if(e.getKeyCode() == 16) shift = false;
  }
  
  private int getQuad(double angle){
    if(angle >= 270) return 3;
    else if(angle >= 180) return 2;
    else if(angle >= 90) return 1;
    else return 0;
  }
  
  public void rotateTo(double newAngle){
    line.rotate(newAngle - angle);
    angle = (double) Math.round(newAngle*100.0)/100.0;
  }
  
  public double checkAngle(double val){
    return correctAngle(val-angle);
  }
  
  /*public void checkAngle(double other){
   int result = checkRange(angle,other);
   if(result != 0 ) System.out.printf("forward: %d\n",result);
   else{
   result = checkRange(correctAngle(angle-90),other);
   if(result != 0 ) System.out.printf("right: %d\n",result);
   else{
   result = checkRange(correctAngle(angle-180),other);
   if(result != 0 ) System.out.printf("back: %d\n",result);
   else{
   result = checkRange(correctAngle(angle+90),other);
   if(result != 0 ) System.out.printf("left : %d\n",result);
   }
   }
   }
   }
   */
  /**
   * a match means the value is in the range of (start-45) -> start -> (start+45)
   * 0 = no match
   * 1 = ccw of starting point
   * -1 = cw of starting point
   */
  /*private int checkRange(double start, double val){
   double left = start + 45;
   double right = start - 45;
   double tmp = correctAngle(left);
   boolean lCross = left != tmp;
   left = tmp;
   tmp = correctAngle(right);
   boolean rCross = right != correctAngle(right);
   right = tmp;
   if(lCross){
   left -> 0
   360-> start
   start -> right
   
   if( (val <= left && val >= 0) || (val <= 360 && val >= start) ) return 1;
   else if( val <= start && val >= right) return -1;
   else return 0;
   }
   else if(rCross){
   
   left -> start
   start -> 0
   360 -> right
   
   if( (val <= left) && (val >= start) ) return 1;
   else if( (val <= start && val >= 0) || (val <= 360 && val >= right) ) return -1;
   else return 0;
   }
   else{
   
   left -> start        
   start -> right
   
   if(val <= left && val >= start) return 1;
   else if(val <= start && val >= right) return -1 ;
   else return 0;
   }
   }*/
  
  public double correctAngle(double nAngle){
    if(nAngle < 0) return nAngle += 360;
    else if(nAngle >= 360) return nAngle -= 360;
    return nAngle;
  }
  
  /*public static void main(String[] args){
   double num = 50;
   for(int x=0; x<100; x++){
   num *= 0.999;
   }
   System.out.println(num);
   }*/
  
}