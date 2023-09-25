package com.dvc.points.resource;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.dvc.points.dto.ReservationPoints;
import com.dvc.points.exception.CustomException;
import com.dvc.points.exception.ErrorObject;
import com.dvc.points.manager.PointsManager;

@Component
public class PointsResourceImpl implements PointsResource {

	@Autowired 
	PointsManager manager;
	
	@Override
	public ResponseEntity<ReservationPoints> createReservationPoints(ReservationPoints reservationPoints) {

		ReservationPoints reservation = manager.createReservation(reservationPoints);

		// Check if the reservation was successfully created
		if (reservation != null) {
			return new ResponseEntity<>(reservation, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public ResponseEntity<String> bookResort(ReservationPoints transaction) {
		
		manager.bookResort(transaction);
		return ResponseEntity.ok("Resort Booked");
	}
	
	@Override
	public ResponseEntity<ErrorObject> handleException(CustomException ex) {

		ErrorObject eObject = new ErrorObject(HttpStatus.NOT_FOUND.value(), ex.getMessage(),
				System.currentTimeMillis());

		return new ResponseEntity<>(eObject, HttpStatus.NOT_FOUND);
	}

}
