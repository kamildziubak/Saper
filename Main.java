import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.text.NumberFormatter;

public class Main extends JFrame implements MouseListener, ActionListener{
	static JOptionPane opHighscores;
	static JDialog dHighscores;
	static Map<String, String> settings;
	static JComboBox cHighscores;
	static String lHighscores;
	static List<Highscore> highscores;
	static int x, y, minesNumber, width, height;
	static Cord[][] positionsMap;
	static Field[][] board;
	static Board drawnBoard;
	static Main window;
	static Language language;
	static Boolean started, lost, won;
	static ComponentListener listener;
	static JMenuBar menuBar;
	static JMenu game, help;
	static JMenuItem newGame, mhighscores, msettings, exit, about;
	static double version;
	static long startTime;
	
	public Main(HashMap<String, String> language, Board board, int x, int y)
	{
		setSize(width+20, height +70);
		setTitle((String)language.get("TITLE"));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		menuBar = new JMenuBar();
		add(menuBar);
		setJMenuBar(menuBar);
		
		game = new JMenu();
		game.setText((String) language.get("GAME"));
		menuBar.add(game);
		
		help = new JMenu();
		help.setText((String) language.get("HELP"));
		menuBar.add(help);
		
		board.setBounds(0,0, width, height);
		add(board);
		setVisible(true);
		addMouseListener(this);
		setLocation(x, y);
		setMinimumSize(new Dimension(this.x*35, this.y*35));
		
		newGame = new JMenuItem((String) language.get("NEW_GAME"));
		game.add(newGame);
		newGame.addActionListener(this);
		
		mhighscores = new JMenuItem((String) language.get("HIGHSCORES"));
		game.add(mhighscores);
		mhighscores.addActionListener(this);
		
		msettings = new JMenuItem((String) language.get("SETTINGS"));
		game.add(msettings);
		msettings.addActionListener(this);
		
		game.addSeparator();
		
		exit = new JMenuItem((String) language.get("EXIT"));
		game.add(exit);
		exit.addActionListener(this);
		
		about = new JMenuItem((String) language.get("ABOUT"));
		help.add(about);
		about.addActionListener(this);
		
		setLayout(null);
	}
	
