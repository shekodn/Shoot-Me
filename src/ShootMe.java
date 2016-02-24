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
 * El jugador debe de mover a la niña, la cual puede disparar. El objetivo del 
 * juego es destruir a todas las manzanas antes de quedarse sin vidas
 *
 * @authors Sergio Diaz A01192313, Ana Karen Beltran A01192508
 * @version 1
 * @date 23/feb/2016
 */
public class ShootMe extends JFrame implements Runnable, KeyListener {

    /*OBJETOS*/
    private Base basPrincipal;         // Objeto principal
    /*Lista de los malitos*/
    private LinkedList<Malo> lklMalos; //Objetos malos (salen de la derecha)
    
    private LinkedList<Bala> lklBalas; // Objeto bala que destruye a malos 
    private LinkedList<Base> lklVidas; //Lista con vidas 
    
    private boolean bDisparo; //bandera para generar una nueva bala
   
    
    /*IMAGENES*/
    private Image imaImagenFondo;        // para dibujar la imagen de fondo
    private Image imaImagenGameOver; //para dibujar cuando se acabe el juego
    private Image imaImagenPausa;
    private Image imaImagenMalo; //para dibujar el objeto malo
    private Image imaImagenBueno; //para dibujar el objeto bueno
    private Image imaImagenBala; //Imagen de una balita
    private Image imaImagenVida; //imagen de la vida
    
    /* objetos para manejar el buffer del Applet y que la imagen no parpadee */
    private Image imaImagenApplet;   // Imagen a proyectar en Applet (principal)
    private Graphics graGraficaApplet;  // Objeto grafico de la Imagen

    
    /*URLS*/
    private URL urlImagenPrincipal;
    private URL urlImagenFondo;
    private URL urlImagenGameOver;
    private URL urlImagenPausa;
    private URL urlImagenMalo;
    private URL urlImagenBueno;
    private URL urlImagenBala;
    private URL urlImagenVida;

    /*AUDIOS*/
    private SoundClip fall; // Audio al haber colisión con el suelo
    private SoundClip bump;  //Audio al hacer colison entre objetos

    /*ENTEROS*/
    private int iVelocidad; // Lleva la velocidad acumulada del objeto principal
    private int iVidas; //Lleva la cuenta de las vidas (empieza en 5)
    private int iPuntos;
    private int iDireccion;
    private int iContMalo; //Lleva la cuenta de cuantos malos han colisionado
    private int iRandomMalos; //indica el # de malos a crear
    private int iRandomBuenos; //indica el # de buenos a crear
    private int iBalas; //Numero de balas
    private int iDireccionBala; //Maraca si la vala sale horizontal o con 
            //inclinacioon
    
   
    private int iPosicionVidas; //Offset de vidas en el applet 

   
    /*VECTORES*/
    private Vector vec;    // Objeto vector para agregar el puntaje.

    /*STRINGS */
    private String nombreArchivo;    //Nombre del archivo.
    private String[] arr;    //Arreglo del archivo divido.

    /*BOOLEANOS*/
    private boolean bPressed; //Indica si una tecla esta siendo presionada
    private boolean bPause;    //Boleano para pausar el juego.

    /**
     * init
     *
     * Metodo sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se inizializan las variables o se crean los objetos a
     * usarse en el <code>Applet</code> y se definen funcionalidades.
     *
     */
    public ShootMe() {

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
        lklMalos = new LinkedList<Malo>(); //Creo lista de malitos
        lklBalas = new LinkedList<Bala>(); //Creo la lista de balas
        lklVidas = new LinkedList<Base>(); //se crean vidas
      
        bPause = false; //Juego comienza sin pausa 
        
        iBalas = 0; //se inicializa el cartucho con la bala de la posicion 0
        
        iPosicionVidas = 0;

        /* genero el random de los malitos entre 8 y 10*/
        iRandomMalos = (int) (Math.random() * 4) + 8;
       
        iDireccionBala = 12; //se iniciliza la bala apuntando arriba
        
        bDisparo = false; //bandera que controla el disparo

        iVelocidad = 2; //Inicializo velocidad inicial
        iPuntos = 0; //Inicializar los puntos
        iVidas = 5;
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
        
        // Creo la imagen de la pausa de juego
        urlImagenPausa = this.getClass().getResource("GamePause.jpg");
        imaImagenPausa = Toolkit.getDefaultToolkit().getImage(urlImagenPausa);

        //Creo la imagen del malo
        urlImagenMalo = this.getClass().getResource("bad.png");
        imaImagenMalo = Toolkit.getDefaultToolkit().getImage(urlImagenMalo);

        //Creo la imagen del bueno
        urlImagenBueno = this.getClass().getResource("good.png");
        imaImagenBueno = Toolkit.getDefaultToolkit().getImage(urlImagenBueno);
        
        //Creo la imagen de la bala 
        urlImagenBala = this.getClass().getResource("bala.png");
        imaImagenBueno = Toolkit.getDefaultToolkit().getImage(urlImagenBala);
        
        //Creo la imagen de la vida 
        urlImagenVida = this.getClass().getResource("vidas.png");
        imaImagenVida = Toolkit.getDefaultToolkit().getImage(urlImagenVida);

    }

