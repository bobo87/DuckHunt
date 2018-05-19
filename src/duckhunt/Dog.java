/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package duckhunt;

import gameFramework.*;

/**
 *
 * @author Peter
 */
public class Dog extends Actor{
    /// current game information
    private GameInfo game;

    /// is dog states
    public enum DogState { HIDDEN, GOUP, STILL, GODOWN, GO, GO2, NUZZLE1, NUZZLE2, CLUE, JUMPUP, JUMPDOWN };

    /// current dog state
    private DogState state;

    /// current state time
    private long stateTime;

    /// dogs default position
    private static final Vector2D defaultPos = new Vector2D(230,300),

    /// hunting start position
        huntingPos = new Vector2D(20,270);

    /// dogs top when showed
    private static final int topLine = 220;

    /// creates new dog
    public Dog(GameInfo game) throws java.io.IOException{
        super();

        this.game = game;

        // add animations for dog
        anims.add(new Animation("/images/dog/laugh.png",    2, 3),  "LAUGH");
        anims.add(new Animation("/images/dog/oneDuck.png",  1, 100), "ONEDUCK");
        anims.add(new Animation("/images/dog/twoDucks.png", 1, 100), "TWODUCKS");
        anims.add(new Animation("/images/dog/go.png", 4, 3), "GO");
        anims.add(new Animation("/images/dog/nuzzle.png", 2, 3), "NUZZLE");
        anims.add(new Animation("/images/dog/clue.png", 1, 100), "CLUE");
        anims.add(new Animation("/images/dog/jump.png", 2, 30), "JUMP");

        try{
            sounds.put("LAUGH", new SimpleSound("/sounds/laugh.wav"));
            sounds.put("APPLAUSE", new SimpleSound("/sounds/applause.wav"));
            sounds.put("BARK", new SimpleSound("/sounds/dogbrk.wav"));
        }catch(Exception e){
            System.out.println(" * error loading dog sounds");
        }

        state = DogState.HIDDEN;
        stateTime = 0;

        setPosition(defaultPos);
    }
    
    /// update dog
    @Override public void update(){
        super.update();
        stateTime++;

        switch(state){
            case GOUP:
                if(getPosition().getIntY() <= topLine){
                    stateTime = 0;
                    state = DogState.STILL;
                    setSpeed(0,0);
                }
                break;

            case GODOWN:
                if(getPosition().getIntY() >= defaultPos.getIntY()){
                    stateTime = 0;
                    state = DogState.HIDDEN;
                    setSpeed(0,0);
                }
                break;

            case STILL:
                if(stateTime >= 10 && game.getState()!=GameInfo.GameState.GAMEOVER){
                    stateTime = 0;
                    state = DogState.GODOWN;
                    setSpeed(0, 4);
                }
                break;

            default:
                updateGo();
                break;
        }
    }

    /**
     * update going, jumping...
     */
    public void updateGo(){

        switch(state){
            case GO:
                if(getPosition().getX() >= 60){
                    stateTime=0;
                    state=DogState.NUZZLE1;
                    anims.select("NUZZLE");
                    setSpeed(0,0);
                }
                break;

            case NUZZLE1:
                if(stateTime>30){
                    stateTime=0;
                    state=DogState.GO2;
                    setSpeed(2,0);
                    anims.select("GO");
                }
                break;

            case GO2:
                if(getPosition().getX() >= 120){
                    stateTime=0;
                    state=DogState.NUZZLE2;
                    anims.select("NUZZLE");
                    setSpeed(0,0);
                }
                break;

            case NUZZLE2:
                if(stateTime>30){
                    stateTime=0;
                    state=DogState.CLUE;
                    anims.select("CLUE");
                }
                break;

            case CLUE:
                if(stateTime>10){
                    stateTime=0;
                    state=DogState.JUMPUP;
                    setSpeed(4f, -6f);
                    anims.select("JUMP");
                    sounds.play("BARK");
                }
                break;

            case JUMPUP:
                if(getPosition().getY() <= topLine-40){
                    state=DogState.JUMPDOWN;
                    stateTime=0;
                    setSpeed(4f, 6f);
                }
                break;

            case JUMPDOWN:
                if(getPosition().getY() >= defaultPos.getY()){
                    state=DogState.HIDDEN;
                    stateTime=0;
                    setSpeed(0,0);
                }
                break;
        }       
    }

    /**
     * is dog hidden?
     * 
     * @return ture, if dog is hidden
     */
    public boolean isHidden(){
        return state==DogState.HIDDEN;
    }

    /**
     * gets dog state
     * 
     * @return current dog state
     */
    public DogState getState(){
        return state;
    }

    /**
     * render dog if it is not hidden
     *
     * @param g graphics
     */
    @Override public void render(java.awt.Graphics2D g){
        if(!isHidden())
            super.render(g);
    }

    /**
     * show shotted ducks
     *
     * @param count how many ducks are shotted
     */
    public void showDucks(int count){
        stateTime = 0;
        state = DogState.GOUP;
        anims.select(count==0? "LAUGH" : count==1? "ONEDUCK" : "TWODUCKS");
        setPosition(defaultPos);
        setSpeed(0, -4);        
        sounds.play(count==0? "LAUGH" : "APPLAUSE");
    }

    /**
     * start hunting ducks ;)
     */
    public void goHunt(){
        stateTime = 0;
        state = DogState.GO;
        anims.select("GO");
        setPosition(huntingPos);
        setSpeed(2,0);
    }
}
