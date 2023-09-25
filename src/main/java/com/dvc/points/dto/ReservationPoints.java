package com.dvc.points.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationPoints implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int transactionId;
	private int membershipId;
	private int contractNumber;  
	private int points;
	private int allocatedPoints; 
	private String resortName; 
	private String utxoStatus;  

}
