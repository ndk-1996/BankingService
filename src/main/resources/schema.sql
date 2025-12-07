CREATE TABLE accounts (
    account_id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    document_number VARCHAR NOT NULL
);

CREATE TABLE operation_types (
    operation_type_id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    description VARCHAR NOT NULL,
    operation_type VARCHAR NOT NULL
);

CREATE TABLE transactions (
    transaction_id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    account_id BIGINT NOT NULL,
    operation_type_id BIGINT NOT NULL,
    amount NUMERIC NOT NULL,
    balance NUMERIC NOT NULL,
    event_date TIMESTAMP NOT NULL,

    CONSTRAINT fk_account
        FOREIGN KEY(account_id)
        REFERENCES accounts(account_id),

    CONSTRAINT fk_operation_type
        FOREIGN KEY(operation_type_id)
        REFERENCES operation_types(operation_type_id)
);
