// current approach, create 8 threads and make a thread safe counter,
// then increment counter each time a thread finishes 
// also make use of square root property for primes with function
public class assignment1 {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        long start = System.currentTimeMillis();
        Counter num = new Counter(2);
        testThread t1 = new testThread(num);
        testThread t2 = new testThread(num);
        testThread t3 = new testThread(num);
        testThread t4 = new testThread(num);
        testThread t5 = new testThread(num);
        testThread t6 = new testThread(num);
        testThread t7 = new testThread(num);
        testThread t8 = new testThread(num);
        t1.run();
        t2.run();
        t3.run();
        t4.run();
        t5.run();
        t6.run();
        t7.run();
        t8.run();
        long end = System.currentTimeMillis();
        System.out.println(end - start + " milliseconds");

        // testing loop
        long start2 = System.currentTimeMillis();
        for(int i = 0; i < 8; i++){
            System.out.println(i);
        }
        long end2 = System.currentTimeMillis();
        System.out.println(end2 - start2 + " milliseconds");

    }
}
class testThread extends Thread {
    long curValue;
    public testThread(Counter count) {
        curValue = count.getAndIncrement();
    }
    public void run(){
        //long startTime = System.currentTimeMillis();
        System.out.println(curValue);
        
    }
}
class Counter{
    public long count;
    private Object lock = new Object();
    public Counter(long count) {
        this.count = count;
    }
    public long getAndIncrement() {
        synchronized (lock) {
            long temp = count;
            count = temp + 1;
            return temp;
        }
        
    }
    
}