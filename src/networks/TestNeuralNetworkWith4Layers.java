
package networks;
import utils.NetworkFunctions;
import utils.Statistics;


public class TestNeuralNetworkWith4Layers {

	public static void main(String... args) {

		NeuralNetworkWith4Layers n = new NeuralNetworkWith4Layers(1, 100, 100, 1, 0.78);

//
//		String t = " 3.3 3.1 6.8 6.4 5.3 5. 2.3 0.7 4.95 2.95 " + "2.4 7.3 " + "11.6 10.8 2.8 3.4 10.2 3.45 7.9 3.5 "
//				+ "5.5 3.2 4. 0.95 1.6 7. 4.85 4.3 5.4 12.5 " + " 11.5 11.8 12.8" + "17.2" + " 13. 6 3 ";
//		String inp = " 386. 58. 272. 39. 196. 37. 134. 269. 88. 208. 103." + " 102.   "
//				+ " 113. 14. 2. 112. 105. 48. 83. 2. 40. 24. 20. 40. " + " 6. 9. 16." + " 55. 12. 19. 2. 23. 90. 10. 59 ";


		String inp=" 1.    2.    1.    1.   37.    7.    1.   18.    2.   70.    1.    6.\n" +
				"     6.    1.   59.   17.    1.    3.   35.    8.    1.   26.    3.    1.\n" +
				"    17.    1.    1.   15.    1.    1.    4.    2.    2.  112.    2.    1.\n" +
				"    29.    1.    1.    1.    1.    1.    1.    1.    1.    1.    1.    3.\n" +
				"     4.    1.    1.    2.    2.    2.    2.    7.    4.    2.    1.    1.\n" +
				"    10.    1.    6.    9.    1.    1.    2.    2.   10.    1.    1.    9.\n" +
				"     1.    1.    1.    1.   16.    68.    2.    80.    1.    1.    2.    1.\n" +
				"     3.    6.    19.    29.    1.    1.    10.    1.";
		String t="  4.99   6.99   3.29   5.29  12.95   5.19   5.45   5.14  10.49  12.9\n" +
				"    5.35   5.24   5.09   5.65   6.43   6.37   5.04  13.16   6.48   6.63\n" +
				"    6.89   6.53   6.27   6.32  13.     7.3    5.91   6.58   6.17   6.01\n" +
				"    6.78  10.43   6.84  12.85  13.21   7.86  12.8    7.25   6.94  11.72\n" +
				"   15.16   6.68  15.27  12.13   8.02  15.68  12.34   6.22   7.56  18.04\n" +
				"    7.92  13.42  13.11   7.45  15.42  10.33  12.59   7.14  12.23  14.14\n" +
				"   12.75  13.31  10.28   7.66   8.33  12.39  13.57   8.28   7.76  14.19\n" +
				"   13.98  13.06  13.62  14.65  14.08   8.38   7.71   7.81  12.18  12.7\n" +
				"   12.44  11.46   7.61   9.41  10.23  10.38   9.2   12.64  25.7    8.94\n" +
				"   10.13  25.91";
	n.trainAndTest(inp, t, Statistics.Normalisation.NORMALIZECUMULATIVE, 1, 100, args, ""+0, true);

		double[][] i = new double[][] { NetworkFunctions.convertStringToNumbers(inp) };
		double[][] tar = new double[][] { NetworkFunctions.convertStringToNumbers(t) };

		NetworkFunctions.printMatrixSize(i);
		double[][] iTrain = Statistics.divideTrainSet(i);
		double[][] tarTrain = Statistics.divideTrainSet(tar);

		double[][] iTest = Statistics.divideTestSet(i);
		double[][] tarTest = Statistics.divideTestSet(tar);

		NetworkFunctions.printArray(iTrain);
		NetworkFunctions.printArray(tarTrain);
		NetworkFunctions.printArray(iTest);
		NetworkFunctions.printArray(tarTest);



	}
}
