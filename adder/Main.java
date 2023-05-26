public class Main {
    public static void main(String[] args) {
        int[] left_bits = {1, 1, 1, 1, 0, 0, 0, 0};
        int[] right_bits = {1, 1, 1, 1, 0, 0, 0, 0};
        EightBitAdder adder = new EightBitAdder(left_bits, right_bits);
        
        System.out.println(adder.sum);
    }
}
