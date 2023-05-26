public abstract class Gate extends Thread {
  Gate left_input;
  Gate right_input;
  boolean wait_left;
  boolean wait_right;
  int left_int;
  int right_int;


  boolean done;

  public int output;

  public Gate(Gate left_input, Gate right_input) {
    this.left_input = left_input;
    this.right_input = right_input;
    this.wait_left = true;
    this.wait_right = true;
    this.done = false;
    start();
  }

  public Gate(Gate left_input, int right_int) {
    this.left_input = left_input;
    this.right_int = right_int;
    this.wait_left = true;
    this.wait_right = false;
    this.done = false;
    start();
  }

  public Gate(int left_int, Gate right_input) {
    this.right_input = right_input;
    this.left_int = left_int;
    this.wait_left = false;
    this.wait_right = true;
    this.done = false;
    start();
  }

  public Gate(int left_int, int right_int) {
    this.wait_left = false;
    this.wait_right = false;
    this.left_int = left_int;
    this.right_int = right_int;
    this.done = false;
    start();
  }

  public void run() {
    if (wait_left){
      synchronized (left_input) {
        while(!left_input.done){
          try {
            left_input.wait(1000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        left_int = left_input.output;
        left_input.notifyAll();
      }
    }

    if (wait_right){
      synchronized (right_input) {
        while(!right_input.done){
          try {
            right_input.wait(1000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        right_int = right_input.output;
        right_input.notifyAll();
      }
    }

    set_output(left_int, right_int);
    synchronized(this) {
      notifyAll();
    }

  }

  public abstract void set_output(int left_int, int right_int);
}


class AndGate extends Gate {

  public AndGate(Gate left_input, Gate right_input) {
    super(left_input, right_input);
  }

  public AndGate(Gate left_input, int right_int) {
    super(left_input, right_int);
  }

  public AndGate(int left_int, Gate right_input) {
    super(left_int, right_input);
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

  public OrGate(Gate left_input, int right_int) {
    super(left_input, right_int);
  }

  public OrGate(int left_int, Gate right_input) {
    super(left_int, right_input);
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

  public XorGate(Gate left_input, int right_int) {
    super(left_input, right_int);
  }

  public XorGate(int left_int, Gate right_input) {
    super(left_int, right_input);
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

class FullAdder {
  Gate first_xor;
  Gate sum;
  Gate first_and;
  Gate second_and;
  Gate cout;

  public FullAdder(Gate left_input, Gate right_input, Gate cin) {
    first_xor = new XorGate(left_input, right_input);
    sum = new XorGate(first_xor, cin);
    first_and = new AndGate(first_xor, cin);
    second_and = new AndGate(left_input, right_input);
    cout = new OrGate(first_and, second_and);
  }

  public FullAdder(int left_int, int right_int, int cin) {
    first_xor = new XorGate(left_int, right_int);
    sum = new XorGate(first_xor, cin);
    first_and = new AndGate(first_xor, cin);
    second_and = new AndGate(left_int, right_int);
    cout = new OrGate(first_and, second_and);
  }

  public FullAdder(int left_int, int right_int, Gate cin) {
    first_xor = new XorGate(left_int, right_int);
    sum = new XorGate(first_xor, cin);
    first_and = new AndGate(first_xor, cin);
    second_and = new AndGate(left_int, right_int);
    cout = new OrGate(first_and, second_and);
  }
}

class EightBitAdder {
  FullAdder firstAdder;
  FullAdder secondAdder;
  FullAdder thirdAdder;
  FullAdder fourthAdder;
  FullAdder fifthAdder;
  FullAdder sixthAdder;
  FullAdder seventhAdder;
  FullAdder eighthAdder;

  int sum;
  int cout;

  public EightBitAdder(int[] left_bits, int[] right_bits) {
    firstAdder = new FullAdder(left_bits[0], right_bits[0], 0);
    secondAdder = new FullAdder(left_bits[1], right_bits[1], firstAdder.cout);
    thirdAdder = new FullAdder(left_bits[2], right_bits[2], secondAdder.cout);
    fourthAdder = new FullAdder(left_bits[3], right_bits[3], thirdAdder.cout);
    fifthAdder = new FullAdder(left_bits[4], right_bits[4], fourthAdder.cout);
    sixthAdder = new FullAdder(left_bits[5], right_bits[5], fifthAdder.cout);
    seventhAdder = new FullAdder(left_bits[6], right_bits[6], sixthAdder.cout);
    eighthAdder = new FullAdder(left_bits[7], right_bits[7], seventhAdder.cout);

    synchronized (eighthAdder.sum) {
      while(!eighthAdder.sum.done) {
        try {
          eighthAdder.sum.wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      sum = (
        firstAdder.sum.output +
        secondAdder.sum.output * 2 + 
        thirdAdder.sum.output * 4 + 
        fourthAdder.sum.output * 8 + 
        fifthAdder.sum.output * 16 + 
        sixthAdder.sum.output * 32 + 
        seventhAdder.sum.output * 64 + 
        eighthAdder.sum.output * 128
      );
      eighthAdder.sum.notifyAll();
    }

    synchronized (eighthAdder.cout) {
      try {
        eighthAdder.cout.wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      cout = eighthAdder.cout.output;
      eighthAdder.cout.notifyAll();
    }
  }
}