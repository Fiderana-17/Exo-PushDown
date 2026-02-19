CREATE TYPE invoice_status AS ENUM ('DRAFT', 'CONFIRMED', 'PAID');

CREATE TABLE  invoice (
    id SERIAL PRIMARY KEY,
    customer_name VARCHAR NOT NULL ,
    status invoice_status
);

CREATE TABLE invoice_line (
                              id SERIAL PRIMARY KEY,
                              invoice_id INT NOT NULL REFERENCES invoice(id),
                              label VARCHAR NOT NULL,
                              quantity INT NOT NULL,
                              unit_price NUMERIC(10,2) NOT NULL
);

CREATE TABLE tax_config (
                                          id SERIAL PRIMARY KEY,
                                          label VARCHAR NOT NULL,
                                          rate NUMERIC(5,2) NOT NULL
);