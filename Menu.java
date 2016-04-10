import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import com.googlecode.lanterna.input.Key;

public abstract class Menu {
	
	int pointer;		// gibt an auf welches Element im Menue gezeigt wird
	final int numberOfOptions;		// Anzahl der Optionen
	Spielfeld feld;		
	Key key;		// speichert den User Input temporaer
	GameLoop game;
	
	public Menu(Spielfeld feld, int numberOfOptions, GameLoop game) {
		this.feld = feld;
		this.numberOfOptions = numberOfOptions;
		pointer = numberOfOptions - 1;
		this.game = game;
	}
	// prueft ob es einen User input gibt und Entscheider was passiert
	public void checkInput() {
		key = feld.getSwingTerminal().readInput();		// user input wird aufgefangen
		
		if (key != null)
			switch (key.getKind()){		
			case ArrowDown:			// pointer bewegt sich nach unten
				pointerDown();
				break;
			case ArrowUp:			// pointer bewegt sich nach oben
				pointerUp(); 
				break;
			case Enter:				// option im Menue wird ausgewaehlt
				selectOption(pointer);
				break;
			
			case Escape:			// Menue wird verlassen
				feld.getSwingTerminal().exitPrivateMode();
				System.exit(0);
				break;
			}
		
	}
	
	public void selectOption(int pointer){};		// wird in den Unterklassen definiert
	
	
	public void pointerDown() {		// pointer bewegt sich nach unten
		if (pointer > 0) {
			pointer--;
		} else {
			pointer = numberOfOptions - 1;
		}
	}
	
	public void pointerUp() {		// pointer bewegt sich nach oben
		if (pointer < numberOfOptions - 1) {
			pointer++;
		} else {
			pointer = 0;
		}
	}
	
}
