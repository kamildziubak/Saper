
public class Field 
{
	String type;
	Boolean covered;
	Boolean flagged;
	
	public Field(String type)
	{
		this.type = type;
		covered=true;
		flagged=false;
	}
	
	public Field()
	{
		this("empty");
	}
	
	public String getType()
	{
		return type;
	}
	
	public Boolean getCovered()
	{
		return covered;
	}
	
	public void setType(String type)
	{
		this.type=type;
	}
	
	public void uncover()
	{
		covered=false;
	}
	
	public Boolean getFlag()
	{
		return flagged;
	}
	
	public void changeFlag()
	{
		if (flagged==true)
			flagged=false;
		else
			flagged=true;
	}
	
}
