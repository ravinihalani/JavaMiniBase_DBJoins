package iterator.sm_join_assign_src;

import java.lang.*;
import chainexception.*;

public class NoOutputBuffer extends ChainException {
  public NoOutputBuffer(String s){super(null,s);}
  public NoOutputBuffer(Exception prev, String s){super(prev,s);}
}
