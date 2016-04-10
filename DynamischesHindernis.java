import java.util.concurrent.ThreadLocalRandom;

public class DynamischesHindernis extends Koordinate {
	
	private static char sign = 'D';	/* static weil alle Objekte das
								gleiche Zeichen haben sollen*/
	public Koordinate lastCoordinate = new Koordinate(getX(), getY());	// Koordinaten auf dem sich das Hindernis waehrend des letzten zugs befandt
	private boolean chaseLogic = true;		// wird benutzt um zu entscheiden wie sich das Hindernis verhaelt
	private int speed = 1000;	// kleiner -> schneller
	private int counter = 0;	// entscheidet ob sich das Hinderniss random bewegt oder nicht
	private int direction;		// Bewegungs Richtung des Hidnernisses im random mode
	private boolean goRandomly = false;	// die Logik des Hindernisses andert sich, von quasi-schlau auf random
	
	// relevante getter und setter
	public static void setSign(char sign) {
		DynamischesHindernis.sign = sign;
	}
	public int getSpeed() {
		return speed;
	}
	
	public DynamischesHindernis(int x, int y){
		super(x,y);
	}

	public static char getSign() {
		return sign;
	}
	
	/* Die Dynamischen Hindernisse haben 2 verschidene Verhalten. Am Anfang sind sie stets nach dem Spieler
	 * her. Falls ein fuer sie unueberwindbares Hinderniss (wand) zwischen ihnen und dem Spieler ist dann 
	 * fangen sie an zufaellig ihren Weg zu waehlen (random mode). 
	 * */
	public void move(int x, int y, Spielfeld feld, Spieler spieler) {	// bewegt das dynamische Hindernis
		if (lastCoordinate.getX() == getX() && lastCoordinate.getY() == getY()) {	// falls sich das Hinderniss 5 Zuege lang nicht bewegt -> random mode 
			counter++;
			if (counter >= 5) {
				direction = ThreadLocalRandom.current().nextInt(0, 3 + 1);	// die Richtung wird zufaellig bestimmt
				goRandomly = true;
				counter = 4;
			}
		} else {
			counter = 0;
		}
		
		lastCoordinate.setX(getX());	// die letzte Position des Hindernisses wird gesetzt, so wird ermittelt ob sich das Hindernis bewegt
		lastCoordinate.setY(getY());
		if (goRandomly) {
			switch (direction) {
			case 0:		// bewegt sich nach rechts
				if (interact(getX()+1, getY(), feld, spieler))	
					setX(getX() + 1);
				break;
			case 1:		// bewegt sich nach links
				if (interact(getX()-1, getY(), feld, spieler))
					setX(getX() - 1);
				break;
			case 2:		// bewegt sich nach unten
				if (interact(getX(), getY()+1, feld, spieler))
					setY(getY() + 1);
				break;
			case 3:		// bewegt sich nach oben
				if (interact(getX(), getY()-1, feld, spieler))
					setY(getY() - 1);
				break;
			}
		}
		else if (chaseLogic) {
				if (getX() < x) {	// Hindernis bewegt sich nach rechts
					if (interact(getX()+1, getY(), feld, spieler))	
						setX(getX() + 1);
					else
						chaseLogic = false;	// falls die vorgesehene bewegung nicht moeglich ist aendert sich die zukuenftige
				}
				else if (getX() > x) {	// Hindernis bewegt sich nach links
					if (interact(getX()-1, getY(), feld, spieler))
						setX(getX() - 1);
					else
						chaseLogic = false;	// falls die vorgesehene bewegung nicht moeglich ist aendert sich die zukuenftige
				}
				else if (getX() == x){
					chaseLogic = false;		// falls die vorgesehene bewegung nicht moeglich ist aendert sich die zukuenftige
				}
			} else {
				if (getY() < y) {	// Hindernis bewegt sich nach unten
					if (interact(getX(), getY()+1, feld, spieler))
						setY(getY() + 1);
					else
						chaseLogic = true;	// falls die vorgesehene bewegung nicht moeglich ist aendert sich die zukuenftige
				}
				else if (getY() > y) {	// Hindernis bewegt sich nach oben
					if (interact(getX(), getY()-1, feld, spieler))
						setY(getY() - 1);
					else
						chaseLogic = true;	// falls die vorgesehene bewegung nicht moeglich ist aendert sich die zukuenftige
				}
				else if (getY() == y) {
					chaseLogic = true;	// falls die vorgesehene bewegung nicht moeglich ist aendert sich die zukuenftige
				}
			}
	}
	// prueft und Entscheidet ob sich das Dynamische Hindernis auf das gewuenschte feld bewegen kann
	private boolean interact(int newX, int newY, Spielfeld feld, Spieler spieler) {
		
		char fieldType = feld.identifyField(newX, newY);
		
			if (fieldType == Wand.getSign()) {		// falls sich das Hindernis auf die Position einer Wand bewegt
				return false;
			}
			else if (fieldType == Spieler.getSign())	{	// falls sich das Hindernis auf die Position eines Spielers bewegt
				spieler.subLife(feld);
				return false;
			}
			else if (fieldType == Schluessel.getSign()) {	// falls sich das Hindernis auf die Position eines Schluessels bewegt
				return false;
			}
			else if (fieldType == StatischesHindernis.getSign()) {	// falls sich das Hindernis auf die Position eines Statischen Hindernisses bewegt
				return false;
			}
			else if (fieldType == DynamischesHindernis.getSign()) {	// falls sich das Hindernis auf die Position eines Dynamischen Hindernisses bewegt
				return false;
			}
			else if (fieldType == Eingang.getSign() || fieldType == Ausgang.getSign()) {	// falls sich das Hindernis auf die Position eines Eingangs/Ausgangs bewegt
				return false;
			}
			else if (newX > feld.getWidth() || newX < 0 || newY > feld.getHeight() || newY < feld.getShiftLabyrinth()) {	// verhindert das sich das Hindernis aus dem Labyrinth bewegt 
				return false;
			}
			else {	// falls der Weg fuer das Hindernis frei ist
				return true;
			} 
			
		
	}

}
