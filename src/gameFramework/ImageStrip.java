/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gameFramework;

import java.awt.image.*;
import java.awt.*;
import javax.imageio.*;
import java.io.*;

/**
 * array of image created from one strip
 *
 * @author Peter
 */
public class ImageStrip{
    /// system graphics environment
    private static GraphicsEnvironment ge = null;

    /// system graphics configuration
    private static GraphicsConfiguration gc = null;

    /// cutted image
    private BufferedImage[] frames;

    /**
     * creates new image strip
     *
     * @param fname file name with images
     * @param framesCount subimages count
     * @throws java.io.IOException when strip can't be loaded
     */
    public ImageStrip(String fname, int framesCount) throws IOException {
        // can't create <=0 images
        assert framesCount > 0;

        // if haven't these, get from system
        if( ge == null )
            ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        if( gc == null )
            gc = ge.getDefaultScreenDevice().getDefaultConfiguration();

        // load image
        BufferedImage strip = loadImage(fname);
        if( strip == null ){
            System.out.println(" * Can't load image strip");
            throw new IOException("Can't load image strip!");
        }

        // compute dividing and transparency
        int width        = (int) strip.getWidth() / framesCount;
        int height       = strip.getHeight();
        int transparency = strip.getColorModel().getTransparency();

        // create frames array
        frames = new BufferedImage[framesCount];

        // cut image into frames
        Graphics2D stripGC;
        for(int i=0; i<framesCount; i++){
            frames[i] = gc.createCompatibleImage(width, height, transparency);
            stripGC = frames[i].createGraphics();
            stripGC.drawImage(strip, 0, 0, width, height,
                              i*width, 0, (i*width)+width, height, null);
            stripGC.dispose();
        }
    }

    /**
     * loads device compatible buffered image
     *
     * @param fname file name to load
     * @return image
     */
    private BufferedImage loadImage(String fname){
        try{
            // read image as resource
            System.out.println(" * loading image \""+fname+"\"");
            BufferedImage im = ImageIO.read(getClass().getResource(fname));
            int transparency = im.getColorModel().getTransparency();

            // create compatible image to speed up operations
            BufferedImage copy = gc.createCompatibleImage(im.getWidth(), im.getHeight(), transparency);
            Graphics2D g2d = copy.createGraphics();

            // draw image into returned graphics
            g2d.drawImage(im, 0, 0, null);
            g2d.dispose();
        
            return copy;

        }catch (IOException e){
            System.out.print(" * Can't load image \""+fname+"\"");
            return null;
        }
    }

    /**
     * return image from the strip
     *
     * @param index index of the image <0; framesCount-1>
     * @return image
     */
    public BufferedImage getImage(int index){
        assert index>0 && index<frames.length;

        return frames[index];
    }

    /**
     * gets total frames count
     * 
     * @return frames count
     */
    public int getFramesCount(){
        return frames.length;
    }
}
