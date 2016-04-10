import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import com.googlecode.lanterna.input.Key;

public class MainMenu extends Menu {
	
	private boolean visible = true;		// gibt an ob das Menu sichtbar ist
	private boolean legendScreenVisible = false;	// gibt an ob die Legende sichtbar ist
	private boolean scoreBoardVisible = false;		// gibt an ob die Scores sichtbar sind
	private LoadGameMenu loadGame = new LoadGameMenu(feld, game, this);	// wird benutzt um Spiele aus dem Menu zu laden
	private SkinsMenu skinsMenu = new SkinsMenu(feld, game);	// wird benutzt um skins/das aussehen zu aendern
	private Properties savedScores = new Properties();		// wird benutzt um die Scores zu speichern
	private String savedScoresFileName = "SavedScores.properties";	// name der Datei mit den gespeicherten Scores
	
	public MainMenu(Spielfeld feld, GameLoop game) {	
		super(feld, 6, game);
	}
	// relevante getter und setter
	public Properties getSavedScores() {
		return savedScores;
	}
	public String getSavedScoresFileName() {
		return savedScoresFileName;
	}
	public void setVisible(boolean temp) {
		this.visible = temp;
	}
	public boolean getVisible() {
		return visible;
	}
	public void showMainMenu() {		// zeigt das Menu an
		feld.getSwingTerminal().clearScreen();	// loescht alle Zeichen auf dem Terminal
		
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2-10, "M E N U");	
		if (pointer == 5)															// Optionen im Menu
			feld.writeTerminal(feld.TERMINAL_WIDTH/2-4, feld.TERMINAL_HEIGHT/2-8, '>');
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2-8, "Quick game");
		if (pointer == 4)
			feld.writeTerminal(feld.TERMINAL_WIDTH/2-4, feld.TERMINAL_HEIGHT/2-6, '>');
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2-6, "Load game");
		if (pointer == 3)
			feld.writeTerminal(feld.TERMINAL_WIDTH/2-4, feld.TERMINAL_HEIGHT/2-4, '>');
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2-4, "Legend");
		if (pointer == 2)
			feld.writeTerminal(feld.TERMINAL_WIDTH/2-4, feld.TERMINAL_HEIGHT/2-2, '>');
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2-2, "Scoreboard");
		if (pointer == 1)
			feld.writeTerminal(feld.TERMINAL_WIDTH/2-4, feld.TERMINAL_HEIGHT/2, '>');
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2, "Skins");
		if (pointer == 0)
			feld.writeTerminal(feld.TERMINAL_WIDTH/2-4, feld.TERMINAL_HEIGHT/2+2, '>');
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2+2, "Exit");
		
		try {			// verursacht einen Delay 
			TimeUnit.MILLISECONDS.sleep(40);
		//	TimeUnit.SECONDS.sleep(1);    

		} catch (InterruptedException e) {
		    //Handle exception
		}
		
		checkInput();	// kuemmert sich um den User input
		
	}
	
	public void selectOption(int pointer) {	// hier wird die funktionalitaet der Optionen im Menu implementiert
		switch (pointer) {
		case 5:			// quick game
			
			game.setLevel(loadGame.getSavedGames().get(0));		
			visible = false;
			break;
		case 4:			// load game
			loadGame.setVisible(true);
			while(loadGame.getVisible()) {
				loadGame.showLoadGameMenu();
			}	
			if(loadGame.isGameSelected())
				visible = false;
			break;
		case 3:			// legende
			legendScreenVisible = true;
			while (legendScreenVisible) {
				showLegendScreen();
			}
			break;
		case 2:			// scoreboard
			scoreBoardVisible = true;
			while (scoreBoardVisible) {
				showScoreBoard();
			}
			break;
		case 1:			// skins
			skinsMenu.setVisible(true);
			while (skinsMenu.getVisible()) {
				skinsMenu.showSkinsMenu();
			}
			break;
		case 0:			// exit
			System.exit(0);
			break;
		}
		
	}
	
	public void showScoreBoard() {		// scores werden angezeigt
		feld.getSwingTerminal().clearScreen();		// Zeichen werden von dem Terminal entfernt
		
		prepareScoreFile(false);	// die Properties Datei wird vorbereitet
   		
		readProperties(savedScoresFileName);		// liest die Properties Datei mit den scoreboard infos
		
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2-10, "S C O R E  B O A R D");
		
       	for (int i = 1; i < 11; i++) {	// itteriert durch die Liste mit den gespeicherten Spielen und gibt sie fuer den User aus
       			String entry = savedScores.getProperty(Integer.toString(i));	// 
       			if (entry == null) {
       				entry = "--Empty--";
       				feld.writeTerminalString(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2-8+2*i, entry);
       			} else {
	     		   SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy"); //  Format: Tag/Monat/Jahr
	     		   Date now = new Date();
	     		   String date = sdfDate.format(now);
	     		   
	     		   feld.writeTerminalString(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2-8+2*i, date + " -> "  + entry);  
       			}
       	}
       	feld.writeTerminalString(feld.TERMINAL_WIDTH/2-10, feld.TERMINAL_HEIGHT/2+15, "--Press Escape to exit--"); 
		
		key = feld.getSwingTerminal().readInput(); // user input
		
		if (key != null)	// bei escape wird das Menu verlassen
			switch (key.getKind()){
			case Escape:
				scoreBoardVisible = false;
				break;
			}
		
		try {			// verursacht einen Delay 
			TimeUnit.MILLISECONDS.sleep(100);
		//	TimeUnit.SECONDS.sleep(1);    

		} catch (InterruptedException e) {
		    //Handle exception
		}
	}
	// falls keine properties Datei fuer die Scores existiert wird eine neue erstellt
	public void prepareScoreFile(boolean save) {
		File file1 = new File(".");
		int counter = 0;
   		for(File f1 : file1.listFiles()){
   			if (!f1.getName().contains(savedScoresFileName)) { 
   				counter++;	// falls der counter so Gross wie die Anzahl der Dateien ist dann muss eine neue Properties Datei erstellt werden 
   			}
   		}
   		if (counter == file1.listFiles().length || save) {
			try {					// Properties Datei fuer Scores wird erstellt falls keine vorhanden ist
				File file = new File(savedScoresFileName);			
				FileOutputStream fileOut = new FileOutputStream(file);
				savedScores.store(fileOut, "Saved Scores");
				fileOut.close();
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			}
   		}
	}
	
	private void readProperties(String fileName){	// liest Properties Datei
		try {
			File file = new File(fileName);
			FileInputStream fileInput = new FileInputStream(file);
			savedScores = new Properties();
			savedScores.load(fileInput);
			fileInput.close();
		} catch (FileNotFoundException e) {	// Datei wurde  nicht gefunden 
			
		} catch (IOException e) {
		}
	}
	
	public void showLegendScreen() {		// zeigt die Legende an
		feld.getSwingTerminal().clearScreen();
		
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2-10, "L E G E N D");
		
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2-8, "Spieler: " + Spieler.getSign());
	
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2-6, "Dynamisches Hindernis: " + DynamischesHindernis.getSign());
	
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2-4, "Statisches Hindernis: " + StatischesHindernis.getSign());
	
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2-2, "Schluessel: " + Schluessel.getSign());
	
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2, "Wand: " + Wand.getSign());
	
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2+2, "Eingang: " + Eingang.getSign());
			
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2+4, "Ausgang: " + Ausgang.getSign());
		
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2+7, "Press Escape to go back...");
		
		key = feld.getSwingTerminal().readInput();		// user input
		
		if (key != null)		// Menu wird verlassen mit dem eingeben von escape
			switch (key.getKind()){
			case Escape:
				legendScreenVisible = false;
				break;
			}
		
		try {			// verursacht einen Delay 
			TimeUnit.MILLISECONDS.sleep(40);
		//	TimeUnit.SECONDS.sleep(1);    

		} catch (InterruptedException e) {
		    //Handle exception
		}
			
		
	}
	
}
