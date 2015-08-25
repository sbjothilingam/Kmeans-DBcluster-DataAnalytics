
package kmeans;

import java.awt.Color;
import java.io.File;
import java.util.*;

/**
 *
 * @author Suresh Babu Jothilingam
 * @author Nitish Krishna Ganesan
 */
class KCluster {

    double x;
    double y;
    ArrayList<Double> inClusterX;
    ArrayList<Double> inClusterY;

    KCluster(double x, double y) {
        this.x = x;
        this.y = y;
        inClusterX = new ArrayList<Double>();
        inClusterY = new ArrayList<Double>();
    }

    public void addNode(double x, double y) {
        inClusterX.add(x);
        inClusterY.add(y);
    }

    public void removeNode(double x, double y) {
        if (inClusterX.contains(x) && inClusterY.contains(y)) {
            inClusterX.remove(inClusterX.indexOf(x));
            inClusterY.remove(inClusterY.indexOf(y));
        }
    }

    public void updateCentroid(double x, double y) {
        this.x = x;
        this.y = y;
    }
}


public class Kmeans {

    static ArrayList<KCluster> clusters = new ArrayList<KCluster>();
    //to store the X and Y points of each instance from file
    static ArrayList<Double> nodeX = new ArrayList<Double>();
    static ArrayList<Double> nodeY = new ArrayList<Double>();
    //keep track of centroids for convergence
    static ArrayList<Double> centroidX = new ArrayList<Double>();
    static ArrayList<Double> centroidY = new ArrayList<Double>();
    static boolean convergence = false;

    public static double getEuclideanDistance(double x1, double x2, double y1, double y2) {
        double distance = Math.sqrt(((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2)));
        return distance;
    }
    //round robin 
    public static void roundRobinAssignment() {
        int clusterIndex = 0;
        for (int i = 0; i < nodeX.size(); i++) {
            if (clusterIndex == clusters.size()) {
                clusterIndex = 0;
            }
            clusters.get(clusterIndex).addNode(nodeX.get(i), nodeY.get(i));
            clusterIndex++;
        }
    }
    //sequential
    public static void sequentialAssignment() {
        int chunkSize = nodeX.size() / clusters.size();
        int startIndex = 0;
        int clusterIndex = 0;
        int amountOfDataAdded = 0;
        while (clusterIndex != (clusters.size())) {
            for (int i = startIndex; i < (amountOfDataAdded + chunkSize); i++) {
                clusters.get(clusterIndex).addNode(nodeX.get(i), nodeY.get(i));
            }
            amountOfDataAdded += chunkSize;
            startIndex = amountOfDataAdded;
            clusterIndex++;
        }
        //add the remaining elements to the last cluster
        for (int i = startIndex; i < nodeX.size(); i++) {
            clusters.get(clusterIndex).addNode(nodeX.get(i), nodeY.get(i));
        }
    }
    //random
    public static void randomAssignment() {
        ArrayList<Integer> uniqueRandom = new ArrayList<Integer>();
        int chunkSize = nodeX.size() / clusters.size();
        int startIndex = 0;
        int clusterIndex = 0;
        int amountOfDataAdded = 0;
        while (clusterIndex != (clusters.size())) {
            for (int i = startIndex; i < (amountOfDataAdded + chunkSize); i++) {
                int randomPoint = new Random().nextInt(nodeX.size());
                while (uniqueRandom.contains(randomPoint)) {
                    randomPoint = new Random().nextInt(nodeX.size());
                }
                clusters.get(clusterIndex).addNode(nodeX.get(randomPoint), nodeY.get(randomPoint));
            }
            amountOfDataAdded += chunkSize;
            startIndex = amountOfDataAdded;
            clusterIndex++;
        }
        //add remaming elements to the last cluster
        for (int i = startIndex; i < nodeX.size(); i++) {
            int randomPoint = new Random().nextInt(nodeX.size());
            while (uniqueRandom.contains(randomPoint)) {
                randomPoint = new Random().nextInt(nodeX.size());
            }
            clusters.get(clusterIndex).addNode(nodeX.get(randomPoint), nodeY.get(randomPoint));
        }
    }

    public static void changeCentroid() {
        convergence = true;
        //take the mean of X and Y and assign it as cluster Mid point
        for (int i = 0; i < clusters.size(); i++) {
            double newCentroidX = 0;
            double newCentroidY = 0;
            for (int j = 0; j < clusters.get(i).inClusterX.size(); j++) {
                newCentroidX += clusters.get(i).inClusterX.get(j);
                newCentroidY += clusters.get(i).inClusterY.get(j);
            }
            newCentroidX = newCentroidX / clusters.get(i).inClusterX.size();
            newCentroidY = newCentroidY / clusters.get(i).inClusterY.size();
            if (!((centroidX.get(i).equals(newCentroidX)) && (centroidY.get(i).equals(newCentroidY)))) {
                centroidX.set(i, newCentroidX);
                centroidY.set(i, newCentroidY);
                clusters.get(i).updateCentroid(newCentroidX, newCentroidY);
                convergence = false;
            }
        }
    }

