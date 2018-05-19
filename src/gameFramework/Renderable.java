/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gameFramework;

/**
 *
 * @author Peter
 */
public interface Renderable {
    public void render(java.awt.Graphics2D g);
    public boolean removeReady();
}
