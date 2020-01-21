package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ObjectPosition implements Serializable {
	private String object; // will be a partition key
	private BigDecimal latitude;
	private BigDecimal longitude;
	private long timestamp;
}
