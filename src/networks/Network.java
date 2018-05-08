package networks;

import utils.Statistics;

public abstract class Network {

//    static double learningRate = 0;
//    static double[][] wih1 = null;
//    static double[][] wh1o = null;
//    static double threshold = 0;
    String name="";
    boolean saved = false;
    public static Statistics.Normalisation type;

    public void train(double a, double b) {}
    public void train(double[][] a, double [][]b){}
    public double[][] query(double [][] a, double [][] b, boolean check) {return null;}
    public void retrieveData(String name) {}
    public void setName(String name){}
    public String getName(){ return "";};

    public void trainAndTest(double[][] i, double[][] tar, Statistics.Normalisation normalisationType,
                             double desiredAccuracy, int iterations, String[] args, String file_name, boolean show) {

    }
    public void trainAndTest(double[][] a, double[][] t, boolean visualize, String[] args, int index) {

    }


    }