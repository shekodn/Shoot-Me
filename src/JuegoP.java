

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.LinkedList;
import javax.swing.JFrame;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Juego1
 *
 * El jugador debe mover el vaso de cafe con el mouse y atrapar los granos de
 * cafe que van cayendo del cielo. El Juego maneja: puntaje y controlador de
 * vidas.
 *
 * @authors Sergio Diaz A01192313, Ana Karen Beltran A01192508
 * @version 1
 * @date 16/feb/2016
 */
public class JuegoP extends JFrame implements Runnable, KeyListener {

    /*OBJETOS*/
    private Base basPrincipal;         // Objeto principal
    /*Lista de los malitos*/
    private LinkedList<Base> lklMalitos; //Objetos malos (salen de la derecha)
    /*Lista de los buenos*/
    private LinkedList<Base> lklBuenitos; //Objetos que buenos (salen izq)

    /*IMAGENES*/
    private Image imaImagenFondo;        // para dibujar la imagen de fondo
    private Image imaImagenGameOver; //para dibujar cuando se acabe el juego
    private Image imaImagenMalo; //para dibujar el objeto malo
    private Image imaImagenBueno; //para dibujar el objeto bueno
    /* objetos para manejar el buffer del Applet y que la imagen no parpadee */
    private Image imaImagenApplet;   // Imagen a proyectar en Applet (principal)
    private Graphics graGraficaApplet;  // Objeto grafico de la Imagen

    /*URLS*/
    private URL urlImagenPrincipal;
    private URL urlImagenFondo;
    private URL urlImagenGameOver;
    private URL urlImagenMalo;
    private URL urlImagenBueno;

    /*AUDIOS*/
    private SoundClip fall; // Audio al haber colisión con el suelo
    private SoundClip bump;  //Audio al hacer colison entre objetos

    /*ENTEROS*/
    private int iVelocidad; // Lleva la velocidad acumulada del objeto principal
    private int iVidas; //Lleva la cuenta de las vidas (empieza en 5)
    private int iPuntos;
    private int iDireccion;
    private int iContMalo; //Lleva la cuenta de cuantos malos han colisionado
    int iRandomMalos; //indica el # de malos a crear
    int iRandomBuenos; //indica el # de buenos a crear
    
    private long tiempoActual;	//Tiempo de control de la animación

   
    /*VECTORES*/
    private Vector vec;    // Objeto vector para agregar el puntaje.

    /*STRINGS */
    private String nombreArchivo;    //Nombre del archivo.
    private String[] arr;    //Arreglo del archivo divido.

    /*BOOLEANOS*/
    private boolean bPressed; //Indica si una tecla esta siendo presionada
    private boolean pause;    //Boleano para pausar el juego.

    /**
     * init
     *
     * Metodo sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se inizializan las variables o se crean los objetos a
     * usarse en el <code>Applet</code> y se definen funcionalidades.
     *
     */
    public JuegoP() {
        fall = new SoundClip("beep1.wav");
        bump = new SoundClip("gunshot3.wav");
        
        initVars();
        creaImagenes();
        creaObjetos();
        posicionaPers();

        // Se va a oír el teclado (usar)
        addKeyListener(this);
        //Escojo color y tamaño
        setFont(new Font("Helvetica", Font.BOLD, 15));

        
         // Declaras un hilo
        Thread th = new Thread(this);
        // Empieza el hilo
        th.start();
    }

    /**
     */
    public void initVars() {
        lklMalitos = new LinkedList<Base>(); //Creo lista de malitos
        lklBuenitos = new LinkedList<Base>(); //Creo lista de buenitos

        /* genero el random de los malitos entre 8 y 10*/
        iRandomMalos = (int) (Math.random() * 4) + 8;
        /* genero el random de los buenitos entre 10 y 15*/
        iRandomBuenos = (int) (Math.random() * 7) + 10;

        iVelocidad = 2; //Inicializo velocidad inicial
        iPuntos = 0; //Inicializar los puntos
        iVidas = (int) (Math.random() * 4) + 3; //Inicializo el valor de las vidas
        iContMalo = 0; //Inicializo contaor

        nombreArchivo = "Puntaje.txt";//nombre del archivo
        vec = new Vector();//crea vector de puntos

        bPressed = false; //inicializa el booleano de pressed
    }

