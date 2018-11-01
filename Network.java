import java.util.Arrays;

public class Network {
	
	
	/* First index represents the layer in the neural network
	 * Second index represents the neuron in the specific layer
	 * 
	 * i.e. output[2][0] represents the output in the 1st node
	 * in layer 3
	 */
	private double[][] output;
	
	/* First index represents the layer in the neural network
	 * Second index represents the neuron in the specific layer
	 * Third index represents the neuron in the previous layer
	 * 
	 * i.e. weights[2][0][0] represents the weight going from
	 * node 1 in layer 2 to node 1 in layer 3
	 */
	private double[][][] weights;
	
	private double[][] bias;
	
	private double[][] error_signal;
	
	private double[][] output_derivative;
	
	public final int[] NETWORK_LAYER_SIZES;
	public final int INPUT_SIZE;
	public final int OUTPUT_SIZE;
	public final int NETWORK_SIZE;
	
	public Network(int ... NETWORK_LAYER_SIZES) {
		this.NETWORK_LAYER_SIZES = NETWORK_LAYER_SIZES;
		this.INPUT_SIZE = NETWORK_LAYER_SIZES[0];
		this.NETWORK_SIZE = NETWORK_LAYER_SIZES.length;
		this.OUTPUT_SIZE = NETWORK_LAYER_SIZES[NETWORK_SIZE-1];
		
		this.output = new double[NETWORK_SIZE][];
		this.weights = new double[NETWORK_SIZE][][];
		this.bias = new double[NETWORK_SIZE][];
		this.error_signal = new double[NETWORK_SIZE][];
		this.output_derivative = new double[NETWORK_SIZE][];
		
		// Create arrays at each layer, except in the first layer, which has no weights
		for(int i = 0; i < NETWORK_SIZE; i++) {
			this.output[i] = new double[NETWORK_LAYER_SIZES[i]];
			this.bias[i] = NetworkTools.createRandomArray(NETWORK_LAYER_SIZES[i], 0.3, 0.7);
			this.error_signal[i] = new double[NETWORK_LAYER_SIZES[i]];
			this.output_derivative[i] = new double[NETWORK_LAYER_SIZES[i]];
			
			if(i > 0) {
				weights[i] = new double[NETWORK_LAYER_SIZES[i]][NETWORK_LAYER_SIZES[i-1]];
				
				weights[i] = NetworkTools.createRandomArray(NETWORK_LAYER_SIZES[i],
						NETWORK_LAYER_SIZES[i-1], -0.3, 0.5);
			}
		}
	}
	
	// Forward propagation algorithm
	public double[] calculateOutput(double ... input) {
		// Number of input layer neurons does not match with the input parameter
		if(input.length != this.INPUT_SIZE) {
			return null;
		}
		
		// Set the output layer of the input layer to the input
		this.output[0] = input;
		
		// Iterate through the other layers and calculate each neurons' outputs and weights
		for(int layer = 1; layer < NETWORK_SIZE; layer++) {
			for(int neuron = 0; neuron < NETWORK_LAYER_SIZES[layer]; neuron++) {
				
				double sum = bias[layer][neuron];
				for(int prevNeuron = 0; prevNeuron < NETWORK_LAYER_SIZES[layer-1]; prevNeuron++) {
					sum += (output[layer-1][prevNeuron]) * (weights[layer][neuron][prevNeuron]);
				}
				output[layer][neuron] = sigma(sum);
				output_derivative[layer][neuron] = (output[layer][neuron]) *
						(1 - output[layer][neuron]);
			}
		}
		return output[NETWORK_SIZE-1];
	}
	
	// Trains the training set
	public void train(TrainSet ts, int loops, int batch_size) {
		if(ts.INPUT_SIZE != INPUT_SIZE || ts.OUTPUT_SIZE != OUTPUT_SIZE) {
			return;
		}
		for(int i = 0; i < loops; i++) {
			TrainSet batch = ts.extractBatch(batch_size);
			for(int b = 0; b < batch_size; b++) {
				this.trainNetwork(batch.getInput(b), batch.getOutput(b), 0.3);
			}
			System.out.println(MSE(batch));
		}
	}
	
	// Sigma transfer function used to calculate the output
	private double sigma(double x) {
		return 1 / (1 + Math.exp(-1*x));
	}
	
