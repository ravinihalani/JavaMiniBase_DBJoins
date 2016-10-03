package tests;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import global.AttrOperator;
import global.AttrType;
import global.GlobalConst;
import global.RID;
import global.SystemDefs;
import global.TupleOrder;
import heap.Heapfile;
import heap.Scan;
import heap.Tuple;
import iterator.CondExpr;
import iterator.FileScan;
import iterator.FldSpec;
import iterator.IESelfJoin;
import iterator.PredEval;
import iterator.RelSpec;
import iterator.Sort;

//Define the Q schema
class Q {
	public int q_1;
	public int q_2;
	public int q_3;
	public int q_4;

	public Q(int col1, int col2, int col3, int col4) {
		q_1 = col1;
		q_2 = col2;
		q_3 = col3;
		q_4 = col4;
	}
}

// Define the R schema
class R {
	public int r_1;
	public int r_2;
	public int r_3;
	public int r_4;

	public R(int col1, int col2, int col3, int col4) {
		r_1 = col1;
		r_2 = col2;
		r_3 = col3;
		r_4 = col4;
	}
}

// Define the S schema
class S {
	public int s_1;
	public int s_2;
	public int s_3;
	public int s_4;
	private byte[] data;

	public S(int col1, int col2, int col3, int col4) {
		s_1 = col1;
		s_2 = col2;
		s_3 = col3;
		s_4 = col4;
	}

	public S(Tuple _atuple) {
		data = new byte[_atuple.getLength()];
		data = _atuple.getTupleByteArray();
		System.out.println(data.toString());
	}
}

class P3JoinDriver implements GlobalConst {

	private boolean OK = true;
	private boolean FAIL = false;
	private Vector q;
	private Vector r;
	private Vector s;

