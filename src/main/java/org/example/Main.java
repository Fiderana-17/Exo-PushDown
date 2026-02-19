package org.example;

import org.example.Config.DBConnection;
import org.example.Entity.InvoiceStatusTotals;
import org.example.Entity.InvoiceTaxSummary;
import org.example.Entity.InvoiceTotal;
import org.example.Service.DataRetriever;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        DBConnection dbConnection = new DBConnection();

        try (Connection connection = dbConnection.getConnection()) {

            DataRetriever dataRetriever = new DataRetriever();

            // Q1 : Totaux par facture
            System.out.println("---- Q1 : Totaux par facture ----");
            List<InvoiceTotal> totals = dataRetriever.findInvoiceTotals(connection);
            totals.forEach(System.out::println);
            System.out.println();

            // Q2 : Factures confirmées et payées
            System.out.println("---- Q2 : Factures confirmées et payées ----");
            List<InvoiceTotal> confirmedAndPaid = dataRetriever.findConfirmedAndPaidInvoiceTotals(connection);
            confirmedAndPaid.forEach(invoice ->
                    System.out.println(invoice.getId() + " | " + invoice.getCustomerName() + " | " +
                            invoice.getStatus() + " | " + invoice.getTotal())
            );
            System.out.println();


        } catch (Exception e) {
            System.err.println("Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }
}