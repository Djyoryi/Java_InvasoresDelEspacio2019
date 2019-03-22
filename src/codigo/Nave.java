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
        
    }
    
    public void mueve(){
        if (pulsadoIzquierda && x > 0){
        x -= 3;
        } 
        if(pulsadoDerecha && x < VentanaJuego.ANCHOPANTALLA - imagen.getWidth(null)){
        x += 3;
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
