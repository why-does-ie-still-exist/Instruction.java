# IrohAsm
The Iroh CPU Assembler

This is an assembler written in Java for the [Iroh CPU](https://github.com/why-does-ie-still-exist/IrohCPU). 
More information about assembly syntax can be found there.

This assembler was written using JDK 17 but only uses features from Java 10(var keyword).

### Sample Usage:

After Compilation:

`java -jar [Your Compiled Jar].jar "Multiplication_Example.asm"`

*By default, output binary is written to program.dat*

`java -jar [Your Compiled Jar].jar "Multiplication_Example.asm" customfile.dat"`
