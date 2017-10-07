import java.nio.charset.StandardCharsets;
import java.util.BitSet;

public class BloomFilterFNV {
	
	private static final long FNV64INIT = 0xcbf29ce484222325L;
	private static final long FNV64PRIME = 0x100000001b3L;
	
	private int filterSize;
	private int numHashes;	
	private BitSet bloomFilter;
	
	public BloomFilterFNV(int setSize, int bitsPerElement){
		this.filterSize = setSize * bitsPerElement;
		this.bloomFilter = new BitSet(filterSize); 
		this.numHashes = (setSize == 0) ? 0 : (int) (Math.log(2)*(filterSize/setSize));
	}
	
	public int[] fnvHash(byte[] bytes){
		int[] hashValues = new int[numHashes];
		for (int i=0 ; i<numHashes; i++){
			long hash = FNV64INIT;
			for (int j=0; j<bytes.length; j++) {
				hash *= FNV64PRIME;
			    hash ^= bytes[j];
			}
			if(i>0)
				hashValues[i]=Math.abs((int)(hash+(i*hashValues[i-1])+i)%filterSize); 
			else
				hashValues[i]=Math.abs((int)hash%filterSize);
		}
		return hashValues;	
	}
	
	public void add(String s){
		if (s != null ) {
			byte[] b = s.getBytes(StandardCharsets.UTF_8);
			for(int index : fnvHash(b)){
				bloomFilter.set(index,true);
			}
		}
	}
	
	public boolean appears(String s){
		if (s == null) {
            		return false;
        	}
		byte[] b = s.getBytes(StandardCharsets.UTF_8);
		for (int index : fnvHash(b)) { 
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
