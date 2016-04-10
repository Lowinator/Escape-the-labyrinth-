import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;


public class Terminal {

	private SwingTerminal terminal;		// werden benutzt um dem User das Labyrinth sichtbar zu machen
	private JFrame frame;
	
	public Terminal(int height, int width) {
		terminal = new SwingTerminal(height, width);
		
		terminal.enterPrivateMode();	
		
		frame = terminal.getJFrame();
		frame.setResizable(false);							// die Groesse des Terminals veraendert sich nicht
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);	// schliessen des Spiels
		
		terminal.setCursorVisible(false);	// der cursor ist nicht sichtbar, sieht besser aus
	}
	// wird benutzt um auf das Terminal zu schreiben
	public void write(int x, int y, char character) {
		terminal.moveCursor(x, y);
		terminal.putCharacter(character);
	}
	// getter
	public SwingTerminal getSwingTerminal() {	
		return terminal;
	}
}