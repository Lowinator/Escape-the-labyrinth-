
public class Eingang extends Koordinate {
	
	private static char sign = 'E';	/* static weil alle Eingang Objekte das
	 								gleiche Zeichen haben sollen*/
	
	public Eingang(int x, int y){
		super(x,y);
	}
	// relevante getter und setter
	public static void setSign(char sign) {
		Eingang.sign = sign;
	}
	public static char getSign() {
		return sign;
	}

}
