import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import com.googlecode.lanterna.terminal.swing.SwingTerminal;

public class Spielfeld {

	private Terminal terminal;	// das, dem Spieler sichtbare, Terminal
	public int HEIGHT;	// groesse des Spielfelds
	public int WIDTH;	// breite des Spielfelds
	public final int TERMINAL_HEIGHT = 40;	// groesse des Terminals
	public final int TERMINAL_WIDTH = 150;	// breite des Terminals
	private Properties prop = new Properties();	// properties Datei die das Labyrinth speichern wird
	private String farbe = "DEFAULT";			// farbe des Labyrinths
	private final int shiftLabyrinth = 4; /* shiftet das Labyrinth nach unten um Platz
											 fuer andere Spielinfos zu machen (leben..)*/

	// eine Liste die zur verwaltund der dynamischen Hindernisse benutzt wird 
	private LinkedList<DynamischesHindernis> dynamischesHindernis = new LinkedList<>();		
	private Spieler spielFigur;		// Spieler 
	
	private char[][] labyrinth;		// zweidimensionaler array fuer das Spielfeld
	private char[][] screen;		// der auf dem Terminal sichtbare Teil des Labyrinths
	private int screenX;		// die x,y Koordinaten der Spielfigur auf dem screen (Ausschnitt der Labyrinths)
	private int screenY;
	
	int focusX;	// letzte x koordinate von spielFigur bei getNewPart(), um diese Position wird der sichtbare Abschnit erstellt
	int focusY;	// letzte y koordinate von spielFigur bei getNewPart()
	
	// relevante getter und setter
	public DynamischesHindernis getDH() {
		return dynamischesHindernis.getFirst();
	}
	public void setFarbe(String farbe) {
		this.farbe = farbe;
	}
	public String getFarbe() {
		return farbe;
	}
	public void setLabyrinth(int x, int y, char character) {
		labyrinth[x][y] = character;
	}
	public void setScreen(int x, int y, char character) {
		screen[x][y] = character;
	}
	public int getScreenX() {
		return screenX;
	}
	public int getScreenY() {
		return screenY;
	}
	public void setScreenX(int x) {
		screenX = x;
	}
	public void setScreenY(int y) {
		screenY = y;
	}
	public int getHeight() {
		return HEIGHT;
	}
	public int getWidth() {
		return WIDTH;
	}
	public int getTerminalHeight() {
		return TERMINAL_HEIGHT;
	}
	public int getTerminalWidth() {
		return TERMINAL_WIDTH;
	}
	public int getShiftLabyrinth() {
		return shiftLabyrinth;
	}
	public Spieler getSpielFigur() {
		return spielFigur;
	}
	public Terminal getTerminal() {
		return terminal;
	}
	public SwingTerminal getSwingTerminal() {
		return terminal.getSwingTerminal();
	}
	public Spielfeld() {
		terminal = new Terminal(TERMINAL_WIDTH , TERMINAL_HEIGHT);
	}
	
