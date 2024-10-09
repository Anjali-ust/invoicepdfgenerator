package com.ust.invoicepdfgenerator.repository;

import com.ust.invoicepdfgenerator.model.Invoice;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InvoiceRepository extends MongoRepository<Invoice,String> {
    Invoice findByInvoiceNumber(String invoiceNumber);
}
