/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package duckhunt;

import gameFramework.*;
import java.util.*;

/**
 *
 * @author net
 */
public class DuckList implements Renderable, Updateable {
    /// last shotted position
    private Vector2D lastPos = new Vector2D();

    /// ducks to render
    private RenderList renders;

    /// ducks to update
    private UpdateList updates;

    /// all ducks
    private java.util.List<Duck> ducks;

    /// constructor, creates lists
    public DuckList(){
        ducks = new java.util.LinkedList<Duck>();
        renders = new RenderList();
        updates = new UpdateList();        
    }

    /// add new duck
    public void add(Duck duck){
        ducks.add(duck);
        renders.add(duck);
        updates.add(duck);
    }

    /**
     * fly away all flying ducks (no bullets left)
     *
     * @return true, if any duck changed state to fly away
     */
    public boolean flyAwayAll(){
        Iterator<Duck> it = ducks.iterator();
        boolean anyDuck = false;

        while(it.hasNext()){
            Duck item = it.next();
            
            if(item.getState() == Duck.DuckState.FLYING){
                item.flyAway();
                anyDuck = true;
            }
        }

        return anyDuck;
    }

    /// update ducks
    public void update(){
        updates.updateAll();

        // collect died ducks
        Iterator<Duck> it = ducks.iterator();
        while(it.hasNext())
            if(it.next().removeReady())
                it.remove();
    }

    /// render ducks
    public void render(java.awt.Graphics2D g){
        renders.renderAll(g);
    }

    /**
     * duck list is ready to remove?
     *
     * @return true, if there is no ducks left
     */
    public boolean removeReady(){
        return false;
    }

    /**
     * shot to ducks
     *
     * If duck is shotted, return score, else 0. It can shot only one duck
     * a time.
     *
     * @param x horizontal position of gun
     * @param y vertical position of gun
     *
     * @return score for this shot
     */
    public int shot(int x, int y){
        Iterator<Duck> it = ducks.iterator();

        while(it.hasNext()){
            Duck item = it.next();
            int score = item.shot(x, y);
            
            if(score>0){
                lastPos = item.getPosition();
                return score;
            }
        }

        return 0;
    }

    /**
     * @return ducks count
     */
    public int getSize(){
        return ducks.size();
    }

    /**
     * return last shotted duck position
     * 
     * @return
     */
    public Vector2D lastDuckPosition(){
        return lastPos;
    }
}
