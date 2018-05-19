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
public class SimpleSound implements Sound{
    /// contain used sound
    private Clip clip;

    /// how many times repeat?
    private int looping;

    /**
     * create simple sound
     * @param fname file name
     * @throws javax.sound.sampled.LineUnavailableException
     * @throws java.io.IOException
     * @throws javax.sound.sampled.UnsupportedAudioFileException
     */
    public SimpleSound(String fname) throws LineUnavailableException,
            java.io.IOException, UnsupportedAudioFileException{
        clip = AudioSystem.getClip();
        AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(fname));
        clip.open(ais);        
    }

    /**
     * create simple sound
     * @param fname file name
     * @param loops looping count
     * @throws javax.sound.sampled.LineUnavailableException
     * @throws java.io.IOException
     * @throws javax.sound.sampled.UnsupportedAudioFileException
     */
    public SimpleSound(String fname, int loops) throws LineUnavailableException,
            java.io.IOException, UnsupportedAudioFileException{
        clip = AudioSystem.getClip();
        AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(fname));
        clip.open(ais);
        looping=loops;
    }
    /// play sound
    public void play(){
        if(!isPlaying())
            clip.setMicrosecondPosition(0);

        if(looping==0)
            clip.start();
        else
            clip.loop(looping);
    }

    /// stop sound
    public void stop(){
        clip.stop();
    }

    /**
     * is clip playing?
     *
     * @return true, if playing
     */
    public boolean isPlaying(){
        return clip.isRunning();
    }
}
