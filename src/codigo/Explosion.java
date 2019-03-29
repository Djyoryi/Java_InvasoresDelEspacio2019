/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
/**
 *
 * @author Fernando de Castro
 */
public class Explosion {
    public Image imagenExplosion = null;   
    private int duracionVivo = 100;
    private int x = 0;
    private int y = 0;

 public Explosion (){
       // try {
     //       imagenExplosion = ImageIO.read((getClass().getResource("/imagenes/invaders2.png")));
     //   } catch (Exception ex) {
    //    }
}
    
    public int getDuracionVivo() {
        return duracionVivo;
    }
    
    public void setDuracionVivo(int tiempoDeVida) {
        this.duracionVivo = tiempoDeVida;
    }
 
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

}