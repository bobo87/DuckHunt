/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gameFramework;

/**
 *
 * @author Peter
 */
public class Vector2D {
    /// x vector coordinate
    private float x,
    /// y vector coordinate
        y;

    /// default constructor at (0; 0)
    public Vector2D(){
        x = y = 0.0f;
    }

    /**
     * creates vector using coordinates
     * @param x x coordinate
     * @param y y coordinate
     */
    public Vector2D(float x, float y){
        this.x = x;
        this.y = y;
    }

    /**
     * create new vector from polar data
     * @param angle angle in degrees
     * @param radius radius of vector
     * @return new vector
     */
    public static Vector2D createPolar(float angle, float radius){
        return new Vector2D( radius * (float)Math.cos( Math.toRadians(angle%360.0f)) ,
                             radius * (float)Math.sin( Math.toRadians(angle%360.0f)) );
    }

    /**
     * adds actual vector with other
     * @param other vector to sumate with
     * @return actual vector after adding
     */
    public Vector2D add(Vector2D other){
        x += other.getX();
        y += other.getY();
        
        return this;
    }

    /**
     * sets new vector
     * @param x x coord
     * @param y y coord
     */
    public void setCartesian(float x, float y){
        this.x = x;
        this.y = y;
    }
    /**
     * @return the x
     */
    public float getX() {
        return x;
    }

    /**
     * @return x casted as int
     */
    public int getIntX(){
        return (int)x;
    }

    /**
     * @param x the x to set
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public float getY() {
        return y;
    }

    /**
     * @return y casted as int
     */
    public int getIntY(){
        return (int)y;
    }

    /**
     * @param y the y to set
     */
    public void setY(float y) {
        this.y = y;
    }
    
}
