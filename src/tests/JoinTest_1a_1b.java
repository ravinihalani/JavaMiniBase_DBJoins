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
class Sailor_1 {
  public int s_1;
  public int s_2;
  public int s_3;
  public int s_4;
  
  public Sailor_1 (int sid1, int sid2, int sid3,int sid4) {
    s_1  = sid1;
    s_2  = sid2;
    s_3 =  sid3;
    s_4 =  sid4;
  }
}



//Define the Reserves schema
class Reserves_1 {
  public int    r_1;
  public int    r_2;
  public int 	r_3;
  public int	r_4;
  
  public Reserves_1 (int rid1, int rid2, int rid3,int rid4) {
    r_1  = rid1;
    r_2  = rid2;
    r_3 =  rid3;
    r_4 =  rid4;
  }
}

class JoinsDriver_1 implements GlobalConst {
  
  private boolean OK = true;
  private boolean FAIL = false;
  private Vector sailors;
  private Vector reserves;
  /** Constructor
   */
  public JoinsDriver_1() {
    
    //build Sailor, Reserves table from files R.txt and S.txt
    sailors  = new Vector();
    reserves = new Vector();
    int rlines = 0;
    try {
        FileInputStream fstream = null;
    	try {
    		fstream = new FileInputStream("R.txt");
    	} catch (FileNotFoundException e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    	}
        BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(fstream));	
    
	// Reading Reserves data into reserves vector
    String line = null;
    boolean firstLine = true;
    while((line = bufferedReader1.readLine()) != null) {
      if(firstLine) {
    	  firstLine = false;
    	  continue;
      }
    	
    	rlines++;
    	String[] temp = line.split(",");
	int[] arr = new int[4];
	for (int i = 0;i<4;i++){
		arr[i] = Integer.parseInt(temp[i]);	
	}
	reserves.addElement(new Reserves_1(arr[0],arr[1],arr[2],arr[3]));
    }
    //System.out.println("data" + ((Reserves_1)reserves.get(0)).r_1);
    bufferedReader1.close();         
}
    catch(FileNotFoundException ex) {
    	System.out.println("Unable to open file  R.txt");                
    }
    catch(IOException ex) {
    	System.out.println("Error reading file R.txt");                  
    }

