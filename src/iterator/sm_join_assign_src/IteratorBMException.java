package iterator.sm_join_assign_src;

import chainexception.*;
import java.lang.*;

public class IteratorBMException extends ChainException {
  public IteratorBMException(String s){super(null,s);}
  public IteratorBMException(Exception prev, String s){ super(prev,s);}
}
