package test;

import java.util.ArrayList;
import driver.Command;

public class MockCommand extends Command{

  public MockCommand() {
    super(true);
  }

  @Override
  public void executeCommand(ArrayList<String> arguments) {

    if (arguments.isEmpty()) {
      this.setError("empty arguments");
    }

    else {
      this.setPrintCommand(arguments.toString());
    }
  }

  
}
