/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package duckhunt;

import gameFramework.*;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 *
 * @author net
 */
public class Game extends Screen{
    /// how many duck we need to pass?
    public static final int ducksForNextRound = 6;

    /// current game data
    private GameInfo game;

    /// Head Up Display
    private HUD hud;

    /// dog :)
    private Dog dog;

    /// rendering list for ducks
    private DuckList ducks;
    
    /// world picture (ground, grass, tree...)
    private Picture world;

    /// current state timer
    private long stateTime;

    /// game type - one or two ducks
    private int gameDucks;

    /// default background color (blue)
    protected static final Color defaultBackground = new Color(0x64, 0xb0, 0xff),
    /// default fly away background color (light red)
                                 flyAwayBackground = new Color(0xff, 0xcc, 0xc5);

    /// ducks per three shots
    private int ducksNow=0;

    /// score rendering list
    private RenderList scoreRender;

    /// game sounds
    private SoundMap sounds;

    /// outro sound played?
    private boolean outroPlayed=false;

    /**
     * game constructor
     *
     * @param view panel  to use as game rendering output
     * @param gameType how many ducks?     
     * @throws java.io.IOException
     */
    public Game(JPanel view, int gameType) throws java.io.IOException {
        super(view, 20, view.getWidth(), view.getHeight());

        game = new GameInfo();

        hud = new HUD(game);
        updateList.add(hud);

        dog = new Dog(game);
        updateList.add(dog);
        dog.goHunt();

        ducks = new DuckList();
        updateList.add(ducks);                
        
        world = new Picture("/images/world.png");

        sounds = new SoundMap();
        try{
            sounds.put("START", new MidiSound("/sounds/levelStart.mid"));
            sounds.put("COMPLETE", new MidiSound("/sounds/levelComplete.mid"));
            sounds.put("SHOT", new MultiSound("/sounds/shot.wav", 3));
        }catch(Exception e){
            System.out.println(" * error during game sounds loading - "+e.getMessage());
        }

        stateTime = 0;
        gameDucks = gameType;
        scoreRender = new RenderList();
    }

    /**
     * do all rendering things
     * 
     * @param g Graphics2D to which we will draw
     */
    public void render(Graphics2D g){
        g.setColor(game.getState()==GameInfo.GameState.FLYAWAY ?
                   flyAwayBackground : defaultBackground);
        g.fillRect(0, 0, getWidth(), getHeight());

        if((game.getState() != GameInfo.GameState.INTRO) ||
            ( (game.getState() == GameInfo.GameState.INTRO) && dog.getState()==Dog.DogState.JUMPDOWN))
            dog.render(g);

        ducks.render(g);

        g.drawImage(world.getImage(), 0, 0, null);

        if((game.getState() == GameInfo.GameState.INTRO) &&
           !( (game.getState() == GameInfo.GameState.INTRO) && dog.getState()==Dog.DogState.JUMPDOWN))
            dog.render(g);

        scoreRender.renderAll(g);
        hud.render(g);        
    }

    /**
     * start duck at basic place
     *
     * @throws java.io.IOException
     */
    protected void addDuck() throws IOException {
        Duck duck;

        if(Math.random() < 0.1)
            duck = new Duck("red", 1500, game);
        else
            if(Math.random() < 0.5)
                duck = new Duck("blue", 1000, game);
            else
                duck = new Duck("black", 500, game);

        ducks.add(duck);

        System.out.println(" * adding duck");
    }
    
    /**
     * players wants to shot
     *
     * @param x horizontal coordinate of mouse
     * @param y vertical coordinate of maouse
     */
    public void shot(int x, int y){        
        if(game.getBulletsLeft() == 0 || game.getState()!=GameInfo.GameState.PLAYING)
            return;

        sounds.play("SHOT");
        game.setBulletsLeft(game.getBulletsLeft()-1);
        int score = ducks.shot(x, y);
        if(score>0){
            game.setScore(score+game.getScore());
            new ScoreText(updateList, scoreRender, ducks.lastDuckPosition(), score);
            ducksNow++;
        }

        // if no bullets left, let all ducks fly away
        if(game.getBulletsLeft() == 0)
            if(ducks.flyAwayAll())
                game.setState(GameInfo.GameState.FLYAWAY);
    }

    /// update game
    @Override public void update(){
        super.update();
        stateTime++;

        switch(game.getState()){
            case INTRO:
                updateIntro();
                break;
            case PLAYING:
            case FLYAWAY:
                updatePlay();
                break;
            case DOGSTATE:
                updateDogState();
                break;
            case OUTRO:
                updateOutro();
                break;
            case GAMEOVER:
                if(stateTime > 100)
                    game.setState(GameInfo.GameState.DONE);
                break;
        }
    }

    /**
     * updates while waiting for dog
     */
    public void updateDogState(){
        if(!dog.isHidden())
            return;

        if(game.getDucksInGame()==GameInfo.ducksInRound){
            stateTime=0;
            game.setState(GameInfo.GameState.OUTRO);            
        }else{
            addGameDucks();
            game.setState(GameInfo.GameState.PLAYING);
            hud.findBadDuck();
        }
    }

    /**
     * updates for outro (ducks counting)
     */
    public void updateOutro(){
        if(hud.getState() == HUD.HUDState.BLINKING && !sounds.isPlaying("COMPLETE") && !outroPlayed){
            sounds.play("COMPLETE");
            outroPlayed = true;
        }

        // still counting ducks in outro?
        if(hud.getState() != HUD.HUDState.NORMAL)
            return;


        if(game.getShottedDucks() >= ducksForNextRound){ // OK, next round
            stateTime=0;
            game.setRound(game.getRound()+1);
            game.clearDucks();
            game.setState(GameInfo.GameState.INTRO);
            dog.goHunt();
        }else{ // so bad, then game over
            stateTime=0;
            game.clearDucks();
            game.setState(GameInfo.GameState.GAMEOVER);
            dog.showDucks(0);
        }
    }
    /**
     * add ducks, depends of game type
     */
    private void addGameDucks(){
        for(int i=0; i<gameDucks; i++)
            try{
                addDuck();
            }catch(java.io.IOException e){
                System.out.println(" * duck load error");
            }
    }

    /// update game intro
    public void updateIntro(){
        if(stateTime == 1)
            sounds.play("START");

        if(!dog.isHidden())
            return;

        addGameDucks();
        game.setState(GameInfo.GameState.PLAYING);        
        stateTime=0;
        outroPlayed=false;
    }

    /// update gameplay
    public void updatePlay(){
       if(ducks.getSize() == 0){ // ducks are killed
            game.setBulletsLeft(3);
            game.setState(GameInfo.GameState.DOGSTATE);
            stateTime = 0;
            dog.showDucks(ducksNow);
            ducksNow=0;
            return;            
       }

       if(stateTime>200){
           if(ducks.flyAwayAll())
               game.setState(GameInfo.GameState.FLYAWAY);
       }
    }

    /// @return game is done?
    public boolean done(){
        return game.getState() == GameInfo.GameState.DONE;
    }

    /// @return game score
    public int getScore(){
        return game.getScore();
    }
}
