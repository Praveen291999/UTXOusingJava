package com.dvc.points.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservationPoints implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int transactionId;
	private int membershipId;
	private int contractNumber;
	private String pointsType;
	private int points;
	private String resortName; 
	private String utxoStatus;  

}