    public void creaObjetos() {
        // Creo el objeto para principal
        basPrincipal = new Base(0, 0, Toolkit.getDefaultToolkit().getImage(urlImagenPrincipal));
        
        
        //Creo el objeto para las balas 
        for (int iI = 0; iI < 1; iI++) {
            //creo a un malito
            Bala basBala = new Bala ('0', 1, 0, 0, Toolkit.getDefaultToolkit().getImage
                (urlImagenBala));
            
            //añado un elemento de bala a la lista 
            lklBalas.add(basBala);
        }
        
        //Creo el objeto para las vidas 
        for (int iI = 0; iI < iVidas; iI++) {
            //creo a un malito
            Base basVida = new Base(0, 0, Toolkit.getDefaultToolkit().getImage
                (urlImagenVida));
            
            //añado un elemento de bala a la lista 
            lklVidas.add(basVida);
        }
        
        
        //Creo el objeto para el malo
        for (int iI = 0; iI < iRandomMalos; iI++) {
            //creo a un malito
            Malo basMalo = new Malo(0, 0, Toolkit.getDefaultToolkit().getImage(urlImagenMalo));
            //añado un elemento-malito a la lista 
            lklMalos.add(basMalo);
        }
       
    }

    public void posicionaPers() {
        // Se posiciona a principal en el cuarto cuadrante 
        basPrincipal.setX(getWidth() / 2 - basPrincipal.getAncho() / 2);
        basPrincipal.setY(getHeight() - basPrincipal.getAlto());

        //Se posiciona a los objetos malos, en derecha y fuera del applet
        for (Base basMalo : lklMalos) {
            reposicionaMalo(basMalo);
        }
        
        //Se posiciona a los objetos malos, en derecha y fuera del applet
        for (Base basBala : lklBalas) {
           basBala.setX(basPrincipal.getX()-100);
           basBala.setY(basPrincipal.getY() -100);
           
        }
        
        //Se posiciona a los objetos malos, en derecha y fuera del applet
        for (Base basVida : lklVidas) {
           basVida.setX(iPosicionVidas + 10);
           basVida.setY(30);
           
           iPosicionVidas = iPosicionVidas + 45;
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
        basMalo.setX((int) (Math.random() * (getWidth() - basMalo.getAncho())));
        basMalo.setY(((int) (Math.random() * (-1 * getHeight()))));
        //basMalo.setY((int) (Math.random() * (40)-(-1*(getHeight()+90))));
    }
    
    
    
    /**
     * disparo
     *
     * Metodo que reposiciona a un elmento de la lista de objetos denominados 
     * como bala
     *
     * @param basBala , es el objeto de la lista que necesita ser reposicionado
     * 
     *
     */
    public void disparo() {
       
        if (bDisparo) {
            
            Bala basBala = new Bala('0', 1, 0, 0, Toolkit.getDefaultToolkit().getImage(urlImagenBala));
            lklBalas.add(basBala);
            
        }
        
        lklBalas.get(iBalas).avanza();
          
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

            if (!bPause) {
                actualiza();
                checaColision();
                //repaint();
                try {
                    // El hilo del juego se duerme. 
                    Thread.sleep(20);
                } catch (InterruptedException iexError) {
                    System.out.println("Hubo un error en el juego " + iexError.
                            toString());
                }
            }
            repaint();
        }
        
        //gameover
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
        disparo();
        
        
    }

    public void actualizaPrincipal() {
        //Si la tecla esta siendo presionada, cambiar su posicion del principal
        
        if (bPressed) {
            if (iDireccion == 1) {
                basPrincipal.setX(basPrincipal.getX() - 1 * 4);
                
            } else if (iDireccion == 2) {
                basPrincipal.setX(basPrincipal.getX() + 1 * 4);
                           
            } else {
                
                basPrincipal.setX(basPrincipal.getX());
                basPrincipal.setY(basPrincipal.getY());
            }
        }
    }
    
