/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gameFramework;

import java.io.*;
import java.awt.image.*;

/**
 * animation (all frames with same speed) class
 *
 * @author Peter
 */
public class Animation extends UpdateAdapter {
    /// animation image file name
    private String animFile;

    /// animation contain few frames
    private ImageStrip frames = null;

    /// animation playing speed in ticks per frame
    private int speed       = 0,

    /// current tick counter
                tick        = 0,

    /// active frame counter
                activeFrame = 0;
    
    /**
     * creates new animation
     *
     * @param fname filename with animation
     * @param framesCount number of frames
     * @param speed animation playback speed (ticks per image shift)
     * @throws java.io.IOException can't load image
     */
    public Animation(String fname, int framesCount, int speed) throws IOException{
        // add to updateable list
        super();

        // using this file
        animFile = fname;

        // try to load image
        frames = new ImageStrip(fname, framesCount);

        // set anim speed
        this.speed = speed;
    }

    /**
     * get one (currently active) frame from animation
     *
     * @return animation active frame
     */
    public BufferedImage getActiveFrame(){
        return frames.getImage(activeFrame);
    }

    /**
     * do one animation tick
     *
     * if ticks count is bigger, or equal to speed, then switch to
     * next animation frame
     */
    @Override public void update(){
        if( ++tick < getSpeed() )
            return;

        // go to next frame
        tick = 0;
        if( ++activeFrame >= frames.getFramesCount() )
            activeFrame = 0;
    }

    /**
     * get last speed
     * 
     * @return the speed
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * set new speed
     *
     * @param value the speed to set
     */
    public void setSpeed(int value) {
        speed = value;
        if( tick >= speed)
            tick = 0;
    }

    /// select randomly one frame as active
    public void randomizePhase(){
       activeFrame = ((int)(Math.random()*1000.0))%frames.getFramesCount();
    }
}
