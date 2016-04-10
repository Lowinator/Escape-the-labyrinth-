
public class Spieler extends Koordinate {
	
	private static char sign = 'P';	/*  ♀ static weil alle Spieler Objekte das
									gleiche Zeichen haben sollen*/
	
	public final int maxLifes = 9;			// maximale Anzahl von Leben
	private int lifesLeft = maxLifes;	// Leben zu einem bestimmten Zeitpunkt
	private boolean hasKey = false;		// gibt an ob ein Schluessel eingesammelt wurde
	private boolean winGame = false;	// gibt an ob der Spieler gewonnnen hat
	private int punkte = 0;				// gibt die Anzahl von Punkten an
	
	public Spieler(int x, int y) {
		super(x,y);		// Koordinaten des Spielers werden bestimmt
	}
	
	// getter und setter fuer relevante Attribute
	public void setWinGame(boolean temp) {
		this.winGame = temp;
	}
	public static void setSign(char sign) {
		Spieler.sign = sign;
	}
	public boolean getWinGame() {
		return winGame;
	}
	public void addPunkte(int punkte) {
		this.punkte += punkte;		
	}
	public int getPunkte() {
		return punkte;
	}
	public boolean getHasKey() {
		return hasKey;
	}
	public void setHasKey(boolean temp) {
		this.hasKey = temp;
	}
	public void setLifesLeft(int lifesLeft) {
		this.lifesLeft = lifesLeft;
	}
	public int getLifesLeft() {
		return lifesLeft;
	}
	
	public static char getSign() {
		return sign;	// getter fuer den character von Spieler Objekt
	}
	
	public void moveUp(Spielfeld feld) {	// Spieler bewegt sich nach oben
		
			if (interact(getX(), getY() - 1, feld)) {	// gibt an ob der Spieler sich auf das Feld bewegen kann und was evtl. passiert (leben, schluessel)
				feld.getSwingTerminal().applyForegroundColor(255,20,147);
				feld.setLabyrinth(getX(), getY(), ' ');				// Spieler wird aus dem Labyrinth geloescht
				feld.setScreen(feld.getScreenX(), feld.getScreenY(), ' ');		// Spieler wird aus dem Screen geloescht
				feld.writeTerminal(feld.getScreenX(), feld.getScreenY(), ' ');
				
				setY(getY() - 1); 							// Position/Koordinate des Spielers aendert sich
				feld.setScreenY(feld.getScreenY() - 1);
						
				feld.setLabyrinth(getX(), getY(), sign);		// Spieler wird auf seinen neuen Platz eingetragen
				feld.setScreen(feld.getScreenX(), feld.getScreenY(), sign);
				feld.writeTerminal(feld.getScreenX(), feld.getScreenY(), sign);
				feld.getSwingTerminal().applyForegroundColor(255,255,255);
			}
		} 
	
	public void moveDown(Spielfeld feld) {	// Spieler bewegt sich nach unten
		
			if (interact(getX(), getY() + 1, feld)) {	// gibt an ob der Spieler sich auf das Feld bewegen kann und was evtl. passiert (leben, schluessel)
				feld.getSwingTerminal().applyForegroundColor(255,20,147);
				feld.setLabyrinth(getX(), getY(), ' ');				// Spieler wird aus dem Labyrinth geloescht
				feld.setScreen(feld.getScreenX(), feld.getScreenY(), ' ');		// Spieler wird aus dem Screen geloescht
				feld.writeTerminal(feld.getScreenX(), feld.getScreenY(), ' ');
				
				setY(getY() + 1); 							// Position/Koordinate des Spielers aendert sich
				feld.setScreenY(feld.getScreenY() + 1);
				
				feld.setLabyrinth(getX(), getY(), sign);			// Spieler wird auf seinen neuen Platz eingetragen
				feld.setScreen(feld.getScreenX(), feld.getScreenY(), sign);
				feld.writeTerminal(feld.getScreenX(), feld.getScreenY(), sign);
				feld.getSwingTerminal().applyForegroundColor(255,255,255);
			}
		} 
	
	public void moveLeft(Spielfeld feld) {	// Spieler bewegt sich nach links

			if (interact(getX() - 1, getY(), feld)) {	// gibt an ob der Spieler sich auf das Feld bewegen kann und was evtl. passiert (leben, schluessel)
				feld.getSwingTerminal().applyForegroundColor(255,20,147);
				feld.setLabyrinth(getX(), getY(), ' ');						// Spieler wird aus dem Labyrinth geloescht
				feld.setScreen(feld.getScreenX(), feld.getScreenY(), ' ');		// Spieler wird aus dem Screen geloescht
				feld.writeTerminal(feld.getScreenX(), feld.getScreenY(), ' ');
				
				setX(getX() - 1); 							// Position/Koordinate des Spielers aendert sich
				feld.setScreenX(feld.getScreenX() - 1);
	
				feld.setLabyrinth(getX(), getY(), sign);			// Spieler wird auf seinen neuen Platz eingetragen
				feld.setScreen(feld.getScreenX(), feld.getScreenY(), sign);
				feld.writeTerminal(feld.getScreenX(), feld.getScreenY(), sign);
				feld.getSwingTerminal().applyForegroundColor(255,255,255);
			}
		}
	
