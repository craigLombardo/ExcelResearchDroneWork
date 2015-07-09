import javax.swing.JOptionPane;

public class PopUpBox{
  
  public PopUpBox(String infoMessage, String titleBar){
    JOptionPane.showMessageDialog(null, infoMessage, "Error Type: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
  }
}