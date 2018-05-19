/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package duckhunt;

/**
 *
 * @author Peter
 */
public class GameInfo {
    /// how many ducks are in one game round
    public static final int ducksInRound = 10;

    /// possible general duck states
    public enum HUDDuckState { CLEAR, HUNTING, DIED };

    /// all ducks in round states
    private HUDDuckState[] ducksStates = new HUDDuckState[ducksInRound];

    /// count ducks used in game(round)
    private int ducksInGame = 0;


    /// constructor
    public GameInfo(){
        clearDucks();
    }

    /**
     * gets numbers of played ducks
     *
     * @return ducks in game
     */
    public int getDucksInGame(){
        return ducksInGame;
    }

    /**
     * add hunted duck
     *
     * @return index of duck
     */
    public int addHuntedDuck(){
        setDuckState(ducksInGame, HUDDuckState.HUNTING);
        return ducksInGame++;
    }

    /// set all ducks state to clear
    public void clearDucks(){
        ducksInGame = 0;

        for(int i=0; i<ducksStates.length; i++)
            ducksStates[i] = HUDDuckState.CLEAR;
    }

    /**
     * get amount of shotted ducks
     *
     * @return how many ducks died?
     */
    public int getShottedDucks(){
        int ret=0;

        for(int i=0; i<ducksStates.length; i++)
            if(ducksStates[i]!=HUDDuckState.CLEAR)
                ret++;

        return ret;
    }

    /**
     * set one ducks state
     *
     * @param duck index of duck
     * @param state it's new state
     */
    public void setDuckState(int duck, HUDDuckState state){
        assert duck>=0 && duck<ducksStates.length;
        
        ducksStates[duck] = state;
    }

    /**
     * get ducks state
     *
     * @param duck index of duck
     * @return it's state
     */
    public HUDDuckState getDuckState(int duck){
        assert duck>=0 && duck<ducksStates.length;
        return ducksStates[duck];
    }

    /**
     * @return the state
     */
    public GameState getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(GameState state) {
        this.state = state;
    }
    /// game states
    public enum GameState { INTRO, PLAYING, FLYAWAY, DOGSTATE, OUTRO, GAMEOVER, DONE };

    /// current game state
    private GameState state = GameState.INTRO;

    /// game round
    private int round       = 1;

    /// players score
    private int score       = 0; 
    
    /// bullets left
    private int bulletsLeft = 3;

    /**
     * @return the round
     */
    public int getRound() {
        return round;
    }

    /**
     * @param round the round to set
     */
    public void setRound(int round) {
        this.round = round;
    }

    /**
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(int score) {
        if(score>999999) // max displayable score
            score = 999999;

        this.score = score;
    }

    /**
     * @return the duckSpeed
     */
    public float getDuckSpeed() {
        return 7+(float)round;
    }

    /**
     * @return the bullets
     */
    public int getBulletsLeft() {
        return bulletsLeft;
    }

    /**
     * @param bullets the bullets to set
     */
    public void setBulletsLeft(int bullets) {
        this.bulletsLeft = bullets;
    }


}