	// Reading Sailors data into sailors vector
	int slines = 0;
    try {
        FileInputStream fstream = null;
    	try {
    		fstream = new FileInputStream("S.txt");
    	} catch (FileNotFoundException e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    	}
        BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(fstream));	        
        String line2;
        line2 = null;
        boolean firstLine = true;
        while((line2 = bufferedReader2.readLine()) != null) 
        {
        	if(firstLine) {
        		firstLine = false;
        		continue;
        	}
        
          slines++;
          //System.out.println("line "+ line2 );
    	String[] temp1 = line2.split(",");
	int[] arr1 = new int[4];
	for (int i = 0;i<4;i++){
		arr1[i] = Integer.parseInt(temp1[i]);	
	}
	sailors.addElement(new Sailor_1(arr1[0],arr1[1],arr1[2],arr1[3]));
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
    int numsailors = slines;
    int numsailors_attrs = 4;
    int numreserves = rlines;
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
	t.setIntFld(1, ((Sailor_1)sailors.elementAt(i)).s_1);
	t.setIntFld(2, ((Sailor_1)sailors.elementAt(i)).s_2);
	t.setIntFld(3, ((Sailor_1)sailors.elementAt(i)).s_3);
	t.setIntFld(4, ((Sailor_1)sailors.elementAt(i)).s_4);
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
	t.setIntFld(1, ((Reserves_1)reserves.elementAt(i)).r_1);
	t.setIntFld(2, ((Reserves_1)reserves.elementAt(i)).r_2);
	t.setIntFld(3, ((Reserves_1)reserves.elementAt(i)).r_3);
	t.setIntFld(4, ((Reserves_1)reserves.elementAt(i)).r_4);

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

  String fileName = "Query.txt";
  String line  = null;
  String line2 = null;
  int par = 0;
      int numlines = 0;
      
      
      String[] out = new String[20];
       int nselectitems=-1;
        int svar_items=0;

        int oper1=-1;
        int oper2=-1;
       int[] var_id = new int[6];
        int mad=0;
   
  try {
	  
	    FileInputStream fstream = null;
		try {
			fstream = new FileInputStream("Query.txt");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    BufferedReader bufferedReader  = new BufferedReader(new InputStreamReader(fstream));
      System.out.println("Query");
      while((line = bufferedReader.readLine()) != null) 
      {
        numlines++;
        System.out.print(line + "  ");      
        String[] ip = line.split(" ");
         
          int temp = 0;
            while(temp < ip.length)
            {
              out[par] = ip[temp];
              temp = temp + 1;
              par = par+1;  
            }
      }


    //  System.out.println("numbers of lines "+numlines);

      
      while(out[mad]!=null)
      {
        mad++;
      }

     // System.out.println("numbers in that would be "+mad);

      String[] q_array = new String[mad];

      for(int i=0;i<mad; i++)
      {
        q_array[i]=out[i];

      }


      //for(int k=0;k<q_array.length;k++)
        //   System.out.println("q_array "+q_array[k]);


       
        if(q_array.length ==6 )
        {
          nselectitems=1;
          svar_items=3;
          oper1=Integer.parseInt(q_array[q_array.length-2]);

        }
        else if(q_array.length ==7)
        {
          nselectitems=2;
          svar_items = 4;
          oper1=Integer.parseInt(q_array[q_array.length-2]);
        }
        
        if(q_array.length==10)
        {
          nselectitems=1;
          svar_items=5;
          oper1=Integer.parseInt(q_array[4]);
          oper2=Integer.parseInt(q_array[q_array.length-2]);


        }
        else if(q_array.length==11)
        {
          nselectitems=2;
          svar_items = 6;
          oper1=Integer.parseInt(q_array[5]);
          oper2=Integer.parseInt(q_array[q_array.length-2]);
        }

      //  System.out.println("svar_items "+ svar_items );
       // System.out.println("operand 1 "+ oper1 );
       // System.out.println("operand 2 "+ oper2 );



      
      int temp2 = 0;

      
      
     // System.out.println("q_array length" + q_array.length);

      for(int k=0;k<q_array.length;k++)
      {
        

        if(q_array[k].contains("_"))
          {
            String[] q1 = new String[2];
            
            q1 = q_array[k].split("_");
            int q1len = q1.length;
           // for(int i=0; i<q1len;i++)
             // System.out.println("1saa" + q1[i]);

            var_id[temp2] = Integer.parseInt(q1[q1len-1]);
            temp2 = temp2 + 1;
           }

           
      }


      //for(int k = 0;k< var_id.length;k++)
        //System.out.println("***** " + var_id[k]);

           // Always close files.
        bufferedReader.close();         
        } // try end

        catch(FileNotFoundException ex) 
        {
            System.out.println("Unable to open file '" + fileName + "'");                
        }
        catch(IOException ex) 
        {
            System.out.println("Error reading file '" + fileName + "'");                  
            
        }

        if(oper2== -1)
        {
          final long startTime1 = System.currentTimeMillis();
          Query1(nselectitems, var_id, oper1);
          final long endTime1 = System.currentTimeMillis();
          System.out.println("Execution time of Query1 in millisec: " + (endTime1-startTime1));
        }
        else
        {
          final long startTime2 = System.currentTimeMillis();
          Query2(nselectitems,var_id, oper1,oper2);
          final long endTime2 = System.currentTimeMillis();
          System.out.println("Execution time of Query2 in millisec: " + (endTime2-startTime2));

        }
    
    
    
    System.out.print ("Finished joins testing"+"\n");
   
    
    return true;
  }


  public void Query1_CondExpr(CondExpr[] expr, int outernum, int innernum, int oper1)
  {

    
    if(oper1 ==1)
    {
      expr[0].op    = new AttrOperator(AttrOperator.aopLT);
    }
    else if (oper1 == 2) 
    {
      expr[0].op    = new AttrOperator(AttrOperator.aopLE);
    }
    else if(oper1 == 3)
    {
      expr[0].op    = new AttrOperator(AttrOperator.aopGE);
    }

    else if(oper1 == 4)
    {
      expr[0].op    = new AttrOperator(AttrOperator.aopGT);
    }
    else if(oper1 == 5)
    {
      expr[0].op    = new AttrOperator(AttrOperator.aopNE);
    }
    
   

    expr[0].next  = null;
    //expr[0].op    = new AttrOperator(AttrOperator.aopLT);
    expr[0].type1 = new AttrType(AttrType.attrSymbol);
    expr[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),outernum);
    expr[0].type2 = new AttrType(AttrType.attrSymbol);
    expr[0].operand2.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),innernum);
    expr[1] = null;
  }

  public void Query2_CondExpr(CondExpr[] expr,CondExpr[] expr2,int outernum1,int innernum1, int outernum2, int innernum2,int oper1,int oper2)
  {
     
    if(oper1 ==1)
    {
      expr[0].op    = new AttrOperator(AttrOperator.aopLT);
    }
    else if (oper1 == 2) 
    {
      expr[0].op    = new AttrOperator(AttrOperator.aopLE);
    }
    else if(oper1 == 3)
    {
      expr[0].op    = new AttrOperator(AttrOperator.aopGE);
    }

    else if(oper1 == 4)
    {
      expr[0].op    = new AttrOperator(AttrOperator.aopGT);
    }
    else if(oper1 == 5)
    {
      expr[0].op    = new AttrOperator(AttrOperator.aopNE);
    }
    
    

    
    if(oper2 ==1)
    {
      expr2[0].op    = new AttrOperator(AttrOperator.aopLT);
    }
    else if (oper2 == 2) 
    {
      expr2[0].op    = new AttrOperator(AttrOperator.aopLE);
    }
    else if(oper2 == 3)
    {
      expr2[0].op    = new AttrOperator(AttrOperator.aopGE);
    }

    else if(oper2 == 4)
    {
      expr2[0].op    = new AttrOperator(AttrOperator.aopGT);
    }
    else if(oper1 == 5)
    {
      expr2[0].op    = new AttrOperator(AttrOperator.aopNE);
    }
    



    expr[0].next  = null;
    //expr[0].op    = new AttrOperator(AttrOperator.aopLT);
    expr[0].type1 = new AttrType(AttrType.attrSymbol);
    expr[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),outernum1);
    expr[0].type2 = new AttrType(AttrType.attrSymbol);
    expr[0].operand2.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),innernum1);
    expr[1] = null;

    expr2[0].next  = null;
    //expr2[0].op    = new AttrOperator(AttrOperator.aopLE);
    expr2[0].type1 = new AttrType(AttrType.attrSymbol);
    expr2[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),outernum2);
    expr2[0].type2 = new AttrType(AttrType.attrSymbol);
    expr2[0].operand2.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),innernum2);
    expr2[1] = null;
    

  }

  public void Query1(int selectitems, int var[], int oper1 )
  {

   // System.out.println("number of selectitems "+ selectitems+" operand number"+ oper1);


   // for(int i=0;i<var.length;i++)
     // System.out.println("var items"+var[i]);

    boolean status= OK;

    //System.out.println("Query 1a Select R_1, S_1 from R, S where R_3=S_3");

    int outernum;
    int innernum;

    CondExpr[] outFilter = new CondExpr[selectitems];

    if(selectitems==1)
    {

    outernum= var[1];
    innernum= var[2];

    
    outFilter[0]=new CondExpr();
    
    Query1_CondExpr(outFilter, outernum, innernum, oper1);

    }
    else if(selectitems==2)
    {
      System.out.println("enter if selectitems");

    outernum= var[2];
    innernum= var[3];

    
    outFilter[0]=new CondExpr();
    outFilter[1]=new CondExpr();

    Query1_CondExpr(outFilter, outernum, innernum, oper1);

    }
    

    

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

    //scanning "R" data base
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

    FldSpec [] Sprojection = 
    {
       new FldSpec(new RelSpec(RelSpec.outer), 1),
       new FldSpec(new RelSpec(RelSpec.outer), 2),
       new FldSpec(new RelSpec(RelSpec.outer), 3),
       new FldSpec(new RelSpec(RelSpec.outer), 4)
    };

    //Scanning "S" database

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


      // joining, assuming attributes will always be integers 
    AttrType [] jtype = new AttrType[2];
    jtype[0] = new AttrType (AttrType.attrInteger);
    jtype[1] = new AttrType (AttrType.attrInteger);

    // number will be the select id select R_1, S_1 ;

    int selectnumouter;
    int selectnuminner;



    if(selectitems == 1)
    {
      selectnumouter = var[0];

      
      FldSpec [] proj_list = 
      {
        new FldSpec(new RelSpec(RelSpec.outer), selectnumouter)
        
      };

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


  //printing the output
  System.out.println("Printing the output for the Query");
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
      System.err.println ("*** Error setting up scan for reserves");
      Runtime.getRuntime().exit(1);
    }
      
    }

    else
    {
      selectnumouter = var[0];
      selectnuminner = var[1];
      
     FldSpec [] proj_list = 
      {
        new FldSpec(new RelSpec(RelSpec.outer), selectnumouter),
        new FldSpec(new RelSpec(RelSpec.innerRel), selectnuminner)
      }; 

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

   int nreturn=0;
  //printing the output
  System.out.println("Printing the output for the Query");
  try
  {
    while((t=nl1.get_next())!= null )
    {
      nreturn++;
      t.print(jtype);
    }
    System.out.println("Number of tuples returned "+ nreturn);
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
      System.err.println ("*** Error setting up scan for reserves");
      Runtime.getRuntime().exit(1);
    }

    }

    

