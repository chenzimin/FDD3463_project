public class Main {
    public static void main(String[] args) {
        int[] left_bits = {1, 1, 1, 1, 0, 0, 0, 0};  // 0000 1111 = 15
        int[] right_bits = {1, 1, 1, 1, 0, 0, 0, 0};  // 0000 1111 = 15
        EightBitAdder adder = new EightBitAdder(left_bits, right_bits);
        
        System.out.println(adder.sum);

        /*
        // Verify this piece of code should be fast and without error.

        FullAdder adder = new FullAdder(1, 1, 0);
        synchronized(adder.sum) {
            while(!adder.sum.done) {
                try {
                adder.sum.wait(1000);
                } catch (InterruptedException e) {
                e.printStackTrace();
                }
            }
            System.out.println(adder.sum.output);
        }
         */
    }
}
