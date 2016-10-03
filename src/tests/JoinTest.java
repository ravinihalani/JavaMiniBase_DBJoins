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
  public int    sid;
  public String sname;
  public int    rating;
  public double age;
  
  public Sailor (int _sid, String _sname, int _rating,double _age) {
    sid    = _sid;
    sname  = _sname;
    rating = _rating;
    age    = _age;
  }
}

class IntegerQuery {
	  public int    integer1;
	  public int 	integer2;
	  public int    integer3;
	  public int 	integer4;
	  
	  public IntegerQuery (int _integer1, int _integer2, int _integer3,int _integer4) {
		  integer1 = _integer1;
		  integer2 = _integer2;
		  integer3 = _integer3;
		  integer4 = _integer4;
	  }
	}

//Define the Boat schema
class Boats {
  public int    bid;
  public String bname;
  public String color;
  
  public Boats (int _bid, String _bname, String _color) {
    bid   = _bid;
    bname = _bname;
    color = _color;
  }
}

//Define the Reserves schema
class Reserves {
  public int    sid;
  public int    bid;
  public String date;
  
  public Reserves (int _sid, int _bid, String _date) {
    sid  = _sid;
    bid  = _bid;
    date = _date;
  }
}

class JoinsDriver implements GlobalConst {
  
