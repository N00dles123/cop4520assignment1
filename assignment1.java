import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.*;
import java.util.*;
import java.io.*;
// current approach, create 8 threads and initialize them from 1-8,
// then increment counter by 8 each time a thread executes a prime check 
// also make use of square root property for primes with function
// about 20 seconds on java make use of flag principle
// try to use segmented sieve
public class assignment1 {
    // shared values and resources
    public int max = 100000000;
    public int numThreads = 8;
    public int blockSize = 10000;
    //shared value
    public AtomicInteger low = new AtomicInteger(1);
    public AtomicInteger numPrimes = new AtomicInteger(0);
    public AtomicLong sum = new AtomicLong(0);

    public PriorityBlockingQueue<Integer> top10 = new PriorityBlockingQueue<Integer>(1, Collections.reverseOrder());
    

    //public AtomicIntegerArray isPrimeArray = new AtomicIntegerArray(max + 1);

    //public AtomicIntegerArray used = new AtomicIntegerArray(max + 1);

    primeThread threads[] = new primeThread[numThreads];
    Thread t[] = new Thread[numThreads];
    // test Single Thread
    Thread test;

    public static void main(String[] args) {
        assignment1 a1 = new assignment1();
        long start = System.currentTimeMillis();
        
        // create threads

        a1.threads[0] = new primeThread(a1);
        a1.threads[1] = new primeThread(a1);
        a1.threads[2] = new primeThread(a1);
        a1.threads[3] = new primeThread(a1);
        a1.threads[4] = new primeThread(a1);
        a1.threads[5] = new primeThread(a1);
        a1.threads[6] = new primeThread(a1);
        a1.threads[7] = new primeThread(a1);
        for(primeThread thread: a1.threads){
            thread.start();
        }
        // wait for threads to finish
        for(primeThread thread: a1.threads){
            try {
                thread.join();
            } catch(Exception e){
                System.out.println("Error: " + e);
            }
        }
        
        // handle file io stuff
  
        long end = System.currentTimeMillis();
        try {
            File write = new File("primes.txt");
            FileWriter fw = new FileWriter(write);
            BufferedWriter out = new BufferedWriter(fw);
            
            out.write((end - start) + " " + a1.numPrimes.get() + " " + a1.sum.get() + "\n");
            
             
            for(int i = 0; i < 10; i++){
                out.write(a1.top10.poll() + " ");
            }
            

            out.close();
        } catch(Exception e){
            System.out.println("Error: " + e);
        }
        
    }
    
}

class primeThread extends Thread {
    assignment1 a1;
    public primeThread(assignment1 a1){
        this.a1 = a1; 
    }
    public void run(){
        try {
            while(a1.low.get() < a1.max){
                int curLow = a1.low.getAndAdd(a1.blockSize);
                int curHigh = curLow + a1.blockSize;
                sieve(curLow, curHigh);
            }
            

        } catch(Exception e){
            System.out.println("Error: " + e);
        }
    }
    //segmented sieve 
    public void sieve(int low, int high){
        boolean[] isPrime = new boolean[a1.blockSize + 1];


        Arrays.fill(isPrime, true);
        int limit = (int) Math.sqrt(high);

        for(int i = 2; i <= limit; i++){
            for(int j = Math.max(i * i, (low + i - 1) / i * i); j < high; j += i){
                isPrime[j - low] = false;
            }
        }

        if(low == 1){
            isPrime[0] = false;
        }

        // now process
        for(int i = 0; i < a1.blockSize && low + i <= a1.max; i++){
            if(isPrime[i]){
                a1.numPrimes.incrementAndGet();
                a1.top10.add(i + low);
                a1.sum.addAndGet(i + low);
            }
        }
    }
    

}