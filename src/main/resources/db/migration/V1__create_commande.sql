CREATE TABLE commande (
    id       BIGSERIAL PRIMARY KEY,
    client   VARCHAR(255)  NOT NULL,
    montant  NUMERIC(10,2) NOT NULL CHECK (montant > 0),
    statut   VARCHAR(20)   NOT NULL
);
