//baire bones v0.1
public class BaireBones {
	public static void main(String[] args) {
		int W = 0;
		int X = 0;
		int Y = 0;
		int Z = 0;
		X = 0;
		X += 1;
		X += 1;
		Y = 0;
		Y += 1;
		Y += 1;
		Y += 1;
		Z = 0;
		while (X != 0) {
			W = 0;
			while (Y != 0) {
				Z += 1;
				W += 1;
				Y -= 1;
			}
			while (W != 0) {
				Y += 1;
				W -= 1;
			}
			X -= 1;
		}
		Y = X;
		System.out.println("W : " + W);
		System.out.println("X : " + X);
		System.out.println("Y : " + Y);
		System.out.println("Z : " + Z);
	}
}
