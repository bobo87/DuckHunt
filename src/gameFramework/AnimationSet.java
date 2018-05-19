/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gameFramework;

import java.util.*;
import java.awt.image.*;

/**
 * contain named animations
 *
 * @author net
 */
public class AnimationSet extends UpdateAdapter{
    /// animations hash table
    private Hashtable<String, Animation> anims;

    /// selected animation name
    private String selected;

    /// selected animation
    private Animation selectedAnim;

    /**
     * create empty animation set
     */
    public AnimationSet(){
        anims = new Hashtable<String, Animation>();
        selected = "";
        selectedAnim = null;
    }

    /**
     * add animation to set
     *
     * @param animation animation to use
     * @param name animation name
     */
    public void add(Animation animation, String name){
        anims.put(name, animation);
    }

    /**
     * select active animation
     *
     * @param name animation to select
     * @return animation name found?
     */
    public boolean select(String name){
        selected = name;
        selectedAnim = getSelected();
        
        return anims.containsKey(name);
    }

    /// get selected animation
    public Animation getSelected(){
        if(!selected.equals(""))
            return anims.get(selected);
        return null;
    }

    /// get selected animation name
    public String getSelectedName(){
        return selected;
    }

    /// get active frame
    public BufferedImage getFrame(){
        if(!selected.equals(""))
            return anims.get(selected).getActiveFrame();
        return null;
    }

    /// randomize animation phases
    public void randomizePhases(){
        Enumeration<Animation> element = anims.elements();
        while(element.hasMoreElements())
            element.nextElement().randomizePhase();
    }

    /// update animations
    @Override public void update(){
        Enumeration<Animation> element = anims.elements();
        while(element.hasMoreElements())
            element.nextElement().update();
    }
}
