import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

public class Assembler {
  public static void main(String[] args) {
    if (args.length == 0 || args.length > 2) {
      System.out.println("You need one or two files as an argument.");
      System.exit(2); // ERROR_FILE_NOT_FOUND Code
    }
    if (args.length == 1 && args[0].toLowerCase(Locale.ROOT).equals("-h")) {
      System.out.println(
          "This assembler takes one or two arguments: first an assembly input,"
              + "and possibly a second argument for an output file. If not provided,"
              + "the program will write out to program.dat in the current working directory.");
      System.out.println("Example usage:");
      System.out.println("java -jar IrohAsm.jar input.asm output.dat");
      System.exit(0);
    }
    var asmFile = new File(args[0]);
    var outFile = new File(args[1]);
    PrintWriter outWriter = null;
    try {
      outWriter = new PrintWriter(outFile);
    } catch (FileNotFoundException e) {
      System.out.println("Output File has a problem.");
      e.printStackTrace();
    }
    Scanner input = null;
    try {
      input = new Scanner(asmFile);
    } catch (FileNotFoundException f) {
      System.out.println("File not found: " + args[0]);
      System.exit(2); // ERROR_FILE_NOT_FOUND Code
    }
    var instructions = new ArrayList<Instruction>();
    var labels = new HashMap<String, Integer>();
    int instructionNum = 0;
    int lineNum = 1;
    while (input.hasNext()) {
      var line = input.nextLine().trim();
      if (line.charAt(line.length() - 1) == ':') {
        // Line is label
        labels.put(line.toLowerCase(Locale.ROOT).substring(0, line.length() - 1), instructionNum);
      } else {
        // Line is instruction
        instructionNum += 1;
      }
      lineNum += 1;
    }
    Scanner input2 = null;
    try {
      input2 = new Scanner(asmFile);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    while (input2.hasNext()) {
      var line = input2.nextLine();
      if (!(line.charAt(line.length() - 1) == ':')) {
        String[] tokens = line.split("[\\s|,]+");
        String[] operands = Arrays.copyOfRange(tokens, 1, tokens.length);
        try {
          if (operands.length == 1) {
            if (labels.containsKey(operands[0])) {
              operands[0] = labels.get(operands[0]).toString() + "d";
            }
            instructions.add(Mnemonic.getMnemonic(tokens[0]).getInstruction(operands));
          } else if (operands.length == 2) {
            instructions.add(Mnemonic.getMnemonic(tokens[0]).getInstruction(operands));
          } else {
            throw new RuntimeException(
                "Instruction Malformed, has " + (tokens.length - 1) + " operands");
          }
        } catch (Exception e) {
          System.out.println("Line " + lineNum + " has had an error.");
          System.out.println(e.getMessage());
          System.exit(13); // ERROR_INVALID_DATA Code
        }
        instructionNum += 1;
      }
    }

    for (Instruction instruction : instructions) {
      outWriter.println(instruction);
    }
    outWriter.close();
    System.out.println("Assembled without errors.");
  }
}
