# Multi Threaded Prime Calculation Between 1 and 10^8
Jason Lin COP 4520
## Compilation and Running in Command Line
To compile this assignment, cd into the directory of assignment1.java in the terminal and then in the terminal type "javac assignment1.java"
after compiling, run the program by then typing "java assignment1" in the terminal.
## Proof of Correctness and Experimental Evaluation
In this program, in order to divide the work evenly between all 8 threads, I implemented it where each thread would share a global Atomic Integer counter that starts at 1, each thread would continue computation on the current value in the counter and increment until it reaches 10^8. This would ensure that each thread does an equal amount of computation on its own pace. To help get the top 10 prime values I made use of a PriorityBlockingQueue which is basically a thread-safe PriorityQueue that way I can just make use of the sorting property as I add numbers to the priorityqueue. 

The time complexity of my prime calculation was $\sqrt{n}$
where n is the number that is being tested to see if it is a prime number. This function also included a few optimizations such as checking for even numbers to lower the amount of computations needed. 
In the single threaded method, the overall complexity to calculate the prime numbers between 1 and 10^8 would be $n\sqrt{n}$

In order to find out whether using multiple threads helps speed up the process I made one portion of my code test the multiple threads runtime and one portion test the single thread runtime. I ran this code on an M2 Macbook Air using Java and the results I got for single thread were 12.01 seconds while the results I got for 8 threads were 6.71 seconds. I also wanted to further test this by running the program 5 times and gathering the average times. For 8 threads, the average time to run was 6.73 seconds and for single thread, the average time to run was 11.506 seconds. So, multiple threads does help speed up the process of counting prime numbers from 1 to 10^8