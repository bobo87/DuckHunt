/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package duckhunt;

import gameFramework.*;



/**
 * class describing duck on screen
 *
 * @author net
 */
public class Duck extends Actor{
    /// score for this duck
    public int score = 0;

    /// what is duck doing?
    public enum DuckState { FLYING, SCARED, FALLING, FLYAWAY, DIED };

    /// how long is duck scared?
    private int scaredTimer = 0;

    /// duck velocity (absolute)
    private float speed = 7.5f;

    /// avoid 10px from left
    private static final int leftEdge = 10,
    
    /// avoid right edge (screenWidth - duckWidth - 10)px
            rightEdge = 438,
            
    /// avoid 10px from top
            topEdge = 10,
            
    /// fly over grass
            bottomEdge = 280;

    /// actual duck state
    private DuckState state;

    /// current game data
    private GameInfo game;

    /// our round ID
    private int id;

    /**
     * creates one duck on desired position (in grass)
     *
     * @param color color of the duck
     * @param score value of the duck
     * @param gameInfo game information
     *
     * @throws java.io.IOException
     */
    public Duck(String color, int score, GameInfo gameInfo) throws java.io.IOException{
        super();

        // create animations for duck
        anims.add(new Animation("/images/"+color+"/away.png",        4, 2), "FLYAWAY");
        anims.add(new Animation("/images/"+color+"/leftUp.png",      4, 2), "LEFTUP");
        anims.add(new Animation("/images/"+color+"/rightUp.png",     4, 2), "RIGHTUP");
        anims.add(new Animation("/images/"+color+"/left.png",        4, 2), "LEFT");
        anims.add(new Animation("/images/"+color+"/right.png",       4, 2), "RIGHT");
        anims.add(new Animation("/images/"+color+"/scaredLeft.png",  1, 2), "SCAREDLEFT");
        anims.add(new Animation("/images/"+color+"/scaredRight.png", 1, 2), "SCAREDRIGHT");
        anims.add(new Animation("/images/"+color+"/fall.png",        2, 2), "FALL");

        try{
            sounds.put("SPLAT", new SimpleSound("/sounds/splat.wav"));
            sounds.put("FALL", new SimpleSound("/sounds/fall.wav"));
            sounds.put("FLAP", new SimpleSound("/sounds/flap.wav", javax.sound.sampled.Clip.LOOP_CONTINUOUSLY));
        }catch(Exception e){
            System.out.println(" * error loading duck sounds");
        }

        // init
        game = gameInfo;
        speed = game.getDuckSpeed();
        this.score = score;
        takeOff();

        id = game.addHuntedDuck();
    }

    /**
     * reset duck to flying start state
     */
    public void takeOff(){
        setPosition(100+(int)(Math.random()*300), 300);
        selectMove(Math.random()>0.5, true);

        if(getSpeed().getY() < (-speed/2))
            anims.select(getSpeed().getX()>0.0f ? "RIGHTUP" : "LEFTUP");
        else
            anims.select(getSpeed().getX()>0.0f ? "RIGHT" : "LEFT");
        anims.randomizePhases();
        state = DuckState.FLYING;
        sounds.play("FLAP");
    }

    /**
     * kill duck ;)
     */
    private void kill(){
        // we can kill only flying duck
        if( state != DuckState.FLYING )
            return;

        game.setDuckState(id, GameInfo.HUDDuckState.DIED);
        state = DuckState.SCARED;
        anims.select(getSpeed().getX()>0? "SCAREDRIGHT" : "SCAREDLEFT");
        scaredTimer = 0;
        setSpeed(0,0);
        sounds.stopAll();
        sounds.play("FALL");
    }

    /**
     * duck fly away
     */
    public void flyAway(){
        if( state != DuckState.FLYING)
            return;

        game.setDuckState(id, GameInfo.HUDDuckState.CLEAR);
        state = DuckState.FLYAWAY;
        setSpeed(0.0f, -speed);
        anims.select("FLYAWAY");
    }

    /**
     * update ducks AI
     */
    @Override public void update(){
        super.update();
        
        switch(state){
            case FLYING:
                updateFly();
                break;
            case FALLING:
                updateFall();
                break;
            case FLYAWAY:
                updateFlyAway();
                break;
            case SCARED:
                updateScared();
                break;
        }
    }

    /**
     * updates fly away duck
     */
    private void updateFlyAway(){
        if(getPosition().getIntY()< -anims.getFrame().getHeight()){
            state = DuckState.DIED;
            setSpeed(0.0f, 0.0f);
            sounds.stopAll();
        }
    }

    /**
     * update scared duck
     */
    private void updateScared(){
        if(++scaredTimer < 8)
            return;

        state = DuckState.FALLING;
        setSpeed(0.0f, 10.0f);
        anims.select("FALL");
    }

    /**
     * updates falling duck
     */
    private void updateFall(){
        if(getPosition().getIntY()> (bottomEdge+40)){
            state = DuckState.DIED;
            setSpeed(0.0f, 0.0f);
            sounds.stopAll(); // stops falling sound
            sounds.play("SPLAT");
        }
    }

    /// when duck is died, it is ready to remove
    @Override public boolean removeReady(){
        return state==DuckState.DIED;
    }

    /**
     * updates flying duck
     */
    private void updateFly(){
        Vector2D pos = getPosition(),
                 spd = getSpeed();
  
        if( pos.getIntY() < topEdge && spd.getY() < 0.0f ){ // upper edge?
            selectMove(spd.getX()>0.0f, false);
            return;
        }

        if( pos.getIntY() > bottomEdge && spd.getY() > 0.0f ){ // bottom edge?
            selectMove(spd.getX()>0.0f, true);
            return;
        }

        if( pos.getIntX() < leftEdge && spd.getX() < 0.0f ){ // left edge?
            selectMove(true, spd.getY()<0.0f);            
            return;
        }

        if( pos.getIntX() > rightEdge && spd.getX() > 0.0f ){ // rigth edge?
            selectMove(false, spd.getY()<0.0f);
            return;
        }

        // randomize Y
        if( Math.random() < 1e-2 )
            selectMove(spd.getX()>0, spd.getY()>0);
        // randomize X
        if( Math.random() < 1e-2 )
            selectMove(spd.getX()<0, spd.getY()<0);        
    }
    
    /**
     * set speed vector in desired direction
     *
     * @param right should go right?
     * @param up should go up?
     */
    private void selectMove(boolean right, boolean up){
        // select random angle between 10 and 60 deg
        float angle = 10.0f + (float)Math.random()*50.0f;

        // correct in right direction
        angle = up    ? -angle : angle;
        angle = right ?  angle : 180.0f-angle;

        // use correct animation
        if(right)
            anims.select("RIGHTUP");
        else
            anims.select("LEFTUP");

        // set as new speed
        setSpeed(Vector2D.createPolar(angle, speed));
    }

    /**
     * what we are doing?
     *
     * @return duck state
     */
    public DuckState getState(){
        return state;
    }

    /**
     * try shot the duck
     *
     * @param x x global coordinate
     * @param y y global coordinate
     *
     * @return if duck is shotted, then score, else 0
     */
    public int shot(int x, int y){
        Vector2D pos = getPosition();
        int imgW = anims.getFrame().getWidth();
        int imgH = anims.getFrame().getHeight();
        
        if( x>pos.getIntX()        && y>pos.getIntY() &&
            x<(pos.getIntX()+imgW) && y<(pos.getIntY()+imgH) &&
            state == DuckState.FLYING){
            kill();
            return score;
        }

        return 0;
    }
}
