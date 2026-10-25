package com.bank.tanner.budget.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Entity
@Table(name = "Gauge")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Gauge {
    @Id
    private String gaugeName;

    private Double budgetAmount;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> categoryList;
}
