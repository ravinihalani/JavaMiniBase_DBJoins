package tests;


import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.opencsv.CSVReader;

public class Histogram {
	
	public static void singlereadFromExcel(String Filename,int col, ArrayList<Integer> columnValues) throws IOException{
		//System.out.println(col);
		CSVReader reader = new CSVReader(new FileReader("res/"+Filename));
		 String [] nextLine;
		 int count=0;
		 ArrayList<String []> file = new ArrayList<String []>();
		 while ((nextLine = reader.readNext()) != null) {
			 columnValues.add(Integer.parseInt(nextLine[col-1]));
			// System.out.println(nextLine);
		 }
	}

	static ArrayList<Integer> getHistogram (String table_name,int column_no,int percentage) throws IOException
	{
		ArrayList<Integer> histogram = new ArrayList<Integer>();
		
		ArrayList<Integer> columnValues = new ArrayList<Integer>();
		singlereadFromExcel(table_name+".csv",column_no, columnValues);
		//System.out.println(columnValues.size());
		int histogram_length=(int) (columnValues.size()*percentage*0.01);
		//System.out.println("HISTO LENGTH"+histogram_length);
		Collections.sort(columnValues); 
		int max=columnValues.get(columnValues.size() - 1);
		int min=columnValues.get(0);
		//System.out.println("Max" + max );
		//System.out.println("Min" + min);
		int diff= (max-min)/10;
		//System.out.println("Diff"+diff);
		
		int sets[]={min,min+diff,min+2*diff,min+3*diff,min+4*diff,min+5*diff,min+6*diff,min+7*diff,min+8*diff,min+9*diff,max};
		//System.out.println(Arrays.toString(sets));
		
		
		int count =0;
		whileloop: while (count < histogram_length) {
			for (int i = 0; i < sets.length - 1; i++) {
				innerloop: for (int j = 0; j < columnValues.size(); j++) {
					if (columnValues.get(j) >= sets[i] && columnValues.get(j) < sets[i + 1]) {
						histogram.add(columnValues.get(j));
						//System.out.println(columnValues.get(j));
						columnValues.remove(j);
						++count;
						if (count >= histogram_length) {
						break whileloop;
						}
						break innerloop;
					}
				}
			}
		}
		//System.out.println("HISTOGRAM VALUES");
		for(int i = 0; i < histogram.size(); i++) {   
		   // System.out.println(histogram.get(i));
		} 
		return histogram;
	}
	
	
//	public static void main (String[] args) throws IOException
//	{
//		System.out.println("Ravi");
//		getHistogram("/Users/ravinihalani/Documents/workspace/histogram/src/F1r",1,1);
//	}

}