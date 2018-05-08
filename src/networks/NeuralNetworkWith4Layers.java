package networks;

import java.io.*;

import visualize.LineChartSample;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;


import utils.NetworkFunctions;
import utils.Statistics;


public class NeuralNetworkWith4Layers extends Network {
    NetworkFunctions n = new NetworkFunctions();
    double learningRate;
    double[][] wih1;
    double[][] wh1h2;
    double[][] wh2o;
    double threshold;
    String name = "";
    boolean saved;

    public NeuralNetworkWith4Layers(int inputN, int hiddenN1, int hiddenN2, int outputN, double lr) {
        learningRate = lr;
        threshold = 2;
        saved = false;
        try (PrintWriter saveFile = new PrintWriter("results4Layers.txt")) {
        } catch (Exception e) {
        }

        if (wh1h2 == null | wih1 == null || wh2o == null) {
            initialize(inputN, hiddenN1, hiddenN2, outputN);
        }

        retrieveData(name);

        if (!valuesSaved() || wh1h2.length != hiddenN2 || wh2o.length != outputN || wih1.length != hiddenN1
                || learningRate != lr) {
            this.saveData(inputN, hiddenN1, hiddenN2, outputN);
        }

    }

    public void initialize(int inputN, int hiddenN1, int hiddenN2, int outputN) {

        wih1 = new double[hiddenN1][inputN];
        wh1h2 = new double[hiddenN2][hiddenN1];
        wh2o = new double[outputN][hiddenN2];
        for (int i = 0; i < wih1.length; i++) {
            for (int j = 0; j < wih1[i].length; j++) {
                Random random = new Random();
                wih1[i][j] = random.nextGaussian();
            }
        }

        for (int i = 0; i < wh1h2.length; i++) {
            for (int j = 0; j < wh1h2[i].length; j++) {
                Random random = new Random();
                wh1h2[i][j] = random.nextGaussian();
            }
        }

        for (int i = 0; i < wh2o.length; i++) {
            for (int j = 0; j < wh2o[i].length; j++) {
                Random random = new Random();
                wh2o[i][j] = random.nextGaussian();
            }
        }
    }

