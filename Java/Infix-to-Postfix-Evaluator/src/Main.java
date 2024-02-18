// Vlad Florea
// ID: 22409144

import javax.swing.*;

public class Main {

    public static int bracketSemaphore = 0; // Increment when opening a bracket, decrement when closing a bracket

    public static void main(String[] args) {

        String input = "";

        // Keep looping until a valid Infix expression is provided
        do {

            input = JOptionPane.showInputDialog("Enter an Infix expression between 3 and 20 characters. " +
                    "\nValid Characters: [0-9] AND +, -, *, /, ^, (, )");

            if (input == null) { // If user cancels input box
                return;
            }

        } while (!isValid(input));

        // Convert Infix expression to Postfix notation equivalent
        String postfixOutput = makePostfix(input);

        // Evaluate Postfix expression
        double postfixResult = evaluatePostfix(postfixOutput);

        // Build a message string and display outputs to the user
        String message = "Infix Expression: " + input + "\n" +
                         "Postfix Expression: " + postfixOutput + "\n" +
                         "Output: " + postfixResult;
        JOptionPane.showMessageDialog(null, message);
    }

    /****************************** Main Program Methods ******************************/

    public static boolean isValid(String input) {

        // Must be TRUE if expression is valid
        boolean hasOperator = false;
        boolean hasDigit = false;

        // Must be FALSE if expression is valid
        boolean hasInvalidChar = false;

        input = input.replace(" ", ""); // Remove all spaces from input

        // Check if expression is of valid length
        if (input.length() > 20 || input.length() < 3) {
            JOptionPane.showMessageDialog(null, "ERROR: Expression length is invalid. Please try again.");
            return false;
        }

        // Check if expression has valid characters
        for (int i = 0; i < input.length() && !hasInvalidChar; i++) {
            char currChar = input.charAt(i);

            // Check if the CURRENT character is an operator
            if (isOperator(currChar)) {
                if (!hasOperator) { // Set once

                    // CHECK: Expression has at least one operator
                    hasOperator = true;
                }

            // Check if the CURRENT character is a digit
            } else if (Character.isDigit(currChar)) {
                if (!hasDigit) { // Set once

                    // CHECK: Expression has at least one digit
                    hasDigit = true;
                }

            // FINAL CHECK: If also not a bracket, character is invalid
            } else if (!isBracket(currChar)) {
                hasInvalidChar = true;
            }

        }

        // Check if expression has an invalid character
        if (hasInvalidChar) {
            JOptionPane.showMessageDialog(null, "ERROR: Expression contains invalid character(s). Please try again.");
            return false;
        }

        // Checked in loop above (Open brackets are present)
        if (bracketSemaphore != 0) {
            JOptionPane.showMessageDialog(null, "ERROR: Expression contains open bracket(s). Please try again.");
            return false;
        }

        // Check if expression has BOTH a digit and an operator
        if (hasDigit && hasOperator) {

            // Check if expression is in INFIX notation (And a few other checks)
            // --> Cannot start with an operator (unless it is a LEFT bracket)
            // --> Cannot have two operators in a row
            // --> Must have number on both sides of an operator
            // --> Cannot have two digits in a row (single digits only)

            for (int i = 1; i < input.length(); i++) {
                // Comparing each character with the previous character

                char prevChar = input.charAt(i-1); // Check previous character
                char currChar = input.charAt(i);
                char nextChar = i != input.length()-1 ? input.charAt(i+1) : 0; // Ensure NullPointerException is not thrown

                // Checks if expression starts with an operator
                if (i == 1) {
                    if (isOperator(prevChar)) {
                        JOptionPane.showMessageDialog(null, "ERROR: Expression is not infix (Cannot start with operator). Please try again.");
                        return false;
                    }
                }

                // Checks if expression contains two operators in a row
                if (isOperator(prevChar) && isOperator(currChar)) {
                    JOptionPane.showMessageDialog(null, "ERROR: Expression is not infix (Cannot contain two operators in a row). Please try again.");
                    return false;
                }

                // Checks if expression contains two digits in a row
                if (Character.isDigit(prevChar) && Character.isDigit(currChar)) {
                    JOptionPane.showMessageDialog(null, "ERROR: Expression contains (a) double digit number(s). Please try again.");
                    return false;
                }

                // Checks whether there is either a bracket OR a digit on BOTH sides of the operator
                if (isOperator(currChar)) {
                    if (!(Character.isDigit(prevChar) || isBracket(prevChar)) && !(Character.isDigit(nextChar) || isBracket(nextChar))) {
                        JOptionPane.showMessageDialog(null, "ERROR: Expression is not infix (Invalid usage of operator(s)). Please try again.");
                        return false;
                    }
                }
            }

            return true; // Valid infix expression

        } else {
            JOptionPane.showMessageDialog(null, "ERROR: Expression must contain (at least) one operator AND one digit. Please try again.");
            return false;
        }
    }

