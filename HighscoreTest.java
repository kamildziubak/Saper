import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HighscoreTest {

    @Test
    public void checkScoreTest(){
        Map<Float, String> scores = new HashMap<Float, String>();
        for(Integer i=1000; i<1200; i++)
            scores.put(Float.valueOf(i.toString()), "ERWIN");
        Highscore highscore = new Highscore(10, 10, 10);
        highscore.setScores(scores);

        System.out.println(highscore.getScores());

        assertTrue(highscore.checkScore(9f));
        assertFalse(highscore.checkScore(749328749821f));
    }

    @Test
    public void getSortedScoresTest(){
        Map<Float, String> scores = new HashMap<Float, String>();
        for(Integer i=10; i>0; i--)
            scores.put(Float.valueOf(i.toString()), "ERWIN");
        Highscore highscore = new Highscore(10, 10, 10);
        highscore.setScores(scores);

        Score[] sortedScores = highscore.getSortedScores(highscore);
        for(Score score:sortedScores)
            System.out.println(score.getTime());
    }

    @Test
    public void insertScoreTest(){
        Map<Float, String> scores = new HashMap<Float, String>();
        for(Integer i=10; i>0; i--)
            scores.put(Float.valueOf(i.toString()), "ERWIN");
        Highscore highscore = new Highscore(10, 10, 10);
        highscore.setScores(scores);

        highscore.insertScore("Kamil", 5.5f);
        for(Score score: highscore.getSortedScores(highscore))
            System.out.println(score.getTime());
    }
}
