import java.util.Locale;

public enum Mnemonic {
  ADD {
    @Override
    Instruction getInstruction(String[] ops, int lineNum) {
      return new Instruction("1000", ops, lineNum);
    }
  },
  SUB {
    @Override
    Instruction getInstruction(String[] ops, int lineNum) {
      return new Instruction("1001", ops, lineNum);
    }
  },
  MOV {
    @Override
    Instruction getInstruction(String[] ops, int lineNum) {
      return new Instruction("0000", ops, lineNum);
    }
  },
  JMP {
    @Override
    Instruction getInstruction(String[] ops, int lineNum) {
      return new Instruction("0001", ops, lineNum);
    }
  },
  JEZ {
    @Override
    Instruction getInstruction(String[] ops, int lineNum) {
      return new Instruction("0010", ops, lineNum);
    }
  };

  public static Mnemonic getMnemonic(String s) {
    return Mnemonic.valueOf(s.toUpperCase(Locale.ROOT));
  }

  abstract Instruction getInstruction(String[] ops, int lineNum);
}
