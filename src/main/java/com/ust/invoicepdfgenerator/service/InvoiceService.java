package com.ust.invoicepdfgenerator.service;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.ust.invoicepdfgenerator.model.Invoice;
import com.ust.invoicepdfgenerator.model.Item;
import com.ust.invoicepdfgenerator.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.List;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    public Invoice saveInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    public Invoice getInvoice(String invoiceNumber) {
        return invoiceRepository.findById(invoiceNumber).orElse(null);
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Invoice getInvoicePDF(String invoiceNumber) throws FileNotFoundException {
        Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber);//.orElse(null);
        String path="invoice.pdf";
        PdfWriter pdfWriter = new PdfWriter(path);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);
        float threecol = 98f;
        float dwidth = 198f;
        float twocol = 285f;
        float twocol150 = twocol+150f;
        float twocolWidth[] = {twocol150,twocol};
        float fullwidth[]={dwidth*3};

        float fiveColumnWidth[] = {threecol, threecol, threecol,threecol,threecol};
        Paragraph onesp = new Paragraph("\n");

        Table table = new Table(twocolWidth);
        table.addCell(new Cell().add(new Paragraph("Invoice")) // Use Paragraph here
                .setBorder(Border.NO_BORDER).setBold());
        Table nestedtable=new Table(new float[]{twocol/2,twocol/2});
        nestedtable.addCell(new Cell().add(new Paragraph("Invoice Number:"))
                .setBorder(Border.NO_BORDER).setBold());
        nestedtable.addCell(new Cell().add(new Paragraph(invoiceNumber))
                .setBorder(Border.NO_BORDER));
        nestedtable.addCell(new Cell().add(new Paragraph("Invoice date:"))
                .setBorder(Border.NO_BORDER).setBold());
        nestedtable.addCell(new Cell().add(new Paragraph(invoice.getDate().toString()))
                .setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(nestedtable).setBorder(Border.NO_BORDER));

        Border border = new SolidBorder(ColorConstants.GRAY,2f);

        Table divider = new Table(fullwidth);
        divider.setBorder(border);

        Table companyTable = new Table(twocolWidth);
        companyTable.addCell(new Cell().add(new Paragraph("Company Details")) // Use Paragraph here
                .setBorder(Border.NO_BORDER).setBold());
        companyTable.addCell(new Cell().add(new Paragraph(invoice.getCompanyDetails()))
                .setBorder(Border.NO_BORDER));

        Table clientTable=new Table(new float[]{twocol/2,twocol/2});
        clientTable.addCell(new Cell().add(new Paragraph("Client Details")) // Use Paragraph here
                .setBorder(Border.NO_BORDER).setBold());
        clientTable.addCell(new Cell().add(new Paragraph(invoice.getClientDetails()))
                .setBorder(Border.NO_BORDER));
        companyTable.addCell(new Cell().add(clientTable).setBorder(Border.NO_BORDER));

        Table bankTable=new Table(new float[]{twocol/2,twocol/2});
        bankTable.addCell(new Cell().add(new Paragraph("Bank Details")) // Use Paragraph here
                .setBorder(Border.NO_BORDER).setBold());
        bankTable.addCell(new Cell().add(new Paragraph(invoice.getBankDetails()))
                .setBorder(Border.NO_BORDER));

        //Product details
        Paragraph prodPara = new Paragraph("Product details");


        document.add(table);
        document.add(onesp);
        document.add(divider);
        //document.add(onesp);
        document.add(companyTable);
        document.add(onesp);
        document.add(divider);
        document.add(onesp);
        document.add(bankTable);
        document.add(onesp);
        document.add(divider);
        document.add(prodPara.setBold());

        Table fiveColumnTable1 = new Table(fiveColumnWidth);
        fiveColumnTable1.setBackgroundColor(ColorConstants.BLACK,0.7f);
        fiveColumnTable1.addCell(new Cell().add(new Paragraph("Date")) // Use Paragraph here
                .setBold().setFontColor(ColorConstants.WHITE));

        fiveColumnTable1.addCell(new Cell().add(new Paragraph("Item name")) // Use Paragraph here
                .setBold().setFontColor(ColorConstants.WHITE).setTextAlignment(TextAlignment.CENTER));

        fiveColumnTable1.addCell(new Cell().add(new Paragraph("Number of days")) // Use Paragraph here
                .setBold().setFontColor(ColorConstants.WHITE).setTextAlignment(TextAlignment.CENTER));
        fiveColumnTable1.addCell(new Cell().add(new Paragraph("Price per day")) // Use Paragraph here
                .setBold().setFontColor(ColorConstants.WHITE).setTextAlignment(TextAlignment.CENTER));

        fiveColumnTable1.addCell(new Cell().add(new Paragraph("Total price")) // Use Paragraph here
                .setBold().setFontColor(ColorConstants.WHITE).setTextAlignment(TextAlignment.CENTER));

        document.add(fiveColumnTable1);

        Table fiveColumnTable2 = new Table(fiveColumnWidth);
        List<Item> items = invoice.getItems();
        for(Item item:items){
            fiveColumnTable2.addCell(new Cell().add(new Paragraph(invoice.getDate().toString())) // Use Paragraph here
                    .setTextAlignment(TextAlignment.CENTER));

            fiveColumnTable2.addCell(new Cell().add(new Paragraph(item.getItemName())) // Use Paragraph here
                    .setTextAlignment(TextAlignment.CENTER));

            fiveColumnTable2.addCell(new Cell().add(new Paragraph(String.valueOf(item.getNumberOfDays())))
                    .setTextAlignment(TextAlignment.CENTER));

            fiveColumnTable2.addCell(new Cell().add(new Paragraph(String.valueOf(item.getPricePerDay())))
                    .setTextAlignment(TextAlignment.CENTER));

            fiveColumnTable2.addCell(new Cell().add(new Paragraph(String.valueOf(item.getTotalPrice())))
                    .setTextAlignment(TextAlignment.CENTER));
        }
        document.add(fiveColumnTable2.setMarginBottom(20f));
        float onetwo[] = {threecol+125f,threecol*2};
        Table totalTable1=new Table(onetwo);
        totalTable1.addCell(new Cell().add(new Paragraph(" "))
                .setBorder(Border.NO_BORDER));
        totalTable1.addCell(divider).setBorder(Border.NO_BORDER);
        //document.add(totalTable1);

        Table totalTable2=new Table(fiveColumnWidth);