    public static void doKmeans() {
        int noOfConvergence=1;
        while (noOfConvergence!=10) {
            for (int i = 0; i < clusters.size(); i++) {
                double distMin = getEuclideanDistance(clusters.get(i).x, clusters.get(i).inClusterX.get(0), clusters.get(i).y, clusters.get(i).inClusterY.get(0));
                //System.out.println("cluster "+i+" with "+" its first node point "+clusters.get(i).x+" "+clusters.get(i).inClusterX.get(0)+" "+clusters.get(i).y+" "+clusters.get(i).inClusterY.get(0)+" dist "+distMin);
                ArrayList<Double> indexToBeDeletedX = new ArrayList<Double>();
                ArrayList<Double> indexToBeDeletedY = new ArrayList<Double>();
                //loop for all the elements in the cluster to compare it with other cluster centroids
                for (int j = 0; j < clusters.get(i).inClusterX.size(); j++) {
                    boolean newMinDist = false;
                    double deletePointX = 0;
                    double deletePointY = 0;
                    int addToCluster = 0;
                    double pointX = 0;
                    double pointY = 0;
                    //to compare distance with other centroids
                    for (int k = 0; k < clusters.size(); k++) {
                        double tempDistMin = getEuclideanDistance(clusters.get(k).x, clusters.get(i).inClusterX.get(j), clusters.get(k).y, clusters.get(i).inClusterY.get(j));
                        //System.out.println("comparing cluster "+k+" with cluster "+i+" node "+j+" "+clusters.get(k).x+" "+clusters.get(i).inClusterX.get(j)+" "+clusters.get(k).y+" "+clusters.get(i).inClusterY.get(j)+" temp dist "+tempDistMin);
                        if (tempDistMin < distMin) {
                            newMinDist = true;
                            //changing the min distance
                            distMin = tempDistMin;
                            //to which cluster this point should be added
                            addToCluster = k;
                            //which index should be deleted from the old cluster
                            deletePointX = clusters.get(i).inClusterX.get(j);
                            deletePointY = clusters.get(i).inClusterY.get(j);
                            //point to be added into the new cluster
                            pointX = clusters.get(i).inClusterX.get(j);
                            pointY = clusters.get(i).inClusterY.get(j);
                        }
                    }
                    //add the point to the new cluster and delete it from the old cluster
                    if (newMinDist) {
                        clusters.get(addToCluster).addNode(pointX, pointY);
                        indexToBeDeletedX.add(deletePointX);
                        indexToBeDeletedY.add(deletePointY);
                    }
                }
                //delete the transferred point from the old cluster
                for (int j = 0; j < indexToBeDeletedX.size(); j++) {
                    clusters.get(i).inClusterX.remove(indexToBeDeletedX.get(j));
                    clusters.get(i).inClusterY.remove(indexToBeDeletedY.get(j));
                }
            }
            changeCentroid();
            if(convergence){
                noOfConvergence++;
            }
        }
    }

    public void assign(String fName, int assignMethod, int nClusters) throws Exception{
        // TODO code application logic here
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
        ArrayList<Integer> uniqueRandom = new ArrayList<Integer>();
        Scanner scan=new Scanner(new File(fName));
        while(scan.hasNext()){
            String line=scan.nextLine();
            String[] xy=line.split("\t");
            Double x=Double.parseDouble(xy[0]);
            Double y=Double.parseDouble(xy[1]);
            nodeX.add(x);
            nodeY.add(y);
        }
        int clusterSize =nClusters;
        //generate random points in the nodes to be assigned as initial centroid for each cluster
        for (int i = 0; i < clusterSize; i++) {
            int randomPoint = new Random().nextInt(clusterSize);
            while (uniqueRandom.contains(randomPoint)) {
                randomPoint = new Random().nextInt(clusterSize);
            }
            uniqueRandom.add(randomPoint);
            centroidX.add(nodeX.get(randomPoint));
            centroidY.add(nodeY.get(randomPoint));
            clusters.add(new KCluster(nodeX.get(randomPoint), nodeY.get(randomPoint)));
        }
        if(assignMethod==1){
        roundRobinAssignment();
        System.out.println("Round Robin");
        doKmeans();
        
        }else if(assignMethod==2){
         sequentialAssignment(); 
         System.out.println("Sequential Assignment");
            doKmeans();
        }else if(assignMethod==3){
            randomAssignment(); 
         System.out.println("Random Assignment");
            doKmeans();
        }
        System.out.println("Error Rate of Each Cluster");
        for (int i = 0; i < clusters.size(); i++) {
            System.out.println("Cluster " + (i + 1));
            double distance=getEuclideanDistance(clusters.get(i).x, clusters.get(i).inClusterX.get(0), clusters.get(i).y, clusters.get(i).inClusterY.get(0));
        }
        System.out.println("Final centroid for each cluster");
        for(int i=0;i<clusters.size();i++){
            System.out.println("X: " + clusters.get(i).x + " Y: " + clusters.get(i).y);
        }
        List<Point> points=new ArrayList<Point>();
        for(int i=0;i<clusters.size();i++){
            Random r=new Random();
            int R = (int)(Math.random()*256);
            int G = (int)(Math.random()*256);
            int B= (int)(Math.random()*256);
            Color c=new Color(R,G,B);
            c.brighter();
            for(int j=0;j<clusters.get(i).inClusterX.size();j++){
                Point p=new Point();
                p.setColor(c);
                Long x=Math.round(clusters.get(i).inClusterX.get(j));
                Long y=Math.round(clusters.get(i).inClusterY.get(j));
                p.setXY(x.intValue(),y.intValue());
                points.add(p);
            }
        }
        Visualize vis=new Visualize("Kmeans");
        vis.showPoints(points);
        }
}
