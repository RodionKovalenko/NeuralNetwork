/**
 * Created by Eigenaar on 15-3-2018.
 */
package networks;

import utils.NetworkFunctions;

import java.util.ArrayList.*;

import java.util.*;

public class KMeans {

    double[] centroids;
    HashMap<Double, Integer> mapFinal = new HashMap<>();

    public void caclulateKMean(double[] data, double[] centers) {
        centroids = centers;

        HashMap<Integer, Integer> map = new HashMap<>();


        boolean stop = false;
        while (!stop) {

            ArrayList<Double> tmp = new ArrayList();
            mapFinal = new HashMap<>();
            double[][] result = new double[centers.length][data.length];

            result = initializeArray(result);

            for (int j = 0; j < data.length; j++) {
                tmp = new ArrayList();
                for (int i = 0; i < centroids.length; i++) {
                    double distance = Math.abs(centroids[i] - data[j]);
                    tmp.add(distance);
                }

                double[] s = new double[tmp.size()];
                for (int k = 0; k < tmp.size(); k++) {
                    s[k] = tmp.get(k);
                }
                h:
                for (int l = 0; l < result[0].length; l++) {
                    if (result[tmp.indexOf(NetworkFunctions.getMinValue(s))][l] == Double.POSITIVE_INFINITY) {
                        map.put(j, tmp.indexOf(NetworkFunctions.getMinValue(s)));
                        break h;
                    }
                }
            }

            for (int i = 0; i < map.size(); i++) {
                mapFinal.put(data[i], map.get(i));
            }


            Collection<Integer> s = mapFinal.values();
            int indexJ = 0;
            Iterator iterator = mapFinal.keySet().iterator();
            for (Integer integer : s) {
                Double key = (Double) iterator.next();
                //  System.out.println(key);
                if (result[integer][indexJ] == Double.POSITIVE_INFINITY) {
                    result[integer][indexJ] = key;
                    indexJ++;

                }
            }

            for (int k = 0; k < result.length; k++) {
                for (int l = 0; l < result[0].length; l++) {
                    if (result[k][l] == Double.POSITIVE_INFINITY) {
                        result[k][l] = 0;
                    }
                }
            }

            //     NetworkFunctions.printArray(result);
            double[] newCentroids = new double[centroids.length];
            for (int i = 0; i < centroids.length; i++) {
                newCentroids[i] = meanForKMean(result[i]);
            }

            stop = checkCentroid(newCentroids);
            centroids = newCentroids;


            // NetworkFunctions.printArray(centroids);


        }


        System.out.print("Array Final ");
        System.out.print(mapFinal);
    }

    public static double[][] initializeArray(double[][] result) {
        for (int k = 0; k < result.length; k++) {
            for (int l = 0; l < result[0].length; l++) {
                result[k][l] = Double.POSITIVE_INFINITY;

            }
        }

        return result;
    }


    public static double meanForKMean(double... a) {
        double result = 0;
        double count = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i] != 0) {
                result += (int) a[i];
                count++;
            }
        }
        result /= count;
        //    System.out.println("\nMean: " + result);

        return result;
    }


    public  boolean checkCentroid(double[] centers) {
        int count = 0;
        for (int i = 0; i < centroids.length; i++) {
            if (centroids[i] == centers[i]) {
                count++;
            }
        }
        if (count == centers.length) {
            return true;
        } else {
            return false;
        }

    }


    public static void main(String... args) {
        double[] data = {2, 4, -10, 12, 3, 20, 30, 11, 332d};

        double[] centroids = {1, 5, 2};
        KMeans k=new KMeans();

        k.caclulateKMean(data, centroids);


        double[] test = {2, 4, 10, -2, 3, 20, 30, 33, 332d};

        //  System.out.println("Double posive infinity "+Double.NaN);


        //  NetworkFunctions.printArray(minmaxlens);

        // System.out.println(minmaxlens[0][0]-2000000e38);


        try {
            double[][] minmaxlens = new double[][]{
                    {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY},
                    {Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY},
                    {0d, 0d}
            };
            java.util.List points = new ArrayList();

            double[] point = {3, 42, 32};
            points.add(point);
            point = new double[]{4, 3, 42};
            points.add(point);


            minmaxlens = (double[][]) points.toArray(new double[points.size()][]);

            NetworkFunctions.printArray(minmaxlens);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}