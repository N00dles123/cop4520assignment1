import java.util.*;
import java.io.*;
public class singlethread {
    public PriorityQueue<Integer> top10 = new PriorityQueue<Integer>(1, Collections.reverseOrder());
    public int numPrimes = 0;
    public long sum = 0;
    public static void main(String[] args) {
        singlethread testing = new singlethread();
        long start = System.currentTimeMillis();
        testing.doPrimes(100000000);
        
        long end = System.currentTimeMillis();
        try{
            File write = new File("linear.txt");
            FileWriter fw = new FileWriter(write);
            BufferedWriter out = new BufferedWriter(fw);

            out.write((end - start) + " " + testing.numPrimes + " " + testing.sum + "\n");

            for(int i = 0; i < 10; i++){
                out.write(testing.top10.poll() + " ");
            }

            out.close();
        } catch (Exception e){
            System.out.println("Error: " + e);
        }
    }
    public void doPrimes(int n){
        int blocksize = 10000;
        

        int low = 1;
        int high = low + blocksize;
        while(low < n){
            boolean[] isPrime = new boolean[blocksize + 1];
            Arrays.fill(isPrime, true);
            int lim = (int) Math.sqrt(high);
            for(int i = 2; i <= lim; i++){
                for(int j = Math.max(i * i, (low + i - 1) / i * i); j < high; j += i){
                    isPrime[j - low] = false;
                }
            }
            if(low == 1){
                isPrime[0] = false;
            }
            for(int i = 0; i < blocksize && low + i <= n; i++){
                if(isPrime[i]){
                    sum += (i + low);
                    numPrimes++;
                    top10.add(i + low);
                }
            }
            low += blocksize;
            high = low + blocksize;
        }
    }
}
