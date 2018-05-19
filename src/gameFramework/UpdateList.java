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
public class UpdateList extends LinkedList<Updateable>{

    /// updates all contained objects
    public void updateAll(){
        Iterator<Updateable> it = iterator();
        while(it.hasNext()){
            Updateable item = it.next();
            if(item.removeReady())
                it.remove();
            else
                item.update();
        }
    }
}
