CREATE TABLE IF NOT EXISTS app_bills (
    id INT NOT NULL AUTO_INCREMENT,
    is_open BOOLEAN NOT NULL DEFAULT 1,
    date timestamp DEFAULT NOW(),
    table_nr VARCHAR(10),

    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS app_bill_has_product(
    bill_id INT NOT NULL,
    product_id INT NOT NULL,

    PRIMARY KEY (bill_id, product_id),
    FOREIGN KEY (bill_id) REFERENCES app_bills(id)
);

CREATE TABLE IF NOT EXISTS app_waiters (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50),

    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS app_bill_has_waiter (
    bill_id INT NOT NULL,
    waiter_id INT NOT NULL,

    PRIMARY KEY (bill_id, waiter_id),
    FOREIGN KEY (bill_id) REFERENCES app_bills(id),
    FOREIGN KEY (waiter_id) REFERENCES app_waiters(id)

);
