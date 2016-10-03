package tests;
//originally from : joins.C


import iterator.*;
import heap.*;
import global.*;
import index.*;
import java.io.*;
import java.util.*;
import java.lang.*;
import diskmgr.*;
import bufmgr.*;
import btree.*; 
import catalog.*;

/**
   Here is the implementation for the tests. There are N tests performed.
   We start off by showing that each operator works on its own.
   Then more complicated trees are constructed.
   As a nice feature, we allow the user to specify a selection condition.
   We also allow the user to hardwire trees together.
*/

//Define the Sailor schema
class Sailor {
  public int s_1;
  public int s_2;
  public int s_3;
  public int s_4;
  
  public Sailor (int sid1, int sid2, int sid3,int sid4) {
    s_1  = sid1;
    s_2  = sid2;
    s_3 =  sid3;
    s_4 =  sid4;
  }
}



//Define the Reserves schema
class Reserves {
  public int    r_1;
  public int    r_2;
  public int 	r_3;
  public int	r_4;
  
  public Reserves (int rid1, int rid2, int rid3,int rid4) {
    r_1  = rid1;
    r_2  = rid2;
    r_3 =  rid3;
    r_4 =  rid4;
  }
}

class JoinsDriver implements GlobalConst {
  
  private boolean OK = true;
  private boolean FAIL = false;
  private Vector sailors;
  private Vector reserves;
  /** Constructor
 * @throws IOException 
   */
  public JoinsDriver() throws IOException {
    
    //build Sailor, Reserves table from files R.txt and S.txt
    sailors  = new Vector();
    reserves = new Vector();
    
    LineNumberReader  lnr = new LineNumberReader(new FileReader(new File("R.txt")));
    lnr.skip(Long.MAX_VALUE);
    int number_of_records=lnr.getLineNumber() + 1;
    System.out.println("R table has records : "+(number_of_records)); //Add 1 because line index starts at 0
    lnr.close();
    
    
   int u= (int) Math.floor(number_of_records*0.05);
   int randomR[]=new int[u];
   
    ArrayList<Integer> listl = new ArrayList<Integer>();
   for (int i=1; i<number_of_records+1; i++) {
       listl.add(new Integer(i));
   }
   Collections.shuffle(listl);
   System.out.println("Random number generation");
   for (int i=0; i<u; i++) {
	   randomR[i]=listl.get(i);
  //     System.out.println(randomR[i]);
   }
  
   Arrays.sort(randomR);
 //  System.out.println("RandomRRR");
   System.out.println(java.util.Arrays.toString(randomR));
 
    
    try {
    String fileName1 = "R.txt";
    FileReader fileReader1 = new FileReader(fileName1);
    BufferedReader bufferedReader1 = new BufferedReader(fileReader1);

	// Reading Reserves data into reserves vector
    String line = null;
    int counter=0;
    int u1=1;
    readR:   while((line = bufferedReader1.readLine()) != null  )   {
    	//System.out.println("Counter"+counter);
    	if(u1 == randomR[counter] )
    	{
    	System.out.println("Rtable");
    	System.out.println("Line "+line);
    	String[] temp = line.split(",");
	int[] arr = new int[4];
	for (int i = 0;i<4;i++){
		arr[i] = Integer.parseInt(temp[i]);	
	}
	reserves.addElement(new Reserves(arr[0],arr[1],arr[2],arr[3]));
	++counter;
	if(counter==randomR.length)
		break readR;
	
    }
    ++u1;
    }
    //System.out.println("data" + ((Reserves)reserves.get(0)).r_1);
    bufferedReader1.close();         
}
    catch(FileNotFoundException ex) {
    	System.out.println("Unable to open file  R.txt");                
    }
    catch(IOException ex) {
    	System.out.println("Error reading file R.txt");                  
    }

	// Reading Sailors data into sailors vector
    LineNumberReader  lnrs = new LineNumberReader(new FileReader(new File("S.txt")));
    lnrs.skip(Long.MAX_VALUE);
    int number_of_recordsS=lnrs.getLineNumber() + 1;
    System.out.println("S table has records : "+(number_of_recordsS)); //Add 1 because line index starts at 0
    lnrs.close();
    
    int v= (int) Math.floor(number_of_recordsS*0.05);
    int randomS[]=new int[v];
    
     ArrayList<Integer> listS = new ArrayList<Integer>();
    for (int i=1; i<number_of_recordsS+1; i++) {
        listS.add(new Integer(i));
    }
    Collections.shuffle(listS);
    System.out.println("Random number generation");
    for (int i=0; i<v; i++) {
 	   randomS[i]=listS.get(i);
   //     System.out.println(randomR[i]);
    }
   
    Arrays.sort(randomS);
  //  System.out.println("RandomRRR");
    System.out.println(java.util.Arrays.toString(randomS));
  

    try {
	String fileName2 = "S.txt";
        FileReader fileReader2 = new FileReader(fileName2);
        BufferedReader bufferedReader2 = new BufferedReader(fileReader2);
        String line2;
        line2 = null;
       
        int counterS=0;
        int v1=1;
        readS:  while((line2 = bufferedReader2.readLine()) != null ) 
        {
         	if(v1 == randomS[counterS] )
        	{
        //	System.out.println("Stable");
        	System.out.println("Line "+line2);

          //System.out.println("line "+ line2 );
    	String[] temp1 = line2.split(",");
	int[] arr1 = new int[4];
	for (int i = 0;i<4;i++){
		arr1[i] = Integer.parseInt(temp1[i]);	
	}
	sailors.addElement(new Sailor(arr1[0],arr1[1],arr1[2],arr1[3]));
	++counterS;
	if(counterS==randomS.length)
		break readS;
	
    }
    ++v1;
    }
   // System.out.println("data" + ((Sailors)sailors.get(0)).s_1);
    bufferedReader2.close();         
}
    catch(FileNotFoundException ex) {
    	System.out.println("Unable to open file  S.txt ");                
    }
    catch(IOException ex) {
    	System.out.println("Error reading file S.txt");                  
    }    
    


    boolean status = OK;
    int numsailors = v;
    System.out.println("s "+numsailors);
    int numsailors_attrs = 4;
    int numreserves = u;
    System.out.println("r "+numreserves);
    int numreserves_attrs = 4;

    
    String dbpath = "/tmp/"+System.getProperty("user.name")+".minibase.jointestdb"; 
    String logpath = "/tmp/"+System.getProperty("user.name")+".joinlog";

    String remove_cmd = "/bin/rm -rf ";
    String remove_logcmd = remove_cmd + logpath;
    String remove_dbcmd = remove_cmd + dbpath;
    String remove_joincmd = remove_cmd + dbpath;

    try {
      Runtime.getRuntime().exec(remove_logcmd);
      Runtime.getRuntime().exec(remove_dbcmd);
      Runtime.getRuntime().exec(remove_joincmd);
    }
    catch (IOException e) {
      System.err.println (""+e);
    }

   
    /*
    ExtendedSystemDefs extSysDef = 
      new ExtendedSystemDefs( "/tmp/minibase.jointestdb", "/tmp/joinlog",
			      1000,500,200,"Clock");
    */

    SystemDefs sysdef = new SystemDefs( dbpath, 1000, NUMBUF, "Clock" );
    
    // creating the sailors relation
    AttrType [] Stypes = new AttrType[4];
    Stypes[0] = new AttrType (AttrType.attrInteger);
    Stypes[1] = new AttrType (AttrType.attrInteger);
    Stypes[2] = new AttrType (AttrType.attrInteger);
    Stypes[3] = new AttrType (AttrType.attrInteger);

    //SOS
    short [] Ssizes = new short [1];
    Ssizes[0] = 0; //first elt. is 30
    
    Tuple t = new Tuple();
    try {
      t.setHdr((short) 4,Stypes, Ssizes);
    }
    catch (Exception e) {
      System.err.println("*** error in Tuple.setHdr() ***");
      status = FAIL;
      e.printStackTrace();
    }
    
    int size = t.size();
    
    // inserting the tuple into file "sailors"
    RID             rid;
    Heapfile        f = null;
    try {
      f = new Heapfile("sailors.in");
    }
    catch (Exception e) {
      System.err.println("*** error in Heapfile constructor ***");
      status = FAIL;
      e.printStackTrace();
    }
    
    t = new Tuple(size);
    try {
      t.setHdr((short) 4, Stypes, Ssizes);
    }
    catch (Exception e) {
      System.err.println("*** error in Tuple.setHdr() ***");
      status = FAIL;
      e.printStackTrace();
    }
    
    for (int i=0; i<numsailors; i++) {
      try {
	t.setIntFld(1, ((Sailor)sailors.elementAt(i)).s_1);
	t.setIntFld(2, ((Sailor)sailors.elementAt(i)).s_2);
	t.setIntFld(3, ((Sailor)sailors.elementAt(i)).s_3);
	t.setIntFld(4, ((Sailor)sailors.elementAt(i)).s_4);
      }
      catch (Exception e)
       {
	//System.err.println("*** Heapfile error in Tuple.setStrFld() ***");
	status = FAIL;
	e.printStackTrace();
      }
      
      try {
	rid = f.insertRecord(t.returnTupleByteArray());
      }
      catch (Exception e) {
	System.err.println("*** error in Heapfile.insertRecord() ***");
	status = FAIL;
	e.printStackTrace();
      }      
    }
    if (status != OK) {
      //bail out
      System.err.println ("*** Error creating relation for sailors");
      Runtime.getRuntime().exit(1);
    }
    
   
    //creating the Reserves relation
    AttrType [] Rtypes = new AttrType[4];
    Rtypes[0] = new AttrType (AttrType.attrInteger);
    Rtypes[1] = new AttrType (AttrType.attrInteger);
    Rtypes[2] = new AttrType (AttrType.attrInteger);
    Rtypes[3] = new AttrType (AttrType.attrInteger);

    short [] Rsizes = new short [1];
    Rsizes[0] = 0; 
    t = new Tuple();
    try {
      t.setHdr((short) 4,Rtypes, Rsizes);
    }
    catch (Exception e) {
      System.err.println("*** error in Tuple.setHdr() ***");
      status = FAIL;
      e.printStackTrace();
    }
    
    size = t.size();
    
    // inserting the tuple into file "reserves"
    //RID             rid;
    f = null;
    try {
      f = new Heapfile("reserves.in");
    }
    catch (Exception e) {
      System.err.println("*** error in Heapfile constructor ***");
      status = FAIL;
      e.printStackTrace();
    }
    
    t = new Tuple(size);
    try {
      t.setHdr((short) 4, Rtypes, Rsizes);
    }
    catch (Exception e) {
      System.err.println("*** error in Tuple.setHdr() ***");
      status = FAIL;
      e.printStackTrace();
    }
    
    for (int i=0; i<numreserves; i++) {
      try {
	t.setIntFld(1, ((Reserves)reserves.elementAt(i)).r_1);
	t.setIntFld(2, ((Reserves)reserves.elementAt(i)).r_2);
	t.setIntFld(3, ((Reserves)reserves.elementAt(i)).r_3);
	t.setIntFld(4, ((Reserves)reserves.elementAt(i)).r_4);

      }
      catch (Exception e) {
	System.err.println("*** error in Tuple.setStrFld() ***");
	status = FAIL;
	e.printStackTrace();
      }      
      
      try {
	rid = f.insertRecord(t.returnTupleByteArray());
      }
      catch (Exception e) {
	System.err.println("*** error in Heapfile.insertRecord() ***");
	status = FAIL;
	e.printStackTrace();
      }      
    }
    if (status != OK) {
      //bail out
      System.err.println ("*** Error creating relation for reserves");
      Runtime.getRuntime().exit(1);
    }
    
  }
  
