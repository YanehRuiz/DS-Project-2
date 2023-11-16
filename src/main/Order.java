package main;

import java.io.BufferedReader;
import java.io.FileReader;

import data_structures.ArrayList;
import interfaces.List;
import interfaces.Map;

/**
 * 
 * Constructor that holds the id, customerName, requestedParts and the fulfilled boolean.
 * Getters and Setters for the id, customerName, requestedParts and the fulfilled boolean.
 *
 */

public class Order {
	private int id;
	private String customerName;
	public  Map<Integer, Integer> requestedParts;
	private boolean fulfilled;

	public Order(int id, String customerName, Map<Integer, Integer> requestedParts, boolean fulfilled) {
		this.id = id;
		this.customerName = customerName;
		this.requestedParts = requestedParts;
		this.fulfilled = fulfilled;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public boolean isFulfilled() {
		return fulfilled;
	}
	public void setFulfilled(boolean fulfilled) {
		this.fulfilled = fulfilled ;
	}
	public Map<Integer, Integer> getRequestedParts() {
		return requestedParts;
	}
	public void setRequestedParts(Map<Integer, Integer> requestedParts) {
		this.requestedParts = requestedParts;

	}
	public String getCustomerName() {
		return  customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;

	}
	/**
	 * Returns the order's information in the following format: {id} {customer name} {number of parts requested} {isFulfilled}
	 */
	@Override
	public String toString() {
		return String.format("%d %s %d %s", this.getId(), this.getCustomerName(), this.getRequestedParts().size(), (this.isFulfilled())? "FULFILLED": "PENDING");
	}



}
