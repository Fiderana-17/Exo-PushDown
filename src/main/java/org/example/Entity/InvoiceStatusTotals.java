package org.example.Entity;

import java.math.BigDecimal;

public class InvoiceStatusTotals {
    private BigDecimal totalPaid;
    private BigDecimal totalConfirmed;
    private BigDecimal totalDraft;

    public InvoiceStatusTotals(BigDecimal totalPaid, BigDecimal totalConfirmed, BigDecimal totalDraft) {
        this.totalPaid = totalPaid;
        this.totalConfirmed = totalConfirmed;
        this.totalDraft = totalDraft;
    }

    public BigDecimal getTotalPaid() { return totalPaid; }
    public BigDecimal getTotalConfirmed() { return totalConfirmed; }
    public BigDecimal getTotalDraft() { return totalDraft; }

    @Override
    public String toString() {
        return "total_paid = " + totalPaid + "\n" +
                "total_confirmed = " + totalConfirmed + "\n" +
                "total_draft = " + totalDraft;
    }
}