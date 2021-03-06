
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.LinkedList;
import javax.swing.JFrame;

/**
 * Contra ataque
 *
 * El jugador debe de mover al objeto, la cual puede disparar. El objetivo del
 * juego es destruir a todas los objetos malos que caen del cielo
 * antes de quedarse sin vidas
 *
 * @authors Sergio Diaz A01192313, Ana Karen Beltran A01192508
 * @version 1
 * @date 24/feb/2016
 */
public class ShootMe extends JFrame implements Runnable, KeyListener {

    /*OBJETOS*/
    private Base basPrincipal;         // Objeto principal
    /*Lista de los malitos*/
    private LinkedList<Malo> lklMalos; //Objetos malos (salen de arriba)
    /*Lista de las balas*/
    private LinkedList<Bala> lklBalas; // Objeto bala que destruye a malos 
    /*Lista de las vidas*/
    private LinkedList<Base> lklVidas; //Lista con vidas del personaje

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
    // Audio de colision objetos malos - personaje principal
    private SoundClip soundMalosPrincipal; 
     
    //Audio al hacer colison entre objetos y balas 
    private SoundClip soundDestruyeMalos; 

    /*ENTEROS*/
    private int iVelocidad; // Lleva la velocidad acumulada del objeto principal
    private int iVidas; //Lleva la cuenta de las vidas (empieza en 5)
    private int iPuntos; //Puntos acumulados de; juego
    private int iDireccion;
    private int iContMalo; //Lleva la cuenta de cuantos malos han colisionado
    private int iRandomMalos; //indica el # de malos a crear
    private int iPosicionVidas; //Offset de vidas en el applet 

    /*BOOLEANOS*/
    private boolean bPressed; //Indica si una tecla esta siendo presionada
    private boolean bPause;    //Boleano para pausar el juego.
    private boolean bGameOver; //Indica si ya acabo el juego
    

    /**
     * init
     *
     * Metodo sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se inizializan las variables o se crean los objetos a
     * usarse en el <code>Applet</code> y se definen funcionalidades.
     *
     */
    public ShootMe() {

        soundDestruyeMalos = new SoundClip("audio/gunshot3.wav");
        soundMalosPrincipal = new SoundClip("audio/beep1.wav");

        initVars();
        creaImagenes();
        creaObjetos();
        posicionaPerssonajes();

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
        
        //Listas
        lklMalos = new LinkedList<Malo>(); //Creo lista de malitos
        lklBalas = new LinkedList<Bala>(); //Creo la lista de balas
        lklVidas = new LinkedList<Base>(); //se crean vidas

        /* genero el random de los malitos entre 8 y 10*/
        iRandomMalos = (int) (Math.random() * (6)) + 10;
        iVelocidad = 2; //Inicializo velocidad inicial
        iPuntos = 0; //Inicializar los puntos
        iVidas = 5; //Vidas del personaje
        iContMalo = 0; //Inicializo contaor
        iPosicionVidas = 0; //poscion inicial de vidas dibujadas
        
        //Booleanas
        bPause = false; //Juego comienza sin pausa 
        bGameOver = false;//Indica si el juego esta en gameOver
        bPressed = false; //inicializa el booleano de pressed
    }

    public void creaImagenes() {
        // Defino la imagen principal
        urlImagenPrincipal = this.getClass().getResource("images/girl.png");
        imaImagenApplet = 
                Toolkit.getDefaultToolkit().getImage(urlImagenPrincipal);

        // Creo la imagen de fondo
        urlImagenFondo = this.getClass().getResource("images/field.png");
        imaImagenFondo = Toolkit.getDefaultToolkit().getImage(urlImagenFondo);

        // Creo la imagen del fin de juego
        urlImagenGameOver = this.getClass().getResource("images/GameOver.png");
        imaImagenGameOver = Toolkit.getDefaultToolkit().
                getImage(urlImagenGameOver);

        // Creo la imagen de la pausa de juego
        urlImagenPausa = this.getClass().getResource("images/Pause.png");
        imaImagenPausa = Toolkit.getDefaultToolkit().getImage(urlImagenPausa);

        //Creo la imagen del malo
        urlImagenMalo = this.getClass().getResource("images/malos.gif");
        imaImagenMalo = Toolkit.getDefaultToolkit().getImage(urlImagenMalo);

        //Creo la imagen de la bala 
        urlImagenBala = this.getClass().getResource("images/bala.gif");
        imaImagenBueno = Toolkit.getDefaultToolkit().getImage(urlImagenBala);

        //Creo la imagen de la vida 
        urlImagenVida = this.getClass().getResource("images/vidas.png");
        imaImagenVida = Toolkit.getDefaultToolkit().getImage(urlImagenVida);

    }

