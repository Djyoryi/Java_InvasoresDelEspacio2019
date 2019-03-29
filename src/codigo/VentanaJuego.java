/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import com.sun.javafx.scene.control.skin.Utils;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.Timer;

/**
 *
 * @author Fernando de Castro
 */
public class VentanaJuego extends javax.swing.JFrame {

    //para que una constante no cambie durante el juego
    static int ANCHOPANTALLA = 800;
    static int ALTOPANTALLA = 600;
    //declaramos variables

    //numero de marcianos que van a aparecer
    int filas = 8;
    int columnas = 10;
    //el buffer es un trozo de memoria y cuando estamos trabajan con cualquier 
    //juego, yo tengo una pantalla que van pasando cosas que las naves se mueves x ej.

    BufferedImage buffer = null;
    

    //creo un objeto de tipo nave
    Nave miNave = new Nave();
    Disparo miDisparo = new Disparo();
    Explosion miExplosion = new Explosion();
    boolean cancion = true;
    boolean dejaDeSonar = true;
    //Marciano miMarciano = new Marciano();
    Marciano[][] listaMarcianos = new Marciano[filas][columnas];
    boolean direccionMarcianos = false;
    // el contador me sirve para saber cuantas veces cambia de direccion
   
    int contadorMarcianos = 80;
    //el contador me sirve para decidir que imagen de marciano toca poner
    int contador = 0;
    
    
    //imagen para cargar el spritesheet con todos los sprites(imagenes) del juego
    BufferedImage plantilla = null;
    Image [][] imagenes;
    boolean comienzaElJuego = false;
    boolean finDelJuego = false;
    String mensaje ="";
    
    //el nuevo hilo de ejecucion es una nueva variable de tipo timer
    //cada 10 milisegundos va a llamar al codigo que esta ahi dentro
    Timer temporizador = new Timer(10 , new ActionListener() {
        @Override
       public void actionPerformed(ActionEvent e) {
           bucleDelJuego();
           
        }   
    });
    Timer temporizador1 = new Timer(0 , new ActionListener() {
        @Override
    public void actionPerformed(ActionEvent e) {
        if (cancion && dejaDeSonar){
           reproduce("/sonidos/gladiator1.wav");
           cancion = false;
        }
    }
    });       
    /**
     * Creates new form VentanaJuego
     */
    public VentanaJuego() {
        initComponents();
        //para cargar archivo de imagenes:
        //1º ruta
        //2º numero de filas
        //3º numero de columnas
        //4º ancho de cada elemento(sprite) del spritesheet
        //5º alto de cada elemento(sprite) del spritesheet
        //6º cambiar el tamaño de los sprites
        imagenes = cargaImagenes("/imagenes/invaders2.png", 5, 4, 64, 64, 2);
        temporizador1.start();
        setSize(ANCHOPANTALLA, ALTOPANTALLA);
        //crea la imagen del mismo alto y ancho que el jpanel.
        buffer = (BufferedImage) jPanel1.createImage(ANCHOPANTALLA, ALTOPANTALLA);
        //para poder escribir algo grafico con cosas como draw y demas inicializamos esto.
        buffer.createGraphics();
        //nuestro hilo de ejecucion paralelo se va a encargar de copiar el buffer sobre el jpanel
        //una cosa es escuchar lo que hace el usuario y otra es que los objetos que hay en la pantalla 
        //se dibujen otra vez todos los elementos en la posicion correspondiente
        //vamos a recalcular posiciones y dibujamos en el buffer las nuevas posiciones
        //y dibujamos el buffer sobre el jpanel.

        temporizador.start();
        
        //indico la imagen del disparo que quiero del sprite
        miDisparo.imagen = imagenes[2][4];

        //inicializo la posicion inicial de la nave
        miNave.imagen = imagenes[5][1];
        miNave.x = ANCHOPANTALLA / 2 - miNave.imagen.getWidth(this) / 2;
        miNave.y = ALTOPANTALLA - miNave.imagen.getHeight(this) - 40;
        

        //inicializo el array de marcianos
        /*for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                listaMarcianos[i][j] = new Marciano();
                listaMarcianos[i][j].imagen1 = imagenes[0][0];
                listaMarcianos[i][j].imagen2 = imagenes[0][1];
                listaMarcianos[i][j].x = j * (15 + listaMarcianos[i][j].imagen1.getWidth(null));
                listaMarcianos[i][j].y = i * (10 + listaMarcianos[i][j].imagen1.getHeight(null));
            }
        }
        */
        //1º numero de la fila de marcianos que esto creando en el juego    
        //2º fila dentro del spritesheet de marciano que quiero elegir
        //3º columna dentro del spritesheet de marciano que quiero elegir
        creaFilaDeMarciano(0, 0, 0);
        creaFilaDeMarciano(1, 2, 2);
        creaFilaDeMarciano(2, 4, 0);
        creaFilaDeMarciano(3, 0, 2);
        creaFilaDeMarciano(4, 1, 0);
        creaFilaDeMarciano(5, 1, 1);
        creaFilaDeMarciano(6, 1, 2);
        creaFilaDeMarciano(7, 1, 3);
        
       
    }
    private void inicioDePartida(Graphics2D start) {
        try {
            Image imagenInicio = ImageIO.read((getClass().getResource("/imagenes/inicio.png")));
            start.drawImage(imagenInicio, 250, 200, null);
        } catch (IOException ex) {
        }
    }

