package com.dvc.points.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.dvc.points.dto.ReservationPoints;
import com.dvc.points.exception.CustomException;
import com.dvc.points.repository.PointsRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PointsManager {

	@Autowired
	PointsRepository repo;

	public static final String UNLOCKED = "unlocked";
	public static final String LOCKED = "locked";

	public ReservationPoints createReservation(ReservationPoints reservationPoints) {

		return repo.createReservationPoints(reservationPoints);
	}

	public ResponseEntity<String> bookResort(ReservationPoints transaction) {

		ReservationPoints latestTransaction = repo
				.findLatestTransactionByContractIdAndStatus(transaction.getContractNumber(), UNLOCKED);

		if (latestTransaction.getPoints() <= transaction.getPoints()) {
			throw new CustomException("Insufficient Points to Book");

		}
		try {
			if (latestTransaction != null && latestTransaction.getUtxoStatus().equals(UNLOCKED)
					&& latestTransaction.getPoints() >= transaction.getPoints()) {
				// Book the resort and insert 2 records into the database

				int unspent = latestTransaction.getPoints() - transaction.getPoints();

				ReservationPoints newTransaction1 = new ReservationPoints();
				newTransaction1.setMembershipId(transaction.getMembershipId());
				newTransaction1.setContractNumber(transaction.getContractNumber());
				newTransaction1.setAllocatedPoints(latestTransaction.getAllocatedPoints());
				newTransaction1.setPoints(transaction.getPoints());
				newTransaction1.setResortName(transaction.getResortName());
				newTransaction1.setUtxoStatus("locked");

				ReservationPoints newTransaction2 = new ReservationPoints();
				newTransaction2.setMembershipId(transaction.getMembershipId());
				newTransaction2.setContractNumber(transaction.getContractNumber());
				newTransaction2.setAllocatedPoints(latestTransaction.getAllocatedPoints());
				newTransaction2.setPoints(unspent);
				newTransaction2.setResortName(transaction.getResortName());
				newTransaction2.setUtxoStatus("unlocked");
				
				log.info("Transaction1 {}",newTransaction1);
				log.info("Transaction1 {}",newTransaction2);

				// Insert the 2 records
				repo.saveTransaction(newTransaction1);
				repo.saveTransaction(newTransaction2);

				// Update the original transaction to locked state
				repo.updateTransaction(latestTransaction.getTransactionId(), LOCKED);

				return ResponseEntity.ok("Resort Booked");
			} else {
				throw new CustomException("Cannot book resort");
			}
		} catch (Exception ex) {

			throw new CustomException("Error while booking resort");
		}

	}

}
