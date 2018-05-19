/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gameFramework;

import javax.sound.sampled.*;

/**
 *
 * @author Peter
 */
public class MultiSound implements Sound{
    /// used sounds
    private SimpleSound[] sounds;

    /**
     * multiple sound (can be played 'count' times at same time)
     *
     * @param fname file name
     * @param count how many times at same time?
     */
    public MultiSound(String fname, int count)throws LineUnavailableException,
            java.io.IOException, UnsupportedAudioFileException{
        sounds = new SimpleSound[count];
        
        for(int i=0; i<sounds.length; i++)
            sounds[i] = new SimpleSound(fname);
    }

    /// plays sound, if any free
    public void play(){
        for(int i=0; i<sounds.length; i++)
            if(!sounds[i].isPlaying()){
                sounds[i].play();
                return;
            }
    }

    /// stop all sounds
    public void stop(){
        for(int i=0; i<sounds.length; i++)
            sounds[i].stop();
    }

    /// is any sound playing?
    public boolean isPlaying(){
        for(int i=0; i<sounds.length; i++)
            if(sounds[i].isPlaying())
                return true;

        return false;
    }
}
