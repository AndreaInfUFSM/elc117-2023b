class SharedObjectExample {
  private int sharedValue;

  public SharedObjectExample(int value) {
    this.sharedValue = value;
  }

  public void updateValue(int newValue) {
    sharedValue = newValue;
  }

  public int getValue() {
    return sharedValue;
  }
}

class SharedObjectApp {

  public static void main(String[] args) {
    SharedObjectExample e = new SharedObjectExample(10);
  }
}


