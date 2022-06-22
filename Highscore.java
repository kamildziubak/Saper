import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Highscore 
{
	int x, y, mines;
	Map<Float, String> scores;
	
	public Highscore(int x, int y, int mines)
	{
		this.x=x;
		this.y=y;
		this.mines=mines;
		scores=new HashMap<Float, String>();
	}

	public Boolean checkScore(Float time){
		Set<Float> times = scores.keySet();
		if(times.size()<10){
			return true;
		}
		for(Float score:times){
			if(score>time)
				return true;
		}
		return false;
	}

	public static Score[] getSortedScores(Highscore scores){
		Set<Float> times = scores.getScores().keySet();
		List<Float> timesList = new ArrayList<Float>();

		if(scores.getScores().size()<10){
			for(int i=0; i<(10-scores.getScores().size()); i++)
				timesList.add(999f+i);
		}

		for(Float time:times)
			timesList.add(time);

		Collections.sort(timesList);

		Score[] timesArray = new Score[10];

		for(int i=0; i<10; i++)
			timesArray[i] = new Score(scores.getScores().get(timesList.get(i)),timesList.get(i));
		return timesArray;
	}

	public void insertScore(String name, Float time){
		if(checkScore(time)){
			Score[] scores = getSortedScores(this);
			this.scores.remove(scores[9].getTime());
			this.scores.put(time, name);
		}
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getMines() {
		return mines;
	}

	public void setMines(int mines) {
		this.mines = mines;
	}

	public Map<Float, String> getScores() {
		return scores;
	}

	public void setScores(Map<Float, String> scores) {
		this.scores = scores;
	}

	@Override
	public String toString(){
		String output = x+"x"+y+", " + mines;
		for(Float score: scores.keySet())
			output = output + "\n" + score;
		return output;
	}
}
