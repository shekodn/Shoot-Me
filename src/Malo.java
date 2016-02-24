
import java.awt.Image;

/**
 * Malo
 *
 * Modela la definición de todos los objetos de tipo malo
 * <code>Malo</code>
 *
 * @author Sergio Diaz y Ana Karen Beltran
 * @version 1
 * @date 24/01/2016
 */

public class Malo extends Base{
   
    private char cTipo;
    
    
    public Malo(char cTipo, int iX, int iY, Image imaImagen) {
        super(iX, iY, imaImagen);
        
        this.cTipo = cTipo;
        
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
    
    
    
}

