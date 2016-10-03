package iterator;
   

import heap.*;
import global.*;
import bufmgr.*;
import diskmgr.*;
import index.*;
import java.lang.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.io.*;
/** 
 *
 *  This file contains an implementation of the nested loops join
 *  algorithm as described in the Shapiro paper.
 *  The algorithm is extremely simple:
 *
 *      foreach tuple r in R do
 *          foreach tuple s in S do
 *              if (ri == sj) then add (r, s) to the result.
 */

public class IESelfJoin  extends Iterator 
{
  private AttrType      _in1[],  _in2[];
  private   int        in1_len, in2_len;
  private   Iterator  outer;
  private   short t2_str_sizescopy[];
  private   CondExpr OutputFilter[];
  private   CondExpr RightFilter[];
  private   int        n_buf_pgs;        // # of buffer pages available.
  private   boolean        done,         // Is the join complete
    get_from_outer;                 // if TRUE, a tuple is got from outer
  private   Tuple     outer_tuple, inner_tuple;
  private   Tuple     Jtuple;           // Joined tuple
  private   FldSpec   perm_mat[];
  private   int        nOutFlds;
  private   Heapfile  hf;
  private   Scan      inner;
  private   Tuple outer_tuples[];
  private   Tuple L1[];
  private   Tuple L2[];
  private   Tuple output_tuple[];
  private   int permutation_array [];
  private   boolean bit_array [];
  private boolean bloom_filter[];
  private int nrows;
  
  
  /**constructor
   *Initialize the two relations which are joined, including relation type,
   *@param in1  Array containing field types of R.
   *@param len_in1  # of columns in R.
   *@param t1_str_sizes shows the length of the string fields.
   *@param in2  Array containing field types of S
   *@param len_in2  # of columns in S
   *@param  t2_str_sizes shows the length of the string fields.
   *@param amt_of_mem  IN PAGES
   *@param am1  access method for left i/p to join
   *@param relationName  access hfapfile for right i/p to join
   *@param outFilter   select expressions
   *@param rightFilter reference to filter applied on right i/p
   *@param proj_list shows what input fields go where in the output tuple
   *@param n_out_flds number of outer relation fileds
   *@exception IOException some I/O fault
   *@exception NestedLoopException exception from this class
   */
  public IESelfJoin( AttrType    in1[],    
			   int     len_in1,           
			   short   t1_str_sizes[],
			   AttrType    in2[],         
			   int     len_in2,           
			   short   t2_str_sizes[],   
			   int     amt_of_mem,        
			   Iterator     am1,          
			   String relationName,      
			   CondExpr outFilter[],      
			   CondExpr rightFilter[],    
			   FldSpec   proj_list[],
			   int        n_out_flds, int nrows1
			   ) throws IOException,NestedLoopException
    {
      nrows=nrows1;
      _in1 = new AttrType[in1.length];
      _in2 = new AttrType[in2.length];
      System.arraycopy(in1,0,_in1,0,in1.length);
      System.arraycopy(in2,0,_in2,0,in2.length);
      in1_len = len_in1;
      in2_len = len_in2;      
      
      outer = am1;
      t2_str_sizescopy =  t2_str_sizes;
      inner_tuple = new Tuple();
      Jtuple = new Tuple();
      OutputFilter = outFilter;
      RightFilter  = rightFilter;
      
      n_buf_pgs    = amt_of_mem;
      inner = null;
      done  = false;
      get_from_outer = true;
      
      AttrType[] Jtypes = new AttrType[n_out_flds];
      short[]    t_size;
      
      perm_mat = proj_list;
      nOutFlds = n_out_flds;
      try {
	t_size = TupleUtils.setup_op_tuple(Jtuple, Jtypes,
					   in1, len_in1, in2, len_in2,
					   t1_str_sizes, t2_str_sizes,
					   proj_list, nOutFlds);
      }catch (TupleUtilsException e){
	throw new NestedLoopException(e,"TupleUtilsException is caught by NestedLoopsJoins.java");
      }
      
      try {
    	  hf = new Heapfile(relationName);
      }
      catch(Exception e) {
	throw new NestedLoopException(e, "Create new heapfile failed.");
      }
    }
  