    public void creaImagenes() {
        // Defino la imagen principal
        urlImagenPrincipal = this.getClass().getResource("girl.png");
        imaImagenApplet = Toolkit.getDefaultToolkit().getImage(urlImagenPrincipal);

        // Creo la imagen de fondo
        urlImagenFondo = this.getClass().getResource("field.jpg");
        imaImagenFondo = Toolkit.getDefaultToolkit().getImage(urlImagenFondo);

        // Creo la imagen del fin de juego
        urlImagenGameOver = this.getClass().getResource("gameOver1.jpg");
        imaImagenGameOver = Toolkit.getDefaultToolkit().getImage(urlImagenGameOver);

        //Creo la imagen del malo
        urlImagenMalo = this.getClass().getResource("bad.png");
        imaImagenMalo = Toolkit.getDefaultToolkit().getImage(urlImagenMalo);

        //Creo la imagen del bueno
        urlImagenBueno = this.getClass().getResource("good.png");
        imaImagenBueno = Toolkit.getDefaultToolkit().getImage(urlImagenBueno);

    }

    public void creaObjetos() {
        // Creo el objeto para principal
        basPrincipal = new Base(0, 0, Toolkit.getDefaultToolkit().getImage(urlImagenPrincipal));

        //Creo el objeto para el malo
        for (int iI = 0; iI < iRandomMalos; iI++) {
            //creo a un malito
            Base basMalo = new Base(0, 0, Toolkit.getDefaultToolkit().getImage(urlImagenMalo));
            //añado un elemento-malito a la lista 
            lklMalitos.add(basMalo);
        }
        //Creo el objeto para el bueno
        for (int iI = 0; iI < iRandomBuenos; iI++) {
            //creo a un bueno
            Base basBueno = new Base(0, 0, Toolkit.getDefaultToolkit().getImage(urlImagenBueno));
            //añado un elemento-bueno a la lista 
            lklBuenitos.add(basBueno);
        }
    }

    public void posicionaPers() {
        // Se posiciona a principal en el cuarto cuadrante 
        basPrincipal.setX(getWidth() / 2 - basPrincipal.getAncho() / 2);
        basPrincipal.setY(getHeight() / 2 - basPrincipal.getAlto() / 2);

        //Se posiciona a los objetos malos, en derecha y fuera del applet
        for (Base basMalo : lklMalitos) {
            reposicionaMalo(basMalo);
        }
        //Se posiciona a los objetos buenos, en izquierda y fuera del applet
        for (Base basBueno : lklBuenitos) {
            reposicionaBueno(basBueno);
        }
    }

    /**
     * reposicionaMalo
     *
     * Metodo que reposiciona a un elmento de la lista de objetos que caen Este
     * metodo se llama en <code>checaColision</code> cada que un objeto cae al
     * suelo o colisiona con el principal
     *
     * @param basMalo , es el objeto de la lista que necesita ser reposicionado
     *
     */
    public void reposicionaMalo(Base basMalo) {
        //reposiciona a un elemento en especifico
        basMalo.setY((int) (Math.random() * (getHeight() + (basMalo.getAlto() + 60))));
        basMalo.setX((int) (Math.random() * 201 + getWidth()));

    }

    /**
     * reposicionaBueno
     *
     * Metodo que reposiciona a un elmento de la lista de objetos que caen Este
     * metodo se llama en <code>checaColision</code> cada que un objeto cae al
     * suelo o colisiona con el principal
     *
     * @param basBueno , es el objeto de la lista que necesita ser reposicionado
     *
     */
    public void reposicionaBueno(Base basBueno) {
        //reposiciona a un elemento en especifico
        basBueno.setY((int) (Math.random() * ((getHeight() - basBueno.getAlto()) + (basBueno.getAlto() + 60))));
        basBueno.setX((int) (Math.random() * (201 - basBueno.getAncho()) - 200));
    }

    /**
     * run
     *
     * Método sobrescrito de la clase <code>Thread</code>.<P>
     * En este método se ejecuta el hilo, que contendrá las instrucciones de
     * nuestro juego.
     *
     */
    public void run() {
        /* mientras dure el juego, se actualizan posiciones de jugadores
           se checa si hubo colisiones para desaparecer jugadores o corregir
           movimientos y se vuelve a pintar todo
         */
        while (iVidas != 0) {

            actualiza();
            checaColision();
            repaint();
            try {
                // El hilo del juego se duerme. 
                Thread.sleep(20);
            } catch (InterruptedException iexError) {
                System.out.println("Hubo un error en el juego " + iexError.
                        toString());
            }
        }
    }

