package domain.Logic;



public class Pair {

	private int positionX;
	private int positionY;

	public Pair(int x, int y) {
		this.positionX = x;
		this.positionY = y;
	}

	public Pair(String pair) {
		// character subtraction to get X value from char
		this.positionX = pair.toCharArray()[0] - 'a';
		this.positionY = Integer.parseInt(pair.substring(1, 2)) - 1;
	}

	public boolean isPairValid() {
		return (positionX >= 0 && positionX < 8) && (positionY >= 0 && positionY < 8);
	}

	public int getX() {
		return this.positionX;
	}

	public int getY() {
		return this.positionY;
	}

	public void setX(int x) {
		this.positionX = x;
	}

	public void setY(int y) {
		this.positionY = y;
	}

	@Override
	public String toString() {
		return getReadablePair();
	}

	@Override
	public boolean equals(Object o) { //compare the X and Y fields of Pairs
		if (o == this)
			return true;
		if (!(o instanceof Pair)) {
			return false;
		}
		Pair otherpair = (Pair) o;
		return this.positionX == otherpair.getX() && this.positionY == otherpair.getY();
	}

	@Override
	public int hashCode() {
		return 17 * this.hashCode() * positionX - positionY;
	}
	
	private String getReadablePair() {
		String parsedString = "";
		parsedString = (char) (positionX + 'a') + Integer.toString(positionY + 1);
		return parsedString;
	}
}