  /**  
   *@return The joined tuple is returned
 * @exception IOException I/O errors
   *@exception JoinsException some join exception
   *@exception IndexException exception from super class
   *@exception InvalidTupleSizeException invalid tuple size
   *@exception InvalidTypeException tuple type not valid
   *@exception PageNotReadException exception from lower layer
   *@exception TupleUtilsException exception from using tuple utilities
   *@exception PredEvalException exception from PredEval class
   *@exception SortException sort exception
   *@exception LowMemException memory error
   *@exception UnknowAttrType attribute type unknown
   *@exception UnknownKeyTypeException key type unknown
   *@exception Exception other exceptions

   */
  private static Tuple[] ascbubbleSort(int n ,int pos, Tuple[] temp_tuples) throws UnknowAttrType, TupleUtilsException, IOException {
	  ArrayList<Tuple> sortedList = new ArrayList<Tuple>(Arrays.asList(temp_tuples));
	  Collections.sort(sortedList, new Comparator<Tuple>() {
          @Override
          public int compare(Tuple t1, Tuple t2) {
              try {
				return ((Integer)t1.getIntFld(pos)).compareTo(t2.getIntFld(pos));
			} catch (FieldNumberOutOfBoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0;
			}// ascending
          }
      });
  /*
	          Tuple temp = new Tuple();
         
          for(int i=0; i < n; i++){
                  for(int j=1; j < (n-i); j++){
                         
                        if (TupleUtils.CompareTupleWithTuple(new AttrType(AttrType.attrInteger), temp_tuples[j-1], pos, temp_tuples[j], pos) == 1){	  
                                  //swap the elements!
                                  temp = temp_tuples[j-1];
                                  temp_tuples[j-1] = temp_tuples[j];
                                  temp_tuples[j] = temp;
                          }
                         
                  }
          }
    */      
          //return temp_tuples;
          return sortedList.toArray(new Tuple[sortedList.size()]);
}

  
  private static Tuple[] dscbubbleSort(int n ,int pos, Tuple[] temp_tuples) throws UnknowAttrType, TupleUtilsException, IOException {
	  ArrayList<Tuple> sortedList = new ArrayList<Tuple>(Arrays.asList(temp_tuples));
	  Collections.sort(sortedList, new Comparator<Tuple>() {
          @Override
          public int compare(Tuple t1, Tuple t2) {
              try {
				return ((Integer)t2.getIntFld(pos)).compareTo(t1.getIntFld(pos));
			} catch (FieldNumberOutOfBoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0;
			}// Descending
          }
      });
  
      return sortedList.toArray(new Tuple[sortedList.size()]);
	  
/*      Tuple temp = new Tuple();
     
      for(int i=0; i < n; i++){
              for(int j=1; j < (n-i); j++){
                     
                    if (TupleUtils.CompareTupleWithTuple(new AttrType(AttrType.attrInteger), temp_tuples[j-1], pos, temp_tuples[j], pos) == -1){	  
                              //swap the elements!
                              temp = temp_tuples[j-1];
                              temp_tuples[j-1] = temp_tuples[j];
                              temp_tuples[j] = temp;
                      }
              }
      }
      return temp_tuples;*/
  }

  
  private boolean is_duplicate(Tuple L1, Tuple L2) throws UnknowAttrType, TupleUtilsException, IOException {
	  for (int d =0;d< _in1.length;d++)
		  {
			  if (TupleUtils.CompareTupleWithTuple(new AttrType(_in1[d].attrType), L1, d+1, L2, d+1) == 0 )
			  {
				  if (d == _in1.length -1)
					  return true;	  
			  }
		  }
	  return false;
}

  
  
