import java.util.*;

public class test {
    public PriorityQueue<Integer> top10 = new PriorityQueue<Integer>(1, Collections.reverseOrder());
    public int numPrimes = 0;
    public long sum = 0;
    public static void main(String[] args) {
        test testing = new test();
        long start = System.currentTimeMillis();
        testing.doPrimes(100000000);
        ArrayList<Integer> topten = new ArrayList<Integer>();
        for(int i = 0; i < 10; i++){
            topten.add(testing.top10.poll());
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        System.out.println(testing.numPrimes);
        System.out.println(testing.sum);
        System.out.println(topten.toString());
    }
    public void doPrimes(int n){
        int blocksize = 10000;
        int lim = (int) (Math.floor(Math.sqrt(n))+ 1);

        int low = 1;
        int high = low + blocksize;

        while(low < n){
            if(high >= n){
                high = n;
            }
            boolean[] isPrime = new boolean[blocksize + 1];
            Arrays.fill(isPrime, true);
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
            high += blocksize;
        }
    }
}
