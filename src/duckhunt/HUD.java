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
public class HUD implements Renderable, Updateable {
    /// possible HUD states
    public enum HUDState { NORMAL, COUNTING, BLINKING };

    /// current state
    private HUDState state;

    /// how long we are in current state?
    private long stateTime;

    /// contains game info for HUD
    private GameInfo game;

    /// HUD main picture
    private Picture hudMain;

    /// bullet picture
    private Picture bullet;

    /// red duck picture
    private Picture redDuck;

    /// round text rectangle
    private Picture roundText;

    /// fly away text rectangle
    private Picture flyAwayText;

    /// game over text rectangle
    private Picture gameOverText;

    /// numbers strip
    private ImageStrip numbers;

    /// blinking timer
    private int blinkTimer;

    /// blink on flag
    private boolean blink;

    /// duck count sound
    private SimpleSound duckCountSnd;

    /**
     * creates HUD instance
     *
     * @param gameInfo game info to display
     *
     * @throws java.io.IOException
     */
    public HUD(GameInfo gameInfo) throws java.io.IOException{
        game = gameInfo;

        hudMain      = new Picture("/images/hud.png");
        bullet       = new Picture("/images/bullet.png");
        numbers      = new ImageStrip("/images/numbers.png", 10);
        redDuck      = new Picture("/images/redDuck.png");
        roundText    = new Picture("/images/round.png");
        flyAwayText  = new Picture("/images/flyAway.png");
        gameOverText = new Picture("/images/gameOver.png");

        try{
            duckCountSnd = new SimpleSound("/sounds/count.wav");
        }catch(Exception e){
            System.out.println(" * error loading duck count sound");
        }

        blinkTimer = 0;
        stateTime  = 0;
        blink      = false;
        state      = HUDState.NORMAL;
    }

    /**
     * HUD rendering
     *
     * @param g context to render
     */
    @Override public void render(Graphics2D g){
        // main picture
        g.drawImage(hudMain.getImage(), 35, 390, null);

        // if intro, then display round text
        if(game.getState() == GameInfo.GameState.INTRO){
            g.drawImage(roundText.getImage(), 201, 100, null);
            renderNumberCenter(game.getRound(), 256, 135, g);
        }

        // if ducks fly away, then display it
        if(game.getState() == GameInfo.GameState.FLYAWAY)
            g.drawImage(flyAwayText.getImage(), 174, 100, null);

        // if game over, then display it
        if(game.getState() == GameInfo.GameState.GAMEOVER)
            g.drawImage(gameOverText.getImage(), 165, 100, null);

        // bullets - if 0, then blinking text
        if(game.getBulletsLeft() == 0){
            g.setColor(Color.black);
            if(blink)
                g.fillRect(40, 410, 46, 16);
        }else
            for(int i=0; i<game.getBulletsLeft(); i++)
                g.drawImage(bullet.getImage(), 45+16*i, 396, null);

        // score
        renderNumberRight(game.getScore(), 472, 395, g);

        // ducks states
        for(int i=0; i<game.getDucksInGame(); i++)
            switch(game.getDuckState(i)){
                case HUNTING:
                    g.setColor(Color.black);
                    if(blink){
                        if(state != HUDState.NORMAL)
                            g.drawImage(redDuck.getImage(), 185+i*16, 398, null);
                        else
                            g.fillRect(185+i*16, 398, 14, 14);
                    }
                    break;
                case DIED:
                    g.drawImage(redDuck.getImage(), 185+i*16, 398, null);
                    break;
            }
    }

    /// HUD update method
    @Override public void update(){
        stateTime++;
        if(++blinkTimer > 4){
            blinkTimer = 0;
            blink = !blink;
        }

        switch(state){
            case NORMAL:
                if(game.getState() == GameInfo.GameState.OUTRO){
                    stateTime = 0;
                    state = HUDState.COUNTING;
                }
                break;

            case COUNTING:
                int from = findBadDuck();

                if(from == 0){
                    if(stateTime >= 10){
                        stateTime = 0;
                        state = HUDState.BLINKING;
                        for(int i=0; i<GameInfo.ducksInRound; i++)
                            if(game.getDuckState(i)==GameInfo.HUDDuckState.DIED)
                                game.setDuckState(i, GameInfo.HUDDuckState.HUNTING);
                    }
                }else
                    if(stateTime == 10){ // move one duck to right place
                        stateTime = 0;
                        int to=0;
                        for(int i=0; game.getDuckState(i)!=GameInfo.HUDDuckState.CLEAR ; i++)
                            to=i+1;
                        
                        game.setDuckState(to, GameInfo.HUDDuckState.DIED);
                        game.setDuckState(from, GameInfo.HUDDuckState.CLEAR);
                        duckCountSnd.play();
                    }
                break;

            case BLINKING:
                if(stateTime > 100)
                    state = HUDState.NORMAL;
                break;
        }
    }

    /// HUD is not removable
    @Override public boolean removeReady(){
        return false;
    }

    /**
     * gets HUD state
     * 
     * @return current HUD state
     */
    public HUDState getState(){
        return state;
    }

    /**
     * render number to output (justified at top left)
     *
     * @param number number to render
     * @param x horizontal position
     * @param y vertical position
     * @param g graphics to render
     */
    private void renderNumber(long number, int x, int y, java.awt.Graphics2D g){
        for(int digit=Long.toString(number).length(); digit>0; digit--){
            g.drawImage(numbers.getImage((int)(number%10l)),
                        x+(digit-1)*numbers.getImage(0).getWidth(), y, null);
            number /= 10;
        }
    }

    /**
     * render number to output (justified at top right)
     *
     * @param number number to render
     * @param x horizontal position
     * @param y vertical position
     * @param g graphics to render
     */
    private void renderNumberRight(long number, int x, int y, java.awt.Graphics2D g){
        renderNumber(number, x-Long.toString(number).length()*numbers.getImage(0).getWidth(), y, g);
    }

    /**
     * render number to output (justified at top center)
     *
     * @param number number to render
     * @param x horizontal position
     * @param y vertical position
     * @param g graphics to render
     */
    private void renderNumberCenter(long number, int x, int y, java.awt.Graphics2D g){
        renderNumber(number, x-Long.toString(number).length()*numbers.getImage(0).getWidth()/2, y, g);
    }

    /**
     * checks if ducks are divided into two groups
     *
     * RRRRRWWWWW - 0, red ducks group and white ducks group
     * WWRWWWWWWW - 2, one red duck at position 2 is at wrong place
     *
     * @return index of bad duck, or 0 when ducks are sorted
     */
    public int findBadDuck(){
        int red = game.getShottedDucks();

        // calculate how many red ducks are in group
        int groupped;
        for(groupped=0; groupped<GameInfo.ducksInRound; groupped++)
            if(game.getDuckState(groupped)!=GameInfo.HUDDuckState.DIED)
                break;

        // all ducks are groupped
        if(red == groupped)
            return 0;

        // find first red after white space
        int badRed = 0;
        for(badRed=groupped; badRed<GameInfo.ducksInRound; badRed++)
            if(game.getDuckState(badRed)==GameInfo.HUDDuckState.DIED)
                break;
        
        return badRed;
    }
}