  private int get_L1_position(Tuple L2 ,int len) throws UnknowAttrType, TupleUtilsException, IOException {

	  
	  for (int i=0;i<L1.length;i++)
	  {
/*		  if (TupleUtils.CompareTupleWithTuple(new AttrType(AttrType.attrInteger), L2, len-1, L1[i], len-1) == 0
				  &&
			  TupleUtils.CompareTupleWithTuple(new AttrType(AttrType.attrInteger), L2, len-2, L1[i], len-2) == 0)
	      {
	    	return i;  
	      }  
		  */
	
		  for (int d =0;d< _in1.length;d++)
		  {
			  //System.out.println("Vishnu"+_in1[d].attrType);
			  if (TupleUtils.CompareTupleWithTuple(new AttrType(_in1[d].attrType), L2, d+1, L1[i], d+1) == 0 )
			  {
				  if (d== _in1.length -1)
					  return i;	  
			  }
		  }
	  }
	  return -1;
}
  
  
  public void print_tuples (Tuple[] t) throws IOException 
  {
	  //System.out.println("Inside print tuples");
	  for (int i=0;i<t.length;i++)
		  t[i].print(_in1);
	  System.out.println();
  }

  
  public void IESelfJoinLogicInit(boolean bloom_filter_flag) throws JoinsException, IndexException, InvalidTupleSizeException, InvalidTypeException, PageNotReadException, TupleUtilsException, PredEvalException, SortException, LowMemException, UnknowAttrType, UnknownKeyTypeException, IOException, Exception
  {
	  outer_tuples = new Tuple[nrows];
	  L1 = new Tuple[nrows];
	  L2 = new Tuple[nrows];
	  permutation_array= new int [nrows];
	  bit_array= new boolean [nrows];
	  bloom_filter= new boolean[100];
	  //bloom_filter_flag = !bloom_filter_flag;
	
	  RID rid = new RID();


		try {
			inner = hf.openScan();
		} catch (Exception e) {
			throw new NestedLoopException(e, "openScan failed");
		}
		Tuple temp1 = new Tuple();
		for (int k = 0; k < nrows; k++) {
			outer_tuples[k] = new Tuple();
			L1[k] = new Tuple();
			L2[k] = new Tuple();

			temp1 = inner.getNext(rid);
			outer_tuples[k] = temp1;
			L1[k] = temp1;
			L2[k] = temp1;
			outer_tuples[k].setHdr((short) in2_len, _in2, t2_str_sizescopy);
			L1[k].setHdr((short) in2_len, _in2, t2_str_sizescopy);
			L2[k].setHdr((short) in2_len, _in2, t2_str_sizescopy);
		}
	       //    0        if the two are equal,
	   //    1        if the tuple is greater,
	   //   -1        if the tuple is smaller,

		int eqOffset = 1;
	    CondExpr op1 = OutputFilter[0];
	    CondExpr op2 = OutputFilter[1];
		if((op1.op.attrOperator == AttrOperator.aopLE || op1.op.attrOperator == AttrOperator.aopGE) && (op2.op.attrOperator == AttrOperator.aopLE || op2.op.attrOperator == AttrOperator.aopGE)) {
			eqOffset = 0;
		}
			
	    //print_tuples(outer_tuples);

	    switch (op1.op.attrOperator) {
		case AttrOperator.aopLT:
			//eqOffset = 1;
		case AttrOperator.aopLE:
			//System.out.println("L1 content : ");
			L1 = IESelfJoin.dscbubbleSort(nrows, op1.operand1.symbol.offset, L1);
			//print_tuples(L1);
			break;
		case AttrOperator.aopGT:
			//eqOffset = 1;
		case AttrOperator.aopGE:
			//System.out.println("L1 content : ");
			L1 = IESelfJoin.ascbubbleSort(nrows, op1.operand1.symbol.offset, L1);
//			/print_tuples(L1);
			break;
		case AttrOperator.aopNE:
		case AttrOperator.aopEQ:
		case AttrOperator.aopNOT:
		default:
			break;
		}
		
		switch (op2.op.attrOperator) {
		case AttrOperator.aopLT:
			//eqOffset = 1;
		case AttrOperator.aopLE:
		      //System.out.println("L2 content : ");
		      L2= IESelfJoin.ascbubbleSort(nrows, op2.operand1.symbol.offset,L2);
		      //print_tuples(L2);
		      break;
		case AttrOperator.aopGT:
//			eqOffset = 1;
		case AttrOperator.aopGE:
		      //System.out.println("L2 content : ");
		      L2= IESelfJoin.dscbubbleSort(nrows, op2.operand1.symbol.offset,L2);
		      //print_tuples(L2);			
		      break;
		case AttrOperator.aopNE:
		case AttrOperator.aopEQ:
		case AttrOperator.aopNOT:
		default:
			break;
		}
		

		// duplicates handling for operator 1
	      for (int i=0;i<nrows;i++)
	      {
	    	  Tuple temp[] = new Tuple[nrows];
	    	  int dupindex = i+1;
	    	  int dupcount = 0;
	    	  temp[dupcount] = new Tuple();
	    	  temp[dupcount] = L1[i];
	    	  while(dupindex < nrows && TupleUtils.CompareTupleWithTuple(new AttrType(AttrType.attrInteger), L1[i], op1.operand1.symbol.offset, L1[dupindex], op1.operand1.symbol.offset) == 0){
	    		  dupcount++;
	    		  temp[dupcount] = new Tuple();
	    		  temp[dupcount] = L1[dupindex];
	    		  temp[dupcount].setHdr((short) in2_len, _in2, t2_str_sizescopy);
	    		  dupindex++;
	    	  }
	    	  
	    	  Tuple temp2[] = new Tuple[dupcount+1];
	    	  for(int k = 0; k <= dupcount; k++) {
	    		  temp2[k] = temp[k];
	    	  }

	    	  //System.out.println("temp2 content : ");
	    	  //print_tuples(temp2);
	    	  
	    	  //System.out.println("L1 content : ");
	    	  //print_tuples(L1);

	    	  if(dupcount > 0) {
	    		  // operator 1
	    		    switch (op1.op.attrOperator) {
	    		    case AttrOperator.aopLE:
	    		    case AttrOperator.aopGE:
	    				switch (op2.op.attrOperator) {
	    				case AttrOperator.aopGT:
	    				case AttrOperator.aopGE:
	    					//System.out.println("temp2 content : ");
	    					temp2 = IESelfJoin.ascbubbleSort(dupcount, op2.operand1.symbol.offset,temp2);	    				    
	    					//print_tuples(temp2);
	    					break;
	    				case AttrOperator.aopLT:
	    				case AttrOperator.aopLE:
	    					//System.out.println("temp2 content : ");
	    					temp2 = IESelfJoin.dscbubbleSort(dupcount, op2.operand1.symbol.offset,temp2);	    				    
	    					//print_tuples(temp2);
	    				default:
	    					break;
	    				}
	    				break;
	    		    case AttrOperator.aopLT:
	    		    case AttrOperator.aopGT:
	    				switch (op2.op.attrOperator) {
	    				case AttrOperator.aopGT:
	    				case AttrOperator.aopGE:
	    					//System.out.println("temp2 content : ");
	    					temp2 = IESelfJoin.dscbubbleSort(dupcount, op2.operand1.symbol.offset,temp2);	    				    
	    					//print_tuples(temp2);
	    					break;
	    				case AttrOperator.aopLT:
	    				case AttrOperator.aopLE:
	    					//System.out.println("temp2 content : ");
	    					temp2 = IESelfJoin.ascbubbleSort(dupcount, op2.operand1.symbol.offset,temp2);	    				    
	    					//print_tuples(temp2);
	    				default:
	    					break;
	    				}
	    				break;
	    		    default:
	    				break;
	    			}
	    		    
	  	    	  for(int j = 0; j <= dupcount; j++) {
		    		  Tuple t1 = new Tuple();
		    		  t1 = temp2[j];
		    		  //t1.print(_in1);
		    		  //System.out.println();
		    		  L1[i] = t1;
		    		  //L1[i].setHdr((short) in2_len, _in2, t2_str_sizescopy);
		    		  i++;
		    	  }
		    	  i--;
					//System.out.println("L1 content : ");
					//print_tuples(L1);

	    	  }
	    	  
	      }

		// Duplicates handling for operator 2
	      for (int i=0;i<nrows;i++)
	      {
	    	  Tuple temp[] = new Tuple[nrows];
	    	  int dupindex = i+1;
	    	  int dupcount = 0;
	    	  temp[dupcount] = new Tuple();
	    	  temp[dupcount] = L2[i];
	    	  while(dupindex < nrows && TupleUtils.CompareTupleWithTuple(new AttrType(AttrType.attrInteger), L2[i], op2.operand1.symbol.offset, L2[dupindex], op2.operand1.symbol.offset) == 0){
	    		  dupcount++;
	    		  temp[dupcount] = new Tuple();
	    		  temp[dupcount] = L2[dupindex];
	    		  temp[dupcount].setHdr((short) in2_len, _in2, t2_str_sizescopy);
	    		  dupindex++;
	    	  }
	    	  
	    	  Tuple temp2[] = new Tuple[dupcount+1];
	    	  for(int k = 0; k <= dupcount; k++) {
	    		  temp2[k] = temp[k];
	    	  }

	    	  //System.out.println("temp2 content : ");
	    	  //print_tuples(temp2);
	    	  
	    	  if(dupcount > 0) {
	    		  // operator 2
	    		    switch (op2.op.attrOperator) {
	    		    case AttrOperator.aopLE:
	    		    case AttrOperator.aopGE:
	    				switch (op1.op.attrOperator) {
	    				case AttrOperator.aopGT:
	    				case AttrOperator.aopGE:
	    					//System.out.println("temp2 content : ");
	    					temp2 = IESelfJoin.dscbubbleSort(dupcount, op1.operand1.symbol.offset,temp2);	    				    
	    					//print_tuples(temp2);
	    					break;
	    				case AttrOperator.aopLT:
	    				case AttrOperator.aopLE:
	    					//System.out.println("temp2 content : ");
	    					temp2 = IESelfJoin.ascbubbleSort(dupcount, op1.operand1.symbol.offset,temp2);	    				    
	    					//print_tuples(temp2);
	    				default:
	    					break;
	    				}
	    				break;
	    		    case AttrOperator.aopLT:
	    		    case AttrOperator.aopGT:
	    				switch (op1.op.attrOperator) {
	    				case AttrOperator.aopGT:
	    				case AttrOperator.aopGE:
	    					//System.out.println("temp2 content : ");
	    					temp2 = IESelfJoin.ascbubbleSort(dupcount, op1.operand1.symbol.offset,temp2);	    				    
	    					//print_tuples(temp2);
	    					break;
	    				case AttrOperator.aopLT:
	    				case AttrOperator.aopLE:
	    					//System.out.println("temp2 content : ");
	    					temp2 = IESelfJoin.dscbubbleSort(dupcount, op1.operand1.symbol.offset,temp2);	    				    
	    					//print_tuples(temp2);
	    				default:
	    					break;
	    				}
	    				break;
	    		    default:
	    				break;
	    			}
	    		    
	  	    	  for(int j = 0; j <= dupcount; j++) {
		    		  Tuple t1 = new Tuple();
		    		  t1 = temp2[j];
//		    		  /t1.print(_in1);
		    		  //System.out.println();
		    		  L2[i] = t1;
		    		  //L2[i].setHdr((short) in2_len, _in2, t2_str_sizescopy);
		    		  i++;
		    	  }
		    	  i--;
					//System.out.println("L2 content : ");
					//print_tuples(L2);

	    	  }
	    	  
	      }

		
      for (int i=0;i<nrows;i++)
      {
    	  permutation_array[i]=get_L1_position(L2[i],nrows);  
      }
      //System.out.println("Permutation array: "+java.util.Arrays.toString(permutation_array)); 
      //System.out.println("Bit array: 	   "+java.util.Arrays.toString(bit_array));
      //System.out.println();
      
      //AttrType[] jtype1 = { new AttrType(AttrType.attrString), new AttrType(AttrType.attrString) };
      AttrType[] jtype1 = { new AttrType(AttrType.attrInteger), new AttrType(AttrType.attrInteger) };
      int output_count = 0;
      if(bloom_filter_flag)
      { 
      for (int p=0;p<nrows;p++) // permutation array index is p
      {
    	  bit_array[permutation_array[p]]= true;
    	  bloom_filter[get_bloom_filter_pos(permutation_array[p], nrows)]=true;
    	  for (int b=permutation_array[p]+ eqOffset;b<nrows;b++) // bit array index is b // this value 1 is eqoff 
    	  {
    		  if (bit_array[b]== true)
    		  {
    			  //if(is_duplicate(L2[p], L1[b]) == false) {
    				  Projection.Join(L1[b], _in2, 
							  L2[p], _in2, 
							  Jtuple, perm_mat, nOutFlds);
					  //System.out.print("**************Returned joined tuple : ");
					  //Jtuple.print(jtype1);
					  output_count++;
    			  //}	    
    		  } else {
    			  if(bloom_filter[get_bloom_filter_pos(permutation_array[p], nrows)]==false) {
    				  b = b + nrows/bloom_filter.length;
    			  }
    			  
    		  }
    	  }
    	  //System.out.println("Permutation array: "+p+" "+java.util.Arrays.toString(permutation_array)); 
          //System.out.println("Bit array: 	   "+permutation_array[p]+" "+java.util.Arrays.toString(bit_array));
          //System.out.println();
      }
      }
      else
      {
    	   for (int p=0;p<nrows;p++) // permutation array index is p
    	      {
    	    	  bit_array[permutation_array[p]]= true;
    	//    	  bloom_filter[get_bloom_filter_pos(permutation_array[p], nrows)]=true;
    	    	  for (int b=permutation_array[p]+ eqOffset;b<nrows;b++) // bit array index is b // this value 1 is eqoff 
    	    	  {
    	    		  if (bit_array[b]== true)
    	    		  {
    	    			  //if(is_duplicate(L2[p], L1[b]) == false) {
    	    				  Projection.Join(L1[b], _in2, 
    								  L2[p], _in2, 
    								  Jtuple, perm_mat, nOutFlds);
    						  //System.out.print("**************Returned joined tuple : ");
    						  //Jtuple.print(jtype1);
    						  output_count++;
    	    			  //}	    
    	    		  } 
    	    	  }
    	    	  //System.out.println("Permutation array: "+p+" "+java.util.Arrays.toString(permutation_array)); 
    	          //System.out.println("Bit array: 	   "+permutation_array[p]+" "+java.util.Arrays.toString(bit_array));
    	          //System.out.println();
    	      }  
      }
      
      
      
      
      System.out.println("Output Tuple count: " + output_count);
}
  
