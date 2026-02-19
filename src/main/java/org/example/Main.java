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

            // Q3 : Totaux cumulés par statut
            System.out.println("---- Q3 : Totaux cumulés par statut ----");
            InvoiceStatusTotals statusTotals = dataRetriever.computeStatusTotals(connection);
            System.out.println(statusTotals);
            System.out.println();

            // Q4 : Chiffre d'affaires pondéré
            System.out.println("---- Q4 : Chiffre d'affaires pondéré ----");
            Double weightedTurnover = dataRetriever.computeWeightedTurnover(connection);
            System.out.println(weightedTurnover);
            System.out.println();

            // Q5 : Totaux HT, TVA, TTC
            System.out.println("---- Q5-A : Totaux HT / TVA / TTC ----");
            List<InvoiceTaxSummary> taxSummaries =
                    dataRetriever.findInvoiceTaxSummaries(connection);

            taxSummaries.forEach(i ->
                    System.out.println(i.getId()
                            + " | HT " + i.getTotalHt()
                            + " | TVA " + i.getTotalTva()
                            + " | TTC " + i.getTotalTtc())
            );

            //Q5B
            System.out.println("\n---- Q5-B : Chiffre d'affaires pondéré TTC ----");
            BigDecimal weightedTtc =
                    dataRetriever.computeWeightedTurnoverTtc(connection);

            System.out.println("Weighted turnover TTC = " + weightedTtc);


        } catch (Exception e) {
            System.err.println("Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }
}