	public static void generateBoard()
	{
		board = new Field[x][y];
		for (int xx=0; xx<x; xx++)
		{
			for (int yy=0; yy<y; yy++)
			{
				board[xx][yy]=new Field();
			}
		}
		
		Mine[] mines = new Mine[minesNumber];
		
		int i=0;
		if((x*y)/2>minesNumber)
		{
		while(i<minesNumber) {
			Random random = new Random();
			
			int hypoX=random.nextInt(x);
			int hypoY=random.nextInt(y);
			Cord hypoCord = new Cord(hypoX, hypoY);
			
			Boolean repetitionFound=false;
			
			for (int ii=0; ii<i; ii++)
			{
				if (hypoCord.equals(mines[ii].getCords()))
				{
					repetitionFound=true;
				}
			}
			
			if (repetitionFound==false)
			{
				mines[i]= new Mine(hypoCord);
				i++;
			}
			else
			{
				i--;
			}
		}
		}
		else
		{
			while(i<(x*y-minesNumber)) {
				for (int xx=0; xx<x; xx++)
				{
					for (int yy=0; yy<y; yy++)
						board[xx][yy].setType("mine");
				}
				
				Random random = new Random();
				
				int hypoX=random.nextInt(x);
				int hypoY=random.nextInt(y);
				Cord hypoCord = new Cord(hypoX, hypoY);
				
				Boolean repetitionFound=false;
				
				for (int ii=0; ii<i; ii++)
				{
					if (hypoCord.equals(mines[ii].getCords()))
					{
						repetitionFound=true;
					}
				}
				
				if (repetitionFound==false)
				{
					mines[i]= new Mine(hypoCord);
					i++;
				}
				else
				{
					i--;
				}
			}
		}
		if((x*y)/2>minesNumber)
		{
			for (i=0; i<minesNumber; i++)
			{
				Cord cords = mines[i].getCords();
				board[cords.getX()][cords.getY()].setType("mine");
			}
		}
		else
		{
			for (i=0; i<((x*y)-minesNumber); i++) {
				Cord cords = mines[i].getCords();
				board[cords.getX()][cords.getY()].setType("empty");
		}
		}
		
		for (int yy=0; yy<y; yy++)
		{
			Boolean upLimited=false;
			Boolean downLimited=false;
			
			if (yy==0)
			{
				upLimited=true;
			}
			if(yy==y-1)
			{
				downLimited=true;
			}
			
			
			for (int xx=0; xx<x; xx++)
			{	
				Boolean leftLimited=false;
				Boolean rightLimited=false;
				
				int mineCounter=0;
				if(board[xx][yy].getType().equals("mine")==false)
				{	
					if (xx==0)
					{
						leftLimited=true;
					}
					if (xx==x-1)
					{
						rightLimited=true;
					}

						//góra
						
						if (upLimited==false)
						{
							//góra-lewo
							if (leftLimited==false)
							{
								if (board[xx-1][yy-1].getType().equals("mine"))
								{
									mineCounter++;
								}
							}
							//góra
							if (board[xx][yy-1].getType().equals("mine"))
							{
								mineCounter++;
							}
							
							//góra-prawo
							
							if (rightLimited==false)
							{
								if (board[xx+1][yy-1].getType().equals("mine"))
								{
									mineCounter++;
								}
							}
						}
						
						//lewo
						
						if (leftLimited==false)
						{
							if (board[xx-1][yy].getType().equals("mine"))
							{
								mineCounter++;
							} 
						}
						
						//dó³
						
						if (downLimited==false)
						{
							//dó³-lewo
							if (leftLimited==false)
							{
								if (board[xx-1][yy+1].getType().equals("mine"))
								{
									mineCounter++;
								} 
							}
							
							//dó³
							
							if (board[xx][yy+1].getType().equals("mine"))
							{
								mineCounter++;
							} 
							
							//dó³ prawo
							
							if (rightLimited==false)
							{
								if (board[xx+1][yy+1].getType().equals("mine"))
								{
									mineCounter++;
								} 
							}
						}
						
						//prawo
						
						if (rightLimited==false)
						{
							if (board[xx+1][yy].getType().equals("mine"))
							{
								mineCounter++;
							} 
						}
					
					if (mineCounter>0)
					{
						board[xx][yy].setType(Integer.toString(mineCounter));
					}
				}
			}
		}
	}
	
	public static void generateMap()
	{
		Insets tb = window.getInsets();
		tb.top = tb.top + menuBar.getHeight();
		positionsMap = new Cord[width+tb.left][height+tb.top];
		
		for (double yy=0; yy<height; yy++)
		{
			for (double xx=0; xx<width; xx++)
			{
				double dwidth = width;
				double dheight = height;
				double dx = x;
				double dy = y;
				positionsMap[(int) Math.round(xx+tb.left)][(int) Math.round(yy+tb.top)] = new Cord((int)Math.floor(((xx)/dwidth)*dx), (int)Math.floor(((yy)/dheight)*dy));
			}
		}
	}
	
