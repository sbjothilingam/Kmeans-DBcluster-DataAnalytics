/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kmeans;

/**
 *
 * @author suresh
 */

/**
 *Visualize.java
 * 
 * Version: 
 *     $Id: $
 * 
 * Revisions: 
 *     $Log: $
 *
 */


import java.awt.*;
import javax.swing.*;
import java.util.*;

/**
 * A simple GUI used for visualizing clusters
 * 
 * @author Trudy Howles	tmh@cs.rit.edu
 */

public class Visualize {


    //  I set the JFrame size at 200 by 200 so you can easily distinguish points
    //  My data used to test your program makes the assumption that [200,200] is
    //  a valid point on the Visualizer.
    private static final int MAX_SIZE = 200;

    JFrame jf = null;
    JPanel jp = new JPanel();
    JButton points[] [];


    /** 
     * Constructor for a Visualize object that uses "Unknown" in the title bar
     *  
     */

    public Visualize () {
        this ("Unknown");
    }


    /** 
     * Constructor for a Visualize object that uses the name provided 
     * in the  title bar
     *   
     * @param name  the name to use in the window's title bar 
     */
    public Visualize (String name) {

       jf = new JFrame (name);
       try {
            UIManager.setLookAndFeel (UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println ("Could not change LAF");
        }


        jp.setLayout (new GridLayout (MAX_SIZE, MAX_SIZE));

        jf.setSize (1400,1450);
        
        points = new JButton [MAX_SIZE] [MAX_SIZE];

        for (int loop = MAX_SIZE - 1; loop >= 0; loop--) {
            for (int loop2 = 0; loop2 < MAX_SIZE; loop2++) {
                JButton but = new JButton ( );
                but.setText ("" + loop2 + "," + loop );
                but.setToolTipText (""+ loop2 + "," + loop);
                points[loop2][loop] = but;
                but.setBackground (Color.white);
                but.setForeground (Color.white);
                jp.add (but);
            }
        }
        jf.getContentPane().add (jp);
        jf.setBackground(Color.black);
        jp.setBackground(Color.black);
        //  remove comment to see initial (blank) window
        //jf.setVisible (true);

    }


    /**  
     * Display the points in the List using the color specified.
     *
     * @param data a List <Point> containing the data to plot
     * @param color the Color.color to use when displaying these points
     *
     * You should define a Point class with x and y coordinates (integers)
     * and a Color as shown below.
     *
     */
    

    public void showPoints (java.util.List <Point> data){
        Iterator <Point> it = data.iterator();
        while (it.hasNext()) {
            Point p = it.next();
            int x = p.getX();
            int y = p.getY();
            Color color = p.getColor();

            points[x][y].setBackground (color);
        }
            jf.setVisible (true);
    }

}

    
