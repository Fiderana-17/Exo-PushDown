CREATE DATABASE pushdown;

CREATE USER pushdown_db_manager WITH PASSWORD '123456';

GRANT CONNECT ON DATABASE pushdown TO pushdown_db_manager;

\c pushdown

GRANT CREATE ON SCHEMA public TO pushdown_db_manager;


