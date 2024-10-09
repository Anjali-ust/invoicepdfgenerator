//package com.ust.invoicepdfgenerator.controller;
//import com.itextpdf.kernel.font.PdfFont;
//import com.itextpdf.kernel.font.PdfFontFactory;
//import com.itextpdf.kernel.pdf.PdfDocument;
//import com.itextpdf.kernel.pdf.PdfWriter;
//import com.itextpdf.layout.element.Text;
//import com.itextpdf.text.Font;
//import com.ust.invoicepdfgenerator.model.Invoice;
//import com.ust.invoicepdfgenerator.service.InvoiceService;
//import com.itextpdf.io.source.ByteArrayOutputStream;
//
//import com.itextpdf.layout.element.Paragraph;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import com.itextpdf.text.pdf.PdfPTable;
//
//import com.itextpdf.text.*;
//
//import java.awt.*;
//import java.io.ByteArrayInputStream;
//import java.util.List;
//
//
//@RestController
//@RequestMapping("/invoice")
//public class InvoiceController {
//
//    @Autowired
//    private InvoiceService invoiceService;
//
//    @PostMapping("/save")
//    public ResponseEntity<Invoice> saveInvoice(@RequestBody Invoice invoice) {
//        Invoice savedInvoice = invoiceService.saveInvoice(invoice);
//        return new ResponseEntity<>(savedInvoice, HttpStatus.CREATED);
//    }
//
//    // GET: Retrieve all invoices
//    @GetMapping("/getallinvoices")
//    public ResponseEntity<List<Invoice>> getAllInvoices() {
//        List<Invoice> invoices = invoiceService.getAllInvoices();
//        return new ResponseEntity<>(invoices, HttpStatus.OK);
//    }
//
//    // GET: Retrieve an invoice by invoice number
//    @GetMapping("/getinvoicebynumber{invoiceNumber}")
//    public ResponseEntity<Invoice> getInvoice(@PathVariable String invoiceNumber) {
//        Invoice invoice = invoiceService.getInvoice(invoiceNumber);
//        if (invoice != null) {
//            return new ResponseEntity<>(invoice, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//    // POST: Generate and download invoice PDF by invoice number
//    @PostMapping("/generatepdf/{invoiceNumber}")
//    public ResponseEntity<byte[]> generateInvoicePDF(@PathVariable String invoiceNumber) {
//        Invoice invoice = invoiceService.getInvoice(invoiceNumber);
//        if (invoice == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        ByteArrayInputStream bis = generatePDF(invoice);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Disposition", "inline; filename=invoice_" + invoiceNumber + ".pdf");
//
//        return ResponseEntity.ok()
//                .headers(headers)
//                .contentType(MediaType.APPLICATION_PDF)
//                .body(bis.readAllBytes());
//    }
//
//    private ByteArrayInputStream generatePDF(Invoice invoice) {
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(out));
//        Document document = new Document();
//
//
//        try {
////            PdfWriter.getInstance(document, out);
////            document.open();
//
//            PdfFont fontTitle = PdfFontFactory.createFont("Helvetica-Bold", "UTF-8", true);
//            PdfFont font = PdfFontFactory.createFont("Helvetica", "UTF-8", true);
//            // Add title and details
//            document.add((Element) new Paragraph(new Text("Invoice").setFont(fontTitle)));
//            document.add((Element) new Paragraph(new Text("Invoice Number: " + invoice.getInvoiceNumber()).setFont(font)));
//            document.add((Element) new Paragraph(new Text("Company Details: " + invoice.getCompanyDetails()).setFont(font)));
//            document.add((Element) new Paragraph(new Text("Client Details: " + invoice.getClientDetails()).setFont(font)));
//            document.add((Element) new Paragraph(new Text("PO Number: " + invoice.getPoNumber()).setFont(font)));
//            document.add((Element) new Paragraph(new Text("Bank Details: " + invoice.getBankDetails()).setFont(font)));
//            document.add((Element) new Paragraph(new Text("Date: " + invoice.getDate().toString()).setFont(font)));
//
//            // Table for items
//            PdfPTable table = new PdfPTable(5);
//            table.addCell("Item Name");
//            table.addCell("Number of Days");
//            table.addCell("Price per Day");
//            table.addCell("Total Price");
//            table.addCell("TDS Percentage");
//
//            invoice.getItems().forEach(item -> {
//                table.addCell(item.getItemName());
//                table.addCell(String.valueOf(item.getNumberOfDays()));
//                table.addCell(String.valueOf(item.getPricePerDay()));
//                table.addCell(String.valueOf(item.getTotalPrice()));
//                table.addCell(String.valueOf(invoice.getTdsPercentage()));
//            });
//
//            document.add(table);
//
//            // Total amount and amount in words
//            document.add((Element) new Paragraph(new Text("Total Amount: " + invoice.getTotalAmount()).setFont(font)));
//
//            //document.add(new Paragraph("Amount in Words: " + invoice.getAmountInWords(), font));
//
//            // Signature
//            //document.add(new Paragraph("Signature: " + invoice.getSignature(), font));
//
//            document.close();
//
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        }
//
//        return new ByteArrayInputStream(out.toByteArray());
//    }
//
//}
//

package com.ust.invoicepdfgenerator.controller;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.ust.invoicepdfgenerator.model.Invoice;
import com.ust.invoicepdfgenerator.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping("/save")
    public ResponseEntity<Invoice> saveInvoice(@RequestBody Invoice invoice) {
        Invoice savedInvoice = invoiceService.saveInvoice(invoice);
        return new ResponseEntity<>(savedInvoice, HttpStatus.CREATED);
    }

    // GET: Retrieve all invoices
    @GetMapping("/getallinvoices")
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        List<Invoice> invoices = invoiceService.getAllInvoices();
        return new ResponseEntity<>(invoices, HttpStatus.OK);
    }

    // GET: Retrieve an invoice by invoice number
    @GetMapping("/getinvoicebynumber/{invoiceNumber}")
    public ResponseEntity<Invoice> getInvoice(@PathVariable String invoiceNumber) {
        Invoice invoice = invoiceService.getInvoice(invoiceNumber);
        if (invoice != null) {
            return new ResponseEntity<>(invoice, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // POST: Generate and download invoice PDF by invoice number
    @GetMapping("/generatepdf/{invoiceNumber}")
    public ResponseEntity<Invoice> generateInvoicePDF(@PathVariable String invoiceNumber) throws FileNotFoundException {
        Invoice invoice = invoiceService.getInvoicePDF(invoiceNumber);
        if (invoice == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else{
            return new ResponseEntity<>(invoice, HttpStatus.OK);
        }

//                .headers(headers)
//                .contentType(MediaType.APPLICATION_PDF)
//                .body(bis.readAllBytes());
    }


}