  private boolean OK = true;
  private boolean FAIL = false;
  private Vector sailors;
  private Vector boats;
  private Vector reserves;
  private Vector integerQuery;
  private int nrows;
  /** Constructor
   */
  public JoinsDriver() {
    
    //build Sailor, Boats, Reserves table
    sailors  = new Vector();
    boats    = new Vector();
    reserves = new Vector();
    integerQuery = new Vector();
    
    
 // Open the file
    FileInputStream fstream = null;
	try {
		fstream = new FileInputStream("S.txt");
	} catch (FileNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
    BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

    String strLine;
    //integerQuery.addElement(new IntegerQuery(5,5,5,5));
    //Read File Line By Line
    try {
    	boolean isFirstLine = true;
		while ((strLine = br.readLine()) != null)   {
		  // Print the content on the console
		  //System.out.println (strLine);
			if(isFirstLine == false) {
				String[] parts = strLine.split(",");
/*			    System.out.print("Number = " + Integer.parseInt(parts[0]) + " ");
			    System.out.print("Number = " + Integer.parseInt(parts[1]) + " ");
			    System.out.print("Number = " + Integer.parseInt(parts[2]) + " ");
			    System.out.println("Number = " + Integer.parseInt(parts[3]) + " ");*/
				integerQuery.addElement(new IntegerQuery(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3])));
			}
			isFirstLine = false;
		}
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
    //Close the input stream
    try {
		br.close();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
//    Enumeration an=integerQuery.elements();

/*    // let us print all the elements available in enumeration
    System.out.println("Numbers in the enumeration are :- "); 
    while (an.hasMoreElements()) {         
    System.out.println("Number = " + an.nextElement().toString());
    }*/
    
 
    sailors.addElement(new Sailor(404, "Vishnu",       6, 100));
    sailors.addElement(new Sailor(498, "Ravi",       11, 140));
    sailors.addElement(new Sailor(676, "Guru",       10, 80));
    sailors.addElement(new Sailor(742, "Tushar",       5, 90));   
    
    
/*  sailors.addElement(new Sailor(404, "Vishnu",       4, 100));
    sailors.addElement(new Sailor(498, "Ravi",       9, 140));
    sailors.addElement(new Sailor(676, "Guru",       8, 80));
    sailors.addElement(new Sailor(742, "Tushar",       3, 90));*/
/*    
    sailors.addElement(new Sailor(53, "Bob Holloway",       9, 53.6));
    sailors.addElement(new Sailor(57, "Yannis Ioannidis",   8, 40.2));
    sailors.addElement(new Sailor(59, "Deborah Joseph",    10, 39.8));
    sailors.addElement(new Sailor(61, "Landwebber",         8, 56.7));
    sailors.addElement(new Sailor(63, "James Larus",        9, 30.3));
    sailors.addElement(new Sailor(64, "Barton Miller",      5, 43.7));
    sailors.addElement(new Sailor(67, "David Parter",       1, 99.9));   
    sailors.addElement(new Sailor(69, "Raghu Ramakrishnan", 9, 37.1));
    sailors.addElement(new Sailor(71, "Guri Sohi",         10, 42.1));
    sailors.addElement(new Sailor(73, "Prasoon Tiwari",     8, 39.2));
    sailors.addElement(new Sailor(39, "Anne Condon",        3, 30.3));
    sailors.addElement(new Sailor(47, "Charles Fischer",    6, 46.3));
    sailors.addElement(new Sailor(49, "James Goodman",      4, 50.3));
    sailors.addElement(new Sailor(50, "Mark Hill",          5, 35.2));
    sailors.addElement(new Sailor(75, "Mary Vernon",        7, 43.1));
    sailors.addElement(new Sailor(79, "David Wood",         3, 39.2));
    sailors.addElement(new Sailor(84, "Mark Smucker",       9, 25.3));
    sailors.addElement(new Sailor(87, "Martin Reames",     10, 24.1));
    sailors.addElement(new Sailor(10, "Mike Carey",         9, 40.3));
    sailors.addElement(new Sailor(21, "David Dewitt",      10, 47.2));
    sailors.addElement(new Sailor(29, "Tom Reps",           7, 39.1));
    sailors.addElement(new Sailor(31, "Jeff Naughton",      5, 35.0));
    sailors.addElement(new Sailor(35, "Miron Livny",        7, 37.6));
    sailors.addElement(new Sailor(37, "Marv Solomon",      10, 48.9));
*/
    boats.addElement(new Boats(1, "Onion",      "white"));
    boats.addElement(new Boats(2, "Buckey",     "red"  ));
    boats.addElement(new Boats(3, "Enterprise", "blue" ));
    boats.addElement(new Boats(4, "Voyager",    "green"));
    boats.addElement(new Boats(5, "Wisconsin",  "red"  ));
 
    reserves.addElement(new Reserves(10, 1, "05/10/95"));
    reserves.addElement(new Reserves(21, 1, "05/11/95"));
    reserves.addElement(new Reserves(10, 2, "05/11/95"));
    reserves.addElement(new Reserves(31, 1, "05/12/95"));
    reserves.addElement(new Reserves(10, 3, "05/13/95"));
    reserves.addElement(new Reserves(69, 4, "05/12/95"));
    reserves.addElement(new Reserves(69, 5, "05/14/95"));
    reserves.addElement(new Reserves(21, 5, "05/16/95"));
    reserves.addElement(new Reserves(57, 2, "05/10/95"));
    reserves.addElement(new Reserves(35, 3, "05/15/95"));

    boolean status = OK;
    int numsailors = sailors.size();
    int numIntegerRows = integerQuery.size();
    nrows = numIntegerRows;
    //nrows = numsailors;
    //int numsailors = 25;
    int numInteger_attrs = 4;
    int numsailors_attrs = 4;
    int numreserves = 10;
    int numreserves_attrs = 3;
    int numboats = 5;
    int numboats_attrs = 3;
    
    //String dbpath = "C:\\tmp\\"+System.getProperty("user.name")+".minibase.jointestdb"; 
    //String logpath = "C:\\tmp\\"+System.getProperty("user.name")+".joinlog";

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

    SystemDefs sysdef = new SystemDefs( dbpath, 1000000000, NUMBUF, "Clock" );
    
    // creating the integerQuery relation
    AttrType [] Itypes = new AttrType[4];
    Itypes[0] = new AttrType (AttrType.attrInteger);
    Itypes[1] = new AttrType (AttrType.attrInteger);
    Itypes[2] = new AttrType (AttrType.attrInteger);
    Itypes[3] = new AttrType (AttrType.attrInteger);

    //SOS
    short [] Ssizes = new short [1];
    Ssizes[0] = 30; //first elt. is 30
    
    Tuple t = new Tuple();
    try {
      t.setHdr((short) 4,Itypes, Ssizes);
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
      f = new Heapfile("integerQuery.in");
    }
    catch (Exception e) {
      System.err.println("*** error in Heapfile constructor ***");
      status = FAIL;
      e.printStackTrace();
    }
    
    t = new Tuple(size);
    try {
      t.setHdr((short) 4, Itypes, Ssizes);
    }
    catch (Exception e) {
      System.err.println("*** error in Tuple.setHdr() ***");
      status = FAIL;
      e.printStackTrace();
    }
    
    for (int i=0; i<numIntegerRows; i++) {
      try {
	t.setIntFld(1, ((IntegerQuery)integerQuery.elementAt(i)).integer1);
	t.setIntFld(2, ((IntegerQuery)integerQuery.elementAt(i)).integer2);
	t.setIntFld(3, ((IntegerQuery)integerQuery.elementAt(i)).integer3);
	t.setIntFld(4, ((IntegerQuery)integerQuery.elementAt(i)).integer4);
      }
      catch (Exception e) {
	System.err.println("*** Heapfile error in Tuple.setStrFld() ***");
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
    
///////////////
    
    
    // creating the sailors relation
    AttrType [] Stypes = new AttrType[4];
    Stypes[0] = new AttrType (AttrType.attrInteger);
    Stypes[1] = new AttrType (AttrType.attrString);
    Stypes[2] = new AttrType (AttrType.attrInteger);
    Stypes[3] = new AttrType (AttrType.attrReal);

    //SOS
    //short [] Ssizes = new short [1];
    Ssizes = new short [1];
    Ssizes[0] = 30; //first elt. is 30
    
    //Tuple t = new Tuple();
    t = new Tuple();
    try {
      t.setHdr((short) 4,Stypes, Ssizes);
    }
    catch (Exception e) {
      System.err.println("*** error in Tuple.setHdr() ***");
      status = FAIL;
      e.printStackTrace();
    }
    
    //int size = t.size();
    size = t.size();
    
    // inserting the tuple into file "sailors"
    //RID             rid;
    //Heapfile        f = null;
    f = null;
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
	t.setIntFld(1, ((Sailor)sailors.elementAt(i)).sid);
	t.setStrFld(2, ((Sailor)sailors.elementAt(i)).sname);
	t.setIntFld(3, ((Sailor)sailors.elementAt(i)).rating);
	t.setFloFld(4, (float)((Sailor)sailors.elementAt(i)).age);
      }
      catch (Exception e) {
	System.err.println("*** Heapfile error in Tuple.setStrFld() ***");
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
    
    //creating the boats relation
    AttrType [] Btypes = {
      new AttrType(AttrType.attrInteger), 
      new AttrType(AttrType.attrString), 
      new AttrType(AttrType.attrString), 
    };
    
    short  []  Bsizes = new short[2];
    Bsizes[0] = 30;
    Bsizes[1] = 20;
    t = new Tuple();
    try {
      t.setHdr((short) 3,Btypes, Bsizes);
    }
    catch (Exception e) {
      System.err.println("*** error in Tuple.setHdr() ***");
      status = FAIL;
      e.printStackTrace();
    }
    
    size = t.size();
    
    // inserting the tuple into file "boats"
    //RID             rid;
    f = null;
    try {
      f = new Heapfile("boats.in");
    }
    catch (Exception e) {
      System.err.println("*** error in Heapfile constructor ***");
      status = FAIL;
      e.printStackTrace();
    }
    
    t = new Tuple(size);
    try {
      t.setHdr((short) 3, Btypes, Bsizes);
    }
    catch (Exception e) {
      System.err.println("*** error in Tuple.setHdr() ***");
      status = FAIL;
      e.printStackTrace();
    }
    
    for (int i=0; i<numboats; i++) {
      try {
	t.setIntFld(1, ((Boats)boats.elementAt(i)).bid);
	t.setStrFld(2, ((Boats)boats.elementAt(i)).bname);
	t.setStrFld(3, ((Boats)boats.elementAt(i)).color);
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
      System.err.println ("*** Error creating relation for boats");
      Runtime.getRuntime().exit(1);
    }
    
    //creating the boats relation
    AttrType [] Rtypes = new AttrType[3];
    Rtypes[0] = new AttrType (AttrType.attrInteger);
    Rtypes[1] = new AttrType (AttrType.attrInteger);
    Rtypes[2] = new AttrType (AttrType.attrString);

    short [] Rsizes = new short [1];
    Rsizes[0] = 15; 
    t = new Tuple();
    try {
      t.setHdr((short) 3,Rtypes, Rsizes);
    }
    catch (Exception e) {
      System.err.println("*** error in Tuple.setHdr() ***");
      status = FAIL;
      e.printStackTrace();
    }
    
    size = t.size();
    
    // inserting the tuple into file "boats"
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
      t.setHdr((short) 3, Rtypes, Rsizes);
    }
    catch (Exception e) {
      System.err.println("*** error in Tuple.setHdr() ***");
      status = FAIL;
      e.printStackTrace();
    }
    
    for (int i=0; i<numreserves; i++) {
      try {
	t.setIntFld(1, ((Reserves)reserves.elementAt(i)).sid);
	t.setIntFld(2, ((Reserves)reserves.elementAt(i)).bid);
	t.setStrFld(3, ((Reserves)reserves.elementAt(i)).date);

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
  
  public boolean runTests() throws JoinsException, IndexException, InvalidTupleSizeException, InvalidTypeException, PageNotReadException, TupleUtilsException, PredEvalException, SortException, LowMemException, UnknowAttrType, UnknownKeyTypeException, IOException, Exception {
    
    Disclaimer();
    long startTime = System.currentTimeMillis();
    //IEjoin("query_2c_2.txt");
    //IEjoin_optimized("query_2c_2.txt");
    //Query1();
    //Query2();
    //Query3();
    //Query4();
    //Query5();
    //Query6();
    //Query7();
    //Query8(); 
    //Query9();
    Query10(); // Not Equal
    
    long endTime   = System.currentTimeMillis();
    long totalTime = endTime - startTime;
    System.out.print ("Finished joins testing"+"\n");
    System.out.println(totalTime);
    return true;
  }  
  
	public static void IEjoin_optimized(String Filename){


		//Scanner n = new Scanner(System.in);
		String fileName = Filename;
		String line = null;
		String relations [] = null;
		String[] query = new String [5];
		int temp2pos =0,temp1pos=0,countlast=1;

		int lineNumber = 0,x,xdash,y,ydash,op1,op2,off2,off1,t,last,b,ano,rev=0,orderpos;
		int start,end,current,left;
		String roneTypes = null;
		String rtwoTypes = null;
		ArrayList<ArrayList<Integer>> maps = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> temp = new ArrayList<Integer>();
		List<Integer> temp1 = new ArrayList<Integer>();
		ArrayList<Integer> empty = new ArrayList<Integer>();
		List<Integer> temp2 = new ArrayList<Integer>();
		List<Integer> order = new ArrayList<Integer>();
		ArrayList<Integer> l1 = new ArrayList<Integer>();
		ArrayList<Integer> l2 = new ArrayList<Integer>();
		ArrayList<Integer> l1pos = new ArrayList<Integer>();
		ArrayList<Integer> l2pos = new ArrayList<Integer>();
		ArrayList<Integer> p = new ArrayList<Integer>();
		ArrayList<Integer> pdash = new ArrayList<Integer>();
		ArrayList<Integer> l1dash = new ArrayList<Integer>();
		ArrayList<Integer> l2dash = new ArrayList<Integer>();
		ArrayList<Integer> l1dashpos = new ArrayList<Integer>();
		ArrayList<Integer> l2dashpos = new ArrayList<Integer>();
		ArrayList<Integer> o1 = new ArrayList<Integer>();
		ArrayList<Integer> o2 = new ArrayList<Integer>();
		ArrayList<Integer> bdash = new ArrayList<Integer>();
		ArrayList<Integer> inter = new ArrayList<Integer>();
		ArrayList<Integer> answer = new ArrayList<Integer>();
		ArrayList<Integer> inter1 = new ArrayList<Integer>();
		try {
			 // Open the file
		    FileInputStream fstream = null;
			try {
				fstream = new FileInputStream(fileName);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			// Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(new InputStreamReader(fstream));

            while((line = bufferedReader.readLine()) != null) {
            	//System.out.println("Read");
            	
            		query[lineNumber] = line;
            		lineNumber++;			
            }   
            relations = query[1].split(" ");

            // Always close files.
            bufferedReader.close();
            //System.out.println(relations[0]);
            // INITIALIZATION process
            x = Integer.parseInt(query[2].split(" ")[0].split("_")[1]);
            xdash = Integer.parseInt(query[2].split(" ")[2].split("_")[1]);
            op1 = Integer.parseInt(query[2].split(" ")[1]);
            y = Integer.parseInt(query[4].split(" ")[0].split("_")[1]);
            ydash = Integer.parseInt(query[4].split(" ")[2].split("_")[1]);
            op2 = Integer.parseInt(query[4].split(" ")[1]);
            //System.out.println(x+""+xdash+""+y+""+ydash+""+op1+""+op2);
            fstream = null;
            fstream = new FileInputStream(relations[0]+".txt");        	
            bufferedReader = new BufferedReader(new InputStreamReader(fstream));
            lineNumber = 0;
            while((line = bufferedReader.readLine()) != null) {
            	//System.out.println("Read");
            		if(lineNumber == 0){
            			roneTypes = line;
            		}
            		else{
            			if(roneTypes.split(",")[x-1].equals("attrInteger"))
            				l1.add(Integer.parseInt(line.split(",")[x-1]));
            			else
            				System.out.println("Error: Integer Column not Provided");
            			if(roneTypes.split(",")[y-1].equals("attrInteger"))
            				l2.add(Integer.parseInt(line.split(",")[y-1]));
            			else
            				System.out.println("Error: Integer Column not Provided");
            			
            		}
            		
            		lineNumber++;			
            }
            
            ///////////////////// l1pos
            System.out.println("Computing l1pos");
            temp1pos = 0;temp2pos =0;countlast=1;
            order.clear();
            maps.clear();
            temp1.clear();
            inter1.clear();
            answer.clear();
            temp2.clear();
            inter.clear();
            temp.clear();
            temp.clear();
            rev = 0;
            temp.addAll(l1);
            //System.out.println(temp);
            Collections.sort(l1);

            if(op1==3 || op1==4){
            	// > or <=
            	Collections.reverse(l1);
            	rev = 1;
            }
            if(rev == 1){
            	// to make it in ascending order for the binary search operation
            	Collections.reverse(l1);
            }
            for(int i=0;i<temp.size();i++){
            	inter.add(Collections.binarySearch(l1, temp.get(i))+1);
            	
            }
            if(rev == 1){
            	Collections.reverse(l1);
            }
            if(rev == 1){
            	b=0;
            	if(inter.size()%2==0){
            		// even
            		b = inter.size()/2;
            		for(int i =0;i<inter.size();i++){
            			if(inter.get(i)>b)
            				inter.set(i, b-inter.get(i)+b+1);
            			else
            				inter.set(i, b+b-inter.get(i)+1);
            		}
            	}
            	else{
            		// odd

            		b = (inter.size()/2)+1;
            		for(int i =0;i<inter.size();i++){
            			if(inter.get(i)>b)
            				inter.set(i, b-inter.get(i)+b);
            			else
            				inter.set(i, b+b-inter.get(i));
            		}
            	}
            	
            	rev = 0;
            }
            // inter is in correct order
            for(int i=0;i<inter.size();i++){
            	l1pos.add(0);
            }
            
            for(int i=0;i<inter.size();i++){
            	t = inter.get(i)-1;
            	if(l1pos.get(t) == 0)
            	l1pos.set(t, i+1);
            	else
            		l1pos.set(t, -1);
            }
            inter.clear();
            temp1.clear();// stores zeros
            temp2.clear();// stores -1's
            orderpos = 0;
            for(int i=0;i<l1pos.size();i++){
            	if(l1pos.get(i)==0){
            		temp1.add(i);
            		orderpos ++;
            	}
            	if(l1pos.get(i)==-1){
            		inter1.add(l1.get(i));
            		temp2.add(i);
            		order.add(orderpos);
            		orderpos ++;
            	}
            }
            orderpos=0;
            
            // to make more fast
            
            
            empty.clear();
            for(int i=0;i<inter1.size();i++){
				
				maps.add(new ArrayList<Integer>());
			}
            for(int i=0;i<inter1.size();i++){
            	last=0;
            	//System.out.println(i);
            	b = temp.subList(last, temp.size()).indexOf(inter1.get(i)); 
            	while(b != -1){
            		//System.out.println("co"+temp.subList(last, temp.size()));
            		last = last + b;
            		maps.get(i).add(last);
            		last ++;
            		b = temp.subList(last, temp.size()).indexOf(inter1.get(i));
            	}
            }
            
            while(temp2pos<temp2.size()){
            	countlast = 1;
            	last=1;
            	b = temp2.get(temp2pos);
            	
            	l1pos.set(b, 0);
            	temp1.add(order.get(orderpos), b);
            	orderpos++;
            	lineNumber = 0;
            	
            	t = l1.get(b);
            	ano = temp.subList(lineNumber, temp.size()).indexOf(t);
				//ano = answer.get(answerpos);
				//answerpos++;
				ano = maps.get(temp2pos).get(last-1);
				countlast = ano;
				last++;
				l1pos.set(temp1.get(temp1pos),lineNumber + ano + 1);
        		temp1pos++;
            	while(last <= maps.get(temp2pos).size()){

            		lineNumber =  lineNumber+ ano + 1;
            		ano = maps.get(temp2pos).get(last-1) - countlast - 1;
            		countlast = maps.get(temp2pos).get(last-1);
            		last++;
    				l1pos.set(temp1.get(temp1pos),lineNumber + ano + 1);
            		temp1pos++;

            	}
            	temp2pos++;
            	}
           System.out.println("l1pos ready");
            ///////////////////////// l2pos 
           System.out.println("Computing l2pos");
            
            temp1pos = 0;temp2pos =0;countlast=1;
            order.clear();
            maps.clear();
            temp1.clear();
            inter1.clear();
            answer.clear();
            temp2.clear();
            inter.clear();
            temp.clear();
            temp.clear();
            rev = 0;
            temp.addAll(l2);
            Collections.sort(l2);

            if(op2==2 || op2==1){
            	// > or <=
            	Collections.reverse(l2);
            	rev = 1;
            }
            if(rev == 1){
            	// to make it in ascending order for the binary search operation
            	Collections.reverse(l2);
            }
            for(int i=0;i<temp.size();i++){
            	inter.add(Collections.binarySearch(l2, temp.get(i))+1);
            	
            }
            
            if(rev == 1){
            	Collections.reverse(l2);
            }
            if(rev == 1){
            	b=0;
            	if(inter.size()%2==0){
            		// even
            		b = inter.size()/2;
            		for(int i =0;i<inter.size();i++){
            			if(inter.get(i)>b)
            				inter.set(i, b-inter.get(i)+b+1);
            			else
            				inter.set(i, b+b-inter.get(i)+1);
            		}
            	}
            	else{
            		// odd

            		b = (inter.size()/2)+1;
            		for(int i =0;i<inter.size();i++){
            			if(inter.get(i)>b)
            				inter.set(i, b-inter.get(i)+b);
            			else
            				inter.set(i, b+b-inter.get(i));
            		}
            	}
            	
            	//Collections.reverse(inter);
            	rev = 0;
            }
            // inter is in correct order
             for(int i=0;i<inter.size();i++){
            	l2pos.add(0);
            }
            
            for(int i=0;i<inter.size();i++){
            	t = inter.get(i)-1;
            	if(l2pos.get(t) == 0)
            	l2pos.set(t, i+1);
            	else
            		l2pos.set(t, -1);
            }
            inter.clear();
            temp1.clear();// stores zeros
            temp2.clear();// stores -1's
            orderpos = 0;
            for(int i=0;i<l2pos.size();i++){
            	if(l2pos.get(i)==0){
            		temp1.add(i);
            		orderpos ++;
            	}
            	if(l2pos.get(i)==-1){
            		inter1.add(l2.get(i));
            		temp2.add(i);
            		order.add(orderpos);
            		orderpos ++;
            	}
            }
            orderpos=0;
            
            // to make more fast
            
           // System.out.println(inter1);
            empty.clear();
            for(int i=0;i<inter1.size();i++){
				
				maps.add(new ArrayList<Integer>());
			}
            for(int i=0;i<inter1.size();i++){
            	last=0;
            	//System.out.println(i);
            	b = temp.subList(last, temp.size()).indexOf(inter1.get(i)); 
            	while(b != -1){
            		//System.out.println("co"+temp.subList(last, temp.size()));
            		last = last + b;
            		maps.get(i).add(last);
            		last ++;
            		b = temp.subList(last, temp.size()).indexOf(inter1.get(i));
            	}
            }
            
            //System.out.println(maps);
            //System.out.println(countOfdup);
            while(temp2pos<temp2.size()){
            	countlast = 1;
            	last=1;
            	b = temp2.get(temp2pos);
            	
            	l2pos.set(b, 0);
            	temp1.add(order.get(orderpos), b);
            	orderpos++;
            	lineNumber = 0;
            	
            	t = l2.get(b);
            	ano = temp.subList(lineNumber, temp.size()).indexOf(t);
				//ano = answer.get(answerpos);
				//answerpos++;
				ano = maps.get(temp2pos).get(last-1);
				countlast = ano;
				last++;
				l2pos.set(temp1.get(temp1pos),lineNumber + ano + 1);
        		temp1pos++;
            	while(last <= maps.get(temp2pos).size()){

            		lineNumber =  lineNumber+ ano + 1;
            		ano = maps.get(temp2pos).get(last-1) - countlast - 1;
            		countlast = maps.get(temp2pos).get(last-1);
            		last++;
    				l2pos.set(temp1.get(temp1pos),lineNumber + ano + 1);
            		temp1pos++;

            	}
            	temp2pos++;
            	}
            System.out.println("l2pos ready");
            ///////////////////////// p
            System.out.println("Computing p");
            	temp.clear();
            	inter.clear();
     			temp.clear();
     			temp1.clear();
     			temp.addAll(l1pos);
     			Collections.sort(temp);
     			for(int i=0;i<l1pos.size();i++){
     				inter.add(Collections.binarySearch(temp, l1pos.get(i))+1);
     				}
     			
     			for(int i=0;i<l1pos.size();i++){
     				temp1.add(0);
     			}
     			
     			for(int i=0;i<inter.size();i++){
     				temp1.set(inter.get(i)-1, i+1);
     			}
     			
     			for(int i=0;i<l2pos.size();i++){
     				p.add(temp1.get(l2pos.get(i)-1));
     			}
                bufferedReader.close();	
                System.out.println("p ready");
            ///////////////////////////////////////////////////////////
            // Initialization ( second relation )
            
            //////////////////////////////////////////////////////////
            
                
                fstream = null;
                fstream = new FileInputStream(relations[1]+".txt");        	
                bufferedReader = new BufferedReader(new InputStreamReader(fstream));
                  lineNumber = 0;
            while((line = bufferedReader.readLine()) != null) {
            	//System.out.println("Read");
            		if(lineNumber == 0){
            			rtwoTypes = line;
            		}
            		else{
            			if(rtwoTypes.split(",")[xdash-1].equals("attrInteger"))
            				l1dash.add(Integer.parseInt(line.split(",")[xdash-1]));
            			else
            				System.out.println("Error: Integer Column not Provided");
            			if(roneTypes.split(",")[ydash-1].equals("attrInteger"))
            				l2dash.add(Integer.parseInt(line.split(",")[ydash-1]));
            			else
            				System.out.println("Error: Integer Column not Provided");
            			
            		}
            		
            		lineNumber++;			
            }
            
            //////////////////// l1dashpos
            System.out.println("Computing l1dashpos");
            temp1pos = 0;temp2pos =0;countlast=1;
            order.clear();
            maps.clear();
            temp1.clear();
            inter1.clear();
            answer.clear();
            temp2.clear();
            inter.clear();
            temp.clear();
            temp.clear();
            rev = 0;
            temp.addAll(l1dash);
            //System.out.println(temp);
            Collections.sort(l1dash);

            if(op1==3 || op1==4){
            	// > or <=
            	Collections.reverse(l1dash);
            	rev = 1;
            }
            if(rev == 1){
            	// to make it in ascending order for the binary search operation
            	Collections.reverse(l1dash);
            }
            for(int i=0;i<temp.size();i++){
            	inter.add(Collections.binarySearch(l1dash, temp.get(i))+1);
            	
            }
            if(rev == 1){
            	Collections.reverse(l1dash);
            }
            if(rev == 1){
            	b=0;
            	if(inter.size()%2==0){
            		// even
            		b = inter.size()/2;
            		for(int i =0;i<inter.size();i++){
            			if(inter.get(i)>b)
            				inter.set(i, b-inter.get(i)+b+1);
            			else
            				inter.set(i, b+b-inter.get(i)+1);
            		}
            	}
            	else{
            		// odd

            		b = (inter.size()/2)+1;
            		for(int i =0;i<inter.size();i++){
            			if(inter.get(i)>b)
            				inter.set(i, b-inter.get(i)+b);
            			else
            				inter.set(i, b+b-inter.get(i));
            		}
            	}
            	
            	rev = 0;
            }
            // inter is in correct order
            for(int i=0;i<inter.size();i++){
            	l1dashpos.add(0);
            }
            
            for(int i=0;i<inter.size();i++){
            	t = inter.get(i)-1;
            	if(l1dashpos.get(t) == 0)
            	l1dashpos.set(t, i+1);
            	else
            		l1dashpos.set(t, -1);
            }
            inter.clear();
            temp1.clear();// stores zeros
            temp2.clear();// stores -1's
            orderpos = 0;
            for(int i=0;i<l1dashpos.size();i++){
            	if(l1dashpos.get(i)==0){
            		temp1.add(i);
            		orderpos ++;
            	}
            	if(l1dashpos.get(i)==-1){
            		inter1.add(l1dash.get(i));
            		temp2.add(i);
            		order.add(orderpos);
            		orderpos ++;
            	}
            }
            orderpos=0;
            
            // to make more fast
            
            empty.clear();
            for(int i=0;i<inter1.size();i++){
				
				maps.add(new ArrayList<Integer>());
			}
            for(int i=0;i<inter1.size();i++){
            	last=0;
            	//System.out.println(i);
            	b = temp.subList(last, temp.size()).indexOf(inter1.get(i)); 
            	while(b != -1){
            		//System.out.println("co"+temp.subList(last, temp.size()));
            		last = last + b;
            		maps.get(i).add(last);
            		last ++;
            		b = temp.subList(last, temp.size()).indexOf(inter1.get(i));
            	}
            }
            
            //System.out.println(maps);
            //System.out.println(countOfdup);
            while(temp2pos<temp2.size()){
            	countlast = 1;
            	last=1;
            	b = temp2.get(temp2pos);
            	
            	l1dashpos.set(b, 0);
            	temp1.add(order.get(orderpos), b);
            	orderpos++;
            	lineNumber = 0;
            	
            	t = l1dash.get(b);
            	ano = temp.subList(lineNumber, temp.size()).indexOf(t);
				//ano = answer.get(answerpos);
				//answerpos++;
				ano = maps.get(temp2pos).get(last-1);
				countlast = ano;
				last++;
				l1dashpos.set(temp1.get(temp1pos),lineNumber + ano + 1);
        		temp1pos++;
            	while(last <= maps.get(temp2pos).size()){

            		lineNumber =  lineNumber+ ano + 1;
            		ano = maps.get(temp2pos).get(last-1) - countlast - 1;
            		countlast = maps.get(temp2pos).get(last-1);
            		last++;
    				l1dashpos.set(temp1.get(temp1pos),lineNumber + ano + 1);
            		temp1pos++;

            	}
            	temp2pos++;
            	}
            System.out.println("l1dashpos ready");
            //////////////////////////////// l2dashpos
            System.out.println("Computing l2dashpos");
            temp1pos = 0;temp2pos =0;countlast=1;
            order.clear();
            maps.clear();
            temp1.clear();
            inter1.clear();
            answer.clear();
            temp2.clear();
            inter.clear();
            temp.clear();
            temp.clear();
            rev = 0;
            temp.addAll(l2dash);
            //System.out.println(temp);
            Collections.sort(l2dash);

            if(op2==2 || op2==1){
            	// > or <=
            	Collections.reverse(l2dash);
            	rev = 1;
            }
            if(rev == 1){
            	// to make it in ascending order for the binary search operation
            	Collections.reverse(l2dash);
            }
            for(int i=0;i<temp.size();i++){
            	inter.add(Collections.binarySearch(l2dash, temp.get(i))+1);
            	
            }
            if(rev == 1){
            	Collections.reverse(l2dash);
            }
            if(rev == 1){
            	b=0;
            	if(inter.size()%2==0){
            		// even
            		b = inter.size()/2;
            		for(int i =0;i<inter.size();i++){
            			if(inter.get(i)>b)
            				inter.set(i, b-inter.get(i)+b+1);
            			else
            				inter.set(i, b+b-inter.get(i)+1);
            		}
            	}
            	else{
            		// odd

            		b = (inter.size()/2)+1;
            		for(int i =0;i<inter.size();i++){
            			if(inter.get(i)>b)
            				inter.set(i, b-inter.get(i)+b);
            			else
            				inter.set(i, b+b-inter.get(i));
            		}
            	}
            	
            	rev = 0;
            }
            // inter is in correct order
            for(int i=0;i<inter.size();i++){
            	l2dashpos.add(0);
            }
            
            for(int i=0;i<inter.size();i++){
            	t = inter.get(i)-1;
            	if(l2dashpos.get(t) == 0)
            	l2dashpos.set(t, i+1);
            	else
            		l2dashpos.set(t, -1);
            }
            inter.clear();
            temp1.clear();// stores zeros
            temp2.clear();// stores -1's
            orderpos = 0;
            for(int i=0;i<l2dashpos.size();i++){
            	if(l2dashpos.get(i)==0){
            		temp1.add(i);
            		orderpos ++;
            	}
            	if(l2dashpos.get(i)==-1){
            		inter1.add(l2dash.get(i));
            		temp2.add(i);
            		order.add(orderpos);
            		orderpos ++;
            	}
            }
            orderpos=0;
            
            // to make more fast
            
            empty.clear();
            for(int i=0;i<inter1.size();i++){
				
				maps.add(new ArrayList<Integer>());
			}
            for(int i=0;i<inter1.size();i++){
            	last=0;
            	//System.out.println(i);
            	b = temp.subList(last, temp.size()).indexOf(inter1.get(i)); 
            	while(b != -1){
            		//System.out.println("co"+temp.subList(last, temp.size()));
            		last = last + b;
            		maps.get(i).add(last);
            		last ++;
            		b = temp.subList(last, temp.size()).indexOf(inter1.get(i));
            	}
            }
            
            //System.out.println(maps);
            //System.out.println(countOfdup);
            while(temp2pos<temp2.size()){
            	countlast = 1;
            	last=1;
            	b = temp2.get(temp2pos);
            	
            	l2dashpos.set(b, 0);
            	temp1.add(order.get(orderpos), b);
            	orderpos++;
            	lineNumber = 0;
            	
            	t = l2dash.get(b);
            	ano = temp.subList(lineNumber, temp.size()).indexOf(t);
				//ano = answer.get(answerpos);
				//answerpos++;
				ano = maps.get(temp2pos).get(last-1);
				countlast = ano;
				last++;
				l2dashpos.set(temp1.get(temp1pos),lineNumber + ano + 1);
        		temp1pos++;
            	while(last <= maps.get(temp2pos).size()){

            		lineNumber =  lineNumber+ ano + 1;
            		ano = maps.get(temp2pos).get(last-1) - countlast - 1;
            		countlast = maps.get(temp2pos).get(last-1);
            		last++;
    				l2dashpos.set(temp1.get(temp1pos),lineNumber + ano + 1);
            		temp1pos++;

            	}
            	temp2pos++;
            	}
            System.out.println("l2dashpos ready");
            ///////////////////////////////// pdash
            System.out.println("Computing pdash");
            
            temp.clear();
         
  			inter.clear();
  			temp.clear();
  			temp1.clear();
  			temp.addAll(l1dashpos);
  			Collections.sort(temp);
  			for(int i=0;i<l1dashpos.size();i++){
  				inter.add(Collections.binarySearch(temp, l1dashpos.get(i))+1);
  				}
  			for(int i=0;i<l1dashpos.size();i++){
  				temp1.add(0);
  			}
  			
  			for(int i=0;i<inter.size();i++){
  				temp1.set(inter.get(i)-1, i+1);
  			}
  			
  			
  			for(int i=0;i<l2dashpos.size();i++){
  				pdash.add(temp1.get(l2dashpos.get(i)-1));
  			}
  			bufferedReader.close();	
  			System.out.println("pdash ready");
  			///////////////////////////// intialization over above
  			
  			
  			/*  System.out.println("Initialization .......");
            //n.nextInt();
            System.out.println("l1:\t\t"+l1);
//            System.out.println("l1pos:\t "+l1pos);
            System.out.println("l2:\t\t"+l2);
//            System.out.println("l2pos:\t "+l2pos);
            System.out.println("p:\t\t"+p);            
            System.out.println("l1dash: \t"+l1dash);
            System.out.println("l2dash: \t"+l2dash);
//            System.out.println("l2dashpos: \t"+l2dashpos);
            System.out.println("pdash: \t\t"+pdash);*/
            System.out.println("--------------------");
 
            /////////////////////////////////////////////////////////////////////////////////////////////////
            
            								//Works Fine above here
            
            /////////////////////////////////////////////////////////////////////////////////////////////////
            
         System.out.println("Computing Offsets");   
             // Computing offset o1 
         if(op1==2 || op1==3){
        	 
        	 if(op1==2){
        		 // l1dash is in ascending order
        		 for(int i=0;i<l1.size();i++){
                 	lineNumber = 0;
                 	while(lineNumber<l1dash.size() &&l1dash.get(lineNumber)<l1.get(i))
                 		lineNumber++;
                 	o1.add(lineNumber+1);
                 }	 
        		 
        	 }
        	 if(op1==3){
        		 //l1dash is in descending order

        		 for(int i=0;i<l1.size();i++){
                 	lineNumber = 0;
                 	while(lineNumber<l1dash.size() &&l1dash.get(lineNumber)>l1.get(i))
                 		lineNumber++;
                 	o1.add(lineNumber+1);
                 }
        	 }
        	 
         }
         if(op1==1 || op1==4){
        	 
        	 if(op1==1){
            		 // l1dash is in ascending order
            		 for(int i=0;i<l1.size();i++){
                     	lineNumber = 0;
                     	while(lineNumber<l1dash.size() &&l1dash.get(lineNumber)<=l1.get(i))
                     		lineNumber++;
                     	o1.add(lineNumber+1);
                     }	 
        	 }
        	 if(op1==4){
        		 //l1dash is in descending order
        		 for(int i=0;i<l1.size();i++){
                  	lineNumber = 0;
                  	while(lineNumber<l1dash.size() &&l1dash.get(lineNumber)>=l1.get(i))
                  		lineNumber++;
                  	o1.add(lineNumber+1);
                  }
        	 
        	 }
        	 
         }
   
            // Computing offset o2 
        if(op2==1 || op2==4){
       	 
       	 if(op2==1){
       		 // l2dash is in descending order
       		 for(int i=0;i<l2.size();i++){
                	lineNumber = 0;
                	while(lineNumber<l2dash.size() &&l2dash.get(lineNumber)>l2.get(i))
                		lineNumber++;
                	o2.add(lineNumber+1);
                }	 
       		 
       	 }
       	 if(op2==4){
       		 // l2dash is in ascending order
       		 for(int i=0;i<l2.size();i++){
                	lineNumber = 0;
                	while(lineNumber<l2dash.size() &&l2dash.get(lineNumber)<l2.get(i))
                		lineNumber++;
                	o2.add(lineNumber+1);
                }	 
       		 
       	 }       	 
        }
        if(op2==2 || op2==3){
       	 
          	 if(op2==2){
           		 // l2dash is in descending order
           		 for(int i=0;i<l2.size();i++){
                    	lineNumber = 0;
                    	while(lineNumber<l2dash.size() &&l2dash.get(lineNumber)>=l2.get(i))
                    		lineNumber++;
                    	o2.add(lineNumber+1);
                    }	 
           		 
           	 }
           	 if(op2==3){
           		 // l2dash is in ascending order
           		 for(int i=0;i<l2.size();i++){
                    	lineNumber = 0;
                    	while(lineNumber<l2dash.size() &&l2dash.get(lineNumber)<=l2.get(i))
                    		lineNumber++;
                    	o2.add(lineNumber+1);
                    }	 
           		 
           	 }       	        	 
        }
    
            /*for(int i=0;i<l1dash.size();i++){
            	bdash.add(0);
            }
           */
        for(int i=0;i<l1dash.size();i++){
        	bdash.add(-1);
        }
       
            
           
           
            
           /* System.out.println("o1: \t\t"+o1);
            System.out.println("o2: \t\t"+o2);
            System.out.println("bdash: \t\t"+bdash);*/
            System.out.println("------------Initialization Over !!-------------");
            //n.nextInt();
            // Initialization over
            start = bdash.size()+1;
            end = -1;
            current = 0;
            left = 0;
            //System.out.println(start+" "+end);
            for(int i=0;i<l2.size();i++){
            	off2 = o2.get(i);
            	if(off2>=2){
            		for(int j=0;j<=off2-2;j++){
            			current = pdash.get(j);
            			//System.out.println(j+"current"+current);
            			if(bdash.get(current-1)==-1)
            			{
            				//System.out.println("Processing....");
            			if(current<start){
            				bdash.set(current-1, start);
            				start = current;
            			}
            			if(current>end){
            				if(end>-1){
            					bdash.set(end-1, current);
            					end = current;
            					bdash.set(end-1, bdash.size()+1);
            				}
            				else{
            					end = current;
            				}
            			}
            			else if(start<current && current<end){
            				//System.out.println("came");
            				for(int z=current-1;z>=start;z--){
            					if(bdash.get(z-1)!=-1){
            						left = z;
            						break;
            					}
            					
            				}
            				bdash.set(current-1, bdash.get(left-1));
            				bdash.set(left-1, current);
            			}
            		}
            			//System.out.println("bdash"+bdash+"start"+start+"end"+end);
            		}
            	}
            	
            	off1 = o1.get(p.get(i)-1);
            	if(off1<start)
            		b = start;
            	else
            		b = off1;
            	//System.out.println("value of b"+b);
            	if(b<end){
            		if(bdash.get(b-1)==-1){
            		for(int z=b+1;z<=end;z++){
            			if(bdash.get(z-1)!= -1)
            			{
            				b=z;
            				break;
            			}
            		}
            		}
            	}
            	//System.out.println("Got offset"+off1+"b"+b);            	
            	while(b<=end){
            		System.out.println("Ans:\t\t"+l2pos.get(i)+" "+l1dashpos.get(b-1));
            		b = bdash.get(b-1);
            	}

            }
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }
        catch(ArrayIndexOutOfBoundsException ex){
        	System.out.println("Not Enough Arguments provided");
        }
	
	}

  
	public static void IEjoin(String Filename){
		String fileName = Filename;
		String line = null;
		String relations [] = null;
		String[] query = new String [5];
		int temp2pos =0,temp1pos=0,countlast=1;
		int lineNumber = 0,x,xdash,y,ydash,op1,op2,off2,off1,t,last,b,ano,rev=0,orderpos;
		String roneTypes = null;
		String rtwoTypes = null;
		ArrayList<ArrayList<Integer>> maps = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> temp = new ArrayList<Integer>();
		List<Integer> temp1 = new ArrayList<Integer>();
		ArrayList<Integer> empty = new ArrayList<Integer>();
		List<Integer> temp2 = new ArrayList<Integer>();
		List<Integer> order = new ArrayList<Integer>();
		ArrayList<Integer> l1 = new ArrayList<Integer>();
		ArrayList<Integer> l2 = new ArrayList<Integer>();
		ArrayList<Integer> l1pos = new ArrayList<Integer>();
		ArrayList<Integer> l2pos = new ArrayList<Integer>();
		ArrayList<Integer> p = new ArrayList<Integer>();
		ArrayList<Integer> pdash = new ArrayList<Integer>();
		ArrayList<Integer> l1dash = new ArrayList<Integer>();
		ArrayList<Integer> l2dash = new ArrayList<Integer>();
		ArrayList<Integer> l1dashpos = new ArrayList<Integer>();
		ArrayList<Integer> l2dashpos = new ArrayList<Integer>();
		ArrayList<Integer> o1 = new ArrayList<Integer>();
		ArrayList<Integer> o2 = new ArrayList<Integer>();
		ArrayList<Integer> bdash = new ArrayList<Integer>();
		ArrayList<Integer> inter = new ArrayList<Integer>();
		ArrayList<Integer> answer = new ArrayList<Integer>();
		ArrayList<Integer> inter1 = new ArrayList<Integer>();
		try {
			
			 // Open the file
		    FileInputStream fstream = null;
			try {
				fstream = new FileInputStream(fileName);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			// Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(new InputStreamReader(fstream));

            while((line = bufferedReader.readLine()) != null) {
            	//System.out.println("Read");
            	
            		query[lineNumber] = line;
            		lineNumber++;			
            }   
            relations = query[1].split(" ");

            // Always close files.
            bufferedReader.close();
            //System.out.println(relations[0]);
            // INITIALIZATION process
            x = Integer.parseInt(query[2].split(" ")[0].split("_")[1]);
            xdash = Integer.parseInt(query[2].split(" ")[2].split("_")[1]);
            op1 = Integer.parseInt(query[2].split(" ")[1]);
            y = Integer.parseInt(query[4].split(" ")[0].split("_")[1]);
            ydash = Integer.parseInt(query[4].split(" ")[2].split("_")[1]);
            op2 = Integer.parseInt(query[4].split(" ")[1]);
            //System.out.println(x+""+xdash+""+y+""+ydash+""+op1+""+op2);
            fstream = null;
        		fstream = new FileInputStream(relations[0]+".txt");
        	
        bufferedReader = new BufferedReader(new InputStreamReader(fstream));
            
            lineNumber = 0;
            while((line = bufferedReader.readLine()) != null) {
            	//System.out.println("Read");
            		if(lineNumber == 0){
            			roneTypes = line;
            		}
            		else{
            			if(roneTypes.split(",")[x-1].equals("attrInteger"))
            				l1.add(Integer.parseInt(line.split(",")[x-1]));
            			else
            				System.out.println("Error: Integer Column not Provided");
            			if(roneTypes.split(",")[y-1].equals("attrInteger"))
            				l2.add(Integer.parseInt(line.split(",")[y-1]));
            			else
            				System.out.println("Error: Integer Column not Provided");
            			
            		}
            		
            		lineNumber++;			
            }
            
            ///////////////////// l1pos
            System.out.println("Calculating l1pos");
            temp1pos = 0;temp2pos =0;countlast=1;
            order.clear();
            maps.clear();
            temp1.clear();
            inter1.clear();
            answer.clear();
            temp2.clear();
            inter.clear();
            temp.clear();
            temp.clear();
            rev = 0;
            temp.addAll(l1);
            //System.out.println(temp);
            Collections.sort(l1);

            if(op1 ==3 || op1 ==4){
            	// > or <=
            	Collections.reverse(l1);
            	rev = 1;
            }
            if(rev == 1){
            	// to make it in ascending order for the binary search operation
            	Collections.reverse(l1);
            }
            for(int i=0;i<temp.size();i++){
            	inter.add(Collections.binarySearch(l1, temp.get(i))+1);
            	
            }
            if(rev == 1){
            	Collections.reverse(l1);
            }
            if(rev == 1){
            	b=0;
            	if(inter.size()%2==0){
            		// even
            		b = inter.size()/2;
            		for(int i =0;i<inter.size();i++){
            			if(inter.get(i)>b)
            				inter.set(i, b-inter.get(i)+b+1);
            			else
            				inter.set(i, b+b-inter.get(i)+1);
            		}
            	}
            	else{
            		// odd

            		b = (inter.size()/2)+1;
            		for(int i =0;i<inter.size();i++){
            			if(inter.get(i)>b)
            				inter.set(i, b-inter.get(i)+b);
            			else
            				inter.set(i, b+b-inter.get(i));
            		}
            	}
            	
            	rev = 0;
            }
            // inter is in correct order
            for(int i=0;i<inter.size();i++){
            	l1pos.add(0);
            }
            
            for(int i=0;i<inter.size();i++){
            	t = inter.get(i)-1;
            	if(l1pos.get(t) == 0)
            	l1pos.set(t, i+1);
            	else
            		l1pos.set(t, -1);
            }
            inter.clear();
            temp1.clear();// stores zeros
            temp2.clear();// stores -1's
            orderpos = 0;
            for(int i=0;i<l1pos.size();i++){
            	if(l1pos.get(i)==0){
            		temp1.add(i);
            		orderpos ++;
            	}
            	if(l1pos.get(i)==-1){
            		inter1.add(l1.get(i));
            		temp2.add(i);
            		order.add(orderpos);
            		orderpos ++;
            	}
            }
            orderpos=0;
            
            // to make more fast
            
            
            empty.clear();
            for(int i=0;i<inter1.size();i++){
				
				maps.add(new ArrayList<Integer>());
			}
            for(int i=0;i<inter1.size();i++){
            	last=0;
            	//System.out.println(i);
            	b = temp.subList(last, temp.size()).indexOf(inter1.get(i)); 
            	while(b != -1){
            		//System.out.println("co"+temp.subList(last, temp.size()));
            		last = last + b;
            		maps.get(i).add(last);
            		last ++;
            		b = temp.subList(last, temp.size()).indexOf(inter1.get(i));
            	}
            }
            
            while(temp2pos<temp2.size()){
            	countlast = 1;
            	last=1;
            	b = temp2.get(temp2pos);
            	
            	l1pos.set(b, 0);
            	temp1.add(order.get(orderpos), b);
            	orderpos++;
            	lineNumber = 0;
            	
            	t = l1.get(b);
            	ano = temp.subList(lineNumber, temp.size()).indexOf(t);
				//ano = answer.get(answerpos);
				//answerpos++;
				ano = maps.get(temp2pos).get(last-1);
				countlast = ano;
				last++;
				l1pos.set(temp1.get(temp1pos),lineNumber + ano + 1);
        		temp1pos++;
            	while(last <= maps.get(temp2pos).size()){

            		lineNumber =  lineNumber+ ano + 1;
            		ano = maps.get(temp2pos).get(last-1) - countlast - 1;
            		countlast = maps.get(temp2pos).get(last-1);
            		last++;
    				l1pos.set(temp1.get(temp1pos),lineNumber + ano + 1);
            		temp1pos++;

            	}
            	temp2pos++;
            	}
           System.out.println("l1pos ready");
            ///////////////////////// l2pos 
           System.out.println("Calculating l2pos");
            
            temp1pos = 0;temp2pos =0;countlast=1;
            order.clear();
            maps.clear();
            temp1.clear();
            inter1.clear();
            answer.clear();
            temp2.clear();
            inter.clear();
            temp.clear();
            temp.clear();
            rev = 0;
            temp.addAll(l2);
            Collections.sort(l2);

            if(op2==2 || op2==1){
            	// > or <=
            	Collections.reverse(l2);
            	rev = 1;
            }
            if(rev == 1){
            	// to make it in ascending order for the binary search operation
            	Collections.reverse(l2);
            }
            for(int i=0;i<temp.size();i++){
            	inter.add(Collections.binarySearch(l2, temp.get(i))+1);
            	
            }
            
            if(rev == 1){
            	Collections.reverse(l2);
            }
            if(rev == 1){
            	b=0;
            	if(inter.size()%2==0){
            		// even
            		b = inter.size()/2;
            		for(int i =0;i<inter.size();i++){
            			if(inter.get(i)>b)
            				inter.set(i, b-inter.get(i)+b+1);
            			else
            				inter.set(i, b+b-inter.get(i)+1);
            		}
            	}
            	else{
            		// odd

            		b = (inter.size()/2)+1;
            		for(int i =0;i<inter.size();i++){
            			if(inter.get(i)>b)
            				inter.set(i, b-inter.get(i)+b);
            			else
            				inter.set(i, b+b-inter.get(i));
            		}
            	}
            	
            	//Collections.reverse(inter);
            	rev = 0;
            }
            // inter is in correct order
             for(int i=0;i<inter.size();i++){
            	l2pos.add(0);
            }
            
            for(int i=0;i<inter.size();i++){
            	t = inter.get(i)-1;
            	if(l2pos.get(t) == 0)
            	l2pos.set(t, i+1);
            	else
            		l2pos.set(t, -1);
            }
            inter.clear();
            temp1.clear();// stores zeros
            temp2.clear();// stores -1's
            orderpos = 0;
            for(int i=0;i<l2pos.size();i++){
            	if(l2pos.get(i)==0){
            		temp1.add(i);
            		orderpos ++;
            	}
            	if(l2pos.get(i)==-1){
            		inter1.add(l2.get(i));
            		temp2.add(i);
            		order.add(orderpos);
            		orderpos ++;
            	}
            }
            orderpos=0;
            
            // to make more fast
            
           // System.out.println(inter1);
            empty.clear();
            for(int i=0;i<inter1.size();i++){
				
				maps.add(new ArrayList<Integer>());
			}
            for(int i=0;i<inter1.size();i++){
            	last=0;
            	//System.out.println("Handling Duplicates: "+ i + " out of "+ inter1.size());
            	b = temp.subList(last, temp.size()).indexOf(inter1.get(i)); 
            	while(b != -1){
            		//System.out.println("co"+temp.subList(last, temp.size()));
            		last = last + b;
            		maps.get(i).add(last);
            		last ++;
            		b = temp.subList(last, temp.size()).indexOf(inter1.get(i));
            	}
            }
            
            //System.out.println(maps);
            //System.out.println(countOfdup);
            while(temp2pos<temp2.size()){
            	countlast = 1;
            	last=1;
            	b = temp2.get(temp2pos);
            	
            	l2pos.set(b, 0);
            	temp1.add(order.get(orderpos), b);
            	orderpos++;
            	lineNumber = 0;
            	
            	t = l2.get(b);
            	ano = temp.subList(lineNumber, temp.size()).indexOf(t);
				//ano = answer.get(answerpos);
				//answerpos++;
				ano = maps.get(temp2pos).get(last-1);
				countlast = ano;
				last++;
				l2pos.set(temp1.get(temp1pos),lineNumber + ano + 1);
        		temp1pos++;
            	while(last <= maps.get(temp2pos).size()){

            		lineNumber =  lineNumber+ ano + 1;
            		ano = maps.get(temp2pos).get(last-1) - countlast - 1;
            		countlast = maps.get(temp2pos).get(last-1);
            		last++;
    				l2pos.set(temp1.get(temp1pos),lineNumber + ano + 1);
            		temp1pos++;

            	}
            	temp2pos++;
            	}
            System.out.println("l2pos ready");
            ///////////////////////// p
            System.out.println("Calculating p");
            	temp.clear();
            	inter.clear();
     			temp.clear();
     			temp1.clear();
     			temp.addAll(l1pos);
     			Collections.sort(temp);
     			for(int i=0;i<l1pos.size();i++){
     				inter.add(Collections.binarySearch(temp, l1pos.get(i))+1);
     				}
     			
     			for(int i=0;i<l1pos.size();i++){
     				temp1.add(0);
     			}
     			
     			for(int i=0;i<inter.size();i++){
     				temp1.set(inter.get(i)-1, i+1);
     			}
     			
     			for(int i=0;i<l2pos.size();i++){
     				p.add(temp1.get(l2pos.get(i)-1));
     			}
                bufferedReader.close();	
                System.out.println("p ready");
            ///////////////////////////////////////////////////////////
            // Initialization ( second relation )
            
            //////////////////////////////////////////////////////////
                fstream = null;
            	try {
            		fstream = new FileInputStream(relations[1]+".txt");
            	} catch (FileNotFoundException e1) {
            		// TODO Auto-generated catch block
            		e1.printStackTrace();
            	}
            	
            bufferedReader = new BufferedReader(new InputStreamReader(fstream));
            lineNumber = 0;
            while((line = bufferedReader.readLine()) != null) {
            	//System.out.println("Read");
            		if(lineNumber == 0){
            			rtwoTypes = line;
            		}
            		else{
            			if(rtwoTypes.split(",")[xdash-1].equals("attrInteger"))
            				l1dash.add(Integer.parseInt(line.split(",")[xdash-1]));
            			else
            				System.out.println("Error: Integer Column not Provided");
            			if(roneTypes.split(",")[ydash-1].equals("attrInteger"))
            				l2dash.add(Integer.parseInt(line.split(",")[ydash-1]));
            			else
            				System.out.println("Error: Integer Column not Provided");
            			
            		}
            		
            		lineNumber++;			
            }
            
            //////////////////// l1dashpos
            System.out.println("Calculating l1dashpos");
            temp1pos = 0;temp2pos =0;countlast=1;
            order.clear();
            maps.clear();
            temp1.clear();
            inter1.clear();
            answer.clear();
            temp2.clear();
            inter.clear();
            temp.clear();
            temp.clear();
            rev = 0;
            temp.addAll(l1dash);
            //System.out.println(temp);
            Collections.sort(l1dash);

            if(op1==3 || op1==4){
            	// > or <=
            	Collections.reverse(l1dash);
            	rev = 1;
            }
            if(rev == 1){
            	// to make it in ascending order for the binary search operation
            	Collections.reverse(l1dash);
            }
            for(int i=0;i<temp.size();i++){
            	inter.add(Collections.binarySearch(l1dash, temp.get(i))+1);
            	
            }
            if(rev == 1){
            	Collections.reverse(l1dash);
            }
            if(rev == 1){
            	b=0;
            	if(inter.size()%2==0){
            		// even
            		b = inter.size()/2;
            		for(int i =0;i<inter.size();i++){
            			if(inter.get(i)>b)
            				inter.set(i, b-inter.get(i)+b+1);
            			else
            				inter.set(i, b+b-inter.get(i)+1);
            		}
            	}
            	else{
            		// odd

            		b = (inter.size()/2)+1;
            		for(int i =0;i<inter.size();i++){
            			if(inter.get(i)>b)
            				inter.set(i, b-inter.get(i)+b);
            			else
            				inter.set(i, b+b-inter.get(i));
            		}
            	}
            	
            	rev = 0;
            }
            // inter is in correct order
            for(int i=0;i<inter.size();i++){
            	l1dashpos.add(0);
            }
            
            for(int i=0;i<inter.size();i++){
            	t = inter.get(i)-1;
            	if(l1dashpos.get(t) == 0)
            	l1dashpos.set(t, i+1);
            	else
            		l1dashpos.set(t, -1);
            }
            inter.clear();
            temp1.clear();// stores zeros
            temp2.clear();// stores -1's
            orderpos = 0;
            for(int i=0;i<l1dashpos.size();i++){
            	if(l1dashpos.get(i)==0){
            		temp1.add(i);
            		orderpos ++;
            	}
            	if(l1dashpos.get(i)==-1){
            		inter1.add(l1dash.get(i));
            		temp2.add(i);
            		order.add(orderpos);
            		orderpos ++;
            	}
            }
            orderpos=0;
            
            // to make more fast
            
            empty.clear();
            for(int i=0;i<inter1.size();i++){
				
				maps.add(new ArrayList<Integer>());
			}
            for(int i=0;i<inter1.size();i++){
            	last=0;
            	//System.out.println(i);
            	b = temp.subList(last, temp.size()).indexOf(inter1.get(i)); 
            	while(b != -1){
            		//System.out.println("co"+temp.subList(last, temp.size()));
            		last = last + b;
            		maps.get(i).add(last);
            		last ++;
            		b = temp.subList(last, temp.size()).indexOf(inter1.get(i));
            	}
            }
            
            //System.out.println(maps);
            //System.out.println(countOfdup);
            while(temp2pos<temp2.size()){
            	countlast = 1;
            	last=1;
            	b = temp2.get(temp2pos);
            	
            	l1dashpos.set(b, 0);
            	temp1.add(order.get(orderpos), b);
            	orderpos++;
            	lineNumber = 0;
            	
            	t = l1dash.get(b);
            	ano = temp.subList(lineNumber, temp.size()).indexOf(t);
				//ano = answer.get(answerpos);
				//answerpos++;
				ano = maps.get(temp2pos).get(last-1);
				countlast = ano;
				last++;
				l1dashpos.set(temp1.get(temp1pos),lineNumber + ano + 1);
        		temp1pos++;
            	while(last <= maps.get(temp2pos).size()){

            		lineNumber =  lineNumber+ ano + 1;
            		ano = maps.get(temp2pos).get(last-1) - countlast - 1;
            		countlast = maps.get(temp2pos).get(last-1);
            		last++;
    				l1dashpos.set(temp1.get(temp1pos),lineNumber + ano + 1);
            		temp1pos++;

            	}
            	temp2pos++;
            	}
            System.out.println("l1dashpos ready");
            //////////////////////////////// l2dashpos
            System.out.println("Calculating l2dashpos");
            temp1pos = 0;temp2pos =0;countlast=1;
            order.clear();
            maps.clear();
            temp1.clear();
            inter1.clear();
            answer.clear();
            temp2.clear();
            inter.clear();
            temp.clear();
            temp.clear();
            rev = 0;
            temp.addAll(l2dash);
            //System.out.println(temp);
            Collections.sort(l2dash);

            if(op2==2 || op2==1){
            	// > or <=
            	Collections.reverse(l2dash);
            	rev = 1;
            }
            if(rev == 1){
            	// to make it in ascending order for the binary search operation
            	Collections.reverse(l2dash);
            }
            for(int i=0;i<temp.size();i++){
            	inter.add(Collections.binarySearch(l2dash, temp.get(i))+1);
            	
            }
            if(rev == 1){
            	Collections.reverse(l2dash);
            }
            if(rev == 1){
            	b=0;
            	if(inter.size()%2==0){
            		// even
            		b = inter.size()/2;
            		for(int i =0;i<inter.size();i++){
            			if(inter.get(i)>b)
            				inter.set(i, b-inter.get(i)+b+1);
            			else
            				inter.set(i, b+b-inter.get(i)+1);
            		}
            	}
            	else{
            		// odd

            		b = (inter.size()/2)+1;
            		for(int i =0;i<inter.size();i++){
            			if(inter.get(i)>b)
            				inter.set(i, b-inter.get(i)+b);
            			else
            				inter.set(i, b+b-inter.get(i));
            		}
            	}
            	
            	rev = 0;
            }
            // inter is in correct order
            for(int i=0;i<inter.size();i++){
            	l2dashpos.add(0);
            }
            
            for(int i=0;i<inter.size();i++){
            	t = inter.get(i)-1;
            	if(l2dashpos.get(t) == 0)
            	l2dashpos.set(t, i+1);
            	else
            		l2dashpos.set(t, -1);
            }
            inter.clear();
            temp1.clear();// stores zeros
            temp2.clear();// stores -1's
            orderpos = 0;
            for(int i=0;i<l2dashpos.size();i++){
            	if(l2dashpos.get(i)==0){
            		temp1.add(i);
            		orderpos ++;
            	}
            	if(l2dashpos.get(i)==-1){
            		inter1.add(l2dash.get(i));
            		temp2.add(i);
            		order.add(orderpos);
            		orderpos ++;
            	}
            }
            orderpos=0;
            
            // to make more fast
            
            empty.clear();
            for(int i=0;i<inter1.size();i++){
				
				maps.add(new ArrayList<Integer>());
			}
            for(int i=0;i<inter1.size();i++){
            	last=0;
            	//System.out.println("Handling Duplicates: "+i+ " out of "+inter1.size());
            	b = temp.subList(last, temp.size()).indexOf(inter1.get(i)); 
            	while(b != -1){
            		//System.out.println("co"+temp.subList(last, temp.size()));
            		last = last + b;
            		maps.get(i).add(last);
            		last ++;
            		b = temp.subList(last, temp.size()).indexOf(inter1.get(i));
            	}
            }
            
            //System.out.println(maps);
            //System.out.println(countOfdup);
            while(temp2pos<temp2.size()){
            	countlast = 1;
            	last=1;
            	b = temp2.get(temp2pos);
            	
            	l2dashpos.set(b, 0);
            	temp1.add(order.get(orderpos), b);
            	orderpos++;
            	lineNumber = 0;
            	
            	t = l2dash.get(b);
            	ano = temp.subList(lineNumber, temp.size()).indexOf(t);
				//ano = answer.get(answerpos);
				//answerpos++;
				ano = maps.get(temp2pos).get(last-1);
				countlast = ano;
				last++;
				l2dashpos.set(temp1.get(temp1pos),lineNumber + ano + 1);
        		temp1pos++;
            	while(last <= maps.get(temp2pos).size()){

            		lineNumber =  lineNumber+ ano + 1;
            		ano = maps.get(temp2pos).get(last-1) - countlast - 1;
            		countlast = maps.get(temp2pos).get(last-1);
            		last++;
    				l2dashpos.set(temp1.get(temp1pos),lineNumber + ano + 1);
            		temp1pos++;

            	}
            	temp2pos++;
            	}
            System.out.println("l2dashpos ready");
            ///////////////////////////////// pdash
            
            System.out.println("Calculating pdash");
            temp.clear();
         
  			inter.clear();
  			temp.clear();
  			temp1.clear();
  			temp.addAll(l1dashpos);
  			Collections.sort(temp);
  			for(int i=0;i<l1dashpos.size();i++){
  				inter.add(Collections.binarySearch(temp, l1dashpos.get(i))+1);
  				}
  			for(int i=0;i<l1dashpos.size();i++){
  				temp1.add(0);
  			}
  			
  			for(int i=0;i<inter.size();i++){
  				temp1.set(inter.get(i)-1, i+1);
  			}
  			
  			
  			for(int i=0;i<l2dashpos.size();i++){
  				pdash.add(temp1.get(l2dashpos.get(i)-1));
  			}
  			bufferedReader.close();	
  			System.out.println("pdash ready");
  			///////////////////////////// intialization over above
  			
  			
  			/*System.out.println("Initialization .......");
            //n.nextInt();
            System.out.println("l1:\t\t"+l1);
//            System.out.println("l1pos:\t "+l1pos);
            System.out.println("l2:\t\t"+l2);
//            System.out.println("l2pos:\t "+l2pos);
            System.out.println("p:\t\t"+p);            
            System.out.println("l1dash: \t"+l1dash);
            System.out.println("l2dash: \t"+l2dash);
//            System.out.println("l2dashpos: \t"+l2dashpos);
            System.out.println("pdash: \t\t"+pdash);*/
            System.out.println("--------------------");
 
            /////////////////////////////////////////////////////////////////////////////////////////////////
            
            								//Works Fine above here
            
            /////////////////////////////////////////////////////////////////////////////////////////////////
            
         System.out.println("Computing Offsets");   
             // Computing offset o1 
         if(op1==2 || op1==3){
        	 
        	 if(op1==2){
        		 // l1dash is in ascending order
        		 for(int i=0;i<l1.size();i++){
                 	lineNumber = 0;
                 	while(lineNumber<l1dash.size() &&l1dash.get(lineNumber)<l1.get(i))
                 		lineNumber++;
                 	o1.add(lineNumber+1);
                 }	 
        		 
        	 }
        	 if(op1==3){
        		 //l1dash is in descending order

        		 for(int i=0;i<l1.size();i++){
                 	lineNumber = 0;
                 	while(lineNumber<l1dash.size() &&l1dash.get(lineNumber)>l1.get(i))
                 		lineNumber++;
                 	o1.add(lineNumber+1);
                 }
        	 }
        	 
         }
         if(op1==1 || op1==4){
        	 
        	 if(op1==1){
            		 // l1dash is in ascending order
            		 for(int i=0;i<l1.size();i++){
                     	lineNumber = 0;
                     	while(lineNumber<l1dash.size() &&l1dash.get(lineNumber)<=l1.get(i))
                     		lineNumber++;
                     	o1.add(lineNumber+1);
                     }	 
        	 }
        	 if(op1==4){
        		 //l1dash is in descending order
        		 for(int i=0;i<l1.size();i++){
                  	lineNumber = 0;
                  	while(lineNumber<l1dash.size() &&l1dash.get(lineNumber)>=l1.get(i))
                  		lineNumber++;
                  	o1.add(lineNumber+1);
                  }
        	 
        	 }
        	 
         }
   
            // Computing offset o2 
        if(op2==1 || op2==4){
       	 
       	 if(op2==1){
       		 // l2dash is in descending order
       		 for(int i=0;i<l2.size();i++){
                	lineNumber = 0;
                	while(lineNumber<l2dash.size() &&l2dash.get(lineNumber)>l2.get(i))
                		lineNumber++;
                	o2.add(lineNumber+1);
                }	 
       		 
       	 }
       	 if(op2==4){
       		 // l2dash is in ascending order
       		 for(int i=0;i<l2.size();i++){
                	lineNumber = 0;
                	while(lineNumber<l2dash.size() &&l2dash.get(lineNumber)<l2.get(i))
                		lineNumber++;
                	o2.add(lineNumber+1);
                }	 
       		 
       	 }       	 
        }
        if(op2==2 || op2==3){
       	 
          	 if(op2==2){
           		 // l2dash is in descending order
           		 for(int i=0;i<l2.size();i++){
                    	lineNumber = 0;
                    	while(lineNumber<l2dash.size() &&l2dash.get(lineNumber)>=l2.get(i))
                    		lineNumber++;
                    	o2.add(lineNumber+1);
                    }	 
           		 
           	 }
           	 if(op2==3){
           		 // l2dash is in ascending order
           		 for(int i=0;i<l2.size();i++){
                    	lineNumber = 0;
                    	while(lineNumber<l2dash.size() &&l2dash.get(lineNumber)<=l2.get(i))
                    		lineNumber++;
                    	o2.add(lineNumber+1);
                    }	 
           		 
           	 }       	        	 
        }
    
            for(int i=0;i<l1dash.size();i++){
            	bdash.add(0);
            }
           
            
           
           
            
            /*System.out.println("o1: \t\t"+o1);
            System.out.println("o2: \t\t"+o2);
            System.out.println("bdash: \t\t"+bdash);*/
            System.out.println("------------Initialization Over !!-------------");
            //n.nextInt();
            // Initialization over
/*            if((query[2].split(" ")[1].equals("2") || query[2].split(" ")[1].equals("3"))&&(query[4].split(" ")[1].equals("2") || query[4].split(" ")[1].equals("3"))){
            	// >= or <=
            	eqOff = 0;
            }
            else 
            	eqOff = 1;*/
            //System.out.println("eqOff "+eqOff);
            for(int i=0;i<l2.size();i++){
            	off2 = o2.get(i);
            	if(off2>=2){
            		for(int j=0;j<=off2-2;j++){
            			bdash.set(pdash.get(j)-1,1);
            		}
            	}
            	off1 = o1.get(p.get(i)-1);
            	if(off1<=l1dash.size()){
                	for(int j= off1-1;j<bdash.size();j++){
                		if(bdash.get(j)==1){
                			System.out.println("Ans:\t\t"+l2pos.get(i)+" "+l1dashpos.get(j));
                		}
                	}
            		
            	}

            }
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }
        catch(ArrayIndexOutOfBoundsException ex){
        	System.out.println("Not Enough Arguments provided");
        }
	}

  
  private void Query1_CondExpr(CondExpr[] expr) {

    expr[0].next  = null;
    expr[0].op    = new AttrOperator(AttrOperator.aopEQ);
    expr[0].type1 = new AttrType(AttrType.attrSymbol);
    expr[0].type2 = new AttrType(AttrType.attrSymbol);
    expr[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),1);
    expr[0].operand2.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),1);

    expr[1].op    = new AttrOperator(AttrOperator.aopEQ);
    expr[1].next  = null;
    expr[1].type1 = new AttrType(AttrType.attrSymbol);
    expr[1].type2 = new AttrType(AttrType.attrInteger);
    expr[1].operand1.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),2);
    expr[1].operand2.integer = 1;
 
    expr[2] = null;
  }

  private void Query2_CondExpr(CondExpr[] expr, CondExpr[] expr2) {

    expr[0].next  = null;
    expr[0].op    = new AttrOperator(AttrOperator.aopEQ);
    expr[0].type1 = new AttrType(AttrType.attrSymbol);
    expr[0].type2 = new AttrType(AttrType.attrSymbol);
    expr[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),1);
    expr[0].operand2.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),1);
    
    expr[1] = null;
 
    expr2[0].next  = null;
    expr2[0].op    = new AttrOperator(AttrOperator.aopEQ); 
    expr2[0].type1 = new AttrType(AttrType.attrSymbol);
    expr2[0].type2 = new AttrType(AttrType.attrSymbol);   
    expr2[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),2);
    expr2[0].operand2.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),1);
    
    expr2[1].op   = new AttrOperator(AttrOperator.aopEQ);
    expr2[1].next = null;
    expr2[1].type1 = new AttrType(AttrType.attrSymbol);
    expr2[1].type2 = new AttrType(AttrType.attrString);
    expr2[1].operand1.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),3);
    expr2[1].operand2.string = "red";
 
    expr2[2] = null;
  }  
  
  /**
 * @param expr
 */
