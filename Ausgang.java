
public class Ausgang extends Koordinate {
	
	private static char sign = 'A';	/* static weil alle Ausgang Objekte das
									gleiche Zeichen haben sollen*/
	
	public Ausgang(int x, int y) {
		super(x,y);
	}
	// relevante getter und setter
	public static void setSign(char sign) {
		Ausgang.sign = sign;
	}
	public static char getSign() {
		return sign;
	}


}
