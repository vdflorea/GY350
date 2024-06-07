package ct255;
import java.util.*;

/**
 *
 * @author Michael Schukat / Vlad Florea
 */

public class CT255_HashFunction1 {

    public static void main(String[] args) {
        int res = 0; // Declaring result of Hash function here

        if (args != null && args.length > 0) { // Ensuring that command line arguments are present when executing the code
            res = hashF1(args[0]); // Executing the Hash algorithm and assigning it to the result variable
            if (res < 0) {
                // Error output if result is negative

                System.out.println("Error: <input> must be 1 to 64 characters long.");
            }
            else {
                // Prints out the command line input as well as the resulting Hash value for that input
                System.out.println("input = " + args[0] + " : Hash = " + res);

                // Code to look for a hash collisions
                // H(Bamb0) -> 1079524045
                System.out.println("Start searching for collisions");

                boolean isMethod1 = false; // Set true/false depending on which method you want to use
                boolean isHashF1 = true; // Set true/false depending on which algorithm you want to use
                int numCollisions = 0;
                int testNum = 0;
                int resHash = 0;
                int targetHash = 1079524045;

                // Method 1
                // Finds a single collision at first and then many after 5 minutes
                if (isMethod1) {
                    while (numCollisions != 10) {
                        String numString = Integer.toString(testNum); // Turns an integer into a string

                        if (isHashF1) {
                            resHash = hashF1(numString); // Hashing the (integer) string
                        } else {
                            resHash = hashF2(numString); // Hashing the (integer) string (enhanced algorithm)
                        }

                        if (resHash == targetHash) {
                            // Executes once the resulting hash matches the hash for "Bamb0"

                            numCollisions++;
                            System.out.println("Hash Collision detected! H(Bamb0) = H(" + numString + ")");
                        }
                        testNum++; // Increment the integer so that we will have a 'new' string on the next iteration
                    }
                }

                // Method 2
                // Quickly finds 10 collisions
                while (numCollisions != 10) {
                    String randomString = generateString(new Random(), 64); // Generates a random string of length 64

                    if (isHashF1) {
                        resHash = hashF1(randomString); // Hashing the random string
                    } else {
                        resHash = hashF2(randomString); // Hashing the random string (enhanced algorithm)
                    }

                    if (resHash == targetHash) {
                        // Executes once the resulting hash matches the hash for "Bamb0"

                        numCollisions++;
                        System.out.println("Hash Collision detected! H(Bamb0) = H(" +randomString+ ")");
                    }
                }
            }
        }
        else {
            // Error output if no command line arguments are present
            System.out.println("Use: CT255_HashFunction1 <Input>");
        }
    }

    private static String generateString (Random random, int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"; // String containing upper/lowercase alphabet and digits 0-9
        int charStringLength = characters.length(); // Storing the length of the characters string above in a variable
        char[] text = new char[length]; // Array of characters of input length
        for (int i = 0; i < length; i++) {
            // Building up the text array with random characters for each element
            // Random characters are selected from the pool of characters/digits within the characters string

            int randomIndex = random.nextInt(charStringLength); // Selects an index between 0 and the length of the characters string
            text[i] = characters.charAt(randomIndex); // Uses the random index to select a single character at that index in the characters string

        }
        return new String(text); // Converts the character array into a string and returns it
    }

    private static int hashF1(String s){

        int ret = -1, i;
        int[] hashA = new int[]{1, 1, 1, 1}; // Declaring an integer array with 4 elements

        String filler, sIn;

        filler = new String("ABCDEFGHABCDEFGHABCDEFGHABCDEFGHABCDEFGHABCDEFGHABCDEFGHABCDEFGH"); // Creating a "filler" string to fill rest of all 64 characters of the hashing string

        if ((s.length() > 64) || (s.length() < 1)) {
            // Command line argument string has an invalid number of characters for this algorithm
            ret = -1;
        }
        else {
            sIn = s + filler; // Concatenate command line string with the "filler" string created above (filling in the empty spaces -> 64)
            sIn = sIn.substring(0, 64); // Extract first 64 characters of the long concatenated string

            for (i = 0; i < sIn.length(); i++){
                // For each character in our input string, we multiply the ASCII value by X (a prime number value) and then add the result to the 4 element positions in our integer array

                char byPos = sIn.charAt(i); // get i'th character
                hashA[0] += (byPos * 17); // X = 17
                hashA[1] += (byPos * 31); // X = 31
                hashA[2] += (byPos * 101); // X = 101
                hashA[3] += (byPos * 79); // X = 79
            }

            // By this point, we will likely have very large values for each element in the integer array
            // We take each of the 4 element's values modulo 255
            // Therefore each of the values for each of the four elements will be between 0 and 255
            // --> This will reduce the hash value to a specific range, limiting the number of unique hash values (less potential collisions)
            hashA[0] %= 255;
            hashA[1] %= 255;
            hashA[2] %= 255;
            hashA[3] %= 255;

            // Hash values are combined to create the final hash value
            ret = hashA[0] + (hashA[1] * 256) + (hashA[2] * 256 * 256) + (hashA[3] * 256 * 256 * 256); // Each element is multiplied by 256^(element position)

            if (ret < 0) ret *= -1; // Turns a negative integer result into positive
        }
        return ret;
    }

    private static int hashF2(String s){
        // An enhanced version of hashF1:
        // -> Each character in the input string is now multiplied by a LARGE prime number value Y
        // -> Larger prime numbers will create a larger space of potential hash values

        int ret = -1, i;
        int[] hashA = new int[]{1, 1, 1, 1}; // Declaring an integer array with 4 elements

        String filler, sIn;

        filler = new String("ABCDEFGHABCDEFGHABCDEFGHABCDEFGHABCDEFGHABCDEFGHABCDEFGHABCDEFGH"); // Creating a "filler" string to fill rest of all 64 characters of the hashing string

        if ((s.length() > 64) || (s.length() < 1)) {
            // Command line argument string has an invalid number of characters for this algorithm
            ret = -1;
        }
        else {
            sIn = s + filler; // Concatenate command line string with the "filler" string created above (filling in the empty spaces -> 64)
            sIn = sIn.substring(0, 64); // Extract first 64 characters of the long concatenated string

            for (i = 0; i < sIn.length(); i++){
                // For each character in our input string, we multiply the ASCII value by Y (a LARGE prime number value) and then add the result to the 4 element positions in our integer array

                char byPos = sIn.charAt(i); // get i'th character
                hashA[0] += (byPos * 21169); // Y = 21169
                hashA[1] += (byPos * 22189); // Y = 22189
                hashA[2] += (byPos * 28813); // Y = 28813
                hashA[3] += (byPos * 37633); // Y = 37633
            }

            // By this point, we will likely have very large values for each element in the integer array
            // We take each of the 4 element's values modulo 255
            // Therefore each of the values for each of the four elements will be between 0 and 255
            // --> This will reduce the hash value to a specific range, limiting the number of unique hash values (less potential collisions)
            hashA[0] %= 255;
            hashA[1] %= 255;
            hashA[2] %= 255;
            hashA[3] %= 255;

            // Hash values are combined to create the final hash value
            ret = hashA[0] + (hashA[1] * 256) + (hashA[2] * 256 * 256) + (hashA[3] * 256 * 256 * 256); // Each element is multiplied by 256^(element position)
            if (ret < 0) ret *= -1; // Turns a negative integer result into positive
        }
        return ret;
    }
}

