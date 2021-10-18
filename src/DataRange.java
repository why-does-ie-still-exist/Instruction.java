public class DataRange {
  private int startAddress;
  private int length;
  public DataRange(int startAddress, int length){
    if(startAddress + length > 255){
      throw new IllegalArgumentException("Memory range exceeds available memory");
    }
    this.length = length;
    this.startAddress = startAddress;
  }

  public int getNextAddress(){
    return startAddress + length;
  }
  public int getNthAddress(int n){
    if(n >= length){
      throw new IllegalArgumentException("Memory range exceeds defined length");
    }
    return startAddress + n;
  }
}
