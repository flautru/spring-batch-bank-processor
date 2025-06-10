package com.fabien.spring_batch_bank_processor.processor;

import com.fabien.spring_batch_bank_processor.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class TransactionProcessor implements ItemProcessor<Transaction, Transaction> {

    private static final Logger log = LoggerFactory.getLogger(TransactionProcessor.class);

    @Override
    public Transaction process(Transaction transaction) throws Exception {
        if(transaction.getAmount() == null || transaction.getAmount() <= 0){
            log.info("Transaction ignorée car montant invalide : {}", transaction);
            return null;
        }
        if(transaction.getCategory() == null || transaction.getCategory().isBlank()){
            if(transaction.getAmount()>100){
                transaction.setCategory("Loisirs");
            }
            else {
                transaction.setCategory("Courses");
            }
            log.info("Catégorie ajoutée {}", transaction.getCategory());
        }
        return transaction;
    }
}
