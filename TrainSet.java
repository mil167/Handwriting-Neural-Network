import java.util.ArrayList;
import java.util.Arrays;

public class TrainSet {
	
	public final int INPUT_SIZE;
	public final int OUTPUT_SIZE;
	
	private ArrayList<double[][]> data = new ArrayList<>();
	
	public TrainSet(int INPUT_SIZE, int OUTPUT_SIZE) {
		this.INPUT_SIZE = INPUT_SIZE;
		this.OUTPUT_SIZE = OUTPUT_SIZE;
	}
	
	// Add new data to the training set
	public void addData(double[] input, double[] target) {
		if(input.length != INPUT_SIZE || target.length != OUTPUT_SIZE) {
			return;
		}	
		data.add(new double[][] {input, target});
	}
	
    public TrainSet extractBatch(int size) {
        if(size > 0 && size <= this.size()) {
            TrainSet set = new TrainSet(INPUT_SIZE, OUTPUT_SIZE);
            Integer[] ids = NetworkTools.randomValues(0, this.size() - 1, size);
            for(Integer i:ids) {
                set.addData(this.getInput(i),this.getOutput(i));
            }
            return set;
        }
        else {
        	return this;
        }
    }
	
    public String toString() {
        String s = "TrainSet ["+INPUT_SIZE+ " ; "+OUTPUT_SIZE+"]\n";
        int index = 0;
        for(double[][] r:data) {
            s += index +":   "+Arrays.toString(r[0]) +"  >-||-<  "+Arrays.toString(r[1]) +"\n";
            index++;
        }
        return s;
    }
	
    // Get the size of the training set
	public int size() {
		return data.size();
	}
	
	// Get the input data at a specific index
    public double[] getInput(int index) {
        if(index >= 0 && index < size()) {
            return data.get(index)[0];
        }
        else {
        	return null;
        }
    }
    
    // Get the output data at a specific index
    public double[] getOutput(int index) {
        if(index >= 0 && index < size()) {
            return data.get(index)[1];
        }
        else {
        	return null;
        }
    }

    public int getINPUT_SIZE() {
        return INPUT_SIZE;
    }

    public int getOUTPUT_SIZE() {
        return OUTPUT_SIZE;
    }
    
    public static void main(String[] args) {
        TrainSet set = new TrainSet(3,2);

        for(int i = 0; i < 8; i++) {
            double[] a = new double[3];
            double[] b = new double[2];
            for(int k = 0; k < 3; k++) {
                a[k] = (double)((int)(Math.random() * 10)) / (double)10;
                if(k < 2) {
                    b[k] = (double)((int)(Math.random() * 10)) / (double)10;
                }
            }
            set.addData(a,b);
        }

        System.out.println(set);
        System.out.println(set.extractBatch(3));
    }
	
}
