package fr.inria.DataStructure;

/**
 * Created by nharrand on 21/03/17.
 */
public class IsometricCoordinate {
    public int x = 0, y = 0, z = 0;
    public static double k = Math.sqrt(2.0);
    public static double lx = Math.sqrt(5.0) / 2.0;
    public static double ly = Math.sqrt(5.0);

    public IsometricCoordinate(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getIsoX() {
        return x + (int) (z / lx);
    }

    public int getIsoY() {
        return y - (int) (z / ly);
    }

}
