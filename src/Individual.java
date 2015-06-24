/*	CS381 Project - Individual Class
 * 	Summer 2014
 * 	Lee, Juhyun
 */

import java.util.Random;

//	individual class for representing indivdual in population
//	contains chromosome of the individual(array of binary.)
//	contains fitness value
//	contains the value of items added
public class Individual {
	
	double fitness = 0;
	int[] indivArr;
	double indivWeight = 0.0;
	
	
	//	constructor with parameter integer for assigning binary array.
	public Individual( int n ){
		
		indivArr = new int [n];
		
	}//end of individual constructor with int parameter
	
	//	constructor with parameter int and int array 
	//	used when individual is created with same binary array is needed.
	public Individual( int n , int[] tempArr){
		
		indivArr = new int [n];
		indivArr = tempArr;
		
	}//end of individual constructor with two parameter
	
	
	//	sets the fitness of the individual
	public void setFitness( double d){
		
		this.fitness = d;	
	
	}//end of setfitness method.
	
	
	//	fetches the fitness of the individual
	public double getFitness(){
		
		return fitness;
	}//end of fitness method.
	
	
	//	randomly fill the binary(integer) array.
	public void randomfill(){
		
		Random n = new Random();
		
		for(int i = 0; i < indivArr.length; i ++){
			this.indivArr[i] = n.nextInt(2);	
			
		}//end of for loop
		
	}//end of randomfill method.
	
	
	//	assign weight to the individual
	public double GetindivWeight(){
		return indivWeight;
	}//end of get indvidiaul weight method.
	
	
	//	sets the individuals weight of items included in the array.
	public void SetIndivWeight( double weight){
		this.indivWeight = weight;
		
	}//end of set Individual Weight
	
	
	

}//end of class individual.
