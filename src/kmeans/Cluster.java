
package kmeans;

/**
 *
 * @author Suresh Babu Jothilingam
 * @author Nitish Ganesan
 */
public class Cluster {
    public static void main(String[] arg) throws Exception{
        switch (arg[0]) {
            case "K":
                Kmeans k=new Kmeans();
                k.assign(arg[1], Integer.parseInt(arg[2]), Integer.parseInt(arg[3]));
                break;
            case "D":
                DbScan d=new DbScan();
                d.assign(arg[1], Integer.parseInt(arg[2]), Integer.parseInt(arg[3]));
                break;
        }
    }
}
