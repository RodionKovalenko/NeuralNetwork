package networks;

import java.io.*;

import javafx.application.Platform;
import visualize.LineChartSample;

import java.nio.file.StandardOpenOption;
import java.util.Random;

import utils.NetworkFunctions;
import utils.Statistics;

import java.io.*;
import java.nio.file.*;


public class NeuralNetworkWith3Layers extends Network {

    NetworkFunctions n = new NetworkFunctions();
    double learningRate;
    double[][] wih1;
    double[][] wh1o;
    double threshold;
    String name = "";
    boolean saved;



    public NeuralNetworkWith3Layers(int inputN, int hiddenN1, int outputN, double lr) {
        learningRate = lr;
        threshold = 2;
        saved = false;

        try (PrintWriter saveFile = new PrintWriter("results3Layers.txt")) {
        } catch (Exception e) {
        }


        if (wih1 == null || wh1o == null) {
            initialize(inputN, hiddenN1, outputN);
        }

        retrieveData(name);

        if (!valuesSaved() || wh1o.length != outputN || wih1.length != hiddenN1 || wih1[0].length != inputN
                || learningRate != learningRate) {
            this.saveData(inputN, hiddenN1, outputN);
        }

    }
    public String getName(){
        return name;
    }

    public double[][] getWih1() {
        return wih1;
    }