	/**
	 * Constructor
	 */
	P3JoinDriver() {

		for(int i=0;i<10;i++){
			visited[i]=true;
		}
		
		String dbpath = "/tmp/" + System.getProperty("user.name") + ".minibase.jointestdb";
		String logpath = "/tmp/" + System.getProperty("user.name") + ".joinlog";

		String remove_cmd = "/bin/rm -rf ";
		String remove_logcmd = remove_cmd + logpath;
		String remove_dbcmd = remove_cmd + dbpath;
		String remove_joincmd = remove_cmd + dbpath;

		try {
			Runtime.getRuntime().exec(remove_logcmd);
			Runtime.getRuntime().exec(remove_dbcmd);
			Runtime.getRuntime().exec(remove_joincmd);
		} catch (IOException e) {
			System.err.println("" + e);
		}

		SystemDefs sysdef = new SystemDefs(dbpath, 1000000, NUMBUF, "Clock");
		
		
		
		
		q = new Vector();
		r = new Vector();
		s = new Vector();

		boolean status = OK;
		int qcount = 0;
		int rcount = 0;
		int scount = 0;
		
		boolean loadQ = false,loadR=true,loadS=true;

		try {
			FileReader input = new FileReader("Q.txt");
			BufferedReader br = new BufferedReader(input);
			String line;
			System.out.println("Start");
			if(loadQ){
				
				
				
	
				line = br.readLine();
				line = br.readLine();
				while (line != null) {
					// System.out.println(line);
					qcount++;
					List<String> colData = Arrays.asList(line.split("\\s*,\\s*"));
					int count = colData.size();
					if (count != 4) {
						System.out.println("Error in file ");
					} else
						q.addElement(new Q(Integer.parseInt(colData.get(0)), Integer.parseInt(colData.get(1)),
								Integer.parseInt(colData.get(2)), Integer.parseInt(colData.get(3))));
					line = br.readLine();
				}
				br.close();
				System.out.println("End Q input");
			}
			input = new FileReader("R.txt");
			br = new BufferedReader(input);
			line = "";

			line = br.readLine();
			line = br.readLine();
			while (line != null) {
				// System.out.println(line);
				rcount++;
				List<String> colData = Arrays.asList(line.split("\\s*,\\s*"));
				int count = colData.size();
				if (count != 4) {
					System.out.println("Error in file ");
				} else
					r.addElement(new R(Integer.parseInt(colData.get(0)), Integer.parseInt(colData.get(1)),
							Integer.parseInt(colData.get(2)), Integer.parseInt(colData.get(3))));
				line = br.readLine();
			}
			br.close();
			System.out.println("End R input");

			input = new FileReader("S.txt");
			br = new BufferedReader(input);
			line = "";

			line = br.readLine();
			line = br.readLine();
			while (line != null) {
				// System.out.println(line);
				scount++;
				List<String> colData = Arrays.asList(line.split("\\s*,\\s*"));
				int count = colData.size();
				if (count != 4) {
					System.out.println("Error in file ");
				} else
					s.addElement(new S(Integer.parseInt(colData.get(0)), Integer.parseInt(colData.get(1)),
							Integer.parseInt(colData.get(2)), Integer.parseInt(colData.get(3))));
				line = br.readLine();
			}
			br.close();
			System.out.println("End S input");

			System.out.println("Counts " + qcount + " " + rcount + " " + scount);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Tuple t;
		int size;
		 RID rid;
		 Heapfile f = null;
		if(loadQ){
		 System.out.println("creating the q relation start");
		 // creating the q relation
		 AttrType[] Qtypes = new AttrType[4];
		 Qtypes[0] = new AttrType(AttrType.attrInteger);
		 Qtypes[1] = new AttrType(AttrType.attrInteger);
		 Qtypes[2] = new AttrType(AttrType.attrInteger);
		 Qtypes[3] = new AttrType(AttrType.attrInteger);
		
		 // String size will be 0 as we don't use any string
		 short[] Qsizes = new short[1];
		 Qsizes[0] = 0;
		
		 t = new Tuple();
		 try {
		 t.setHdr((short) 4, Qtypes, Qsizes);
		 } catch (Exception e) {
		 System.err.println("*** error in Tuple.setHdr() ***");
		 status = FAIL;
		 e.printStackTrace();
		 }
		
		 size = t.size();
		
		 // inserting the tuple into file "sailors"
		
		 try {
		 f = new Heapfile("Q.in");
		 } catch (Exception e) {
		 System.err.println("*** error in Heapfile constructor ***");
		 status = FAIL;
		 e.printStackTrace();
		 }
		
		 t = new Tuple(size);
		 try {
		 t.setHdr((short) 4, Qtypes, Qsizes);
		 } catch (Exception e) {
		 System.err.println("*** error in Tuple.setHdr() ***");
		 status = FAIL;
		 e.printStackTrace();
		 }
		
		 qcount=1000;
		 for (int i = 0; i < qcount; i++) {
			 bar(i,qcount);			 
			 try {
				 t.setIntFld(1, ((Q) q.elementAt(i)).q_1);
				 t.setIntFld(2, ((Q) q.elementAt(i)).q_2);
				 t.setIntFld(3, ((Q) q.elementAt(i)).q_3);
				 t.setIntFld(4, ((Q) q.elementAt(i)).q_4);
			 } catch (Exception e) {
				 System.err.println("*** Heapfile error in Tuple.setStrFld() ***");
				 status = FAIL;
				 e.printStackTrace();
			 }
		
			 try {
				 rid = f.insertRecord(t.returnTupleByteArray());
			 } catch (Exception e) {
				 System.err.println("*** error in Heapfile.insertRecord() ***");
				 status = FAIL;
				 e.printStackTrace();
			 }
		}
		 if (status != OK) {
			 // bail out
			 System.err.println("*** Error creating relation for sailors");
			 Runtime.getRuntime().exit(1);
		 }
		 System.out.println("creating the q relation complete");
		}
		

		System.out.println("creating the r relation start");
		// creating the r relation
		AttrType[] Rtypes = new AttrType[4];
		Rtypes[0] = new AttrType(AttrType.attrInteger);
		Rtypes[1] = new AttrType(AttrType.attrInteger);
		Rtypes[2] = new AttrType(AttrType.attrInteger);
		Rtypes[3] = new AttrType(AttrType.attrInteger);

		short[] Rsizes = new short[1];
		Rsizes[0] = 0;
		t = new Tuple();
		try {
			t.setHdr((short) 4, Rtypes, Rsizes);
		} catch (Exception e) {
			System.err.println("*** error in Tuple.setHdr() ***");
			status = FAIL;
			e.printStackTrace();
		}

		size = t.size();

		// inserting the tuple into file "boats"
		// RID rid;
		f = null;
		try {
			f = new Heapfile("R.in");
		} catch (Exception e) {
			System.err.println("*** error in Heapfile constructor ***");
			status = FAIL;
			e.printStackTrace();
		}

		t = new Tuple(size);
		try {
			t.setHdr((short) 4, Rtypes, Rsizes);
		} catch (Exception e) {
			System.err.println("*** error in Tuple.setHdr() ***");
			status = FAIL;
			e.printStackTrace();
		}

		for (int i = 0; i < rcount; i++) {
			try {
				t.setIntFld(1, ((R) r.elementAt(i)).r_1);
				t.setIntFld(2, ((R) r.elementAt(i)).r_2);
				t.setIntFld(3, ((R) r.elementAt(i)).r_3);
				t.setIntFld(4, ((R) r.elementAt(i)).r_4);

			} catch (Exception e) {
				System.err.println("*** error in Tuple.setStrFld() ***");
				status = FAIL;
				e.printStackTrace();
			}

			try {
				rid = f.insertRecord(t.returnTupleByteArray());
			} catch (Exception e) {
				System.err.println("*** error in Heapfile.insertRecord() ***");
				status = FAIL;
				e.printStackTrace();
			}
		}
		if (status != OK) {
			// bail out
			System.err.println("*** Error creating relation for reserves");
			Runtime.getRuntime().exit(1);
		}
		System.out.println("creating the r relation complete");

		System.out.println("creating the s relation start");
		// creating the s relation
		AttrType[] Stypes = new AttrType[4];
		Stypes[0] = new AttrType(AttrType.attrInteger);
		Stypes[1] = new AttrType(AttrType.attrInteger);
		Stypes[2] = new AttrType(AttrType.attrInteger);
		Stypes[3] = new AttrType(AttrType.attrInteger);

		short[] Ssizes = new short[1];
		Ssizes[0] = 0;
		t = new Tuple();
		try {
			t.setHdr((short) 4, Stypes, Ssizes);
		} catch (Exception e) {
			System.err.println("*** error in Tuple.setHdr() ***");
			status = FAIL;
			e.printStackTrace();
		}

		size = t.size();

		// inserting the tuple into file "s"
		// RID rid;
		f = null;
		try {
			f = new Heapfile("S.in");
		} catch (Exception e) {
			System.err.println("*** error in Heapfile constructor ***");
			status = FAIL;
			e.printStackTrace();
		}

		t = new Tuple(size);
		try {
			t.setHdr((short) 4, Stypes, Ssizes);
		} catch (Exception e) {
			System.err.println("*** error in Tuple.setHdr() ***");
			status = FAIL;
			e.printStackTrace();
		}

		for (int i = 0; i < scount; i++) {
			try {
				t.setIntFld(1, ((S) s.elementAt(i)).s_1);
				t.setIntFld(2, ((S) s.elementAt(i)).s_2);
				t.setIntFld(3, ((S) s.elementAt(i)).s_3);
				t.setIntFld(4, ((S) s.elementAt(i)).s_4);
			} catch (Exception e) {
				System.err.println("*** error in Tuple.setStrFld() ***");
				status = FAIL;
				e.printStackTrace();
			}

			try {
				rid = f.insertRecord(t.returnTupleByteArray());
			} catch (Exception e) {
				System.err.println("*** error in Heapfile.insertRecord() ***");
				status = FAIL;
				e.printStackTrace();
			}
		}

		if (status != OK) {
			// bail out
			System.err.println("*** Error creating relation for boats");
			Runtime.getRuntime().exit(1);
		}
		System.out.println("creating the s relation complete");
		 
	}

	boolean[] visited = new boolean[10];
		
	public void bar(int pc, int total){
		if(pc>(total/90) && visited[9]){
			visited[9]=false;
			System.out.print("90 - ");
			System.out.print("==");
		}
		else if(pc>(total/80) && visited[8]){
			visited[8]=false;
			System.out.print("80 - ");
			//System.out.print("==");
		}
		else if(pc>(total/70) && visited[7]){
			visited[7]=false;
			System.out.print("70 - ");
			//System.out.print("==");
		}
		else if(pc>(total/60) && visited[6]){
			visited[6]=false;
			System.out.print("60 - ");
			//System.out.print("==");
		}
		else if(pc>(total/50) && visited[5]){
			visited[5]=false;
			System.out.print("50 - ");
			//System.out.print("==");
		}
		else if(pc>(total/40) && visited[4]){
			visited[4]=false;
			//System.out.print("==");
			System.out.print("40 - ");
		}
		else if(pc>(total/30) && visited[3]){
			visited[3]=false;
			//System.out.print("==");
			System.out.print("30 - ");
		}
		else if(pc>(total/20) && visited[2]){
			visited[2]=false;
			System.out.print("20 - ");
			//System.out.print("==");
		}
		else if(pc>(total/10) && visited[1]){
			visited[1]=false;
			System.out.print("10 - ");
			//System.out.print("==");
		}
		else if(pc>(1) && visited[0]){
			visited[0]=false;
			
			System.out.print("==");
		}
		
		
	}
	public void load_Datasets(ArrayList<String> dblines){
		
		ArrayList<String> dbFile = new ArrayList<>();
		for(String line : dblines){
			String[] str = line.split("\\s* \\s*");
			for(String s : str){
				if(s.equals("AND") || s.equals("OR"))
					continue;
				String t = s.substring(0,1);
				try{
					Integer.parseInt(t);
				}
				catch(Exception e){
					if(!dbFile.contains(t))
					{
						dbFile.add(t);
						System.out.print( t + " ");
					}
						
				}
			}
		}
		
		System.out.println();
		
		if(dbFile.size() > 2){
			System.err.println("Something is fishy !!! We don't need more than 2 tables");
		}
		
		for(String str : dbFile){
			int rCount = 0;			
			try{
				System.out.println("Loading File "+str);
				FileReader input = new FileReader(str+".txt");
				BufferedReader br = new BufferedReader(input);
				String line;

				line = br.readLine();
				//line = br.readLine();
				while ((line = br.readLine()) != null) {
					// System.out.println(line);
					rCount++;
					List<String> colData = Arrays.asList(line.split("\\s*,\\s*"));
					int count = colData.size();
					if (count != 4) {
						System.err.println("Error in file ");
					} else
						q.addElement(new Q(Integer.parseInt(colData.get(0)), Integer.parseInt(colData.get(1)),
								Integer.parseInt(colData.get(2)), Integer.parseInt(colData.get(3))));
				}
				br.close();
				System.out.println("End "+str+" input\n with "+rCount+" rows");
				
				AttrType[] Stypes = new AttrType[4];
				Stypes[0] = new AttrType(AttrType.attrInteger);
				Stypes[1] = new AttrType(AttrType.attrInteger);
				Stypes[2] = new AttrType(AttrType.attrInteger);
				Stypes[3] = new AttrType(AttrType.attrInteger);

				short[] Ssizes = new short[1];
				Ssizes[0] = 0;
				Tuple t = new Tuple();
				try {
					t.setHdr((short) 4, Stypes, Ssizes);
				} catch (Exception e) {
					System.err.println("*** error in Tuple.setHdr() ***");
					e.printStackTrace();
				}

				int size = t.size();

				// inserting the tuple into file "s"
				// RID rid;
				Heapfile f = null;
				try {
					f = new Heapfile(str+".in");
				} catch (Exception e) {
					System.err.println("*** error in Heapfile constructor ***");
					e.printStackTrace();
				}

				t = new Tuple(size);
				try {
					t.setHdr((short) 4, Stypes, Ssizes);
				} catch (Exception e) {
					System.err.println("*** error in Tuple.setHdr() ***");
					e.printStackTrace();
				}

				for (int i = 0; i < rCount; i++) {
					try {
						t.setIntFld(1, ((S) s.elementAt(i)).s_1);
						t.setIntFld(2, ((S) s.elementAt(i)).s_2);
						t.setIntFld(3, ((S) s.elementAt(i)).s_3);
						t.setIntFld(4, ((S) s.elementAt(i)).s_4);
					} catch (Exception e) {
						System.err.println("*** error in Tuple.setStrFld() ***");
						e.printStackTrace();
					}

					try {
						f.insertRecord(t.returnTupleByteArray());
					} catch (Exception e) {
						System.err.println("*** error in Heapfile.insertRecord() ***");
						e.printStackTrace();
					}
				}
				
				
			}
			catch(Exception e){
				System.err.println("Error while making .in file; Error at "+s); 
			}
				
		}
	}
		
	
	
	
	
	public void Query_1a(){
		//This method is strictly for Single Predicate only
		System.out.println("Executing Query 1a - Extending present JOINS to implement single predicate inequality join");
		FileReader fileReader = null;
		BufferedReader bReader = null;
		FileScan fileScan = null;
		ArrayList<String> lines =  new ArrayList<String>();
		try {
			fileReader= new FileReader("query_1a.txt");
			bReader = new BufferedReader(fileReader);
			String line = null;
			while((line=bReader.readLine())!=null){
				lines.add(line);
				//System.out.println("1 "+line);
			}
			// SELECT FIELDS
			String[] t_str = lines.get(0).split(" ");
			int select_Col1,select_Col2;
			select_Col1 = Integer.parseInt(t_str[0].substring(2));
			select_Col2 = Integer.parseInt(t_str[1].substring(2));
			FldSpec [] proj_list = new FldSpec[2];
		    proj_list[0] = new FldSpec(new RelSpec(RelSpec.outer),select_Col1 );
		    proj_list[1] = new FldSpec(new RelSpec(RelSpec.innerRel), select_Col2);

		    //Select File names
		    t_str = lines.get(1).split(" ");
		    String file1 = t_str[0].trim()+".in";
		    String file2 = t_str[1].trim()+".in";
		    
		    
		    //Setting outFilter  -- Strictly for Single Predicate
		    t_str = lines.get(2).split(" ");		    
			CondExpr[] outFilter = new CondExpr[2];
			
			outFilter[1] = null;
		    outFilter[0] = new CondExpr();
		    
		    outFilter[0].next  = null;
		    outFilter[0].type1 = new AttrType(AttrType.attrSymbol);
		    outFilter[0].type2 = new AttrType(AttrType.attrSymbol);
		    		    
		    //Setting the operator
		    int opr = Integer.parseInt(t_str[1]);
		    switch (opr) {
			case 1: outFilter[0].op    = new AttrOperator(AttrOperator.aopLT);	break;
			case 2: outFilter[0].op    = new AttrOperator(AttrOperator.aopLE);	break;
			case 3: outFilter[0].op    = new AttrOperator(AttrOperator.aopGE);	break;
			case 4: outFilter[0].op    = new AttrOperator(AttrOperator.aopGT);	break;
			default:
				System.out.println("The Operator is out of range (1-4)");
				break;
			}
		    
		    //Setting the columns numbers using in Join Condition -- Line3 of input file
		    int outerTab_col,innerTab_col;
		    outerTab_col = Integer.parseInt(t_str[0].substring(2));
		    innerTab_col = Integer.parseInt(t_str[2].substring(2));
		    outFilter[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),outerTab_col);
		    outFilter[0].operand2.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),innerTab_col);
			
