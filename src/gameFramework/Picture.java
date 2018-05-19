/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gameFramework;

import java.awt.image.*;

/**
 * picture with loading and drawing
 *
 * @author Peter
 */
public class Picture extends ImageStrip {

    /// create picture from image strip
    public Picture(String fname) throws java.io.IOException{
        super(fname, 1);
    }

    /// gets current image
    public BufferedImage getImage(){
        return getImage(0);
    }
}
