// Vlad Florea
// 22409144

import java.util.function.Function;

public class Palindrome_Analysis {

    private static int currNumIterations = 100000;
    private static final int TOTAL_ITERATIONS = 1000000;

    private static int numDoublePalindromes = 0;
    private static int numOperationsReversePC = 0;
    private static int numOperationsIterativePC = 0;
    private static int numOperationsStructuredPC = 0;
    private static int numOperationsRecursivePC = 0;

    public static void main (String[] args) {

        // Store each palindrome check method as a Function reference
        Function<String, Boolean> reversePalindromeCheck = Palindrome_Analysis::reversePalindromeCheck;
        Function<String, Boolean> iterativePalindromeCheck = Palindrome_Analysis::iterativePalindromeCheck;
        Function <String, Boolean> structuredPalindromeCheck = Palindrome_Analysis::structuredPalindromeCheck;
        Function <String, Boolean> recursivePalindromeCheck = Palindrome_Analysis::recursivePalindromeCheck;

        // Will run 10x from currNumIterations up to TOTAL_ITERATIONS
        while (currNumIterations <= TOTAL_ITERATIONS) {
            // Print out current iteration
            System.out.println("**************************************** Iteration (" +(currNumIterations/100000)+ ") ****************************************");
            System.out.println("Number of Iterations: " + currNumIterations + "\n");

            // Test each algorithm by sending Function reference to "testAlgorithm" method
            // --> Ensure each algorithm is working correctly if each get the same number of double palindromes
            long reversePalindromeCheckTime = testAlgorithm(reversePalindromeCheck);
            int numDoublePalindromesReversePC = numDoublePalindromes;
            long iterativePalindromeCheckTime = testAlgorithm(iterativePalindromeCheck);
            int numDoublePalindromesIterativePC = numDoublePalindromes;
            long structuredPalindromeCheckTime = testAlgorithm(structuredPalindromeCheck);
            int numDoublePalindromesStructuredPC = numDoublePalindromes;
            long recursivePalindromeCheckTime = testAlgorithm(recursivePalindromeCheck);
            int numDoublePalindromesRecursivePC = numDoublePalindromes;

            // Print out each Algorithm's time and numOperations
            printAlgorithmStats("Reverse", reversePalindromeCheckTime, numOperationsReversePC, numDoublePalindromesReversePC);
            printAlgorithmStats("Iterative", iterativePalindromeCheckTime, numOperationsIterativePC, numDoublePalindromesIterativePC);
            printAlgorithmStats("Structured", structuredPalindromeCheckTime, numOperationsStructuredPC, numDoublePalindromesStructuredPC);
            printAlgorithmStats("Recursive", recursivePalindromeCheckTime, numOperationsRecursivePC, numDoublePalindromesRecursivePC);

            currNumIterations += TOTAL_ITERATIONS / 10;
        }

    }

    public static long testAlgorithm(Function<String, Boolean> palindromeCheck) {
        numDoublePalindromes = 0; // Reset for each new algorithm passed in

        long startTime = System.currentTimeMillis(); // Start the timer

        for (int i = 0; i <= currNumIterations; i++) {
            String numString = Integer.toString(i); // Convert Integer to String
            String binaryString = decimalToBinary(numString); // Convert Decimal String to Binary String

            // Test decimal number and its binary representation with algorithm passed in
            boolean isPalindrome = palindromeCheck.apply(numString);
            boolean isBinaryPalindrome = palindromeCheck.apply(binaryString);

            // Track cases in which both decimal number and its binary representation are palindromes
            numDoublePalindromes += isPalindrome && isBinaryPalindrome ? 1 : 0;
        }

        return System.currentTimeMillis() - startTime; // Return total time elapsed
    }

    public static void printAlgorithmStats(String name, long time, int numOperations, int numDoublePalindromes) {
        System.out.println("******************** " +name+ " Palindrome Check ********************");
        System.out.println("Algorithm Time: " +time+ "ms");
        System.out.println("Number of Operations: " + numOperations);
        System.out.println("No. Double Palindromes: " +numDoublePalindromes);
        System.out.println();
    }