    public void creaObjetos() {
        // Creo el objeto para principal
        basPrincipal = new Base(0, 0, 
                Toolkit.getDefaultToolkit().getImage(urlImagenPrincipal));

        //Creo el objeto para las vidas 
        for (int iI = 0; iI < iVidas; iI++) {
            //creo a un malito
            Base basVida = new Base(0, 0, Toolkit.getDefaultToolkit().
                    getImage(urlImagenVida));

            //añado un elemento de bala a la lista 
            lklVidas.add(basVida);
        }

        //Creo el objeto para el malo
        if (iRandomMalos < 15) {

            for (int iI = 0; iI < 1; iI++) {
                //creo a un malito
                Malo basMalo = new Malo('s', 0, 0, Toolkit.getDefaultToolkit().
                        getImage(urlImagenMalo));
                //añado un elemento-malito a la lista 
                lklMalos.add(basMalo);
            }

        } else {

            for (int iI = 0; iI < 2; iI++) {
                //creo a un malito
                Malo basMalo = new Malo('s', 0, 0, Toolkit.getDefaultToolkit().
                        getImage(urlImagenMalo));
                //añado un elemento-malito a la lista 
                lklMalos.add(basMalo);
            }

        }

        for (int iI = 0; iI < iRandomMalos; iI++) {
            //creo a un malito
            Malo basMalo = new Malo('n', 0, 0, Toolkit.getDefaultToolkit().
                    getImage(urlImagenMalo));
            //añado un elemento-malito a la lista 
            lklMalos.add(basMalo);
        }

    }
    
