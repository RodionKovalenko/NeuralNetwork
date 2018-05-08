
package networks;

import utils.Statistics;


public class TetsAndTrainAdaline  {
	public static void main(String... args) {
		/*
		 * Adaline-Netze wurden in Ingenieursapplikationen eingesetzt, nämlich
		 * für adaptive Filter, Echounterdrückung, Mustererkennung oder
		 * Vorhersage (Patterson 1997, S. 112–117).
		 */

		// Adaline represents only a one single neuron
		// Despite the fact the adaline was originally designed to accept values
		// from +1 to -1, it can be used also for
		// real numbers

		Adaline a = new Adaline(1, 1, 0.00009, 1, 20);
		String inp=" 1.    2.    1.    1.   37.    7.    1.   18.    2.   70.    1.    6.\n" +
				"     6.    1.   59.   17.    1.    3.   35.    8.    1.   26.    3.    1.\n" +
				"    17.    1.    1.   15.    1.    1.    4.    2.    2.  112.    2.    1.\n" +
				"    29.    1.    1.    1.    1.    1.    1.    1.    1.    1.    1.    3.\n" +
				"     4.    1.    1.    2.    2.    2.    2.    7.    4.    2.    1.    1.\n" +
				"    10.    1.    6.    9.    1.    1.    2.    2.   10.    1.    1.    9.\n" +
				"     1.    1.    1.    1.   16.    6.    2.    8.    1.    1.    2.    1.\n" +
				"     3.    6.    1.    2.    1.    1.    1.    1.";
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

		a.trainAndTest(inp,t, Statistics.Normalisation.STANDARDIZE,0.85, 200, args, "+"+0, true);
		
		


	}

}
