package utils;

public class Statistics {

	static NetworkFunctions n = new NetworkFunctions();
	public enum Normalisation { NORMALIZEMAXMIN, STANDARDIZE, NORMALIZECUMULATIVE
	};
	public static double mean(double... a) {
		double result = 0;
        for (double anA : a) {
            result += anA / a.length;
        }
	//	System.out.println("\nMean: " + result);

		return result;
	}

	public static double mean(double[]... a) {
		double result = 0;
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				result += a[i][j] / (a.length * a[i].length);
			}
		}
	//	System.out.println("\nMean: " + result);

		return result;
	}

	public static double variance(double... a) {

		double mean = mean(a);

		double variance = 0;
		for (int i = 0; i < a.length; i++) {
			variance += Math.pow(a[i] - mean, 2) / a.length;
		}

		//System.out.println("Variance " + variance);
		return variance;

	}

	public static double variance(double[]... a) {

		double mean = mean(a);

		double variance = 0;
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				variance += Math.pow(a[i][j] - mean, 2) / (a.length * a[i].length);
			}
		}
	//	System.out.println("Variance " + variance);
		return variance;

	}

	public static double cov(double... a) {
		double mean = mean(a);

		double sqDif = 0;
		for (int i = 0; i < a.length; i++) {
			sqDif += Math.pow(a[i] - mean, 2);
		}
		if (a.length > 1) {
			sqDif /= a.length - 1;
		}

		System.out.println("Result of cov: " + sqDif);

		return sqDif;
	}

	public static double cov(double[] a, double[] b) {
		double meanA = mean(a);
		double meanB = mean(b);

		double sum = 0;
		for (int i = 0; i < a.length; i++) {
			sum += (a[i] - meanA) * (b[i] - meanB);
		}

		System.out.println("Result is cov(A,B) " + sum);

		return sum;
	}

	public static double cov2(double[] a, double[] b) {

		double sum = 0;
		for (int i = 0; i < a.length; i++) {
			sum += (a[i] * b[i]);
		}
		sum -= (matrixSum(a) * matrixSum(b));

		return sum;
	}

	public static double matrixSum(double[] a) {
		double sum = 0;
		for (int i = 0; i < a.length; i++) {
			sum += a[i];
		}

		return sum;
	}

	public static double matrixSum(double[][] a) {
		double sum = 0;
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				sum += a[i][j];
			}
		}
		return sum;
	}

	public static double corCoefficient(double[] a, double[] b) {
		double meanA = mean(a);
		double meanB = mean(b);
		double cov = cov(a, b);
		double sqD1 = 0;
		for (int i = 0; i < a.length; i++) {
			sqD1 += Math.pow(a[i] - meanA, 2);
		}

		double sqD2 = 0;
		for (int i = 0; i < b.length; i++) {
			sqD2 += Math.pow(b[i] - meanB, 2);
		}

		double result = Math.sqrt(sqD1) * Math.sqrt(sqD2);
		double corCoef = 0;
		if (result != 0) {
			corCoef = cov / (result);
			System.out.println("Correlation Coefficient: " + corCoef);
		} else {
			System.out.println("Correlation Coefficient: " + corCoef);
		}

		return corCoef;
	}

	public static double[] fit(double[] a) {
		return a;
	}

	// Standardization
	public static double[] standardize(double[] a) {

		double[] result = new double[a.length];
		double variance = variance(a);
		double mean = mean(a);
		for (int i = 0; i < a.length; i++) {
			result[i] = (a[i] - mean) / Math.sqrt(variance);
		}

		//n.printArray(result);
		return result;
	}

	public static double[][] standardize(double[][] a) {
		double[][] result = new double[a.length][a[0].length];
		double variance = variance(a);
		double mean = mean(a);
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				result[i][j] = (a[i][j] - mean) / Math.sqrt(variance);
			}
		}
//		System.out.print("Standardized result: ");
//		NetworkFunctions.printArray(result);
		return result;
	}

	public static double[][] standardizeInverse(double[][] a, double mean, double variance) {
		double[][] result = new double[a.length][a[0].length];

		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				result[i][j] = mean + (a[i][j] * Math.sqrt(variance));
			}
		}
