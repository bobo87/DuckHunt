/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gameFramework;

import java.awt.*;

/**
 * describes screen actors (player, AI, etc...)
 *
 * @author net
 */
public abstract class Actor extends Moveable implements Updateable, Renderable{
    /// actor animations set
    protected AnimationSet anims;

    /// sound map - contains actor sounds

    protected SoundMap sounds;
    /**
     * create new actor
     */
    public Actor(){
        super();

        anims  = new AnimationSet();
        sounds = new SoundMap();
    }

    /**
     * on actor removing
     *
     * @throws java.lang.Throwable
     */
    @Override public void finalize() throws Throwable{
        super.finalize();        
    }

    /**
     * default actor update is only moving in selected speed vector
     */
    public void update(){
        move();
        anims.update();
    }

    /**
     * delete us?
     *
     * @return true, if object wants to be deleted
     */
    public boolean removeReady(){
        return false;
    }
    
    /**
     * render actor
     *
     * default - draws actual animation frame at selected position
     *
     * @param g graphics to draw on
     */
    public void render(Graphics2D g){
        g.drawImage(anims.getFrame(), getPosition().getIntX(),
                getPosition().getIntY(), null);
    }
}
