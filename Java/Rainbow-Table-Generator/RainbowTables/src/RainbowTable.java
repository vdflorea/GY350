
/*  CT255 Assignment 2
 *  This class provides functionality to build rainbow tables (with a different reduction function per round) for 8 character long strings, which
    consist of the symbols "a .. z", "A .. Z", "0 .. 9", "!" and "#" (64 symbols in total).
    Properly used, it creates the following value pairs (start value - end value) after 10,000 iterations of hashFunction() and reductionFunction():
          start value  -  end value
          Kermit12        lsXcRAuN
          Modulus!        L2rEsY8h
          Pigtail1        R0NoLf0w
          GalwayNo        9PZjwF5c
          Trumpets        !oeHRZpK
          HelloPat        dkMPG7!U
          pinky##!        eDx58HRq
          01!19!56        vJ90ePjV
          aaaaaaaa        rLtVvpQS
          036abgH#        klQ6IeQJ
          
          
 *
 * @author Michael Schukat / Vlad Florea
 * @version 1.0
 */
public class RainbowTable
{

    public static void main(String[] args) {
		String start;

		if (args != null && args.length > 0) { // Check for <input> value
			start = args[0]; // Assign first command line argument to "start" variable
				
			if (start.length() != 8) {
                // Command line argument must be 8 characters long exactly

				System.out.println("Input " + start + " must be 8 characters long - Exit");
			}
			else {
                long resHash; // Result of hashing function
                String resReduction = ""; // Result of reduction function

                // Parallel arrays storing start/end values of each chain
                String[] startValues = new String[10];
                String[] endValues = new String[10];

                // Assigning 10 start values to each index in an array
                startValues[0] = "Kermit12";
                startValues[1] = "Modulus!";
                startValues[2] = "Pigtail1";
                startValues[3] = "GalwayNo";
                startValues[4] = "Trumpets";
                startValues[5] = "HelloPat";
                startValues[6] = "pinky##!";
                startValues[7] = "01!19!56";
                startValues[8] = "aaaaaaaa";
                startValues[9] = "036abgH#";

                // Part ONE
                System.out.println("-------------------------Part ONE-------------------------");

                for (int i = 0; i < 10; i++) {
                    // Iterates through 10 chains of 10,000 hash/reduction cycles
                    // --> Stores end value of each chain in endValues array

                    System.out.print("\n\n");
                    System.out.println("---------------Chain " +(i+1)+ "---------------");
                    System.out.println("Start Value ---> " + startValues[i]);
                    System.out.print("\n\n");
                    resHash = hashFunction(startValues[i]); // Initial hash of the start value of chain "i+1"

                    for (int j = 0; j < 10000; j++) {
                        // Iterates through 10,000 hash/reduction cycles within a particular chain "i+1"
                        // --> Different j sent in as "round" every iteration to the reduction function to vary the reduction algorithm

                        resReduction = reductionFunction(resHash, j); // Sending in the hash to be reduced along with j as "round"
                        resHash = hashFunction(resReduction); // Sending in the reduced value to be hashed again

                        if (j % 500 == 0) {
                            // Each 500th "reduced" value is printed out to illustrate the chain

                            System.out.printf("Chain %d, Value %d ---> %s\n", (i+1), j, resReduction);
                        }

                        if (j == 9999) {
                            // 10,000th value stored as the endValue corresponding to chain "i+1"

                            endValues[i] = resReduction;
                            System.out.printf("\nEnd Value ---> %s\n", endValues[i]);
                        }
                    }
                }

                // Print out each start/value pair of each of the 10 chains
                System.out.print("\n\n");
                System.out.println("---------------Start/End Value Pairs---------------");
                for (int i = 0; i < 10; i++) {
                    System.out.println("Start Value: " +startValues[i]+ "\t" + "End Value: " + endValues[i]);
                }

                // Part TWO
                System.out.println("\n\n-------------------------Part TWO-------------------------");

                long[] hashValues = new long[]{895210601874431214L, 750105908431234638L, 111111111115664932L, 977984261343652499L};
                resReduction = ""; // Resetting resReduction from result of Part ONE


                for (int i = 0; i < 4; i++) {
                    // Searches for a hash collision for each of the 4 given hashes

                    boolean found = false;

                    for (int j = 0; j < 10 && !found; j++) {
                        // Searches through each of the 10 chains between the start and end values
                        // --> Stops searching through subsequent chains if a match is found

                        int k = 0;

                        resHash = hashFunction(startValues[j]); // Initial hash of the start value of chain "i+1"

                        while (!resReduction.equals(endValues[j]) && !found) {
                            // Loops until the reduced value equals the end value i.e. hits the end of the chain (10,000 iterations)
                            // OR
                            // Loops until one of the given 4 hash values matches a hash value inside one of the 10 chains

                            if (resHash == hashValues[i]) {
                                // If a match is found, details are printed out and moves onto the subsequent hash in the hashValues array

                                System.out.println("\nMatch Found! ---> Chain = " +j+  " ||| " + "Between: " +startValues[j]+ " <---> " +endValues[j]);
                                System.out.println(resReduction+ " reduces to: " +resHash+ " == " +hashValues[i]);
                                found = true;
                            }

                            resReduction = reductionFunction(resHash, k); // Sending in the hash to be reduced along with k as "round"
                            resHash = hashFunction(resReduction); // Sending in the reduced value to be hashed again

                            k++; // Different k sent in as "round" every iteration to the reduction function to vary the reduction algorithm
                        }
                    }
                }

			}
		}
		else { // No <input> 
			System.out.println("Use: RainbowTable <Input>");
		} 
	}
        
    private static long hashFunction(String s){
        long ret = 0;
        int i;
        long[] hashA = new long[]{1, 1, 1, 1};
        
        String filler, sIn;
        
        int DIV = 65536;
        
        filler = new String("ABCDEFGHABCDEFGHABCDEFGHABCDEFGHABCDEFGHABCDEFGHABCDEFGHABCDEFGH");
        
        sIn = s + filler; // Add characters, now have "<input>HABCDEF..."
        sIn = sIn.substring(0, 64); // // Limit string to first 64 characters

        for (i = 0; i < sIn.length(); i++) {
            char byPos = sIn.charAt(i); // get i'th character
            hashA[0] += (byPos * 17111); // Note: A += B means A = A + B
            hashA[1] += (hashA[0] + byPos * 31349);
            hashA[2] += (hashA[1] - byPos * 101302);
            hashA[3] += (byPos * 79001);
        } 
           
        ret = (hashA[0] + hashA[2]) + (hashA[1] * hashA[3]);
        if (ret < 0) ret *= -1;
        return ret;
    } 
    
    private static String reductionFunction(long val, int round) {  // Note that for the first function call "round" has to be 0, 
        String car, out;                                            // and has to be incremented by one with every subsequent call. 
        int i;                                                      // I.e. "round" created variations of the reduction function.
        char dat;                                                  
        
        car = new String("0123456789ABCDEFGHIJKLMNOPQRSTUNVXYZabcdefghijklmnopqrstuvwxyz!#");
        out = new String(""); 
      
        for (i = 0; i < 8; i++) {
            val -= round;
            dat = (char) (val % 63);
            val = val / 83;
            out = out + car.charAt(dat);
        }
        
        return out;
    }
}
