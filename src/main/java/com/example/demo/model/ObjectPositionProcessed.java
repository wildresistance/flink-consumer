package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ObjectPositionProcessed {
	private String object; // will be a partition key
	private BigDecimal latitude1;
	private BigDecimal longitude1;
	private BigDecimal latitude2;
	private BigDecimal longitude2;
	private long from;
	private long to;
	private double distance;
	private double speed;


}
