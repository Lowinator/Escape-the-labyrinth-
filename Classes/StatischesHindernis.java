
public class StatischesHindernis extends Koordinate{
	
	private static char sign = 'X'; 	/* ☢☼ static weil alle Objekte das
								gleiche Zeichen haben sollen*/

	public StatischesHindernis(int x, int y){
		super(x,y);
	}
	// relevante getter und setter
	public static void setSign(char sign) {
		StatischesHindernis.sign = sign;
	}
	public static char getSign() {
		return sign;
	}
}
