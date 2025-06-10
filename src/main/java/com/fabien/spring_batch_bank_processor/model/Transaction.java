package com.fabien.spring_batch_bank_processor.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transaction {

    @Id
    private Long id;
    private LocalDate date;
    private String client;
    private Double amount;
    private String category;

}
