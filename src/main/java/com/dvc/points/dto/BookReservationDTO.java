package com.dvc.points.dto;

import java.io.Serializable;

import lombok.Data;


@Data
public class BookReservationDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int contractNumber;
	private int membershipId;
	private int points;
	private String resortName; 
	
	

}
