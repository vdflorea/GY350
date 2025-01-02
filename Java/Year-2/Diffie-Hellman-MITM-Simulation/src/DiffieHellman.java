// @author Vlad Florea
// 22409144

import java.util.*;

public class DiffieHellman {
    public static void main (String[] args) {

        DiffieHellman dh = new DiffieHellman();
        dh.problem1();
        dh.problem2();

    }

    public void problem1() {
        List<Integer> primeNumbers = generatePrimeNumbers(100000, 10000);
        int randomPrime = getRandomPrimeNumber(primeNumbers);
        int primitiveRoot = findPrimitiveRoot(randomPrime);

        Random random = new Random();
        int XBob = random.nextInt(randomPrime - 2) + 1; // Generate a (private) random number between 1 and randomPrime - 1
        int XAlice = random.nextInt(randomPrime - 2) + 1; // Generate a (private) random number between 1 and randomPrime - 1

        int YBob = generateYValue(primitiveRoot, XBob, randomPrime); // Generate (public) value YBob and send to Alice
        int YAlice = generateYValue(primitiveRoot, XAlice, randomPrime); // Generate (public) value YAlice and send to Bob

        int keyBob = generatePrivateKey(YAlice, XBob, randomPrime); // Bob uses a combination of his chosen value and Alice's Y value to create the same session key
        int keyAlice = generatePrivateKey(YBob, XAlice, randomPrime); // Alice uses a combination of her chosen value and Bob's Y value to create the same session key

        System.out.println("-------------------- Part 1: Alice & Bob DH Key Exchange --------------------");
        System.out.println("Random Prime Number: " + randomPrime);
        System.out.println("Primitive Root Of " +randomPrime+ ": " + primitiveRoot);
        System.out.println();
        System.out.println("Bob X Value (Private): " +XBob+ "\t" + "Alice X Value (Private): " + XAlice);
        System.out.println("Bob Y Value (Public): " +YBob+ "\t" + "Alice Y Value (Public): " + YAlice);
        System.out.println();
        System.out.println("Bob Session Key: " +keyBob+ "\t" + "Alice Session Key: " + keyAlice);
    }

