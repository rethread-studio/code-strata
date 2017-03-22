package fr.inria.View;

import fr.inria.DataStructure.Context;
import fr.inria.DataStructure.Execution;
import fr.inria.DataStructure.IsometricCoordinate;
import processing.core.PApplet;

import java.io.File;

/**
 * Created by nharrand on 21/03/17.
 */
public class MiniMapView extends PApplet {

    static int strokeLight = 255;
    public static Execution e;

    public void settings(){

        e = Context.currentExec;
        //size(800, 400, P2D);
        size(800, 400);

    }

    public void drawIsometricRect(int x, int y, int h, int w) {
        IsometricCoordinate a0 = new IsometricCoordinate(x, y, 0);
        IsometricCoordinate a1 = new IsometricCoordinate(a0.x, a0.y, a0.z+h);
        IsometricCoordinate a2 = new IsometricCoordinate(a0.x+w, a0.y, a0.z+h);
        IsometricCoordinate a3 = new IsometricCoordinate(a0.x+w, a0.y, a0.z);

        quad(a0.getIsoX(), a0.getIsoY(), a1.getIsoX(), a1.getIsoY(), a2.getIsoX(), a2.getIsoY(), a3.getIsoX(), a3.getIsoY());
    }

    public void setup() {
        drawMiniMaps();
    }

    public void drawMiniMaps() {
        //PGraphics pg;
        //pg = createGraphics(800, 400, P2D);
        stroke(0);
        strokeWeight(2);
        noFill();
        for(int i = 0; i < 8; i++) {
            background(255);
            //pg.beginDraw();
            for(int j = 0; j < 8; j++) {
                if((i != 5) || (j != 6))
                    drawLevel(j, j == i);
            }

            //pg.endDraw();
            //pg.save("mini/mini_" + i + ".png");
            save(e.outputDir + "/img/" + "mini/mini_" + i + ".png");
        }
    }

    public void drawLevel(int level, boolean highlighted) {
        if(highlighted) {
            stroke(239, 179, 38);
            fill(255, 255, 160);
        }
        switch (level) {
            case 0:
                drawIsometricRect(10,360,210,280);
                break;

            case 1:
                drawIsometricRect(310,360,150,200);
                break;

            case 2:
                drawIsometricRect(10,330,150,200);
                break;
            case 3:
                drawIsometricRect(230,330,150,200);
                break;

            case 4:
                drawIsometricRect(10,300,180,240);
                drawIsometricRect(10,300,75,100);
                break;

            case 5:
                drawIsometricRect(10,270,150,200);
                if(highlighted) drawIsometricRect(10,270,60,80);
                break;

            case 6:
                drawIsometricRect(10,270,60,80);
                break;

            case 7:
                drawIsometricRect(10,240,60,80);
                break;
        }
        if(highlighted) {
            stroke(0);
            fill(255);
        }

    }
}
