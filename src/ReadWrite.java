/*	ReadWrite Class
 * 	Lee, Juhyun
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;


/*
 *	ReadWrite class is only a extension to GAKnapsack
 *	Separated the file input output handling methods.
 */
public class ReadWrite extends GAKnapsack{
	
	public ReadWrite(){
	}//end of empty constructor
	
	//	this method will out put in to console of all the static data before the program runs
	public static void OutToConsole(){
		
		System.out.println("max population	" + maxPop );
		System.out.println("max generation	" + maxGen );
		System.out.println("crossover rate	" + crossRate );
		System.out.println("Mutation rate	" + mutRate);
		System.out.println("accepted Fitness	" + acceptFit  );
		System.out.println("weight of bag	" + bagWeight  );
		System.out.println("total # of Items	" + numOfItem  );
		
	}//end of out to console method.
	
	
	//	setPara method will handle the parameter.txt file
	//	read in to assign the static value.
	public static void setPara() throws FileNotFoundException, IOException{
		
		try(BufferedReader br = new BufferedReader(new FileReader("parameter.txt"))) {
			
			StringTokenizer st;
	        String line = br.readLine();
	        
	        String para = new String();
	        
	       
			//	while loop will tokenize and assign the value to correct static variable.
	        while(line != null ){
	        	
	        	st = new StringTokenizer(line , "	|	");
	        	para = st.nextToken();

	        	if(para.equals("Population Size"))
	        		maxPop = Integer.parseInt(st.nextToken());
	        	
	           	else if(para.equals("Max Generation"))
	        		maxGen = Integer.parseInt(st.nextToken());
	        	
	        	else if(para.equals("CrossOver Rate"))
	        		crossRate = Integer.parseInt(st.nextToken());
	        	
	        	else if(para.equals("Mutation Rate"))
	        		mutRate = Integer.parseInt(st.nextToken());
	        	
	        	else if(para.equals("top % of Population allowed to mate"))
	        		acceptFit = Integer.parseInt(st.nextToken());
	        	
	        	line = br.readLine();
	        	
	        }//end of while
	    
	    br.close(); //close file parameter.txt
	        
		}//end of try for parameter set.
		
		catch(Exception e){
			System.exit(0);
		}//end of catch.
		
	}//end of public static setPara
	
	
	//	
	//	setItem method will read the input text file and assign a value accordingly.
	public static void setItem() throws FileNotFoundException, IOException{
		
		// i variable to count for while loop.
		int i = 0;
		
		try(BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
			StringTokenizer st;
	        String line = br.readLine();
	        String inputs = new String();
	        
	        while( ! line.contains("-") ){
	        	
	        	st = new StringTokenizer(line , "	|	");
	        	inputs = st.nextToken();

	        	if(inputs.equals("Bag Weight"))
	        		bagWeight = Integer.parseInt(st.nextToken());
	        	
	           	else if(inputs.equals("# Of Items"))
	        		numOfItem = Integer.parseInt(st.nextToken());

	        	line = br.readLine();
	        	
	        }//end of while
	        
	        //read in the number of items from text file and allocate the array to size of item.
	        itemWeight = new double [numOfItem];
	        
	        line = br.readLine();
	        double totalItemWeight = 0;
	        
	        // using while loop, fill the itemweight array.
	        while (line != null && i < numOfItem ) {
	        	itemWeight[i] = Double.parseDouble(line);
	            line = br.readLine();
	            totalItemWeight = totalItemWeight + itemWeight[i];
	            i++;
	        }//end of while.
	        
	        //if the added total of all the item is less than the bag, than no need to run the program.
	        //it will notify to console than exit.
	        if(totalItemWeight <= bagWeight){
	        	
	        	line = "total value of item added is less or equal to bag weight limit";
	        	WritetoFile(line);
	        	System.out.println(line);
	        	System.exit(0);
	        	
	        }//end of if
	        
	    br.close(); //close file input.txt
	        
		}//end of try
		catch(Exception e){
			System.exit(0);
		}//end of catch.
		
	}//end of public static void setItem.
	
	//	Write to file method,
	//	it will build a string(stringbuilder - no need for synchronization on the string.)
	//	write to result.txt file
	//	take in single Individual as parameter, since only one is requred to be written on the file.
	public static void WritetoFile(Individual bestFit) throws IOException{
		
		BufferedWriter out = new BufferedWriter(new FileWriter("result.txt"));
		StringBuilder sb = new StringBuilder();
		
		//	if statement to see if its true solution
		if(bestFit.getFitness() == 100.0){
			sb.append("Found the True Solution. from Generation " + currGen + "\n");
		}//	end of if
		else{
			sb.append("The best possible Solution out of " + currGen + " generation : \n" );
			sb.append("best Fitness of " + bestFit.getFitness() + " %\n");
		}//	end of else	
		
		//	out to system console, and write to txt file.
		//	clear the stringbuilder due to not knowing the number of items.
		System.out.println(sb);
		out.write(sb.toString());
		sb.setLength(0);
		
		//
		for(int i = 0; i < numOfItem; i++){
				sb.append(itemWeight[i] + "	:	" +  bestFit.indivArr[i] + "\n");			
		}//end of for
		
		sb.append("with the added weight of Items of : " + bestFit.GetindivWeight());
		
		System.out.println(sb);
		out.write(sb.toString());
		
		//	close file.
		out.close();
		 
	}//end of Write to File method.
	
	//	simple method which will take a string than write it to txt file.
	//	only used when bag has more weight than all the items combined.
	public static void WritetoFile(String str) throws IOException{
		
		BufferedWriter out = new BufferedWriter(new FileWriter("result.txt"));
		out.append(str);
		
		out.close();
		
	}//end of write to file with string parameter 

}//end of read write class
