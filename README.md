# individual
SML individual project assignment for SDP

## Instructions to run program
   1. To launch application, run the Machine class
   2. A suitable file with instructions will be required in the src folder - some files are provided already
   
## Implementation notes and comments
   
#### Instructions

Each sub instruction type is built similarly, with two constructors -one initializing just the label and opcode and one
accepting the registers. The latter one will be used for the purposes of generating Instruction objects through reflection

Where println is required, logging functionality has been used alongside or as an alternative

#### Translator

The approach taken to find the required instruction class is as follows:
    1. Find all roots in the classpath
    2. Using this list, determine all folders which are not jar files and extract all .class files from them
    3. Convert the given files to a list of Class objects and only return the ones which are children of Instruction
    4. Find the class object required for the specific instruction type from the class generated in 3 based on opcode

Point 1-3 are in essence not required for a solution which utilizes the opcode(capitalized first letter concatenated witt
the word Instruction to determine the class name, but have been left in the code 
as they can be leveraged towards a more sophisticated potential solution which does not rely the developer
of future instructions to use the opcode+Instruction format to naming

#### Other classes

No validation checks have been made in the remainder of the code, even though registers out of range and duplicate label
checks may be appropriate, as instructions were given not to amend the remainder of the code





