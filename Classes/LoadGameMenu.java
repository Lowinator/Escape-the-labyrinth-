import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class LoadGameMenu extends Menu {
	
	private LinkedList<String> savedGames = new LinkedList<>();	// Liste mit allen gespeicherten Spielen
	private boolean visible = false;		// entscheidet ob das Menu sichtbar ist
	private boolean gameSelected = false;	// gibt an ob ein gespeichertes Spiel ausgewaehlt wurde
	MainMenu main;		// das Menu mit gespeicherten Spielen gehoert zu diesem Main Menu 
	
	// relevante getter und setter
	public boolean isGameSelected() {
		return gameSelected;
	}
	public LinkedList<String> getSavedGames() {
		return savedGames;
	}
	public boolean getVisible() {
		return visible;
	}
	public void setVisible(boolean temp) {
		this.visible = temp;
	}
	
	public LoadGameMenu(Spielfeld feld, GameLoop game, MainMenu main) {
		super(feld, 13, game);
		this.main = main;
		
		// liest existierende properties Dateien ein die im Menu als gespeicherte Spiele angezeigt werden
		File actual = new File(".");
   		for( File f1 : actual.listFiles()){		// itteriert durch alle Dateien
   			if (f1.getName().toLowerCase().contains(".properties")) { 	// waehlt nur Properties Dateien aus
   					savedGames.add(f1.getName());		// addet diese Properties Dateien zur Liste der gespeicherten Spiele
   			}
   		}
	}
	
	public void showLoadGameMenu() {		// zeigt das Menu an
		
		feld.getSwingTerminal().clearScreen();		// loescht alles auf dem Terminal
       	
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2-10, "S a v e d  G a m e s");	// Menu Name
		
       	for (int i = 0; i < 12; i++) {	// itteriert durch die Liste mit den gespeicherten Spielen und gibt sie fuer den User aus
       		try {
       			if (pointer == 12-i)
       				feld.writeTerminal(feld.TERMINAL_WIDTH/2-4, feld.TERMINAL_HEIGHT/2-8+2*i, '>');
       			if (savedGames.get(i).equals(main.getSavedScoresFileName()))
       				i /= 0;
       			
				feld.writeTerminalString(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2-8+2*i, savedGames.get(i));
			} catch (Exception e) {
				feld.writeTerminalString(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2-8+2*i, "--Empty--");	// falls kein Spiel vorhanden ist
			}
       	}
       	
			if (pointer == 0 )	// letzte Option im Menu ist 'Exit'
   				feld.writeTerminal(feld.TERMINAL_WIDTH/2-4, feld.TERMINAL_HEIGHT/2+16, '>');
			feld.writeTerminalString(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2+16, "Exit");
       	
		try {			// verursacht einen Delay 
			TimeUnit.MILLISECONDS.sleep(100);
		//	TimeUnit.SECONDS.sleep(1);    

		} catch (InterruptedException e) {
		    //Handle exception
		}
		
       	checkInput();	// prueft und reagiert auf User input
	}
	
	public void selectOption(int pointer) {	// hier wird Entschieden was die Optionen im Menu machen
		
		if (pointer == 0) {			// die Exit Option
			gameSelected = false;
			visible = false;
		}
		
		try {			// verhindert eine Exception falls der Spieler auf empty drueckt
			if (pointer != 0) {		// ladet das gewaehlte level aus der liste mit den gespeicherten Spieln
				if (!savedGames.get((pointer - 12)*-1).equals(main.getSavedScoresFileName()))	{	
					game.setLevel(savedGames.get((pointer - 12)*-1));	
				
					gameSelected = true;	// gibt an dass eines der gespeicherten Spiele ausgewaehlt wurde
					visible = false;		// das load game Menu wird geschlossen
				}
			}
		} catch (Exception e) {
			
		}
	}

}
