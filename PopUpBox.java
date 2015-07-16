import javax.swing.JOptionPane;

/*
 * This class creates a popup which will halt the program and pull up a dialog box 
 */
public class PopUpBox{
  
  public PopUpBox(String infoMessage, String titleBar){
    JOptionPane.showMessageDialog(null, infoMessage, "Error Type: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
  }
}