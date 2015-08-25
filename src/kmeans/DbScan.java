
package kmeans;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import kmeans.Point;
import kmeans.Visualize;

/**
 *
 * @author Suresh Babu Jothilingam
 * @author Nitish Ganesan
 */
class DbCluster{
    ArrayList<Double> inClusterX;
    ArrayList<Double> inClusterY;
    DbCluster(){
        inClusterX = new ArrayList<Double>();
        inClusterY = new ArrayList<Double>();
    }
    public boolean isAlreadyPresent(double x,double y){
        boolean status=false;
        for(int i=0;i<this.inClusterX.size();i++){
            if(this.inClusterX.get(i)==x){
                if(this.inClusterY.get(i)==y){
                    status=true;
                }
            }
        }
        return status;
    }
    public void addNode(double x,double y){
        if(!isAlreadyPresent(x, y)){
            this.inClusterX.add(x);
            this.inClusterY.add(y);
        }
    }
}
public class DbScan {
    static int minPoints;
    static int radius;
    //to keep track of visited nodes
    static ArrayList<Double> visitedX=new ArrayList<Double>();
    static ArrayList<Double> visitedY=new ArrayList<Double>();
    //to store noise nodes
    static ArrayList<Double> noiseX=new ArrayList<Double>();
    static ArrayList<Double> noiseY=new ArrayList<Double>();
    //data from the file
    static ArrayList<Double> nodeX = new ArrayList<Double>();
    static ArrayList<Double> nodeY = new ArrayList<Double>();
    //to store the clusters
    static ArrayList<DbCluster> clusters=new ArrayList<DbCluster>();
    //to get distance
    public static double getDistance(double x1, double x2, double y1, double y2) {
        double distance = Math.sqrt(((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2)));
        return distance;
    }
    //remove node from noise if it is in any cluster
    public static void removeFromNoise(double x,double y){
        int index=0;boolean status=false;
            for(int j=0;j<noiseX.size();j++){
                if(noiseX.get(j)==x && noiseY.get(j)==y)
                    status=true;
                    index = j;
            }
         if(status){
         noiseX.remove(index);noiseY.remove(index);
         }
    }
    //to expand the cluster when the points in the initial cluster are greater than minPoints
    public static void expandCluster(double x,double y,ArrayList<Double> neighX,ArrayList<Double> neighY,DbCluster c){
        c.addNode(x, y);
            for(int i=0;i<neighX.size();i++){
                if(!isVisited(neighX.get(i),neighY.get(i))){
                    visitedX.add(neighX.get(i));
                    visitedY.add(neighY.get(i));
                    ArrayList<ArrayList<Double>> tempNei=neighbourList(neighX.get(i), neighY.get(i));
                    //removeFromNoise(tempNei);
                    if(tempNei.get(0).size()>=minPoints){
                        neighX.addAll(tempNei.get(0));
                        neighY.addAll(tempNei.get(1));
                    }
                }
                if(!isPartOfAnyCluster(neighX.get(i),neighY.get(i))){
                        c.addNode(neighX.get(i), neighY.get(i));
                    }
            }
    }
    public static boolean isVisited(double x,double y) {
        boolean status=false;
        for(int i=0;i<visitedX.size();i++){
            if(visitedX.get(i)==x){
                if(visitedY.get(i)==y){
                    status=true;
                }
            }
        }
        return status;
    }
    public static boolean isPartOfAnyCluster(double x,double y){
        boolean status=false;
        for(int i=0;i<clusters.size();i++){
            for(int j=0;j<clusters.get(i).inClusterX.size();j++){
                if(clusters.get(i).inClusterX.get(j)==x){
                    if(clusters.get(i).inClusterY.get(j)==y){
                        status=true;
                    }
                }
            }
        }
        return status;
    }
    //returns the neighbour list of the given point
    public static ArrayList<ArrayList<Double>> neighbourList(double x,double y){
        ArrayList<ArrayList<Double>> neighbours=new ArrayList<ArrayList<Double>>();
        ArrayList<Double> neighX=new ArrayList<Double>();
        ArrayList<Double> neighY=new ArrayList<Double>();
        for(int i=0;i<nodeX.size();i++){
                //if the point lies inside circle
                double distance=getDistance(x, nodeX.get(i), y, nodeY.get(i));
                //System.out.println("neighbour list of "+x+" "+y);
                if(distance<=(double)radius){
                    //System.out.println(nodeX.get(i)+" "+nodeY.get(i));
                    if(!(x==nodeX.get(i) && y==nodeY.get(i))){
                        neighX.add(nodeX.get(i));
                        neighY.add(nodeY.get(i));
                    }
                }
        }
        neighbours.add(neighX);
        neighbours.add(neighY);
        return neighbours;
    }
    //perform dbscan
    public static void doDbScan(){
        //delete the noise values from the node and expand the core values
        for(int i=0;i<nodeX.size();i++){
            if(!isVisited(nodeX.get(i),nodeY.get(i))){
                visitedX.add(nodeX.get(i));
                visitedY.add(nodeY.get(i));
                ArrayList<ArrayList<Double>> tempNei=new ArrayList<ArrayList<Double>>();
                tempNei=neighbourList(nodeX.get(i), nodeY.get(i));
                if(tempNei.get(0).size()<minPoints){
                    noiseX.add(nodeX.get(i));
                    noiseY.add(nodeY.get(i));
                }else {
                    DbCluster c=new DbCluster();
                    expandCluster(nodeX.get(i), nodeY.get(i), tempNei.get(0), tempNei.get(1), c);
                    clusters.add(c);
                }
            }
        }
    }
    public void assign(String fName, int minPoint, int rad)throws Exception{
        ArrayList<Integer> indexSizeOne=new ArrayList<Integer>();
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
        Scanner input=new Scanner(System.in);
        minPoints=minPoint;
        radius=rad;
        Scanner scan=new Scanner(new File(fName));
        while(scan.hasNext()){
            String line=scan.nextLine();
            String[] xy=line.split("\t");
            Double x=Double.parseDouble(xy[0]);
            Double y=Double.parseDouble(xy[1]);
            nodeX.add(x);
            nodeY.add(y);
        }
        System.out.println("node x "+nodeX);
        System.out.println("node y "+nodeY);
        doDbScan();
        List<Point> points=new ArrayList<Point>();
        for(int i=0;i<clusters.size();i++){
            Random r=new Random();
            int R = (int)(Math.random()*256);
            int G = (int)(Math.random()*256);
            int B= (int)(Math.random()*256);
            Color c=new Color(R,G,B);
            c.brighter();
            for(int j=0;j<clusters.get(i).inClusterX.size();j++){
                if(!(clusters.get(i).inClusterX.size()==1)){
                    removeFromNoise(clusters.get(i).inClusterX.get(j),clusters.get(i).inClusterY.get(j));
                    Point p=new Point();
                    p.setColor(c);
                    Long x=Math.round(clusters.get(i).inClusterX.get(j));
                    Long y=Math.round(clusters.get(i).inClusterY.get(j));
                    p.setXY(x.intValue(),y.intValue());
                    points.add(p);
                }
                else{
                    indexSizeOne.add(i);
                }
            }
        }
        //add clusters which has only itself in the cluster to noise
        for(int i=0;i<indexSizeOne.size();i++){
            noiseX.add(clusters.get(indexSizeOne.get(i)).inClusterX.get(0));
            noiseY.add(clusters.get(indexSizeOne.get(i)).inClusterY.get(0));
        }
        //remove those clusters which has only one elements
        for(int i=0;i<indexSizeOne.size();i++){
            clusters.remove(indexSizeOne.get(i));
        }
        for(int i=0;i<noiseX.size();i++){
            Random r=new Random();
            int R = (int)(Math.random()*256);
            int G = (int)(Math.random()*256);
            int B= (int)(Math.random()*256);
            Color c=new Color(R,G,B);
            c.brighter();
            Point p=new Point();
                p.setColor(c);
                Long x=Math.round(noiseX.get(i));
                Long y=Math.round(noiseY.get(i));
                p.setXY(x.intValue(),y.intValue());
                points.add(p);
        }
        System.out.println("noise size "+noiseX.size());
        System.out.println("noise x "+noiseX);
        System.out.println("noise y "+noiseY);
        for(int i=0;i<clusters.size();i++){
            System.out.println("Cluster "+i+" size "+clusters.get(i).inClusterX.size());
            System.out.println("x "+clusters.get(i).inClusterX);
            System.out.println("y "+clusters.get(i).inClusterY);
        }
        Visualize vis=new Visualize("Db Scan");
        vis.showPoints(points);
    }
}
