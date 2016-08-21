
public class Schluessel extends Koordinate {
	
	private static char sign = 'S';	/* static weil alle Schluessel Objekte das
									gleiche Zeichen haben sollen*/
	
	public Schluessel(int x, int y){
		super(x,y);
	}
	// relevante getter und setter
	public static void setSign(char sign) {
		Schluessel.sign = sign;
	}

	public static char getSign() {
		return sign;
	}

}