    public static String makePostfix(String input) {

        input = input.replace(" ", ""); // Remove all spaces from input

        char[] inputArray = input.toCharArray(); // Convert string to Character array
        String output = "";
        ArrayStack stack = new ArrayStack(20); // Maximum of 20 characters

        System.out.println("\n----------Infix to Postfix----------");
        System.out.println("INFIX INPUT: " + input);

        for (int i = 0; i < inputArray.length; i++) {
            Character currChar = inputArray[i];

            // Append all operands to output string
            if (Character.isDigit(currChar)) {
                output += currChar;
            } else {

                // Push to Stack IF:
                // --> Stack is empty
                // OR --> Precedence of current scanned Character is HIGHER than the precedence of Character at TOP of the Stack
                // OR --> Top of Stack contains an opening (left) bracket
                if (stack.isEmpty()) {
                    stack.push(currChar);
                } else if (getPrecedence(currChar) > getPrecedence((Character) stack.top()) || (Character)stack.top() == '(') {
                    stack.push(currChar);
                } else {

                    // Append to Output String IF:
                    // --> Precedence of Character at TOP of the stack is greater than or equal to the precedence of current scanned Character
                    // AND --> Character at TOP of stack is NOT a bracket
                    // AND --> Current scanned Character is NOT a bracket (Extra condition implemented after testing)
                    while (getPrecedence((Character) stack.top()) >= getPrecedence(currChar) && !isBracket((Character) stack.top()) && !isBracket(currChar)) { // may need to test again

                        output += stack.pop();

                        // In case Stack is now empty after popping
                        if (stack.isEmpty()) {
                            // Otherwise, stack.top() will be called again in loop condition
                            // --> Will return message dialog saying Stack is empty

                            break;
                        }
                    }

                    // Append all operators between bracket pair to Output String
                    if (currChar == ')') {

                        // Pop until an opening (left) bracket is encountered
                        while ((Character)stack.top() != '(') {
                            output += stack.pop();
                        }

                        // Discard closing (right) bracket on Stack
                        stack.pop();

                    } else {
                        stack.push(currChar); // Push OPENING (left) brackets OR any other operator
                    }
                }
            }

            // Display outputs of each iteration
            System.out.println("\n-----Iteration (" + (i+1) + ")----- " +
                    "\n(Current Character) -> " +currChar +
                    "\n(Current Output) -> " + output +
                    "\n(Stack : RIGHT-MOST = TOP) -> " + stack.tempPrintStack());

        }

        // Append rest of Stack content onto Output String (excluding brackets)
        while (!stack.isEmpty()) {
            if (!isBracket((Character) stack.top())) {
                output += stack.pop();
            } else {
                stack.pop();
            }
        }

        System.out.println("\nPOSTFIX OUTPUT: " + output);

        return output;

    }

    public static double evaluatePostfix(String expression) {

        ArrayStack stack = new ArrayStack(20); // Maximum of 20 characters
        char[] inputArray = expression.toCharArray(); // Convert string to Character array

        System.out.println("\n----------Postfix Expression Evaluation----------");

        for (int i = 0; i < inputArray.length; i++) {
            Character currChar = inputArray[i];
            String currCalculation = "N/A"; // Applicable only when current scanned Character is an OPERATOR

            if (Character.isDigit(currChar)) {
                double currDigit = currChar - '0'; // Convert 'Character' type to Double
                stack.push(currDigit); // Push Double to stack
            } else { // Character is an OPERATOR

                // Cast generic 'Object' to Double
                double operand1 = (Double) stack.pop();
                double operand2 = (Double) stack.pop();

                currCalculation = String.format("---- %f %c %f ----", operand2, currChar, operand1);

                // Case for each possible OPERATOR (Push to Stack after calculation)
                switch(currChar) {
                    case '^':
                        stack.push(Math.pow(operand2, operand1));
                        break;
                    case '*':
                        stack.push(operand2 * operand1);
                        break;
                    case '/':
                        stack.push(operand2 / operand1);
                        break;
                    case '+':
                        stack.push(operand2 + operand1);
                        break;
                    case '-':
                        stack.push(operand2 - operand1);
                        break;
                }
            }

            // Display outputs of each iteration
            System.out.println("\n-----Iteration (" + (i+1) + ")----- " +
                    "\n(Current Character) -> " +currChar +
                    "\n(Current Calculation) -> " + currCalculation +
                    "\n(Stack : RIGHT-MOST = TOP) -> " + stack.tempPrintStack());
        }
        return (Double)stack.pop(); // Return last value on stack
    }

    /****************************** Utility Methods ******************************/

    public static boolean isOperator(Character c) {

        switch (c) {
            case '+', '-', '*', '/', '^':
               return true;
        }

        return false;
    }

    public static boolean isBracket(Character c) {

        switch (c) {
            case '(':
                bracketSemaphore++; // Open bracket
                return true;
            case ')':
                bracketSemaphore--; // Close bracket
                return true;
        }

        return false;
    }

    public static int getPrecedence(Character operator) {

        switch (operator) {
            case '^':
                return 3;
            case '*', '/':
                return 2;
            case '+', '-':
                return 1;
        }

        return 0;
    }

}