	public void trainNetwork(double[] input, double[] target, double learnRate) {
		if(input.length != INPUT_SIZE || target.length != OUTPUT_SIZE) {
			return;
		}
		
		calculateOutput(input);
		backPropagationError(target);
		updateWeights(learnRate);
	}
	
	// Calculate the error used for the back propagation algorithm
	public void backPropagationError(double[] target) {
		// Get the error signals for the output neurons
		for(int neuron = 0; neuron < NETWORK_LAYER_SIZES[NETWORK_SIZE-1]; neuron++) {
			error_signal[NETWORK_SIZE-1][neuron] = (output[NETWORK_SIZE-1][neuron] - 
					target[neuron]) * (output_derivative[NETWORK_SIZE-1][neuron]);
		}
		
		// Get the error signals for the hidden neurons
		for(int layer = NETWORK_SIZE-2; layer > 0; layer--) {
			// Go through every neuron in the current layer
			for(int neuron = 0; neuron < NETWORK_LAYER_SIZES[layer]; neuron++) {
				double sum = 0;
				// Go through every neuron in the next layer
				for(int nextNeuron = 0; nextNeuron < NETWORK_LAYER_SIZES[layer+1]; nextNeuron++) {
					sum += (weights[layer+1][nextNeuron][neuron]) *
							(error_signal[layer+1][nextNeuron]);
				}
				this.error_signal[layer][neuron] = sum * output_derivative[layer][neuron];
			}
		}
	}
	
	// Update the weights in the back propagation algorithm
	public void updateWeights(double learnRate) {
		for(int layer = 1; layer < NETWORK_SIZE; layer++) {
			for(int neuron = 0; neuron < NETWORK_LAYER_SIZES[layer]; neuron++) {
				
				// Update the biases
				double delta = (-1 * learnRate) * error_signal[layer][neuron];
				bias[layer][neuron] += delta;
				
				for(int prevNeuron = 0; prevNeuron < NETWORK_LAYER_SIZES[layer-1]; prevNeuron++) {
					// Update the weights
					weights[layer][neuron][prevNeuron] += delta * output[layer-1][prevNeuron];
				}
			}
		}
	}
	
	// Calculate the mean squared error (MSE) for the neural network
	public double MSE(double[] input, double[] target) {
		if(input.length != INPUT_SIZE || target.length != OUTPUT_SIZE) {
			return 0;
		}
		
		calculateOutput(input);
		double v = 0;
		
		for(int i = 0; i < target.length; i++) {
			v += (target[i] - output[NETWORK_SIZE-1][i]) * 
					(target[i] - output[NETWORK_SIZE-1][i]);
		}
		
		return v / (2d * target.length);
	}
	
	// Calculate the mean squared error (MSE) for the training set
	public double MSE(TrainSet ts) {
		double v = 0;
		for(int i = 0; i < ts.size(); i++) {
			v += MSE(ts.getInput(i), ts.getOutput(i));
		}
		
		return v / ts.size();
	}
	
	
	public static void main(String[] args) {
		/*
		Network network = new Network(4, 3, 3, 2);

		double[] input = {0.1, 0.2, 0.3, 0.4};
		double[] target = {0.9, 0.1};
		
		double[] input2 = {0.6, 0.1, 0.4, 0.8};
		double[] target2 = {0.1, 0.9};
		
		for(int i = 0; i < 10000; i++) {
			network.trainNetwork(input, target, 0.3);
			network.trainNetwork(input2, target2, 0.3);
		}
		
		System.out.println(Arrays.toString(network.calculateOutput(input)));
		System.out.println(Arrays.toString(network.calculateOutput(input2)));
		*/
		
		Network network = new Network(4, 3, 3, 2);
		TrainSet ts = new TrainSet(4, 2);
		
		ts.addData(new double[] {0.1, 0.2, 0.3, 0.4}, new double[] {0.9,0.1});
		ts.addData(new double[] {0.9, 0.8, 0.7, 0.6}, new double[] {0.1,0.9});
		ts.addData(new double[] {0.3, 0.8, 0.1, 0.4}, new double[] {0.3,0.7});
		ts.addData(new double[] {0.9, 0.8, 0.1, 0.2}, new double[] {0.7,0.3});
		
		network.train(ts, 100000, 4);
		
		for(int i = 0; i < 4; i ++) {
			System.out.println(Arrays.toString(network.calculateOutput(ts.getInput(i))));
		}
	}
}
