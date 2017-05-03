package fr.inria;

/**
 * Created by nharrand on 03/05/17.
 */
public class SimpleColor extends ColorPicker{

    int[][] colors;

    public SimpleColor () {
        colors = new int[10][3];

        int i = 0;
        colors[i][0] = 0xFF;
        colors[i][1] = 0xBF;
        colors[i][2] = 0x00;
        i++;
        colors[i][0] = 0xE3;
        colors[i][1] = 0x26;
        colors[i][2] = 0x36;
        i++;
        colors[i][0] = 0x99;
        colors[i][1] = 0x66;
        colors[i][2] = 0xCC;
        i++;
        colors[i][0] = 0x00;
        colors[i][1] = 0xFF;
        colors[i][2] = 0xFF;
        i++;
        colors[i][0] = 0x7F;
        colors[i][1] = 0xFF;
        colors[i][2] = 0x00;
        i++;
        colors[i][0] = 0x00;
        colors[i][1] = 0x47;
        colors[i][2] = 0xAB;
        i++;
        colors[i][0] = 0xCC;
        colors[i][1] = 0x55;
        colors[i][2] = 0x00;
        i++;
    }

    public SimpleColor(int r, int g, int b, int n, int s) {
        this();
    }

    public SimpleColor(int r, int g, int b, int n) {
        this();
    }


    public int[] getColor(int i) {
        int[] c = new int[3];
        c[0] = colors[i][0];
        c[1] = colors[i][1];
        c[2] = colors[i][2];
        return c;
    }
}
