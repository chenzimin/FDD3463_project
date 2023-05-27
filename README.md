# FDD3463_project

This project uses NuSMV to verify the correctness of a 8-bit adder. We can use CTL to verify that the sum and cout is correct. The cool thing is that we can ask it for counterexample and then we can see the whole calculation bit by bit.

The detailed model is implemented in Java, where each Gate is a thread. The Gate thread will wait for the left and right input Gate to finish its computation before computing its own value. The detailed model is verified with JPF. The verification is very slow, but I canceled it because it does not report error after several minutes. I have also verified a single full adder with JPF and it reports no error.
