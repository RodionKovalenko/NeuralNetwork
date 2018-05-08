package networks;

import java.io.*;

import visualize.LineChartSample;
import utils.NetworkFunctions;
import utils.Statistics;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;


public class Adaline extends Network {
    // adaline consists of only one neuron with linear activation function
    // (identity
    // function) f(x)=x

    NetworkFunctions n = new NetworkFunctions();
    Statistics s = new Statistics();
    double learningRate;
    double[][] wio;
    boolean saved;
    double bias;
    String name = "";

    public Adaline(int inputN, int outputN, double learningRate, double bias, int iter) {
        this.learningRate = learningRate;
        this.bias = bias;
        saved = false;

        try (PrintWriter saveFile = new PrintWriter("resultsAdaline.txt")) {
        } catch (Exception e) {
        }

        if (wio == null) {
            initialize(inputN, outputN);
        }

        retrieveData(name);
        System.out.println(this.learningRate);
        System.out.println(learningRate != this.learningRate);

        if (!valuesSaved() | this.learningRate != learningRate) {
            this.saveData(inputN, outputN, learningRate);
            System.out.println("Values are saved");
        }

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void initialize(int inputN, int outputN) {
        wio = new double[1][inputN];
        for (int i = 0; i < wio.length; i++) {
            Random random = new Random();
            for (int j = 0; j < wio[i].length; j++) {
                wio[i][j] = 0.0; // random.nextGaussian();
            }
        }
    }

    public void retrieveData(String name) {
        try {
            double[][] l1;
            double l2;
            double l3;
            FileInputStream fileIn = new FileInputStream("NeuralNetworkAdaline" + name + ".ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            l1 = (double[][]) in.readObject();
            l2 = (double) in.readObject();
            l3 = (double) in.readObject();
            System.out.println("Saved value are : ");
            wio = l1;
            learningRate = l2;
            bias = l3;
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
            FileInputStream fileIn = new FileInputStream("NeuralNetworkAdaline" + name + ".ser");
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
            FileOutputStream fileOut = new FileOutputStream("NeuralNetworkAdaline" + name + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(wio);
            out.writeObject(learningRate);
            out.writeObject(bias);
            out.close();
            fileOut.close();
            //System.out.printf("Serialized data is saved in NeuralNetworkAdaline.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }

    }

    public void saveData(int inputN, int outputN, double learningRate) {

        try {
            this.learningRate = learningRate;
            initialize(inputN, outputN);
            FileOutputStream fileOut = new FileOutputStream("NeuralNetworkAdaline" + name + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(wio);
            out.writeObject(learningRate);
            out.writeObject(bias);
            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in NeuralNetworkAdaline.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }

    }

    public double[][] query(double[][] input, double[][] target, boolean finalResult) {

        double[][] result = n.matrixMultiply(wio, input);
        result = n.matrixAddVector(result, bias);
        double[][] errors = n.defineErrors(target, result);

        if (finalResult) {
            System.out.println("General Error of Adaline : " + NetworkFunctions.matrixSum(errors));
//		System.out.println("\nQuery result:");
//		//n.printArray(result);
//		System.out.println("\nTarget result");
            //	n.printArray(target);
            try {
                String s = "-----------------------------------------------------------";
                s += "\n Traingsset Number " + name;
                s += "\nMatrix length " + result.length + ", " + result[0].length;
                s += "\n" + "General Error " + NetworkFunctions.matrixSum(errors) + "\n";
                s += "Accuracy of stardadized data: " + Statistics.measureAccuracy(result, target);
                s += "\n-----------------------------------------------------------";
                Files.write(Paths.get("resultsAdaline.txt"), s.getBytes(), StandardOpenOption.APPEND);
            } catch (Exception e) {
            }
        }
        return result;
    }

    public double[][] query(double[][] input) {
        double[][] result = n.matrixMultiply(wio, input);
        result = n.matrixAddVector(result, bias);
        System.out.println("\nQuery result:");
        n.printArray(result);
        return result;
    }

    public void train(double a, double b) {

    }

    public void train(double[][] input, double[][] target) {
        if (input.length != wio[0].length) {
            input = n.transpose(input);
        }

        double costGeneral[] = new double[0];
        double costs;
        double[][] result = NetworkFunctions.matrixMultiply(wio, input);
        double[][] finalResult = n.matrixAddVector(this.bias, result);
        double[][] errors = n.matrixMinusMatrix(target, finalResult);
        this.wio = n.matrixAdd(this.wio,
                n.matrixMultiplyVector(this.learningRate, n.matrixMultiply(input, n.transpose(errors))));
        this.bias += this.learningRate * n.matrixSum(errors);
        costs = n.matrixCosts(errors);
        costGeneral = n.matrixAddNumber(costGeneral, costs);

        saveData();

    }

    public void train(double[][] input, double[][] target, int iter) {
        if (input.length != wio[0].length) {
            input = n.transpose(input);
        }

        double costGeneral[] = new double[0];

        double costs;
        while (iter >= 0) {
            double[][] result = NetworkFunctions.matrixMultiply(wio, input);
            double[][] finalResult = n.matrixAddVector(this.bias, result);
            double[][] errors = n.matrixMinusMatrix(target, finalResult);
            // System.out.println("\n Errors");
            // n.printArray(errors);
            this.wio = n.matrixAdd(this.wio,
                    n.matrixMultiplyVector(this.learningRate, n.matrixMultiply(input, n.transpose(errors))));
            this.bias += this.learningRate * n.matrixSum(errors);

            costs = n.matrixCosts(errors);
            costGeneral = n.matrixAddNumber(costGeneral, costs);
            iter--;

        }
        saveData();

    }

    public void trainAndTest(double[][] i, double[][] tar, Statistics.Normalisation normalisationType,
                             double desiredAccuracy, int iterations, String[] args, String file_name, boolean show) {
        name = file_name;
        retrieveData(name);
        type = normalisationType;
        double[][] iTrain = Statistics.divideTrainSet(i);
        double[][] tarTrain = Statistics.divideTrainSet(tar);


        double[][] iTest = Statistics.divideTestSet(i);
        double[][] tarTest = Statistics.divideTestSet(tar);

        if (normalisationType == Statistics.Normalisation.NORMALIZECUMULATIVE) {
            double sumTrain = NetworkFunctions.matrixSum(tarTrain);
            double sumTest = NetworkFunctions.matrixSum(tarTest);
            double[][] inputTest = Statistics.normalizeHistogram(iTest, tarTest);
            double[][] targetTest = Statistics.normalizeHistogram(tarTest, iTest);

            double[][] input2St = Statistics.normalizeHistogram(iTrain, tarTrain);
            double[][] target2St = Statistics.normalizeHistogram(tarTrain, iTrain);

            double sumTotal = NetworkFunctions.matrixSum(tar);
            double testLength = tarTest[0].length * tarTest.length;
            double trainLength = tarTrain[0].length * tarTrain.length;
            double maxL = Math.max(testLength, trainLength);
            double minL = Math.min(testLength, trainLength);
            double maxA = Math.max(sumTest, sumTrain);
            double minA = Math.min(sumTest, sumTrain);

            double[][] result = new double[][]{{}};
            double[][] resultTest = new double[][]{{}};
            double accuracy = 0;
            while (accuracy < desiredAccuracy && iterations > 0) {
                for (int k = 0; k < input2St.length; k++) {
                    for (int j = 0; j < input2St[k].length; j++) {
                        int indexI = (int) Math.floor(Math.random() * input2St.length);
                        int indexJ = (int) Math.floor(Math.random() * input2St[0].length);
                        train(input2St, target2St);
                    }
                }

                result = Statistics.normalizeHistogramInverse(query(input2St, target2St, false), sumTrain);
                accuracy = Statistics.measureAccuracy(result, tarTrain);
                iterations--;
            }
            try {
                System.out.println("*******************************************************");
                System.out.println("************************Train results******************");
                result = Statistics.normalizeHistogramInverse(query(input2St, target2St, true), sumTrain);
                accuracy = Statistics.measureAccuracy(result, tarTrain);
                String label = "Adaline with " + wio[0].length + " neuron, learning rate + " + learningRate+"" +
                        ", \naccuracy in trainings phase "+accuracy;

                String s = "\nAccuracy Train : " + accuracy;
                System.out.println("Accuracy of Adaline: Train phase " + accuracy);
                double[][] errorOutput = NetworkFunctions.defineErrors(tarTrain, result);
                double errors = (NetworkFunctions.matrixSum(errorOutput));
                // double  errors=(NetworkFunctions.matrixSum(errorOutput));
                s += "\n Mean absolute error (training phase): " + errors;///(tarTrain.length*tarTrain[0].length);
                NetworkFunctions.printArray(result);
                System.out.println("*************************Test results********************");
                double scalar = (minA + (minA - minA * (minL / maxL)));
                resultTest = Statistics.normalizeHistogramInverse(query(inputTest, targetTest,
                        true), scalar);
                accuracy = Statistics.measureAccuracy(resultTest, tarTest);
                label+=",\n accuracy in test phase "+ accuracy;
                s += "\nAccuracy Test : " + accuracy;

                if (show) {
                    LineChartSample.network = this;
                    LineChartSample.setData(iTrain, result, iTest, resultTest, i, tar, NetworkFunctions.getMaxValue(i) + 5,
                            NetworkFunctions.getMaxValue(tar) + 15, label);
                    LineChartSample.main(args);
                } else {
                    LineChartSample.setData(iTrain, result, iTest, resultTest, i, tar, NetworkFunctions.getMaxValue(i) + 5,
                            NetworkFunctions.getMaxValue(tar) + 15, label);
                }


                System.out.println("Accuracy of Adaline: Test phase " + accuracy);
                NetworkFunctions.printArray(resultTest);
                errorOutput = NetworkFunctions.defineErrors(tarTest, resultTest);
                errors = (NetworkFunctions.matrixSum(errorOutput));
                // errors=(NetworkFunctions.matrixSum(errorOutput));
                s += "\n Mean absolute error (test phase): " + errors;///(tarTest.length*tarTest[0].length);
                s += "\n" + "Mean price Train: " + Statistics.mean(tarTrain);
                s += "\nMean price Test: " + Statistics.mean(tarTest);
                System.out.println("*******************************************************");
                s += "\n------------------------------------------------------------------------\n";
                s += "\n*************************************************************************\n";
                Files.write(Paths.get("resultsAdaline.txt"), s.getBytes(), StandardOpenOption.APPEND);

            } catch (Exception e) {
            }

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
                        train(input2St, target2St);
                    }
                }

                result = Statistics.normalizeMaxMinInverse(query(input2St, target2St, false), maxTrain, minTrain);
                accuracy = Statistics.measureAccuracy(result, tarTrain);
                iterations--;
            }
            try {
                System.out.println("*******************************************************");
                System.out.println("**********************Train results********************");
                result = Statistics.normalizeMaxMinInverse(query(input2St, target2St, true), maxTrain, minTrain);
                accuracy = Statistics.measureAccuracy(result, tarTrain);
                String label = "Adaline with " + wio[0].length + " neuron, learning rate + " + learningRate+"" +
                        ", \naccuracy in trainings phase "+accuracy;

                String s = "\nAccuracy Train : " + accuracy;
                double[][] errorOutput = NetworkFunctions.defineErrors(tarTrain, result);
                double errors = (NetworkFunctions.matrixSum(errorOutput));
                s += "\n Mean absolute error (training phase): " + errors; ///(tarTrain.length*tarTrain[0].length);
                System.out.println("***********************Test results**********************");
                resultTest = Statistics.normalizeMaxMinInverse(query(inputTest, targetTest, true), maxTest, minTest);
                accuracy = Statistics.measureAccuracy(resultTest, tarTest);
                label+=", \naccuracy in test phase "+ accuracy;
                s += "\nAccuracy Test : " + accuracy;
                errorOutput = NetworkFunctions.defineErrors(tarTest, resultTest);
                errors = (NetworkFunctions.matrixSum(errorOutput));
                s += "\nAccuracy Test : " + accuracy;
                 if (show) {
                    LineChartSample.network = this;
                    LineChartSample.setData(iTrain, result, iTest, resultTest, i, tar, NetworkFunctions.getMaxValue(i) + 5,
                            NetworkFunctions.getMaxValue(tar) + 15, label);
                    LineChartSample.main(args);
                } else {
                    LineChartSample.setData(iTrain, result, iTest, resultTest, i, tar, NetworkFunctions.getMaxValue(i) + 5,
                            NetworkFunctions.getMaxValue(tar) + 15, label);
                }
                s += "\n" + "Mean price Train: " + Statistics.mean(tarTrain);
                s += "\nMean price Test: " + Statistics.mean(tarTest);
                System.out.println("*******************************************************");

                s += "\n------------------------------------------------------------------------\n";
                s += "\n*************************************************************************\n";
                Files.write(Paths.get("resultsAdaline.txt"), s.getBytes(), StandardOpenOption.APPEND);

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
                        train(input2St, target2St);
                    }
                }

                result = Statistics.standardizeInverse(query(input2St, target2St, false), meanTrain, varianceTrain);
                accuracy = Statistics.measureAccuracy(result, tarTrain);
                iterations--;
            }
            try {
                System.out.println("*******************************************************");
                System.out.println("************************Train results******************");
                result = Statistics.standardizeInverse(query(input2St, target2St, true), meanTrain, varianceTrain);
                accuracy = Statistics.measureAccuracy(result, tarTrain);
                String label = "Adaline with " + wio[0].length + " neuron, learning rate + " + learningRate+"" +
                        ", \naccuracy in trainings phase "+accuracy;

                String s = "\nAccuracy Train : " + accuracy;
                double[][] errorOutput = NetworkFunctions.defineErrors(tarTrain, result);
                double errors = (NetworkFunctions.matrixSum(errorOutput));
                s += "\n Mean absolute error (training phase): " + errors;///(tarTrain.length*tarTrain[0].length);
                System.out.println("*************************Test results********************");
                resultTest = Statistics.standardizeInverse(query(inputTest, targetTest, true), meanTest, varianceTest);
                accuracy = Statistics.measureAccuracy(resultTest, tarTest);
                label+=",\n accuracy in test phase "+ accuracy;
                s += "\nAccuracy Test : " + accuracy;
                errorOutput = NetworkFunctions.defineErrors(tarTest, resultTest);
                errors = (NetworkFunctions.matrixSum(errorOutput));

                s += "\n Mean absolute error (test phase): " + errors; ///(tarTest.length*tarTest[0].length);
                s += "\nAccuracy Test : " + accuracy;

                s += "\n" + "Mean price Train: " + Statistics.mean(tarTrain);
                s += "\nMean price Test: " + Statistics.mean(tarTest);
                System.out.println("*******************************************************");


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
                Files.write(Paths.get("resultsAdaline.txt"), s.getBytes(), StandardOpenOption.APPEND);

            } catch (Exception e) {
            }
        }

    }

    public void trainAndTest(String inp, String t, Statistics.Normalisation normalisationType, double desiredAccuracy,
                             int iterations, String[] args, String file_name, boolean show) {
        double[][] i = new double[][]{NetworkFunctions.convertStringToNumbers(inp)};
        double[][] tar = new double[][]{NetworkFunctions.convertStringToNumbers(t)};

        trainAndTest(i, tar, normalisationType, desiredAccuracy, iterations, args, file_name, show);
    }


}
