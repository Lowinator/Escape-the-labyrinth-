import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.googlecode.lanterna.input.InputProvider;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;

public class GameLoop {
	
	private Spielfeld feld = new Spielfeld();		// Spielfeld auf dem gespielt wird
	private Key key;					// speichert den user input
	private Spieler spielFigur;			// instanz des Spielers		
	
	private MainMenu mainMenu = new MainMenu(feld, this);	// verfuegbare Menues
	private InGameMenu inGameMenu = new InGameMenu(feld, this);
	private GameOverMenu gameOverMenu = new GameOverMenu(feld, this);
	private YouWinMenu youWinMenu = new YouWinMenu(feld, this);
	private SkinsMenu skinsMenu = new SkinsMenu(feld, this);
	
	private long lastCall = 0;	// wird fuer die bewegung der Dynamischen Hindernisse benutzt
	private boolean isRunning = true;		// gibt an ob das Spiel laueft
	private boolean inGameMenuVisible = false;	// gibt an ob das ingame Menu sichtbar ist
	private static boolean gameOver = false;	// gibt an ob der Spieler verloren hat
	
	private String level;		// name der Property Datei aus der das Labyrinth geladen wird

	// relevante getter und setter
	public Spieler getSpielFigur() {
		return spielFigur;
	}
	public MainMenu getMainMenu() {
		return mainMenu;
	}
	public static void playerLost() {
		gameOver = true;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public void setInGameMenuVisible(boolean temp) {
		inGameMenuVisible = temp;
	}
	
	public void runGame() {		// das Spiel wird gestartet
		
		while(isRunning) {	
			while (mainMenu.getVisible()) {		// gibt das mainMenu aus falls noetig
				mainMenu.showMainMenu();

				if (!mainMenu.getVisible())		// wenn ein Spiel geladen werden soll
					loadNewLevel();
			}
			// es wird nur dann gescrollt wenn das Labyrinth groesser als das Terminal ist
			if (feld.getTerminalHeight() < feld.getHeight() || feld.getTerminalWidth() < feld.getWidth() )
				if (feld.getScreenX() < 10 || feld.getScreenX() > 130 ||
					feld.getScreenY() < 10 || feld.getScreenY() > 30 ) {
					feld.getSwingTerminal().clearScreen();
					feld.getNewPart();			// erstellt den sichtbaren Teil des Labyrinths
			}
			checkGameInput();		// prueft und reagiert auf user input
			
			// bewegt die Dynamischen Hindernise abhaengig von der Eingestellten schnelligkeit
			if (System.currentTimeMillis() - lastCall > 500) {
				lastCall = System.currentTimeMillis();
				feld.moveEnemies();
			}
			while (inGameMenu.getVisible()) {
				inGameMenu.showInGameMenu();		// zeigt das ingame Menu an
				if (!inGameMenu.getVisible()) {	// das Labyrinth wird ausgegeben nachdem das Menu geschlossen wurde
					if (inGameMenu.getLoadGameMenu().isGameSelected()) {
						loadNewLevel();
					} else
						feld.getNewPart();
				}
			}
			if (spielFigur.getWinGame()) {
				youWinMenu.saveScore(getSpielFigur().getPunkte());
				while (spielFigur.getWinGame()) {		// der Spieler hat gewonnen
					youWinMenu.showYouWinMenu();
				}
			}
			try {			// verursacht einen Delay 
				TimeUnit.MILLISECONDS.sleep(15);
			} catch (InterruptedException e) {
			    //Handle exception
			}
			if (spielFigur.getLifesLeft() <= 0) {		// game over
				gameOverMenu.setVisible(true);
				while(gameOverMenu.getVisible()) {
					gameOverMenu.showGameOverMenu();
				}
			}
		}
	}
	public void loadNewLevel() {		// ladet neues Labyrinth
		feld.startGame(level);
		spielFigur = feld.getSpielFigur();
		feld.getNewPart();
	}
	public void checkGameInput() {		// wie das Spiel auf den user input reagiert wird hier angegeben
		key = feld.getSwingTerminal().readInput();		// speichert den user input temporaer
		if (key != null)
			switch (key.getKind()){		// die verschiedenen moeglichen Eingaben des Users
			case ArrowDown:
				spielFigur.moveDown(feld); 
				break;
			case ArrowUp:
				spielFigur.moveUp(feld); 
				break;
			case ArrowRight:
				spielFigur.moveRight(feld);
				break;
			case ArrowLeft:
				spielFigur.moveLeft(feld);
				break;
			case Escape:
				inGameMenu.setVisible(true);
				break;
			case Enter:
				break;	
			}	
	}
}