public class Instruction {
  private String instructionID;
  private String orderString;
  private String regNum;
  private String value;

  public Instruction(String instructionID, String[] ops){
    this.instructionID = instructionID;
    Operand dest;
    Operand src;
    if(ops.length == 1){
      dest = Operand.parseOperand(ops[0]);
      if(dest.getType() == 'r'){
        orderString = "00";
        regNum = Integer.toBinaryString(dest.getValue());
        regNum = "00".substring(regNum.length()) + regNum;
        value = "00000000";
      } else if(dest.getType() == 'i' || dest.getType() == 'm') {
        value = Integer.toBinaryString(dest.getValue());
        value = "00000000".substring(value.length()) + value;
        regNum = "00";
        orderString = dest.getType() == 'm' ? "01" : "11";
      } else {
        throw new IllegalArgumentException("Operands Malformed");
      }
    } else {
      dest = Operand.parseOperand(ops[0]);
      src = Operand.parseOperand(ops[1]);

      if(dest.getType() == 'm' && src.getType() == 'r') {
        orderString = "10";
        value = Integer.toBinaryString(dest.getValue());
        value = "00000000".substring(value.length()) + value;
        regNum = Integer.toBinaryString(src.getValue());
        regNum = "00".substring(regNum.length()) + regNum;
      } else {
        value = Integer.toBinaryString(src.getValue());
        value = "00000000".substring(value.length()) + value;
        regNum = Integer.toBinaryString(dest.getValue());
        regNum = "00".substring(regNum.length()) + regNum;
        if(dest.getType() == 'r' && src.getType() == 'i'){
          orderString = "11";
        } else if(dest.getType() == 'r' && src.getType() == 'm'){
          orderString = "01";
        } else if(dest.getType() == 'r' && src.getType() == 'r'){
          orderString = "00";
        } else {
          throw new IllegalArgumentException("Operands Malformed");
        }
      }
    }
  }

  @Override
  public String toString() {
    return orderString + regNum + instructionID + value;
  }
}
