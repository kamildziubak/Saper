
public class Mine 
{
	Boolean detonated;
	Cord cords;
	
	public Mine(Cord cords)
	{
		this.cords=cords;
		detonated=false;
	}
	
	public Cord getCords()
	{
		return cords;
	}
}
