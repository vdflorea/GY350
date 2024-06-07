// Vlad Florea
// 22409144

package topic5;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class GuessingGame {
    public static void main(String[] args) {
        BinaryTree<String> tree = new BinaryTree<String>(); // Initialise binary tree

        String loadTreeDecision = "";
        Scanner s = new Scanner(System.in);
        do {
            System.out.println("Would you like to load your own decision tree?");
            System.out.print("Yes / No > ");
            loadTreeDecision = s.nextLine();
            loadTreeDecision = loadTreeDecision.toLowerCase();

            if (!isValid(loadTreeDecision)) {
                System.out.println("\nInvalid Input. Enter: \"Yes\" OR \"No\"\n");
            }
        } while (!isValid(loadTreeDecision));

        if (loadTreeDecision.equals("yes") || loadTreeDecision.equals("y")) {
            tree = new BinaryTree<>();
            tree.setRootNode(loadTree());
            if (tree.getRootNode() == null) {
                System.out.println("ERROR: Could not load saved tree file. Starting game with default game tree ...");
                createDefaultTree(tree);
            }
        } else {
            System.out.println("Constructing initial game tree ...");
            createDefaultTree(tree);
        }

        while (true) {
            // Initially same value for previous/current nodes
            BinaryNodeInterface<String> currentNode = tree.getRootNode();
            BinaryNodeInterface<String> previousNode = tree.getRootNode(); // Used later to enhance user experience

            displayTreeStats(tree);

            // Prints the tree nodes (questions/guesses) in a visually appealing diagram to the console
            System.out.println("********** Complete Tree Diagram **********");
            printTreeDiagram(tree);

            System.out.println("\n********** Guessing Game **********");

            // Stop when we reach a guessing (leaf) node
            while (! currentNode.isLeaf()) {
                String currQuestionAnswer = ""; // Yes/No

                do {
                    System.out.println(currentNode.getData()); // Print out QUESTION data
                    System.out.print("Yes / No > ");
                    currQuestionAnswer = s.nextLine();
                    currQuestionAnswer = currQuestionAnswer.toLowerCase();

                    if (!isValid(currQuestionAnswer)) {
                        System.out.println("\nInvalid Input. Enter: \"Yes\" OR \"No\"\n");
                    }
                } while (!isValid(currQuestionAnswer));

                previousNode = currentNode;

                if (currQuestionAnswer.equals("yes") || currQuestionAnswer.equals("y")) {
                    currentNode = previousNode.getLeftChild();
                } else {
                    currentNode = previousNode.getRightChild();
                }
            }

            String guessedCorrectly = "";

            do {
                System.out.println(currentNode.getData()); // Print out GUESS data
                System.out.print("Yes / No > ");
                guessedCorrectly = s.nextLine();
                guessedCorrectly = guessedCorrectly.toLowerCase();

                if (!isValid(guessedCorrectly)) {
                    System.out.println("\nInvalid Input. Enter: \"Yes\" OR \"No\"\n");
                }
            } while (!isValid(guessedCorrectly));

            if (guessedCorrectly.equals("yes") || guessedCorrectly.equals("y")) {
                String option = "";
                boolean isValid = false;

                System.out.println("\n********** Game Over **********");
                System.out.println("Great! I knew what you were thinking after all!");

                do {
                    System.out.println("Please enter an option '1', '2', '3' or '4':");
                    System.out.println("(1) Play again");
                    System.out.println("(2) Save the tree");
                    System.out.println("(3) Load another tree");
                    System.out.println("(4) Quit");
                    System.out.println();
                    System.out.print("1 / 2 / 3 / 4 > ");
                    option = s.nextLine();

                    if (option.equals("1") || option.equals("2") || option.equals("3") || option.equals("4")) {
                        isValid = true;
                    } else {
                        System.out.println("\nInvalid Input. Enter: \"1\" OR \"2\" OR \"3\" OR \"4\"\n");
                    }
                } while (!isValid);
                System.out.println();

                switch (option) {
                    case "1": // Play again
                        continue; // Loop back to start
                    case "2": // Save the tree
                        saveTree(tree);
                        break;
                    case "3": // Load another tree
                        tree = new BinaryTree<>();
                        tree.setRootNode(loadTree());
                        break;
                    case "4":
                        return;
                }
            } else {
                // User must enter:
                // --> Correct answer (child) leaf node
                // --> New distinguishing question (parent) node
                // --> Whether correct answer is a left/right child node of distinguishing question (parent) node

                // Current leaf node replaced by one of two potential subtrees:
                //        userQuestion
                //       /            \
                // userAnswer    initialGuess
                //
                //             OR:
                //
                //        userQuestion
                //       /            \
                // initialGuess    userAnswer

                // Variables to store user answers
                String userAnswer = "";
                boolean isAnswerValid = false;

                String userQuestion = "";
                boolean isQuestionValid = false;

                String userAnswerType = ""; // Yes or No

                System.out.println("\n********** Expand Tree **********");
                System.out.println("I have no idea what you are thinking!");
                System.out.println("Let's expand my knowledge. Enter the following:");
                System.out.println("(1) What you were thinking about (YOUR answer)");
                System.out.println("(2) A new question that will follow the previous question AND will distinguish MY guess from YOUR answer");
                System.out.println("(3) Whether \"Yes\" OR \"No\" must be answered to the distinguishing question to arrive at YOUR answer");
                System.out.println();

                // (1) Validate user's answer
                do {
                    System.out.println("(1) Enter your answer (what you were thinking about):");
                    System.out.print("> ");
                    userAnswer = s.nextLine();

                    // Validate user's input
                    if (userAnswer.isEmpty()) {
                        System.out.println("\nInvalid Input. Answer must not be empty\n");
                    } else {
                        isAnswerValid = true;
                    }
                } while (!isAnswerValid);
                userAnswer = "GUESS: " + userAnswer + "?"; // Take on "GUESS" format
                System.out.println();

                // (2) Validate user's distinguishing question (parent) node
                do {
                    System.out.println("(2) Enter a new distinguishing question that will be asked AFTER the previous question:");
                    System.out.println("PREVIOUS " + previousNode.getData());
                    System.out.println("NOTE: Questions must end with a '?'");
                    System.out.print("> ");
                    userQuestion = s.nextLine();

                    // Validate user's input
                    if (userQuestion.isEmpty()) {
                        System.out.println("\nInvalid Input. Question must not be empty\n");
                    } else if (userQuestion.charAt(userQuestion.length() - 1) == '?') {
                        isQuestionValid = true;
                    } else {
                        System.out.println("\nInvalid Input. Question must end with a '?'\n");
                    }
                } while(!isQuestionValid);
                userQuestion = "QUESTION: " + userQuestion; // Take on "QUESTION" format
                System.out.println();

                // (3) Validate whether user's answer is a "Yes" or "No" answer to distinguishing question
                do {
                    System.out.println("(3) Enter whether YOUR answer is a \"Yes\" OR \"No\" answer to the distinguishing question");
                    System.out.println("YOUR " + userQuestion);
                    System.out.println("YOUR " +userAnswer);
                    System.out.print("Yes / No> ");
                    userAnswerType = s.nextLine();
                    userAnswerType = userAnswerType.toLowerCase();

                    // Validate user's input
                    if (!isValid(userAnswerType)) {
                        System.out.println("\nInvalid Input. Enter: \"Yes\" OR \"No\"\n");
                    }
                } while (!isValid(userAnswerType));
                System.out.println();

                // Build new subtree
                BinaryNodeInterface<String> leftChild;
                BinaryNodeInterface<String> rightChild;

                String initialGuess = currentNode.getData(); // First extract initial guess from current node
                currentNode.setData(userQuestion); // Set distinguishing question (parent) node

                if (userAnswerType.equals("yes") || userAnswerType.equals("y")) {
                    leftChild = new BinaryNode<>(userAnswer); // Left guess (child) node
                    rightChild = new BinaryNode<>(initialGuess); // Right guess (child) node
                } else {
                    leftChild = new BinaryNode<>(initialGuess); // Left guess (child) node
                    rightChild = new BinaryNode<>(userAnswer); // Right guess (child) node
                }

                currentNode.setLeftChild(leftChild);
                currentNode.setRightChild(rightChild);
            }
        }
    }

    public static boolean isValid(String userInput) {
        userInput = userInput.toLowerCase(); // Input should be lower-cased already in parent calling function (added here also just in case)

        if (userInput.equals("yes") || userInput.equals("no") || userInput.equals("y") || userInput.equals("n")) {
            return true;
        }
        return false;
    }
    public static void createDefaultTree(BinaryTree<String> tree) {
        // If the user does not load a tree, this will be the default initial tree
        // --> The left side this tree is 4 levels in size
        // --> The right side is 2 levels in size (root node and one guess) i.e., must be expanded upon by user

        // To create a tree, build it up from the bottom:
        // --> Create leaf nodes first
        // --> Create parent node of leaf nodes
        // --> Link leaf nodes to parent node by creating parent tree with leaf nodes as children on left/right side
        // --> Link all the way up to root node, creating larger trees with each level moved up

        // NOTE: Questions start with "QUESTION" - Guesses start with "GUESS:"
        // --> Done automatically for the user when building a tree

        System.out.println("\nCreating a tree that looks like this:\n");
        System.out.println("          A           ");
        System.out.println("        /   \\        ");
        System.out.println("       B     C        ");
        System.out.println("     /   \\           ");
        System.out.println("    D     E           ");
        System.out.println("  /  \\  /  \\        ");
        System.out.println(" H    I J    K        ");
        System.out.println();


        // < LEVEL 4 > Leaf Nodes (Guesses)
        BinaryTree<String> hTree = new BinaryTree<>("GUESS: Zebra?"); // "Yes" to parent node D
        BinaryTree<String> iTree = new BinaryTree<>("GUESS: Lion?"); // "No" to parent node D

        BinaryTree<String> jTree = new BinaryTree<>("GUESS: Eagle?"); // "Yes" to parent node E
        BinaryTree<String> kTree = new BinaryTree<>("GUESS: Crocodile?"); // "No" to parent node E

        // < LEVEL 3 > Internal Nodes
        BinaryTree<String> dTree = new BinaryTree<>("QUESTION: Does it have stripes?", hTree, iTree); // "Yes" to parent node B
        BinaryTree<String> eTree = new BinaryTree<>("QUESTION: Is it a bird?", jTree, kTree); // "No" to parent node B

        // < LEVEL 2 > Internal Nodes
        BinaryTree<String> bTree = new BinaryTree<>("QUESTION: Is it a mammal?", dTree, eTree); // "Yes" to root node
        BinaryTree<String> cTree = new BinaryTree<>("GUESS: Mercedes Benz?"); // "No" to root node

        // < LEVEL 1 > Root Node (A) (First Question)
        tree.setTree("QUESTION: Is it an animal?", bTree, cTree);
    }
    public static void displayTreeStats(BinaryTree<String> tree) {
        System.out.println("********** Tree Statistics **********");

        if (tree.isEmpty()) {
            System.out.println("The tree is empty");
            return; // No other stats to display, prevent NullPointerException
        } else {
            System.out.println("The tree is not empty");
        }

        System.out.println("Root of tree: " + tree.getRootData());
        System.out.println("Height of tree: " + tree.getHeight());
        System.out.println("No. of nodes in tree: " + tree.getNumberOfNodes());
        System.out.println();
    }

    public static void saveTree(BinaryTree<String> tree) {
        // Uses a serialisation utility method to save tree to a save file
        // NOTE: This implementation only allows for one single save file

        BinaryNodeInterface<String> root = tree.getRootNode();
        String serialisedTree = serialiseTree(root);

        // For testing
        //System.out.println(serialisedTree);

        System.out.println("\n********** Save Tree **********");

        String workingDirectory = System.getProperty("user.dir"); // Retrieve user's working directory as a String
        String fileName = workingDirectory + "\\src\\savedTree.txt"; // NOTE: Modify if using non-Windows OS

        // Write output String to save file
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(serialisedTree); // Write output string to file
            writer.close();
            System.out.println("Tree saved successfully at: " +fileName);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String serialiseTree(BinaryNodeInterface<String> root) {
        // Encode tree into a single String

        if (root == null) {
            return null;
        }
        Stack<BinaryNodeInterface<String>> stack = new Stack<>();
        stack.push(root);

        ArrayList<String> list = new ArrayList<>();
        while (!stack.isEmpty()) {
            BinaryNodeInterface<String> t = stack.pop();

            // Store a marker if current node is null
            if (t == null) {
                list.add("#");
            }
            else {
                // Store current node and its children
                list.add("" + t.getData());
                stack.push(t.getRightChild());
                stack.push(t.getLeftChild());
            }
        }
        return String.join(",", list);
    }
    public static BinaryNodeInterface<String> loadTree() {

        System.out.println("\n********** Load Tree **********");

        String workingDirectory = System.getProperty("user.dir"); // Retrieve user's working directory as a String
        String fileName = workingDirectory + "\\src\\savedTree.txt"; // NOTE: Modify if using non-Windows OS
        File file = new File(fileName);

        String in = "";

        if (file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(fileName));
                in = reader.readLine(); // Single line (serialised tree)
                reader.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("ERROR: Save file does not exist");
            return null;
        }

        BinaryNodeInterface<String> savedTree = deSerialiseTree(in);

        if (savedTree.getNumberOfNodes() > 0) {
            System.out.println("Tree loaded successfully from: " +fileName);
            System.out.println();
        }

        return savedTree;
    }

    static int i; // Declare i here to be within scope of de-serialising functions
    public static BinaryNodeInterface<String> deSerialiseTree(String serialisedTree) {

        i = 0;
        String[] nodeArray = serialisedTree.split(","); // Create an array of strings delimited by ','
        return deSerialiseTreeHelper(nodeArray); // Call recursive helper function
    }

    public static BinaryNodeInterface<String> deSerialiseTreeHelper(String[] arr) {
        // Recursively builds the tree from the array of nodes (also accounts for leaf nodes)

        // Reached end of a subtree (final leaf nodes)
        if (arr[i].equals("#")) {
            return null;
        }

        BinaryNodeInterface<String> root = new BinaryNode<>(arr[i]);
        i++;
        root.setLeftChild(deSerialiseTreeHelper(arr));
        i++;
        root.setRightChild(deSerialiseTreeHelper(arr));
        return root; // Return entire tree
    }

    /******************** PRINT OUT DECISION TREE TO CONSOLE ********************/

    public static void printTreeDiagram(BinaryTree<String> tree) {
        System.out.println(preOrderTraverse(tree.getRootNode()));
    }

    public static String preOrderTraverse(BinaryNodeInterface<String> root) {
        // Traverses tree in pre-order fashion, creating a horizontal filesystem-like diagram
        // -> Non-recursive function which applies a single behaviour to the root node ONLY
        // --> Rest of nodes are appended using separate recursive utility function
        // ----> This prevents incorrect formatting of final String

        if (root == null) {
            return "";
        }

        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(root.getData()); // First, we append root node data to String

        // Pointer characters to format horizontal diagram
        String pointerRight = "'--";
        String pointerLeft = (root.getRightChild() != null) ? "|--" : "'--";

        // Recursive helper functions used to traverse rest of tree
        preOrderTraverseHelper(sBuilder, "", pointerLeft, root.getLeftChild(), root.getRightChild() != null); // Traverse left side of the tree
        preOrderTraverseHelper(sBuilder, "", pointerRight, root.getRightChild(), false); // Traverse right side of the tree

        return sBuilder.toString(); // Output diagram String
    }

    public static void preOrderTraverseHelper(StringBuilder sBuilder, String padding, String pointer, BinaryNodeInterface<String> node, boolean hasRightSibling) {
        // Recursive function which applies the same behaviour to all other nodes EXCEPT the root node
        // --> Nodes are appended to output String to create horizontal filesystem-like diagram

        if (node != null) { // Base case
            sBuilder.append("\n");
            sBuilder.append(padding); // Empty space "   " OR Downwards pointer "│  "
            sBuilder.append(pointer); // Right pointer "└──" OR Left Pointer 1 "├──" OR Left pointer 2 "└──"
            sBuilder.append(node.getData());

            // Build a padding String for recursive calls later
            StringBuilder paddingBuilder = new StringBuilder(padding);
            if (hasRightSibling) {
                paddingBuilder.append("|  ");
            } else {
                paddingBuilder.append("   ");
            }

            String paddingBoth = paddingBuilder.toString();
            String pointerRight = "'--";
            String pointerLeft = (node.getRightChild() != null) ? "|--" : "'--"; // Different pointer depending on whether current node has a right child

            preOrderTraverseHelper(sBuilder, paddingBoth, pointerLeft, node.getLeftChild(), node.getRightChild() != null); // Traverse left side of the tree
            preOrderTraverseHelper(sBuilder, paddingBoth, pointerRight, node.getRightChild(), false); // Traverse right side of the tree
        }
    }
}
