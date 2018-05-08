package networks;

import utils.NetworkFunctions;
import utils.Statistics;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Locale;


import visualize.LineChartSample;

import javax.swing.*;


public class RBFN extends Network {

    public RBFN() {
        try (PrintWriter saveFile = new PrintWriter("ResultsRadialBasisNetwork.txt")) {
        } catch (Exception e) {
        }
    }

    double[][] weightMatrix;
    double[] centers;
    double sigma = 10;
    int numberOfHiddenNeurons = 0;

    public void trainAndTest(String a, String t, boolean visualize, String[] args, int index) {
        double[][] input = new double[][]{NetworkFunctions.convertStringToNumbers(a)};
        double[][] target = new double[][]{NetworkFunctions.convertStringToNumbers(t)};
        trainAndTest(input, target, visualize, args, index);
    }

    public void trainAndTest(double[][] a, double[][] t, boolean visualize, String[] args, int index) {

        double[][] trainMenge = a;
        numberOfHiddenNeurons = trainMenge[0].length * trainMenge.length;
        double[][] trainPreis =  t;


        centers = new double[trainMenge[0].length * trainMenge.length];
        int indexCenters = 0;
        for (int i = 0; i < trainMenge.length; i++) {
            for (int j = 0; j < trainMenge[i].length; j++) {
                centers[indexCenters] = a[i][j];
                indexCenters++;
            }
        }

//        System.out.println("centers");
//        NetworkFunctions.printArray(centers);

        System.out.println("Interpolation matrix is being calculated");
        double[][] matrixS = interpolationMatrix(trainMenge);
        System.out.println("Interpolation matrix is calculated");
        double[][] pseudoInverse = NetworkFunctions.pseudoInverse(matrixS);
        NetworkFunctions.printMatrixSize(matrixS);
        System.out.println("Pseudo Inverse calculated: ");
        NetworkFunctions.printMatrixSize(pseudoInverse);
        double[][] trainPreisTransomedTo1D = new double[][]{NetworkFunctions.convertToOneDimension(trainPreis)};
        weightMatrix = NetworkFunctions.matrixMultiply(pseudoInverse, NetworkFunctions.transpose(trainPreisTransomedTo1D));
        System.out.println("weight matrix calculated: ");
        NetworkFunctions.printMatrixSize(weightMatrix);

//test

        double[][] m = interpolationMatrix(trainMenge);
        System.out.println("Predict ");
        NetworkFunctions.printMatrixSize(weightMatrix);
        NetworkFunctions.printMatrixSize(m);
        // NetworkFunctions.printMatrixSize(t);
        double[][] result = NetworkFunctions.matrixMultiply(
                NetworkFunctions.transpose(weightMatrix), NetworkFunctions.transpose(m));

        // System.out.println("Accuracy train:" + Statistics.measureAccuracy2(result, trainPreis));
        System.out.println("result matrix: ");
        NetworkFunctions.printMatrixSize(result);


    }

    public  void predict(double[][] a, double[][] t, boolean visualize, String[] args) {
        double[][] m = interpolationMatrix(a);
        System.out.println("Predict ");
        NetworkFunctions.printMatrixSize(weightMatrix);
        NetworkFunctions.printMatrixSize(m);
        NetworkFunctions.printMatrixSize(t);
        double[][] result = NetworkFunctions.matrixMultiply(
                NetworkFunctions.transpose(weightMatrix), NetworkFunctions.transpose(m));

        System.out.println("Accuracy :" + Statistics.measureAccuracy(result, t));
        NetworkFunctions.printArray(result);

        if (visualize) {
            String label = "Radial basis function network ";
            LineChartSample.setData(a, result, a, t, NetworkFunctions.getMaxValue(result) + 5,
                    NetworkFunctions.getMaxValue(t) + 15, label);
            LineChartSample.main(args);
        }

    }


    public double[][] interpolationMatrix(double[][] a) {

        double[] s = NetworkFunctions.convertToOneDimension(a);
        double[][] matrixS = new double[s.length][numberOfHiddenNeurons];
//
        for (int i = 0; i < s.length; i++) {
            for (int k = 0; k < centers.length; k++) {
                matrixS[i][k] = calculateKernel(s[i], centers[k]);

            }
        }
        return matrixS;
    }


    public  double calculateKernel(double a, double b) {
        return Math.exp(-sigma * Math.pow(a - b, 2));
    }


    public static void main(String[] args) {

        double[][] input = new double[][]{NetworkFunctions.convertStringToNumbers(Data.mengen[13])};
        double[][] target = new double[][]{NetworkFunctions.convertStringToNumbers(Data.preise[13])};

        double[][] menge = {{1, 0, 0}, {3, 1, 0}, {2, 1, 1}};
        double[][] preis = {{1, 2, 4}, {0, 2, 2}, {0, 0, 3}};

        NetworkFunctions.printMatrixSize(menge);
        NetworkFunctions.printArray(NetworkFunctions.matrixMultiply(menge, preis));
        NetworkFunctions.printArray(NetworkFunctions.matrixMultiply2(menge, preis));

    }


}




