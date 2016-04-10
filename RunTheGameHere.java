/**Das Spiel erfuellt die Mindestanforderungen und geht (in den Punkten unten beschrieben) ueber sie hinaus.
 * Erweiterungen:
 * - Hauptmenue
 * - Eine Punkte Liste (in Scoreboard)
 * - Die moglichkeit die farbe des Labyrinths und die
 * 	 Zeichen der Zeichenklassen zu aendern (in Skins)
 * - Eine HP bar zur darstellung der verbliebenden Leben
 * - Graphische darstellung der gespeicherten Spiele (in Load Game)
 * - Bei dem einsammeln von einem Schluessel bekommt der Spieler ein Leben
 * - Ein Menu nachdem der Spieler gewinnt/verliert
 * */

/**
 * @author David Glavas
 *
 */
public class RunTheGameHere {
	
	public static void main(String[] args) {
		
		GameLoop game = new GameLoop();	// instanz eines Spiels wird erstellt
		
		game.runGame();	// Spiel wird ausgefuehrt
	}
}