    public static boolean reversePalindromeCheck(String s) {
        String sReverse = "";
        numOperationsReversePC++; // Instantiate "sReverse"

        for (int i = s.length()-1; i >= 0; i--) {
            numOperationsReversePC++; // Each iteration of loop
            sReverse += s.charAt(i);
            numOperationsReversePC++; // Each character concatenation onto "sReverse"
        }
        numOperationsReversePC++; // Final check of loop

        numOperationsReversePC++; // Return statement
        return s.equals(sReverse);
    }

    public static boolean iterativePalindromeCheck(String s) {
        int j = s.length() - 1; // Last element
        numOperationsIterativePC++; // Instantiate "j"

        // Check for match by comparing first to last, second to second last etc.
        // --> Stop loop early if mismatch found OR if i >= j
        for (int i = 0; i < s.length() && i < j; i++) {
            numOperationsIterativePC++; // Each iteration of loop

            if (s.charAt(i) != s.charAt(j)) {
                numOperationsIterativePC += 2; // Return statement (once) AND last character comparison check
                return false;
            }
            numOperationsIterativePC++; // Each character comparison check

            j--;
            numOperationsIterativePC++; // Each time "j" is incremented
        }
        numOperationsIterativePC++; // Final check of loop

        numOperationsIterativePC++; // Return statement
        return true;
    }

    public static boolean structuredPalindromeCheck(String s) {
        boolean isInvalid = false;
        ArrayStack stack = new ArrayStack();
        ArrayQueue queue = new ArrayQueue();
        numOperationsStructuredPC += 3; // Instantiate variables/objects

        // Add characters to Stack and Queue
        for (int i = 0; i < s.length(); i++) {
            numOperationsStructuredPC++; // Each iteration of loop

            stack.push(s.charAt(i));
            queue.enqueue(s.charAt(i));
            numOperationsStructuredPC += 2; // Push/enqueue each character of "s"
        }
        numOperationsStructuredPC++; // Final check of loop

        while (!stack.isEmpty() && !queue.isEmpty() && !isInvalid) {
            numOperationsStructuredPC++; // Each iteration of loop

            Character stackChar = (Character) stack.pop();
            Character queueChar = (Character) queue.dequeue();

            numOperationsStructuredPC += stack.returnNumOperations();
            numOperationsStructuredPC += queue.returnNumOperations();

            if (stackChar != queueChar) {
                numOperationsStructuredPC += 2; // Return statement (once) AND last character comparison check
                return  false;
            }
            numOperationsStructuredPC++; // Each character comparison check
        }
        numOperationsStructuredPC++; // Final check of loop

        numOperationsStructuredPC++; // Return statement
        return true;
    }

    public static boolean recursivePalindromeCheck(String s) {
        String sReverse = recursiveReverseString(s);
        numOperationsRecursivePC++; // Instantiate "sReverse"

        numOperationsRecursivePC++; // Return statement
        return s.equals(sReverse);
    }

    public static String recursiveReverseString(String s) {
        // Reduce length of String recursively until no characters left
        // Work backwards, building return String recursively

        numOperationsRecursivePC++; // Each String length check
        if (s.length() == 0) {
            numOperationsRecursivePC++; // Return statement (once)
            return "";
        } else {
            numOperationsRecursivePC++; // Each recursive return statement
            return s.charAt(s.length() -  1) + recursiveReverseString(s.substring(0, s.length() -  1));
        }
    }

    public static String decimalToBinary(String s) {
        int decimalNum = Integer.parseInt(s); // Convert String to Integer
        String binaryString = "";

        while (decimalNum > 0) {
            int remainder = decimalNum % 2; // Remainder can either be a 1 or a 0
            binaryString += remainder; // Concatenate remainder onto binary String
            decimalNum /= 2; // Divide decimal number by two
        }

        return  binaryString;
    }
}
