import java.util.concurrent.TimeUnit;

public class YouWinMenu extends Menu {
	
	public YouWinMenu (Spielfeld feld, GameLoop game) {
		super(feld, 3, game );
	}
	
	// dieses Menu wird angezeigt wenn der Spieler gewinnt
	public void showYouWinMenu() {		// zeigt das Menu an 
		
		feld.getSwingTerminal().clearScreen();
		
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2-10, "Y O U  W I N !");
		
		if (pointer == 2)														// Optionen im Menu
			feld.writeTerminal(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2-7, '>');
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2, feld.TERMINAL_HEIGHT/2-7, "Play Again");
		
		if (pointer == 1)
			feld.writeTerminal(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2-5, '>');
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2, feld.TERMINAL_HEIGHT/2-5, "Back to Menu");
		
		if (pointer == 0)
			feld.writeTerminal(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2-3, '>');
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2, feld.TERMINAL_HEIGHT/2-3, "Exit");
		
		try {			// verursacht einen Delay 
			TimeUnit.MILLISECONDS.sleep(100);
		//	TimeUnit.SECONDS.sleep(1);    

		} catch (InterruptedException e) {
		    //Handle exception
		}
		
       	checkInput();	// user input
       	
	}
	
	public void saveScore(int score) {	// speichert die errungene Punkteanzahl des Spielers
		game.getMainMenu().prepareScoreFile(false);	// bereitet die Properties Datei vor (die selbe wird zum Speichen benutzt)
		int i = 1;
		String temp;	// speichert eine Punkteanzahl temporaer
		while (true) {
			temp = game.getMainMenu().getSavedScores().getProperty(Integer.toString(i));
			if (temp != null)
				i++;
			else
				break;	// der Platz nach dem letzten Key in der properties Datei wurde gefunden
			System.out.println("i: " + i);
		}
		// auf diesen Platz wird nun geschrieben
		game.getMainMenu().getSavedScores().setProperty(Integer.toString(i), Integer.toString(score));

		game.getMainMenu().prepareScoreFile(true);	// die Properties Datei wird nun permanent im File System des Benutzers gespeicher
	}
	
	public void selectOption(int pointer) {		// hier werden die funktionalitaeten der Optionen im Menu implementiert
		switch (pointer) {			
		case 2:			// Play again
			
			game.getSpielFigur().setWinGame(false);// schliesst dieses youWinMenu
			game.loadNewLevel();  			// ladet das Level nochmal
			
			break;
			
		case 1:			// Back to menu
			game.getSpielFigur().setWinGame(false); // schliesst dieses youWinMenu
			game.getMainMenu().setVisible(true);	// oeffnet das mainMenu
			
			break;
			
		case 0:			// exit
			System.exit(0);
			break;
		}
	}
}