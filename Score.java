
public class Score {
	String name;
	Float time;
	
	public Score(String name, Float time)
	{
		this.name=name;
		this.time=time;
	}
	
	public Float getTime()
	{
		return time;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String toString()
	{
		return name + ", " + String.valueOf(time);
	}
}