//        totalTable2.addCell(new Cell().add(new Paragraph(" "))
//                .setBorder(Border.NO_BORDER).setMarginLeft(10f));
        totalTable2.addCell(new Cell().add(new Paragraph("TDS Percentage"))
                .setBold().setBorder(Border.NO_BORDER));
        totalTable2.addCell(new Cell().add(new Paragraph(String.valueOf(invoice.getTdsPercentage())))
                .setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
        Table totalTable3=new Table(fiveColumnWidth);
        totalTable3.addCell(new Cell().add(new Paragraph("TotalAmount"))
                .setBold().setBorder(Border.NO_BORDER));
        totalTable3.addCell(new Cell().add(new Paragraph(String.valueOf(invoice.findTotalAmountWithTds())))
                .setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));

        document.add(onesp);
        //document.add(totalTable1);
        document.add(totalTable2);
        document.add(totalTable3);

        Table amountTable = new Table(twocolWidth);
        amountTable.addCell(new Cell().add(new Paragraph("Amount In Words ")) // Use Paragraph here
                .setBorder(Border.NO_BORDER).setBold());
        amountTable.addCell(new Cell().add(new Paragraph(invoice.convertToWords(invoice.findTotalAmountWithTds())+" only"))
                .setBorder(Border.NO_BORDER));

        document.add(amountTable);

        Table table2 = new Table(twocolWidth);
        table2.addCell(new Cell().add(new Paragraph(" ")) // Use Paragraph here
                .setBorder(Border.NO_BORDER).setBold());
        Table signtable=new Table(new float[]{twocol/2,twocol/2});
        signtable.addCell(new Cell().add(new Paragraph("sign"))
                .setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(signtable).setBorder(Border.NO_BORDER));
        document.add(table2);
        document.close();
        return invoice;
    }
}
