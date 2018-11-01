
public class NetworkTools {

	// Create a new double array with each element having a preset value
	// given by the parameter
	public static double[] createArray(int size, double init_value) {
		if(size < 1) {
			return null;
		}
		
		double [] arr = new double[size];
		for(int i = 0; i < size; i++) {
			arr[i] = init_value;
		}
		return arr;
	}
	
	// Create a new double array with each element having a random value
	public static double[] createRandomArray(int size, double lower_bound, double upper_bound) {
		if(size < 1) {
			return null;
		}
		
		double [] arr = new double[size];
		for(int i = 0; i < size; i++) {
			arr[i] = randomValue(lower_bound, upper_bound);
		}
		return arr;
	}
	
	// Create a new 2D double array with each element having a random value
	public static double[][] createRandomArray(int sizeX, int sizeY, double lower_bound,
			double upper_bound) {
		if(sizeX < 1 || sizeY < 1) {
			return null;
		}
		
		double [][] arr = new double[sizeX][sizeY];
		for(int i = 0; i < sizeX; i++) {
			arr[i] = createRandomArray(sizeY, lower_bound, upper_bound);
		}
		return arr;
	}
	
	// Generate a random value between the lower and upper bounds
	public static double randomValue(double lower_bound, double upper_bound) {
		return Math.random() * (upper_bound - lower_bound) + lower_bound;
	}
	
	// Makes sure that the array contains unique values
	public static Integer[] randomValues(int lowerBound, int upperBound, int amount) {
		lowerBound--;
		
		if(amount > (upperBound - lowerBound)) {
			return null;
		}
		
		Integer[] values = new Integer[amount];
		for(int i = 0; i < amount; i++) {
			int n = (int)(Math.random() * (upperBound - lowerBound + 1) + lowerBound);
			
			while(containsValue(values, n)) {
				n = (int)(Math.random() * (upperBound - lowerBound + 1) + lowerBound);
			}
			values[i] = n;
		}
		return values;
	}
	
	// Checks input array to see if it contains a certain value, given by parameter
	public static <T extends Comparable<T>> boolean containsValue(T[] arr, T value) {
		for(int i = 0; i < arr.length; i++) {
			if(arr[i] != null) {
				if(value.compareTo(arr[i]) == 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	// Returns index of the highest valued element in an array
	public static int indexOfHighestValue(double[] values) {
		int index = 0;
		for(int i = 0; i < values.length; i++) {
			if(values[i] > values[index]) {
				index = i;
			}
		}
		return index;
	}
	
	
}