	public void moveRight(Spielfeld feld) {		// Spieler bewegt sich nach rechts

			if (interact(getX() + 1, getY(), feld)) {		// gibt an ob der Spieler sich auf das Feld bewegen kann und was evtl. passiert (leben, schluessel)
				feld.getSwingTerminal().applyForegroundColor(255,20,147);
				feld.setLabyrinth(getX(), getY(), ' ');					// Spieler wird aus dem Labyrinth geloescht
				feld.setScreen(feld.getScreenX(), feld.getScreenY(), ' ');		// Spieler wird aus dem Screen geloescht
				feld.writeTerminal(feld.getScreenX(), feld.getScreenY(), ' ');
				
				setX(getX() + 1); 					// Position/Koordinate des Spielers aendert sich
				feld.setScreenX(feld.getScreenX() + 1);
				
				feld.setLabyrinth(getX(), getY(), sign);		// Spieler wird auf seinen neuen Platz eingetragen
				feld.setScreen(feld.getScreenX(), feld.getScreenY(), sign);
				feld.writeTerminal(feld.getScreenX(), feld.getScreenY(), sign);
				feld.getSwingTerminal().applyForegroundColor(255,255,255);
			}
		}
	
	public void subLife(Spielfeld feld) {		// Spieler verliert ein Leben
		if (lifesLeft >= 1) {
			lifesLeft--;		// Spieler verliert 1 Leben
			feld.getTerminal().write(5, 1, (char)(lifesLeft + 48));
			printHealthBar(feld);
		}
	}
	
	public void addLife(Spielfeld feld) {		// Spieler bekommt ein Leben
		if (lifesLeft < maxLifes) {
			lifesLeft++;		// Spieler bekommt 1 Leben
			printHealthBar(feld);
		}
	}
	public void printHealthBar(Spielfeld feld) {	// gibt die HP bar aus
		
		feld.writeTerminalString(1, 1, "Health: ");
		for (int i = 0; i < this.maxLifes; i++) {
			feld.writeTerminal(9+i, 1, ' ');
		}
		
		feld.getTerminal().getSwingTerminal().applyBackgroundColor(0,255,0);
		
		for (int i = 0; i < feld.getSpielFigur().getLifesLeft(); i++) {
			feld.writeTerminal(9+i, 1, ' ');
		}
		
		feld.getTerminal().getSwingTerminal().applyBackgroundColor(46,52,54);
	}
	
	public void foundKey(Spielfeld feld) {	// Spieler findet einen Schluessel
		hasKey = true;
		feld.writeTerminalString(6, 2, "Yes");
	}
	public void displayPunkte(Spielfeld feld) {		// gibt die Anzahl der Punkte aus
		feld.writeTerminalString(9, 3, Integer.toString(getPunkte()));
	}
	// prueft und Entscheidet ob der Spieler sich auf das gewuenschte feld bewegen kann
	private boolean interact(int newX, int newY, Spielfeld feld) {
		
		if ((newY >= feld.getShiftLabyrinth()) && newY < feld.getHeight() && newX >= 0 && newX < feld.getWidth() ) {	// verhindert das der Spieler aus dem Labyrinth navigiert 
			char fieldType = feld.identifyField(newX, newY);
			if (fieldType == Wand.getSign()) {
				return false;
			}
			else if (fieldType == Schluessel.getSign()) {	// falls der Spieler sich auf einen Schluessel bewegt
				foundKey(feld);
				addPunkte(100);
				addLife(feld);
				displayPunkte(feld);
				return true;
			}
			else if (fieldType == StatischesHindernis.getSign()) {	// falls der Spieler sich auf ein Statisches Hindernis bewegt
				subLife(feld);
				return false;
			}
			else if (fieldType == DynamischesHindernis.getSign()) {	// falls der Spieler sich auf ein Dynamisches Hindernis bewegt
				subLife(feld);
				return false;
			}
			else if (fieldType == Eingang.getSign()) {	// falls der Spieler sich auf einen Eingang bewegt
				return false;
			}
			else if (fieldType == Ausgang.getSign()) {	// falls der Spieler sich auf einen Ausgang bewegt
				if (hasKey == true){
					winGame = true;
				return true;
				} else
					return false;
			}
			else {	// falls der Weg frei ist
				return true;
			}
		} else {
			return false;
		}
	}
}