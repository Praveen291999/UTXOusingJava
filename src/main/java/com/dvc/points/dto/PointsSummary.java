package com.dvc.points.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PointsSummary {
	private int membershipId;
	private Integer sumOfUnSpentPoints;
	private Integer individualBucketPoint;
	
	
}