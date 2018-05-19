/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package duckhunt;

import java.io.*;

/**
 *
 * @author Peter
 */
public class TopScore {
    /// top score file name
    private static final String topFileName = "./top.score";

    /**
     * return top score, if not created yet, then 0
     *
     * @return return top score, if not created yet, then 0
     */
    public int getTop(){

        try{
            ScoreDB db = new ScoreDB();
            int score = db.getScore();
            if(score<0)
                throw new Exception("empty table");
            return score;
            
        }catch(Exception e){
            System.out.println(" * can't load score from DB : "+e.getLocalizedMessage());
        }       

        if(!(new File(topFileName)).exists())
            return 0;

        int ret=0;
        DataInputStream input;
        try{
            input = new DataInputStream(
                        new BufferedInputStream(
                           new FileInputStream(topFileName)));

            ret = input.readInt();
            input.close();
        }catch(IOException ex){
            System.out.println(" * top score file writing failed : "+ex.getLocalizedMessage());
        }

        return ret;        
    }

    /**
     * add top score, if value is higher, than older
     *
     * @param score new top score
     */
    public void addTop(int score){
        if(score<getTop())
            return;

        try{
            ScoreDB db = new ScoreDB();
            db.setScore(score);
            return;
        }catch(Exception e){
            System.out.println(" * can't save score to DB : "+e.getLocalizedMessage());
        }

        DataOutputStream output;
        try{
            output = new DataOutputStream(
                        new BufferedOutputStream(
                           new FileOutputStream(topFileName)));

            output.writeInt(score);
            output.close();
        }catch(IOException e){
            System.out.println(" * top score file writing failed");
        }       
    }
}