private void Query8_CondExpr(CondExpr[] expr) {
	    expr[0].next  = null;
	    expr[0].op    = new AttrOperator(AttrOperator.aopLT);
	    expr[0].type1 = new AttrType(AttrType.attrSymbol);
	    expr[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),4);
	    expr[0].type2 = new AttrType(AttrType.attrSymbol);
	    expr[0].operand2.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),4);        
 }

private void Query12_CondExpr(CondExpr[] expr) {
    expr[0].next  = null;
    expr[0].op    = new AttrOperator(AttrOperator.aopGT);
    expr[0].type1 = new AttrType(AttrType.attrSymbol);
    expr[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),2);
    expr[0].type2 = new AttrType(AttrType.attrSymbol);
    expr[0].operand2.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),2);        
}

private void Query11_CondExpr(CondExpr[] expr) {
    expr[0].next  = null;
    expr[0].op    = new AttrOperator(AttrOperator.aopLT);
    expr[0].type1 = new AttrType(AttrType.attrSymbol);
    expr[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),2);
    expr[0].type2 = new AttrType(AttrType.attrSymbol);
    expr[0].operand2.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),2);        
}


private void Query10_CondExpr(CondExpr[] expr) {
    expr[0].next  = null;
    expr[0].op    = new AttrOperator(AttrOperator.aopNE);
    expr[0].type1 = new AttrType(AttrType.attrSymbol);
    expr[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),2);
    expr[0].type2 = new AttrType(AttrType.attrSymbol);
    expr[0].operand2.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),2);        
}

  private void Query7_CondExpr(CondExpr[] expr) {
	    expr[0].next  = null;
	    expr[0].op    = new AttrOperator(AttrOperator.aopGT);
	    expr[0].type1 = new AttrType(AttrType.attrSymbol);
	    expr[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),4);
	    expr[0].type2 = new AttrType(AttrType.attrSymbol);
	    expr[0].operand2.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),4);        

	    expr[1].op    = new AttrOperator(AttrOperator.aopLT);
	    expr[1].next  = null;
	    expr[1].type1 = new AttrType(AttrType.attrSymbol);
	    expr[1].type2 = new AttrType(AttrType.attrSymbol);
	    expr[1].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),2);
	    expr[1].operand2.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),2);	 
	    expr[2] = null;	  
  }
  
  private void Query3_CondExpr(CondExpr[] expr) {

    expr[0].next  = null;
    expr[0].op    = new AttrOperator(AttrOperator.aopEQ);
    expr[0].type1 = new AttrType(AttrType.attrSymbol);
    expr[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),1);
    expr[0].type2 = new AttrType(AttrType.attrSymbol);
    //expr[0].operand2.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),1);        
    expr[0].operand2.integer = 1;
    
    
    expr[1] = null;
  }

  private CondExpr[] Query5_CondExpr() {
    CondExpr [] expr2 = new CondExpr[3];
    expr2[0] = new CondExpr();
    
   
    expr2[0].next  = null;
    expr2[0].op    = new AttrOperator(AttrOperator.aopEQ);
    expr2[0].type1 = new AttrType(AttrType.attrSymbol);
    
    expr2[0].operand1.symbol = new FldSpec(new RelSpec(RelSpec.outer),1);
    expr2[0].type2 = new AttrType(AttrType.attrSymbol);
    
    expr2[0].operand2.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),1);
    
    expr2[1] = new CondExpr();
    expr2[1].op   = new AttrOperator(AttrOperator.aopGT);
    expr2[1].next = null;
    expr2[1].type1 = new AttrType(AttrType.attrSymbol);
   
    expr2[1].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),4);
    expr2[1].type2 = new AttrType(AttrType.attrReal);
    expr2[1].operand2.real = (float)40.0;
    

    expr2[1].next = new CondExpr();
    expr2[1].next.op   = new AttrOperator(AttrOperator.aopLT);
    expr2[1].next.next = null;
    expr2[1].next.type1 = new AttrType(AttrType.attrSymbol); // rating
    expr2[1].next.operand1.symbol = new FldSpec ( new RelSpec(RelSpec.outer),3);
    expr2[1].next.type2 = new AttrType(AttrType.attrInteger);
    expr2[1].next.operand2.integer = 7;
 
    expr2[2] = null;
    return expr2;
  }

  private void Query6_CondExpr(CondExpr[] expr, CondExpr[] expr2) {

	    expr[0].next  = null;
	    expr[0].op    = new AttrOperator(AttrOperator.aopEQ);
	    expr[0].type1 = new AttrType(AttrType.attrSymbol);
	   
	    expr[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),1);
	    expr[0].type2 = new AttrType(AttrType.attrSymbol);
	    
	    expr[0].operand2.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),1);

	    expr[1].next  = null;
	    expr[1].op    = new AttrOperator(AttrOperator.aopGT);
	    expr[1].type1 = new AttrType(AttrType.attrSymbol);
	    
	    expr[1].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),3);
	    expr[1].type2 = new AttrType(AttrType.attrInteger);
	    expr[1].operand2.integer = 7;
	 
	    expr[2] = null;
	 
	    expr2[0].next  = null;
	    expr2[0].op    = new AttrOperator(AttrOperator.aopEQ);
	    expr2[0].type1 = new AttrType(AttrType.attrSymbol);
	    
	    expr2[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),2);
	    expr2[0].type2 = new AttrType(AttrType.attrSymbol);
	    
	    expr2[0].operand2.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),1);

	    expr2[1].next = null;
	    expr2[1].op   = new AttrOperator(AttrOperator.aopEQ);
	    expr2[1].type1 = new AttrType(AttrType.attrSymbol);
	    
	    expr2[1].operand1.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),3);
	    expr2[1].type2 = new AttrType(AttrType.attrString);
	    expr2[1].operand2.string = "red";
	 
	    expr2[2] = null;
	  }
  
  public void Query1() {
	  
	    System.out.print("**********************Query1 strating *********************\n");
	    boolean status = OK;
	    
	    // Sailors, Boats, Reserves Queries.
	    System.out.print ("Query: Find the names of sailors who have reserved "
			      + "boat number 1.\n"
			      + "       and print out the date of reservation.\n\n"
			      + "  SELECT S.sname, R.date\n"
			      + "  FROM   Sailors S, Reserves R\n"
			      + "  WHERE  S.sid = R.sid AND R.bid = 1\n\n");
	    
	    System.out.print ("\n(Tests FileScan, Projection, and Sort-Merge Join)\n");
	 
	    CondExpr[] outFilter = new CondExpr[3];
	    outFilter[0] = new CondExpr();
	    outFilter[1] = new CondExpr();
	    outFilter[2] = new CondExpr();
	 
	    Query1_CondExpr(outFilter);
	 
	    Tuple t = new Tuple();
	    
	    AttrType [] Stypes = new AttrType[4];
	    Stypes[0] = new AttrType (AttrType.attrInteger);
	    Stypes[1] = new AttrType (AttrType.attrString);
	    Stypes[2] = new AttrType (AttrType.attrInteger);
	    Stypes[3] = new AttrType (AttrType.attrReal);

	    //SOS
	    short [] Ssizes = new short[1];
	    Ssizes[0] = 30; //first elt. is 30
	    
	    FldSpec [] Sprojection = new FldSpec[4];
	    Sprojection[0] = new FldSpec(new RelSpec(RelSpec.outer), 1);
	    Sprojection[1] = new FldSpec(new RelSpec(RelSpec.outer), 2);
	    Sprojection[2] = new FldSpec(new RelSpec(RelSpec.outer), 3);
	    Sprojection[3] = new FldSpec(new RelSpec(RelSpec.outer), 4);

	    CondExpr [] selects = new CondExpr [1];
	    selects = null;
	    
	 
	    FileScan am = null;
	    try {
	      am  = new FileScan("sailors.in", Stypes, Ssizes, 
					  (short)4, (short)4,
					  Sprojection, null);
	    }
	    catch (Exception e) {
	      status = FAIL;
	      System.err.println (""+e);
	    }

	    if (status != OK) {
	      //bail out
	      System.err.println ("*** Error setting up scan for sailors");
	      Runtime.getRuntime().exit(1);
	    }
	    
	    AttrType [] Rtypes = new AttrType[3];
	    Rtypes[0] = new AttrType (AttrType.attrInteger);
	    Rtypes[1] = new AttrType (AttrType.attrInteger);
	    Rtypes[2] = new AttrType (AttrType.attrString);

	    short [] Rsizes = new short[1];
	    Rsizes[0] = 15; 
	    FldSpec [] Rprojection = new FldSpec[3];
	    Rprojection[0] = new FldSpec(new RelSpec(RelSpec.outer), 1);
	    Rprojection[1] = new FldSpec(new RelSpec(RelSpec.outer), 2);
	    Rprojection[2] = new FldSpec(new RelSpec(RelSpec.outer), 3);
	 
	    FileScan am2 = null;
	    try {
	      am2 = new FileScan("reserves.in", Rtypes, Rsizes, 
					  (short)3, (short) 3,
					  Rprojection, null);
	    }
	    catch (Exception e) {
	      status = FAIL;
	      System.err.println (""+e);
	    }

	    if (status != OK) {
	      //bail out
	      System.err.println ("*** Error setting up scan for reserves");
	      Runtime.getRuntime().exit(1);
	    }
	   
	    
	    FldSpec [] proj_list = new FldSpec[2];
	    proj_list[0] = new FldSpec(new RelSpec(RelSpec.outer), 2);
	    proj_list[1] = new FldSpec(new RelSpec(RelSpec.innerRel), 3);

	    AttrType [] jtype = new AttrType[2];
	    jtype[0] = new AttrType (AttrType.attrString);
	    jtype[1] = new AttrType (AttrType.attrString);
	 
	    TupleOrder ascending = new TupleOrder(TupleOrder.Ascending);
	    SortMerge sm = null;
	    try {
	      sm = new SortMerge(Stypes, 4, Ssizes,
				 Rtypes, 3, Rsizes,
				 1, 4, 
				 1, 4, 
				 
				 10,
				 am, am2, 
				 false, false, ascending,
				 outFilter, proj_list, 2);
	    }
	    catch (Exception e) {
	      System.err.println("*** join error in SortMerge constructor ***"); 
	      status = FAIL;
	      System.err.println (""+e);
	      e.printStackTrace();
	    }

	    if (status != OK) {
	      //bail out
	      System.err.println ("*** Error constructing SortMerge");
	      Runtime.getRuntime().exit(1);
	    }

	   
	 
	    QueryCheck qcheck1 = new QueryCheck(1);
	 
	   
	    t = null;
	 
	    try {
	      while ((t = sm.get_next()) != null) {
	        t.print(jtype);

	        qcheck1.Check(t);
	      }
	    }
	    catch (Exception e) {
	      System.err.println (""+e);
	       e.printStackTrace();
	       status = FAIL;
	    }
	    if (status != OK) {
	      //bail out
	      System.err.println ("*** Error in get next tuple ");
	      Runtime.getRuntime().exit(1);
	    }
	    
	    qcheck1.report(1);
	    try {
	      sm.close();
	    }
	    catch (Exception e) {
	      status = FAIL;
	      e.printStackTrace();
	    }
	    System.out.println ("\n"); 
	    if (status != OK) {
	      //bail out
	      System.err.println ("*** Error in closing ");
	      Runtime.getRuntime().exit(1);
	    }
	  }
  
    
  public void Query2() {
    System.out.print("**********************Query2 strating *********************\n");
    boolean status = OK;

    // Sailors, Boats, Reserves Queries.
    System.out.print 
      ("Query: Find the names of sailors who have reserved "
       + "a red boat\n"
       + "       and return them in alphabetical order.\n\n"
       + "  SELECT   S.sname\n"
       + "  FROM     Sailors S, Boats B, Reserves R\n"
       + "  WHERE    S.sid = R.sid AND R.bid = B.bid AND B.color = 'red'\n"
       + "  ORDER BY S.sname\n"
       + "Plan used:\n"
       + " Sort (Pi(sname) (Sigma(B.color='red')  "
       + "|><|  Pi(sname, bid) (S  |><|  R)))\n\n"
       + "(Tests File scan, Index scan ,Projection,  index selection,\n "
       + "sort and simple nested-loop join.)\n\n");
    
    // Build Index first
    IndexType b_index = new IndexType (IndexType.B_Index);

   
    //ExtendedSystemDefs.MINIBASE_CATALOGPTR.addIndex("sailors.in", "sid", b_index, 1);
    // }
    //catch (Exception e) {
    // e.printStackTrace();
    // System.err.print ("Failure to add index.\n");
      //  Runtime.getRuntime().exit(1);
    // }
    
    


    CondExpr [] outFilter  = new CondExpr[2];
    outFilter[0] = new CondExpr();
    outFilter[1] = new CondExpr();

    CondExpr [] outFilter2 = new CondExpr[3];
    outFilter2[0] = new CondExpr();
    outFilter2[1] = new CondExpr();
    outFilter2[2] = new CondExpr();

    Query2_CondExpr(outFilter, outFilter2);
    Tuple t = new Tuple();
    t = null;

    AttrType [] Stypes = {
      new AttrType(AttrType.attrInteger), 
      new AttrType(AttrType.attrString), 
      new AttrType(AttrType.attrInteger), 
      new AttrType(AttrType.attrReal)
    };

    AttrType [] Stypes2 = {
      new AttrType(AttrType.attrInteger), 
      new AttrType(AttrType.attrString), 
    };

    short []   Ssizes = new short[1];
    Ssizes[0] = 30;
    AttrType [] Rtypes = {
      new AttrType(AttrType.attrInteger), 
      new AttrType(AttrType.attrInteger), 
      new AttrType(AttrType.attrString), 
    };

    short  []  Rsizes = new short[1] ;
    Rsizes[0] = 15;
    AttrType [] Btypes = {
      new AttrType(AttrType.attrInteger), 
      new AttrType(AttrType.attrString), 
      new AttrType(AttrType.attrString), 
    };

    short  []  Bsizes = new short[2];
    Bsizes[0] =30;
    Bsizes[1] =20;
    AttrType [] Jtypes = {
      new AttrType(AttrType.attrString), 
      new AttrType(AttrType.attrInteger), 
    };

    short  []  Jsizes = new short[1];
    Jsizes[0] = 30;
    AttrType [] JJtype = {
      new AttrType(AttrType.attrString), 
    };

    short [] JJsize = new short[1];
    JJsize[0] = 30;
    FldSpec []  proj1 = {
       new FldSpec(new RelSpec(RelSpec.outer), 2),
       new FldSpec(new RelSpec(RelSpec.innerRel), 2)
    }; // S.sname, R.bid

    FldSpec [] proj2  = {
       new FldSpec(new RelSpec(RelSpec.outer), 1)
    };
 
    FldSpec [] Sprojection = {
       new FldSpec(new RelSpec(RelSpec.outer), 1),
       new FldSpec(new RelSpec(RelSpec.outer), 2),
       // new FldSpec(new RelSpec(RelSpec.outer), 3),
       // new FldSpec(new RelSpec(RelSpec.outer), 4)
    };
 
    CondExpr [] selects = new CondExpr[1];
    selects[0] = null;
    
    
    //IndexType b_index = new IndexType(IndexType.B_Index);
    iterator.Iterator am = null;
   

    //_______________________________________________________________
    //*******************create an scan on the heapfile**************
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // create a tuple of appropriate size
        Tuple tt = new Tuple();
    try {
      tt.setHdr((short) 4, Stypes, Ssizes);
    }
    catch (Exception e) {
      status = FAIL;
      e.printStackTrace();
    }

    int sizett = tt.size();
    tt = new Tuple(sizett);
    try {
      tt.setHdr((short) 4, Stypes, Ssizes);
    }
    catch (Exception e) {
      status = FAIL;
      e.printStackTrace();
    }
    Heapfile        f = null;
    try {
      f = new Heapfile("sailors.in");
    }
    catch (Exception e) {
      status = FAIL;
      e.printStackTrace();
    }
    
    Scan scan = null;
    
    try {
      scan = new Scan(f);
    }
    catch (Exception e) {
      status = FAIL;
      e.printStackTrace();
      Runtime.getRuntime().exit(1);
    }

    // create the index file
    BTreeFile btf = null;
    try {
      btf = new BTreeFile("BTreeIndex", AttrType.attrInteger, 4, 1); 
    }
    catch (Exception e) {
      status = FAIL;
      e.printStackTrace();
      Runtime.getRuntime().exit(1);
    }
    
    RID rid = new RID();
    int key =0;
    Tuple temp = null;
    
    try {
      temp = scan.getNext(rid);
    }
    catch (Exception e) {
      status = FAIL;
      e.printStackTrace();
    }
    while ( temp != null) {
      tt.tupleCopy(temp);
      
      try {
	key = tt.getIntFld(1);
      }
      catch (Exception e) {
	status = FAIL;
	e.printStackTrace();
      }
      
      try {
	btf.insert(new IntegerKey(key), rid); 
      }
      catch (Exception e) {
	status = FAIL;
	e.printStackTrace();
      }

      try {
	temp = scan.getNext(rid);
      }
      catch (Exception e) {
	status = FAIL;
	e.printStackTrace();
      }
    }
    
    // close the file scan
    scan.closescan();
    
    
    //_______________________________________________________________
    //*******************close an scan on the heapfile**************
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    System.out.print ("After Building btree index on sailors.sid.\n\n");
    try {
      am = new IndexScan ( b_index, "sailors.in",
			   "BTreeIndex", Stypes, Ssizes, 4, 2,
			   Sprojection, null, 1, false);
    }
    
    catch (Exception e) {
      System.err.println ("*** Error creating scan for Index scan");
      System.err.println (""+e);
      Runtime.getRuntime().exit(1);
    }
   
    
    NestedLoopsJoins nlj = null;
    try {
      nlj = new NestedLoopsJoins (Stypes2, 2, Ssizes,
				  Rtypes, 3, Rsizes,
				  10,
				  am, "reserves.in",
				  outFilter, null, proj1, 2);
    }
    catch (Exception e) {
      System.err.println ("*** Error preparing for nested_loop_join");
      System.err.println (""+e);
      e.printStackTrace();
      Runtime.getRuntime().exit(1);
    }

     NestedLoopsJoins nlj2 = null ; 
    try {
      nlj2 = new NestedLoopsJoins (Jtypes, 2, Jsizes,
				   Btypes, 3, Bsizes,
				   10,
				   nlj, "boats.in",
				   outFilter2, null, proj2, 1);
    }
    catch (Exception e) {
      System.err.println ("*** Error preparing for nested_loop_join");
      System.err.println (""+e);
      Runtime.getRuntime().exit(1);
    }
    
    TupleOrder ascending = new TupleOrder(TupleOrder.Ascending);
    Sort sort_names = null;
    try {
      sort_names = new Sort (JJtype,(short)1, JJsize,
			     (iterator.Iterator) nlj2, 1, ascending, JJsize[0], 10);
    }
    catch (Exception e) {
      System.err.println ("*** Error preparing for nested_loop_join");
      System.err.println (""+e);
      Runtime.getRuntime().exit(1);
    }
    
    
    QueryCheck qcheck2 = new QueryCheck(2);
    
   
    t = null;
    try {
      while ((t = sort_names.get_next()) != null) {
        t.print(JJtype);
        qcheck2.Check(t);
      }
    }
    catch (Exception e) {
      System.err.println (""+e);
      e.printStackTrace();
      Runtime.getRuntime().exit(1);
    }

    qcheck2.report(2);

    System.out.println ("\n"); 
    try {
      sort_names.close();
    }
    catch (Exception e) {
      status = FAIL;
      e.printStackTrace();
    }
    
    if (status != OK) {
      //bail out
   
      Runtime.getRuntime().exit(1);
      }
  }

  public void Query3() {
	    System.out.print("**********************Query3 strating *********************\n"); 
	    boolean status = OK;

	        // Sailors, Boats, Reserves Queries.
	 
	    System.out.print 
	      ( "Query: Find the names of sailors who have reserved a boat.\n\n"
		+ "  SELECT S.sname\n"
		+ "  FROM   Sailors S, Reserves R\n"
		+ "  WHERE  S.sid = R.sid\n\n"
		+ "(Tests FileScan, Projection, and SortMerge Join.)\n\n");
	    
	    CondExpr [] outFilter = new CondExpr[2];
	    outFilter[0] = new CondExpr();
	    outFilter[1] = new CondExpr();
	 
	    Query3_CondExpr(outFilter);
	 
	    Tuple t = new Tuple();
	    t = null;
	 
	    AttrType Stypes[] = {
	      new AttrType(AttrType.attrInteger),
	      new AttrType(AttrType.attrString),
	      new AttrType(AttrType.attrInteger),
	      new AttrType(AttrType.attrReal)
	    };
	    short []   Ssizes = new short[1];
	    Ssizes[0] = 30;

	    AttrType [] Rtypes = {
	      new AttrType(AttrType.attrInteger),
	      new AttrType(AttrType.attrInteger),
	      new AttrType(AttrType.attrString),
	    };
	    short  []  Rsizes = new short[1];
	    Rsizes[0] =15;
	 
	    FldSpec [] Sprojection = {
	       new FldSpec(new RelSpec(RelSpec.outer), 1),
	       new FldSpec(new RelSpec(RelSpec.outer), 2),
	       new FldSpec(new RelSpec(RelSpec.outer), 3),
	       new FldSpec(new RelSpec(RelSpec.outer), 4)
	    };

	    CondExpr[] selects = new CondExpr [1];
	    selects = null;
	 
	    iterator.Iterator am = null;
	    try {
	      am  = new FileScan("sailors.in", Stypes, Ssizes,
					  (short)4, (short) 4,
					  Sprojection, null);
	    }
	    catch (Exception e) {
	      status = FAIL;
	      System.err.println (""+e);
	    }
	 
	    if (status != OK) {
	      //bail out
	      System.err.println ("*** Error setting up scan for sailors");
	      Runtime.getRuntime().exit(1);
	    }

	    FldSpec [] Rprojection = {
	       new FldSpec(new RelSpec(RelSpec.outer), 1),
	       new FldSpec(new RelSpec(RelSpec.outer), 2),
	       new FldSpec(new RelSpec(RelSpec.outer), 3)
	    }; 
	 
	    iterator.Iterator am2 = null;
	    try {
	      am2 = new FileScan("reserves.in", Rtypes, Rsizes, 
					  (short)3, (short)3,
					  Rprojection, null);
	    }
	    catch (Exception e) {
	      status = FAIL;
	      System.err.println (""+e);
	    }
	    
	    if (status != OK) {
	      //bail out
	      System.err.println ("*** Error setting up scan for reserves");
	      Runtime.getRuntime().exit(1);
	    }

	    FldSpec [] proj_list = {
	      new FldSpec(new RelSpec(RelSpec.outer), 2)
	    };

	    AttrType [] jtype     = { new AttrType(AttrType.attrString) };
	 
	    TupleOrder ascending = new TupleOrder(TupleOrder.Ascending);
	    SortMerge sm = null;
	    try {
	      sm = new SortMerge(Stypes, 4, Ssizes,
				 Rtypes, 3, Rsizes,
				 1, 4,
				 1, 4,
				 10,
				 am, am2,
				 false, false, ascending,
				 outFilter, proj_list, 1);
	    }
	    catch (Exception e) {
	      status = FAIL;
	      System.err.println (""+e);
	    }
	 
	    if (status != OK) {
	      //bail out
	      System.err.println ("*** Error constructing SortMerge");
	      Runtime.getRuntime().exit(1);
	    }
	 
	    QueryCheck qcheck3 = new QueryCheck(3);
	 
	   
	    t = null;
	 
	    try {
	      while ((t = sm.get_next()) != null) {
	        t.print(jtype);
	        qcheck3.Check(t);
	      }
	    }
	    catch (Exception e) {
	      System.err.println (""+e);
	      e.printStackTrace();
	       Runtime.getRuntime().exit(1);
	    }
	 
	 
	    qcheck3.report(3);
	 
	    System.out.println ("\n"); 
	    try {
	      sm.close();
	    }
	    catch (Exception e) {
	      status = FAIL;
	      e.printStackTrace();
	    }
	    
	    if (status != OK) {
	      //bail out
	      System.err.println ("*** Error setting up scan for sailors");
	      Runtime.getRuntime().exit(1);
	    }
	  }

  
  
  
  public void Query8() throws JoinsException, IndexException, InvalidTupleSizeException, InvalidTypeException, PageNotReadException, TupleUtilsException, PredEvalException, SortException, LowMemException, UnknowAttrType, UnknownKeyTypeException, IOException, Exception {
	    System.out.print("**********************Query8 strating *********************\n"); 
	    boolean status = OK;
	 
	    System.out.print 
	      ( "Query: Find the names of sailors who have reserved a boat.\n\n"
		+ "  SELECT S.sname, R.sname\n"
		+ "  FROM   Sailors S, Sailors R\n"
		+ "  WHERE  S.age < R.age \n\n"
		+ "(Tests FileScan, Projection, and NestedLoop Join.)\n\n");
	    
	    CondExpr[] outFilter = new CondExpr[3];
	    outFilter[0] = new CondExpr();
	    outFilter[1] = new CondExpr();
	    outFilter[2] = new CondExpr();
	    
	    Query8_CondExpr(outFilter);
	    outFilter[1] = outFilter[0];	 
	    outFilter[2] = null;	  
	    
	    AttrType Stypes[] = {
	  	      new AttrType(AttrType.attrInteger),
	  	      new AttrType(AttrType.attrInteger),
	  	      new AttrType(AttrType.attrInteger),
	  	      new AttrType(AttrType.attrInteger)
	  	    };
	    
/*	    AttrType Stypes[] = {
	      new AttrType(AttrType.attrInteger),
	      new AttrType(AttrType.attrString),
	      new AttrType(AttrType.attrInteger),
	      new AttrType(AttrType.attrReal)
	    };*/
	    
	    short []   Ssizes = new short[1];
	    Ssizes[0] = 30;

	    FldSpec [] Sprojection = {
	       new FldSpec(new RelSpec(RelSpec.outer), 1),
	       new FldSpec(new RelSpec(RelSpec.outer), 2),
	       new FldSpec(new RelSpec(RelSpec.outer), 3),
	       new FldSpec(new RelSpec(RelSpec.outer), 4)
	    };

	    CondExpr[] selects = new CondExpr [1];
	    selects = null;
	 
	    iterator.Iterator am = null;
	    try {
	      am  = new FileScan("integerQuery.in", Stypes, Ssizes,
					  (short)4, (short) 4,
					  Sprojection, null);
	    }
	    catch (Exception e) {
	      status = FAIL;
	      System.err.println (""+e);
	    }
	 
	    if (status != OK) {
	      //bail out
	      System.err.println ("*** Error setting up scan for sailors");
	      Runtime.getRuntime().exit(1);
	    }
	 
	      FldSpec [] proj_list = {
	      new FldSpec(new RelSpec(RelSpec.outer), 2),
	      new FldSpec(new RelSpec(RelSpec.innerRel), 2)
	    };

	    //AttrType[] jtype = { new AttrType(AttrType.attrString), new AttrType(AttrType.attrString) };
	      AttrType[] jtype = { new AttrType(AttrType.attrInteger), new AttrType(AttrType.attrInteger) };

	    IESelfJoin inl = null;
	    try {
		inl = new IESelfJoin (Stypes, 4, Ssizes,
					    Stypes, 4, Ssizes,
					    10,
					    am, "integerQuery.in",
					    outFilter, null, proj_list, 2, nrows);
	    }
	    catch (Exception e) {
		System.err.println ("*** Error preparing for nested_loop_join");
		System.err.println (""+e);
		e.printStackTrace();
		Runtime.getRuntime().exit(1);
	    }
	         
	    inl.IESelfJoinLogicInit(false);
	
	    System.out.println ("\n"); 
	    
	    if (status != OK) {
	      //bail out
	      System.err.println ("*** Error setting up scan for sailors");
	      Runtime.getRuntime().exit(1);
	    }
	  }

  public void Query10() throws JoinsException, IndexException, InvalidTupleSizeException, InvalidTypeException, PageNotReadException, TupleUtilsException, PredEvalException, SortException, LowMemException, UnknowAttrType, UnknownKeyTypeException, IOException, Exception {
	    System.out.print("**********************Query8 strating *********************\n"); 
	    boolean status = OK;
	 
	    System.out.print 
	      ( "Query: Find the names of sailors who have reserved a boat.\n\n"
		+ "  SELECT S.sname, R.sname\n"
		+ "  FROM   Sailors S, Sailors R\n"
		+ "  WHERE  S.age != R.age \n\n"
		+ "(Tests FileScan, Projection, and NestedLoop Join.)\n\n");
	    
	    CondExpr[] outFilter = new CondExpr[3];
	    outFilter[0] = new CondExpr();
	    outFilter[1] = new CondExpr();
	    outFilter[2] = new CondExpr();

	    CondExpr[] outFilter2 = new CondExpr[3];
	    outFilter2[0] = new CondExpr();
	    outFilter2[1] = new CondExpr();
	    outFilter2[2] = new CondExpr();

	    Query10_CondExpr(outFilter);
	    if(outFilter[0].op.attrOperator == AttrOperator.aopNE) {
	    	Query11_CondExpr(outFilter);
		    outFilter[1] = outFilter[0];	 
		    outFilter[2] = null;	  
		    
	    	Query12_CondExpr(outFilter2);
		    outFilter2[1] = outFilter[0];	 
		    outFilter2[2] = null;	  

	    }
	    
	    AttrType Stypes[] = {
	  	      new AttrType(AttrType.attrInteger),
	  	      new AttrType(AttrType.attrInteger),
	  	      new AttrType(AttrType.attrInteger),
	  	      new AttrType(AttrType.attrInteger)
	  	    };
	    
/*	    AttrType Stypes[] = {
	      new AttrType(AttrType.attrInteger),
	      new AttrType(AttrType.attrString),
	      new AttrType(AttrType.attrInteger),
	      new AttrType(AttrType.attrReal)
	    };*/
	    
	    short []   Ssizes = new short[1];
	    Ssizes[0] = 30;

	    FldSpec [] Sprojection = {
	       new FldSpec(new RelSpec(RelSpec.outer), 1),
	       new FldSpec(new RelSpec(RelSpec.outer), 2),
	       new FldSpec(new RelSpec(RelSpec.outer), 3),
	       new FldSpec(new RelSpec(RelSpec.outer), 4)
	    };

	    CondExpr[] selects = new CondExpr [1];
	    selects = null;
	 
	    iterator.Iterator am = null;
	    try {
	      am  = new FileScan("integerQuery.in", Stypes, Ssizes,
					  (short)4, (short) 4,
					  Sprojection, null);
	    }
	    catch (Exception e) {
	      status = FAIL;
	      System.err.println (""+e);
	    }
	 
	    if (status != OK) {
	      //bail out
	      System.err.println ("*** Error setting up scan for sailors");
	      Runtime.getRuntime().exit(1);
	    }
	 
	      FldSpec [] proj_list = {
	      new FldSpec(new RelSpec(RelSpec.outer), 2),
	      new FldSpec(new RelSpec(RelSpec.innerRel), 2)
	    };

	    //AttrType[] jtype = { new AttrType(AttrType.attrString), new AttrType(AttrType.attrString) };
	      AttrType[] jtype = { new AttrType(AttrType.attrInteger), new AttrType(AttrType.attrInteger) };

	    IESelfJoin inl = null;
	    try {
		inl = new IESelfJoin (Stypes, 4, Ssizes,
					    Stypes, 4, Ssizes,
					    10,
					    am, "integerQuery.in",
					    outFilter, null, proj_list, 2, nrows);
	    }
	    catch (Exception e) {
		System.err.println ("*** Error preparing for nested_loop_join");
		System.err.println (""+e);
		e.printStackTrace();
		Runtime.getRuntime().exit(1);
	    }
	         
	    inl.IESelfJoinLogicInit(true);

	    try {
		inl = new IESelfJoin (Stypes, 4, Ssizes,
					    Stypes, 4, Ssizes,
					    10,
					    am, "integerQuery.in",
					    outFilter2, null, proj_list, 2, nrows);
	    }
	    catch (Exception e) {
		System.err.println ("*** Error preparing for nested_loop_join");
		System.err.println (""+e);
		e.printStackTrace();
		Runtime.getRuntime().exit(1);
	    }
	         
	    inl.IESelfJoinLogicInit(true);

	    System.out.println ("\n"); 
	    
	    if (status != OK) {
	      //bail out
	      System.err.println ("*** Error setting up scan for sailors");
	      Runtime.getRuntime().exit(1);
	    }
	  }

  
  public void Query9() throws JoinsException, IndexException, InvalidTupleSizeException, 
  InvalidTypeException, PageNotReadException, TupleUtilsException, PredEvalException, 
  SortException, LowMemException, UnknowAttrType, UnknownKeyTypeException, IOException, Exception {
	  System.out.print("**********************Query9 strating *********************\n"); 
	    boolean status = OK;
	 
	    System.out.print 
	      ( "Query: Find the names of sailors who have reserved a boat.\n\n"
		+ "  SELECT S.sname, R.sname\n"
		+ "  FROM   Sailors S, Sailors R\n"
		+ "  WHERE  S.age < R.age AND S.rating > R.rating \n\n"
		+ "(Tests FileScan, Projection, and NestedLoop Join.)\n\n");
	    
	    CondExpr[] outFilter = new CondExpr[3];
	    outFilter[0] = new CondExpr();
	    outFilter[1] = new CondExpr();
	    outFilter[2] = new CondExpr();
	 
	    Query7_CondExpr(outFilter);

	    Tuple t = new Tuple();
	    t = null;
	    
/*	    Query8_CondExpr(outFilter);
	    outFilter[1] = outFilter[0];	 
	    outFilter[2] = null;	  
*/
	 
	    AttrType Stypes[] = {
	      new AttrType(AttrType.attrInteger),
	      new AttrType(AttrType.attrInteger),
	      new AttrType(AttrType.attrInteger),
	      new AttrType(AttrType.attrInteger)
	    };
	    
	    short []   Ssizes = new short[1];
	    Ssizes[0] = 30;

	    AttrType [] Rtypes = {
	    	      new AttrType(AttrType.attrInteger),
	    	      new AttrType(AttrType.attrInteger),
	    	      new AttrType(AttrType.attrInteger),
	    	      new AttrType(AttrType.attrInteger)
	    };
	    short  []  Rsizes = new short[1];
	    Rsizes[0] =30;
	 
	    FldSpec [] Sprojection = {
	       new FldSpec(new RelSpec(RelSpec.outer), 1),
	       new FldSpec(new RelSpec(RelSpec.outer), 2),
	       new FldSpec(new RelSpec(RelSpec.outer), 3),
	       new FldSpec(new RelSpec(RelSpec.outer), 4)
	    };

	    CondExpr[] selects = new CondExpr [1];
	    selects = null;
	 
	    iterator.Iterator am = null;
	    try {
	      am  = new FileScan("integerQuery.in", Stypes, Ssizes,
					  (short)4, (short) 4,
					  Sprojection, null);
	    }
	    catch (Exception e) {
	      status = FAIL;
	      System.err.println (""+e);
	    }
	 
	    if (status != OK) {
	      //bail out
	      System.err.println ("*** Error setting up scan for sailors");
	      Runtime.getRuntime().exit(1);
	    }

	    FldSpec [] Rprojection = {
	    	       new FldSpec(new RelSpec(RelSpec.outer), 1),
	    	       new FldSpec(new RelSpec(RelSpec.outer), 2),
	    	       new FldSpec(new RelSpec(RelSpec.outer), 3),
	    	       new FldSpec(new RelSpec(RelSpec.outer), 4)
	    }; 
	 
	    iterator.Iterator am2 = null;
	    try {
	      am2 = new FileScan("integerQuery.in", Rtypes, Rsizes, 
					  (short)4, (short)4,
					  Rprojection, null);
	    }
	    catch (Exception e) {
	      status = FAIL;
	      System.err.println (""+e);
	    }
	    
	    if (status != OK) {
	      //bail out
	      System.err.println ("*** Error setting up scan for reserves");
	      Runtime.getRuntime().exit(1);
	    }

	    FldSpec [] proj_list = {
	      new FldSpec(new RelSpec(RelSpec.outer), 2),
	      new FldSpec(new RelSpec(RelSpec.innerRel), 2)
	    };

	    //AttrType[] jtype = { new AttrType(AttrType.attrString), new AttrType(AttrType.attrString) };
	    AttrType[] jtype = { new AttrType(AttrType.attrInteger), new AttrType(AttrType.attrInteger) };

	    NestedLoopsJoins inl = null;
	    try {
		inl = new NestedLoopsJoins (Stypes, 4, Ssizes,
					    Rtypes, 4, Rsizes,
					    10,
					    am, "integerQuery.in",
					    outFilter, null, proj_list, 2);
	    }
	    catch (Exception e) {
		System.err.println ("*** Error preparing for nested_loop_join");
		System.err.println (""+e);
		e.printStackTrace();
		Runtime.getRuntime().exit(1);
	    }
	     
	/*    TupleOrder ascending = new TupleOrder(TupleOrder.Ascending);
	    Sort sort_names = null;
	    try {
		sort_names = new Sort (Stypes,(short)1, Ssizes,
				       (iterator.Iterator) inl, 1, ascending, Ssizes[0], 10);
	    }
	    catch (Exception e) {
		System.err.println ("*** Error preparing for sorting");
		System.err.println (""+e);
		Runtime.getRuntime().exit(1);
	    }
	*/       
	    System.out.print( "After sorting the output tuples.\n");
	   
	    int count = 0;
	    try {
		while ((t =inl.get_next()) !=null) {
				t.print(jtype);
				count++;
		  //qcheck6.Check(t);
		}
	    }catch (Exception e) {
		System.err.println ("*** Error preparing for get_next tuple");
		System.err.println (""+e);
		Runtime.getRuntime().exit(1);
	    }
	    System.out.print(count);
	    
	    //qcheck6.report(6);
	    
	    System.out.println ("\n"); 
	    /*try {
		sort_names.close();
	    }
	    catch (Exception e) {
		status = FAIL;
		e.printStackTrace();
	    }
	        */
	    if (status != OK) {
	      //bail out
	      System.err.println ("*** Error setting up scan for sailors");
	      Runtime.getRuntime().exit(1);
	    }
	  }

   public void Query7() throws JoinsException, IndexException, InvalidTupleSizeException, InvalidTypeException, PageNotReadException, TupleUtilsException, PredEvalException, SortException, LowMemException, UnknowAttrType, UnknownKeyTypeException, IOException, Exception {
    System.out.print("**********************Query7 strating *********************\n"); 
    boolean status = OK;
 
    System.out.print 
      ( "Query: Find the names of sailors who have reserved a boat.\n\n"
	+ "  SELECT S.sname, R.sname\n"
	+ "  FROM   Sailors S, Sailors R\n"
	+ "  WHERE  S.age > R.age AND S.rating < R.rating \n\n"
	+ "(Tests FileScan, Projection, and NestedLoop Join.)\n\n");
    
    CondExpr[] outFilter = new CondExpr[3];
    outFilter[0] = new CondExpr();
    outFilter[1] = new CondExpr();
    outFilter[2] = new CondExpr();
 
    Query7_CondExpr(outFilter);

    AttrType Stypes[] = {
    	      new AttrType(AttrType.attrInteger),
    	      new AttrType(AttrType.attrInteger),
    	      new AttrType(AttrType.attrInteger),
    	      new AttrType(AttrType.attrInteger)
    	    };

/*    AttrType Stypes[] = {
      new AttrType(AttrType.attrInteger),
      new AttrType(AttrType.attrString),
      new AttrType(AttrType.attrInteger),
      new AttrType(AttrType.attrReal)
    };
*/    
    short []   Ssizes = new short[1];
    Ssizes[0] = 30;

    FldSpec [] Sprojection = {
       new FldSpec(new RelSpec(RelSpec.outer), 1),
       new FldSpec(new RelSpec(RelSpec.outer), 2),
       new FldSpec(new RelSpec(RelSpec.outer), 3),
       new FldSpec(new RelSpec(RelSpec.outer), 4)
    };

    CondExpr[] selects = new CondExpr [1];
    selects = null;
 
    iterator.Iterator am = null;
    try {
        am  = new FileScan("integerQuery.in", Stypes, Ssizes,
  				  (short)4, (short) 4,
  				  Sprojection, null);

/*      am  = new FileScan("sailors.in", Stypes, Ssizes,
				  (short)4, (short) 4,
				  Sprojection, null);
*/    }
    catch (Exception e) {
      status = FAIL;
      System.err.println (""+e);
    }
 
    if (status != OK) {
      //bail out
      System.err.println ("*** Error setting up scan for sailors");
      Runtime.getRuntime().exit(1);
    }
 
      FldSpec [] proj_list = {
      new FldSpec(new RelSpec(RelSpec.outer), 2),
      new FldSpec(new RelSpec(RelSpec.innerRel), 2)
    };

    //AttrType[] jtype = { new AttrType(AttrType.attrString), new AttrType(AttrType.attrString) };
      AttrType[] jtype = { new AttrType(AttrType.attrInteger), new AttrType(AttrType.attrInteger) };
      
    IESelfJoin inl = null;
    try {
	inl = new IESelfJoin (Stypes, 4, Ssizes,
				    Stypes, 4, Ssizes,
				    10,
				    am, "integerQuery.in",
				    outFilter, null, proj_list, 2, nrows);
    }
    catch (Exception e) {
	System.err.println ("*** Error preparing for nested_loop_join");
	System.err.println (""+e);
	e.printStackTrace();
	Runtime.getRuntime().exit(1);
    }
         
    inl.IESelfJoinLogicInit(true);        
    System.out.println("Bloomfilter value "+inl.get_bloom_filter_pos(10,nrows));
    System.out.println ("\n"); 
    
    if (status != OK) {
      //bail out
      System.err.println ("*** Error setting up scan for sailors");
      Runtime.getRuntime().exit(1);
    }
  }

   public void Query4() {
     System.out.print("**********************Query4 strating *********************\n");
    boolean status = OK;

    // Sailors, Boats, Reserves Queries.
 
    System.out.print 
      ("Query: Find the names of sailors who have reserved a boat\n"
       + "       and print each name once.\n\n"
       + "  SELECT DISTINCT S.sname\n"
       + "  FROM   Sailors S, Reserves R\n"
       + "  WHERE  S.sid = R.sid\n\n"
       + "(Tests FileScan, Projection, Sort-Merge Join and "
       + "Duplication elimination.)\n\n");
 
    CondExpr [] outFilter = new CondExpr[2];
    outFilter[0] = new CondExpr();
    outFilter[1] = new CondExpr();
 
    Query3_CondExpr(outFilter);
 
    Tuple t = new Tuple();
    t = null;
 
    AttrType Stypes[] = {
      new AttrType(AttrType.attrInteger),
      new AttrType(AttrType.attrString),
      new AttrType(AttrType.attrInteger),
      new AttrType(AttrType.attrReal)
    };
    short []   Ssizes = new short[1];
    Ssizes[0] = 30;

    AttrType [] Rtypes = {
      new AttrType(AttrType.attrInteger),
      new AttrType(AttrType.attrInteger),
      new AttrType(AttrType.attrString),
    };
    short  []  Rsizes = new short[1];
    Rsizes[0] =15;
 
    FldSpec [] Sprojection = {
       new FldSpec(new RelSpec(RelSpec.outer), 1),
       new FldSpec(new RelSpec(RelSpec.outer), 2),
       new FldSpec(new RelSpec(RelSpec.outer), 3),
       new FldSpec(new RelSpec(RelSpec.outer), 4)
    };

    CondExpr[] selects = new CondExpr [1];
    selects = null;
 
    iterator.Iterator am = null;
    try {
      am  = new FileScan("sailors.in", Stypes, Ssizes,
				  (short)4, (short) 4,
				  Sprojection, null);
    }
    catch (Exception e) {
      status = FAIL;
      System.err.println (""+e);
    }
 
    if (status != OK) {
      //bail out
      System.err.println ("*** Error setting up scan for sailors");
      Runtime.getRuntime().exit(1);
    }

    FldSpec [] Rprojection = {
       new FldSpec(new RelSpec(RelSpec.outer), 1),
       new FldSpec(new RelSpec(RelSpec.outer), 2),
       new FldSpec(new RelSpec(RelSpec.outer), 3)
    }; 
 
    iterator.Iterator am2 = null;
    try {
      am2 = new FileScan("reserves.in", Rtypes, Rsizes, 
				  (short)3, (short)3,
				  Rprojection, null);
    }
    catch (Exception e) {
      status = FAIL;
      System.err.println (""+e);
    }
    
    if (status != OK) {
      //bail out
      System.err.println ("*** Error setting up scan for reserves");
      Runtime.getRuntime().exit(1);
    }

    FldSpec [] proj_list = {
      new FldSpec(new RelSpec(RelSpec.outer), 2)
    };

    AttrType [] jtype     = { new AttrType(AttrType.attrString) };
 
    TupleOrder ascending = new TupleOrder(TupleOrder.Ascending);
    SortMerge sm = null;
    short  []  jsizes    = new short[1];
    jsizes[0] = 30;
    try {
      sm = new SortMerge(Stypes, 4, Ssizes,
			 Rtypes, 3, Rsizes,
			 1, 4,
			 1, 4,
			 10,
			 am, am2,
			 false, false, ascending,
			 outFilter, proj_list, 1);
    }
    catch (Exception e) {
      status = FAIL;
      System.err.println (""+e);
    }
 
    if (status != OK) {
      //bail out
      System.err.println ("*** Error constructing SortMerge");
      Runtime.getRuntime().exit(1);
    }
    
   

    DuplElim ed = null;
    try {
      ed = new DuplElim(jtype, (short)1, jsizes, sm, 10, false);
    }
    catch (Exception e) {
      System.err.println (""+e);
      Runtime.getRuntime().exit(1);
    }
 
    QueryCheck qcheck4 = new QueryCheck(4);

    
    t = null;
 
    try {
      while ((t = ed.get_next()) != null) {
        t.print(jtype);
        qcheck4.Check(t);
      }
    }
    catch (Exception e) {
      System.err.println (""+e);
      e.printStackTrace(); 
      Runtime.getRuntime().exit(1);
      }
    
    qcheck4.report(4);
    try {
      ed.close();
    }
    catch (Exception e) {
      status = FAIL;
      e.printStackTrace();
    }
   System.out.println ("\n");  
    if (status != OK) {
      //bail out
      System.err.println ("*** Error setting up scan for sailors");
      Runtime.getRuntime().exit(1);
    }
 }

   public void Query5() {
   System.out.print("**********************Query5 strating *********************\n");  
    boolean status = OK;
        // Sailors, Boats, Reserves Queries.
 
    System.out.print 
      ("Query: Find the names of old sailors or sailors with "
       + "a rating less\n       than 7, who have reserved a boat, "
       + "(perhaps to increase the\n       amount they have to "
       + "pay to make a reservation).\n\n"
       + "  SELECT S.sname, S.rating, S.age\n"
       + "  FROM   Sailors S, Reserves R\n"
       + "  WHERE  S.sid = R.sid and (S.age > 40 || S.rating < 7)\n\n"
       + "(Tests FileScan, Multiple Selection, Projection, "
       + "and Sort-Merge Join.)\n\n");

   
    CondExpr [] outFilter;
    outFilter = Query5_CondExpr();
 
    Tuple t = new Tuple();
    t = null;
 
    AttrType Stypes[] = {
      new AttrType(AttrType.attrInteger),
      new AttrType(AttrType.attrString),
      new AttrType(AttrType.attrInteger),
      new AttrType(AttrType.attrReal)
    };
    short []   Ssizes = new short[1];
    Ssizes[0] = 30;

    AttrType [] Rtypes = {
      new AttrType(AttrType.attrInteger),
      new AttrType(AttrType.attrInteger),
      new AttrType(AttrType.attrString),
    };
    short  []  Rsizes = new short[1];
    Rsizes[0] = 15;

    FldSpec [] Sprojection = {
      new FldSpec(new RelSpec(RelSpec.outer), 1),
      new FldSpec(new RelSpec(RelSpec.outer), 2),
      new FldSpec(new RelSpec(RelSpec.outer), 3),
      new FldSpec(new RelSpec(RelSpec.outer), 4)
    };
    
    CondExpr[] selects = new CondExpr [1];
    selects[0] = null;
 
    FldSpec [] proj_list = {
      new FldSpec(new RelSpec(RelSpec.outer), 2),
      new FldSpec(new RelSpec(RelSpec.outer), 3),
      new FldSpec(new RelSpec(RelSpec.outer), 4)
    };

    FldSpec [] Rprojection = {
      new FldSpec(new RelSpec(RelSpec.outer), 1),
      new FldSpec(new RelSpec(RelSpec.outer), 2),
      new FldSpec(new RelSpec(RelSpec.outer), 3)
    };
  
    AttrType [] jtype     = { 
      new AttrType(AttrType.attrString), 
      new AttrType(AttrType.attrInteger), 
      new AttrType(AttrType.attrReal)
    };


    iterator.Iterator am = null;
    try {
      am  = new FileScan("sailors.in", Stypes, Ssizes, 
				  (short)4, (short)4,
				  Sprojection, null);
    }
    catch (Exception e) {
      status = FAIL;
      System.err.println (""+e);
    }
    
    if (status != OK) {
      //bail out
      System.err.println ("*** Error setting up scan for sailors");
      Runtime.getRuntime().exit(1);
    }

    iterator.Iterator am2 = null;
    try {
      am2 = new FileScan("reserves.in", Rtypes, Rsizes, 
			 (short)3, (short)3,
			 Rprojection, null);
    }
    catch (Exception e) {
      status = FAIL;
      System.err.println (""+e);
    }
 
    if (status != OK) {
      //bail out
      System.err.println ("*** Error setting up scan for reserves");
      Runtime.getRuntime().exit(1);
    }
 
    TupleOrder ascending = new TupleOrder(TupleOrder.Ascending);
    SortMerge sm = null;
    try {
      sm = new SortMerge(Stypes, 4, Ssizes,
			 Rtypes, 3, Rsizes,
			 1, 4,
			 1, 4,
			 10,
			 am, am2,
			 false, false, ascending,
			 outFilter, proj_list, 3);
    }
    catch (Exception e) {
      status = FAIL;
      System.err.println (""+e);
    }
 
    if (status != OK) {
      //bail out
      System.err.println ("*** Error constructing SortMerge");
      Runtime.getRuntime().exit(1);
    }

    QueryCheck qcheck5 = new QueryCheck(5);
    //Tuple t = new Tuple();
    t = null;
 
    try {
      while ((t = sm.get_next()) != null) {
        t.print(jtype);
        qcheck5.Check(t);
      }
    }
    catch (Exception e) {
      System.err.println (""+e);
      Runtime.getRuntime().exit(1);
    }
    
    qcheck5.report(5);
    try {
      sm.close();
    }
    catch (Exception e) {
      status = FAIL;
      e.printStackTrace();
    }
    System.out.println ("\n"); 
    if (status != OK) {
      //bail out
      System.err.println ("*** Error close for sortmerge");
      Runtime.getRuntime().exit(1);
    }
 }

   public void Query6()
   {
     System.out.print("**********************Query6 strating *********************\n");
     boolean status = OK;
     // Sailors, Boats, Reserves Queries.
     System.out.print( "Query: Find the names of sailors with a rating greater than 7\n"
			+ "  who have reserved a red boat, and print them out in sorted order.\n\n"
			+ "  SELECT   S.sname\n"
			+ "  FROM     Sailors S, Boats B, Reserves R\n"
			+ "  WHERE    S.sid = R.sid AND S.rating > 7 AND R.bid = B.bid \n"
			+ "           AND B.color = 'red'\n"
			+ "  ORDER BY S.name\n\n"
			
			+ "Plan used:\n"
			+" Sort(Pi(sname) (Sigma(B.color='red')  |><|  Pi(sname, bid) (Sigma(S.rating > 7)  |><|  R)))\n\n"
			
			+ "(Tests FileScan, Multiple Selection, Projection,sort and nested-loop join.)\n\n");
     
     CondExpr [] outFilter  = new CondExpr[3];
     outFilter[0] = new CondExpr();
     outFilter[1] = new CondExpr();
     outFilter[2] = new CondExpr();
     CondExpr [] outFilter2 = new CondExpr[3];
     outFilter2[0] = new CondExpr();
     outFilter2[1] = new CondExpr();
     outFilter2[2] = new CondExpr();
     
     Query6_CondExpr(outFilter, outFilter2);
     Tuple t = new Tuple();
     t = null;
     
     AttrType [] Stypes = {
	new AttrType(AttrType.attrInteger), 
	new AttrType(AttrType.attrString), 
	new AttrType(AttrType.attrInteger), 
	new AttrType(AttrType.attrReal)
     };
     
     
     
     short []   Ssizes = new short[1];
     Ssizes[0] = 30;
     AttrType [] Rtypes = {
	new AttrType(AttrType.attrInteger), 
	new AttrType(AttrType.attrInteger), 
	new AttrType(AttrType.attrString), 
     };
     
     short  []  Rsizes = new short[1] ;
     Rsizes[0] = 15;
     AttrType [] Btypes = {
	new AttrType(AttrType.attrInteger), 
	new AttrType(AttrType.attrString), 
	new AttrType(AttrType.attrString), 
     };
     
     short  []  Bsizes = new short[2];
     Bsizes[0] =30;
     Bsizes[1] =20;
     
     
     AttrType [] Jtypes = {
	new AttrType(AttrType.attrString), 
	new AttrType(AttrType.attrInteger), 
     };
     
     short  []  Jsizes = new short[1];
     Jsizes[0] = 30;
     AttrType [] JJtype = {
	new AttrType(AttrType.attrString), 
     };
     
     short [] JJsize = new short[1];
     JJsize[0] = 30; 
     
     
     
     FldSpec []  proj1 = {
	new FldSpec(new RelSpec(RelSpec.outer), 2),
	new FldSpec(new RelSpec(RelSpec.innerRel), 2)
     }; // S.sname, R.bid
     
     FldSpec [] proj2  = {
	new FldSpec(new RelSpec(RelSpec.outer), 1)
     };
     
     FldSpec [] Sprojection = {
	new FldSpec(new RelSpec(RelSpec.outer), 1),
	new FldSpec(new RelSpec(RelSpec.outer), 2),
       new FldSpec(new RelSpec(RelSpec.outer), 3),
       new FldSpec(new RelSpec(RelSpec.outer), 4)
     };
     
     FileScan am = null;
     try {
	am  = new FileScan("sailors.in", Stypes, Ssizes, 
			   (short)4, (short)4,
			   Sprojection, null);
     }
     catch (Exception e) {
	status = FAIL;
	System.err.println (""+e);
	e.printStackTrace();
     }
     
     if (status != OK) {
	//bail out
	
	System.err.println ("*** Error setting up scan for sailors");
	Runtime.getRuntime().exit(1);
     }
     
 
     
     NestedLoopsJoins inl = null;
     try {
	inl = new NestedLoopsJoins (Stypes, 4, Ssizes,
				    Rtypes, 3, Rsizes,
				    10,
				  am, "reserves.in",
				    outFilter, null, proj1, 2);
     }
     catch (Exception e) {
	System.err.println ("*** Error preparing for nested_loop_join");
	System.err.println (""+e);
	e.printStackTrace();
	Runtime.getRuntime().exit(1);
     }
    
     System.out.print( "After nested loop join S.sid|><|R.sid.\n");
	
     NestedLoopsJoins nlj = null;
     try {
	nlj = new NestedLoopsJoins (Jtypes, 2, Jsizes,
				    Btypes, 3, Bsizes,
				    10,
				    inl, "boats.in",
				    outFilter2, null, proj2, 1);
     }
     catch (Exception e) {
	System.err.println ("*** Error preparing for nested_loop_join");
	System.err.println (""+e);
	e.printStackTrace();
	Runtime.getRuntime().exit(1);
     }
     
     System.out.print( "After nested loop join R.bid|><|B.bid AND B.color=red.\n");
     
     TupleOrder ascending = new TupleOrder(TupleOrder.Ascending);
     Sort sort_names = null;
     try {
	sort_names = new Sort (JJtype,(short)1, JJsize,
			       (iterator.Iterator) nlj, 1, ascending, JJsize[0], 10);
     }
     catch (Exception e) {
	System.err.println ("*** Error preparing for sorting");
	System.err.println (""+e);
	Runtime.getRuntime().exit(1);
     }
     
     
     System.out.print( "After sorting the output tuples.\n");
  
     
     QueryCheck qcheck6 = new QueryCheck(6);
     
     try {
	while ((t =sort_names.get_next()) !=null) {
	  t.print(JJtype);
	  qcheck6.Check(t);
	}
     }catch (Exception e) {
	System.err.println ("*** Error preparing for get_next tuple");
	System.err.println (""+e);
	Runtime.getRuntime().exit(1);
     }
     
     qcheck6.report(6);
     
     System.out.println ("\n"); 
     try {
	sort_names.close();
     }
     catch (Exception e) {
	status = FAIL;
	e.printStackTrace();
     }
     
     if (status != OK) {
	//bail out
	
	Runtime.getRuntime().exit(1);
     }
     
   }
        
  private void Disclaimer() {
    System.out.print ("\n\nAny resemblance of persons in this database to"
         + " people living or dead\nis purely coincidental. The contents of "
         + "this database do not reflect\nthe views of the University,"
         + " the Computer  Sciences Department or the\n"
         + "developers...\n\n");
  }
}

public class JoinTest
{
  public static void main(String argv[]) throws JoinsException, IndexException, InvalidTupleSizeException, InvalidTypeException, PageNotReadException, TupleUtilsException, PredEvalException, SortException, LowMemException, UnknowAttrType, UnknownKeyTypeException, IOException, Exception
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

