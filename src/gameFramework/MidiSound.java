/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gameFramework;

import javax.sound.midi.*;

/**
 *
 * @author Peter
 */
public class MidiSound implements Sound{
    /// sequence (from file)
    private Sequence sequence;

    /// sequencer object
    private Sequencer sequencer;


    /**
     * creates new midi sound
     *
     * @param fname file name
     * @throws javax.sound.midi.InvalidMidiDataException
     * @throws java.io.IOException
     * @throws javax.sound.midi.MidiUnavailableException
     */
    public MidiSound(String fname) throws InvalidMidiDataException,
            java.io.IOException, MidiUnavailableException {
        sequence = MidiSystem.getSequence(getClass().getResourceAsStream(fname));
        sequencer = MidiSystem.getSequencer();
        sequencer.open();
        sequencer.setSequence(sequence);
    }

    /**
     * creates new midi sound
     *
     * @param fname sound file name
     * @param loops loops count
     * @throws javax.sound.midi.InvalidMidiDataException
     * @throws java.io.IOException
     * @throws javax.sound.midi.MidiUnavailableException
     */
    public MidiSound(String fname, int loops) throws InvalidMidiDataException,
            java.io.IOException, MidiUnavailableException {
        sequence = MidiSystem.getSequence(getClass().getResourceAsStream(fname));
        sequencer = MidiSystem.getSequencer();
        sequencer.open();
        sequencer.setSequence(sequence);
        sequencer.setLoopCount(loops);
    }

    /// play music
    public void play(){
        if(!isPlaying())
            sequencer.setMicrosecondPosition(0l);
        sequencer.start();
    }

    /// stop music
    public void stop(){
        sequencer.stop();
    }

    /// music is playing?
    public boolean isPlaying(){
        return sequencer.isRunning();
    }
}
