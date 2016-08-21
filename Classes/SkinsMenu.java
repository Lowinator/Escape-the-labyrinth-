import java.util.concurrent.TimeUnit;

public class SkinsMenu extends Menu {
	
	private boolean visible = false;	// gibt an ob das Menu sichtbar ist
	
	int ausgangSign = 0;	// werden fuer die auswahl der verschiedenen zeichen benutzt
	int eingangSign = 0;
	int spielerSign = 0;
	int wandSign = 0;
	int schluesselSign = 0;
	int farbe = 0;
	int statischesHindernisSign = 0;
	int dinamischesHindernisSign = 0;
	
	public SkinsMenu(Spielfeld feld, GameLoop game) {
		super(feld, 9, game);
	}
	// relevante getter und setter
	public void setVisible(boolean temp) {
		this.visible = temp;
	}
	public boolean getVisible() {
		return visible;
	}
	public void showSkinsMenu() {		// menu wird angezeigt
		feld.getSwingTerminal().clearScreen();
		
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2-10, "S K I N S");
		
		if (pointer == 8)
			feld.writeTerminal(feld.TERMINAL_WIDTH/2-4, feld.TERMINAL_HEIGHT/2-8, '>');		// Optionen im Menu
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2-8, "Spieler: " + Spieler.getSign());
		if (pointer == 7)
			feld.writeTerminal(feld.TERMINAL_WIDTH/2-4, feld.TERMINAL_HEIGHT/2-6, '>');
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2-6, "Dynamisches Hindernis: " + DynamischesHindernis.getSign());
		if (pointer == 6)
			feld.writeTerminal(feld.TERMINAL_WIDTH/2-4, feld.TERMINAL_HEIGHT/2-4, '>');
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2-4, "Statisches Hindernis: " + StatischesHindernis.getSign());
		if (pointer == 5)
			feld.writeTerminal(feld.TERMINAL_WIDTH/2-4, feld.TERMINAL_HEIGHT/2-2, '>');
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2-2, "Schluessel: " + Schluessel.getSign());
		if (pointer == 4)
			feld.writeTerminal(feld.TERMINAL_WIDTH/2-4, feld.TERMINAL_HEIGHT/2, '>');
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2, "Wand: " + Wand.getSign());
		if (pointer == 3)
			feld.writeTerminal(feld.TERMINAL_WIDTH/2-4, feld.TERMINAL_HEIGHT/2+2, '>');
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2+2, "Eingang: " + Eingang.getSign());
		if (pointer == 2)
			feld.writeTerminal(feld.TERMINAL_WIDTH/2-4, feld.TERMINAL_HEIGHT/2+4, '>');
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2+4, "Ausgang: " + Ausgang.getSign());
		if (pointer == 1)
			feld.writeTerminal(feld.TERMINAL_WIDTH/2-4, feld.TERMINAL_HEIGHT/2+6, '>');
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2+6, "Spielfeld Farbe: " + feld.getFarbe());
		if (pointer == 0)
			feld.writeTerminal(feld.TERMINAL_WIDTH/2-4, feld.TERMINAL_HEIGHT/2+8, '>');
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2-2, feld.TERMINAL_HEIGHT/2+8, "Exit");
		
		feld.writeTerminalString(feld.TERMINAL_WIDTH/2-25, feld.TERMINAL_HEIGHT/2+11, "**Navigate with the arrow keys, press 'Enter' to change values**");
		checkInput();	// user input
		
		try {			// verursacht einen Delay 
			TimeUnit.MILLISECONDS.sleep(100);
		//	TimeUnit.SECONDS.sleep(1);    

		} catch (InterruptedException e) {
		    //Handle exception
		}
	}
	// der Spieler kann fuer jede ZeichenKlasse aus 3 verschiedenen Zeichen waehlen
	public void selectOption(int pointer) {	// hier wird die funktionalitaet der Optionen implementiert
		switch (pointer) {
		case 8:				// Spieler Zeichen
			spielerSign++;
			if (spielerSign >= 3)
				spielerSign = 0;
			switch (spielerSign) {
			case 0:
				Spieler.setSign('P');
				break;
			case 1:
				Spieler.setSign((char)0x2022);
				break;
			case 2:
				Spieler.setSign((char)0x04C1);
				break;
			}
			break;
		case 7:			// Dynamisches Hindernis Zeichen
			dinamischesHindernisSign++;
			if (dinamischesHindernisSign >= 3)
				dinamischesHindernisSign = 0;
			switch (dinamischesHindernisSign) {
			case 0:
				DynamischesHindernis.setSign('D');
				break;
			case 1:
				DynamischesHindernis.setSign((char)0x0040);
				break;
			case 2:
				DynamischesHindernis.setSign((char)0x04DC);
				break;
			}
			
			break;
		case 6:			// Statisches Hindernis Zeichen
			statischesHindernisSign++;
			if (statischesHindernisSign >= 3)
				statischesHindernisSign = 0;
			switch (statischesHindernisSign) {
			case 0:
				StatischesHindernis.setSign('X');
				break;
			case 1:
				StatischesHindernis.setSign((char)0x00D8);
				break;
			case 2:
				StatischesHindernis.setSign((char)0x203C);
				break;
			}
			break;
		case 5:			// Schluessel Zeichen
			schluesselSign++;
			if (schluesselSign >= 3)
				schluesselSign = 0;
			switch (schluesselSign) {
			case 0:
				Schluessel.setSign('S');
				break;
			case 1:
				Schluessel.setSign((char)0x03A8);
				break;
			case 2:
				Schluessel.setSign((char)0x00B6);
				break;
			}

			break;

		case 4:			// Wand Zeichen
			wandSign++;
			if (wandSign >= 3)
				wandSign = 0;
			switch (wandSign) {
			case 0:
				Wand.setSign((char)0x2588);
				break;
			case 1:
				Wand.setSign('W');
				break;
			case 2:
				Wand.setSign((char)0x0489);
				break;
			}
			break;
			
		case 3:			// Eingang Zeichen
			eingangSign++;
			if (eingangSign >= 3)
				eingangSign = 0;
			switch (eingangSign) {
			case 0:
				Eingang.setSign('E');
				break;
			case 1:
				Eingang.setSign((char)0x25a1);
				break;
			case 2:
				Eingang.setSign('O');
				break;
			}
			break;
			
		case 2:			// Ausgang Zeichen
			ausgangSign++;
			if (ausgangSign >= 3)
				ausgangSign = 0;
			switch (ausgangSign) {
			case 0:
				Ausgang.setSign('A');
				break;
			case 1:
				Ausgang.setSign((char)0x2126);
				break;
			case 2:
				Ausgang.setSign((char)0x0394);
				break;
			}
			break;
			
		case 1:			// aenderung der Farbe
			farbe++;
			if (farbe >= 4)
				farbe = 0;
			switch (farbe) {
			case 0:
				feld.setFarbe("RED");
				break;
			case 1:
				feld.setFarbe("BLUE");
				break;
			case 2:
				feld.setFarbe("GREEN");
				break;
			case 3:
				feld.setFarbe("DEFAULT");
				break;
			}
			
			break;
			
		case 0:			// Exit
			visible = false;
			break;
		}
		
	}
}
