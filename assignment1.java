import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.*;
import java.util.*;
import java.io.*;
// current approach, create 8 threads and initialize them from 1-8,
// then increment counter by 8 each time a thread executes a prime check 
// also make use of square root property for primes with function
// about 20 seconds on java make use of flag principle
public class assignment1 {
    // shared values and resources
    public int max = 100000000;
    public int numThreads = 8;

    public AtomicInteger numPrimes = new AtomicInteger(0);
    public AtomicLong sum = new AtomicLong(0);
    public PriorityBlockingQueue<Integer> top10 = new PriorityBlockingQueue<Integer>(1, Collections.reverseOrder());
    //public AtomicIntegerArray used = new AtomicIntegerArray(max + 1);
    primeThread threads[] = new primeThread[numThreads];
    Thread t[] = new Thread[numThreads];
    // test Single Thread
    Thread test;

    public static void main(String[] args) {
        assignment1 a1 = new assignment1();
        long start = System.currentTimeMillis();
        /* 
        for(int i = 0; i <= a1.max; i++){
            a1.used.set(i, 1);
        }
        */

        a1.threads[0] = new primeThread(a1, new AtomicInteger(1));
        a1.threads[1] = new primeThread(a1, new AtomicInteger(2));
        a1.threads[2] = new primeThread(a1, new AtomicInteger(3));
        a1.threads[3] = new primeThread(a1, new AtomicInteger(4));
        a1.threads[4] = new primeThread(a1, new AtomicInteger(5));
        a1.threads[5] = new primeThread(a1, new AtomicInteger(6));
        a1.threads[6] = new primeThread(a1, new AtomicInteger(7));
        a1.threads[7] = new primeThread(a1, new AtomicInteger(8));
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
    AtomicInteger curVal;
    public primeThread(assignment1 a1, AtomicInteger curVal){
        this.a1 = a1; 
        this.curVal = curVal;
    }

    public void run(){
        try {
            while(curVal.get() < a1.max){
                if(isPrime(curVal)){
                    a1.numPrimes.incrementAndGet();
                }
                curVal.addAndGet(8);
            }
        } catch(Exception e){
            System.out.println("Error: " + e);
        }
    }
    /* 
    public void optimizedPrime(AtomicInteger curVal, assignment1 a1){
        // assume everything is true
        int current = curVal.get();
        for(int i = curVal)
    }
   */
    public boolean isPrime(AtomicInteger curVal){
        int current = curVal.get();
        if(current == 2 || current == 3){
            a1.top10.add(current);
            a1.sum.addAndGet(current);
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
        // true scenario
        a1.sum.addAndGet(current);
        a1.top10.add(current);
        return true;
    
        
        
    }
}