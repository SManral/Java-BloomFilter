import java.util.ArrayList;

public class DynamicFilter {
	
	private final int INITIAL_SIZE = 1000;
	
	private int setSize;
	private int bitsPerElement;	
	private int elementsCount;
	private int filterCount;
	
	private ArrayList<BloomFilterRan> allFilters= new ArrayList<>();
	
	public DynamicFilter(int bitsPerElement){
		this.bitsPerElement = bitsPerElement;
		this.elementsCount = 0;
		this.filterCount = 0;
		this.setSize = INITIAL_SIZE; 
		this.allFilters.add(new BloomFilterRan(setSize,bitsPerElement));
	}

	public void add(String s){
		if (s != null) {
			if(elementsCount>=setSize){
				filterCount++;
				elementsCount=0;
				setSize *= 2; 
				allFilters.add(filterCount,new BloomFilterRan(setSize,bitsPerElement));
			}
			allFilters.get(filterCount).add(s);
			if(!appears(s)) 
				elementsCount++;
		}
	}
	
	public boolean appears(String s){
		if (s == null) {
            return false;
        }
		for (BloomFilterRan filter : allFilters)
		{
			if (filter.appears(s))
				return true;
		}
       return false; 	
	}
	
	public int filterSize(){
		return allFilters.get(filterCount).filterSize();
	}
	
	public int dataSize(){
		return 	allFilters.get(filterCount).dataSize();
	}
	
	public int numHashes(){
		return allFilters.get(filterCount).numHashes();	
	}
}
