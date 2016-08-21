import java.util.concurrent.TimeUnit;

public class GameOverMenu extends Menu {
	
	private boolean visible = true;		// gibt an ob das Menu sichtbar ist
	private boolean legendScreenVisible = false;	// gibt an ob die Legende sichtbar ist
	
	// relevante getter und setter
	public boolean getVisible() {
		return visible;
	}
	public void setVisible(boolean temp) {
		this.visible = temp;
	}
	public GameOverMenu(Spielfeld feld, GameLoop game) {
		super(feld, 3, game);
		
	}
	
	public void showGameOverMenu() {		// zeigt das Game Over Szenario an 
		
		feld.getSwingTerminal().clearScreen();
       	
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2-10, "G A M E  O V E R");
		
		if (pointer == 2)														// Optionen im Menu
			feld.writeTerminal(feld.TERMINAL_WIDTH/2-4, feld.TERMINAL_HEIGHT/2-8, '>');
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2-8, "Try again");
		if (pointer == 1)
			feld.writeTerminal(feld.TERMINAL_WIDTH/2-4, feld.TERMINAL_HEIGHT/2-6, '>');		
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2-6, "Legend");
		if (pointer == 0)
			feld.writeTerminal(feld.TERMINAL_WIDTH/2-4, feld.TERMINAL_HEIGHT/2-4, '>');
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2-4, "Exit");

		try {			// verursacht einen Delay 
			TimeUnit.MILLISECONDS.sleep(100);
		//	TimeUnit.SECONDS.sleep(1);    

		} catch (InterruptedException e) {
		    //Handle exception
		}
       	checkInput();	// user input
	}
	
	public void selectOption(int pointer) {		// die funktionalitaet der Optionen wird hier implementiert
		if (pointer == 0) {		// exit
			System.exit(0);
		}
		if (pointer == 1) {		// legende
			legendScreenVisible = true;
			while (legendScreenVisible) {
				game.getMainMenu().showLegendScreen();
				key = feld.getSwingTerminal().readInput();
				
				if (key != null)		// beim druecken von escape wird die Legende verlassen
					switch (key.getKind()){
					case Escape:
						legendScreenVisible = false;
						System.out.println(key);
						break;
					}
			}
		}
		if (pointer == 2) {		// play again
			visible = false;
			game.loadNewLevel();  	// ladet das Level nochmal
		}
	}
}