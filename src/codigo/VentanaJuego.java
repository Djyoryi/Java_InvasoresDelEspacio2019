/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.Timer;

/**
 *
 * @author Fernando de Castro
 */
public class VentanaJuego extends javax.swing.JFrame {

    //para que una constante no cambie durante el juego
    static int ANCHOPANTALLA = 600;
    static int ALTOPANTALLA = 450;
    //declaramos variables

    //numero de marcianos que van a aparecer
    int filas = 5;
    int columnas = 10;
    //el buffer es un trozo de memoria y cuando estamos trabajan con cualquier 
    //juego, yo tengo una pantalla que van pasando cosas que las naves se mueves x ej.

    BufferedImage buffer = null;

    //creo un objeto de tipo nave
    Nave miNave = new Nave();
    Disparo miDisparo = new Disparo();

    //Marciano miMarciano = new Marciano();
    Marciano[][] listaMarcianos = new Marciano[filas][columnas];
    boolean direccionMarcianos = false;
    //el contador me sirve para decidir que imagen de marciano toca poner
    int contador = 0;

    //el nuevo hilo de ejecucion es una nueva variable de tipo timer
    //cada 10 milisegundos va a llamar al codigo que esta ahi dentro
    Timer temporizador = new Timer(10, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            bucleDelJuego();
        }
    });

    /**
     * Creates new form VentanaJuego
     */
    public VentanaJuego() {
        initComponents();
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

        //inicializo la posicion inicial de la nave
        miNave.x = ANCHOPANTALLA / 2 - miNave.imagen.getWidth(this) / 2;
        miNave.y = ALTOPANTALLA - miNave.imagen.getHeight(this) - 40;

        //inicializo el array de marcianos
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                listaMarcianos[i][j] = new Marciano();
                listaMarcianos[i][j].x = j * (15 + listaMarcianos[i][j].imagen1.getWidth(null));
                listaMarcianos[i][j].y = i * (10 + listaMarcianos[i][j].imagen1.getHeight(null));
            }
        }
       
    }

    private void bucleDelJuego() {
        //se encarga del redibujado de los objetos en el jpanel1
        //primero borramos todo lo que hay en el buffer
        contador++;
        //le digo a g2 que apunte a la zona del buffer
        Graphics2D g2 = (Graphics2D) buffer.getGraphics();
        //g2 todo lo que dibujes va a ser en color negro
        g2.setColor(Color.black);
        //y ahora borro
        g2.fillRect(0, 0, ANCHOPANTALLA, ALTOPANTALLA);

        ////////////////////////////////////////////////////////////////////
        //redibujamos aqui cada elemento
        g2.drawImage(miDisparo.imagen, miDisparo.x, miDisparo.y, null);
        g2.drawImage(miNave.imagen, miNave.x, miNave.y, null);
        pintaMarcianos(g2);
        chequeaColision();
        miNave.mueve();
        miDisparo.mueve();

        ////////////////////////////////////////////////////////////////////
        //************* fase final, se dibuja el buffer ******************//
        //************* de golpe sobre el jpanel1 **********************//
        //convertir de una clase a otra
        g2 = (Graphics2D) jPanel1.getGraphics();
        //(lo que va a dibujar , donde, donde ,null(que se pone siempre))
        //en realidad
        g2.drawImage(buffer, 0, 0, null);
    }
    //lo que vamos hacer es que si el rectangulo del marciano y el rectangulo 
    //del disparo comparten pixeles en comun devuelva true
    private void chequeaColision() {
        Rectangle2D.Double rectanguloMarciano = new Rectangle2D.Double();
        Rectangle2D.Double rectanguloDisparo = new Rectangle2D.Double();

        rectanguloDisparo.setFrame(miDisparo.x, miDisparo.y, miDisparo.imagen.getWidth(null), miDisparo.imagen.getHeight(null));
        for (int i = 0; i < filas; i++) {

            for (int j = 0; j < columnas; j++) {
                rectanguloMarciano.setFrame(listaMarcianos[i][j].x, 
                                            listaMarcianos[i][j].y,
                                            listaMarcianos[i][j].imagen1.getWidth(null),
                                            listaMarcianos[i][j].imagen1.getHeight(null));
                if (rectanguloDisparo.intersects(rectanguloMarciano)){
                    listaMarcianos[i][j].y = 2000;
                    miDisparo.posicionaDisparo(miNave);
                    miDisparo.y = 1000;
                    miDisparo.disparado = false;
                }
            }
        }

    }

    private void cambiaDireccionMarcianos() {
        for (int i = 0; i < filas; i++) {

            for (int j = 0; j < columnas; j++) {
                listaMarcianos[i][j].setvX(listaMarcianos[i][j].getvX()*-1);
                
            }
        }
    }

    private void pintaMarcianos(Graphics2D _g2) {
        int anchoMarciano = listaMarcianos[0][0].imagen1.getWidth(null);
        for (int i = 0; i < filas; i++) {
            
            for (int j = 0; j < columnas; j++) {
                listaMarcianos[i][j].mueve();
                //chequeo si el marciano ha chocado contra la pared para cambiar
                //la direccion de todos los marcianos
                if (listaMarcianos[i][j].x + anchoMarciano == ANCHOPANTALLA || listaMarcianos[i][j].x == 0){
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
                }
                else contador = 0;
            }
            }
             if (direccionMarcianos){
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
                miDisparo.posicionaDisparo(miNave);
                miDisparo.disparado = true;
                break;
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
