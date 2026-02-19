package org.example.Entity;

import java.math.BigDecimal;

public class InvoiceTaxSummary {
    private int id;
    private BigDecimal totalHt;
    private BigDecimal totalTva;
    private BigDecimal totalTtc;

    public InvoiceTaxSummary(int id, BigDecimal totalHt, BigDecimal totalTva, BigDecimal totalTtc) {
        this.id = id;
        this.totalHt = totalHt;
        this.totalTva = totalTva;
        this.totalTtc = totalTtc;
    }

    public int getId() { return id; }
    public BigDecimal getTotalHt() { return totalHt; }
    public BigDecimal getTotalTva() { return totalTva; }
    public BigDecimal getTotalTtc() { return totalTtc; }

    @Override
    public String toString() {
        return id + " | HT " + totalHt + " | TVA " + totalTva + " | TTC " + totalTtc;
    }
}