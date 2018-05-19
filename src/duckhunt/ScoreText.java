/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package duckhunt;

import gameFramework.*;
import java.awt.*;

/**
 *
 * @author Peter
 */
public class ScoreText implements Updateable, Renderable {
    /// score in string form
    private String score;

    /// position
    private Vector2D position;

    /// score life time
    private int lifeTime;

    /// font used for score
    private static final Font font = new Font("Arial", Font.BOLD, 14);
    /**
     * create still score text
     *
     * @param updates update list
     * @param renders render list
     * @param position base position
     * @param score score to draw
     */
    public ScoreText(UpdateList updates, RenderList renders,
                     Vector2D position, int score){
        updates.add(this);
        renders.add(this);
        
        this.score    = Integer.toString(score);
        this.position = new Vector2D(position.getX(), position.getY());

        lifeTime = 0;
    }

    /// add life time
    public void update(){
        lifeTime++;
    }

    /**
     * render score
     *
     * @param g graphics
     */
    public void render(Graphics2D g){
        g.setColor(Color.white);
        g.setFont(font);
        g.drawString(score, position.getX()+20, position.getY()-10);
    }

    /**
     * score text ready to remove?
     *
     * @return true, if we live too long
     */
    public boolean removeReady(){
        return lifeTime>25;
    }
}
