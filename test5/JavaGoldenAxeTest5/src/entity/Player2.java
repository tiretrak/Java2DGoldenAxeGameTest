package entity;

import infra.PlayerControl;
import scene.Stage;

/**
 * Player2 class.
 * 
 * @author Leonardo Ono (ono.leo80@gmail.com)
 */
public class Player2 extends Player1 {

    public Player2(Stage stage, PlayerControl control) {
        super(stage, control);
    }

    @Override
    public void init() {
        super.init(); //To change body of generated methods, choose Tools | Templates.
        x = 70;
        z = 180;
    }
 
    
}
