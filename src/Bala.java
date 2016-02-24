import java.awt.Image;

/**
 * Bala
 *
 * Modela la definición de todos los objetos de tipo Bala
 * <code>Bala</code>
 *
 * @author Sergio Diaz y Ana Karen Beltran
 * @version 1
 * @date 24/01/2016
 */

public class Bala extends Base{
        
    private char cTipo;
    private int iVel;
    

    /**
     * Bala
     * 
     * Metodo constructor usado para crear el objeto bala
     * 
     * @param iX es la <code>posicion en x</code> del objeto.
     * @param iY es la <code>posicion en y</code> del objeto.
     * @param iAncho es el <code>ancho</code> del objeto.
     * @param iAlto es el <code>Largo</code> del objeto.
     * @param imaImagen es la <code>imagen</code> del objeto.
     * 
     */
    public Bala(char cTipo, int iVel, int iX, int iY, Image imaImagen) {
        super(iX, iY, imaImagen);
        
        this.cTipo = cTipo;
        this.iVel = iVel;
    }  

    /**
     * setTipo
     * 
     * Metodo modificador usado para cambiar la posicion en x del objeto
     * 
     * @param cTipo es la <code>posicion en x</code> del objeto.
     * 
     */
    public void setTipo(char cTipo) {
        this.cTipo = cTipo;
    }
    
    /**
     * getTipo
     * 
     * Metodo de acceso que regresa la posicion en x del objeto 
     * 
     * @return cTipo es la <code>posicion en x</code> del objeto.
     * 
     */
    public char getTipo() {
            return cTipo;
    }
    
    /**
     * setVel
     * 
     * Metodo modificador usado para cambiar la posicion en x del objeto
     * 
     * @param cTipo es la <code>posicion en x</code> del objeto.
     * 
     */
    public void setVel(int iVel) {
        this.iVel = iVel;
    }
    
    /**
     * getVel
     * 
     * Metodo de acceso que regresa la posicion en x del objeto 
     * 
     * @return iVel es la <code>velocidad de bala</code> del objeto.
     * 
     */
    public int getVel() {
            return iVel;
    }
    
    
    
    public void avanza() {

        if (cTipo == 'c') {//centro

            setY(getY() - (1 * iVel));

        }

        if (cTipo == 'd') {//derecha

            setY(getY() - (1 * iVel));

            setX(getX() + (1 * getVel()));
            setY(getY() - (1 * getVel()));

        }

        if (cTipo == 'i') {//izquierda

            setX(getX() - (1 * getVel()));
            setY(getY() - (1 * getVel()));

        } else {
            //for (Base basBala : lklBalas) {
            setX(getX());
            setY(getY());

        }

    }
    
    
    
}
