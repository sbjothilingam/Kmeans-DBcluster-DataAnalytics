/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kmeans;

import java.awt.Color;

/**
 *
 * @author suresh
 */
public class Point {
    int x;
    int y;
    Color c;
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    public Color getColor(){
        return this.c;
    }
    public void setXY(int x,int y){
        this.x=x;
        this.y=y;
    }
    public void setColor(Color c){
        this.c=c;
    }
}
