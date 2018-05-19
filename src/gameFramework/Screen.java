/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gameFramework;

import java.awt.image.*;
import java.awt.*;

/**
 * one viewed screen
 * 
 * contains thread, which draw itself at desired FPS
 *
 * @author Peter
 */
public abstract class Screen extends UpdateAdapter implements Runnable, Renderable {
    /// things that will be updated
    protected UpdateList updateList;

    /// frame used at drawing space
    protected Container frame = null;

    /// how long (in nanoseconds) takes one frame at current FPS?
    private long frameTotal = 0;

    /// double buffering offscreen
    private BufferedImage backBuffer = null;

    /// is screen running?
    private volatile boolean running = false;

    /// screen thread
    private Thread thread = null;

    /// screen width
    private int width,
    /// screen height
            height;

    /**
     * create new screen
     *
     * @param view panel to use as rendering surface
     * @param FPS desired FPS
     * @param width view width
     * @param height view height
     */
    public Screen(Container view, int FPS, int width, int height){
        assert view != null;
        assert FPS>0 && FPS<100;

        this.width  = width;
        this.height = height;

        // use panel
        frame = view;

        // compute frame time in ns from FPS
        frameTotal = (long)1e9 / FPS;

        // create offscreen buffer
        backBuffer = new BufferedImage(width, height,BufferedImage.TYPE_INT_ARGB);

        // create updates list
        updateList = new UpdateList();
    }

    /**
     * flips back buffer to render surface
     */
    protected void flip(){
        Graphics2D graphics;

        graphics = (Graphics2D)frame.getGraphics();

        // if we have backBuffer and graphics
        if( graphics!=null && backBuffer!=null ){
            // redraw it
            graphics.drawImage(backBuffer, 0, 0, null);

            // and sync
            Toolkit.getDefaultToolkit().sync();

            graphics.dispose();
        }        
    }

    /**
     * screen update method - before rendering, every frame
     */
    @Override synchronized public void update(){
        updateList.updateAll();
    }

    /**
     * threaded run method (screen loop)
     */
    public void run(){
        // frame starting time
        long beforeTime,         
        // rendering time
             timeDiff,
        // time to sleep, for reach desired FPS
             sleepTime;

        running = true;
        while(running){

            // start frame time
            beforeTime = System.nanoTime();

            // update screen
            update();

            // render screen
            render((Graphics2D)backBuffer.getGraphics());

            // flip backbuffer to screen
            flip();

            // compute estimated time
            timeDiff  = System.nanoTime() - beforeTime;
            sleepTime = (frameTotal - timeDiff);

            // can't go lower, than 5ms
            if( sleepTime < (long)5e6 )
                sleepTime = (long)5e6;

            // good night ;)
            try{
                Thread.sleep(sleepTime / (long)1e6, (int)(sleepTime % (long)1e6));
            }catch(InterruptedException e){
            }
        }
    }

    /**
     * starts thread
     */
    public void start(){
        if( running )
            return;

        // prevent from restarting!!! must create new thread
        thread = null;
        
        thread = new Thread(this);

        thread.start();
    }

    /**
     * stops thread
     */
    public void stop(){
        running = false;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return height;
    }
}