    /**
     * actualiza
     *
     * Metodo que actualiza la posiciÓn de los objetos
     *
     */
    public void actualiza() {
        actualizaPrincipal();
        actualizaListas();
    }

    public void actualizaPrincipal() {
        //Si la tecla esta siendo presionada, cambiar su posicion del principal
        if (bPressed) {
            if (iDireccion == 1) {
                basPrincipal.setX(basPrincipal.getX() - 1);
                basPrincipal.setY(basPrincipal.getY() - 1);
            } else if (iDireccion == 2) {
                basPrincipal.setX(basPrincipal.getX() + 1);
                basPrincipal.setY(basPrincipal.getY() - 1);
            } else if (iDireccion == 3) {
                basPrincipal.setX(basPrincipal.getX() - 1);
                basPrincipal.setY(basPrincipal.getY() + 1);
            } else if (iDireccion == 4) {
                basPrincipal.setX(basPrincipal.getX() + 1);
                basPrincipal.setY(basPrincipal.getY() + 1);
            }
        }
    }

    public void actualizaListas() {
        for (Base basMalo : lklMalitos) { //Mover a cada objeto
            //El malo se mueve a la izq
            int iPixeles = (int) (Math.random() * 4) + 3; //3-5
            basMalo.setX(basMalo.getX() - iPixeles);
        }
        for (Base basBueno : lklBuenitos) { //Mover a cada objeto
            //El bueno se mueve a la derecha
            int iPixeles = (int) (Math.random() * 4) + 1; //1-3
            basBueno.setX(basBueno.getX() + iPixeles);
        }
    }

    /**
     * checaColision
     *
     * Método usado para checar la colisión entre objetos
     *
     */
    public void checaColision() {
        chechaColisionMalos();
        checaColisionBuenos();
        checaColisionPrincipal();
    }

    /**
     * chechaColisionMalos
     *
     * checa colision entre malos y principal, checa la colision de malos y el
     * applet
     */
    public void chechaColisionMalos() {
        /*FOR PARA CHECAR COLISION ENTRE MALO Y PRINCIPAL*/
        for (Base basMalo : lklMalitos) {
            //Intersecta esta en la clase Base
            if (basPrincipal.intersecta(basMalo)) {
                bump.play(); //sonido al colisionar
                reposicionaMalo(basMalo); //cambia posicion del que colisiona
                iContMalo++;
                //checar si se debe quitar vida
                if (iContMalo >= 5) {//cada 5 colisiones quita 1 vida
                    iVidas--; //quito vida
                    iContMalo = 0; //reinicio contador a 0
                }
            }
        }
        /*CHECA LA COLISION DE LOS MALOS Y EL APPLET*/
        for (Base basMalo : lklMalitos) {
            //Si los malos llegan al otro lado then..
            if (basMalo.getX() < 0) {
                reposicionaMalo(basMalo); //cambia la posicion del elemento
            }
        }
    }

    public void checaColisionBuenos() {
        /*CHECA COLISION ENTRE EL BUENO Y EL PRINCIPAL*/
        for (Base basBueno : lklBuenitos) {
            if (basPrincipal.intersecta(basBueno)) {//checa colision
                fall.play();//sonido al colisionar
                iPuntos = iPuntos + 10;//aumentar score
                reposicionaBueno(basBueno);//cambia posicion del que colisiono
            }
        }
        /*CHECA LA COLISION DE LOS BUENOS Y EL APPLET*/
        for (Base basBueno : lklBuenitos) {
            //Si los malos llegan al otro lado then..

            if ((basBueno.getX() + basBueno.getAncho()) > getWidth()) {
                reposicionaBueno(basBueno); //cambia la posicion del elemento
            }
        }
    }

