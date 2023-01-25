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
    public AtomicInteger low = new AtomicInteger(1);
    public AtomicInteger numPrimes = new AtomicInteger(0);
    public AtomicLong sum = new AtomicLong(0);

    public PriorityBlockingQueue<Integer> top10 = new PriorityBlockingQueue<Integer>(1, Collections.reverseOrder());
    // shared value
    public AtomicInteger curVal = new AtomicInteger(1);

    //public AtomicIntegerArray isPrimeArray = new AtomicIntegerArray(max + 1);

    //public AtomicIntegerArray used = new AtomicIntegerArray(max + 1);

    primeThread threads[] = new primeThread[numThreads];
    Thread t[] = new Thread[numThreads];
    // test Single Thread
    Thread test;

    public static void main(String[] args) {
        assignment1 a1 = new assignment1();
        long start = System.currentTimeMillis();
        /*
        // 1 is prime 0 is not prime
        for(int i = 0; i <= a1.max; i++){
            a1.isPrimeArray.set(i, 1);
        }
        */
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
            /*for(int i = 2; i <= a1.max; i++){
                if(a1.isPrimeArray.get(i) == 1){
                    a1.numPrimes.incrementAndGet();
                    a1.sum.addAndGet(i);
                }
            } */
            out.write((end - start) + " " + a1.numPrimes.get() + " " + a1.sum.get() + "\n");
            // write top 10
            /* 
            int total = 0;
            int i = a1.max;
            while(total < 10){
                if(a1.isPrimeArray.get(i) == 1){
                    out.write(i + " ");
                    total++;
                }
                i--;
            */
             
            for(int i = 0; i < 10; i++){
                out.write(a1.top10.poll() + " ");
            }
            

            out.close();
        } catch(Exception e){
            System.out.println("Error: " + e);
        }
        /* 
        // test single thread
        long startlinear = System.currentTimeMillis();

        PriorityQueue<Integer> top10linear = new PriorityQueue<Integer>(1, Collections.reverseOrder());
        long sumlinear = 0;
        int numPrimeslinear = 0;
        for(int i = 1; i <= a1.max; i++){
            if(isPrime(i)){
                numPrimeslinear++;
                sumlinear += i;
                top10linear.add(i);
            }
        }

        long endlinear = System.currentTimeMillis();
        try {
            File write = new File("linear.txt");
            FileWriter fw = new FileWriter(write);
            BufferedWriter out = new BufferedWriter(fw);
            out.write((endlinear - startlinear) + " " + numPrimeslinear + " " + sumlinear + "\n");
            for(int i = 0; i < 10; i++){
                out.write(top10linear.poll() + " ");
            }
            out.close();
        } catch (Exception e){
            System.out.println("Error: " + e);
        }
        */
    }
    public static boolean isPrime(int n){
        if(n == 2 || n == 3){
            return true;
        }
        if(n <= 1 || n % 2 == 0 || n % 3 == 0){
            return false;
        }
        for(int i = 5; i * i <= n; i += 6){
            if(n % i == 0 || n % (i + 2) == 0){
                // false scenario
                return false;
            }
        }
        // true scenario
        return true;
    }
}

class primeThread extends Thread {
    assignment1 a1;
    //int curVal;
    public primeThread(assignment1 a1){
        this.a1 = a1; 
        //this.curVal = curVal;
    }

    public void run(){
        try {
            while(a1.low.get() < a1.max){
                int curLow = a1.low.getAndAdd(a1.blockSize);
                int curHigh = curLow + a1.blockSize;
                sieve(curLow, curHigh);
            }
            /* 
            while(a1.curVal.get() < a1.max){
                int cur = a1.curVal.incrementAndGet();
                //System.out.println(cur);
                if(isPrime(cur)){
                    a1.numPrimes.incrementAndGet();
                    a1.top10.add(cur);
                    //a1.used.set(curVal, 1);
                    a1.sum.addAndGet(cur);
                } 
                //sieve(cur);
            }
            */

        } catch(Exception e){
            System.out.println("Error: " + e);
        }
    }
    // prime function
    public boolean isPrime(int curVal){
        int current = curVal;
        if(current == 2 || current == 3){
            return true;
        }
        if(current <= 1 || current % 2 == 0 || current % 3 == 0){
            return false;
        }
        for(int i = 5; i * i <= current; i += 6){
            if(current % i == 0 || current % (i + 2) == 0){
                // false scenario
                return false;
            }
        }
        return true;  
    }
     
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