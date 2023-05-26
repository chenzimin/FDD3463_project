package adder;

public class Main {
    public static void main(String[] args) {
        Gate a1 = new XorGate(1, 0);
        Gate a2 = new XorGate(0, 0);
        Gate a3 = new XorGate(a1, a2);

        synchronized(a3) {
            while(!a3.done){
                try {
                    a3.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(a3.output);
        }
    }
}