    public void checaColisionPrincipal() {
        /*AQUI CHECAR LA COLISION DE LAS PAREDES PARA OBJ PRINCIPAL*/
        //revisa al subir
        if (basPrincipal.getY() < 0) {
            basPrincipal.setY(0);
            basPrincipal.setX(basPrincipal.getX());
        } //revisa al bajar
        else if ((basPrincipal.getY() + basPrincipal.getAlto()) > getHeight()) {
            basPrincipal.setY(getHeight() - basPrincipal.getAlto());
            basPrincipal.setX(basPrincipal.getX());
        } //revisa a la izq
        else if (basPrincipal.getX() < 0) {
            basPrincipal.setX(0);
            basPrincipal.setY(basPrincipal.getY());
        } //revisa a la derecha
        else if ((basPrincipal.getX() + basPrincipal.getAncho()) > getWidth()) {
            basPrincipal.setX(getWidth() - basPrincipal.getAncho());
            basPrincipal.setY(basPrincipal.getY());
        }
    }

    /**
     * paint
     *
     * Método sobrescrito de la clase <code>Applet</code>, heredado de la clase
     * Container.<P>
     * En este método lo que hace es actualizar el contenedor y define cuando
     * usar ahora el paint1
     *
     * @param graGrafico es el <code>objeto grafico</code> usado para dibujar.
     *
     */
    public void paint(Graphics graGrafico) {
        // Inicializan el DoubleBuffer
        if (imaImagenApplet == null) {
            imaImagenApplet = createImage(this.getSize().width,
                    this.getSize().height);
            graGraficaApplet = imaImagenApplet.getGraphics();
        }

        // Actualiza la imagen de fondo.
        URL urlImagenFondo = this.getClass().getResource("field.jpg");
        imaImagenFondo = Toolkit.getDefaultToolkit().getImage(urlImagenFondo);
        graGrafico.drawImage(imaImagenFondo, 0, 0, getWidth(), getHeight(),
                this);

        // Actualiza el Foreground.
        graGrafico.setColor(getForeground());
        
        paint1(graGrafico);
    }

    /**
     * paint1
     *
     * Metodo sobrescrito de la clase <code>Applet</code>, heredado de la clase
     * Container.<P>
     * En este metodo se dibuja la imagen con la posicion actualizada, ademas
     * que cuando la imagen es cargada te despliega una advertencia.
     *
     * @param graDibujo es el objeto de <code>Graphics</code> usado para
     * dibujar.
     *
     */
    public void paint1(Graphics graDibujo) {
        /*IMAGENES*/
        // si la imagen ya se cargo
        if (basPrincipal != null && imaImagenFondo != null && lklMalitos != null
                && lklBuenitos != null) {
            // Dibuja la imagen de fondo
            graDibujo.drawImage(imaImagenFondo, 0, 0, getWidth(), getHeight(),
                    this);

            //Dibuja la imagen de principal en el Applet
            basPrincipal.paint(graDibujo, this);

            //Dibujan los objetos de los personajes malos
            for (Base basMalo : lklMalitos) {
                basMalo.paint(graDibujo, this);
            }
            //Dibujan los objetos de los personajes buenos
            for (Base basBueno : lklBuenitos) {
                basBueno.paint(graDibujo, this);
            }

        } // sino se ha cargado se dibuja un mensaje 
        else {
            //Da un mensaje mientras se carga el dibujo	
            graDibujo.drawString("No se cargo la imagen..", 20, 20);
        }

        /*PUNTAJE Y VIDAS*/
        graDibujo.setColor(Color.white);
        graDibujo.drawString("Vidas:" + iVidas, 20, 45);
        graDibujo.drawString("Score:" + iPuntos, 20, 60);

        //Dibuja imagen de fin de juego cuando se acaban las vidas
        if (iVidas == 0) {
            graDibujo.drawImage(imaImagenGameOver, 0, 0, getWidth(),
                    getHeight(), this);
        }
    }

    public void keyTyped(KeyEvent ke) {
    }

    public void keyPressed(KeyEvent keyEvent) {
        /*
        Q(1)   P(2)
        A(3)   L(4)
         */
        if (keyEvent.getKeyCode() == KeyEvent.VK_Q) {//1
            iDireccion = 1;//arriba-izq
            bPressed = true; //prendo booleana de teclas
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_P) {//2
            iDireccion = 2;//arriba-derecha
            bPressed = true; //prendo booleana de teclas
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_A) {//3
            iDireccion = 3;//abajo-izquierda
            bPressed = true; //prendo booleana de teclas
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_L) {//4
            iDireccion = 4;//abajo-derecha
            bPressed = true; //prendo booleana de teclas
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_G) {
            try {
                grabaArchivo();
            } catch (IOException ex) {
                System.out.println("Error en " + ex.toString());
            }
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_C) {
            try {
                leeArchivo();
            } catch (IOException ex) {
                System.out.println("Error en " + ex.toString());
            }
        }
    }