    public void problem2() {
        // Malory knows the following information as it is public:
        // --> The random prime number (randomPrime)
        // --> The primitive root of the random prime number
        // Malory finds out Bob and Alice's (public) Y values through eavesdropping:
        // --> YBob
        // --> YAlice

        List<Integer> primeNumbers = generatePrimeNumbers(100000, 10000);
        int randomPrime = getRandomPrimeNumber(primeNumbers);
        int primitiveRoot = findPrimitiveRoot(randomPrime);

        Random random = new Random();
        int XBob = random.nextInt(randomPrime - 2) + 1; // Bob generates a (private) X value (Malory will never see this)
        int XAlice = random.nextInt(randomPrime - 2) + 1; // Alice generates a (private) X value (Malory will never see this)

        int YBob = generateYValue(primitiveRoot, XBob, randomPrime); // Bob generates a (public) Y value and attempts to send this to Alice
        int YAlice = generateYValue(primitiveRoot, XAlice, randomPrime); // Alice generates a (public) Y value and attempts to send this to Bob

        int[] maloryIntercepts = { YBob, YAlice }; // Malory intercepts public Y values from Bob and Alice (eavesdropping)

        int maloryXBob = random.nextInt(randomPrime - 2) + 1; // Malory creates FABRICATED XBob value
        int maloryXAlice = random.nextInt(randomPrime - 2) + 1; // Malory creates FABRICATED XAlice value

        int maloryYBob = generateYValue(primitiveRoot, maloryXBob, randomPrime); // Malory creates FABRICATED YBob value and sends "maloryYBob" value to Alice
        int maloryYAlice = generateYValue(primitiveRoot, maloryXAlice, randomPrime); // Malory creates FABRICATED YAlice value and sends "maloryYAlice" value to Bob

        int keyBob = generatePrivateKey(maloryYAlice, XBob, randomPrime); // Bob uses a combination of his chosen value and the FABRICATED "maloryYAlice" value to create the session key
        int keyAlice = generatePrivateKey(maloryYBob, XAlice, randomPrime); // Alice uses a combination of her chosen value and the FABRICATED "maloryYBob" value to create the session key

        int keyMaloryBob = generatePrivateKey(maloryIntercepts[0], maloryXAlice, randomPrime); // Malory creates the same session key as Bob i.e. keyBob == keyMaloryBob
        int keyMaloryAlice = generatePrivateKey(maloryIntercepts[1], maloryXBob, randomPrime); // Malory creates the same session key as Alice i.e. keyAlice == keyMaloryAlice

        System.out.println("\n\n-------------------- Part 2: Man in the Middle Attack --------------------");
        System.out.println("Random Prime Number: " + randomPrime);
        System.out.println("Primitive Root Of " +randomPrime+ ": " + primitiveRoot);
        System.out.println();
        System.out.println("Bob X Value (Private): " +XBob+ "\t" + "Alice X Value (Private): " + XAlice);
        System.out.println("Bob Y Value (Public): " +YBob+ "\t" + "Alice Y Value (Public): " + YAlice);
        System.out.println();
        System.out.println("Malory enters....");
        System.out.println();
        System.out.println("Malory: I now have Bob's Y Value (" +YBob+ ") and Alice's Y Value (" +YAlice+ ")");
        System.out.println("Malory: I will send Bob and Alice fabricated Y values and they will think they have a direct connection with each other");
        System.out.println("Malory: I have chosen two random X values: One for Bob --> " +maloryXBob+ "\tOne for Alice --> " + maloryYAlice);
        System.out.println("Malory: Bob and Alice will receive the Y values for the X values that I have chosen");
        System.out.println("Malory: Bob receives --> " +maloryYAlice+ "\tAlice receives --> " + maloryYBob);
        System.out.println();
        System.out.println("Bob: I believe I have computed the same session key as Alice and therefore I believe I am speaking to her directly.");
        System.out.println("Bob Session Key --> " +keyBob);
        System.out.println("Malory: Bob has actually computed the same session key as I have. (I used his real Y value, he used the fabricated one that I sent)");
        System.out.println("Malory Session Key --> " +keyMaloryBob);
        System.out.println("-----> Malory and Bob are now securely connected <-----");
        System.out.println();
        System.out.println("Alice: I believe I have computed the same session key as Bob and therefore I believe I am speaking to him directly.");
        System.out.println("Alice Session Key --> " +keyAlice);
        System.out.println("Malory: Alice has actually computed the same session key as I have. (I used her real Y value, she used the fabricated one that I sent)");
        System.out.println("Malory Session Key --> " +keyMaloryAlice);
        System.out.println("-----> Malory and Alice are now securely connected <-----");
        System.out.println();
        System.out.println("Malory: My machine now acts as a relay between Bob and Alice so I receive their messages first before they do");
        System.out.println("Bob <----- Session Key: " +keyMaloryBob+ " -----> Malory <----- Session Key: " +keyMaloryAlice+ " -----> Alice");
    }

    public List<Integer> generatePrimeNumbers(int max, int min) {
        // Sieve of Eratosthenes algorithm

        boolean prime[] = new boolean[max + 1]; // Generate array of booleans of size 100001
        Arrays.fill(prime, true); // Set all boolean values in the array to true initially

        for (int p = 2; p * p <= max; p++) {
            // Start at p = 2 initially

            if (prime[p]) { // If the value at index p is STILL "true"
                // If the value of the current p was set to "false" previously, the if statement DOES NOT execute
                // --> Move onto the next p

                for (int i = p * 2; i <= max; i += p) {
                    // Iterate through each multiple of current p, setting the value at each p multiple up to 100000 to "false"

                    prime[i] = false;
                }
            }
        }

        List<Integer> primeNumbers = new ArrayList<>();
        for (int i = min; i <= max; i++) {
            // Generating the list of prime numbers between 10^4 and 10^5

            if (prime[i]) {
                // Prime numbers have already been generated up to 10^5 above
                // --> Iterate through boolean array and add corresponding values to primeNumbers list

                primeNumbers.add(i);
            }
        }
        return primeNumbers;
    }

