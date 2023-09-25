package com.dvc.points.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dvc.points.dto.ReservationPoints;
import com.dvc.points.exception.CustomException;

@Repository
public class PointsRepository {

	private static final String CREATE_RESERVATION_POINTS = "Insert into reservationpoints(membershipId,contractNumber,points,AllocatedPoints,ResortName,"
			+ "utxoStatus) VALUES (:membershipId,:contractNumber,:points,:allocatedPoints,:resortName,:utxoStatus)";

	private static final String BOOK_RESERVATION = "Insert into reservationpoints(membershipId,contractNumber,allocatedPoints,points,resortName,"
			+ "utxoStatus) VALUES (:membershipId,:contractNumber,:allocatedPoints,:points,:resortName,:utxoStatus)";

	private static final String UPDATE_TRANS = "UPDATE reservationpoints SET utxoStatus=:utxoStatus WHERE transactionid=:transactionId";

	private static final String CREATE_EXCEPTION="Failed to create reservation points";
	private static final String SAVE_EXCEPTION="Failed to save reservation points";
	private static final String UPDATE_EXCEPTION="Failed update create reservation points";
	
	@Autowired
	NamedParameterJdbcTemplate namedjdbcTemplate;

	@Autowired
	JdbcTemplate jdbcTemplate;

	public ReservationPoints createReservationPoints(ReservationPoints reservationPoints) {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();

			params.addValue("membershipId", reservationPoints.getMembershipId());
			params.addValue("contractNumber", reservationPoints.getContractNumber());
			params.addValue("points", reservationPoints.getPoints());
			params.addValue("allocatedPoints", reservationPoints.getAllocatedPoints());
			params.addValue("resortName", reservationPoints.getResortName());
			params.addValue("utxoStatus", reservationPoints.getUtxoStatus());

			namedjdbcTemplate.update(CREATE_RESERVATION_POINTS, params);

			return reservationPoints;
		} catch (CustomException ex) {

			throw new CustomException(CREATE_EXCEPTION);
		}
	}

	public ReservationPoints findLatestTransactionByContractIdAndStatus(int contractNumber, String unlocked) {
		String sql = "SELECT * FROM reservationpoints WHERE contractNumber = ? AND utxoStatus = ? ORDER BY transactionid DESC LIMIT 1";

		return jdbcTemplate.queryForObject(sql, new RowMapper<ReservationPoints>() {

			@Override
			public ReservationPoints mapRow(ResultSet rs, int rowNum) throws SQLException {
				ReservationPoints transaction = new ReservationPoints();
				transaction.setTransactionId(rs.getInt("transactionid"));
				transaction.setMembershipId(rs.getInt("membershipid"));
				transaction.setContractNumber(rs.getInt("contractNumber"));
				transaction.setAllocatedPoints(rs.getInt("allocatedPoints"));
				transaction.setPoints(rs.getInt("points"));
				transaction.setResortName(rs.getString("resortName"));
				transaction.setUtxoStatus(rs.getString("utxoStatus"));
				return transaction;
			}

		}, contractNumber, unlocked);
	}

	public void saveTransaction(ReservationPoints reservationPoints) {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();

			params.addValue("membershipId", reservationPoints.getMembershipId());
			params.addValue("contractNumber", reservationPoints.getContractNumber());
			params.addValue("allocatedPoints", reservationPoints.getAllocatedPoints());
			params.addValue("points", reservationPoints.getPoints());
			params.addValue("resortName", reservationPoints.getResortName());
			params.addValue("utxoStatus", reservationPoints.getUtxoStatus());

			namedjdbcTemplate.update(BOOK_RESERVATION, params);
		} catch (CustomException ex) {

			throw new CustomException(SAVE_EXCEPTION);
		}

	}

	public void updateTransaction(int transactionId, String locked) {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("transactionId", transactionId);
			params.addValue("utxoStatus", locked);

			namedjdbcTemplate.update(UPDATE_TRANS, params);
		} catch (CustomException ex) {

			throw new CustomException(UPDATE_EXCEPTION);
		}

	}

}
