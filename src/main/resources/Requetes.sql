SELECT
    i.id,
    i.customer_name,
    SUM(il.quantity * il.unit_price) AS total
FROM invoice i
         JOIN invoice_line il ON il.invoice_id = i.id
GROUP BY i.id, i.customer_name
ORDER BY i.id;


SELECT
    i.id,
    i.customer_name,
    i.status,
    SUM(il.quantity * il.unit_price) AS total
FROM invoice i
         JOIN invoice_line il ON il.invoice_id = i.id
WHERE i.status IN ('CONFIRMED', 'PAID')
GROUP BY i.id, i.customer_name, i.status
ORDER BY i.id;


SELECT
    SUM(CASE WHEN i.status = 'PAID'
                 THEN il.quantity * il.unit_price
             ELSE 0 END) AS total_paid,

    SUM(CASE WHEN i.status = 'CONFIRMED'
                 THEN il.quantity * il.unit_price
             ELSE 0 END) AS total_confirmed,

    SUM(CASE WHEN i.status = 'DRAFT'
                 THEN il.quantity * il.unit_price
             ELSE 0 END) AS total_draft

FROM invoice i
         JOIN invoice_line il ON il.invoice_id = i.id;


SELECT
    SUM(
            (il.quantity * il.unit_price) *
            CASE
                WHEN i.status = 'PAID' THEN 1
                WHEN i.status = 'CONFIRMED' THEN 0.5
                ELSE 0
                END
    ) AS weighted_turnover
FROM invoice i
         JOIN invoice_line il ON il.invoice_id = i.id;



SELECT * FROM tax_config;