    public int getRandomPrimeNumber(List<Integer> primeNumbers) {
        Random random = new Random();

        int randomIndex = random.nextInt(primeNumbers.size()); // Generate a random index between 0 and the number of prime numbers between 10^4 and 10^5
        int randomPrime = primeNumbers.get(randomIndex); // Get the prime number at the random index generated

        return randomPrime;
    }

    public static int findPrimitiveRoot(int primeNumber) {
        // NOTE: This algorithm is very quick but uses more complex mathematics to find the primitive root

        Set<Integer> factors = new HashSet<>(); // Used to store factors of the prime number that are coprime with it

        for (int i = 2; i <= primeNumber - 1; i++) { // i is the primitive root candidate
            if (gcd(i, primeNumber) == 1) {
                // If greatest common divisor between the factor i of the prime number is 1:
                // --> i is coprime to the prime number

                factors.add(i); // Add i as factor coprime to the prime number
            }
        }

        for (int i = 2; i <= primeNumber - 1; i++) {
            boolean isRoot = true; // Assume that i is a primitive root

            for (int factor : factors) {
                if (modPow(i, primeNumber / factor, primeNumber) == 1) {
                    // If i raised to the power of primeNumber / factor modulo primeNumber is 1:
                    // --> i is NOT a primitive root

                    isRoot = false;
                    break;
                }
            }
            if (isRoot) {
                // If the assumption was correct (i.e. isRoot is still true)
                // --> i is a primitive root

                return i;
            }
        }
        return -1;
    }

    public static int findPrimitiveRoot2(int primeNumber) {
        // NOTE: This algorithm is extremely slow but easier to understand

        for (int i = 2; i < primeNumber; i++) { // i is the primitive root candidate

            int k = 0; // k is the exponent to test i candidate
            ArrayList<Integer> distinctNumbers = new ArrayList<>(); // All distinct numbers of equation i^k modulo primeNumber
            boolean isDistinct = true;

            while (k != primeNumber - 2 || !isDistinct) {
                // While loop breaks out early if a non-distinct number is calculated
                // --> Otherwise IF k iterates until primeNumber - 2 then all numbers are distinct and therefore i is a primitive root

                int newNumber = modPow(i, k, primeNumber); // New number of equation i^k modulo primeNumber

                if (distinctNumbers.contains(newNumber)) {
                    // If the new number matches a distinct number in the array list then it is NOT distinct

                    isDistinct = false; // i is NOT a primitive root
                } else {
                    distinctNumbers.add(newNumber); // Add a new distinct number to array list
                    k++;
                }
            }

            if (k == primeNumber - 2) {
                // Each number is distinct for this i and therefore i is a primitive root

                return i;
            }
        }

        return -1; // If no primitive root is found for a number
    }

    public static int gcd(int a, int b) {
        // Calculate the greatest common divisor of two numbers

        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }

    public static int modPow(int base, int exponent, int mod) {
        // Calculate very large exponents AND modulus the result

        int result = 1;
        base = base % mod; // Make sure base is less than mod by taking modulus of base with mod

        while (exponent > 0) {
            if ((exponent & 1) != 0) {
                // If the exponent is ODD, multiply the result with (base) modulo (mod):

                result = (int) ( (long) result * base % mod); // Convert initial result to long to prevent integer overflow
            }

            exponent = exponent >> 1; // Divide the exponent by 2 (bitwise shift operator)

            base = (int) ( (long) base * base % mod); // Convert initial result to long to prevent integer overflow
        }
        return result;
    }

    public int generateYValue(int primitiveRoot, int X, int randomPrime) {
        return modPow(primitiveRoot, X, randomPrime);
    }

    public int generatePrivateKey(int Y, int X, int randomPrime) {
        return modPow(Y, X, randomPrime);
    }

}
