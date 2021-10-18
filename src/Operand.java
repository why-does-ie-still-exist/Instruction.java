import java.util.Locale;

public class Operand {
  private final char type;
  private final char value;

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
    if (s.charAt(0) == '@') {
      return new Operand('m', (char) Integer.parseInt(s.substring(1)));
    } else {
      int radix = 0;
      switch (s.charAt(s.length() - 1)) {
        case 'h':
          radix = 16;
          break;
        case 'b':
          radix = 2;
          break;
        case 'd':
          radix = 10;
          break;
      }
      if (s.charAt(0) == '+' || s.charAt(0) == '-') {
        try {
          String bitString =
              Integer.toBinaryString(Integer.parseInt(s.substring(0, s.length() - 1)));
          bitString =
              s.charAt(0) == '+'
                  ? "00000000".substring(bitString.length()) + bitString
                  : bitString.substring(0, 8);
          char value = (char) Integer.parseInt(bitString, 2);
          return new Operand('i', value);
        } catch (NumberFormatException n) {
          throw new IllegalArgumentException("Operand could not be parsed");
        }
      } else {
        try {
          return new Operand('i', (char) Integer.parseInt(s.substring(0, s.length() - 1), radix));
        } catch (NumberFormatException n) {
          throw new IllegalArgumentException("Operand could not be parsed");
        }
      }
    }
  }

  public char getType() {
    return type;
  }

  public char getValue() {
    return value;
  }
}
