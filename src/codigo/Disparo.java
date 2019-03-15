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
 * @author Fernando de castro 
 */
public class Disparo {
     //necesito una nave que se dibuja en unas coordenadas
    //inicializamos las variables public, es public para que lo veamos desde la ventana del juego
    public Image imagen = null;
    public int x = 0;
    public int y = 0;
    
    //constructor que inicializa el objeto que se llama igual que la clase y no devuleve nada (no es void ni boolean....)
    public Disparo(){
        try {
            imagen = ImageIO.read(getClass().getResource("/imagenes/disparo.png"));
        } catch (IOException ex) {
            //quitamos el logger porque no lo vamos a usar
        }
    }
    public void mueve(){
        
        y--;
        
    }
}