    private void finDePartida(Graphics2D finJuego) {
        try {
            
            Graphics2D g2 = (Graphics2D) buffer.getGraphics();
            g2.fillRect(0, 0, ANCHOPANTALLA, ALTOPANTALLA);
            Image imagenLuser = ImageIO.read((getClass().getResource("/imagenes/fin.png")));
            finJuego.drawImage(imagenLuser, 250, 200, null);
            dejaDeSonar = false;
        } catch (IOException ex) {
        }
    }
    
   private void ganaPartida(Graphics2D win) {
        try {
            
            Graphics2D g2 = (Graphics2D) buffer.getGraphics();
            g2.fillRect(0, 0, ANCHOPANTALLA, ALTOPANTALLA);
            Image imagenLuser = ImageIO.read((getClass().getResource("/imagenes/gana.png")));
            win.drawImage(imagenLuser, 250, 200, null);
            dejaDeSonar = false;
        } catch (IOException ex) {
        }
    }

    private void creaFilaDeMarciano(int numeroFila, int spriteFila, int spriteColumna) {
        for (int j = 0; j < columnas; j++) {
            listaMarcianos[numeroFila][j] = new Marciano();
            listaMarcianos[numeroFila][j].imagen1 = imagenes[spriteFila][spriteColumna];
            listaMarcianos[numeroFila][j].imagen2 = imagenes[spriteFila][spriteColumna];
            listaMarcianos[numeroFila][j].x = j * (10 + listaMarcianos[numeroFila][j].imagen1.getWidth(null));
            listaMarcianos[numeroFila][j].y = numeroFila * (5 + listaMarcianos[numeroFila][j].imagen1.getHeight(null));
        }
    }
    
    /*
    este metodo va a servir para crear el array de imagenes con todas las imagenes
    del spritesheet. devolvera un array de dos dimensiones con las imagenes colocadas
    tal y como estan en el spritesheet.
    */
    
    private Image[][] cargaImagenes(String nombreArchivoImagenes, int numFilas, int numColumnas, int ancho, int alto, int escala){
         try {
            plantilla = ImageIO.read(getClass().getResource(nombreArchivoImagenes));
        } catch (IOException ex) { }
         
         Image [][] arrayImagenes = new Image[numFilas+1][numColumnas+1];
         
        //cargo las imagenes de forma individual en cada imagen del array de imagenes
        for (int i = 0; i < numFilas ; i++) {
            for (int j = 0; j < numColumnas; j++) {
                arrayImagenes[i][j] = plantilla.getSubimage(j*ancho, i*alto, ancho, alto);
                arrayImagenes[i][j] = arrayImagenes[i][j].getScaledInstance(ancho/escala, ancho/escala, Image.SCALE_SMOOTH);
            }
        }
        //la ultima fila aparte porque mide la mitad
        for (int j = 0; j < numColumnas; j++) {
                arrayImagenes[numFilas][j] = plantilla.getSubimage(j*ancho, numFilas*alto, ancho, alto/2);
                arrayImagenes[numFilas][j] = arrayImagenes[numFilas][j].getScaledInstance(2*ancho/escala, ancho/escala, Image.SCALE_SMOOTH);
        }
        //cargo la ultima columna aparte por mide la mitad
        for (int i = 0; i < numFilas ; i++) {
            
                arrayImagenes[i][numColumnas] = plantilla.getSubimage(numColumnas*ancho, i*alto, ancho/2, alto);
                arrayImagenes[i][numColumnas] = arrayImagenes[i][numColumnas].getScaledInstance(ancho/escala /2, ancho/escala, Image.SCALE_SMOOTH);
            
        }
//        //la ultima fila del sprite solo mide 32 de alto asi que la hacemos aparte
//        for (int j = 0; j < 4; j++) {
//            arrayImagenes[54][j] = plantilla.getSubimage(j*64, 5*64, 64, 32);
//            
//        }
//        //la ultima columna del sprite solo mide 32 de ancho asi que la hacemos aparte
//         for (int i = 0; i < 3; i++) {
//            arrayImagenes[i][3] = plantilla.getSubimage(4*64, i*64, 32, 64);
//            
//        }
         return arrayImagenes;
    }
    