	public static void uncoverEmptyFields()
	{
		Boolean nothingFound;
		do {
		nothingFound=true;
		for (int yy=0; yy<y; yy++)
		{
		for (int xx=0; xx<x; xx++)
		{
			if (board[xx][yy].getCovered()==false)
			{
			if (board[xx][yy].getType()=="empty")
			{
			Boolean upLimited=false;
			Boolean downLimited=false;
			Boolean leftLimited=false;
			Boolean rightLimited=false;
			
			if (yy==0)
			{
				upLimited=true;
			}
			if(yy==y-1)
			{
				downLimited=true;
			}
			
			if (xx==0)
			{
				leftLimited=true;
			}
			if (xx==x-1)
			{
				rightLimited=true;
			}
			
			if (upLimited==false)
			{
				//góra-lewo
				if (leftLimited==false)
				{
					if (board[xx-1][yy-1].getType().equals("mine")==false)
					{
						if (board[xx-1][yy-1].getCovered()==true) {
						board[xx-1][yy-1].uncover();
						nothingFound=false;
						}
					}
				}
				//góra
				if (board[xx][yy-1].getType().equals("mine")==false)
				{
					if (board[xx][yy-1].getCovered()==true) {
					board[xx][yy-1].uncover();
					nothingFound=false;
					}
				}
				
				//góra-prawo
				
				if (rightLimited==false)
				{
					if (board[xx+1][yy-1].getType().equals("mine")==false)
					{
						if (board[xx+1][yy-1].getCovered()==true) {
						board[xx+1][yy-1].uncover();
						nothingFound=false;
						}
					}
				}
			}
			
			//lewo
			
			if (leftLimited==false)
			{
				if (board[xx-1][yy].getType().equals("mine")==false)
				{
					if (board[xx-1][yy].getCovered()==true) {
					board[xx-1][yy].uncover();
					nothingFound=false;
					}
				} 
			}
			
			//dó³
			
			if (downLimited==false)
			{
				//dó³-lewo
				if (leftLimited==false)
				{
					if (board[xx-1][yy+1].getType().equals("mine")==false)
					{
						if (board[xx-1][yy+1].getCovered()==true) {
						board[xx-1][yy+1].uncover();
						nothingFound=false;
						}
					} 
				}
				
				//dó³
				
				if (board[xx][yy+1].getType().equals("mine")==false)
				{
					if (board[xx][yy+1].getCovered()==true) {
					board[xx][yy+1].uncover();
					nothingFound=false;
					}
				} 
				
				//dó³ prawo
				
				if (rightLimited==false)
				{
					if (board[xx+1][yy+1].getType().equals("mine")==false)
					{
						if (board[xx+1][yy+1].getCovered()==true) {
						board[xx+1][yy+1].uncover();
						nothingFound=false;
						}
					} 
				}
			}
			
			//prawo
			
			if (rightLimited==false)
			{
				if (board[xx+1][yy].getType().equals("mine")==false)
				{
					if (board[xx+1][yy].getCovered()==true) {
					board[xx+1][yy].uncover();
					nothingFound=false;
					}
				} 
			}
		}
		}
		}
		}
		} while(nothingFound==false);
	}