	public void startGame(String fileName) {	// das Labyrinth wird initialisiert
		while (!dynamischesHindernis.isEmpty()) {	// loescht die DynamischenHindernisse vom letzten spiel
	        dynamischesHindernis.removeFirst();
	    }
		// properties file wird geladen
		readProperties(fileName);
		this.HEIGHT = shiftLabyrinth + Integer.parseInt(readProperty("Height"));
		this.WIDTH = Integer.parseInt(readProperty("Width"));
		
		labyrinth = new char[WIDTH][HEIGHT];	// groesse und breite des Labyrinths werden festgelegt
		screen = new char[TERMINAL_WIDTH][TERMINAL_HEIGHT];		// groesse und breite des sichtbaren Teils werden festgelegt
		
		// weitere Spielinfos (leben, punkte..) werden geladen, falls notwaendig
		boolean temp2 = false;		
		if (prop.getProperty("PlayerX") != null && prop.getProperty("PlayerY") != null) {
			spielFigur = new Spieler(Integer.parseInt(prop.getProperty("PlayerX")), Integer.parseInt(prop.getProperty("PlayerY")));
			labyrinth[Integer.parseInt(prop.getProperty("PlayerX"))][Integer.parseInt(prop.getProperty("PlayerY"))] = Spieler.getSign();
			if (prop.getProperty("Lives") != null)
				spielFigur.setLifesLeft(Integer.parseInt(prop.getProperty("Lives")));
			
			if(prop.getProperty("Punkte") != null)
				spielFigur.addPunkte(Integer.parseInt(prop.getProperty("Punkte")));
			if (prop.getProperty("Key") != null) {
				if (prop.getProperty("Key").equals("Yes"))
					spielFigur.setHasKey(true);
				else if (prop.getProperty("Key").equals("No"))
					spielFigur.setHasKey(false);
			}
		} else {
			temp2 = true;
		}
		
		// itteriert durch die gegebene properties Datei
		for (int i = 0; i <= (HEIGHT - 1); i++)
			for (int j = 0; j <= (WIDTH - 1); j++){
				
				String s = prop.getProperty(Integer.toString(i) + "," + Integer.toString(j));	// values werden hier temporaer gespeichert
				if (s == null)
					s = "-1";
				
				switch (s) {
				case "0": 
					labyrinth[j][i + shiftLabyrinth] = Wand.getSign();	// Waende werden zum labyrinth hinzugefuegt
					break;
					
				case "1": 
					if (temp2 == true){
						labyrinth[j][i + shiftLabyrinth] = Spieler.getSign();		// Spieler zum labyrinth anstatt des ersten Eingangs hinzugefuegt
						spielFigur = new Spieler(j,i + shiftLabyrinth);
						temp2 = false;
					} else
						labyrinth[j][i + shiftLabyrinth] = Eingang.getSign();		// Falls der Spieler zum Labyrinth schon hinzugefuegt wurde werden hier Eingaenge hinzugefuegt
					break;
				case "2": 
					labyrinth[j][i + shiftLabyrinth] = Ausgang.getSign();	// Ausgaenge werden zum labyrinth hinzugefuegt
					break;
				case "3":
					labyrinth[j][i + shiftLabyrinth] = StatischesHindernis.getSign();	// Statische Hindernisse werden zum labyrinth hinzugefuegt
					break;
				case "4": 
					labyrinth[j][i + shiftLabyrinth] = DynamischesHindernis.getSign();	// Dynamische Hindernisse werden zum labyrinth hinzugefuegt
					dynamischesHindernis.add(new DynamischesHindernis(j,i + shiftLabyrinth));
					break;
				case "5": 
					labyrinth[j][i + shiftLabyrinth] = Schluessel.getSign();	// Schluessel werden zum labyrinth hinzugefuegt
					break;
				}			
			}	
	}
	public char identifyField(int x, int y) {	// gibt einen char aus dem 2-D array labyrinth[][] zurueck
		return labyrinth[x][y];
	}
	
