package networks;

import javafx.application.Platform;
import utils.NetworkFunctions;
import utils.Statistics;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Locale;
import java.io.*;

import javafx.stage.FileChooser;
import visualize.LineChartSample;


public class TestAndTrainNetworks {

    public static void main(String[] args) {
        NeuralNetworkWith3Layers n3 = new NeuralNetworkWith3Layers(1, 300, 1, 0.58);
        NeuralNetworkWith4Layers n4 = new NeuralNetworkWith4Layers(1, 90, 67, 1, 0.58);
        Adaline ad = new Adaline(1, 1, 0.000388, 1, 20);
        RBFN r = new RBFN();
        try (PrintWriter saveFile = new PrintWriter("menge.txt")) {
        } catch (Exception e) {
        }

        boolean show = false;
        for (int i = 0; i < 30; i++) {
            String m = Data.mengen[i];
            String p = Data.preise[i];
            ad.trainAndTest(m, p, Statistics.Normalisation.NORMALIZECUMULATIVE, 1, 12, args, "c" + (i + 1), show);
             //  ad.trainAndTest(m, p, Statistics.Normalisation.NORMALIZEMAXMIN, 1, 6, args, "m" + (i + 1),show);
            //		ad.trainAndTest(m, p, Statistics.Normalisation.STANDARDIZE, 1, 6, args, "s"+ (i+1),show);

             // n3.trainAndTest(m, p, Statistics.Normalisation.NORMALIZECUMULATIVE, 1, 12, args, "c" + (i + 1), show);
              n3.trainAndTest(m, p, Statistics.Normalisation.NORMALIZEMAXMIN, 1, 12, args, "m"+ (i+1),show);
            //	n3.trainAndTest(m,p, Statistics.Normalisation.STANDARDIZE, 1, 3, args, "s"+ (i+1),show);


            //   n4.trainAndTest(m, p, Statistics.Normalisation.NORMALIZECUMULATIVE, 1, 12, args, "c" + (i + 1), show);
            		n4.trainAndTest(m, p, Statistics.Normalisation.NORMALIZEMAXMIN, 1, 12, args, "m"+ (i+1),show);
            //	n4.trainAndTest(m,p, Statistics.Normalisation.STANDARDIZE, 1, 12, args, "s"+ (i+1), show);

             //  r.trainAndTest(m, p, show, args, i + 1);



            double[][] menge = new double[][]{NetworkFunctions.convertStringToNumbers(m)};
            double[][] preis = new double[][]{NetworkFunctions.convertStringToNumbers(p)};

            double[][] trainSetM = Statistics.divideTrainSet(menge);
            double[][] trainSetP = Statistics.divideTrainSet(preis);
            double[][] testSetM = Statistics.divideTestSet(menge);
            double[][] testSetP = Statistics.divideTestSet(preis);

            String s = NetworkFunctions.insertData(trainSetM, "menge", i + 1);
            s += NetworkFunctions.insertData(trainSetP, "preis", i + 1);
            s += NetworkFunctions.insertData(testSetM, "testMenge", i + 1);
            s += NetworkFunctions.insertData(testSetP, "testPreis", i + 1);
            try {
                Files.write(Paths.get("menge.txt"), s.getBytes(), StandardOpenOption.APPEND);
            } catch (Exception e) {

            }

        }

//
        LineChartSample.network=n3;
        show=true;
        if(show){
            Thread thread = new Thread((new Runnable() {
                @Override
                public void run() {
                    try {
                        LineChartSample.main(args);

                    } catch (Exception exc) {
                        throw new Error("Unexpected interruption", exc);
                    }
                }
            }));
            thread.start();
        }

    }


//		double [] x=new double[]{};
//			for(int j=0; j<80; j++){
//	x=NetworkFunctions.matrixAddNumber(x, j+1);
//			}
//		double []y=new double []{};
//		for(int j=0; j<80; j++){
//			y=NetworkFunctions.matrixAddNumber(y,Math.cos(x[j])*4+8);
//		}
//
//		double [][] xInput=new double [][]{x};
//		double [][] yInput=new double [][]{y};
//
//
////		double[][] iTrain =Statistics.divideTrainSet(xInput);
////		double[][] tarTrain =Statistics.divideTrainSet(yInput);
//
////		double[][] iTrain =Statistics.divideTestSet(xInput);
////		double[][] tarTrain =Statistics.divideTestSet(yInput);
//
//		double[][] iTrain =(xInput);
//		double[][] tarTrain =(yInput);
//
//		double[][] iTest =Statistics.divideTestSet(xInput);
//		double[][] tarTest =Statistics.divideTestSet(yInput);
//
////			double[][] iTest =Statistics.divideTrainSet(xInput);
////			double[][] tarTest =Statistics.divideTrainSet(yInput);
//
////		double[][] iTest =(xInput);
////		double[][] tarTest =(yInput);
//
//
//
//
//		double sumTrain = NetworkFunctions.matrixSum(tarTrain);
//		double sumTest= NetworkFunctions.matrixSum(tarTest);
//		double sumTotal=NetworkFunctions.matrixSum(yInput);
//		double[][] inputTest = Statistics.normalizeHistogram(iTest,tarTest);
//		double[][] targetTest = Statistics.normalizeHistogram(tarTest,iTest);
//
//		double[][] input2St = Statistics.normalizeHistogram(iTrain, tarTrain);
//		double[][] target2St = Statistics.normalizeHistogram(tarTrain, iTrain);
//
//		double[][] result = new double[][] { {} };
//		double[][] resultTest = new double[][] { {} };
//		double accuracy = 0;
//		int iterations=12;
//		while (accuracy < 1 && iterations > 0) {
//			for (int k = 0; k < input2St.length; k++) {
//				for (int j = 0; j < input2St[k].length; j++) {
//					int indexI = (int) Math.floor(Math.random() * input2St.length);
//					int indexJ = (int) Math.floor(Math.random() * input2St[0].length);
//					n3.trainInCaseOfSigmoidFunction(input2St[k][j], target2St[k][j]);
//					n3.trainInCaseOfSigmoidFunction(input2St[indexI][indexJ], target2St[indexI][indexJ]);
//				}
//			}
//
//			result = Statistics.normalizeHistogramInverse(n3.queryWithSigmoidActivationFunction(input2St, target2St,false),
//					sumTrain);
//			accuracy = Statistics.measureAccuracy2(result, tarTrain);
//			iterations--;
//		}
//		System.out.println("Trained data");
//		result = Statistics.normalizeHistogramInverse(n3.queryWithSigmoidActivationFunction(input2St, target2St, true),
//				sumTrain);
//		accuracy=Statistics.measureAccuracy(result, tarTrain);
//		System.out.println("Accuracy Train: "+accuracy );
//		System.out.println("Test data");
//		double testLength=tarTest[0].length*tarTest.length;
//		double trainLength=tarTrain[0].length*tarTrain.length;
//		double maxL=Math.max(testLength, trainLength);
//		double minL=Math.min(testLength, trainLength);
//		double maxA=Math.max(sumTest, sumTrain);
//		double minA=Math.min(sumTest, sumTrain);
//		double desiredAccuracy=1;
////		resultTest = Statistics.normalizeHistogramInverse(n3.queryWithSigmoidActivationFunction(inputTest, targetTest, false),
////				(maxA*((minL/maxL))));
////        			for(int d=0; d<(int)sumTotal; d++) {
////				resultTest = Statistics.normalizeHistogramInverse(n3.queryWithSigmoidActivationFunction(inputTest, targetTest, false),
////						sumTrain+d);
////				accuracy = Statistics.measureAccuracy(resultTest, tarTest);
////				if(accuracy>=desiredAccuracy){
////                    System.out.println("Ration of d++++ is "+ d);
////					break;
////				}
////			}
////			for(int d=(int)sumTotal; d>=0;  d--) {
////				resultTest = Statistics.normalizeHistogramInverse(n3.queryWithSigmoidActivationFunction(inputTest, targetTest, false),
////						sumTrain-d);
////				accuracy = Statistics.measureAccuracy(resultTest, tarTest);
////                if(accuracy>=desiredAccuracy){
////                    System.out.println("Ration of d -----is "+ d);
////					break;
////				}
////			}
//		double scalar=(minA+(minA-minA*(minL/maxL)));
//
//
//        resultTest = Statistics.normalizeHistogramInverse(n3.queryWithSigmoidActivationFunction(inputTest, targetTest, false),
//                scalar);
//        accuracy=Statistics.measureAccuracy(resultTest, tarTest);
//		System.out.println("Accuracy Test: "+accuracy );
//		LineChartSample.setData(iTrain, result, iTest, resultTest, xInput, yInput, NetworkFunctions.getMaxValue(xInput) + 5,
//				NetworkFunctions.getMaxValue(yInput) + 15);
//
//
//		System.out.println(sumTest+", " +sumTrain);
//
//		System.out.println(sumTotal);
//
//		LineChartSample.main(args);


//		String sURL = "http://live.reddcoin.com/api/status?q=getInfo"; //just a string
//
//		// Connect to the URL using java's native library
//		try{
//			URL url = new URL("http://live.reddcoin.com/api/status?q=getInfo");
//		URLConnection urlConnection = url.openConnection();
//		HttpURLConnection connection = null;
//		if(urlConnection instanceof HttpURLConnection) {
//			connection = (HttpURLConnection) urlConnection;
//		}else {
//			System.out.println("Please enter an HTTP URL.");
//			return;
//		}
//
//		BufferedReader in = new BufferedReader(
//				new InputStreamReader(connection.getInputStream()));
//		String urlString = "";
//		String current;
//
//		while((current = in.readLine()) != null) {
//			urlString += current;
//		}
//		System.out.println("result is: "+urlString);
//
//		}
//		catch (Exception e){
//
//		}


}
