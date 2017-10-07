import java.util.BitSet;
import java.util.Random;

public class BloomFilterRan {
	
	private int filterSize;
	private int numHashes;	
	private BitSet bloomFilter;
	private int smallestPrime;
	private int[]randomVarArray;
	
	public BloomFilterRan(int setSize, int bitsPerElement){
		this.filterSize = setSize * bitsPerElement;
		this.bloomFilter = new BitSet(filterSize); 
		this.numHashes = (int) (Math.log(2)*(filterSize/setSize));
		this.smallestPrime = getFirstPrime(filterSize);
		this.randomVarArray = new int[numHashes*2];
		Random rand = new Random();
		for(int i=0; i<numHashes; i++){
			randomVarArray[i+i] = rand.nextInt(smallestPrime);
			randomVarArray[(i+i)+1] = rand.nextInt(smallestPrime);	
		}
	}
	
	public static int getFirstPrime(int n){
		for(int i=2; i<n; i++){
	        if(n%i==0){
	        	i=1;
	            n=n+1; 
	        } 
		}
	    return n;
	}	
	
	public int[] ranHash(String word){
		int[] hashValues = new int[numHashes];
		for (int i=0 ; i<numHashes; i++){
			int hash = word.hashCode();
			hash = ((randomVarArray[i+i]*hash) + randomVarArray[(i+i)+1])%smallestPrime;
			hashValues[i] = Math.abs(hash)%filterSize;
		}
		return hashValues;
	}
	
	public void add(String s){
		if(s!=null){
			for(int index : ranHash(s)){
				bloomFilter.set(index);
			}
		}
	}
	
	public boolean appears(String s){
		if (s == null) {
            return false;
        }
		for (int index : ranHash(s)) { 
            if (!bloomFilter.get(index)) { 
                return false; 
            } 
        } 
       return true; 	
	}
	
	public int filterSize(){
		return filterSize;
	}
	
	public int dataSize(){
		return bloomFilter.cardinality();			
	}
	
	public int numHashes(){
		return numHashes;	
	}
}
