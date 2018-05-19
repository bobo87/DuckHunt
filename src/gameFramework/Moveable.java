/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gameFramework;

/**
 * moving things on screen
 *
 * @author Peter
 */
public abstract class Moveable extends Positionable{
    /// moving speed
    private Vector2D speed = new Vector2D();

    /**
     * actual speed
     * 
     * @return the speed
     */
    public Vector2D getSpeed() {
        return speed;
    }

    /**
     * use new speed
     *
     * @param speed the speed to set
     */
    public void setSpeed(Vector2D speed) {
        setSpeed(speed.getX(), speed.getY());
    }

    /**
     * use new speed
     *
     * @param x horizontal speed
     * @param y vertical speed
     */
    public void setSpeed(float x, float y){
        speed.setCartesian(x, y);
    }

    /// move in desired speed
    public void move(){
        position.add(speed);
    }

    /// move in forced vector
    public void move(Vector2D delta){
        getPosition().add(delta);
    }    
}
