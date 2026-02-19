CREATE DATABASE pushdown;

CREATE USER pushdown_db_manager WITH PASSWORD '123456';

GRANT CONNECT ON DATABASE pushdown TO pushdown_db_manager;

\c pushdown

GRANT CREATE ON SCHEMA public TO pushdown_db_manager;


GRANT SELECT ON invoice TO pushdown_db_manager;
GRANT SELECT ON invoice_line TO pushdown_db_manager;
GRANT SELECT ON tax_config TO pushdown_db_manager;