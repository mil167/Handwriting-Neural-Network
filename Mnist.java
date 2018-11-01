/*
 * NOTE: This file was provided by Finn Eggers YouTube channel
 * https://www.youtube.com/watch?v=_O2rlikBHcA&list=PLgomWLYGNl1dL1Qsmgumhcg4HOcWZMd3k&index=3
 * 
 */

import java.io.File;

/**
 * Created by Luecx on 10.08.2017.
 */
public class Mnist {

    public static void main(String[] args) {
        Network network = new Network(784, 70, 35, 10);
        TrainSet set = createTrainSet(0,4999);
        trainData(network, set, 100, 50, 100);

        TrainSet testSet = createTrainSet(5000,9999);
        testTrainSet(network, testSet, 10);
    }

    public static TrainSet createTrainSet(int start, int end) {

        TrainSet set = new TrainSet(28 * 28, 10);

        try {

    		String path = new File("").getAbsolutePath();
        	
            MnistImageFile m = new MnistImageFile(path + "/src/trainImage.idx3-ubyte", "rw");
            MnistLabelFile l = new MnistLabelFile(path + "/src/trainLabel.idx1-ubyte", "rw");

            for(int i = start; i <= end; i++) {
                if(i % 100 ==  0){
                    System.out.println("prepared: " + i);
                }

                double[] input = new double[28 * 28];
                double[] output = new double[10];

                output[l.readLabel()] = 1d;
                for(int j = 0; j < 28*28; j++){
                    input[j] = (double)m.read() / (double)256;
                }

                set.addData(input, output);
                m.next();
                l.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
         return set;
    }

    public static void trainData(Network net,TrainSet set, int epochs, int loops, int batch_size) {
        for(int e = 0; e < epochs;e++) {
            net.train(set, loops, batch_size);
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>   "+ e+ "   <<<<<<<<<<<<<<<<<<<<<<<<<<");
        }
    }

    public static void testTrainSet(Network net, TrainSet set, int printSteps) {
        int correct = 0;
        for(int i = 0; i < set.size(); i++) {

            double highest = NetworkTools.indexOfHighestValue(net.calculateOutput(set.getInput(i)));
            double actualHighest = NetworkTools.indexOfHighestValue(set.getOutput(i));
            if(highest == actualHighest) {

                correct ++ ;
            }
            if(i % printSteps == 0) {
                System.out.println(i + ": " + (double)correct / (double) (i + 1));
            }
        }
        System.out.println("Testing finished, RESULT: " + correct + " / " + set.size()+ "  -> " + (double)correct / (double)set.size() +" %");
    }
}
