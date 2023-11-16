package main;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import data_structures.ListQueue;
import interfaces.Queue;



/**
 * 
 * Constructor that holds the id, CarPart, period, weightError and the chanceOfDefective.
 * Within the constructor it also initializes the: 
  		* timer = It goes from the period to zero, and enqueues it to go in decreasing order.
  		* conveyorBelt = (Null) Queue of static size 10.
 * Getters and Setters for the the id, CarPart, period, weightError and the chanceOfDefective.
 *
 **/

public class PartMachine {
	private int id;
	private int period;
	private CarPart part;
	private Queue<Integer> timer;
	private Queue<Integer> emptyTimer;
	private Queue<CarPart> conveyorBelt;
	public int totalPartsProduced;
	private double partWeightError;
	private int chanceOfDefective;
	



	public PartMachine(int id, CarPart p1, int period, double weightError, int chanceOfDefective) {
		this.id = id;
		this.part = p1;
		this.period = period;
	//	this.conveyorBelt = conveyorBelt;
		this.partWeightError = weightError;
		this.chanceOfDefective = chanceOfDefective;  	
	//	initializeTimer();
		timer = new ListQueue<Integer>();
		for (int i = getPeriod() - 1; i >= 0; i--) {  //timer creation
            timer.enqueue(i);
        }
		conveyorBelt = new ListQueue<CarPart>();
		for (int i = 0; i < 10; i++) {
			conveyorBelt.enqueue(null);
		}
		
	}

	/**
	 * 
	 * Get Period Method.
	 * 
	 */

	public int getPeriod() {
		return period;
	}
	
	public void setPeriod() {
		this.period = period; 
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Queue<Integer> getTimer() {
		return timer;
	}
	public void setTimer(Queue<Integer> timer) {
		this.timer = timer;
	}
	public CarPart getPart() {
		return part;
	}
	public void setPart(CarPart part1) {
		this.part = part1;
	}
	public Queue<CarPart> getConveyorBelt() {
		return conveyorBelt;        
	}
	public void setConveyorBelt(Queue<CarPart> conveyorBelt) {
		this.conveyorBelt = conveyorBelt;
	}
	public int getTotalPartsProduced() {
		return totalPartsProduced;         
	}
	public void setTotalPartsProduced(int count) {
		this.totalPartsProduced = count;
	}
	public double getPartWeightError() {
		return partWeightError;        
	}
	public void setPartWeightError(double partWeightError) {
		this.partWeightError = partWeightError;
	}
	public int getChanceOfDefective() {
		return chanceOfDefective;
	}
	public void setChanceOfDefective(int chanceOfDefective) {
		this.chanceOfDefective = chanceOfDefective;
	}


	public void resetConveyorBelt() {
		//		 this.conveyorBelt = conveyorBelt;
		conveyorBelt.clear();
		for (int i = 0; i < 10; i++) {
			conveyorBelt.enqueue(null);
		}

	}
	public int tickTimer() {
		
		int front = getTimer().front();
		int temp = front;
		        getTimer().enqueue(getTimer().dequeue()); // tick the queue. 
		    
		    return temp;
		}

	
	public CarPart produceCarPart() {
		int timerValue = tickTimer();
		if (timerValue == 0) {
		
		double maxWeight = part.getWeight() + getPartWeightError();
		double minWeight = part.getWeight() - getPartWeightError();
		double randomWeight = (ThreadLocalRandom.current().nextDouble(minWeight,maxWeight));
			
			CarPart newPart = new CarPart(part.getId(), part.getName(), randomWeight, false);
			if (totalPartsProduced%chanceOfDefective==0) {
				newPart.setDefective(true);
			}
			totalPartsProduced++;
			conveyorBelt.dequeue();
			conveyorBelt.enqueue(newPart);
			
			return newPart;
		} else {
			conveyorBelt.dequeue();
			conveyorBelt.enqueue(null);
			return null;
		}
	}

	/**
	 * Returns string representation of a Part Machine in the following format:
	 * Machine {id} Produced: {part name} {total parts produced}
	 */
	@Override
	public String toString() {
		return "Machine " + this.getId() + " Produced: " + this.getPart().getName() + " " + this.getTotalPartsProduced();
	}
	/**
	 * Prints the content of the conveyor belt. 
	 * The machine is shown as |Machine {id}|.
	 * If the is a part it is presented as |P| and an empty space as _.
	 */
	public void printConveyorBelt() {
		// String we will print
		String str = "";
		// Iterate through the conveyor belt
		for(int i = 0; i < this.getConveyorBelt().size(); i++){
			// When the current position is empty
			if (this.getConveyorBelt().front() == null) {
				str = "_" + str;
			}
			// When there is a CarPart
			else {
				str = "|P|" + str;
			}
			// Rotate the values
			this.getConveyorBelt().enqueue(this.getConveyorBelt().dequeue());
		}
		System.out.println("|Machine " + this.getId() + "|" + str);
	}
}
