package com.ust.invoicepdfgenerator.model;

import com.ust.invoicepdfgenerator.util.AmountToWordConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "invoice")
public class Invoice {
    private String invoiceNumber;
    private String companyDetails;
    private String clientDetails;
    private String poNumber;
    private String bankDetails;
    private List<Item> items;
    private Date date;
    private double tdsPercentage;
    private double totalAmount;

    public double findTotalAmount() {
        double total = 0.0;
        for (Item item : items) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public double findTotalAmountWithTds() {
        double total = findTotalAmount();
        return total - (total * tdsPercentage / 100);
    }
    public String convertToWords(double amount) {
        // Implementation to convert double amount to words
        return AmountToWordConverter.convert(amount);
    }
}
