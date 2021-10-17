import java.util.Locale;

public class Operand {
  private char type;
  private char value;

  private Operand(char type, char value) {
    if (type == 'r' || type == 'm' || type == 'i') {
      this.type = type;
      this.value = value;
    } else {
      throw new IllegalArgumentException("Operand must be a reg, mem, or imm");
    }
  }

  public static Operand parseOperand(String s) {
    switch (s.toLowerCase(Locale.ROOT)) {
      case "ab":
        return new Operand('r', (char) 0b00);
      case "bb":
        return new Operand('r', (char) 0b01);
      case "cb":
        return new Operand('r', (char) 0b10);
      case "db":
        return new Operand('r', (char) 0b11);
    }
    if(s.charAt(0) == '@'){
      return new Operand('m', (char) Integer.parseInt(s.substring(1)));
    } else {
      if(s.charAt(s.length() - 1) == 'h'){
        return new Operand('i', (char) Integer.parseInt(s.substring(0,s.length() - 1),16));
      } else if(s.charAt(s.length() - 1) == 'b'){
        return new Operand('i', (char) Integer.parseInt(s.substring(0,s.length() - 1),2));
      } else if(s.charAt(s.length() - 1) == 'd'){
        return new Operand('i', (char) Integer.parseInt(s.substring(0,s.length() - 1),10));
      }
    }
    throw new IllegalArgumentException("Invalid Operand");
  }

  public char getType(){
    return type;
  }

  public char getValue() {
    return value;
  }
}
