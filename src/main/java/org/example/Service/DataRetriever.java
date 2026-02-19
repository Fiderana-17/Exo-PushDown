package org.example.Service;

import org.example.Entity.InvoiceStatusTotals;
import org.example.Entity.InvoiceTaxSummary;
import org.example.Entity.InvoiceTotal;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

    // Q1 - Total par facture
    public List<InvoiceTotal> findInvoiceTotals(Connection connection) throws SQLException {
        String sql = """
            SELECT i.id,
                   i.customer_name,
                   i.status::text,
                   COALESCE(SUM(il.quantity * il.unit_price), 0) AS total
            FROM invoice i
            LEFT JOIN invoice_line il ON il.invoice_id = i.id
            GROUP BY i.id, i.customer_name, i.status
            ORDER BY i.id
        """;

        List<InvoiceTotal> results = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Ne pas inclure les factures sans lignes si total = 0
                BigDecimal total = rs.getBigDecimal("total");
                if (total.compareTo(BigDecimal.ZERO) > 0) {
                    InvoiceTotal invoiceTotal = new InvoiceTotal(
                            rs.getInt("id"),
                            rs.getString("customer_name"),
                            rs.getString("status"),
                            total
                    );
                    results.add(invoiceTotal);
                }
            }
        }

        return results;
    }

    // Q2 - Total des factures confirmées et payées
    public List<InvoiceTotal> findConfirmedAndPaidInvoiceTotals(Connection connection) throws SQLException {
        String sql = """
            SELECT i.id,
                   i.customer_name,
                   i.status::text,
                   COALESCE(SUM(il.quantity * il.unit_price), 0) AS total
            FROM invoice i
            LEFT JOIN invoice_line il ON il.invoice_id = i.id
            WHERE i.status IN ('CONFIRMED', 'PAID')
            GROUP BY i.id, i.customer_name, i.status
            HAVING COALESCE(SUM(il.quantity * il.unit_price), 0) > 0
            ORDER BY i.id
        """;

        List<InvoiceTotal> results = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                InvoiceTotal invoiceTotal = new InvoiceTotal(
                        rs.getInt("id"),
                        rs.getString("customer_name"),
                        rs.getString("status"),
                        rs.getBigDecimal("total")
                );
                results.add(invoiceTotal);
            }
        }

        return results;
    }

    // Q3 - Totaux cumulés par statut
    public InvoiceStatusTotals computeStatusTotals(Connection connection) throws SQLException {
        String sql = """
            SELECT 
                COALESCE(SUM(CASE WHEN i.status = 'PAID' THEN il.quantity * il.unit_price ELSE 0 END), 0) AS total_paid,
                COALESCE(SUM(CASE WHEN i.status = 'CONFIRMED' THEN il.quantity * il.unit_price ELSE 0 END), 0) AS total_confirmed,
                COALESCE(SUM(CASE WHEN i.status = 'DRAFT' THEN il.quantity * il.unit_price ELSE 0 END), 0) AS total_draft
            FROM invoice i
            LEFT JOIN invoice_line il ON il.invoice_id = i.id
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return new InvoiceStatusTotals(
                        rs.getBigDecimal("total_paid"),
                        rs.getBigDecimal("total_confirmed"),
                        rs.getBigDecimal("total_draft")
                );
            }
        }

        return new InvoiceStatusTotals(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
    }

    // Q4 - Chiffre d'affaires pondéré
    public Double computeWeightedTurnover(Connection connection) throws SQLException {
        String sql = """
            SELECT COALESCE(SUM(
                il.quantity * il.unit_price * 
                CASE 
                    WHEN i.status = 'PAID' THEN 1.0
                    WHEN i.status = 'CONFIRMED' THEN 0.5
                    WHEN i.status = 'DRAFT' THEN 0.0
                    ELSE 0.0
                END
            ), 0) AS weighted_turnover
            FROM invoice i
            LEFT JOIN invoice_line il ON il.invoice_id = i.id
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble("weighted_turnover");
            }
        }

        return 0.0;
    }

    // Q5-A) Totaux HT, TVA et TTC par facture
    public List<InvoiceTaxSummary> findInvoiceTaxSummaries(Connection connection) throws SQLException {
        String sql = """
        SELECT i.id,
               SUM(il.quantity * il.unit_price) AS total_ht,
               SUM(il.quantity * il.unit_price) * t.rate / 100 AS total_tva,
               SUM(il.quantity * il.unit_price) * (1 + t.rate / 100) AS total_ttc
        FROM invoice i
        JOIN invoice_line il ON il.invoice_id = i.id
        CROSS JOIN tax_config t
        GROUP BY i.id, t.rate
        ORDER BY i.id
    """;

        List<InvoiceTaxSummary> results = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                results.add(new InvoiceTaxSummary(
                        rs.getInt("id"),
                        rs.getBigDecimal("total_ht"),
                        rs.getBigDecimal("total_tva"),
                        rs.getBigDecimal("total_ttc")
                ));
            }
        }

        return results;
    }


    // Q5-B) Chiffre d'affaires TTC pondéré
    public BigDecimal computeWeightedTurnoverTtc(Connection connection) throws SQLException {
        String sql = """
        SELECT SUM(
            (il.quantity * il.unit_price) * (1 + t.rate / 100) *
            CASE
                WHEN i.status = 'PAID' THEN 1
                WHEN i.status = 'CONFIRMED' THEN 0.5
                ELSE 0
            END
        ) AS weighted_turnover_ttc
        FROM invoice i
        JOIN invoice_line il ON il.invoice_id = i.id
        CROSS JOIN tax_config t
    """;

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getBigDecimal("weighted_turnover_ttc");
            }
        }

        return BigDecimal.ZERO;
    }


}