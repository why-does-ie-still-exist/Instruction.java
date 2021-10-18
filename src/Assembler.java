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
    File outFile;
    if (args.length == 2) {
      outFile = new File(args[1]);
    } else {
      outFile = new File("program.dat");
    }
    PrintWriter outWriter = null;
    try {
      outWriter = new PrintWriter(outFile);
    } catch (FileNotFoundException e) {
      System.out.println("Output File has a problem.");
      e.printStackTrace();
      System.exit(2); // ERROR_FILE_NOT_FOUND Code
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

    // Scan for labels
    while (input.hasNext()) {
      var line = input.nextLine().trim();
      if (line.matches("\\w+:")) {
        // Line is label
        labels.put(line.toLowerCase(Locale.ROOT).substring(0, line.length() - 1), instructionNum);
      } else if (!(line.charAt(0) == '#'
          || line.matches("^\\w+\\[\\d+\\]$"))) { // If line is not comment, it must be instruction
        instructionNum += 1;
      }
    }

    // instructionNum is now 1 greater than the address of the last instruction, so one less is the
    // first
    // address where data memory can be placed
    int prevLastAddress = instructionNum;
    // scanning for named memory ranges at top of file
    Scanner input2 = null;
    var ranges = new HashMap<String, DataRange>();
    try {
      input2 = new Scanner(asmFile);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      System.exit(2); // ERROR_FILE_NOT_FOUND Code
    }
    try {
      while (input2.hasNext()) {
        var line = input2.nextLine();
        if (line.matches("^\\w+\\[\\d+\\]$")) { // initialize data ranges
          var tokens = line.split("\\[|\\]");
          var name = tokens[0].toLowerCase(Locale.ROOT);
          var length = Integer.parseInt(tokens[1]);
          var range = new DataRange(prevLastAddress, length);
          prevLastAddress = range.getNextAddress();
          ranges.put(name, range);
        } else {
          if (line.charAt(0) != '#') { // if line is not comment, stop initializing data ranges
            break;
          }
        }
      }
    } catch (Exception e) {
      System.out.println("Error when parsing data ranges.");
      e.printStackTrace();
      System.exit(13); // ERROR_INVALID_DATA Code
    }

    int lineNum = 1;
    Scanner input3 = null;
    try {
      input3 = new Scanner(asmFile);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      System.exit(2); // ERROR_FILE_NOT_FOUND Code
    }
    while (input3.hasNext()) {
      var line = input3.nextLine();
      if (!(line.trim().matches("\\w+:")
          || line.trim().matches("^\\w+\\[\\d+\\]$")
          || line.trim().charAt(0)
              == '#')) { // If line is not label, data range declaration, or comment, proceed.
        String[] tokens = line.split("[\\s|,]+");
        String instruction = tokens[0];
        String[] operands = Arrays.copyOfRange(tokens, 1, tokens.length);
        try {
          for (int i = 0; i < operands.length; i++) { // generate offsets from data range
            if (operands[i].matches("\\w+\\[\\d+\\]")) {
              var dataRangeParts = operands[i].trim().split("\\[|\\]");
              var name = dataRangeParts[0].toLowerCase(Locale.ROOT);
              var nth = Integer.parseInt(dataRangeParts[1]);
              try {
                operands[i] = "@" + ranges.get(name).getNthAddress(nth);
              } catch (NullPointerException n) {
                  throw new NullPointerException("Label not found");
              }
            }
          }
          for (int i = 0; i < operands.length; i++) { // turns labels into imm
            if (labels.containsKey(operands[i])) {
              operands[i] = labels.get(operands[i]) + "d";
            }
          }
          if (operands.length == 1) {
            instructions.add(Mnemonic.getMnemonic(instruction).getInstruction(operands, lineNum));
          } else if (operands.length == 2) {
            if (operands[1].matches(
                "offset:\\w+")) { // Generate imm data range offset, can only be in second operand
              var offsetParts = operands[1].trim().split(":");
              try {
                operands[1] = ranges.get(offsetParts[1].toLowerCase(Locale.ROOT)).getNthAddress(0) + "d";
              } catch (NullPointerException n) {
                throw new NullPointerException("Label not found");
              }
            }
            instructions.add(Mnemonic.getMnemonic(instruction).getInstruction(operands, lineNum));
          } else {
            throw new RuntimeException(
                "Instruction Malformed, has " + (operands.length) + " operands");
          }
        } catch (Exception e) { // catch errors when generating operands
          System.out.println("Line " + lineNum + " has had an error.");
          e.printStackTrace();
          System.exit(13); // ERROR_INVALID_DATA Code
        }
        instructionNum += 1;
      }
      lineNum += 1;
    }

    for (Instruction instruction : instructions) {
      if (instruction.toString().length() == 16) {
        outWriter.println(instruction);
      } else {
        System.out.println(
            "There was an issue with a malformed instruction on line "
                + instruction.getLineNum()
                + ".");
        System.exit(13); // ERROR_INVALID_DATA Code
      }
    }
    outWriter.close();
    System.out.println("Assembled without errors.");
  }
}
