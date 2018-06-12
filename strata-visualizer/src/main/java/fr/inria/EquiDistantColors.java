package fr.inria;

/**
 * Created by nharrand on 29/05/17.
 */
public class EquiDistantColors extends ColorPicker {

    int[][] colors;
    public static int[] hsvToRgb(double hue, double saturation, double value) {

        int h = (int) (hue * 6.0);
        double f = hue * 6.0 - h;
        double p = value * (1.0 - saturation);
        double q = value * (1.0 - f * saturation);
        double t = value * (1.0 - (1.0 - f) * saturation);



        switch (h) {
            case 0: return rgbArray(value, t, p);
            case 1: return rgbArray(q, value, p);
            case 2: return rgbArray(p, value, t);
            case 3: return rgbArray(p, q, value);
            case 4: return rgbArray(t, p, value);
            case 5: return rgbArray(value, p, q);
            default: throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + saturation + ", " + value);
        }
    }

    public static int[] rgbArray(double r, double g, double b) {
        int rgb[] = new int[3];
        rgb[0] = (int) (r * 256);
        if(rgb[0] == 256) rgb[0] = 255;
        rgb[1] = (int) (g * 256);
        if(rgb[1] == 256) rgb[1] = 255;
        rgb[2] = (int) (b * 256);
        if(rgb[2] == 256) rgb[2] = 255;
        return rgb;
    }

    public static double fmod(double d, double mod) {
        if(d < 0.0) return d;
        else {
            while(d >= mod) d -= mod;
        }
        return d;
    }


    public EquiDistantColors(int r, int g, int b, int n, int s) {
        this(r,g,b,n);
    }

    public EquiDistantColors(int r, int g, int b, int n) {

        colors = new int[n][3];
        for(int i = 0; i < n; i++) {
            double d = fmod((double)i * 0.618033988749895, 1.0);
            //double d = ((double)i) / ((double)n) ;
            colors[i] = hsvToRgb(d, 0.5, 1.0);
        }
    }


    public int[] getColor(int i) {
        int[] c = new int[3];
        c[0] = colors[i][0];
        c[1] = colors[i][1];
        c[2] = colors[i][2];
        return c;
    }

}