	public void getNewPart() {
		for (int i = 0; i < TERMINAL_HEIGHT; i++)		// verhindert visual bugs wenn man am Rand entlang laeuft
			for (int j = 0; j < TERMINAL_WIDTH; j++)
				screen[j][i] = ' ';
		
		int x;
		int y = TERMINAL_HEIGHT/2-19;
		
		focusX = spielFigur.getX();		// Koordinaten des Spielers
		focusY = spielFigur.getY();
		for (int i = spielFigur.getY()-15; i < spielFigur.getY() + 18; i++) {		// laedt den abschnit aus dem Labyrinth ins screen array
			y++;
			x = TERMINAL_WIDTH/2-1-70;
			for (int j = spielFigur.getX()-70; j < spielFigur.getX() + 70; j++) {
				x++;
					
				if (j >= 0 && j < WIDTH && i >= 0 && i < HEIGHT) {		// verhindert eine ArrayIndexOutofBoundException
					//writeTerminal(x, y, labyrinth[j][i]);
					screen[x][y] = labyrinth[j][i];
				
					if (labyrinth[j][i] == Spieler.getSign()) {			// speichert die Koordinaten des Spielers auf dem Screen
						screenX = x;
						screenY = y + shiftLabyrinth;
					}
				}
			}
				
		}
		printScreen();		// gibt den sichtbaren Abschnitt des Labyrinths + Spiel Info auf das Terminal aus
	}
	private void printScreen() {	// gibt Spiel Infos und den sichtbaren Abschnitt aus dem labyrinth auf das Terminal aus
		
		int x = -1;
		int y = -1;
		for (int i = shiftLabyrinth; i < TERMINAL_HEIGHT; i++) {		// iteriert durch sceen[][] und gibt den ganzen 2D array aus
			y++;
			x = -1;
				for (int j = 0; j < TERMINAL_WIDTH; j++) {
					x++;
					if (screen[x][y] == Wand.getSign()) {	// farbe des Labyrinths wird ermittelt
						if (farbe.equals("RED")) {
							terminal.getSwingTerminal().applyForegroundColor(255,0,0);
						} else if (farbe.equals("BLUE")) {
							terminal.getSwingTerminal().applyForegroundColor(0,0,255);
						} else if (farbe.equals("GREEN")) {
							terminal.getSwingTerminal().applyForegroundColor(0,255,0);
						}
					}
					
					if (screen[x][y] == Spieler.getSign())	// jedes Zeichen hat seine eigene farbe
						terminal.getSwingTerminal().applyForegroundColor(255,20,147);
					else if (screen[x][y] == Ausgang.getSign()) 
						terminal.getSwingTerminal().applyForegroundColor(160,82,45);
					else if (screen[x][y] == Eingang.getSign())
						terminal.getSwingTerminal().applyForegroundColor(218,165,32);
					else if (screen[x][y] == Schluessel.getSign())
						terminal.getSwingTerminal().applyForegroundColor(160,32,240);
					else if (screen[x][y] == DynamischesHindernis.getSign())
						terminal.getSwingTerminal().applyForegroundColor(255,255,0);
					else if (screen[x][y] == StatischesHindernis.getSign())
						terminal.getSwingTerminal().applyForegroundColor(255,140,0);
					
					terminal.write(j, i, screen[x][y]);		// Zeichen wird auf das Terminal ausgegeben
					terminal.getSwingTerminal().applyForegroundColor(255,255,255);	
				}
		}
		
		writeTerminalString(1, 2, "Key: ");		// gibt an ob der Spieler einen Schluessel eingesammelt hat oder nicht
		if (spielFigur.getHasKey()) {
			writeTerminalString(6, 2, "Yes");
		} else {
			writeTerminalString(6, 2, "No");
		}
		writeTerminalString(1, 3, "Points: " + spielFigur.getPunkte());		// zeigt die Punkte an
		
		spielFigur.printHealthBar(this);		// zeigt HP bar an
	}	
	
