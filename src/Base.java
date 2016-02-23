/**
 * Base
 *
 * Modela la definición de todos los objetos de tipo
 * <code>Base</code>
 *
 * @author Sergio Diaz
 * @version 2 
 * @date 26/01/2016
 */
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import javax.swing.ImageIcon;
import java.awt.Rectangle;

public class Base {

    private int iX;     //posicion en x.       
    private int iY;     //posicion en y.
    private int iAncho; //ancho del objeto
    private int iAlto; //largo del objeto
    private Image imaImagen;	//imagen.
    private ImageIcon imiImagen;  // imagen con medidas

    /**
     * Base
     * 
     * Metodo constructor usado para crear el objeto animal
     * creando el icono a partir de una imagen
     * 
     * @param iX es la <code>posicion en x</code> del objeto.
     * @param iY es la <code>posicion en y</code> del objeto.
     * @param iAncho es el <code>ancho</code> del objeto.
     * @param iAlto es el <code>Largo</code> del objeto.
     * @param imaImagen es la <code>imagen</code> del objeto.
     * 
     */
    public Base(int iX, int iY , Image imaImagen) {
        this.iX = iX;
        this.iY = iY;
        this.imaImagen = imaImagen;
        this.imiImagen = new ImageIcon(imaImagen);
        this.iAncho = this.imiImagen.getIconWidth();
        this.iAlto = this.imiImagen.getIconHeight();
    }

    
    /**
     * setX
     * 
     * Metodo modificador usado para cambiar la posicion en x del objeto
     * 
     * @param iX es la <code>posicion en x</code> del objeto.
     * 
     */
    public void setX(int iX) {
        this.iX = iX;
    }

    /**
     * getX
     * 
     * Metodo de acceso que regresa la posicion en x del objeto 
     * 
     * @return iX es la <code>posicion en x</code> del objeto.
     * 
     */
    public int getX() {
            return iX;
    }

    /**
     * setY
     * 
     * Metodo modificador usado para cambiar la posicion en y del objeto 
     * 
     * @param iY es la <code>posicion en y</code> del objeto.
     * 
     */
    public void setY(int iY) {
            this.iY = iY;
    }

    /**
     * getY
     * 
     * Metodo de acceso que regresa la posicion en y del objeto 
     * 
     * @return posY es la <code>posicion en y</code> del objeto.
     * 
     */
    public int getY() {
        return iY;
    }

    /**
     * setImagen
     * 
     * Metodo modificador usado para cambiar el icono de imagen del objeto
     * tomandolo de un objeto imagen
     * 
     * @param imaImagen es la <code>imagen</code> del objeto.
     * 
     */
    public void setImagen(Image imaImagen) {
        this.imaImagen = imaImagen;
        this.imiImagen = new ImageIcon(imaImagen);
        this.iAncho = this.imiImagen.getIconWidth();
        this.iAlto = this.imiImagen.getIconHeight();
    }

    /**
     * getImagen
     * 
     * Metodo de acceso que regresa la imagen que representa el icono del objeto
     * 
     * @return la imagen a partide del <code>icono</code> del objeto.
     * 
     */
    public Image getImagen() {
        return imaImagen;
    }

    /**
     * getAncho
     * 
     * Metodo de acceso que regresa el ancho del icono 
     * 
     * @return un <code>entero</code> que es el ancho de la imagen.
     * 
     */
    public int getAncho() {
        return iAncho;
    }

    /**
     * getAlto
     * 
     * Metodo que  da el alto del icono 
     * 
     * @return un <code>entero</code> que es el alto de la imagen.
     * 
     */
    public int getAlto() {
        return iAlto;
    }
    
    /**
     * paint
     * 
     * Metodo para pintar el animal
     * 
     * @param graGrafico    objeto de la clase <code>Graphics</code> para graficar
     * @param imoObserver  objeto de la clase <code>ImageObserver</code> es el 
     *    Applet donde se pintara
     * 
     */
    public void paint(Graphics graGrafico, ImageObserver imoObserver) {
        graGrafico.drawImage(getImagen(), getX(), getY(), getAncho(), getAlto(), imoObserver);
    }