  public boolean runTests() {
    
    Disclaimer();

    //Query1();
    Query2();
    
    System.out.print ("Finished joins testing"+"\n");
   
    
    return true;
  }


  public void Query1_CondExpr(CondExpr[] expr)
  {
    expr[0].next  = null;
    expr[0].op    = new AttrOperator(AttrOperator.aopLT);
    expr[0].type1 = new AttrType(AttrType.attrSymbol);
    expr[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),3);
    expr[0].type2 = new AttrType(AttrType.attrSymbol);
    expr[0].operand2.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),3);
    expr[1] = null;
  }

  public void Query2_CondExpr(CondExpr[] expr,CondExpr[] expr2)
  {
    expr[0].next  = null;
    expr[0].op    = new AttrOperator(AttrOperator.aopLT);
    expr[0].type1 = new AttrType(AttrType.attrSymbol);
    expr[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),3);
    expr[0].type2 = new AttrType(AttrType.attrSymbol);
    expr[0].operand2.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),3);
    expr[1] = null;

    expr2[0].next  = null;
    expr2[0].op    = new AttrOperator(AttrOperator.aopLT);
    expr2[0].type1 = new AttrType(AttrType.attrSymbol);
    expr2[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),3);
    expr2[0].type2 = new AttrType(AttrType.attrSymbol);
    expr2[0].operand2.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),3);
    expr2[1] = null;
    

  }

  public void Query1()
  {
    boolean status= OK;

    //System.out.println("Query 1a Select R_1, S_1 from R, S where R_3=S_3");

    CondExpr[] outFilter = new CondExpr[2];
    outFilter[0]=new CondExpr();
    outFilter[1]= new CondExpr();

    Query1_CondExpr(outFilter);

    Tuple t = new Tuple();

    t=null;

    AttrType Stypes[] = {
      new AttrType(AttrType.attrInteger),
      new AttrType(AttrType.attrInteger),
      new AttrType(AttrType.attrInteger),
      new AttrType(AttrType.attrInteger)
    };
    short []   Ssizes = new short[1];
    Ssizes[0] = 0;

    AttrType [] Rtypes = {
      new AttrType(AttrType.attrInteger),
      new AttrType(AttrType.attrInteger),
      new AttrType(AttrType.attrInteger),
      new AttrType(AttrType.attrInteger)
    };
    short  []  Rsizes = new short[1];
    Rsizes[0] =0;

    FldSpec [] Rprojection = {
       new FldSpec(new RelSpec(RelSpec.outer), 1),
       new FldSpec(new RelSpec(RelSpec.outer), 2),
       new FldSpec(new RelSpec(RelSpec.outer), 3),
       new FldSpec(new RelSpec(RelSpec.outer), 4)
    };


    CondExpr[] selects = new CondExpr [1];
    selects = null;

    iterator.Iterator am = null;
    try 
    {
          am  = new FileScan("reserves.in", Rtypes, Rsizes,
          (short)4, (short) 4,
          Rprojection, null);
    }
    catch (Exception e) 
    {
      status = FAIL;
      System.err.println (""+e);
    }
 
    if (status != OK) 
    {
      //bail out
      System.err.println ("*** Error setting up scan for Reserves");
      Runtime.getRuntime().exit(1);
    }

    FldSpec [] Sprojection = {
       new FldSpec(new RelSpec(RelSpec.outer), 1),
       new FldSpec(new RelSpec(RelSpec.outer), 2),
       new FldSpec(new RelSpec(RelSpec.outer), 3),
       new FldSpec(new RelSpec(RelSpec.outer), 4)
    };


    iterator.Iterator am2 = null;
    try
    {
      am2 = new FileScan("sailors.in", Stypes, Ssizes, 
          (short)4, (short)4,
          Sprojection, null);
    }
    catch (Exception e) 
    {
      status = FAIL;
      System.err.println (""+e);
    }
    
    if (status != OK) 
    {
      //bail out
      System.err.println ("*** Error setting up scan for sailors");
      Runtime.getRuntime().exit(1);
    }


     FldSpec [] proj_list = {
      new FldSpec(new RelSpec(RelSpec.outer), 1),
      new FldSpec(new RelSpec(RelSpec.innerRel), 1)
       };


    AttrType [] jtype = new AttrType[2];
    jtype[0] = new AttrType (AttrType.attrInteger);
    jtype[1] = new AttrType (AttrType.attrInteger);


    NestedLoopsJoins nl1= null;
    try
    {
      nl1= new NestedLoopsJoins(Rtypes, 4, Rsizes,   
                                Stypes, 4, Ssizes,
                                10, am, "sailors.in",
                                outFilter, null, proj_list,2);
    }

  catch (Exception e) 
  {
  
  System.err.println ("*** Error preparing for nested_loop_join");
  System.err.println (""+e);
  e.printStackTrace();
  Runtime.getRuntime().exit(1);
  
  }

  if(status!=OK)
  {
    System.err.println("error constructing nested loop !");
    Runtime.getRuntime().exit(1);
  }

  t = null;

  try
  {
    while((t=nl1.get_next())!= null )
    {
      t.print(jtype);
    }
  }
  catch(Exception e)
  {
    System.err.println(""+ e);
    e.printStackTrace();
    Runtime.getRuntime().exit(1);

  }


    System.out.println ("\n"); 
    try 
    {
      nl1.close();
    }
    catch (Exception e) 
    {
      status = FAIL;
      e.printStackTrace();
    }
    
    if (status != OK) 
    {
      //bail out
      System.err.println ("*** Error setting up scan for reserves");
      Runtime.getRuntime().exit(1);
    }

  }



  public void Query2()
  {
    boolean status= OK;

    System.out.println("Query 1a Select R_1, S_1 from R, S where R_3 < S_3 and R_4 <= S_4");

    CondExpr[] outFilter = new CondExpr[2];
    outFilter[0]=new CondExpr();
    outFilter[1]= new CondExpr();

    CondExpr[] outFilter2 = new CondExpr[2];
    outFilter2[0]=new CondExpr();
    outFilter2[1]= new CondExpr();

    Query2_CondExpr(outFilter, outFilter2);

    Tuple t = new Tuple();

    t=null;

    AttrType Stypes[] = {
      new AttrType(AttrType.attrInteger),
      new AttrType(AttrType.attrInteger),
      new AttrType(AttrType.attrInteger),
      new AttrType(AttrType.attrInteger)
    };
    short []   Ssizes = new short[1];
    Ssizes[0] = 0;

    AttrType [] Rtypes = {
      new AttrType(AttrType.attrInteger),
      new AttrType(AttrType.attrInteger),
      new AttrType(AttrType.attrInteger),
      new AttrType(AttrType.attrInteger)
    };
    short  []  Rsizes = new short[1];
    Rsizes[0] =0;

    FldSpec [] Rprojection = {
       new FldSpec(new RelSpec(RelSpec.outer), 1),
       new FldSpec(new RelSpec(RelSpec.outer), 2),
       new FldSpec(new RelSpec(RelSpec.outer), 3),
       new FldSpec(new RelSpec(RelSpec.outer), 4)
    };


    CondExpr[] selects = new CondExpr [1];
    selects = null;

    iterator.Iterator am = null;
    try 
    {
          am  = new FileScan("reserves.in", Rtypes, Rsizes,
          (short)4, (short) 4,
          Rprojection, null);
    }
    catch (Exception e) 
    {
      status = FAIL;
      System.err.println (""+e);
    }
 
    if (status != OK) 
    {
      System.err.println ("*** Error setting up scan for Reserves");
      Runtime.getRuntime().exit(1);
    }

    FldSpec [] Sprojection = {
       new FldSpec(new RelSpec(RelSpec.outer), 1),
       new FldSpec(new RelSpec(RelSpec.outer), 2),
       new FldSpec(new RelSpec(RelSpec.outer), 3),
       new FldSpec(new RelSpec(RelSpec.outer), 4)
    };


    iterator.Iterator am2 = null;
    try
    {
      am2 = new FileScan("sailors.in", Stypes, Ssizes, 
          (short)4, (short)4,
          Sprojection, null);
    }
    catch (Exception e) 
    {
      status = FAIL;
      System.err.println (""+e);
    }
    
    if (status != OK) 
    {
      //bail out
      System.err.println ("*** Error setting up scan for sailors");
      Runtime.getRuntime().exit(1);
    }


     FldSpec [] proj_list = {
      new FldSpec(new RelSpec(RelSpec.outer), 1),
      new FldSpec(new RelSpec(RelSpec.innerRel), 1),
      new FldSpec(new RelSpec(RelSpec.outer), 3)
      
      
       };


    AttrType [] jtype = new AttrType[3];
    jtype[0] = new AttrType (AttrType.attrInteger);
    jtype[1] = new AttrType (AttrType.attrInteger);
    jtype[2] = new AttrType (AttrType.attrInteger);
    


    FldSpec [] proj_list2 = {
      new FldSpec(new RelSpec(RelSpec.outer), 1),
      new FldSpec(new RelSpec(RelSpec.outer), 2)
       };


    AttrType [] jtype2 = new AttrType[2];
    jtype2[0] = new AttrType (AttrType.attrInteger);
    jtype2[1] = new AttrType (AttrType.attrInteger);


    NestedLoopsJoins nl1= null;
    try
    {
      nl1= new NestedLoopsJoins(Rtypes, 4, Rsizes,   
                                Stypes, 4, Ssizes,
                                200, am, "sailors.in",
                                outFilter, null, proj_list,2);
    }

  catch (Exception e) 
  {
  
  System.err.println ("*** Error preparing for nested_loop_join");
  System.err.println (""+e);
  e.printStackTrace();
  Runtime.getRuntime().exit(1);
  
  }

  if(status!=OK)
  {
    System.err.println("error constructing nested loop !");
    Runtime.getRuntime().exit(1);
  }

  t = null;

  try
  {
    while((t=nl1.get_next())!= null )
    {
      t.print(jtype);
    }
  }
  catch(Exception e)
  {
    System.err.println(""+ e);
    e.printStackTrace();
    Runtime.getRuntime().exit(1);

  }


    System.out.println ("\n"); 
    try 
    {
      nl1.close();
    }
    catch (Exception e) 
    {
      status = FAIL;
      e.printStackTrace();
    }
    
    if (status != OK) 
    {
      //bail out
      System.err.println ("*** Error setting up scan for reserves");
      Runtime.getRuntime().exit(1);
    }

    short  []  Jsizes    = new short[1];
    Jsizes[0] = 0;
    NestedLoopsJoins nl2= null;
    try
    {
      nl2= new NestedLoopsJoins(jtype, 3, Jsizes,   
                                Stypes, 4, Ssizes,
                                200, nl1, "sailors.in",
                                outFilter2, null, proj_list2,2);
    }

  catch (Exception e) 
  {
  
  System.err.println ("*** Error preparing for nested_loop_join2");
  System.err.println (""+e);
  e.printStackTrace();
  Runtime.getRuntime().exit(1);
  
  }

  if(status!=OK)
  {
    System.err.println("error constructing nested loop2 !");
    Runtime.getRuntime().exit(1);
  }

  t = null;

  try
  {
    while((t=nl2.get_next())!= null )
    {
      t.print(jtype2);
    }
  }
  catch(Exception e)
  {
    System.err.println(""+ e);
    e.printStackTrace();
    Runtime.getRuntime().exit(1);

  }


    System.out.println ("\n"); 
    try 
    {
      nl2.close();
    }
    catch (Exception e) 
    {
      status = FAIL;
      e.printStackTrace();
    }
    
    if (status != OK) 
    {
      //bail out
      System.err.println ("*** Error setting up scan for reserves");
      Runtime.getRuntime().exit(1);
    }

  }
  
  
  private void Disclaimer() {
    /*System.out.print ("\n\nAny resemblance of persons in this database to"
         + " people living or dead\nis purely coincidental. The contents of "
         + "this database do not reflect\nthe views of the University,"
         + " the Computer  Sciences Department or the\n"
         + "developers...\n\n");*/
  }
}

public class JoinTest
{
  public static void main(String argv[]) throws IOException
  {
    boolean sortstatus;
    //SystemDefs global = new SystemDefs("bingjiedb", 100, 70, null);
    //JavabaseDB.openDB("/tmp/nwangdb", 5000);

    JoinsDriver jjoin = new JoinsDriver();

    sortstatus = jjoin.runTests();
    if (sortstatus != true) {
      System.out.println("Error ocurred during join tests");
    }
    else {
      System.out.println("join tests completed successfully");
    }
  }
}

