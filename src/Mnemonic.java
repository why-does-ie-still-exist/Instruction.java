import java.util.Locale;

public enum Mnemonic {
  ADD {
    @Override
    Instruction getInstruction(String[] ops){
      return new Instruction("1000", ops);
    }
  },
  SUB {
    @Override
    Instruction getInstruction(String[] ops){
      return new Instruction("1001", ops);
    }
  },
  MOV {
    @Override
    Instruction getInstruction(String[] ops){
      return new Instruction("0000", ops);
    }
  },
  JMP {
    @Override
    Instruction getInstruction(String[] ops){
      return new Instruction("0001", ops);
    }
  },
  JEZ {
    @Override
    Instruction getInstruction(String[] ops){
      return new Instruction("0010", ops);
    }
  };

  abstract Instruction getInstruction(String[] ops);
  public static Mnemonic getMnemonic(String s){
    return Mnemonic.valueOf(s.toUpperCase(Locale.ROOT));
  }
}
