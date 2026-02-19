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
}