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
    public int y = 2000; //al principio el disparo se pinta muy por debajo de la pantalla
    public boolean disparado = false;
    
    //constructor que inicializa el objeto que se llama igual que la clase y no devuleve nada (no es void ni boolean....)
    public Disparo(){
        try {
            imagen = ImageIO.read(getClass().getResource("/imagenes/disparo.png"));
        } catch (IOException ex) {
            //quitamos el logger porque no lo vamos a usar
        }
    }
    public void mueve() {
        if (disparado) {
            y-=10;
        }

    }
     public void posicionaDisparo(Nave _nave){
        x = _nave.x + _nave.imagen.getWidth(null)/2 - imagen.getWidth(null)/2;
        y = _nave.y - _nave.imagen.getHeight(null)/2;
      
    }
}