    public void retrieveData(String name) {
        try {
            double[][] l1;
            double[][] l2;
            double[][] l3;
            double thr;
            FileInputStream fileIn = new FileInputStream("NeuralNetwork4Layers" + name + ".ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            l1 = (double[][]) in.readObject();
            //System.out.println("Saved value are : ");
            l2 = (double[][]) in.readObject();
            l3 = (double[][]) in.readObject();
            thr = (double) in.readObject();
            wih1 = l1;
            wh1h2 = l2;
            wh2o = l3;
            threshold = thr;
            saved = true;
            in.close();
            fileIn.close();
        } catch (IOException i) {
            saved = false;
        } catch (ClassNotFoundException c) {
            System.out.println("Neural Network class not found");
            saved = false;
        }
    }

    public boolean valuesSaved() {
        try {
            double[][] l1;
            FileInputStream fileIn = new FileInputStream("NeuralNetwork4Layers" + name + ".ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            l1 = (double[][]) in.readObject();
            if (l1 != null)
                saved = true;
            in.close();
            fileIn.close();
        } catch (IOException i) {
            saved = false;

        } catch (ClassNotFoundException c) {
            System.out.println("Neural Network class not found");
            saved = false;
        }
        return saved;
    }

    public void saveData() {
        try {
            // System.out.println("Matrix 1: ");
            FileOutputStream fileOut = new FileOutputStream("NeuralNetwork4Layers" + name + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(wih1);
            out.writeObject(wh1h2);
            out.writeObject(wh2o);
            out.writeObject(threshold);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }

    }

    public void saveData(int inputN, int hiddenN1, int hiddenN2, int outputN) {

        try {
            initialize(inputN, hiddenN1, hiddenN2, outputN);
            FileOutputStream fileOut = new FileOutputStream("NeuralNetwork4Layers" + name + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(wih1);
            out.writeObject(wh1h2);
            out.writeObject(wh2o);
            out.writeObject(threshold);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in NeuralNetwork4Layers.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }

    }

    public double[][] queryWithSigmoidActivationFunction(double[][] a, double[][] t, boolean finalResult) {
        if (a.length != wih1[0].length) {
            a = NetworkFunctions.transpose(a);
        }
        double[][] h1Inputs = NetworkFunctions.matrixMultiply(wih1, a);
        double[][] h1Outputs = NetworkFunctions.activationSigmoidFunction(h1Inputs);
        double[][] h2Inputs = NetworkFunctions.matrixMultiply(wh1h2, h1Outputs);
        double[][] h2Outputs = NetworkFunctions.activationSigmoidFunction(h2Inputs);
        double[][] oInput = NetworkFunctions.matrixMultiply(wh2o, h2Outputs);
        double[][] oOutput = NetworkFunctions.activationSigmoidFunction(oInput);

        double[][] errorOutput = NetworkFunctions.defineErrors(t, oOutput);
        double errors = (NetworkFunctions.matrixSum(errorOutput));

        if (finalResult) {
            System.out.println("\nQuery Results:");
            NetworkFunctions.printMatrixSize(oOutput);
            System.out.println("General Error, 4 Layers");
            System.out.println(errors);

            try {
                String s = "-----------------------------------------------------------";
                s += "\n Traingsset Number " + name;
                s += "\nMatrix length " + oOutput.length + ", " + oOutput[0].length;
                s += "\n" + "General Error " + errors;
                s += "\n" + "Accuracy with standardized data: " + Statistics.measureAccuracy(oOutput, t) + "\n";
                s += "-----------------------------------------------------------";
                Files.write(Paths.get("results4Layers.txt"), s.getBytes(), StandardOpenOption.APPEND);

            } catch (Exception e) {
            }
        }

        return oOutput;
    }

    public double[][] queryWithReluActivationFunction(double[][] a) {
        if (a.length != wih1[0].length) {
            a = NetworkFunctions.transpose(a);
        }
        double[][] h1Inputs = NetworkFunctions.matrixMultiply(wih1, a);
        double[][] h1Outputs = NetworkFunctions.activationRelu(h1Inputs);
        double[][] h2Inputs = NetworkFunctions.matrixMultiply(wh1h2, h1Outputs);
        double[][] h2Outputs = NetworkFunctions.activationRelu(h2Inputs);
        double[][] oInput = NetworkFunctions.matrixMultiply(wh2o, h2Outputs);
        double[][] oOutput = NetworkFunctions.activationRelu(oInput);
        System.out.println("\nQuery Results:");
        NetworkFunctions.printArray(oOutput);
        return oOutput;
    }

    public double[][] queryWithSoftmaxActivationFunction(double[][] a) {
        if (a.length != wih1[0].length) {
            a = NetworkFunctions.transpose(a);
        }
        double[][] h1Inputs = NetworkFunctions.matrixMultiply(wih1, a);
        double[][] h1Outputs = NetworkFunctions.activationSigmoidFunction(h1Inputs);
        double[][] h2Inputs = NetworkFunctions.matrixMultiply(wh1h2, h1Outputs);
        double[][] h2Outputs = NetworkFunctions.activationSigmoidFunction(h2Inputs);

        double[][] oInput = NetworkFunctions.matrixMultiply(wh2o, h2Outputs);
        double[][] oOutput = NetworkFunctions.activationSoftmax(oInput);
        System.out.println("\nQuery Results:");
        NetworkFunctions.printArray(oOutput);
        return oOutput;
    }

    public void trainInCaseOfSigmoidFunction(double[][] input, double[][] target) {

        double[][] h1Inputs = NetworkFunctions.matrixMultiply(wih1, input);
        double[][] h1Outputs = NetworkFunctions.activationSigmoidFunction(h1Inputs);
        double[][] h2Inputs = NetworkFunctions.matrixMultiply(wh1h2, h1Outputs);
        double[][] h2Outputs = NetworkFunctions.activationSigmoidFunction(h2Inputs);

        double[][] oInput = NetworkFunctions.matrixMultiply(wh2o, h2Outputs);
        double[][] oOutput = NetworkFunctions.activationSigmoidFunction(oInput);

        // Backpropogation

        double[][] errorOutput = NetworkFunctions.matrixMinusMatrix(target, oOutput);
        // System.out.println("\nErrors");
        // System.out.println(NetworkFunctions.matrixSum(errorOutput));
        // double error=NetworkFunctions.matrixSum(errorOutput);
        // if (Math.abs(error)>= 0.0005 && Math.abs(error) <= 0.01) {
        // System.out.println("Train is broken
        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        // return;
        // }
        double[][] errorh2o = NetworkFunctions.matrixMultiply(NetworkFunctions.transpose(wh2o), errorOutput);
        double[][] errorh1h2 = NetworkFunctions.matrixMultiply(NetworkFunctions.transpose(wh1h2), errorh2o);
        // adjust weights of weight matrices

        double[][] m1 = NetworkFunctions.matrixMultiplySameSize(errorOutput, oOutput);

        m1 = NetworkFunctions.matrixMultiplySameSize(m1, NetworkFunctions.matrixMinusEinsVektor(oOutput));

        wh2o = NetworkFunctions.matrixAdd(wh2o, NetworkFunctions.matrixMultiplyVector(learningRate,
                NetworkFunctions.matrixMultiply(m1, NetworkFunctions.transpose(h2Outputs))));

        m1 = NetworkFunctions.matrixMultiplySameSize(errorh2o, h2Outputs);

        m1 = NetworkFunctions.matrixMultiplySameSize(m1, NetworkFunctions.matrixMinusEinsVektor(h2Outputs));
        wh1h2 = NetworkFunctions.matrixAdd(wh1h2, NetworkFunctions.matrixMultiplyVector(learningRate,
                NetworkFunctions.matrixMultiply(m1, NetworkFunctions.transpose(h1Outputs))));
        m1 = NetworkFunctions.matrixMultiplySameSize(errorh1h2, h1Outputs);
        m1 = NetworkFunctions.matrixMultiplySameSize(m1, NetworkFunctions.matrixMinusEinsVektor(h1Outputs));
        wih1 = NetworkFunctions.matrixAdd(wih1, NetworkFunctions.matrixMultiplyVector(learningRate,
                NetworkFunctions.matrixMultiply(m1, NetworkFunctions.transpose(input))));

        saveData();
    }

    public void trainInCaseOfSigmoidFunction(double i, double t) {

        double[][] input = new double[][]{{i}};
        double[][] target = new double[][]{{t}};

        double[][] h1Inputs = NetworkFunctions.matrixMultiply(wih1, input);

        double[][] h1Outputs = NetworkFunctions.activationSigmoidFunction(h1Inputs);
        double[][] h2Inputs = NetworkFunctions.matrixMultiply(wh1h2, h1Outputs);

        double[][] h2Outputs = NetworkFunctions.activationSigmoidFunction(h2Inputs);
        double[][] oInput = NetworkFunctions.matrixMultiply(wh2o, h2Outputs);
        double[][] oOutput = NetworkFunctions.activationSigmoidFunction(oInput);

        // Backpropogation

        double[][] errorOutput = NetworkFunctions.matrixMinusMatrix(target, oOutput);
        // System.out.println("\nErrors");
        // System.out.println(NetworkFunctions.matrixSum(errorOutput));
        // double error=NetworkFunctions.matrixSum(errorOutput);
        // if (Math.abs(error)>= 0.05 && Math.abs(error) <= 0.1) {
        // System.out.println("Train is broken
        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        // return;
        // }

        double[][] errorh2o = NetworkFunctions.matrixMultiply(NetworkFunctions.transpose(wh2o), errorOutput);
        double[][] errorh1h2 = NetworkFunctions.matrixMultiply(NetworkFunctions.transpose(wh1h2), errorh2o);
        // adjust weights of weight matrices

        double[][] m1 = NetworkFunctions.matrixMultiplySameSize(errorOutput, oOutput);

        m1 = NetworkFunctions.matrixMultiplySameSize(m1, NetworkFunctions.matrixMinusEinsVektor(oOutput));

        wh2o = NetworkFunctions.matrixAdd(wh2o, NetworkFunctions.matrixMultiplyVector(learningRate,
                NetworkFunctions.matrixMultiply(m1, NetworkFunctions.transpose(h2Outputs))));

        m1 = NetworkFunctions.matrixMultiplySameSize(errorh2o, h2Outputs);
        m1 = NetworkFunctions.matrixMultiplySameSize(m1, NetworkFunctions.matrixMinusEinsVektor(h2Outputs));

        wh1h2 = NetworkFunctions.matrixAdd(wh1h2, NetworkFunctions.matrixMultiplyVector(learningRate,
                NetworkFunctions.matrixMultiply(m1, NetworkFunctions.transpose(h1Outputs))));
        m1 = NetworkFunctions.matrixMultiplySameSize(errorh1h2, h1Outputs);
        m1 = NetworkFunctions.matrixMultiplySameSize(m1, NetworkFunctions.matrixMinusEinsVektor(h1Outputs));
        wih1 = NetworkFunctions.matrixAdd(wih1, NetworkFunctions.matrixMultiplyVector(learningRate,
                NetworkFunctions.matrixMultiply(m1, NetworkFunctions.transpose(input))));

        saveData();
    }

    public void trainInCaseOfReluActivation(double[][] input, double[][] target) {

        double[][] h1Inputs = NetworkFunctions.matrixMultiply(wih1, input);
        double[][] h1Outputs = NetworkFunctions.activationRelu(h1Inputs);
        double[][] h2Inputs = NetworkFunctions.matrixMultiply(wh1h2, h1Outputs);
        double[][] h2Outputs = NetworkFunctions.activationRelu(h2Inputs);
        double[][] oInput = NetworkFunctions.matrixMultiply(wh2o, h2Outputs);
        double[][] oOutput = NetworkFunctions.activationRelu(oInput);

        // Backpropogation

        double[][] errorOutput = NetworkFunctions.defineErrors(oOutput, target);

        // System.out.println("\nErrors");
        // NetworkFunctions.printArray(errorOutput);
        double[][] errorh2o = NetworkFunctions.matrixMultiply(NetworkFunctions.transpose(wh2o), errorOutput);
        double[][] errorh1h2 = NetworkFunctions.matrixMultiply(NetworkFunctions.transpose(wh1h2), errorh2o);
        // adjust weights of weight matrices

        double[][] m1 = NetworkFunctions.matrixMultiplySameSize(errorOutput, oOutput);

        m1 = NetworkFunctions.matrixMultiplySameSize(m1, NetworkFunctions.matrixMinusEinsVektor(oOutput));

        wh2o = NetworkFunctions.matrixAdd(wh2o, NetworkFunctions.matrixMultiplyVector(learningRate,
                NetworkFunctions.matrixMultiply(m1, NetworkFunctions.transpose(h2Outputs))));

        m1 = NetworkFunctions.matrixMultiplySameSize(errorh2o, h2Outputs);

        m1 = NetworkFunctions.matrixMultiplySameSize(m1, NetworkFunctions.matrixMinusEinsVektor(h2Outputs));
        wh1h2 = NetworkFunctions.matrixAdd(wh1h2, NetworkFunctions.matrixMultiplyVector(learningRate,
                NetworkFunctions.matrixMultiply(m1, NetworkFunctions.transpose(h1Outputs))));
        m1 = NetworkFunctions.matrixMultiplySameSize(errorh1h2, h1Outputs);
        m1 = NetworkFunctions.matrixMultiplySameSize(m1, NetworkFunctions.matrixMinusEinsVektor(h1Outputs));
        wih1 = NetworkFunctions.matrixAdd(wih1, NetworkFunctions.matrixMultiplyVector(learningRate,
                NetworkFunctions.matrixMultiply(m1, NetworkFunctions.transpose(input))));

        saveData();
    }

    public void trainInCaseOfReluActivation(double i, double t) {

        double[][] input = new double[][]{{i}};
        double[][] target = new double[][]{{t}};

        double[][] h1Inputs = NetworkFunctions.matrixMultiply(wih1, input);
        double[][] h1Outputs = NetworkFunctions.activationRelu(h1Inputs);
        double[][] h2Inputs = NetworkFunctions.matrixMultiply(wh1h2, h1Outputs);
        double[][] h2Outputs = NetworkFunctions.activationRelu(h2Inputs);
        double[][] oInput = NetworkFunctions.matrixMultiply(wh2o, h2Outputs);
        double[][] oOutput = NetworkFunctions.activationRelu(oInput);

        // Backpropogation

        double[][] errorOutput = NetworkFunctions.defineErrors(oOutput, target);
        //
        // System.out.println("\nErrors");
        // NetworkFunctions.printArray(errorOutput);
        double[][] errorh2o = NetworkFunctions.matrixMultiply(NetworkFunctions.transpose(wh2o), errorOutput);
        double[][] errorh1h2 = NetworkFunctions.matrixMultiply(NetworkFunctions.transpose(wh1h2), errorh2o);
        // adjust weights of weight matrices

        double[][] m1 = NetworkFunctions.matrixMultiplySameSize(errorOutput, oOutput);

        m1 = NetworkFunctions.matrixMultiplySameSize(m1, NetworkFunctions.matrixMinusEinsVektor(oOutput));

        wh2o = NetworkFunctions.matrixAdd(wh2o, NetworkFunctions.matrixMultiplyVector(learningRate,
                NetworkFunctions.matrixMultiply(m1, NetworkFunctions.transpose(h2Outputs))));

        m1 = NetworkFunctions.matrixMultiplySameSize(errorh2o, h2Outputs);
        m1 = NetworkFunctions.matrixMultiplySameSize(m1, NetworkFunctions.matrixMinusEinsVektor(h2Outputs));

        wh1h2 = NetworkFunctions.matrixAdd(wh1h2, NetworkFunctions.matrixMultiplyVector(learningRate,
                NetworkFunctions.matrixMultiply(m1, NetworkFunctions.transpose(h1Outputs))));
        m1 = NetworkFunctions.matrixMultiplySameSize(errorh1h2, h1Outputs);
        m1 = NetworkFunctions.matrixMultiplySameSize(m1, NetworkFunctions.matrixMinusEinsVektor(h1Outputs));
        wih1 = NetworkFunctions.matrixAdd(wih1, NetworkFunctions.matrixMultiplyVector(learningRate,
                NetworkFunctions.matrixMultiply(m1, NetworkFunctions.transpose(input))));

        saveData();
    }

    public void trainInCaseOfSoftmaxActivation(double[][] input, double[][] target) {

        double[][] h1Inputs = NetworkFunctions.matrixMultiply(wih1, input);
        double[][] h1Outputs = NetworkFunctions.activationSigmoidFunction(h1Inputs);
        double[][] h2Inputs = NetworkFunctions.matrixMultiply(wh1h2, h1Outputs);
        double[][] h2Outputs = NetworkFunctions.activationSigmoidFunction(h2Inputs);
        double[][] oInput = NetworkFunctions.matrixMultiply(wh2o, h2Outputs);
        double[][] oOutput = NetworkFunctions.activationSoftmax(oInput);

        // Backpropogation

        double[][] errorOutput = NetworkFunctions.defineErrors(oOutput, target);

        // System.out.println("\nErrors");
        // NetworkFunctions.printArray(errorOutput);
        double[][] errorh2o = NetworkFunctions.matrixMultiply(NetworkFunctions.transpose(wh2o), errorOutput);
        double[][] errorh1h2 = NetworkFunctions.matrixMultiply(NetworkFunctions.transpose(wh1h2), errorh2o);
        // adjust weights of weight matrices

        double[][] m1 = NetworkFunctions.matrixMultiplySameSize(errorOutput, oOutput);

        m1 = NetworkFunctions.matrixMultiplySameSize(m1, NetworkFunctions.matrixMinusEinsVektor(oOutput));

        wh2o = NetworkFunctions.matrixAdd(wh2o, NetworkFunctions.matrixMultiplyVector(learningRate,
                NetworkFunctions.matrixMultiply(m1, NetworkFunctions.transpose(h2Outputs))));

        m1 = NetworkFunctions.matrixMultiplySameSize(errorh2o, h2Outputs);

        m1 = NetworkFunctions.matrixMultiplySameSize(m1, NetworkFunctions.matrixMinusEinsVektor(h2Outputs));
        wh1h2 = NetworkFunctions.matrixAdd(wh1h2, NetworkFunctions.matrixMultiplyVector(learningRate,
                NetworkFunctions.matrixMultiply(m1, NetworkFunctions.transpose(h1Outputs))));
        m1 = NetworkFunctions.matrixMultiplySameSize(errorh1h2, h1Outputs);
        m1 = NetworkFunctions.matrixMultiplySameSize(m1, NetworkFunctions.matrixMinusEinsVektor(h1Outputs));
        wih1 = NetworkFunctions.matrixAdd(wih1, NetworkFunctions.matrixMultiplyVector(learningRate,
                NetworkFunctions.matrixMultiply(m1, NetworkFunctions.transpose(input))));

        saveData();
    }

    public void trainInCaseOfSoftmaxActivation(double i, double t) {

        double[][] input = new double[][]{{i}};
        double[][] target = new double[][]{{t}};

        double[][] h1Inputs = NetworkFunctions.matrixMultiply(wih1, input);
        double[][] h1Outputs = NetworkFunctions.activationSigmoidFunction(h1Inputs);
        double[][] h2Inputs = NetworkFunctions.matrixMultiply(wh1h2, h1Outputs);
        double[][] h2Outputs = NetworkFunctions.activationSigmoidFunction(h2Inputs);
        double[][] oInput = NetworkFunctions.matrixMultiply(wh2o, h2Outputs);
        double[][] oOutput = NetworkFunctions.activationSoftmax(oInput);

        // Backpropogation

        double[][] errorOutput = NetworkFunctions.defineErrors(oOutput, target);
        System.out.println("\nErrors");
        System.out.println(NetworkFunctions.matrixSum(errorOutput));

        double[][] errorh2o = NetworkFunctions.matrixMultiply(NetworkFunctions.transpose(wh2o), errorOutput);
        double[][] errorh1h2 = NetworkFunctions.matrixMultiply(NetworkFunctions.transpose(wh1h2), errorh2o);
        // adjust weights of weight matrices

        double[][] m1 = NetworkFunctions.matrixMultiplySameSize(errorOutput, oOutput);

        m1 = NetworkFunctions.matrixMultiplySameSize(m1, NetworkFunctions.matrixMinusEinsVektor(oOutput));

        wh2o = NetworkFunctions.matrixAdd(wh2o, NetworkFunctions.matrixMultiplyVector(learningRate,
                NetworkFunctions.matrixMultiply(m1, NetworkFunctions.transpose(h2Outputs))));

        m1 = NetworkFunctions.matrixMultiplySameSize(errorh2o, h2Outputs);
        m1 = NetworkFunctions.matrixMultiplySameSize(m1, NetworkFunctions.matrixMinusEinsVektor(h2Outputs));

        wh1h2 = NetworkFunctions.matrixAdd(wh1h2, NetworkFunctions.matrixMultiplyVector(learningRate,
                NetworkFunctions.matrixMultiply(m1, NetworkFunctions.transpose(h1Outputs))));
        m1 = NetworkFunctions.matrixMultiplySameSize(errorh1h2, h1Outputs);
        m1 = NetworkFunctions.matrixMultiplySameSize(m1, NetworkFunctions.matrixMinusEinsVektor(h1Outputs));
        wih1 = NetworkFunctions.matrixAdd(wih1, NetworkFunctions.matrixMultiplyVector(learningRate,
                NetworkFunctions.matrixMultiply(m1, NetworkFunctions.transpose(input))));

        saveData();
        System.out.println("max value of Training data: " + NetworkFunctions.getMaxValue(h2Outputs));
        NetworkFunctions.printArray(h2Outputs);

    }

    public void trainAndTest(double[][] i, double[][] tar, Statistics.Normalisation normalisationType,
                             double desiredAccuracy, int iterations, String[] args, String file_name, boolean show) {
        name = file_name;
        retrieveData(name);
        type = normalisationType;
        double[][] iTrain = Statistics.divideTrainSet(i);
        double[][] tarTrain = Statistics.divideTrainSet(tar);

        System.out.println("Mean of TarTrain: " + Statistics.mean(tarTrain));
        double[][] iTest = Statistics.divideTestSet(i);
        double[][] tarTest = Statistics.divideTestSet(tar);

        if (normalisationType == Statistics.Normalisation.NORMALIZECUMULATIVE) {
            double sumTrain = NetworkFunctions.matrixSum(tarTrain);
            double sumTest = NetworkFunctions.matrixSum(tarTest);
            double sumTotal = NetworkFunctions.matrixSum(tar);
            double[][] inputTest = Statistics.normalizeHistogram(iTest, tarTest);
            double[][] targetTest = Statistics.normalizeHistogram(tarTest, iTest);

            double[][] input2St = Statistics.normalizeHistogram(iTrain, tarTrain);
            double[][] target2St = Statistics.normalizeHistogram(tarTrain, iTrain);

            double[][] result = new double[][]{{}};
            double[][] resultTest = new double[][]{{}};
            double accuracy = 0;
            while (accuracy < desiredAccuracy && iterations > 0) {
                for (int k = 0; k < input2St.length; k++) {
                    for (int j = 0; j < input2St[k].length; j++) {
                        int indexI = (int) Math.floor(Math.random() * input2St.length);
                        int indexJ = (int) Math.floor(Math.random() * input2St[0].length);
                        trainInCaseOfSigmoidFunction(input2St[k][j], target2St[k][j]);
                        trainInCaseOfSigmoidFunction(input2St[indexI][indexJ], target2St[indexI][indexJ]);
                    }
                }

                result = Statistics.normalizeHistogramInverse(queryWithSigmoidActivationFunction(input2St, target2St, false),
                        sumTrain);
                accuracy = Statistics.measureAccuracy2(result, tarTrain);
                iterations--;
            }
            try {
                String s = "-----------------------------------------------------------";
                System.out.println("***************************************************************************************");
                System.out.println("***************************************************************************************");
                System.out.println("Start 4 layers result*****************************************************");
                System.out.println("Trained data");
                result = Statistics.normalizeHistogramInverse(queryWithSigmoidActivationFunction(input2St, target2St, true),
                        sumTrain);
                accuracy = Statistics.measureAccuracy(result, tarTrain);

                String label = "Multilayer perceptron with 4 layers: " + wih1[0].length + " input, \n" + wih1.length + " and " + wh1h2.length + "hidden neurons, learning rate " + learningRate + ", \n" +
                        "accuracy in training phase " + accuracy;

                s += "\nAccuracy Train : " + accuracy;
                System.out.println("Accuracy Train 4 Layers: " + accuracy);
                double[][] errorOutput = NetworkFunctions.defineErrors(tarTrain, result);
                double errors = (NetworkFunctions.matrixSum(errorOutput));
                //double errors=(NetworkFunctions.matrixSum(errorOutput));
                s += "\nRelative Errors train : " + errors;///(tarTrain.length*tarTrain[0].length);
                System.out.println("Test data");
                System.out.println("Test data");
                double testLength = tarTest[0].length * tarTest.length;
                double trainLength = tarTrain[0].length * tarTrain.length;
                double maxL = Math.max(testLength, trainLength);
                double minL = Math.min(testLength, trainLength);
                double maxA = Math.max(sumTest, sumTrain);
                double minA = Math.min(sumTest, sumTrain);
                double scalar = (minA + (minA - minA * (minL / maxL)));
                resultTest = Statistics.normalizeHistogramInverse(queryWithSigmoidActivationFunction(inputTest, targetTest, true),
                        scalar);
                accuracy = Statistics.measureAccuracy(resultTest, tarTest);

                label += ",\naccuracy in test phase " + accuracy;
                s += "\nAccuracy Test: " + accuracy;
                System.out.println("Accuracy Test 4 layers " + accuracy);
                errorOutput = NetworkFunctions.defineErrors(tarTest, resultTest);
                errors = (NetworkFunctions.matrixSum(errorOutput));
                //errors=(NetworkFunctions.matrixSum(errorOutput));
                s += "\nRelative Errors test : " + errors;///tarTest.length*tarTest[0].length;
                s += "\n" + "Mean price Train: " + Statistics.mean(tarTrain);
                s += "\nMean price Test: " + Statistics.mean(tarTest);
                System.out.println("End 4 layers result*****************************************************");
                System.out.println("***************************************************************************************");
                if (show) {
                    LineChartSample.network = this;
                    LineChartSample.setData(iTrain, result, iTest, resultTest, i, tar, NetworkFunctions.getMaxValue(i) + 5,
                            NetworkFunctions.getMaxValue(tar) + 15, label);
                    LineChartSample.main(args);
                } else {
                    LineChartSample.setData(iTrain, result, iTest, resultTest, i, tar, NetworkFunctions.getMaxValue(i) + 5,
                            NetworkFunctions.getMaxValue(tar) + 15, label);
                }
                s += "\n------------------------------------------------------------------------\n";
                s += "\n*************************************************************************\n";
                Files.write(Paths.get("results4Layers.txt"), s.getBytes(), StandardOpenOption.APPEND);

            } catch (Exception e) {
            }
            NetworkFunctions.printArray(tar);
        } else if (normalisationType == Statistics.Normalisation.NORMALIZEMAXMIN) {
            double minTrain = Statistics.getMinValue(tarTrain);
            double maxTrain = Statistics.getMaxValue(tarTrain);

            double minTest = Statistics.getMinValue(tarTest);
            double maxTest = Statistics.getMaxValue(tarTest);

            double[][] inputTest = Statistics.normalizeMaxMin(iTest);
            double[][] targetTest = Statistics.normalizeMaxMin(tarTest);

            double[][] input2St = Statistics.normalizeMaxMin(iTrain);
            double[][] target2St = Statistics.normalizeMaxMin(tarTrain);

            double[][] result = new double[][]{{}};
            double[][] resultTest = new double[][]{{}};
            double accuracy = 0;
            while (accuracy < desiredAccuracy && iterations > 0) {
                for (int k = 0; k < input2St.length; k++) {
                    for (int j = 0; j < input2St[k].length; j++) {
                        int indexI = (int) Math.floor(Math.random() * input2St.length);
                        int indexJ = (int) Math.floor(Math.random() * input2St[0].length);
                        trainInCaseOfSigmoidFunction(input2St[k][j], target2St[k][j]);
                        trainInCaseOfSigmoidFunction(input2St[indexI][indexJ], target2St[indexI][indexJ]);
                    }
                }

                result = Statistics.normalizeMaxMinInverse(queryWithSigmoidActivationFunction(input2St, target2St, false),
                        maxTrain, minTrain);
                accuracy = Statistics.measureAccuracy(result, tarTrain);
                iterations--;
            }
            try {

                String s = "-----------------------------------------------------------";
                System.out.println("***************************************************************************************");

                System.out.println("***************************************************************************************");
                System.out.println("Start 4 layers result*****************************************************");
                System.out.println("Trained data");
                result = Statistics.normalizeMaxMinInverse(queryWithSigmoidActivationFunction(input2St, target2St, true),
                        maxTrain, minTrain);
                accuracy = Statistics.measureAccuracy(result, tarTrain);
                String label = "Multilayer perceptron with 4 layers: " + wih1[0].length + " input, \n" + wih1.length + " and " + wh1h2.length + "hidden neurons, learning rate " + learningRate + ", \n" +
                        "accuracy in training phase " + accuracy;

                s += "\nAccuracy Train : " + accuracy;
                System.out.println("Accuracy Train 4 layers " + accuracy);
                double[][] errorOutput = NetworkFunctions.defineErrors(tarTrain, result);
                double errors = (NetworkFunctions.matrixSum(errorOutput));
                s += "\nRelative Errors train : " + errors;///(tarTrain.length*tarTrain[0].length);
                System.out.println("Test data");
                System.out.println("Test data");
                resultTest = Statistics.normalizeMaxMinInverse(queryWithSigmoidActivationFunction(inputTest, targetTest, true),
                        maxTest, minTest);
                accuracy = Statistics.measureAccuracy(resultTest, tarTest);
                label += ", \naccuracy in test phase " + accuracy;
                s += "\nAccuracy Test : " + accuracy;
                System.out.println("Accuracy Test 4 layers " + accuracy);
                errorOutput = NetworkFunctions.defineErrors(tarTest, resultTest);
                errors = (NetworkFunctions.matrixSum(errorOutput));
                s += "\nRelative Errors test : " + errors;///(tarTest.length*tarTest[0].length);
                s += "\n" + "Mean price Train: " + Statistics.mean(tarTrain);
                s += "\nMean price Test: " + Statistics.mean(tarTest);
                System.out.println("End 4 layers result*****************************************************");
                System.out.println("***************************************************************************************");
                if (show) {
                    LineChartSample.network = this;
                    LineChartSample.setData(iTrain, result, iTest, resultTest, i, tar, NetworkFunctions.getMaxValue(i) + 5,
                            NetworkFunctions.getMaxValue(tar) + 15, label);
                    LineChartSample.main(args);
                } else {
                    LineChartSample.setData(iTrain, result, iTest, resultTest, i, tar, NetworkFunctions.getMaxValue(i) + 5,
                            NetworkFunctions.getMaxValue(tar) + 15, label);
                }
                s += "\n------------------------------------------------------------------------\n";
                s += "\n*************************************************************************\n";
                Files.write(Paths.get("results4Layers.txt"), s.getBytes(), StandardOpenOption.APPEND);

            } catch (Exception e) {
            }
        } else if (normalisationType == Statistics.Normalisation.STANDARDIZE) {
            double meanTrain = Statistics.mean(tarTrain);
            double varianceTrain = Statistics.variance(tarTrain);

            double meanTest = Statistics.mean(tarTest);
            double varianceTest = Statistics.variance(tarTest);

            double[][] inputTest = Statistics.standardize(iTest);
            double[][] targetTest = Statistics.standardize(tarTest);

            double[][] input2St = Statistics.standardize(iTrain);
            double[][] target2St = Statistics.standardize(tarTrain);

            double[][] result = new double[][]{{}};
            double[][] resultTest = new double[][]{{}};
            double accuracy = 0;
            while (accuracy < desiredAccuracy && iterations > 0) {
                for (int k = 0; k < input2St.length; k++) {
                    for (int j = 0; j < input2St[k].length; j++) {
                        int indexI = (int) Math.floor(Math.random() * input2St.length);
                        int indexJ = (int) Math.floor(Math.random() * input2St[0].length);
                        trainInCaseOfSigmoidFunction(input2St[k][j], target2St[k][j]);
                        trainInCaseOfSigmoidFunction(input2St[indexI][indexJ], target2St[indexI][indexJ]);
                    }
                }

                result = Statistics.standardizeInverse(queryWithSigmoidActivationFunction(input2St, target2St, false),
                        meanTrain, varianceTrain);
                accuracy = Statistics.measureAccuracy(result, tarTrain);
                iterations--;
            }


            try {

                String s = "-----------------------------------------------------------";
                System.out.println("***************************************************************************************");
                System.out.println("Start 4 layers result*****************************************************");
                System.out.println("Trained data");
                result = Statistics.standardizeInverse(queryWithSigmoidActivationFunction(input2St, target2St, true), meanTrain,
                        varianceTrain);
                accuracy = Statistics.measureAccuracy(result, tarTrain);
                String label = "Multilayer perceptron with 4 layers: " + wih1[0].length + " input, \n" + wih1.length + " and " + wh1h2.length + "hidden neurons, learning rate " + learningRate + ", \n" +
                        "accuracy in training phase " + accuracy;

                s += "\nAccuracy Train : " + accuracy;
                System.out.println("Accuracy Train 4 layers " + accuracy);
                double[][] errorOutput = NetworkFunctions.defineErrors(tarTrain, result);
                double errors = (NetworkFunctions.matrixSum(errorOutput));
                s += "\nRelative Errors train : " + errors;///(tarTrain.length*tarTrain[0].length);
                System.out.println("Test data");
                resultTest = Statistics.standardizeInverse(queryWithSigmoidActivationFunction(inputTest, targetTest, true),
                        meanTest, varianceTest);
                accuracy = Statistics.measureAccuracy(resultTest, tarTest);
                label += "\naccuracy in test phase " + accuracy;
                s += "\nAccuracy Test : " + accuracy;
                System.out.println("Accuracy Test 4 layers " + accuracy);
                errorOutput = NetworkFunctions.defineErrors(tarTest, resultTest);
                errors = (NetworkFunctions.matrixSum(errorOutput));
                s += "\nRelative Errors test : " + errors;///(tarTest.length*tarTest[0].length);
                s += "\n" + "Mean price Train: " + Statistics.mean(tarTrain);
                s += "\nMean price Test: " + Statistics.mean(tarTest);
                System.out.println("End 4 layers result*****************************************************");
                System.out.println("***************************************************************************************");
                if (show) {
                    LineChartSample.network = this;
                    LineChartSample.setData(iTrain, result, iTest, resultTest, i, tar, NetworkFunctions.getMaxValue(i) + 5,
                            NetworkFunctions.getMaxValue(tar) + 15, label);
                    LineChartSample.main(args);
                } else {
                    LineChartSample.setData(iTrain, result, iTest, resultTest, i, tar, NetworkFunctions.getMaxValue(i) + 5,
                            NetworkFunctions.getMaxValue(tar) + 15, label);
                }
                s += "\n------------------------------------------------------------------------\n";
                s += "\n*************************************************************************\n";
                Files.write(Paths.get("results4Layers.txt"), s.getBytes(), StandardOpenOption.APPEND);

            } catch (Exception e) {
            }
        }

    }


    public String getName() {
        return name;
    }

    public void trainAndTest(String inp, String t, Statistics.Normalisation normalisationType,
                             double desiredAccuracy, int iterations, String[] args, String file_name, boolean show) {
        double[][] i = new double[][]{NetworkFunctions.convertStringToNumbers(inp)};
        double[][] tar = new double[][]{NetworkFunctions.convertStringToNumbers(t)};

        trainAndTest(i, tar, normalisationType,
                desiredAccuracy, iterations, args, file_name, show);
    }

    public static void main(String... args) {

    }
}
