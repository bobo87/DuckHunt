/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gameFramework;

import java.util.*;

/**
 *
 * @author Peter
 */
public class SoundMap extends HashMap<String, Sound>{
    /// stop all playing sounds
    public void stopAll(){
        Iterator<String> iter = keySet().iterator();
        while(iter.hasNext())
            get(iter.next()).stop();
    }

    /**
     * plays desired sound
     * @param what sound name
     * @return true, if sound was found
     */
    public boolean play(String what){
        if(!containsKey(what))
            return false;

        get(what).play();
        return true;
    }

    /**
     * is sound playing?
     *
     * @param what sound name
     * @return true, if sound is playing
     */
    public boolean isPlaying(String what){
        if(!containsKey(what))
            return false;

        return get(what).isPlaying();
    }
}
