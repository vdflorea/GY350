import javax.swing.JOptionPane;

/**
 * Array implementation of Queue ADT
 */

public class ArrayQueue implements Queue {
    protected Object Q[];                // array used to implement the queue
    protected int rear = -1;            // index for the rear of the queue
    protected int capacity;            // The actual capacity of the queue array
    protected int numOperations = 0;
    public static final int CAPACITY = 1000;    // default array capacity

    public ArrayQueue() {
        // default constructor: creates queue with default capacity
        this(CAPACITY);
    }

    public ArrayQueue(int cap) {
        // this constructor allows you to specify capacity
        capacity = (cap > 0) ? cap : CAPACITY;
        Q = new Object[capacity];
    }

    public void enqueue(Object n) {

        // Commented out as this program will never reach full Queue capacity (smaller number of operations now)
        //		 if (isFull()) {
        //			 JOptionPane.showMessageDialog(null, "Cannot enqueue object; queue is full.");
        //			 return;
        //		 }

        // Two operations (numOperations += 2)
        rear++;
        Q[rear] = n;
    }

    public Object dequeue() {

        // Commented out as this program checks for empty Queue condition (smaller number of operations now)
        //		 if (isEmpty())
        //			 return null;

        Object toReturn = Q[0];
        numOperations++; // Instantiate "toReturn"

        int i = 1;
        numOperations++; // Instantiate "i"

        // At beginning of this loop, rear will be s.length - 1, so numOperations += s.length - 1
        while (i <= rear) {
            numOperations++; // Each iteration of loop

            Q[i - 1] = Q[i];
            i++;
            numOperations += 2; // Two operations within loop
        }
        numOperations++; // Final check of loop

        numOperations += 2; // Two operations including return statement
        rear--;
        return toReturn;
    }

    public boolean isEmpty() {
        return (rear < 0);
    }

    public boolean isFull() {
        return (rear == capacity - 1);
    }

    public Object front() {
        if (isEmpty())
            return null;

        return Q[0];
    }

    public int returnNumOperations() {
        // In theory, function call and function content are also operations (excluding them)

        int prevNumOperations = numOperations;
        numOperations = 0; // Reset numOperations
        return prevNumOperations;
    }

}