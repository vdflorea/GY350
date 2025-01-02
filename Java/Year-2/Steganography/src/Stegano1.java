
/**
 * CT255 - Assignment 4
 *
 * Appending "Vlad" --> A inp.txt out.txt 01010110011011000110000101100100
 * Appending "Florea" --> A inp.txt out.txt 010001100110110001101111011100100110010101100001
 * Extracting --> E out.txt
 *
 * Usage:
 * (1) Change "isMethodOne" boolean to true for Part 1
 * (2) Run append (A) with first name "Vlad" (command above)
 * (3) Run extract (E) (command above)
 * (4) Change "isMethodOne" boolean to false for Part 2
 * (5) Run append (A) with surname "Florea" (command above)
 * (6) Run extract (E) (command above)
 *
 * @author Michael Schukat / Vlad Florea
 * @version 1.0
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Stegano1
{

    public static void main(String[] args) {
        // arg1 = Append / Extract (A/E)
        // arg2 = input file (Make sure to send in output file as input file when Extracting)
        // arg3 = output file
        // arg4 = Name encoded in binary

        String arg1, arg2, arg3, arg4;
        boolean isMethodOne = true; // Hard-code true/false for method1/method2
        boolean err = false;
        
        if (args != null && args.length > 1) { // Check for minimum number of arguments

            arg1 = args[0]; // arg1 = Append / Extract (A/E)
            arg2 = args[1]; // arg2 = input file
                
            if (arg2 == "") {
                err = true;
            }
            else if ((arg1.equals("A")) && (args.length > 3)){

                arg3 = args[2]; // arg3 = output file
                arg4 = args[3]; // arg4 = Name encoded in binary

                if (arg3.isEmpty() || arg4.isEmpty()) {
                    err = true;
                }
                else {

                    // Method One: "Hides" ONE bit per line
                    // Method Two: "Hides" TWO bits per line
                    if (isMethodOne) {
                        hide(arg2, arg3, arg4);
                    } else {
                        hide2(arg2, arg3, arg4);
                    }

                }
            }
            else if (arg1.equals("E")){

                // Method One: Retrieves encoded binary string based on output in out.txt AFTER hide() function
                // Method Two: Retrieves encoded binary string based on output in out.txt AFTER hide2() function
                if (isMethodOne) {
                    retrieve(arg2);
                } else {
                    retrieve2(arg2);
                }

            }
            else {
                err = true;
            }
        }
        else {
            err = true;
        }
        
        if (err == true) {
            // Prints usage details as an error message

            System.out.println();
            System.out.println("Use: Stegano1 <A:E><Input File><OutputFile><Bitstring>");
            System.out.println("Example: Stegano1 A inp.txt out.txt 0010101");
            System.out.println("Example: Stegano1 E inp.txt");
            
        } 
    }
    
    static void hide(String inpFile, String outFile, String binString) {
        BufferedReader reader;
        BufferedWriter writer;
	
        try {
            int i = 0;
            reader = new BufferedReader(new FileReader(inpFile));
            writer = new BufferedWriter(new FileWriter(outFile));
            String line = reader.readLine(); // Read first line of inpFile (inp.txt)

            while (line != null) {

                if (i != binString.length()) {
                    // For each digit in the binary string (name in binary), append spaces to the end of each line
                    // '0' digit --> ONE space
                    // '1' digit --> TWO spaces

                    char c = binString.charAt(i);

                    line += c == '0' ? " " : "  "; // Add secret encoding to end of current line
                    i++;
                }


                writer.write(line); // Write current line to output file
                System.out.println(line); // Print current line to console
                writer.newLine(); // Point to next line
                line = reader.readLine(); // Read next line
            }

            reader.close();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    static void retrieve(String outFile) {
        BufferedReader reader;
	
        try {
            boolean isFinished = false;
            String binString = "";
            reader = new BufferedReader(new FileReader(outFile));
            String line = reader.readLine(); // Read first line of outFile (out.txt)

            while (line != null & !isFinished) {

                if (line.charAt(line.length() - 1) != ' ') {
                    // Loop finishes early once message has been extracted (no more modified lines)

                    isFinished = true;
                } else {

                    if (line.charAt(line.length() - 1) == ' ') {
                        if (line.charAt(line.length() - 2) == ' ') {
                            // Case: <line><space><space>

                            binString += "1";
                        } else {
                            // Case: <line><space>

                            binString += "0";
                        }
                    }

                    line = reader.readLine(); // Read next line
                }
            }

            reader.close();
            System.out.println("Binary String Extracted From \"" + outFile + "\" Using ONE Bit Per Line Extraction: " + binString);

            String encodedText = "";

            for (int i = 0; i < binString.length(); i += 8) {
                // Converting binary string back to ASCII for reference

                String binaryChunk = binString.substring(i, i+8);
                int asciiCode = Integer.parseInt(binaryChunk, 2);
                char c = (char) asciiCode;
                encodedText += c;
            }

            System.out.println("Original Encoded Text: " + encodedText);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void hide2(String inpFile, String outFile, String binString) {
        BufferedReader reader;
        BufferedWriter writer;

        try {
            int i = 0;
            reader = new BufferedReader(new FileReader(inpFile));
            writer = new BufferedWriter(new FileWriter(outFile));
            String line = reader.readLine(); // Read first line of inpFile (inp.txt)

            while (line != null) {

                if (i != binString.length()) {
                    // For each TWO digits in the binary string (name in binary), append spaces AND tabs to the end of each line
                    // 1st digit '0' --> ONE space
                    // 1st digit '1' --> TWO spaces
                    // 2nd digit '0' --> ONE tab
                    // 2nd digit '1' --> TWO tabs

                    char c1 = binString.charAt(i);
                    char c2 = binString.charAt(i+1);

                    line += c1 == '0' ? " " : "  "; // Odd digits
                    line += c2 == '0' ? "\t" : "\t\t"; // Even digits
                    i+=2;
                }

                writer.write(line); // Write current line to output file
                System.out.println(line); // Print current line to console
                writer.newLine(); // Point to next line
                line = reader.readLine(); // Read next line
            }

            reader.close();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static void retrieve2(String outFile) {
        BufferedReader reader;

        try {
            boolean isFinished = false;
            String binString = "";
            reader = new BufferedReader(new FileReader(outFile));
            String line = reader.readLine();

            while (line != null & !isFinished) {

                if (line.charAt(line.length() - 1) != '\t') {
                    // Loop finishes early once message has been extracted (no more modified lines)

                    isFinished = true;
                } else {

                    // Each modified line will ALWAYS have AT LEAST 2 characters
                    char last = line.charAt(line.length() - 1);
                    char secLast = line.charAt(line.length() - 2);


                    switch (line.length()) {
                        case 2:
                            // Only ONE possible scenario:

                            // Case: \n<space><tab>
                            binString += "00";
                            break;
                        case 3:
                            // Only TWO possible scenarios:

                            if (secLast == '\t') {
                                // Case: \n<space><tab><tab>
                                binString += "01";
                            } else {
                                // Case: \n<space><space><tab>
                                binString += "10";
                            }
                            break;
                        case 4:
                            // Only ONE possible scenario:

                            // Case: \n<space><space><tab><tab>
                            binString += "11";
                            break;
                        default:
                            // All OTHER scenarios:
                            // (NOT a new line)

                            char thrLast = line.charAt(line.length() - 3);
                            char frthLast = line.charAt(line.length() - 4);

                            if (last == '\t' && secLast == '\t' && thrLast == ' ' && frthLast == ' ') {
                                // Case: <LINE><space><space><tab><tab>

                                binString += "11";
                            } else if (last == '\t' && secLast == '\t') {
                                // Case: <LINE><space><tab><tab>

                                binString += "01";
                            } else if (secLast == ' ' && thrLast == ' ') {
                                // Case: <LINE><space><space><tab>

                                binString += "10";
                            } else {
                                // Case: <LINE><space><tab>

                                binString += "00";
                            }
                            break;
                    }

                    line = reader.readLine(); // Read next line
                }
            }

            reader.close();
            System.out.println("Binary String Extracted From \"" + outFile + "\" Using TWO Bit Per Line Extraction: " + binString);

            String encodedText = "";

            for (int i = 0; i < binString.length(); i += 8) {
                // Converting binary string back to ASCII for reference

                String binaryChunk = binString.substring(i, i+8);
                int asciiCode = Integer.parseInt(binaryChunk, 2);
                char c = (char) asciiCode;
                encodedText += c;
            }

            System.out.println("Original Encoded Text: " + encodedText);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