    public double[][] getWh1o() {
        return wh1o;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void initialize(int inputN, int hiddenN1, int outputN) {

        wih1 = new double[hiddenN1][inputN];
        wh1o = new double[outputN][hiddenN1];
        for (int i = 0; i < wih1.length; i++) {
            for (int j = 0; j < wih1[i].length; j++) {
                Random random = new Random();
                wih1[i][j] = random.nextGaussian() + 0.01;
            }
        }
        for (int i = 0; i < wh1o.length; i++) {
            for (int j = 0; j < wh1o[i].length; j++) {
                Random random = new Random();
                wh1o[i][j] = random.nextGaussian() + 0.01;
            }
        }
    }

    public void retrieveData(String name) {
        try {
            double[][] l1;
            double[][] l2;
            double thr;
            FileInputStream fileIn = new FileInputStream("NeuralNetwork3Layers" + name + ".ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            l1 = (double[][]) in.readObject();
            //System.out.println("Saved value are : ");
            l2 = (double[][]) in.readObject();
            thr = (double) in.readObject();
            wih1 = l1;
            wh1o = l2;
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
            FileInputStream fileIn = new FileInputStream("NeuralNetwork3Layers" + name + ".ser");
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
            FileOutputStream fileOut = new FileOutputStream("NeuralNetwork3Layers" + name + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(wih1);
            out.writeObject(wh1o);
            out.writeObject(threshold);
            out.close();
            fileOut.close();
            // System.out.printf("Serialized data is saved in
            // NeuralNetwork3Layers.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }

    }

    public void saveData(int inputN, int hiddenN1, int outputN) {

        try {
            initialize(inputN, hiddenN1, outputN);
            FileOutputStream fileOut = new FileOutputStream("NeuralNetwork3Layers" + name + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(wih1);
            out.writeObject(wh1o);
            out.writeObject(threshold);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in NeuralNetwork3Layers.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }

    }

    public double[][] queryWithSigmoidActivationFunction(double[][] a) {

        if (a.length != wih1[0].length) {
            a = NetworkFunctions.transpose(a);
        }
        double[][] h1Inputs = NetworkFunctions.matrixMultiply(wih1, a);
        double[][] h1Outputs = NetworkFunctions.activationSigmoidFunction(h1Inputs);
        double[][] oInput = NetworkFunctions.matrixMultiply(wh1o, h1Outputs);
        double[][] oOutput = NetworkFunctions.activationSigmoidFunction(oInput);
//		System.out.println("\nQuery Results:");
//		NetworkFunctions.maxValue(oOutput);

        return oOutput;
    }

    public double[][] query(double[][] a, double[][] t, boolean finalResult) {

        if (a.length != wih1[0].length) {
            a = NetworkFunctions.transpose(a);
        }
        double[][] h1Inputs = NetworkFunctions.matrixMultiply(wih1, a);
        double[][] h1Outputs = NetworkFunctions.activationSigmoidFunction(h1Inputs);
        double[][] oInput = NetworkFunctions.matrixMultiply(wh1o, h1Outputs);


        double[][] oOutput = NetworkFunctions.activationSigmoidFunction(oInput);
        double[][] errorOutput = NetworkFunctions.defineErrors(t, oOutput);
        double errors = (NetworkFunctions.matrixSum(errorOutput));

        if (finalResult) {
            System.out.println("\nQuery Results:");
            //NetworkFunctions.printArray(oOutput);
            NetworkFunctions.printMatrixSize(oOutput);
            System.out.println("General Error");
            System.out.println(errors);

            try {
                String s = "-----------------------------------------------------------";
                s += "\n Traingsset Number " + name;
                s += "\nMatrix length " + oOutput.length + ", " + oOutput[0].length;
                s += "\n" + "General Error " + errors;
                s += "\n" + "Accuracy with standardized data: " + Statistics.measureAccuracy(oOutput, t) + "\n";
                s += "-----------------------------------------------------------";
                Files.write(Paths.get("results3Layers.txt"), s.getBytes(), StandardOpenOption.APPEND);

            } catch (Exception e) {
            }


        }

        return oOutput;
    }

    public void trainInCaseOfSigmoidFunction(double[][] input, double[][] target) {

        if (input.length != wih1[0].length) {
            input = NetworkFunctions.transpose(input);
        }

        double[][] h1Inputs = NetworkFunctions.matrixMultiply(wih1, input);
        double[][] h1Outputs = NetworkFunctions.activationSigmoidFunction(h1Inputs);
        double[][] oInput = NetworkFunctions.matrixMultiply(wh1o, h1Outputs);
        double[][] oOutput = NetworkFunctions.activationSigmoidFunction(oInput);


        // Backpropogation

        double[][] errorOutput = NetworkFunctions.matrixMinusMatrix(target, oOutput);

//		System.out.println("\nErrors");
//		System.out.println(NetworkFunctions.matrixSum(errorOutput));
//		double error=NetworkFunctions.matrixSum(errorOutput);
//		if (Math.abs(error)>= 0.0005 && Math.abs(error) <= 0.01) {
//			System.out.println("Train is broken >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//			return;
//		}


        double[][] errorh1o = NetworkFunctions.matrixMultiply(NetworkFunctions.transpose(wh1o), errorOutput);
        // adjust weights of weight matrices

        double[][] m1 = NetworkFunctions.matrixMultiplySameSize(NetworkFunctions.matrixMultiplySameSize(errorOutput, oOutput),
                NetworkFunctions.matrixMinusEinsVektor(oOutput));
        wh1o = NetworkFunctions.matrixAdd(wh1o,
                NetworkFunctions.matrixMultiplyVector(learningRate, NetworkFunctions.matrixMultiply(m1, NetworkFunctions.transpose(h1Outputs))));
        double[][] m2 = NetworkFunctions.matrixMultiplySameSize(NetworkFunctions.matrixMultiplySameSize(errorh1o, h1Outputs),
                NetworkFunctions.matrixMinusEinsVektor(h1Outputs));
        wih1 = NetworkFunctions.matrixAdd(wih1, NetworkFunctions.matrixMultiplyVector(learningRate, NetworkFunctions.matrixMultiply(m2, NetworkFunctions.transpose(input))));

        saveData();

    }

    public void train(double[][] a, double[][] b) {

    }

    public void train(double i, double t) {
        double[][] input = new double[][]{{i}};
        double[][] target = new double[][]{{t}};
        if (input.length != wih1[0].length) {
            input = NetworkFunctions.transpose(input);
        }

        double[][] h1Inputs = NetworkFunctions.matrixMultiply(wih1, input);
        double[][] h1Outputs = NetworkFunctions.activationSigmoidFunction(h1Inputs);
        double[][] oInput = NetworkFunctions.matrixMultiply(wh1o, h1Outputs);
        double[][] oOutput = NetworkFunctions.activationSigmoidFunction(oInput);

        // Backpropogation

        double[][] errorOutput = NetworkFunctions.matrixMinusMatrix(target, oOutput);

//		System.out.println("\nErrors");
//		System.out.println(NetworkFunctions.matrixSum(errorOutput));
//		double error=NetworkFunctions.matrixSum(errorOutput);
//		if (Math.abs(error)>= 0.0005 && Math.abs(error) <= 0.01) {
//			System.out.println("Train is broken >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//			return;
//		}
//		
        double[][] errorh1o = NetworkFunctions.matrixMultiply(NetworkFunctions.transpose(wh1o), errorOutput);

        // adjust weights of weight matrices

        double[][] m1 = NetworkFunctions.matrixMultiplySameSize(NetworkFunctions.matrixMultiplySameSize(errorOutput, oOutput),
                NetworkFunctions.matrixMinusEinsVektor(oOutput));
        wh1o = NetworkFunctions.matrixAdd(wh1o,
                NetworkFunctions.matrixMultiplyVector(learningRate, NetworkFunctions.matrixMultiply(m1, NetworkFunctions.transpose(h1Outputs))));
        // adjust weights of wih1
        double[][] m2 = NetworkFunctions.matrixMultiplySameSize(NetworkFunctions.matrixMultiplySameSize(errorh1o, h1Outputs),
                NetworkFunctions.matrixMinusEinsVektor(h1Outputs));
        wih1 = NetworkFunctions.matrixAdd(wih1, NetworkFunctions.matrixMultiplyVector(learningRate, NetworkFunctions.matrixMultiply(m2, NetworkFunctions.transpose(input))));

        saveData();

    }

    public void trainAndTest(double[][] i, double[][] tar, Statistics.Normalisation normalisationType,
                             double desiredAccuracy, int iterations, String[] args, String file_name, boolean show) {
        name = file_name;
        retrieveData(name);
        type=normalisationType;
        double[][] iTrain = Statistics.divideTrainSet(i);
        double[][] tarTrain = Statistics.divideTrainSet(tar);
        System.out.println("Mean of TarTrain: " + Statistics.mean(tarTrain));
        double[][] iTest = Statistics.divideTestSet(i);
        double[][] tarTest = Statistics.divideTestSet(tar);

        if (normalisationType == Statistics.Normalisation.NORMALIZECUMULATIVE) {
            double sumTrain = NetworkFunctions.matrixSum(tarTrain);
            double sumTest = NetworkFunctions.matrixSum(tarTest);
            double sumTotal = NetworkFunctions.matrixSum(tar);
            double testLength = tarTest[0].length * tarTest.length;
            double trainLength = tarTrain[0].length * tarTrain.length;
            double maxL = Math.max(testLength, trainLength);
            double minL = Math.min(testLength, trainLength);
            double maxA = Math.max(sumTest, sumTrain);
            double minA = Math.min(sumTest, sumTrain);
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
                        train(input2St[k][j], target2St[k][j]);
                        train(input2St[indexI][indexJ], target2St[indexI][indexJ]);
                    }
                }


                result = Statistics.normalizeHistogramInverse(query(input2St, target2St, false),
                        sumTrain);
                accuracy = Statistics.measureAccuracy2(result, tarTrain);
                iterations--;
            }
            System.out.println("***************************************************************************************");
            System.out.println("Start 3 layers result*****************************************************");
            System.out.println("Trained data 3 layers");
            try {

                result = Statistics.normalizeHistogramInverse(query(input2St, target2St, true),
                        sumTrain);
                accuracy = Statistics.measureAccuracy(result, tarTrain);

                String label = "Multilayer perceptron with 3 layers:  " + wih1[0].length + " input, \n" + wih1.length + " hidden neurons, learning rate " + learningRate+", \n" +
                        "accuracy in trainings phase " + accuracy;


                String s = "\nAccuracy Train : " + accuracy;
                System.out.println("Accuracy Train 3 layers " + accuracy);
                double[][] errorOutput = NetworkFunctions.defineErrors(tarTrain, result);
                double errors = (NetworkFunctions.matrixSum(errorOutput));
                //double errors=(NetworkFunctions.matrixSum(errorOutput));
                s += "\n Mean absolute error (training phase): " + errors;///(tarTrain.length*tarTrain[0].length);
                System.out.println("Test data");

//			resultTest = Statistics.normalizeHistogramInverse(queryWithSigmoidActivationFunction(inputTest, targetTest),
//  		(minA+((maxA-minA)*(minL/(maxL+minL)))/(maxL/minL)));
                double scalar = (minA + (minA - minA * (minL / maxL)));
                resultTest = Statistics.normalizeHistogramInverse(query(inputTest, targetTest, true),
                        scalar);
//
                //Adjusting the test curve
//			for(int d=0; d<sumTrain; d++) {
//				resultTest = Statistics.normalizeHistogramInverse(queryWithSigmoidActivationFunction(inputTest, targetTest),
//						sumTrain+d);
//				accuracy = Statistics.measureAccuracy(resultTest, tarTest);
//				if(accuracy>=desiredAccuracy){
//					break;
//				}
//			}
//			for(int d=(int)sumTrain; d>=0;  d--) {
//				resultTest = Statistics.normalizeHistogramInverse(queryWithSigmoidActivationFunction(inputTest, targetTest),
//						sumTrain-d);
//				accuracy = Statistics.measureAccuracy(resultTest, tarTest);
//				if(accuracy>=desiredAccuracy){
//					break;
//				}
//			}
                accuracy = Statistics.measureAccuracy(resultTest, tarTest);

                label+=", \naccuracy in test phase " +accuracy;

                System.out.println("Accuracy Test 3 layers " + accuracy);
                s += "\nAccuracy Test : " + accuracy;
                errorOutput = NetworkFunctions.defineErrors(tarTest, resultTest);
                //errors=(NetworkFunctions.matrixSum(errorOutput)/(tarTest[0].length*tarTest.length));
                errors = (NetworkFunctions.matrixSum(errorOutput));

                s += "\n Mean absolute error (test phase): " + errors;///(tarTest.length*tarTest[0].length);
                s += "\n" + "Mean price Train: " + Statistics.mean(tarTrain);
                s += "\nMean price Test: " + Statistics.mean(tarTest);
                System.out.println("End 3 layers result*****************************************************");
                System.out.println("***************************************************************************************");
//                LineChartSample.setData(iTrain, result, iTest, resultTest, i, tar, NetworkFunctions.getMaxValue(i) + 5,
//                        NetworkFunctions.getMaxValue(tar) + 15, label);

                double[][] resultTrain = result;
                double[][] resultTestFinal = resultTest;


                if (show) {
                    LineChartSample.network = this;
                    LineChartSample.setData(iTrain, resultTrain, iTest, resultTestFinal, i, tar, NetworkFunctions.getMaxValue(i) + 5,
                            NetworkFunctions.getMaxValue(tar) + 15, label);
                    LineChartSample.main(args);
                } else {
                    LineChartSample.setData(iTrain, result, iTest, resultTest, i, tar, NetworkFunctions.getMaxValue(i) + 5,
                            NetworkFunctions.getMaxValue(tar) + 15, label);
                }

                s += "\n------------------------------------------------------------------------\n";
                s += "\n*************************************************************************\n";
                Files.write(Paths.get("results3Layers.txt"), s.getBytes(), StandardOpenOption.APPEND);

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
                        train(input2St[k][j], target2St[k][j]);
                        train(input2St[indexI][indexJ], target2St[indexI][indexJ]);
                    }
                }

                result = Statistics.normalizeMaxMinInverse(query(input2St, target2St, false),
                        maxTrain, minTrain);
                accuracy = Statistics.measureAccuracy(result, tarTrain);
                iterations--;
            }
            try {
                System.out.println("***************************************************************************************");
                System.out.println("Start 3 layers result*****************************************************");
                System.out.println("Trained data");
                result = Statistics.normalizeMaxMinInverse(query(input2St, target2St, true),
                        maxTrain, minTrain);
                accuracy = Statistics.measureAccuracy(result, tarTrain);
                String label = "Multilayer perceptron with 3 layers:  " + wih1[0].length + " input, \n" + wih1.length + " hidden neurons, learning rate " + learningRate+", \n" +
                        "accuracy in trainings phase " + accuracy;

                String s = "\nAccuracy Train : " + accuracy;
                System.out.println("Accuracy Train 3 layers " + accuracy);
                double[][] errorOutput = NetworkFunctions.defineErrors(tarTrain, result);
                double errors = (NetworkFunctions.matrixSum(errorOutput));
                s += "\n Mean absolute error (training phase): " + errors;///(tarTrain.length*tarTrain[0].length);
                System.out.println("Test data");
                resultTest = Statistics.normalizeMaxMinInverse(query(inputTest, targetTest, true),
                        maxTest, minTest);
                accuracy = Statistics.measureAccuracy(resultTest, tarTest);

                label+=", \naccuracy in test phase " +accuracy;
                s += "\nAccuracy Test : " + accuracy;
                System.out.println("Accuracy Test 3 layers " + accuracy);
                errorOutput = NetworkFunctions.defineErrors(tarTest, resultTest);
                errors = (NetworkFunctions.matrixSum(errorOutput));
                s += "\n Mean absolute error (test phase): " + errors;///(tarTest.length*tarTest[0].length);
                System.out.println("Mean price Train: " + Statistics.mean(tarTrain));
                System.out.println("Mean price Test: " + Statistics.mean(tarTest));
                s += "\n" + "Mean price Train: " + Statistics.mean(tarTrain);
                s += "\n   Mean price Test: " + Statistics.mean(tarTest);
                System.out.println("End 3 layers result*****************************************************");
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
                Files.write(Paths.get("results3Layers.txt"), s.getBytes(), StandardOpenOption.APPEND);

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
                        train(input2St[k][j], target2St[k][j]);
                        train(input2St[indexI][indexJ], target2St[indexI][indexJ]);
                    }
                }

                result = Statistics.standardizeInverse(query(input2St, target2St, false),
                        meanTrain, varianceTrain);
                accuracy = Statistics.measureAccuracy(result, tarTrain);
                iterations--;
            }
            try {
                System.out.println("***************************************************************************************");
                System.out.println("Start 3 layers result*****************************************************");
                System.out.println("Trained data");
                result = Statistics.standardizeInverse(query(input2St, target2St, true), meanTrain,
                        varianceTrain);
                accuracy = Statistics.measureAccuracy(result, tarTrain);
                String label = "Multilayer perceptron with 3 layers:  " + wih1[0].length + " input, \n" + wih1.length + " hidden neurons, learning rate " + learningRate+", \n" +
                        "accuracy in trainings phase " + accuracy;
                String s = "\nAccuracy Train : " + accuracy;
                System.out.println("Accuracy Train 3 layers " + accuracy);
                double[][] errorOutput = NetworkFunctions.defineErrors(tarTrain, result);
                double errors = (NetworkFunctions.matrixSum(errorOutput));
                s += "\n Mean absolute error (training phase): " + errors;///(tarTrain.length*tarTrain[0].length);
                System.out.println("Test data");
                resultTest = Statistics.standardizeInverse(query(inputTest, targetTest, true),
                        meanTest, varianceTest);
                accuracy = Statistics.measureAccuracy(resultTest, tarTest);

                label+=", \naccuracy in test phase " +accuracy;
                s += "\nAccuracy Test : " + accuracy;
                System.out.println("Accuracy Test 3 layers " + accuracy);
                errorOutput = NetworkFunctions.defineErrors(tarTest, resultTest);
                errors = (NetworkFunctions.matrixSum(errorOutput));
                s += "\n Mean absolute error (test phase): " + errors;////(tarTest.length*tarTest[0].length);
                System.out.println("Mean price Train: " + Statistics.mean(tarTrain));
                System.out.println("Mean price Test: " + Statistics.mean(tarTest));
                s += "\n" + "Mean price Train: " + Statistics.mean(tarTrain);
                s += "\nMean price Test: " + Statistics.mean(tarTest);
                System.out.println("End 3 layers result*****************************************************");
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
                Files.write(Paths.get("results3Layers.txt"), s.getBytes(), StandardOpenOption.APPEND);

            } catch (Exception e) {
            }
        }
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
