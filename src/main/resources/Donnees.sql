INSERT INTO invoice (customer_name, status) VALUES
                                                ('Alice', 'CONFIRMED'),
                                                ('Bob', 'PAID'),
                                                ('Charlie', 'DRAFT');
INSERT INTO invoice_line (invoice_id, label, quantity, unit_price) VALUES
                                                                       (1, 'Produit A', 2, 100),
                                                                       (1, 'Produit B', 1, 50),
                                                                       (2, 'Produit A', 5, 100),
                                                                       (2, 'Service C', 1, 200),
                                                                       (3, 'Produit B', 3, 50);