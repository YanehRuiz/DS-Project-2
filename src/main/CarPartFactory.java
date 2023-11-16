package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import data_structures.ArrayList;
import data_structures.BasicHashFunction;
import data_structures.HashTableSC;
import data_structures.LinkedStack;
import interfaces.List;
import interfaces.Map;
import interfaces.Stack;

public class CarPartFactory {

	private List<PartMachine> machines;
	private List<Order> orders;
	private Map<Integer, CarPart> partCatalog;
	private Map<Integer, List<CarPart>> inventory;
	private Map<Integer, Integer> defectives;
	private Stack<CarPart> production;
	private Map<Integer, Integer>  requestedParts;




	/**
	 * 
	 * Constructor that holds the orderPath and partsPath.
	 * It also initializes the setupMachines, setupOrders
	 * and setupInventory methods. 
	 * 
	 * production Stack and defective Map are also initialized 
	 * in here
	 * 
	 * Under the constructor we have getters and setters for:
	 * machines, orders, partCatalog, inventory, defectives 
	 * and production.
	 *
	 **/
	
	
	public CarPartFactory(String orderPath, String partsPath) throws IOException {
		setupMachines(partsPath);
		setupOrders(orderPath);
		setupInventory();
		
		this.production = new LinkedStack<>();
		storeInInventory();
		
 		this.defectives = new HashTableSC<Integer, Integer>(25, new BasicHashFunction());
		 storeInInventory();

	}
	public List<PartMachine> getMachines() {
		return machines;

	}
	public void setMachines(List<PartMachine> machines) {
		this.machines = machines;

	}
	public Stack<CarPart> getProductionBin() {
		return this.production = production;

	}
	public void setProductionBin(Stack<CarPart> production) {
		this.production = production;
	}
	public Map<Integer, CarPart> getPartCatalog() {
		return partCatalog;
	}
	public void setPartCatalog(Map<Integer, CarPart> partCatalog) {
		this.partCatalog = partCatalog;
	}
	public Map<Integer, List<CarPart>> getInventory() {
		return inventory;

	}
	public void setInventory(Map<Integer, List<CarPart>> inventory) {
		this.inventory = inventory;
	}
	public List<Order> getOrders() {
		return orders;

	}
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
	public Map<Integer, Integer> getDefectives() {
		return defectives;

	}
	public void setDefectives(Map<Integer, Integer> defectives) {
		this.defectives = defectives;
	}
	
	
	
	
	/**
	 * 
	 * We read the "Order.csv" File. Using the bufferReader we extract
	 * the information we need to create a new Order. This orders are
	 * then added to the list of orders.
	 * 
	 * Process: 
	 * We skip the first line, the one that holds the guidelines for the 
	 * columns on the file. Then we extract and save the id and name. After 
	 * extracting the ID and names, we split the list by the "-" which leaves
	 * us with a list of parenthesis and numbers. After that we remove the 
	 * parenthesis that hold the orders as a list, called division. This list
	 * is then split again and parenthesis are taken away. Once the original
	 * list is broken down into numbers we add these to the hashTable, from 
	 * this we are able to construct an "order", and save said order to a list.
	 * 
	 * TLDR; We read the file, get ID and Name. We split the parenthesis divided by
	 * "-". We remove the parenthesis, and keep the numbers. We add those numbers
	 * to a Map. We construct and order, then save this order on a list.
	 *
	 **/
	
	

	public void setupOrders(String path) throws IOException {

		this.orders = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new FileReader(path));
		String line;