	public static void newGame()
	{
		width=window.getWidth()-20;
		height=window.getHeight()-70;
		generateBoard();
		generateMap();
		window.setVisible(false);
		drawnBoard = new Board(width, height, x ,y, board);
		drawnBoard.repaint();
		window = new Main(language, drawnBoard, window.getX(), window.getY());
		window.setVisible(true);
		won=false;
		lost=false;
		started=false;
		window.addComponentListener(listener);
		
		settings.put("x", String.valueOf(x));
		settings.put("y", String.valueOf(y));
		settings.put("mines", String.valueOf(minesNumber));
		
		try {
			FileWriter writer = new FileWriter("settings.ini");
			for(int i=0; i<settings.size(); i++)
				writer.write(settings.keySet().toArray()[i] + ": " + settings.get(settings.keySet().toArray()[i]) + "\n");
			writer.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void loadHighscores()
	{
		highscores=new ArrayList<>();
		try {
			Scanner scanner = new Scanner(new File("data/highscore.hi"));
			boolean endOfTable=true;
			int i=0;
			while(scanner.hasNextLine())
			{
				String line = scanner.nextLine();
				if(endOfTable)
				{
					int xx=Integer.parseInt(line.substring(0, line.indexOf("x")));
					int yy=Integer.parseInt(line.substring(line.indexOf("x")+1, line.indexOf(",")));
					int mines = Integer.parseInt(line.substring(line.indexOf(",")+1, line.indexOf(":")));
					endOfTable=false;
					highscores.add(new Highscore(xx, yy, mines));
					
					for (int ii=0; ii<10; ii++)
					{
						highscores.get(highscores.size()-1).insertScore("null", (ii+1)*999f);
					}
				}
				else
				{
					highscores.get(i).insertScore(line.substring(line.indexOf(",")+1,  line.indexOf(";")), Float.parseFloat(line.substring(0, line.indexOf(","))));
					if (line.contains(";;"))
					{
						endOfTable=true;
						i++;
					}
					if(line.contains(";;;"))
						break;
				}
			}
		} catch (FileNotFoundException e) {
			
		}
		
		
	}
	
	public static void saveHighscores()
	{
		try {
			FileWriter writer = new FileWriter("data/highscore.hi");
			for(int i=0; i<highscores.size(); i++)
			{
				writer.write(highscores.get(i).getX() + "x" + highscores.get(i).getY()+","+highscores.get(i).getMines() + ":");
				Score[] scores = Highscore.getSortedScores(highscores.get(i));
				for(int ii=0; ii<scores.length; ii++)
				{
					writer.write("\n" + scores[ii].getTime() +"," + scores[ii].getName() +";");
				}
					
				writer.write(";");
				if((i+1)==highscores.size())
				{
					writer.write(";");
					writer.close();
				}
				else
				{
					writer.write("\n");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		started=false;
		lost=false;
		won=false;
		settings = new HashMap<String, String>();
		File settingsFile = new File("settings.ini");
		try {
			Scanner reader = new Scanner(settingsFile, StandardCharsets.UTF_8.name());
			while (reader.hasNextLine()==true){
				String line = reader.nextLine();
				int index = line.indexOf(":");
				settings.put(line.substring(0, index), line.substring(index+2));
			}
		} catch (FileNotFoundException e) {
			try {
				settingsFile.createNewFile();
				FileWriter writer = new FileWriter(settingsFile);
				writer.write("x: 10\ny: 10\nmines: 10\nversion: 1.0\nlang: pl");
				writer.close();
				
				Scanner reader = new Scanner(settingsFile, StandardCharsets.UTF_8.name());
				while (reader.hasNextLine()==true){
					String line = reader.nextLine();
					int index = line.indexOf(":");
					settings.put(line.substring(0, index), line.substring(index+2));
				}
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		
		try
		{
			language = new Language(settings.get("lang"));
			
			x=Integer.valueOf(settings.get("x"));
			y=Integer.valueOf(settings.get("y"));
			minesNumber=Integer.valueOf(settings.get("mines"));
			width=800;
			height=600;
			
			version=Double.valueOf(settings.get("version"));
		}
		catch (Exception e)
		{
			try {
				settingsFile.createNewFile();
				FileWriter writer = new FileWriter(settingsFile);
				writer.write("x: 10\ny: 10\nmines: 10\nversion: 1.0\nlang: pl");
				writer.close();
				
				Scanner reader = new Scanner(settingsFile, StandardCharsets.UTF_8.name());
				while (reader.hasNextLine()==true){
					String line = reader.nextLine();
					int index = line.indexOf(":");
					settings.put(line.substring(0, index), line.substring(index+2));
				}
				language = new Language(settings.get("lang"));
				
				x=Integer.valueOf(settings.get("x"));
				y=Integer.valueOf(settings.get("y"));
				minesNumber=Integer.valueOf(settings.get("mines"));
				width=800;
				height=600;
				
				version=Double.valueOf(settings.get("version"));
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		generateBoard();
		
		drawnBoard = new Board(width, height, x ,y, board);
		window = new Main(language, drawnBoard, 0 ,0);
		
		generateMap();
		
		listener = new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentResized(ComponentEvent arg0) {
				width=window.getWidth()-20;
				height=window.getHeight()-70;
				
				window.remove(drawnBoard);
				drawnBoard = new Board(width, height, x, y, board);
				generateMap();
				drawnBoard.repaint();
				
				JFrame layout = new JFrame();
				
				window.setLayout(layout.getLayout());
				window.add(drawnBoard);
				
				drawnBoard.setVisible(false);
				drawnBoard.setVisible(true);
			}
			
			@Override
			public void componentMoved(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentHidden(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		};
		
		window.addComponentListener(listener);
		
		loadHighscores();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int [] field = positionsMap[e.getX()][e.getY()].getCords();
		if (e.getButton()==1)
		{
			if (!started)
			{
				while (board[field[0]][field[1]].getType()=="mine")
				{
					newGame();
				}
				board[field[0]][field[1]].uncover();
				drawnBoard.repaint();
				started=true;
				startTime=System.currentTimeMillis();
			}
			else
			{
				if (!board[field[0]][field[1]].getFlag())
				{
				board[field[0]][field[1]].uncover();
				if (board[field[0]][field[1]].getType()=="mine")
				{

				}
				else if (board[field[0]][field[1]].getType()=="empty")
				{
					uncoverEmptyFields();
					drawnBoard.repaint();
				}
				else
				{
					int xx = field[0];
					int yy = field[1];
					
					Boolean upLimited=false;
					Boolean downLimited=false;
					Boolean leftLimited=false;
					Boolean rightLimited=false;
					
					if (yy==0)
					{
						upLimited=true;
					}
					if(yy==y-1)
					{
						downLimited=true;
					}
					
					if (xx==0)
					{
						leftLimited=true;
					}
					if (xx==x-1)
					{
						rightLimited=true;
					}
					
					int mineCounter=0;
					
					if (!upLimited)
					{
						//góra-lewo
						if (!leftLimited)
						{
							if (board[xx-1][yy-1].getFlag())
							{
								mineCounter++;
							}
						}
						//góra
						if (board[xx][yy-1].getFlag())
						{
							mineCounter++;
						}
						
						//góra-prawo
						
						if (!rightLimited)
						{
							if (board[xx+1][yy-1].getFlag())
							{
								mineCounter++;
							}
						}
					}
					
					//lewo
					
					if (!leftLimited)
					{
						if (board[xx-1][yy].getFlag())
						{
							mineCounter++;
						} 
					}
					
					//dó³
					
					if (!downLimited)
					{
						//dó³-lewo
						if (!leftLimited)
						{
							if (board[xx-1][yy+1].getFlag())
							{
								mineCounter++;
							} 
						}
						
						//dó³
						
						if (board[xx][yy+1].getFlag())
						{
							mineCounter++;
						} 
						
						//dó³ prawo
						
						if (!rightLimited)
						{
							if (board[xx+1][yy+1].getFlag())
							{
								mineCounter++;
							} 
						}
					}
					
					//prawo
					
					if (!rightLimited)
					{
						if (board[xx+1][yy].getFlag())
						{
							mineCounter++;
						} 
					}
				
				if (mineCounter==Integer.parseInt(board[xx][yy].getType()))
				{
					if (!upLimited) {
					//góra-lewo
					if (!leftLimited)
					{
						if (!board[xx - 1][yy - 1].getFlag())
						{
							board[xx-1][yy-1].uncover();
						}
					}
					//góra
					if (!board[xx][yy - 1].getFlag())
					{
						board[xx][yy-1].uncover();
					}
					
					//góra-prawo
					
					if (!rightLimited)
					{
						if (!board[xx + 1][yy - 1].getFlag())
						{
							board[xx+1][yy-1].uncover();
						}
					}
				}
				
				//lewo
				
				if (!leftLimited)
				{
					if (!board[xx - 1][yy].getFlag())
					{
						board[xx-1][yy].uncover();
					} 
				}
				
				//dó³
				
				if (!downLimited)
				{
					//dó³-lewo
					if (!leftLimited)
					{
						if (!board[xx - 1][yy + 1].getFlag())
						{
							board[xx-1][yy+1].uncover();
						} 
					}
					
					//dó³
					
					if (!board[xx][yy + 1].getFlag())
					{
						board[xx][yy+1].uncover();
					} 
					
					//dó³ prawo
					
					if (!rightLimited)
					{
						if (!board[xx + 1][yy + 1].getFlag())
						{
							board[xx+1][yy+1].uncover();
						} 
					}
				}
				
				//prawo
				
				if (!rightLimited)
				{
					if (!board[xx + 1][yy].getFlag())
					{
						board[xx+1][yy].uncover();
					} 
				}
				}
				}
					drawnBoard.repaint();
			}
		}
			if(lost)
			{
				newGame();
			}
			else if (won)
			{
				newGame();
			}
		}
		
		for (int xx=0; xx<x; xx++)
		{
			for (int yy=0; yy<y; yy++)
			{
				if(board[xx][yy].getType()=="mine")
				{
					if(!board[xx][yy].getCovered())
					{
						if(!lost) {
							for(Field[] f:board)
								for(Field ff:f)
									ff.uncover();
						drawnBoard.repaint();
						long time = System.currentTimeMillis()-startTime;
						JOptionPane.showMessageDialog(window, language.get("GAME_OVER") + " " + time/1000.0 + "s",(String) language.get("GAME_OVER") , JOptionPane.ERROR_MESSAGE, null);
						started=false;
						lost=true;
						}
					}
				}
			}
		}

		uncoverEmptyFields();
		drawnBoard.repaint();
		
		Boolean nonMineFound=false;
		for (int xx=0; xx<x; xx++)
		{
			for (int yy=0; yy<y; yy++)
			{
				if(!board[xx][yy].getType().equals("mine"))
				{
					if(board[xx][yy].getCovered())
					{
						nonMineFound=true;
					}
				}
			}
		}
		if(!nonMineFound)
		{
			if (!lost)
			{
				for(Field[] f:board)
					for(Field ff:f)
						if(!ff.getFlag())
							ff.changeFlag();
				float time = System.currentTimeMillis()-startTime;
				JOptionPane.showMessageDialog(window, language.get("VICTORY") + " " + time/1000.0 + "s", (String) language.get("VICTORY"), JOptionPane.INFORMATION_MESSAGE, null);
				
				manageHighscore(time);
				lost=true;
			}
		}
		
		if (e.getButton()==3) {
			if (board[field[0]][field[1]].getCovered())
				board[field[0]][field[1]].changeFlag();
			drawnBoard.repaint();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==exit)
			System.exit(0);
		if (e.getSource()==newGame)
			newGame();
		if (e.getSource()==msettings)
		{	
			NumberFormat format = NumberFormat.getInstance();
			NumberFormatter formatter = new NumberFormatter(format);
			formatter.setValueClass(Integer.class);
			formatter.setMinimum(1);
			formatter.setMaximum(50);
			formatter.setAllowsInvalid(false);
			formatter.setCommitsOnValidEdit(true);
			
			JFormattedTextField tx = new JFormattedTextField(formatter);
			JFormattedTextField ty = new JFormattedTextField(formatter);
			
			NumberFormatter minesFormatter = new NumberFormatter(format);
			minesFormatter.setValueClass(Integer.class);
			minesFormatter.setMinimum(1);
			minesFormatter.setMaximum(2500);
			minesFormatter.setAllowsInvalid(false);
			minesFormatter.setCommitsOnValidEdit(true);
			
			JFormattedTextField tmines = new JFormattedTextField(minesFormatter);
			
			Object [] values = {
					(String) language.get("X_NUMBER"), tx,
					(String) language.get("Y_NUMBER"), ty,
					(String) language.get("MINES_NUMBER"), tmines
			};
			
			JOptionPane.showConfirmDialog(null, values, (String) language.get("SETTINGS"), JOptionPane.DEFAULT_OPTION);
			try
			{
				if (x * y < (int) tmines.getValue())
					throw new Exception("Invalid data");

				minesNumber = (int) tmines.getValue();
				x = (int) tx.getValue();
				y = (int) ty.getValue();



				newGame();
			}
			catch(Exception exception){
				JOptionPane.showMessageDialog(this, language.get("ERROR"), (String) language.get("ERROR"), JOptionPane.ERROR_MESSAGE);
			}
		}
		if (e.getSource()==about)
		{
			String text = (String) language.get("TITLE") + " " + version + "\n" + language.get("AUTHOR") + ": Kamil Dziubak";
			JOptionPane.showConfirmDialog(window, text,  (String) language.get("ABOUT"), JOptionPane.DEFAULT_OPTION);
		}
		if (e.getSource()==mhighscores)
		{
			if(highscores.size()==0)
				JOptionPane.showMessageDialog(null, language.get("NO_HIGHSCORE"));
			else{
				String[] sizes = new String[highscores.size()];

				for(int i=0; i<highscores.size(); i++)
				{
					sizes[i]=highscores.get(i).getX() + "x" + highscores.get(i).getY() + ", "+ highscores.get(i).getMines() + " " + language.get("MINES");
				}

				cHighscores = new JComboBox<>(sizes);
				cHighscores.addActionListener(this);
				lHighscores = "";

				for (int i=0; i<10; i++)
					if(Highscore.getSortedScores(highscores.get(cHighscores.getSelectedIndex()))[i].toString().contains("null")==false)
						lHighscores = lHighscores + (i+1)+ ". " +  Highscore.getSortedScores(highscores.get(cHighscores.getSelectedIndex()))[i] +"\n";

				Object[] objects = {cHighscores, lHighscores};

				JOptionPane optionPane = new JOptionPane(objects);

				dHighscores= optionPane.createDialog((String) language.get("HIGHSCORES"));
				dHighscores.setVisible(true);
			}
		}
		if(e.getSource()==cHighscores)
		{
			String[] sizes = new String[highscores.size()];
			
			for(int i=0; i<highscores.size(); i++)
			{
				sizes[i]=highscores.get(i).getX() + "x" + highscores.get(i).getY() + ", "+ highscores.get(i).getMines() + " " + language.get("MINES");
			}
			
			int selectedSize = cHighscores.getSelectedIndex();
			
			cHighscores = new JComboBox<>(sizes);
			cHighscores.setSelectedIndex(selectedSize);
			cHighscores.addActionListener(this);
			lHighscores="";
			for (int i=0; i<10; i++)
			{
				if(Highscore.getSortedScores(highscores.get(cHighscores.getSelectedIndex()))[i].toString().contains("null")==false)
					lHighscores = lHighscores + (i+1)+ ". " +  Highscore.getSortedScores(highscores.get(cHighscores.getSelectedIndex()))[i] +"\n";
			}
				
			
			cHighscores.addActionListener(this);
			Object[] objects = {cHighscores, lHighscores};
			opHighscores = new JOptionPane(objects);
			JOptionPane optionPane = new JOptionPane(objects);
			dHighscores.setVisible(false);
			dHighscores= optionPane.createDialog((String) language.get("HIGHSCORES"));
			dHighscores.repaint();
			dHighscores.setVisible(true);
		}
	}

	public void manageHighscore(Float score){
		boolean exists=false;

		int highscoreIndex=0;

		for(Highscore highscore:highscores) {
			if (highscore.getX() == x)
				if (highscore.getY() == y)
					if (highscore.getMines() == minesNumber) {
						exists = true;
						break;
					}
			highscoreIndex++;
		}

		if(exists==false) {
			highscores.add(new Highscore(x, y, minesNumber));
			highscoreIndex=highscores.size()-1;
		}

		Highscore highscore = highscores.get(highscoreIndex);

		if(highscore.checkScore(score/1000)){
			String[] button = {"OK"};
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			JLabel lText = new JLabel((String) language.get("HIGHSCORE_COMM")+"\n");
			JTextField textField = new JTextField();
			panel.add(lText);
			panel.add(textField);
			JOptionPane.showOptionDialog(null, panel, (String) language.get("HIGHSCORE_TITLE"), JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, button, button[0]);
			highscore.insertScore(textField.getText(), score/1000);
		}

		saveHighscores();
	}
}