   /**
     * reposicionaPersonajes
     *
     * Metodo que reposiciona a un elmento de la lista de objetos que caen Este
     * metodo se llama en <code>checaColision</code> cada que un objeto cae al
     * suelo o colisiona con el principal
     *
     *
     */
    public void posicionaPerssonajes() {
        // Se posiciona a principal en el cuarto cuadrante 
        basPrincipal.setX(getWidth() / 2 - basPrincipal.getAncho() / 2);
        basPrincipal.setY(getHeight() - basPrincipal.getAlto());

        //Se posiciona a los objetos malos, en derecha y fuera del applet
        for (Base basMalo : lklMalos) {
            reposicionaMalo(basMalo);
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
        basMalo.setY(((int)(Math.random()* (-2 * getHeight())))); 
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
        while (true) {
            if (!bPause && !bGameOver) {
                actualiza();
                checaColision();
                try {
                    // El hilo del juego se duerme. 
                    Thread.sleep(20);
                } catch (InterruptedException iexError) {
                    System.out.println("Hubo un error en el juego " + iexError.
                            toString());
                }
            }

            if (!bGameOver) {
                repaint();
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
        actualizaBalas();

    }

    /**
     * actualizaPrincipal
     *
     * Método usado actualizar al principal Se manda llamar en actualiza
     *
     */
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

    /**
     * actualizaListas
     *
     * Método usado actualizar la lista de malos Se manda llamr en actualiza
     *
     */
    public void actualizaListas() {

        for (Malo basMalo : lklMalos) { //Mover a cada objeto

            if (basMalo.getTipo() == 'n' || basMalo.getTipo() == 'h') {
                //Los malos caen
                int iPixeles = (int) (Math.random() * 4) + 3; //3-5
                basMalo.setY(basMalo.getY() + (1 * iVelocidad));

            } else {

                if (basPrincipal.getX() < basMalo.getX()) {
                    basMalo.setX(basMalo.getX() - iVelocidad);
                } else if (basPrincipal.getX() > basMalo.getX()) {
                    basMalo.setX(basMalo.getX() + iVelocidad);
                }

                if (basPrincipal.getY() < basMalo.getY()) {
                    basMalo.setY(basMalo.getY() - iVelocidad);
                } else if (basPrincipal.getY() > basMalo.getY()) {
                    basMalo.setY(basMalo.getY() + iVelocidad);
                }

                if (basMalo.getY() + basMalo.getAlto() >= getHeight() 
                        - basPrincipal.getAlto()) {

                    basMalo.setTipo('h');
                }
            }
        }
    }

    /**
     * actualizaBalas
     *
     * Metodo que actualiza la lista de balas, manda a llamar a avanza en Bala
     * Se manda a llamar en actualiza
     *
     */
    public void actualizaBalas() {

        for (Bala basBala : lklBalas) {
            basBala.avanza();
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
        chechaColisionBalas();
    }

    /**
     * chechaColisionMalos
     *
     * checa colision entre malos y principal, checa la colision de malos y el
     * applet Se manda llamar en checaColision
     */
    public void chechaColisionMalos() {
        /*FOR PARA CHECAR COLISION ENTRE MALO Y PRINCIPAL*/
        for (Malo basMalo : lklMalos) {
            //Intersecta esta en la clase Base
            if (basPrincipal.intersecta(basMalo)) {
                soundDestruyeMalos.play(); //sonido al colisionar
                reposicionaMalo(basMalo); //cambia posicion del que colisiona
                iPuntos--;
                iContMalo++;

                //checar si se debe quitar vida
                if (iContMalo >= 5) {//cada 5 colisiones quita 1 vida
                    iVidas--; //quito vida
                    lklVidas.removeLast();
                    iVelocidad++;
                    iContMalo = 0; //reinicio contador a 0

                    if (iVidas == 0) {
                        bGameOver = true;
                        bPause = false;
                    }
                }
            }
        }

        //CHECA LA COLISION DE LOS MALOS Y EL JFRAME
        for (Malo basMalo : lklMalos) {
            //Si los malos llegan al final
            if (basMalo.getY() > 500) {

                if (basMalo.getTipo() == 'h') {

                    basMalo.setTipo('s');
                }

                reposicionaMalo(basMalo); //cambia la posicion del elemento
            }
        }

    }

    /*
     * chechaColisionBalas
     * 
     * checa colision entre balas y malos
     * checa colision entre balas y jframe
     */

    public void chechaColisionBalas() {

        //CHECA COLISION ENTRE BALAS Y MALOS
        for (Malo basMalo : lklMalos) {
            for (int iI = 0; iI < lklBalas.size(); iI++) {

                Bala basBala = (Bala) lklBalas.get(iI);

                if (basBala.intersecta(basMalo)) {

                    lklBalas.remove(basBala);
                    reposicionaMalo(basMalo); //cambia posicion del malo
                    iPuntos += 10;
                    soundMalosPrincipal.play();//sonido al colisionar

                }
            }
        }

        //CHECA COLISION ENTRE BALAS Y JFRAME
        for (int iI = 0; iI < lklBalas.size(); iI++) {

            Bala basBala = (Bala) lklBalas.get(iI);

            if (basBala.getY() < 0) {//checa arriba
                lklBalas.remove(basBala); //borra al elemento de la lista
            }
            if (basBala.getX() < 0) {//checa izq
                lklBalas.remove(basBala); //borra al elemento de la lista
            }
            if (basBala.getX() > getWidth()) {//checa derecha
                lklBalas.remove(basBala); //borra al elemento de la lista
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

        if (!bPause) {
            // Inicializan el DoubleBuffer
            if (imaImagenApplet == null) {
                imaImagenApplet = createImage(this.getSize().width,
                        this.getSize().height);
                graGraficaApplet = imaImagenApplet.getGraphics();
            }

            // Actualiza la imagen de fondo.
            URL urlImagenFondo = this.getClass().
                    getResource("images/field.png");
            
            imaImagenFondo = Toolkit.getDefaultToolkit().
                    getImage(urlImagenFondo);
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
        if (!bPause && basPrincipal != null && imaImagenFondo != null
                && lklMalos != null) {
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
            for (Bala basBala : lklBalas) {
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

        if (bPause) {

            graDibujo.drawImage(imaImagenPausa, 0, 0, getWidth(),
                    getHeight(), this);
        }

        /*PUNTAJE Y VIDAS*/
        graDibujo.setColor(Color.white);

        graDibujo.drawString("Score: " + iPuntos, getWidth() - 100,
                getHeight() - 20);

        //Dibuja imagen de fin de juego cuando se acaban las vidas
        if (iVidas == 0) {
            graDibujo.drawImage(imaImagenGameOver, 0, 0, getWidth(),
                    getHeight(), this);
        }
    }

    public void keyTyped(KeyEvent keyEvent) {

    }

    public void keyPressed(KeyEvent keyEvent) {

        //ifs de opciones de juego 
        if ((keyEvent.getKeyCode() == KeyEvent.VK_P) && !bGameOver) {//pausa

            bPause = !bPause;
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

        if ((keyEvent.getKeyCode() == KeyEvent.VK_R)) {//reiniciar juego
            if (iVidas == 0) {
                initVars();
                creaImagenes();
                creaObjetos();
                posicionaPerssonajes();
                bGameOver = false;
            }
        }

        //ifs de disparos
        if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {//centro

           

            Bala basBala = new Bala('c', 2, basPrincipal.getX() + basPrincipal.
                    getAncho() / 2,
                    basPrincipal.getY(), Toolkit.getDefaultToolkit().
                    getImage(urlImagenBala));

            //añado un elemento-bueno a la lista 
            lklBalas.add(basBala);
        }

        if (keyEvent.getKeyCode() == KeyEvent.VK_A) {//izaquierda

           

            Bala basBala = new Bala('i', 2, basPrincipal.getX() 
                    + basPrincipal.getAncho() / 2,
                    basPrincipal.getY(), Toolkit.getDefaultToolkit().
                    getImage(urlImagenBala));

            //añado un elemento-bueno a la lista 
            lklBalas.add(basBala);

        }

        if (keyEvent.getKeyCode() == KeyEvent.VK_S) {//derecha

            Bala basBala = new Bala('d', 2, basPrincipal.getX() 
                    + basPrincipal.getAncho() / 2,
                    basPrincipal.getY(), Toolkit.getDefaultToolkit().
                    getImage(urlImagenBala));

            //añado un elemento-bueno a la lista 
            lklBalas.add(basBala);
        }

        bPressed = false; //apago booleana de teclas
    }

    public int getHeight() {
        return 500;
    }

    public int getWidth() {
        return 800;
    }

    public static void main(String[] args) {

        ShootMe ShootMe = new ShootMe();
        ShootMe.setVisible(true);
        ShootMe.setSize(800, 500);
        ShootMe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