			AttrType[] Stypes = new AttrType[4];
			Stypes[0] = new AttrType(AttrType.attrInteger);
			Stypes[1] = new AttrType(AttrType.attrInteger);
			Stypes[2] = new AttrType(AttrType.attrInteger);
			Stypes[3] = new AttrType(AttrType.attrInteger);
		
			short [] Ssizes = new short[1];
			Ssizes[0]=0;
					

			FldSpec [] Sprojection = new FldSpec[4];
		    Sprojection[0] = new FldSpec(new RelSpec(RelSpec.outer), 1);
		    Sprojection[1] = new FldSpec(new RelSpec(RelSpec.outer), 2);
		    Sprojection[2] = new FldSpec(new RelSpec(RelSpec.outer), 3);
		    Sprojection[3] = new FldSpec(new RelSpec(RelSpec.outer), 4);
			fileScan = new FileScan(file1, Stypes, Ssizes, (short)4, (short)4, Sprojection, null);
			
				
			
			Heapfile f = null;			
			AttrType[] Rtypes = new AttrType[2];
			short[] Rsizes;
			Rtypes[0] = new AttrType(AttrType.attrInteger);
			Rtypes[1] = new AttrType(AttrType.attrInteger);
			Rsizes = new short[1];
			Rsizes[0] = 0;
			Tuple t_res = new Tuple();
			try {
				t_res.setHdr((short) 2, Rtypes, Rsizes);
			} catch (Exception e) {
				System.err.println("*** error in Tuple.setHdr() ***");
				e.printStackTrace();
			}
			int size = t_res.size();
			f = null;
			try {
				f = new Heapfile("result.in");
			} catch (Exception e) {
				System.err.println("*** error in Heapfile constructor ***");
				e.printStackTrace();
			}
			
