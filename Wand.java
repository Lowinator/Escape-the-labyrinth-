
public class Wand extends Koordinate {
	
	private static char sign = (char)0x2588;	/* static weil alle Wand Objekte das
									 			gleiche Zeichen haben sollen*/
	public Wand(int x, int y) {
		super(x,y);
	}
	// relevante getter und setter
	public static void setSign(char sign) {
		Wand.sign = sign;
	}
	public static char getSign() {
		return sign;
	}
}