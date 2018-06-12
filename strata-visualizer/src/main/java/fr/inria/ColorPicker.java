package fr.inria;

import processing.core.PFont;

import java.util.List;

/**
 * Created by nharrand on 14/03/17.
 */
public class ColorPicker {
    public ColorPicker() {
    }

    //Fully (saturated / lighted) complementary triplets are formed as following
    // (255, 0, x), (x, 255, 0), ((0, x, 255)
    enum order {DSZ, ZDS, SZD, SDZ, ZSD, DZS}

    int step;


    private int[][] colors;

    public ColorPicker(int r, int g, int b, int n) {
        this(r, g, b, n, 0);
    }

    public ColorPicker(int r, int g, int b, int n, int s) {
        colors = new int[n][3];
        if(step == 0) {
            this.step = 256 / ((int) Math.ceil(n / 3.0));
        } else {
            this.step = s;
        }

        colors[0][0] = r;
        colors[0][1] = g;
        colors[0][2] = b;

        int[] c;
        for(int i = 1; i < n; i++) {
            if(i % 3 == 0) {
                c = addStep(colors[i-3][0], colors[i-3][1], colors[i-3][2]);
                colors[i][0] = c[0];
                colors[i][1] = c[1];
                colors[i][2] = c[2];
            } else {
                colors[i][0] = colors[i-1][2];
                colors[i][1] = colors[i-1][0];
                colors[i][2] = colors[i-1][1];
            }
        }
        //System.out.println("Stop");
    }

    public int[] getColor(int i) {
        int[] c = new int[3];
        c[0] = colors[i][0];
        c[1] = colors[i][1];
        c[2] = colors[i][2];
        return c;
    }


    int[] addStep(int r, int g, int b) {
        switch (getOrder(r,g,b)) {
            case DSZ:
                if(g + step > 255) {
                    r -= step - (256 - g);
                    g = 255;
                } else {
                    g += step;
                }
                break;
            case ZDS:
                if(b + step > 255) {
                    g -= step - (256 - b);
                    b = 255;
                } else {
                    b += step;
                }
                break;
            case SZD:
                if(r + step > 255) {
                    b -= step - (256 - r);
                    r = 255;
                } else {
                    r += step;
                }
                break;
            case SDZ:
                if(r - step < 0) {
                    b += step - r;
                    r = 0;
                } else {
                    r -= step;
                }
                break;
            case ZSD:
                if(g - step < 0) {
                    r += step - g;
                    g = 0;
                } else {
                    g -= step;
                }
                break;
            case DZS:
                if(b - step < 0) {
                    b += step - b;
                    g = 0;
                } else {
                    b -= step;
                }
                break;
        }
        int[] c = new int[3];
        c[0] = r;
        c[1] = g;
        c[2] = b;
        return c;
    }

    order getOrder(int r, int g, int b) {
        if((r == 255) && (b == 0)) {
            return order.DSZ;
        } else if((r == 0) && (g == 255)) {
            return order.ZDS;
        } else if((g == 0) && (b == 255)) {
            return order.SZD;
        } else if((g == 255) && (b == 0)) {
            return order.SDZ;
        } else if((r == 0) && (b == 255)) {
            return order.ZSD;
        } else {
            return order.DZS;
        }
    }
}
