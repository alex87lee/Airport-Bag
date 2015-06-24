/*	CS381 Project - GAKnapsack class
 * 	Summer 2014
 * 	Lee, Juhyun
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GAKnapsack {
	
	//parameter variables
	public static int crossRate = 0;
	public static int mutRate = 0;
	public static int maxPop = 0;
	public static int maxGen = 0;
	public static double acceptFit = 0;
	
	//input variables
	public static int numOfItem = 0;
	public static double bagWeight = 0;
	public static double[] itemWeight;

	//program variables
	public static int currGen = 0;
	protected static Individual[] currPop;
	private static ArrayList<Individual> allowMate = new ArrayList<Individual>();
	private static int matePopSize = 0;
	
	//	main.
	//	mostly it will call methods in the class.
	//	find the best ftting individual in loop than pass to the readwrite method to be
	//	written to result.txt file.
	public static void main(String[] args) throws FileNotFoundException, IOException {
			
		ReadWrite.setPara();
		ReadWrite.setItem();
		
		ReadWrite.OutToConsole();
		
		currPop = new Individual[maxPop];
		
		matePopSize = (int) Math.ceil((acceptFit*0.01)*maxPop);
		
		while( currGen < maxGen){
			FillPop();
			SortTopPop();
			currGen++;
			
			System.out.println("Currently At Generation " + currGen);
			
		}
		
		Individual bestFit = new Individual(numOfItem);
		
		bestFit = currPop[0];
		
		for(int i = 0; i < maxPop; i++){
			if(bestFit.getFitness() < currPop[i].getFitness() && currPop[i].getFitness() < 100){
				bestFit = currPop[i];
			}//end of if
			else if(bestFit.getFitness() >100 && currPop[i].getFitness() < 100){
				bestFit = currPop[i];
			}//end of else if
			
		}//end of for
		
		ReadWrite.WritetoFile(bestFit);

	}//end of main.
	
	
	//	filling Population method.
	//	this will count the generation number and if its zero, randomly fill the population
	//	if generation number is above zero, it will call crossover method than mutation method.
	//	arraylist allowMate is collection of all the individual object that according to the parameter
	//	is fit to mate, the population is sorted before fillPop method is called if generation is above zero
	//	crossover is done by passing two individuals. mutation is done by passing currPop position
	//	NOTE that since all the individuals that were allowed to mate is in allowMate arraylist,
	//	there is no need to keep the original population, so the static currPop array is overwritten.
	public static void FillPop() throws IOException{
		
		//double file to temporary store fitness value.
		double tempFit = 0.0;
		
		if(currGen == 0){
			for(int i = 0; i < maxPop; i++){
				
				currPop[i] = new Individual(numOfItem);
				currPop[i].randomfill();				
				tempFit = FitnessFunc(i);		
				currPop[i].setFitness(tempFit);
				
			}//end of for loop	
		}//end of if
		
		else{
			
			//	fill the allowMate array list.
			for(int i = 0; i <= matePopSize; i ++){
					allowMate.add(currPop[i]);
			}//end of for loop
			
			//	k is incrementor, ranlimit is limit of random number boundary.
			int k = 0;
			int ranlimit = allowMate.size();
			
			Random rand = new Random();
			
			//	while loop to overwrite the currPop by doing cross over and mutation.
			while(k < maxPop){
				
				int pos1 = rand.nextInt(ranlimit);
				int pos2 = rand.nextInt(ranlimit);
			
				currPop[k] = Crossover(allowMate.get(pos1) , allowMate.get(pos2) );
				Mutation(k);
				
				tempFit = FitnessFunc(k);
				currPop[k].setFitness(tempFit);
				
				if( currPop[k].getFitness() == 100){
					ReadWrite.WritetoFile(currPop[k]);
					System.exit(0);
				}//end of if
				
				k++;
				
			}//end of while loop
			
			//	clear the arraylist for later usage.
			allowMate.clear();
	
		}//end of else.
		
	}//end of fillpop
	
	
	//	Fitness Function method will intake the currPop array position value than 
	//	calculate the fitness as a percentage (out of 100 %)
	// 	than it will return the fitness percentage as double and store it on to individual's fitness value.
	public static double FitnessFunc( int temp ) throws IOException{
		
		//	temporary storage for added weight.
		double addedWeig = 0;
		
		//	for loop to add all the weight by looking at the static information array with the weights
		// 	and match them with 0 and 1 from individual's boolean array with equal size.
		
		for(int i = 0; i < currPop[temp].indivArr.length; i++){
			if(currPop[temp].indivArr[i] == 1){
				addedWeig = addedWeig + itemWeight[i];
			}//end of if
		}//end of for.
		
		//	set the total weight of the individual.
		currPop[temp].SetIndivWeight(addedWeig);
		
		//calculate the fitness as percentage.
		double fitperc = (addedWeig / bagWeight) * 100;
		
		//	returns to fillPop method.
		return fitperc ;
				
	}//end of fitnessfunc
	
	
	//	sortTopPop method will sort the array, but not all the array.
	//	as parameter input of allowed to mate population at its top fitness
	//	it will sort only the highest fitness, and simultaneously being less than 100 percent
	//	fit them in to parameter based top percent
	//	rest is left alone.
	//	it is selection sort up to matePopSize(mate-able population size)
	public static void SortTopPop(){
		
		int k = 0;
		Individual temp = new Individual(numOfItem);
		int tempPos = 0;
		
		while(k <= matePopSize){
			tempPos = k;
			for(int i = k + 1; i < maxPop; i++){
	
				if(currPop[tempPos].getFitness() < currPop[i].getFitness() && currPop[i].getFitness() < 100){
					tempPos = i;
				}//end of if
				else if(currPop[tempPos].getFitness() >100 && currPop[i].getFitness() < 100){
					tempPos = i;
				}//end of else if
			}//end of for
			
			currPop[k] = temp;
			currPop[k] = currPop[tempPos];
			currPop[tempPos] = temp; 
			
			k++;
		}//end of while
		
	}//end of sorting top population size.
	
	//	crossover method will perform crossover with two individual
	//	it is one bit cross over,
	//	crossover rate is given by parapmeter.txt
	public static Individual Crossover (Individual father , Individual mother){
		
		Random diceRoll = new Random();
		Individual offSpring = new Individual(numOfItem);
		
		for(int i = 0; i < father.indivArr.length; i++){

			if(diceRoll.nextInt(101) <= crossRate){
				offSpring.indivArr[i]= mother.indivArr[i];
			}//end of if
			else{
				offSpring.indivArr[i] = father.indivArr[i];
			}//end of else
			
		}//end of for loop
		
		//	returns to fillPop method.
		return offSpring;
	}//end of cross over
	
	
	//	mutation method is done by passing the currPop array position
	//	since the old population is overwritten by new crossovered offspring.
	//	change the bit by (x+1)%2 , x being any binary value.
	public static void Mutation(int arrPos){
		
		Random diceRoll = new Random();
		
		for(int i = 0; i < currPop[arrPos].indivArr.length; i++){
			if(diceRoll.nextInt(101) <= mutRate){

					currPop[arrPos].indivArr[i]= (currPop[arrPos].indivArr[i] + 1) % 2;

			}//end of if
			
		}//end of for loop
	}//end of Mutation.
	
	
}//end of GAKnapsack class.