/*
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
*/

  /*
  t = null;


  //printing the output
  System.out.println("Printing the output for the Query");
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
      System.err.println ("*** Error setting up scan for reserves");
      Runtime.getRuntime().exit(1);
    }

    */

  }



  public void Query2(int selectitems, int[] var, int oper1, int oper2)
  {
    //boolean status= OK;

    // System.out.println("number of selectitems "+ selectitems+" opernad number"+ oper1);


    //for(int i=0;i<var.length;i++)
      //System.out.println("var items"+var[i]);

    boolean status= OK;

    int outernum1=0;
    int innernum1=0;
    int outernum2=0;
    int innernum2=0;


     CondExpr[] outFilter = new CondExpr[2];
     CondExpr[] outFilter2 = new CondExpr[2];

   

    if(selectitems==1)
    {

    outernum1= var[1];
    innernum1= var[2];
    outernum2= var[3];
    innernum2= var[4];



    outFilter[0]=new CondExpr();
    outFilter[1]= new CondExpr();

    
    outFilter2[0]=new CondExpr();
    outFilter2[1]= new CondExpr();
    
    Query2_CondExpr(outFilter, outFilter2, outernum1, innernum1,outernum2,innernum2 ,oper1, oper2);

    }
    else if(selectitems==2)
    {
     // System.out.println("enter if selectitems");

    outernum1= var[2];
    innernum1= var[3];
    outernum2= var[4];
    innernum2= var[5];

    outFilter[0]=new CondExpr();
    outFilter[1]= new CondExpr();

    
    outFilter2[0]=new CondExpr();
    outFilter2[1]= new CondExpr();

    Query2_CondExpr(outFilter, outFilter2 , outernum1, innernum1,outernum2,innernum2 ,oper1, oper2);

    }


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
      new FldSpec(new RelSpec(RelSpec.outer), var[0]),
      new FldSpec(new RelSpec(RelSpec.innerRel), var[1]),
      new FldSpec(new RelSpec(RelSpec.outer), outernum2)
      
      
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
      
      //t.print(jtype);
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
                                10, nl1, "sailors.in",
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
  int nres = 0;
  try
  {
    while((t=nl2.get_next())!= null )
    {
    	nres++;
      t.print(jtype2);
    }
    System.out.println("No. of tuples returned "+ nres);
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

public class JoinTest_1a_1b
{
  public static void main(String argv[])
  {
    boolean sortstatus;
    //SystemDefs global = new SystemDefs("bingjiedb", 100, 70, null);
    //JavabaseDB.openDB("/tmp/nwangdb", 5000);

    JoinsDriver_1 jjoin = new JoinsDriver_1();

    sortstatus = jjoin.runTests();
    if (sortstatus != true) {
      System.out.println("Error ocurred during join tests");
    }
    else {
      System.out.println("join tests completed successfully");
    }
  }
}

