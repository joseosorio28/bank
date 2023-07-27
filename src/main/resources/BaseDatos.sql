CREATE TABLE person
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    gender VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    identification_number VARCHAR(255) NOT NULL UNIQUE,
    address VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NOT NULL
);

CREATE TABLE client
(
    client_id INT NOT NULL,
    password VARCHAR(255) NOT NULL,
    state BOOLEAN,
    FOREIGN KEY (client_id) REFERENCES person (id),
    PRIMARY KEY (client_id)
);

CREATE TABLE account
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    number VARCHAR(255) NOT NULL UNIQUE,
    type VARCHAR(255),
    start_balance DOUBLE PRECISION NOT NULL,
    state BOOLEAN,
    client_id INT NOT NULL,
    FOREIGN KEY (client_id) REFERENCES person (id)
);

CREATE TABLE "transaction"
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    "date" DATE,
    type VARCHAR(255),
    amount DOUBLE PRECISION NOT NULL,
    balance DOUBLE PRECISION NOT NULL,
    account_id INT NOT NULL,
    FOREIGN KEY (account_id) REFERENCES account (id)
);