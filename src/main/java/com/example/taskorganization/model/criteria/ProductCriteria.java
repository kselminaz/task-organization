package com.example.taskorganization.model.criteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCriteria {

    private String productName;
    private BigDecimal priceFrom;
    private BigDecimal priceTo;
    private Double rating;

}