    private void bucleDelJuego() {
        //se encarga del redibujado de los objetos en el jpanel1
        //primero borramos todo lo que hay en el buffer
        contador++;
        
        //le digo a g2 que apunte a la zona del buffer
        Graphics2D g2 = (Graphics2D) buffer.getGraphics();
    if(!comienzaElJuego){
        inicioDePartida(g2);
        
    }else
        
    if(!finDelJuego && comienzaElJuego)  {  
        //g2 todo lo que dibujes va a ser en color negro
        g2.setColor(Color.black);
        
       // pantallaInicial();
        //if(comienzaElJuego == true){
        //y ahora borro
        g2.fillRect(0, 0, ANCHOPANTALLA, ALTOPANTALLA);
        //inicioPartida(g2);
        ////////////////////////////////////////////////////////////////////
        //redibujamos aqui cada elemento
        g2.drawString(mensaje, 100, 200);
        g2.drawImage(miDisparo.imagen, miDisparo.x, miDisparo.y, null);
        g2.drawImage(miNave.imagen, miNave.x, miNave.y, null);
        chequeaColision();
        pintaMarcianos(g2);
        
        pintaExplosiones(g2);
        miNave.mueve();
        miDisparo.mueve();
        
        chequeaColisionNave();
        if(contadorMarcianos == 0){
            ganaPartida(g2);
        }
           }else{
        finDePartida(g2);
    }
        ////////////////////////////////////////////////////////////////////
        //************* fase final, se dibuja el buffer ******************//
        //************* de golpe sobre el jpanel1 **********************//
        //convertir de una clase a otra
        g2 = (Graphics2D) jPanel1.getGraphics();
        //(lo que va a dibujar , donde, donde ,null(que se pone siempre))
        //en realidad
        g2.drawImage(buffer, 0, 0, null);

    }

    private void chequeaColisionNave() {
        //creo un marco para guardar el borde de la imagen de la nave y marciano

        Rectangle2D.Double rectanguloNave = new Rectangle2D.Double();
        Rectangle2D.Double rectanguloMarciano = new Rectangle2D.Double();

        rectanguloNave.setFrame(miNave.x, miNave.y, miNave.imagen.getWidth(null), miNave.imagen.getHeight(null));

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                rectanguloMarciano.setFrame(listaMarcianos[i][j].x,
                        listaMarcianos[i][j].y,
                        listaMarcianos[i][j].imagen1.getWidth(null),
                        listaMarcianos[i][j].imagen1.getHeight(null));
                if (rectanguloNave.intersects(rectanguloMarciano) && listaMarcianos[i][j].vivo == true) {
                    //algún marciano ha tocado con la nave
                    finDelJuego = true;
                }
            }
        }
    }
    
    //lo que vamos hacer es que si el rectangulo del marciano y el rectangulo 
    //del disparo comparten pixeles en comun devuelva true
    private void chequeaColision() {
        Rectangle2D.Double rectanguloMarciano = new Rectangle2D.Double();
        Rectangle2D.Double rectanguloDisparo = new Rectangle2D.Double();
        
        
        rectanguloDisparo.setFrame(miDisparo.x, miDisparo.y, miDisparo.imagen.getWidth(null), miDisparo.imagen.getHeight(null));
        
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (listaMarcianos[i][j].vivo) {
                    rectanguloMarciano.setFrame(listaMarcianos[i][j].x,
                            listaMarcianos[i][j].y,
                            listaMarcianos[i][j].imagen1.getWidth(null),
                            listaMarcianos[i][j].imagen1.getHeight(null));
                    if (rectanguloDisparo.intersects(rectanguloMarciano)) {
                        miExplosion = new Explosion();
                        miExplosion.setX(listaMarcianos[i][j].x);
                        miExplosion.setY(listaMarcianos[i][j].y);
                        miExplosion.imagenExplosion = imagenes[5][3];
                        listaMarcianos[i][j].vivo = false;
                        miDisparo.posicionaDisparo(miNave);
                        miDisparo.y = 1000;
                        reproduce("/sonidos/invaderkilled.wav");
                        miDisparo.disparado = false;
                        contadorMarcianos--;
                     
                    }
                }
            }
        }
       

    }
   // private void aumentaVelocidad() {
       
                    
                 //   listaMarcianos.setvX(2);

                           // }           
    