			t_res = new Tuple(size);
			try {
				t_res.setHdr((short) 2, Rtypes, Rsizes);
			} catch (Exception e) {
				System.err.println("*** error in Tuple.setHdr() ***");
				e.printStackTrace();
			}
			
			long start = System.currentTimeMillis();
			for(int i=0;i<fileScan.getNumRecords();i++){
				Tuple t1 = fileScan.get_next();
				FileScan toRepeat = new FileScan(file2, Stypes, Ssizes, (short)4, (short)4, Sprojection, null);
				for(int j=0;j<toRepeat.getNumRecords();j++){
					Tuple t2 = toRepeat.get_next();
					if(PredEval.Eval(outFilter, t1, t2, Stypes , Stypes ) == true){
						t_res.setIntFld(1, t1.getIntFld(select_Col1));
		    			t_res.setIntFld(2, t2.getIntFld(select_Col2));
						f.insertRecord(t_res.returnTupleByteArray());
						//System.out.println("Record Num "+ ++rc +" : "+val1+","+val2);						
					}
				}
			}
			
			
			//To display results
			FldSpec [] sProjection1 = new FldSpec[2];
		    sProjection1[0] = new FldSpec(new RelSpec(RelSpec.outer), 1);
		    sProjection1[1] = new FldSpec(new RelSpec(RelSpec.outer), 2);
		    int i=0;
		    FileScan result = null;
		    try {
				result = new FileScan("result.in", Rtypes, Rsizes, (short)2, (short)2, sProjection1, null);
				System.out.println("Num of records : "+result.getNumRecords());		
				System.out.println("Exit*********");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
			Tuple t1 = new Tuple();			
			
			System.out.println("*********Entering");
			System.out.println("Num of records main : "+result.getNumRecords());	
			while(t1 != null){
				t1 = result.get_next();
				if(t1!=null){					
					if(t1!=null){				
						System.out.println("Record Num "+ ++i +" : " + t1.getIntFld(1) + " , "+t1.getIntFld(2));
					}
				}
			}
			System.out.println("*********EXIT");
			long end = System.currentTimeMillis();
			double time = (end - start) / 1000.00;
			System.out.println("Time taken --> " + time);

			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			System.out.println("************************************");
			e1.printStackTrace();
			System.out.println(e1.getMessage());
		}
	}

	public void Query_1b(){
		
		System.out.println("Executing Query 1b - ");
		FileReader fileReader = null;
		BufferedReader bReader = null;
		FileScan fileScan = null;
		ArrayList<String> lines =  new ArrayList<String>();
		try {
			fileReader= new FileReader("query_1b.txt");
			bReader = new BufferedReader(fileReader);
			String line = null;
			while((line=bReader.readLine())!=null){
				lines.add(line);
				//System.out.println("1 "+line);
			}
			// SELECT FIELDS
			String[] t_str = lines.get(0).split(" ");
			int select_Col1,select_Col2;
			select_Col1 = Integer.parseInt(t_str[0].substring(2));
			select_Col2 = Integer.parseInt(t_str[1].substring(2));
			FldSpec [] proj_list = new FldSpec[2];
		    proj_list[0] = new FldSpec(new RelSpec(RelSpec.outer),select_Col1 );
		    proj_list[1] = new FldSpec(new RelSpec(RelSpec.innerRel), select_Col2);

		    //Select File names
		    t_str = lines.get(1).split(" ");
		    String file1 = t_str[0].trim()+".in";
		    String file2 = t_str[1].trim()+".in";
		    
		    
		    //Setting outFilter  -- Strictly for Single Predicate
		    t_str = lines.get(2).split(" ");		    
			CondExpr[] outFilter1 = new CondExpr[2];
			CondExpr[] outFilter2 = new CondExpr[2];
			
			outFilter1[1] = null;
		    outFilter1[0] = new CondExpr();
		    
		    outFilter2[1] = null;
		    outFilter2[0] = new CondExpr();
		    
		    outFilter1[0].next  = null;
		    outFilter1[0].type1 = new AttrType(AttrType.attrSymbol);
		    outFilter1[0].type2 = new AttrType(AttrType.attrSymbol);
		    		    
		    //Setting the operator
		    int opr = Integer.parseInt(t_str[1]);
		    switch (opr) {
			case 1: outFilter1[0].op    = new AttrOperator(AttrOperator.aopLT);	break;
			case 2: outFilter1[0].op    = new AttrOperator(AttrOperator.aopLE);	break;
			case 3: outFilter1[0].op    = new AttrOperator(AttrOperator.aopGE);	break;
			case 4: outFilter1[0].op    = new AttrOperator(AttrOperator.aopGT);	break;
			default:
				System.out.println("The Operator is out of range (1-4)");
				break;
			}
		    
		    //Setting the columns numbers using in Join Condition -- Line3 of input file
		    int outerTab_col,innerTab_col;
		    outerTab_col = Integer.parseInt(t_str[0].substring(2));
		    innerTab_col = Integer.parseInt(t_str[2].substring(2));
		    outFilter1[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),outerTab_col);
		    outFilter1[0].operand2.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),innerTab_col);
			
		    t_str = lines.get(4).split(" ");		    
			outFilter2[0] = new CondExpr();		    
		    outFilter2[0].next  = null;
		    outFilter2[0].type1 = new AttrType(AttrType.attrSymbol);
		    outFilter2[0].type2 = new AttrType(AttrType.attrSymbol);		    		    
		    //Setting the operator (<,>,<eq ,>eq)
		    opr = Integer.parseInt(t_str[1]);
		    switch (opr) {
			case 1: outFilter2[0].op    = new AttrOperator(AttrOperator.aopLT);	break;
			case 2: outFilter2[0].op    = new AttrOperator(AttrOperator.aopLE);	break;
			case 3: outFilter2[0].op    = new AttrOperator(AttrOperator.aopGE);	break;
			case 4: outFilter2[0].op    = new AttrOperator(AttrOperator.aopGT);	break;
			default:
				System.err.println("The Operator is out of range (1-4)");
				break;
			}
		    
		    int outerTab_col2,innerTab_col2;
		    outerTab_col2 = Integer.parseInt(t_str[0].substring(2));
		    innerTab_col2 = Integer.parseInt(t_str[2].substring(2));
		    outFilter2[0].operand1.symbol = new FldSpec (new RelSpec(RelSpec.outer),outerTab_col2);
		    outFilter2[0].operand2.symbol = new FldSpec (new RelSpec(RelSpec.innerRel),innerTab_col2);
		    
			AttrType[] Stypes = new AttrType[4];
			Stypes[0] = new AttrType(AttrType.attrInteger);
			Stypes[1] = new AttrType(AttrType.attrInteger);
			Stypes[2] = new AttrType(AttrType.attrInteger);
			Stypes[3] = new AttrType(AttrType.attrInteger);
		
			short [] Ssizes = new short[1];
			Ssizes[0]=0;
			
			FldSpec [] Sprojection = new FldSpec[4];
		    Sprojection[0] = new FldSpec(new RelSpec(RelSpec.outer), 1);
		    Sprojection[1] = new FldSpec(new RelSpec(RelSpec.outer), 2);
		    Sprojection[2] = new FldSpec(new RelSpec(RelSpec.outer), 3);
		    Sprojection[3] = new FldSpec(new RelSpec(RelSpec.outer), 4);
			fileScan = new FileScan(file1, Stypes, Ssizes, (short)4, (short)4, Sprojection, null);
			
			
			Heapfile f = null;			
			AttrType[] Rtypes = new AttrType[2];
			short[] Rsizes;
			Rtypes[0] = new AttrType(AttrType.attrInteger);
			Rtypes[1] = new AttrType(AttrType.attrInteger);
			Rsizes = new short[1];
			Rsizes[0] = 0;
			Tuple t_res = new Tuple();
			try {
				t_res.setHdr((short) 2, Rtypes, Rsizes);
			} catch (Exception e) {
				System.err.println("*** error in Tuple.setHdr() ***");
				e.printStackTrace();
			}
			int size = t_res.size();
			f = null;
			try {
				f = new Heapfile("result.in");
			} catch (Exception e) {
				System.err.println("*** error in Heapfile constructor ***");
				e.printStackTrace();
			}
			
			t_res = new Tuple(size);
			try {
				t_res.setHdr((short) 2, Rtypes, Rsizes);
			} catch (Exception e) {
				System.err.println("*** error in Tuple.setHdr() ***");
				e.printStackTrace();
			}
			long start = System.currentTimeMillis();
			int rc=0;
			for(int i=0;i<fileScan.getNumRecords();i++){
				Tuple t1 = fileScan.get_next();
				FileScan toRepeat = new FileScan(file2, Stypes, Ssizes, (short)4, (short)4, Sprojection, null);
				for(int j=0;j<toRepeat.getNumRecords();j++){
					Tuple t2 = toRepeat.get_next();
					if(PredEval.Eval(outFilter1, t1, t2, Stypes , Stypes ) == true && PredEval.Eval(outFilter2, t1, t2, Stypes , Stypes ) == true){
						t_res.setIntFld(1, t1.getIntFld(select_Col1));
		    			t_res.setIntFld(2, t2.getIntFld(select_Col2));
						f.insertRecord(t_res.returnTupleByteArray());
						System.out.println("Record Num "+ ++rc + " : "+ t1.getIntFld(select_Col1)+","+t2.getIntFld(select_Col2));
					}
				}
			}

			
			//To display results
			FldSpec [] sProjection1 = new FldSpec[2];
		    sProjection1[0] = new FldSpec(new RelSpec(RelSpec.outer), 1);
		    sProjection1[1] = new FldSpec(new RelSpec(RelSpec.outer), 2);
		    int i=0;
		    FileScan result = null;
		    try {
				result = new FileScan("result.in", Rtypes, Rsizes, (short)2, (short)2, sProjection1, null);
				System.out.println("Num of records : "+result.getNumRecords());		
				System.out.println("Exit*********");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
			Tuple t1 = new Tuple();			
			
			System.out.println("*********Entering");
			System.out.println("Num of records main : "+result.getNumRecords());	
			while(t1 != null){
				t1 = result.get_next();
				if(t1!=null){					
					if(t1!=null){				
						System.out.println("Record Num "+ ++i +" : " + t1.getIntFld(1) + " , "+t1.getIntFld(2));
					}
				}
			}
			System.out.println("*********EXIT");
			long end = System.currentTimeMillis();
			double time = (end - start) / 1000.00;
			System.out.println("Time taken --> " + time);
			
			
			
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			System.out.println("************************************");
			e1.printStackTrace();
			System.out.println(e1.getMessage());
		}
	}
	

public static class P3Join{
	
	
	public static void main(String[] args) {
				
		// TODO Auto-generated method stub
		P3JoinDriver p = new P3JoinDriver();
		//p.Query_1a();
		//p.Query_2a();
		//p.Query_2b();
		//p.Query_2c_1();
		p.Query_1b();
		
		
		
		
		

		
	    
	    /*
	    CondExpr [] selects = new CondExpr [1];
	    selects = null;

		

	    Scan scan = null;
		RID rid = new RID();
		FileScan f = null;
		int i =0;
		try {
//			f = new FileScan("s.in",Stypes,Ssizes,
//					(short)4, (short)4,
//					  Sprojection, null);
			
			Tuple tuple = new Tuple();
		      boolean done = false;

		      while (!done) { 
				try {
				  tuple = scan.getNext(rid);
				  S s = new S(tuple);
				  //System.out.print("S :"+s.s_1);

				 

				 
				  if (tuple == null) {
				    done = true;
				  }
				}
				catch (Exception e) {
				  e.printStackTrace();
				}

				

			
		    }

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/

	}
}