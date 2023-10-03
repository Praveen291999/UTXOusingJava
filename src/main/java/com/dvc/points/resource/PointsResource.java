package com.dvc.points.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dvc.points.dto.PointsSummary;
import com.dvc.points.dto.ReservationPoints;
import com.dvc.points.exception.CustomException;
import com.dvc.points.exception.ErrorObject;

@RestController
public interface PointsResource {
	
	@PostMapping("/create")
	public ResponseEntity<ReservationPoints> createReservationPoints(@RequestBody ReservationPoints reservationPoints );
	

    @PostMapping("/book/resort")
    public ResponseEntity<String> bookResort(@RequestBody ReservationPoints transaction);
    
    @GetMapping("/unSpentPoints Details/{membershipId}/{contractNumber}")
	public PointsSummary sumOfUnspentPoints(@PathVariable int membershipId ,int contractNumber, @RequestParam(required=false) String pointsType) ;
	
    //Exception Handling :Display only error message in response.
  	@ExceptionHandler
  	public ResponseEntity<ErrorObject> handleException(CustomException ex);

}
