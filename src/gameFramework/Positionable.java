/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gameFramework;

/**
 * static things on screen
 *
 * @author Peter
 */
public abstract class Positionable{
    /// position on screen
    protected Vector2D position=new Vector2D();

    /**
     * last setted position
     *
     * @return the position
     */
    public Vector2D getPosition() {
        return position;
    }

    /**
     * set new position
     *
     * @param position the position to set
     */
    public void setPosition(Vector2D position) {
        setPosition(position.getX(), position.getY());
    }

    /**
     * set new position
     *
     * @param x horizontal position
     * @param y vertical position
     */
    public void setPosition(float x, float y){
        position.setCartesian(x, y);
    }

}
