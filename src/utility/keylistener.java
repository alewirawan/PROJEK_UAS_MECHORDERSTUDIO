/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utility;

import java.awt.event.KeyEvent;
import javax.swing.JTextField;

/**
 *
 * @author alewi
 */
public class keylistener {
    public static void onlyNumber(KeyEvent evt, JTextField field, int maxLength) {
        char c = evt.getKeyChar();
        
        boolean notNumber = !Character.isDigit(c);
        
        boolean exceedLength = field.getText().length() >= maxLength;
        
        if(notNumber || exceedLength){
            evt.consume();
        }
    }
}