    public void actualizaListas() {
        for (Base basMalo : lklMalos) { //Mover a cada objeto
            //Los malos caen
            int iPixeles = (int) (Math.random() * 4) + 3; //3-5
            basMalo.setY(basMalo.getY() + (1 * iVelocidad));
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
        for (Base basMalo : lklMalos) {
            //Intersecta esta en la clase Base
            if (basPrincipal.intersecta(basMalo)) {
                bump.play(); //sonido al colisionar
                reposicionaMalo(basMalo); //cambia posicion del que colisiona
                iPuntos--;
                iContMalo++;
                
                //checar si se debe quitar vida
                if (iContMalo >= 5) {//cada 5 colisiones quita 1 vida
                    iVidas--; //quito vida
                    lklVidas.removeLast();
                    iVelocidad++;
                    iContMalo = 0; //reinicio contador a 0
                }
            }
        }
        
        //CHECA LA COLISION DE LOS MALOS Y EL APPLET
        for (Base basMalo : lklMalos) {
            //Si los malos llegan al final
            if (basMalo.getY() > 500) {
                reposicionaMalo(basMalo); //cambia la posicion del elemento
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
        
        if (!bPause){
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
        }
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
        if (!bPause && basPrincipal != null && imaImagenFondo != null && 
                lklMalos != null) {
            // Dibuja la imagen de fondo
            graDibujo.drawImage(imaImagenFondo, 0, 0, getWidth(), getHeight(),
                    this);

            //Dibuja la imagen de principal en el Applet
            basPrincipal.paint(graDibujo, this);

            //Dibujan los objetos de los personajes malos
            for (Base basMalo : lklMalos) {
                basMalo.paint(graDibujo, this);
            }
            
            //Dibujan los objetos de los personajes malos
            for (Base basBala : lklBalas) {
                basBala.paint(graDibujo, this);
            }
            
            //Dibuja las vidas
            for (Base basVida : lklVidas) {
                basVida.paint(graDibujo, this);
            }

        } // sino se ha cargado se dibuja un mensaje 
        
        else {
            //Da un mensaje mientras se carga el dibujo	
            graDibujo.drawString("No se cargo la imagen..", 20, 20);
        }
        
        if (bPause){
            
            graDibujo.drawImage(imaImagenPausa, 0, 0, getWidth(),
                    getHeight(), this);
        }

        /*PUNTAJE Y VIDAS*/
        graDibujo.setColor(Color.white);
        graDibujo.drawString("Score:" + iPuntos, getWidth() - 100, 50);

        //Dibuja imagen de fin de juego cuando se acaban las vidas
        if (iVidas == 0) {
            graDibujo.drawImage(imaImagenGameOver, 0, 0, getWidth(),
                    getHeight(), this);
        }
    }

    public void keyTyped(KeyEvent keyEvent) {
         
        
       
        
    }

    public void keyPressed(KeyEvent keyEvent) {
    
        //ifs de disparos
        if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {

            bDisparo = true; //prendo booleana de teclas
            iDireccionBala = 12;

        }

        if (keyEvent.getKeyCode() == KeyEvent.VK_A) {//2

            bDisparo = true; //prendo booleana de teclas
            iDireccionBala = 9;

        }

        if (keyEvent.getKeyCode() == KeyEvent.VK_S) {//2

            bDisparo = true; //prendo booleana de teclas
            iDireccionBala = 3;
        }

        //ifs de opciones de juego 
        if ((keyEvent.getKeyCode() == KeyEvent.VK_P)) {

            bPause = !bPause;
        }

        if (keyEvent.getKeyCode() == KeyEvent.VK_G) {
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

        //if de basPrincipal 
        if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {//1
            iDireccion = 1;//arriba-izq
            bPressed = true; //prendo booleana de teclas

        }
        
        if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {//2
            iDireccion = 2;//arriba-derecha
            bPressed = true; //prendo booleana de teclas

        }
        
    }
    
    public void keyReleased(KeyEvent keyEvent) {
        
        bPressed = false; //apago booleana de teclas
        bDisparo = false;
        if (keyEvent.getKeyCode() == KeyEvent.VK_A || keyEvent.getKeyCode() == KeyEvent.VK_S || keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
            iBalas++;
        }
        
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
        
        sLinea = finArchivo.readLine();
        iRandomMalos = Integer.parseInt(sLinea);
        
         lklMalos = new LinkedList<Malo>(); //Creo lista de malos
        //Creo el objeto para el bueno
        for (int iI = 0; iI < iRandomMalos; iI++) {
            //creo a un bueno
            Malo basMalo = new Malo(0, 0, Toolkit.getDefaultToolkit().
                    getImage(urlImagenMalo));
            //añado un elemento-bueno a la lista 
            lklMalos.add(basMalo);
            
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

        
        fpwArchivo.println(lklMalos.size());//cantidad de malos
        
        for (Base basMalo : lklMalos){
            fpwArchivo.println(basMalo.getX());// x de malo
            fpwArchivo.println(basMalo.getY());// y de malo
        }

        fpwArchivo.close();
    }

    public static void main(String[] args) {

        ShootMe ShootMe = new ShootMe();
        ShootMe.setVisible(true);
        ShootMe.setSize(800, 500);
        ShootMe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}