  public int get_bloom_filter_pos (int val,int total_elements)
  {
	  int window = (int) Math.ceil(Double.parseDouble(""+total_elements)/Double.parseDouble(""+bloom_filter.length));
	  int j=0;
	  for (int i=0;i<window;i++)
	  {
//		  System.out.println("i="+i);
//		  System.out.println("j="+j);
//		  System.out.println("window="+window);
		  if (val>=j && val<j+window)
			  return i;
		  
		  j=j+window;
		 
	  }
	  return 0;
  }
  
  public Tuple get_next()
    throws IOException,
	   JoinsException ,
	   IndexException,
	   InvalidTupleSizeException,
	   InvalidTypeException, 
	   PageNotReadException,
	   TupleUtilsException, 
	   PredEvalException,
	   SortException,
	   LowMemException,
	   UnknowAttrType,
	   UnknownKeyTypeException,
	   Exception
    {
      // This is a DUMBEST form of a join, not making use of any key information...
	  AttrType[] jtype = { new AttrType(AttrType.attrString), new AttrType(AttrType.attrString) };
      
      if (done)
	return null;
      
      do
	{
	  // If get_from_outer is true, Get a tuple from the outer, delete
	  // an existing scan on the file, and reopen a new scan on the file.
	  // If a get_next on the outer returns DONE?, then the nested loops
	  //join is done too.
	  
	  if (get_from_outer == true)
	    {
	      get_from_outer = false;
	      if (inner != null)     // If this not the first time,
		{
		  // close scan
		  inner = null;
		}
	    
	      try {
		inner = hf.openScan();
	      }
	      catch(Exception e){
		throw new NestedLoopException(e, "openScan failed");
	      }
	      
	      if ((outer_tuple=outer.get_next()) == null)
		{
		  done = true;
		  if (inner != null) 
		    { 
		      inner = null;
		    }
		  
		  return null;
		}   
	    }  // ENDS: if (get_from_outer == TRUE)
	 
	  
	  // The next step is to get a tuple from the inner,
	  // while the inner is not completely scanned && there
	  // is no match (with pred),get a tuple from the inner.
	  
	 
	      RID rid = new RID();
	      while ((inner_tuple = inner.getNext(rid)) != null)
		{
	    	
	    	  
		  inner_tuple.setHdr((short)nrows, _in2,t2_str_sizescopy);
		  
		  AttrType [] test1 = {
	    	      new AttrType(AttrType.attrInteger),
	    	      new AttrType(AttrType.attrString),
	    	      new AttrType(AttrType.attrInteger),
	    	      new AttrType(AttrType.attrReal)
	    };
	    	
//		    System.out.println("Inner tupple");
	//	    inner_tuple.print(test1);
		//    System.out.println("Outer tupple");
		  //  outer_tuple.print(test1); 
		    
		    
		  if (PredEval.Eval(RightFilter, inner_tuple, null, _in2, null) == true)
		    {
		      if (PredEval.Eval(OutputFilter, outer_tuple, inner_tuple, _in1, _in2) == true)
			{
			  // Apply a projection on the outer and inner tuples.
			  Projection.Join(outer_tuple, _in1, 
					  inner_tuple, _in2, 
					  Jtuple, perm_mat, nOutFlds);
			  System.out.println("**************Returned joined tuple*************");
			  //Jtuple.print(jtype);
			}
		    }
		}
	      
	      // There has been no match. (otherwise, we would have 
	      //returned from t//he while loop. Hence, inner is 
	      //exhausted, => set get_from_outer = TRUE, go to top of loop
	      
	      get_from_outer = true; // Loop back to top and get next outer tuple.	      
	} while (true);
    } 
 
  /**
   * implement the abstract method close() from super class Iterator
   *to finish cleaning up
   *@exception IOException I/O error from lower layers
   *@exception JoinsException join error from lower layers
   *@exception IndexException index access error 
   */
  public void close() throws JoinsException, IOException,IndexException 
    {
      if (!closeFlag) {
	
	try {
	  outer.close();
	}catch (Exception e) {
	  throw new JoinsException(e, "IESelfJoin.java: error in closing iterator.");
	}
	closeFlag = true;
      }
    }
}