		while ((line = reader.readLine()) != null) {
			String[] values = line.split(",");
			if(!values[0].equals("ID")) {
				int id = Integer.parseInt(values[0]);
				String names = values[1];

				String[] division = values[2].split("-");	

				this.requestedParts = new HashTableSC<>(25, new BasicHashFunction());
				for (int i = 0; i < division.length; i++) {
					division[i]=division[i].replace("(", "").replace(")", "");
					String[] parts = division[i].split(" ");
					int request = Integer.parseInt(parts[0]);
					int requestedPart = Integer.parseInt(parts[1]);
					requestedParts.put(request, requestedPart);
				}

				Order newOrder = new Order(id, names, requestedParts, false) ;
				this.orders.add(newOrder);
				
			}



		}
		reader.close();
	}  		
	
	
	

	/**
	 * 
	 * We read the "Parts.csv" File. Using the bufferReader we extract
	 * the information we need to create a new partMachine. These machines
	 * are then added to the machines array. 
	 * 
	 * Process: 
	 * We skip the first line, the one that holds the guidelines for the 
	 * columns on the file. Then we split bu commas and then we extract 
	 * and save the id, partName , periods , weightErrors and 
	 * chanceOfDefectives. For the partName, we make a constructor consisting
	 * of CarPart's id, name from the list, weights, and we make the boolean false.
	 * After extracting these, we add the machine to the machines list. We also add
	 * the id and name to the partCatalogue Map and we push the name of the part
	 * to the production Stack. 
	 * 
	 * 
	 **/


	public void setupMachines(String path) throws IOException {
	
		
		this.production = new LinkedStack<>();
		this.partCatalog=new HashTableSC<>(15, new BasicHashFunction());
		BufferedReader reader = new BufferedReader(new FileReader(path));
		String line;

		this.machines = new ArrayList<>();
		
		while ((line = reader.readLine()) != null) {
			String[] values = line.split(",");
			if(!values[0].equals("ID")) {
				int id = Integer.parseInt(values[0]);
				double weights = Double.parseDouble(values[2]);
				CarPart partName = new CarPart(id, values[1], weights, false);
				double weightErrors = Double.parseDouble(values[3]);
				int periods = Integer.parseInt(values[4]);
				int chanceOfDefectives = Integer.parseInt(values[5]);


				PartMachine machineFactory = new PartMachine(id,partName,periods,weightErrors,chanceOfDefectives) ;
				this.machines.add(machineFactory);
				
				this.partCatalog.put(id, partName);
				this.production.push(partName);

			}
		}
		reader.close();


		//      }


	}
	public void setupInventory() {

		this.inventory=new HashTableSC<>(25, new BasicHashFunction());	
		
		for (PartMachine partMachine : machines) {
			this.inventory.put(partMachine.getPart().getId(), new ArrayList<>());
		}
	
		
		

	}
	
	
	
	
	/**
	 * 
	 * While production isnt null or empty, then we pop it. If the part 
	 * is defective we increment the defective counter.
	 * If its not defective we add it to the inventory list.
	 * 
	 * 
	 **/

	public void storeInInventory() {
		
	
		//while(production!=null) {
			//we pop it
			//if defective - def++
			//if!def .add list 
	
		
		    while (!production.isEmpty()) {
		        CarPart part = production.pop();

		        if (part.isDefective()) {
		            int partId = part.getId();
		            Integer defectiveCount = defectives.get(partId);
		            if (defectiveCount == null) {
		                defectiveCount = 0;
		            }
		            defectives.put(partId, defectiveCount + 1);
		        } else {
		            int partId = part.getId();
		            List<CarPart> partList = inventory.get(partId);

		            if (partList == null) {
		                partList = new ArrayList<>();
		                inventory.put(partId, partList);
		            }

		            partList.add(part);
		        }
		    }
		}



	public void runFactory(int days, int minutes) {
		//for loop
		//while loop
		//reset
		////EMPTY IT



	}


	public void processOrders() {
		//nested it
		//orders y req 
		//req part has enough pieces
		//if ! skip to the next
		//if yes rem from inv
		
		
		

	}
	/**
	 * Generates a report indicating how many parts were produced per machine,
	 * how many of those were defective and are still in inventory. Additionally, 
	 * it also shows how many orders were successfully fulfilled. 
	 */
	public void generateReport() {
		String report = "\t\t\tREPORT\n\n";
		report += "Parts Produced per Machine\n";
		for (PartMachine machine : this.getMachines()) {
			report += machine + "\t(" + 
					this.getDefectives().get(machine.getPart().getId()) +" defective)\t(" + 
					this.getInventory().get(machine.getPart().getId()).size() + " in inventory)\n";
		}

		report += "\nORDERS\n\n";
		for (Order transaction : this.getOrders()) {
			report += transaction + "\n";
		}
		System.out.println(report);
	}



}