//                if (contadorMarcianos < 30) {
//                    listaMarcianos[i][j].setvX(listaMarcianos[i][j].getvX() + 1);
//                    listaMarcianos[i][j].setvX(listaMarcianos[i][j].getvX()*-1 + 1);
//                }
//                if (contadorMarcianos < 15) {
//                    listaMarcianos[i][j].setvX(listaMarcianos[i][j].getvX() + 3/2);
//                    listaMarcianos[i][j].setvX(listaMarcianos[i][j].getvX()*-1 + 3/2);
//                }
//        }
//    }
        
    
   
    private void cambiaDireccionMarcianos() {
        for (int i = 0; i < filas; i++) {

            for (int j = 0; j < columnas; j++) {
                if(contadorMarcianos >=75){
                listaMarcianos[i][j].setvX(listaMarcianos[i][j].getvX()*-1);
                listaMarcianos[i][j].y += 20;
                }else
                if(contadorMarcianos <75){
                    listaMarcianos[i][j].setvX1(listaMarcianos[i][j].getvX1()*-1);
                listaMarcianos[i][j].y += 20;
                }
                
            }
        }
    }
     private void reproduce (String cancion){
           try {
               if(dejaDeSonar){
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream( getClass().getResource(cancion) ));
            clip.loop(0);
               }else if(!dejaDeSonar){
               Clip clip = AudioSystem.getClip();
            clip.stop();
            clip.loop(0);
               
               }
        } catch (Exception e) {      
        } 
   }
     
    private void pintaExplosiones( Graphics2D g2){
       
            //miExplosion.imagenExplosion = imagenes[5][3];
            miExplosion.setDuracionVivo(miExplosion.getDuracionVivo()- 1);
            if (miExplosion.getDuracionVivo()> 10){
                g2.drawImage(miExplosion.imagenExplosion, miExplosion.getX(), miExplosion.getY(), null);
            }
           
            
             //si el tiempo de vida de la explosión es menor que 0 la elimino
            if (miExplosion.getDuracionVivo()<= 0){
                g2.drawImage(miExplosion.imagenExplosion, miExplosion.getX() + 1000, miExplosion.getY() + 1000, null);
            }
            
        
    }
 
    

    private void pintaMarcianos(Graphics2D _g2) {
        int anchoMarciano = listaMarcianos[0][0].imagen1.getWidth(null);
        
        
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (listaMarcianos[i][j].vivo) {
                
                if(contadorMarcianos >=75){
                listaMarcianos[i][j].mueve();
                }
                
                if(contadorMarcianos <75){
                listaMarcianos[i][j].aumentaVelocidad();
               }
                
                //chequeo si el marciano ha chocado contra la pared para cambiar
                //la direccion de todos los marcianos
                if (listaMarcianos[i][j].x + anchoMarciano == ANCHOPANTALLA || listaMarcianos[i][j].x == 0) {
                    direccionMarcianos = true;
                 
                }
                              
                if (contador < 50) {
                    
                    _g2.drawImage(listaMarcianos[i][j].imagen1,
                            listaMarcianos[i][j].x,
                            listaMarcianos[i][j].y,
                            null);
                  
                } else if (contador < 100) {
                    
                    _g2.drawImage(listaMarcianos[i][j].imagen2,
                            listaMarcianos[i][j].x,
                            listaMarcianos[i][j].y,
                            null);
                  
                } else {
                    contador = 0;
                }
                
                }
            
        }
        }
        
                 
        
        if (direccionMarcianos) {
            cambiaDireccionMarcianos();
            direccionMarcianos = false;
           
        } 
        
        
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 450, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                miNave.setPulsadoIzquierda(true);
                break;
            case KeyEvent.VK_RIGHT:
                miNave.setPulsadoDerecha(true);
                break;
            case KeyEvent.VK_SPACE:
                reproduce("/sonidos/shoot.wav");
                miDisparo.posicionaDisparo(miNave);
                miDisparo.disparado = true;
                break;
            case KeyEvent.VK_ENTER:
                comienzaElJuego = true;
        }
    }//GEN-LAST:event_formKeyPressed

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        switch (evt.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                miNave.setPulsadoIzquierda(false);
                break;
            case KeyEvent.VK_RIGHT:
                miNave.setPulsadoDerecha(false);
                break;

        }
    }//GEN-LAST:event_formKeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaJuego.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaJuego().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
