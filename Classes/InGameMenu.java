import java.util.concurrent.TimeUnit;

public class InGameMenu extends Menu {
	
	private boolean visible = false;		// gibt an ob das Menu sichtbar ist
	private boolean legendScreenVisible = false;	// gibt an ob die Legende sichtbat ist
	private LoadGameMenu loadGame = new LoadGameMenu(feld, game, game.getMainMenu());	// der spieler kann aus dem Menu Spiele laden
	
	// relevante getter und setter
	public LoadGameMenu getLoadGameMenu() {
		return loadGame;
	}
	public boolean getVisible() {
		return visible;
	}
	public void setVisible(boolean temp) {
		this.visible = temp;
	}
	public InGameMenu(Spielfeld feld, GameLoop game) {
		super(feld, 6, game);
	}
	
	public void showInGameMenu() {		// zeigt das Menu an
		feld.getSwingTerminal().clearScreen();		// loescht alle Zeichen vom Terminal
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2+1, feld.TERMINAL_HEIGHT/2-5, "M E N U");	// headline
								
		if (pointer == 5)															// Optionen im Menu..
			feld.writeTerminal(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2-3, '>');
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2, feld.TERMINAL_HEIGHT/2-3, "Resume");
		
		if (pointer == 4)
			feld.writeTerminal(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2-1, '>');
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2, feld.TERMINAL_HEIGHT/2-1, "Load");
		
		if (pointer == 3)
			feld.writeTerminal(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2+1, '>');
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2, feld.TERMINAL_HEIGHT/2+1, "Legend");
		
		if (pointer == 2)
			feld.writeTerminal(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2+3, '>');
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2, feld.TERMINAL_HEIGHT/2+3, "Back to Menu");
		
		if (pointer == 1)
			feld.writeTerminal(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2+5, '>');
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2, feld.TERMINAL_HEIGHT/2+5, "Save");
		feld.writeTerminal(feld.TERMINAL_WIDTH/2+5, feld.TERMINAL_HEIGHT/2+5, '&');
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2+7, feld.TERMINAL_HEIGHT/2+5, "Exit");
		
		if (pointer == 0)
			feld.writeTerminal(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2+7, '>');
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2, feld.TERMINAL_HEIGHT/2+7, "Exit");
		
		try {			// verursacht einen Delay 
			TimeUnit.MILLISECONDS.sleep(100);
		//	TimeUnit.SECONDS.sleep(1);    

		} catch (InterruptedException e) {
		    //Handle exception
		}
		
		checkInput();	// prueft und reagiert auf user input
	}
	
	public void selectOption(int pointer) {		// hier wird die Funktionalitaet der Optionen implementiert
		switch (pointer) {
		case 5:			// resume game
			visible = false;		// Menu ist nicht mehr sichtbar und Spiel wird fortgesetzt
			break;
		case 4:			// load game
			loadGame.setVisible(true);	// das Menu fuer dass Spiele laden wird sichtbar
			while(loadGame.getVisible()) {
				loadGame.showLoadGameMenu();
			}	
			if(loadGame.isGameSelected()) {
				visible = false;
			}
			break;
		case 3:			// legende
			legendScreenVisible = true;		// Legende wird sichtbar
			while (legendScreenVisible) {
				game.getMainMenu().showLegendScreen();
				
				key = feld.getSwingTerminal().readInput();
				
				if (key != null)			// exit wenn der user escape eingibt
					switch (key.getKind()){
					case Escape:
						legendScreenVisible = false;
						break;
					}
			}
			break;
		case 2:			// back to Menu
			visible = false;		// schliesst dieses inGameMenu
			game.getMainMenu().setVisible(true);	// oeffnet das mainMenu
			break;
		case 1:			// save & exit
			feld.saveGame(game.getLevel());	// Spiel wird gespeichert
			System.exit(0);
			break;
		case 0:			// exit (no save)
			System.exit(0);
			break;
		}
	}
}