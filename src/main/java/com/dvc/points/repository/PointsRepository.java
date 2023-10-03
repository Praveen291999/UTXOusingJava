package com.dvc.points.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.dvc.points.dto.ReservationPoints;
import com.dvc.points.exception.CustomException;

@Repository
public class PointsRepository {

	private static final String CREATE_RESERVATION_POINTS = "Insert into reservationpoints(membershipId,contractNumber,pointsType,points,ResortName,"
			+ "utxoStatus) VALUES (:membershipId,:contractNumber,:pointsType,:points,:resortName,:utxoStatus)";

	private static final String BOOK_RESERVATION = "Insert into reservationpoints(membershipId,contractNumber,pointsType,points,resortName,"
			+ "utxoStatus) VALUES (:membershipId,:contractNumber,:pointsType,:points,:resortName,:utxoStatus)";

	private static final String UPDATE_TRANS = "UPDATE reservationpoints SET utxoStatus=:utxoStatus WHERE transactionid=:transactionId";

	private static final String CREATE_EXCEPTION="Failed to create reservation points";
	private static final String SAVE_EXCEPTION="Failed to save reservation points";
	private static final String UPDATE_EXCEPTION="Failed update create reservation points";
	
	private static final String SUM_UNSPENT_QUERY="select * from reservationPoints where membershipId= :membershipId AND contractNumber=:contractNumber AND utxoStatus = 'unlocked' ";	
	
	private static final String UNSPENT_QUERY = "select * from reservationPoints where membershipId= :membershipId AND contractNumber=:contractNumber AND utxoStatus = 'unlocked'"
			+ " AND pointsType=:pointsType";
	
	@Autowired
	NamedParameterJdbcTemplate namedjdbcTemplate;

	@Autowired
	JdbcTemplate jdbcTemplate;

	public ReservationPoints createReservationPoints(ReservationPoints reservationPoints) {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();

			params.addValue("membershipId", reservationPoints.getMembershipId());
			params.addValue("contractNumber", reservationPoints.getContractNumber());
			params.addValue("pointsType", reservationPoints.getPointsType());
			params.addValue("points", reservationPoints.getPoints());
			params.addValue("resortName", reservationPoints.getResortName());
			params.addValue("utxoStatus", reservationPoints.getUtxoStatus());

			namedjdbcTemplate.update(CREATE_RESERVATION_POINTS, params);

			return reservationPoints;
		} catch (CustomException ex) {

			throw new CustomException(CREATE_EXCEPTION);
		}
	}

	public ReservationPoints findLatestTransactionByContractIdAndStatus(int contractNumber, String unlocked ,String pointsType) {
		String sql = "SELECT * FROM reservationpoints WHERE contractNumber = ? AND utxoStatus = ? AND pointsType=? ";

		return jdbcTemplate.queryForObject(sql, new RowMapper<ReservationPoints>() {

			@Override
			public ReservationPoints mapRow(ResultSet rs, int rowNum) throws SQLException {
				ReservationPoints transaction = new ReservationPoints();
				transaction.setTransactionId(rs.getInt("transactionid"));
				transaction.setMembershipId(rs.getInt("membershipid"));
				transaction.setContractNumber(rs.getInt("contractNumber"));
				transaction.setPointsType(rs.getString("pointsType"));
				transaction.setPoints(rs.getInt("points"));
				transaction.setResortName(rs.getString("resortName"));
				transaction.setUtxoStatus(rs.getString("utxoStatus"));
				return transaction;
			}

		}, contractNumber, unlocked,pointsType);
	}

	public void saveTransaction(ReservationPoints reservationPoints) {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();

			params.addValue("membershipId", reservationPoints.getMembershipId());
			params.addValue("contractNumber", reservationPoints.getContractNumber());
			params.addValue("pointsType", reservationPoints.getPointsType());
			params.addValue("points", reservationPoints.getPoints());
			params.addValue("resortName", reservationPoints.getResortName());
			params.addValue("utxoStatus", reservationPoints.getUtxoStatus());

			namedjdbcTemplate.update(BOOK_RESERVATION, params);
		} catch (CustomException |DataAccessException a) {

			throw new CustomException(a.getMessage());
		}

	}

	public void updateTransaction(int transactionId, String locked) {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("transactionId", transactionId);
			params.addValue("utxoStatus", locked);

			namedjdbcTemplate.update(UPDATE_TRANS, params);
		} catch (CustomException |DataAccessException e) {

			throw new CustomException(e.getMessage());
		}

	}
	public List<ReservationPoints> getSumofUnspent(int membershipId,int contractNumber, String unlocked) {
		
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("membershipId", membershipId);
		params.addValue("contractNumber", contractNumber);
		return namedjdbcTemplate.query(SUM_UNSPENT_QUERY,params,new BeanPropertyRowMapper<>(ReservationPoints.class) );
	}

	public ReservationPoints getUnspent(int membershipId,int contractNumber, String unlocked, String pointsType) {
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("membershipId", membershipId);
		params.addValue("contractNumber", contractNumber);
		params.addValue("pointsType", pointsType);
		return namedjdbcTemplate.queryForObject(UNSPENT_QUERY,params,new BeanPropertyRowMapper<>(ReservationPoints.class));
	
	
	}

}
