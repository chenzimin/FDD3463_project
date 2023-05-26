package adder;


public abstract class Gate extends Thread {
  Gate left_input;
  Gate right_input;
  boolean done;

  public int output;

  public Gate(Gate left_input, Gate right_input) {
    this.left_input = left_input;
    this.right_input = right_input;
    this.done = false;
    start();
  }

  public Gate(int left_int, int right_int) {
    this.done = false;
    set_output(left_int, right_int);
  }

  public void run() {
    synchronized (left_input) {
      while(!left_input.done){
        try {
          left_input.wait(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      synchronized (right_input) {
        while(!right_input.done){
          try {
            right_input.wait(1000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        set_output(left_input.output, right_input.output);
        synchronized(this) {
          notifyAll();
        }
        right_input.notifyAll();
      }
      left_input.notifyAll();
    }
  }

  public abstract void set_output(int left_int, int right_int);
}


class AndGate extends Gate {

  public AndGate(Gate left_input, Gate right_input) {
    super(left_input, right_input);
  }

  public AndGate(int left_int, int right_int) {
    super(left_int, right_int);
  }

  @Override
  public void set_output(int left_int, int right_int) {
    output = left_int & right_int;
    done = true;
  }
}

class OrGate extends Gate {

  public OrGate(Gate left_input, Gate right_input) {
    super(left_input, right_input);
  }

  public OrGate(int left_int, int right_int) {
    super(left_int, right_int);
  }

  @Override
  public void set_output(int left_int, int right_int) {
    output = left_int | right_int;
    done = true;
  }
}

class XorGate extends Gate {

  public XorGate(Gate left_input, Gate right_input) {
    super(left_input, right_input);
  }

  public XorGate(int left_int, int right_int) {
    super(left_int, right_int);
  }

  @Override
  public void set_output(int left_int, int right_int) {
    output = left_int ^ right_int;
    done = true;
  }
}