//		System.out.print("Transformed to target result: ");
//		n.printArray(result);
		return result;
	}

	public static double[][] normalize(double[][] a) {
		double squareSum = 0;
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				squareSum += Math.pow(a[i][j], 2);
			}
		}
		double n = Math.sqrt(squareSum);

		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				a[i][j] /= n;
			}
		}
		return a;

	}

	public static double[] normalize(double[] a) {

		double squareSum = 0;
		for (int i = 0; i < a.length; i++) {
			squareSum += Math.pow(a[i], 2);
		}
		double n = Math.sqrt(squareSum);
		for (int i = 0; i < a.length; i++) {
			a[i] /= n;

		}
		return a;

	}

	public static  double[][] normalizeInverse(double[][] a) {
		double squareSum = 0;
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				a[i][j] *= a.length;
			}
		}
		double n = Math.sqrt(squareSum);

		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				a[i][j] /= n;
			}
		}
		return a;

	}

	public static double[][] normalizeMaxMin(double[][] a) {
		double[][] result = new double[a.length][a[0].length];
		double max = getMaxValue(a);
		double min = getMinValue(a);

		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				result[i][j] = (a[i][j] - min) / (max - min);
			}
		}

		return result;
	}

	public static double[][] normalizeMaxMinInverse(double[][] a, double max, double min) {
		double[][] result = new double[a.length][a[0].length];

		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				result[i][j] = min + a[i][j] * (max - min);
			}
		}

		return result;
	}

	public static double[][] normalizeHistogram(double[][] a, double[][] b) {
		double sum = 0;
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				sum += a[i][j];
			}
		}
		double[][] r = cumulativeDistributionFunction(a, b);

		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				r[i][j] = a[i][j] / sum;
			}
		}
		return r;

	}

	public static double[][] normalizeHistogramInverse(double[][] a, double sum) {
		double[][] r = new double[a.length][a[0].length];
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				r[i][j] = a[i][j] * sum;
			}
		}

		return r;

	}

	public static double getMaxValue(double[][] a) {
		double max = a[0][0];
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				if (max < a[i][j]) {
					max = a[i][j];
				}
			}
		}
		return max;
	}

	public static double getMinValue(double[][] a) {
		double min = a[0][0];
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				if (min > a[i][j]) {
					min = a[i][j];
				}
			}
		}
		return min;
	}

	// pmf
	public static double[] probabilityMassFunction(double[] a) {
		double[] result = new double[a.length];
		a = sortNumbersDownUp(a);
		double sum = matrixSum(a);
		for (int i = 0; i < a.length; i++) {
			result[i] = (a[i] / sum) * 100;
		}

		return result;
	}

	public static double[][] probabilityMassFunction(double[][] a, double[][] b) {
		double[][] result = new double[a.length][a[0].length];
		a = sortNumbersDownUp(a, b);
		double sum = matrixSum(a);
		//System.out.println(sum);
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				result[i][j] = (a[i][j] / sum);
			}
		}

		return result;
	}

	public static double[] cumulativeDistributionFunction(double[] a) {
		double[] result = new double[a.length];
		double[] pmf = probabilityMassFunction(a);
		result[0] = pmf[0];
		for (int i = 1; i < a.length; i++) {
			result[i] = result[i - 1] + pmf[i];
		}

	//	n.printArray(result);
		return result;
	}

	public static double[][] cumulativeDistributionFunction(double[][] a, double[][] b) {
		double[][] result = new double[a.length][a[0].length];
		double[][] pmf = probabilityMassFunction(a, b);
		result[0][0] = pmf[0][0];
		for (int i = 0; i < a.length; i++) {
			for (int j = 1; j < a[i].length; j++) {
				if (j == 1 && i >= 1) {
					result[i][j - 1] += result[i - 1][a[i].length - 1] + pmf[i][j - 1];
					result[i][j] += result[i][j - 1] + pmf[i][j];
				} else if (j == 1) {
					result[i][j] += result[i][j - 1] + pmf[i][j];
				} else {
					result[i][j] += result[i][j - 1] + pmf[i][j];
				}
			}
		}
		//n.printArray(result);

		return result;
	}

	public static double[] sortNumbersDownUp(double[] a) {
		double[] result = a.clone();
		double tmp = 0;
		for (int i = 0; i < a.length; i++) {
			for (int j = i + 1; j < a.length; j++) {
				if (result[i] > result[j]) {
					tmp = result[i];
					result[i] = result[j];
					result[j] = tmp;
				}

			}
		}

	//	n.printArray(result);
		return result;
	}

	public static double[][] sortNumbersDownUp(double[][] a, double[][] b) {
		double[][] result = a.clone();
		double[][] result2 = b.clone();
		double tmp = 0;
		double tmp2 = 0;
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				for (int k = 0; k < a.length; k++) {
					for (int d = 0; d < a[k].length; d++) {
						if (result[i][j] < result[k][d]) {
							tmp = result[i][j];
							result[i][j] = result[k][d];
							result[k][d] = tmp;

							tmp2 = result2[i][j];
							result2[i][j] = result2[k][d];
							result2[k][d] = tmp2;
						}
					}
				}
			}
		}

	//	n.printArray(result);
		return result;
	}

	public static double measureAccuracy(double[][] output, double[][] target) {

		double st = Math.sqrt(variance(target));
		double count = 0;
		for (int i = 0; i < output.length; i++) {
			for (int j = 0; j < output[i].length; j++) {
				if (st != 0) {
					if (output[i][j] >= target[i][j] - st && output[i][j] <= target[i][j] + st) {
						count++;
					}
				} else {
					if (output[i][j] >= target[i][j] - 0.01 && output[i][j] <= target[i][j] + 0.01) {
						count++;
					}
				}
			}
		}
	//	System.out.println("Accuracy: " + count / (target.length * target[0].length));
	//	System.out.println("Standard deviation: " + st);


		return count / (target.length * target[0].length);
	}

	public static double measureAccuracy2(double[][] output, double[][] target) {

		double st = Math.sqrt(variance(target));
		st /= 2;
		double count = 0;
		for (int i = 0; i < output.length; i++) {
			for (int j = 0; j < output[i].length; j++) {
				if (st != 0) {
					if (output[i][j] >= target[i][j] - st && output[i][j] <= target[i][j] + st) {
						count++;
					}
				} else {
					if (output[i][j] >= target[i][j] - 0.01 && output[i][j] <= target[i][j] + 0.01) {
						count++;
					}
				}
			}
		}
//		System.out.println("Accuracy: " + count / (target.length * target[0].length));
//		System.out.println("Standard deviation: " + st);
		return count / (target.length * target[0].length);
	}

	public static double[][] divideTrainSet(double[][] a) {
		double[][] result = new double[a.length][a[0].length];
		int count = 0;
		int ind = (a.length * a[0].length / 3) * 2;
		if((a.length * a[0].length) %3==1) {
			ind+=1; 
		}
		else if ((a.length * a[0].length)%3==2){
			ind+=2; 
		}
		//System.out.println(ind);
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				if (ind > count) {
					result[i][j] = a[i][j];
					count++;
				}
			}
		}

		double[][] finalResult = new double[1][count];
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				if (result[i][j] != 0) {
					finalResult[i][j] = a[i][j];
				}
			}
		}

	//	n.printArray(finalResult);
		return finalResult;
	}

	public static double[][] divideTestSet(double[][] a) {
		double[][] result = new double[a.length][a[0].length];
		int count = 0;
		int ind = a.length * a[0].length / 3;
		for (int i = a.length - 1; i >= 0; i--) {
			for (int j = a[i].length - 1; j >= 0; j--) {
				if (ind > count) {
					result[i][j] = a[i][j];
					count++;
				}
			}
		}

		double[][] finalResult = new double[1][count];
		count=0; 
		for (int i = a.length - 1; i >= 0; i--) {
			for (int j = a[i].length - 1; j >= 0; j--) {
				if (result[i][j] != 0) {
					if(count<finalResult.length*finalResult[0].length)
					finalResult[i][j%finalResult[0].length] = result[i][j];
					count++; 
				}
			}
		}

	//	n.printArray(finalResult);
		return finalResult;
	}

	public static void main(String... args) {
		mean(3.7, 4.0, 1.7, 2.7, 3.3, 3.0, 3.3, 1.3, 2.7, 2.7, 3.7, 3.7, 1.7, 2.3, 2.7, 3.0, 3.7, 3.0, 3.3, 2.7, 2.7,
				1.3, 2.7, 2.0, 1.7, 2.0, 1.3);

		NetworkFunctions n = new NetworkFunctions();

		double[] x = { 6.575, 6.4210, 7.185, 6.998, 7.147, 6.43, 6.012, 6.172, 5.631, 6.004 };

		double[] c = { 0.538, 0.469, 0.469, 0.458, 0.458, 0.458, 0.524, 0.524, 0.524, 0.524 };
		double[][] input = { { 7, 99, 24, 21.6, 34.7, 33.4, 99, 78, 88 },
				{ 24, 21.6, 34.7, 33.4, 36.2, 28.7, 22.9, 27.1, 16.5 } };
		double[][] target = { { 3, 9, 12, 4, 8, 6.8, 33, 23, 43 }, { 33, 1.6, 2.7, 2.4, 6.2, 28.7, 22.9, 27.1, 16.5 } };

		probabilityMassFunction(input, target);
		cumulativeDistributionFunction(input, target);

	}
}
