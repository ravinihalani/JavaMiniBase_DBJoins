import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.QNameSet;

import com.opencsv.CSVReader;

public class mul {

	static HashMap<String, ArrayList<Integer>> join(twoPredicate q, ArrayList<Integer> next, int which, HashMap<String, ArrayList<Integer>> tables) throws IOException{
		// Variable declaration
		//ArrayList<ArrayList<Integer>> tuples = new ArrayList<ArrayList<Integer>>();
		HashMap<String,ArrayList<Integer>> store = new HashMap<String,ArrayList<Integer>>();
		// store the keys of tables in store 
		for(int i=0;i<tables.size();i++){
			store.put(tables.keySet().toArray()[i].toString(), new ArrayList<Integer>());
		}
		/*for(int i=0;i<next.size();i++)
			tuples.add(new ArrayList<Integer>());
		*/ArrayList<Integer> columns = new ArrayList<Integer>();
		
		String line = null;
		String relations [] = null;
		String[] query = new String [5];
		int temp2pos =0,temp1pos=0,countlast=1;

		int lineNumber = 0,x,xdash,y,ydash,op1,op2,off2,off1,last,b,ano,rev=0,orderpos;
		
		int t,start,end,current,left;
		String rF1rTypes = null;
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
		
		
		
        // INITIALIZATION process
        x = q.LHS1Column;
        xdash = q.LHS2Column;
        op1 = q.LHS;
        y = q.RHS1Column;
        ydash = q.RHS2Column;
        op2 = q.RHS;
       
        
        String Filename ="res/"+q.LHS1+".csv";
        if(tables.get(q.LHS1).size()>0){
        	// already visited table
        // l1 should contain the columns it wants
        //l2 should contain the columns it wants
        // Filename,l1,colNumber,rows
        	singlereadFromExcel(Filename,l1,q.LHS1Column,tables.get(q.LHS1));
        	singlereadFromExcel(Filename,l2,q.RHS1Column,tables.get(q.LHS1));
        }
        else{
        	// not visited already
        	readFromExcel(Filename,l1,x);
            readFromExcel(Filename,l2,y);
        }
        //System.out.println(l1);
        //System.out.println(l2);
        //System.out.println(x+""+xdash+""+y+""+ydash+""+op1+""+op2);
        
        
        
        // l1 & l2 ready
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
	//System.out.println(l1pos);
	
	
	
	///
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
     //System.out.println(l1pos);
     //System.out.println(l2pos);
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
     System.out.println("p ready");
	//////////////////////////////////////////
    //F1r side over above
     ////////////////////////////////////////
     
     /*Filename ="res/"+q.LHS2+".csv";
     readFromExcel(Filename,l1dash,xdash);
     readFromExcel(Filename,l2dash,ydash);
     */
     Filename ="res/"+q.LHS2+".csv";
     if(tables.get(q.LHS2).size()>0){
     	// already visited table
     // l1 should contain the columns it wants
     //l2 should contain the columns it wants
     // Filename,l1,colNumber,rows
     	singlereadFromExcel(Filename,l1dash,q.LHS2Column,tables.get(q.LHS2));
     	singlereadFromExcel(Filename,l2dash,q.RHS2Column,tables.get(q.LHS2));
     }
     else{
     	// not visited already
     	readFromExcel(Filename,l1dash,xdash);
         readFromExcel(Filename,l2dash,ydash);
     }
     
////////////////////l1dashpos
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
System.out.println("pdash ready");
///////////////////////////// intialization over above


/*  System.out.println("Initialization .......");
//n.nextInt();
System.out.println("l1:\t\t"+l1);
//System.out.println("l1pos:\t "+l1pos);
System.out.println("l2:\t\t"+l2);
//System.out.println("l2pos:\t "+l2pos);
System.out.println("p:\t\t"+p);            
System.out.println("l1dash: \t"+l1dash);
System.out.println("l2dash: \t"+l2dash);
//System.out.println("l2dashpos: \t"+l2dashpos);
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
		//System.out.println("Ans:\t\t"+l2pos.get(i)+" "+l1dashpos.get(b-1));
		if(which==0){
			store.get(q.LHS1).add(l2pos.get(i));
			store.get(q.LHS2).add(l1dashpos.get(b-1));	
		}
		else{

			for(int fornext=0;fornext<tables.size();fornext++){
				if(tables.keySet().toArray()[fornext].toString().equals(q.LHS2)){
					// new tables elements
					store.get(q.LHS2).add(l1dashpos.get(b-1));
				}
				else{
					// old ones
					System.out.println(tables.get(tables.keySet().toArray()[fornext].toString()).size()+"--->"+(l2pos.get(i)-1));
					if(tables.get(tables.keySet().toArray()[fornext].toString()).size()==0){
						int tostop=9;
					}
					store.get(tables.keySet().toArray()[fornext].toString()).add(tables.get(tables.keySet().toArray()[fornext].toString()).get(l2pos.get(i)-1));
	
				}
				
			}
			
		}
		
		
		/*columns.clear();
		singlereadFromExcel(Filename,columns,l1dashpos.get(b-1),next);
		for(int add=0;add<next.size();add++)
			tuples.get(add).add(columns.get(add));*/
		b = bdash.get(b-1);
	}

}
return store;
	}
	public static void singlereadFromExcel(String Filename, ArrayList<Integer> a,int col,ArrayList<Integer> rows) throws IOException{
		//System.out.println(col);
		CSVReader reader = new CSVReader(new FileReader(Filename));
		 String [] nextLine;
		 int count=0;
		 ArrayList<String []> file = new ArrayList<String []>();
		 while ((nextLine = reader.readNext()) != null) {
			 file.add(nextLine);
			 /*count++;
			 if(rows.contains(o)){
				 // row to be read;
				 for(int i=0;i<col.size();i++){
					 a.add(Double.valueOf(nextLine[col.get(i)-1]).intValue());
					 
				 }
			 }*/
		 }
		 for(int i=0;i<rows.size();i++){
			 a.add(Double.valueOf(file.get(rows.get(i)-1)[col-1]).intValue());
		 }
	}
	
	public static void readFromExcel(String Filename,ArrayList<Integer> a,int x) throws IOException{
		System.out.println(Filename);
		CSVReader reader = new CSVReader(new FileReader(Filename));
		 String [] nextLine;
	     while ((nextLine = reader.readNext()) != null) {
	        // nextLine[] is an array of values from the line
	        //System.out.println(nextLine[0] + nextLine[1] + " -   -"+ nextLine.length);
	    	 //a.add(Integer.parseInt(nextLine[x-1]));
	    	 //b.add(Integer.parseInt(nextLine[y-1]));
	    	 //System.out.println(x);
	    	 a.add(Double.valueOf(nextLine[x-1]).intValue());
	    	 //b.add(Double.valueOf(nextLine[y-1]).intValue());
	     }
		}
	public static int diag(ArrayList<twoPredicate> q){
		String a,b,c,d;
		int found = -1;
		int temp;String temp1;
		ArrayList<String> visited = new ArrayList<String>();
		visited.add(q.get(0).LHS1);
		visited.add(q.get(0).LHS2);
		a = q.get(0).LHS1;
		b = q.get(0).LHS2;
		c = q.get(0).RHS1;
		d = q.get(0).RHS2;
		if(!a.equals(c)|| !b.equals(d))
			return 0;
		for(int i=1;i<q.size();i++){
			a = q.get(i).LHS1;
			b = q.get(i).LHS2;
			c = q.get(i).RHS1;
			d = q.get(i).RHS2;
			if(visited.contains(a)&&visited.contains(b)){
				// both of the tables have been visited;
				return 0;
			} 
			else if(visited.contains(a)|| visited.contains(b)){
				// exactly one of them has been visited earlier
				if(visited.contains(a)){
					if((a.equals(c) && b.equals(d)) || (a.equals(d)&&b.equals(c))){
						// right and left has same predicates
						if(a.equals(d)&&b.equals(c)){
							// swap d & c
							q.get(i).RHS2 = c;
							q.get(i).RHS1 = d;
							temp = q.get(i).RHS2Column;
							q.get(i).RHS2Column = q.get(i).RHS1Column;
							q.get(i).RHS1Column = temp;
							if(q.get(i).RHS==1)
								q.get(i).RHS = 4;
							else if(q.get(i).RHS==2)
								q.get(i).RHS = 3;
							else if(q.get(i).RHS==3)
								q.get(i).RHS = 2;
							else if(q.get(i).RHS==4)
								q.get(i).RHS = 1;
							
						}
						visited.add(b);
					}
					
				
					else{
						// not same predicates
						return 0;
					}
					
				}
				else{
					// visited contains b
					q.get(i).LHS2 = a;
					q.get(i).LHS1 = b;
					temp = q.get(i).LHS2Column;
					q.get(i).LHS2Column = q.get(i).LHS1Column;
					q.get(i).LHS1Column = temp;
					if(q.get(i).LHS==1)
						q.get(i).LHS = 4;
					else if(q.get(i).LHS==2)
						q.get(i).LHS = 3;
					else if(q.get(i).LHS==3)
						q.get(i).LHS = 2;
					else if(q.get(i).LHS==4)
						q.get(i).LHS = 1;
					a = q.get(i).LHS1;
					b = q.get(i).LHS2;
					// now visited contains a

					if((a.equals(c) && b.equals(d)) || (a.equals(d)&&b.equals(c))){
						// right and left has same predicates
						if(a.equals(d)&&b.equals(c)){
							// swap d & c
							q.get(i).RHS2 = c;
							q.get(i).RHS1 = d;
							temp = q.get(i).RHS2Column;
							q.get(i).RHS2Column = q.get(i).RHS1Column;
							q.get(i).RHS1Column = temp;
							if(q.get(i).RHS==1)
								q.get(i).RHS = 4;
							else if(q.get(i).RHS==2)
								q.get(i).RHS = 3;
							else if(q.get(i).RHS==3)
								q.get(i).RHS = 2;
							else if(q.get(i).RHS==4)
								q.get(i).RHS = 1;
							
						}
						visited.add(b);
					}
					
				
					else{
						// not same predicates
						return 0;
					}
					
				
				}
			}
			else{
				// none of them has been visited earlier
				return 0;
			}
		
		}
		return 1;
		
	}
	public static void diagonalize(ArrayList<twoPredicate> q){
		String a ;
		String b ;
		int found = -1;
		int temp;String temp1;
		for(int i=1;i<q.size();i++){
			// see which is continued
			 a = q.get(i-1).LHS1;
			 b = q.get(i-1).LHS2;
			found = -1;
			if(q.get(i).LHS1.equals(a)||q.get(i).LHS2.equals(a))
				found = 0;
			else 
				found =1;
			if(found == 0){
				if(!q.get(i).LHS1.equals(a)){
					q.get(i).LHS2 = q.get(i).LHS1;
					q.get(i).LHS1 = a;
					temp = q.get(i).LHS2Column;
					q.get(i).LHS2Column = q.get(i).LHS1Column;
					q.get(i).LHS1Column = temp;
					if(q.get(i).LHS==1)
						q.get(i).LHS = 4;
					else if(q.get(i).LHS==2)
						q.get(i).LHS = 3;
					else if(q.get(i).LHS==3)
						q.get(i).LHS = 2;
					else if(q.get(i).LHS==4)
						q.get(i).LHS = 1;
				}
				if(!q.get(i).RHS1.equals(a)){
					q.get(i).RHS2 = q.get(i).RHS1;
					q.get(i).RHS1 = a;
					temp = q.get(i).RHS2Column;
					q.get(i).RHS2Column = q.get(i).RHS1Column;
					q.get(i).RHS1Column = temp;
					if(q.get(i).RHS==1)
						q.get(i).RHS = 4;
					else if(q.get(i).RHS==2)
						q.get(i).RHS = 3;
					else if(q.get(i).RHS==3)
						q.get(i).RHS = 2;
					else if(q.get(i).RHS==4)
						q.get(i).RHS = 1;
				}
			}
			else if(found ==1){

				if(!q.get(i).LHS1.equals(b)){
					q.get(i).LHS2 = q.get(i).LHS1;
					q.get(i).LHS1 = b;
					temp = q.get(i).LHS2Column;
					q.get(i).LHS2Column = q.get(i).LHS1Column;
					q.get(i).LHS1Column = temp;
					if(q.get(i).LHS==1)
						q.get(i).LHS = 4;
					else if(q.get(i).LHS==2)
						q.get(i).LHS = 3;
					else if(q.get(i).LHS==3)
						q.get(i).LHS = 2;
					else if(q.get(i).LHS==4)
						q.get(i).LHS = 1;
				}
				if(!q.get(i).RHS1.equals(b)){
					q.get(i).RHS2 = q.get(i).RHS1;
					q.get(i).RHS1 = b;
					temp = q.get(i).RHS2Column;
					q.get(i).RHS2Column = q.get(i).RHS1Column;
					q.get(i).RHS1Column = temp;
					if(q.get(i).RHS==1)
						q.get(i).RHS = 4;
					else if(q.get(i).RHS==2)
						q.get(i).RHS = 3;
					else if(q.get(i).RHS==3)
						q.get(i).RHS = 2;
					else if(q.get(i).RHS==4)
						q.get(i).RHS = 1;
				}
			
				
			}
			
			
		}
	}
	public static void showQuery(ArrayList<twoPredicate> q){
		for(int i=0;i<q.size();i++){
			q.get(i).show();
		}
	}
	
	public static void thejoin(ArrayList<twoPredicate> qs) throws IOException{
		ArrayList<Integer> next = new ArrayList<Integer>();
		HashMap<String, ArrayList<Integer>> got = new HashMap<String, ArrayList<Integer>>();
		HashMap<String,ArrayList<Integer>> tables = new HashMap<String,ArrayList<Integer>>();
		int found = -1;
		for(int i=0;i<qs.size();i++){
			if(tables.get(qs.get(i).LHS1) == null){
				tables.put(qs.get(i).LHS1, new ArrayList<Integer>());	
			}
			if(tables.get(qs.get(i).LHS2) == null){
				tables.put(qs.get(i).LHS2, new ArrayList<Integer>());	
			}
			
			tables = join(qs.get(i),next,i,tables);
			if(tables.get(qs.get(0).LHS1).size()<=0){
				break;
			}
			System.out.println("------Over---"+"-----------");
			qs.get(i).show();
		}
/*		next.clear();
		next.add(1);
		next.add(2);
		got=join(qs.get(qs.size()-1),next,qs.size(),got);*/
		System.out.println("\n\nFinally");
		//System.out.println(got);
		System.out.println("Count(*) = "+tables.get(qs.get(0).LHS1).size());
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		twoPredicate q = new twoPredicate();
		//q.set("east", 2, 3, "west", 2, "east", 3, 2, "west", 3);
		ArrayList<twoPredicate> qs = new ArrayList<twoPredicate>();
		ArrayList<ArrayList<Integer>> resulttuples = new ArrayList<ArrayList<Integer>>();
		/*q.set("F1r", 3, 4, "F2r", 3, "F1r", 4, 1, "F2r", 4);
		qs.add(q);
		q = new twoPredicate();
		q.set("F2r", 6, 1, "F3r", 3, "F2r", 7, 4, "F3r", 2);
		qs.add(q);
		q = new twoPredicate();
		q.set("F3r", 6, 4, "F4r", 3, "F3r", 7, 1, "F4r", 4);
		qs.add(q);
		q = new twoPredicate();
		q.set("F4r", 6, 1, "F5r", 3, "F4r", 7, 4, "F5r", 2);
		qs.add(q);*/
		
		q.set("F1r", 3, 4, "F2r", 3, "F1r", 4, 1, "F2r", 4);
		//r,salary, >, s,salary,r,tax,< s,tax 
		//q.set(table name,tables
		qs.add(q);
		q = new twoPredicate();
		q.set("F1r", 3, 4, "F4r", 3, "F1r", 4, 1, "F4r", 4);
		qs.add(q);
		q = new twoPredicate();
		q.set("F2r", 6, 1, "F3r", 3, "F2r", 7, 4, "F3r", 2);
		qs.add(q);
		q = new twoPredicate();
		q.set("F4r", 6, 1, "F5r", 3, "F4r", 7, 4, "F5r", 2);
		qs.add(q);
		
		
		/*q.set("1", 1, 2, "2", 1, "1", 2, 3, "2", 2);
		qs.add(q);
		q = new twoPredicate();
		q.set("5", 1, 2, "2", 1, "5", 2, 3, "2", 2);
		qs.add(q);
		q = new twoPredicate();
		q.set("4", 1, 2, "1", 1, "4", 2, 3, "1", 3);
		qs.add(q);
		q = new twoPredicate();
		q.set("3", 1, 2, "4", 1, "3", 2, 3, "4", 2);
		qs.add(q);*/
		//showQuery(qs);
		//diagonalize(qs);
		//System.out.println("");
		if(diag(qs) == 1){
			showQuery(qs);
			Long start = System.currentTimeMillis();
			thejoin(qs);
			float unoptime = (float)((float)(System.currentTimeMillis() - start)/(float)60000);
		System.out.println("Time: ="+unoptime);
		}
		
		//System.out.println("Count(*) = "+resulttuples.get(0).size());
	}

}
