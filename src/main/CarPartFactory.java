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




	public CarPartFactory(String orderPath, String partsPath) throws IOException {
		setupMachines(partsPath);
		setupOrders(orderPath);
		setupInventory();
		
		this.production = new LinkedStack<>();
		storeInInventory();
		
 		this.defectives = new HashTableSC<Integer, Integer>(25, new BasicHashFunction());
		// storeInInventory();

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

	public void storeInInventory() {
		
	
		//while(production!=null) {
			//we pop it
			//if defective - def++
			//if!def .add list 
	//	}
		



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
