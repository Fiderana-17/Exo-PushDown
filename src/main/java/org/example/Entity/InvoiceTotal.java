package org.example.Entity;

import java.math.BigDecimal;

public class InvoiceTotal {
    private int id;
    private String customerName;
    private String status;
    private BigDecimal total;

    public InvoiceTotal(int id, String customerName, String status, BigDecimal total) {
        this.id = id;
        this.customerName = customerName;
        this.status = status;
        this.total = total;
    }

    public int getId() { return id; }
    public String getCustomerName() { return customerName; }
    public String getStatus() { return status; }
    public BigDecimal getTotal() { return total; }

    @Override
    public String toString() {
        return id + " | " + customerName + " | " + total;
    }
}