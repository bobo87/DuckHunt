/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gameFramework;

import java.util.*;

/**
 *
 * @author net
 */
public class RenderList extends LinkedList<Renderable>{

    /// updates all contained objects
    public void renderAll(java.awt.Graphics2D g){
        Iterator<Renderable> it = iterator();
        while(it.hasNext()){
            Renderable item = it.next();
            if(item.removeReady())
                it.remove();
            else
                item.render(g);
        }
    }
}
