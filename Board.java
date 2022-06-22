import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class Board extends JComponent
{
	int x, y, xmines, ymines;
	Field[][] board;
	
	public Board(int x, int y, int xmines, int ymines, Field[][] board)
	{
		this.x=x;
		this.y=y;
		this.xmines=xmines;
		this.ymines=ymines;
		this.board=board;
	}
	
	public void paintComponent(Graphics g)
	{
		int xMineSize=Math.round(x/xmines);
		int yMineSize=Math.round(y/ymines);
		
		Graphics2D g2 = (Graphics2D) g;
		Rectangle2D.Double backgroundrect = new Rectangle2D.Double(0, 0, x-x%xMineSize, y-y%yMineSize);
		g2.setColor(Color.white);
		g2.fill(backgroundrect);
		
		
		g2.setColor(Color.black);
		
		for (int i=0; i<x; i=i+xMineSize)
		{
			g2.draw(new Line2D.Double(i, 0, i, y-y%yMineSize));
		}
		
		for (int i=0; i<=y; i=i+yMineSize)
		{
			g2.draw(new Line2D.Double(0, i, x-x%xMineSize, i));
		}
		
		Rectangle2D.Double[][] field = new Rectangle2D.Double[xmines][ymines];
		
		g2.setColor(Color.red);
		
		for (int yy = 0; yy<ymines; yy++)
		{
			for (int xx=0; xx<xmines; xx++)
			{
				field[xx][yy]=new Rectangle2D.Double(xx*xMineSize+1, yy*yMineSize+1, xMineSize-1, yMineSize-1);
				if (board[xx][yy].getCovered()==true)
				{
					if (board[xx][yy].getFlag()==true)
						g2.setColor(Color.ORANGE);
					else
						g2.setColor(Color.gray);
					g2.fill(field[xx][yy]);
				}
				else {
					if (board[xx][yy].getType().equals("mine"))
					{
						g2.setColor(Color.red);
						g2.fill(field[xx][yy]);
					}
					else if (board[xx][yy].getType().equals("empty")==false)
					{
						int font;
						if (xMineSize<yMineSize)
							font=xMineSize;
						else
							font=yMineSize;
						g2.setFont(new Font("Times New Roman", Font.BOLD, font));
						g2.setColor(Color.blue);
						g2.drawString(board[xx][yy].getType(), xx*xMineSize, yMineSize*yy+yMineSize);
					}
				}
			}
		}
		
	}
}
