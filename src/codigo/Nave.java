/*
 * La nave del juego
 */
package codigo;

import java.awt.Image;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Fernando de Castro
 */
public class Nave {
    //necesito una nave que se dibuja en unas coordenadas
    //inicializamos las variables public, es public para que lo veamos desde la ventana del juego
    public Image imagen = null;
    public int x = 0;
    public int y = 0;
    private boolean pulsadoIzquierda = false;
    private boolean pulsadoDerecha = false;
    
    //constructor que inicializa el objeto que se llama igual que la clase y no devuleve nada (no es void ni boolean....)
    public Nave(){
        try {
            imagen = ImageIO.read(getClass().getResource("/imagenes/nave.png"));
        } catch (IOException ex) {
            //quitamos el logger porque no lo vamos a usar
        }
    }
    
    public void mueve(){
        if (pulsadoIzquierda){
        x--;
        } 
        if(pulsadoDerecha){
        x++;
        }
    
    }

    public boolean isPulsadoIzquierda() {
        return pulsadoIzquierda;
    }

    public void setPulsadoIzquierda(boolean pulsadoIzquierda) {
        this.pulsadoIzquierda = pulsadoIzquierda;
        this.pulsadoDerecha = false;
    }

    public boolean isPulsadoDerecha() {
        return pulsadoDerecha;
    }

    public void setPulsadoDerecha(boolean pulsadoDerecha) {
        this.pulsadoDerecha = pulsadoDerecha;
        this.pulsadoIzquierda = false;
    }
    
}
