
import java.awt.Image;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @authors Sergio Diaz A01192313, Ana Karen Beltran A01192508
 * @version 1
 * @date 23/feb/2016
 */
public class Bala extends Base {
    
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
     * @return iVel es la <code>posicion en x</code> del objeto.
     * 
     */
    public int getVel() {
            return iVel;
    }
    
}
