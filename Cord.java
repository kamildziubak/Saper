
public class Cord 
{
	int x, y;
	
	public Cord(int x, int y)
	{
		this.x=x;
		this.y=y;
	}
	
	public int[] getCords()
	{
		int [] cords = new int[2];
		cords[0]=x;
		cords[1]=y;
		return cords;
	}
	
	public Boolean equals(Cord cords)
	{
		if (cords.getX()==x)
		{
			if (cords.getY()==y)
			{
				return true;
			}
		}
		return false; 
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
}