    public void keyReleased(KeyEvent ke) {
        bPressed = false; //apago booleana de teclas
    }

    public int getHeight() {
        return 500;
    }

    public int getWidth() {
        return 800;
    }

    /**
     * Metodo que lee a informacion de un archivo y lo agrega a un vector.
     *
     * @throws IOException
     */
    public void leeArchivo() throws IOException {
        BufferedReader finArchivo;
        try {
            finArchivo = new BufferedReader(new FileReader(nombreArchivo));
        } catch (FileNotFoundException e) {
//    		File puntos = new File(nombreArchivo);
//    		PrintWriter fileOut = new PrintWriter(puntos);
//    		fileOut.println("100");
//                fileOut.println("200");
//    		fileOut.close();

        }
        finArchivo = new BufferedReader(new FileReader(nombreArchivo));
        //leeo vidads
        String sLinea = finArchivo.readLine();
        iVidas = Integer.parseInt(sLinea);
        //leo puntaje
        sLinea = finArchivo.readLine();
        iPuntos = Integer.parseInt(sLinea);
        //leo posicion de prnicipal
        sLinea = finArchivo.readLine();
        basPrincipal.setX(Integer.parseInt(sLinea));
        sLinea = finArchivo.readLine();
        basPrincipal.setY(Integer.parseInt(sLinea));
        
        sLinea = finArchivo.readLine();
        iRandomBuenos = Integer.parseInt(sLinea);
        
        lklBuenitos = new LinkedList<Base>(); //Creo lista de buenitos
        //Creo el objeto para el bueno
        for (int iI = 0; iI < iRandomBuenos; iI++) {
            //creo a un bueno
            Base basBueno = new Base(0, 0, Toolkit.getDefaultToolkit().
                    getImage(urlImagenBueno));
            //añado un elemento-bueno a la lista 
            lklBuenitos.add(basBueno);
            
            sLinea = finArchivo.readLine();
            basBueno.setX(Integer.parseInt(sLinea));
            sLinea = finArchivo.readLine();
            basBueno.setY(Integer.parseInt(sLinea));

        }
        
        sLinea = finArchivo.readLine();
        iRandomMalos = Integer.parseInt(sLinea);
        
         lklMalitos = new LinkedList<Base>(); //Creo lista de buenitos
        //Creo el objeto para el bueno
        for (int iI = 0; iI < iRandomMalos; iI++) {
            //creo a un bueno
            Base basMalo = new Base(0, 0, Toolkit.getDefaultToolkit().
                    getImage(urlImagenMalo));
            //añado un elemento-bueno a la lista 
            lklMalitos.add(basMalo);
            
            sLinea = finArchivo.readLine();
            basMalo.setX(Integer.parseInt(sLinea));
            sLinea = finArchivo.readLine();
            basMalo.setY(Integer.parseInt(sLinea));

        }
        
        finArchivo.close();

    }

    /**
     * Metodo que agrega la informacion del vector al archivo.
     *
     * @throws IOException
     */
    public void grabaArchivo() throws IOException {
        PrintWriter fpwArchivo = new PrintWriter(new FileWriter(nombreArchivo));

        fpwArchivo.println(iVidas);//vidas
        fpwArchivo.println(iPuntos);//score
        fpwArchivo.println(basPrincipal.getX());//posicion X principal
        fpwArchivo.println(basPrincipal.getY());//posicion Y principal
        fpwArchivo.println(lklBuenitos.size());//cantidad de buenos
        
        for (Base basBueno : lklBuenitos){
            fpwArchivo.println(basBueno.getX());// x de malo
            fpwArchivo.println(basBueno.getY());// y de malo
        }
        
        fpwArchivo.println(lklMalitos.size());//cantidad de malos
        
        for (Base basMalo : lklMalitos){
            fpwArchivo.println(basMalo.getX());// x de malo
            fpwArchivo.println(basMalo.getY());// y de malo
        }

        fpwArchivo.close();
    }

    public static void main(String[] args) {

        JuegoP x = new JuegoP();
        x.setVisible(true);
        x.setSize(800, 500);
        x.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}