	public void saveGame(String levelName) {	// speichert das Spiel in eine properties Datei
		Properties prop1 = new Properties();
		char tempChar;
			
		for (int i = 0; i <= (HEIGHT-1); i++) {		// itteriert durch das ganze Labyrinth und speichert die Positionen relevanter Objekte
			for (int j = 0; j <= (WIDTH-1); j++) {
				tempChar = labyrinth[j][i];
				
				if (tempChar == Wand.getSign()) {	
					prop1.setProperty(Integer.toString(i-shiftLabyrinth) + "," + Integer.toString(j), "0");	// speichert Waende
				} else if (tempChar == Eingang.getSign() ) {
					prop1.setProperty(Integer.toString(i-shiftLabyrinth) + "," + Integer.toString(j), "1");	// speichert Eingaenge
				} else if (tempChar == Ausgang.getSign()) {
					prop1.setProperty(Integer.toString(i-shiftLabyrinth) + "," + Integer.toString(j), "2");	// speichert Ausgaenge
				} else if (tempChar == StatischesHindernis.getSign()) {
					prop1.setProperty(Integer.toString(i-shiftLabyrinth) + "," + Integer.toString(j), "3");	// speichert Statische Hindernisse
				} else if (tempChar == DynamischesHindernis.getSign()) {
					prop1.setProperty(Integer.toString(i-shiftLabyrinth) + "," + Integer.toString(j), "4");	// speichert Dynamische Hindernisse
				} else if (tempChar == Schluessel.getSign()) {
					prop1.setProperty(Integer.toString(i-shiftLabyrinth) + "," + Integer.toString(j), "5");	// speichert Schluessel
				}
			}
		}
		
		prop1.setProperty("Height", Integer.toString(HEIGHT - shiftLabyrinth));	// speichert die hoehe des labyrinths
		prop1.setProperty("Width", Integer.toString(WIDTH));					// speichert die breite des labyrinths
		prop1.setProperty("PlayerX", Integer.toString(spielFigur.getX()));		// speichert die x Koordinate der Spielfigur
		prop1.setProperty("PlayerY", Integer.toString(spielFigur.getY()));		// speichert die y Koordinate der Spielfigur
		prop1.setProperty("Lives", Integer.toString(spielFigur.getLifesLeft()));	// speichert die Anzahl der Leben
		prop1.setProperty("Punkte", Integer.toString(spielFigur.getPunkte())); 	// speichert die Anzahl der Punkte
		
		String temp = "";
		if (spielFigur.getHasKey())		// speichert ob der Spieler einen Schluessel eingesammelt hat
			temp = "Yes";
		else
			temp = "No";
		
		prop1.setProperty("Key", temp);  
		
		try {					// die mit Spielinformationen gefuellte Properties Datei wird hier im Projekt folder gespeichert
			File file = new File(levelName);			
			FileOutputStream fileOut = new FileOutputStream(file);
			prop1.store(fileOut, "saved game");
			fileOut.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// schreibt das gewuenschte Zeichen auf das gewuenschte Feld im Labyrinth UND im Terminal
	public void write(int x, int y, char character){
		labyrinth[x][y] = character;
			terminal.write(x, y, character);
		
	}
	// schreibt das gewuenschte Zeichen auf das gewuenschte Feld im Terminal
	public void writeTerminal(int x, int y, char character){
		terminal.write(x, y, character);
	}
	// // schreibt den gewuenschten String ab dem gewuenschten feld im Terminal
	public void writeTerminalString(int x, int y, String s) {
		for (int i = 0; i < s.length(); i++)
			terminal.write(x + i, y, s.charAt(i));
		
	}
	
	// liest eine value aus der property Datei
	private String readProperty(String key) {
		return prop.getProperty(key);
	}
	
	public void moveEnemies() {		// initialisiert die bewegung der dynamischen Hindernisse
		terminal.getSwingTerminal().applyForegroundColor(255,255,0);
	//	System.out.println(dynamischesHindernis.size());
		for (DynamischesHindernis i: dynamischesHindernis) {
			labyrinth[i.getX()][i.getY()] = ' ';
			i.move(spielFigur.getX(), spielFigur.getY(), this, spielFigur);
			labyrinth[i.getX()][i.getY()] = DynamischesHindernis.getSign();
		}
		
		int x;
		int y = TERMINAL_HEIGHT/2-19;		
		
		for (int i = focusY - 15; i < focusY + 18; i++) {		// laedt den sichtbaren abschnit aus dem Labyrinth ins screen array 
			y++;
			x = TERMINAL_WIDTH/2-71;
			for (int j = focusX - 70; j < focusX + 70; j++) {
				x++;
				
				if (j >= 0 && j < WIDTH && i >= 0 && i < HEIGHT) {		// verhindert eine ArrayIndexOutofBoundException
					if (screen[x][y] == DynamischesHindernis.getSign()) {
						terminal.write(x, y + shiftLabyrinth, ' ');;			// loescht die alten positionen der dynamischen Hindernisse
					}
					screen[x][y] = labyrinth[j][i];
					if (labyrinth[j][i] == DynamischesHindernis.getSign()) {	// zeigt die neue Position der dynamischen Hindernisse an
						writeTerminal(x, y + shiftLabyrinth, DynamischesHindernis.getSign());
					}
				}
			}
		}
		x = -1;
		y = -1;
		int counter = 0;
		for (int i = shiftLabyrinth; i < TERMINAL_HEIGHT; i++) {		// iteriert durch sceen[][] und gibt den ganzen 2D array aus
			y++;
			x = -1;
				for (int j = 0; j < TERMINAL_WIDTH; j++) {
					x++;
					// ein fix fuer visual bugs die in manchen randfaellen auftreten koennen, sicher ist sicher :P
					if ((screen[x][y] == ' ') || (screen[x][y] == '\u0000')) {
						writeTerminal(x, y+shiftLabyrinth, ' ');
					}
				}
		}
		terminal.getSwingTerminal().applyForegroundColor(255,255,255);
	}
	// liest die Properties Datei mit der dann das Labyrinth erstellt wird
	private void readProperties(String fileName){
		
		// liest die Properties Datei
		try {
			File file = new File(fileName);
			FileInputStream fileInput = new FileInputStream(file);
			prop = new Properties();
			prop.load(fileInput);
			fileInput.close();
			
		} catch (FileNotFoundException e) {	// Datei wurde  nicht gefunden 
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 
}