    /**
     * equals
     * 
     * Metodo para checar igualdad con otro objeto
     * 
     * @param objObjeto    objeto de la clase <code>Object</code> para comparar
     * @return un valor <code>boleano</code> que sera verdadero si el objeto
     *   que invoca es igual al objeto recibido como parámetro
     * 
     */
    public boolean equals(Object objObjeto) {
        // si el objeto parametro es una instancia de la clase Base
        if (objObjeto instanceof Base) {
            // se regresa la comparación entre este objeto que invoca y el
            // objeto recibido como parametro
            Base basParam = (Base) objObjeto;
            return this.getX() ==  basParam.getX() && 
                    this.getY() == basParam.getY() &&
                    this.getAncho() == basParam.getAncho() &&
                    this.getAlto() == basParam.getAlto() &&
                    this.getImagen() == basParam.getImagen();
        }
        else {
            // se regresa un falso porque el objeto recibido no es tipo Base
            return false;
        }
    }

    /**
     * toString
     * 
     * Metodo para obtener la interfaz del objeto
     * 
     * @return un valor <code>String</code> que representa al objeto
     * 
     */
    public String toString() {
        return " x: " + this.getX() + " y: "+ this.getY() +
                " ancho: " + this.getAncho() + " alto: " + this.getAlto();
    }
    
    public Rectangle getPerimetro() {
        return new Rectangle(getX(), getY(), getAncho(), getAlto());
    }
    
    
    //Crea rectangulo superior en el objeto principal
    public Rectangle getRectanguloSuperior() {
        
        // creo un rectangulo en la parte de .25 alto *.25 ancho en el 
        //centro de la base
        
       // return new Rectangle(getX()+(int)(getAncho()*.25),
         //       getY() + (int)(getAlto()*.25), getAncho()/4, getAlto()/4);
        
        return new Rectangle(getX()+(int)(getAncho()*.15),
                getY() + (int)(getAlto()*.15), (int)(getAncho()*.30), 
                    (int)(getAlto()*.10));
    }
    
    //Crea rectangulo inferior en el objeto principal
    public Rectangle getRectanguloInferior() {
        
        // creo un rectangulo del 10% del personaje, que estaria en la parte
        // inferior
        return new Rectangle(getX(), getY() + (int)(getAlto()*.9), 
                getAncho(),(int)(getAlto()*.1));
    }
    
   //Crea rectangulos lateral izquierdo en el objeto principal
    public Rectangle getRectanguloIzquierdo() {
        
        //se crea barra vertical izquierda
        return new Rectangle(getX(), getY(), (int)(getAncho()*.10), getAlto());
        
    }
    
   //Crea rectangulos lateral derecho en el objeto principal
    public Rectangle getRectanguloDerecho() {
        
        //se crea barra vertical derecho
        return new Rectangle(getX()+(int)(getAncho()*.9), getY(), 
                (int)(getAncho()*.1), getAlto());
    }

    
    /**
     * toString
     * 
     * Metodo para obtener el boleano de si el objeto se interesecta o no
     * 
      * @return un valor <code>bool</code> que indica su el objeto colisiona
     * 
     */
    
    public boolean intersecta(Object obj) { 

        if (obj instanceof Base){
         
            return getPerimetro().intersects(((Base) obj).getPerimetro());
            
        } else if (obj instanceof Rectangle) {
            
            return getPerimetro().intersects(((Rectangle) obj).getBounds());
        }
                
        else {
            
            return false;
        }
        
    }
    
        
    /**
     * toString
     * 
     * Metodo para obtener el boleano de si el objeto se interesecta o no
     * 
     * @return un valor <code>bool</code> que indica si dos coordenadas
     * colisionan
     * 
     */
    
    public boolean intersecta(int iX, int iY) { 
        
        return getPerimetro().contains(iX,iY);
        
    }   
}