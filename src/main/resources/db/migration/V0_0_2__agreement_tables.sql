-- =====================================================================
-- Tables issues du fichier AGREEMENT (UNBNK_AGREEMENT_DG_*_DT_*.json)
-- PostgreSQL 13+ (gen_random_uuid() est natif)
-- A executer dans le schema cible (loan, loc ou letter), par exemple :
--   SET search_path TO loan;
-- =====================================================================

-- ---------------------------------------------------------------------
-- Fonction commune : met a jour sys_upd_ts a chaque UPDATE
-- (indispensable pour le JDBC Source Connector en mode "timestamp")

-- ---------------------------------------------------------------------
-- tbl_agreement : informations de base de l'agreement
-- ---------------------------------------------------------------------

create schema if not exists commercialloan;

CREATE TABLE tbl_agreement
(
    agreement_id            UUID           NOT NULL,
    agreement_no            VARCHAR(20)    NOT NULL,               -- agmtId (cle d'affaires)
    credit_facility_no      VARCHAR(20)    NOT NULL,               -- creditFacilityId (vers DEAL)
    agreement_type          VARCHAR(20)    NOT NULL,               -- agmtTypeCd
    product_cd              VARCHAR(30)    NOT NULL,               -- productCd
    status_cd_src           VARCHAR(20)    NOT NULL,               -- statusCdSrc
    status_cd               VARCHAR(20)    NOT NULL,               -- statusCd
    unbnk_letter_issuer_ind BOOLEAN        NOT NULL,               -- unbnkLetterIssuerInd
    original_amt            NUMERIC(18, 2) NOT NULL,               -- originalAmt
    effctv_dt               DATE           NOT NULL,               -- effctvDt
    expiry_dt               DATE           NOT NULL,               -- expiryDt
    maturity_dt             DATE           NOT NULL,               -- maturityDt
    currency                CHAR(3)        NOT NULL,               -- currency
    source_system           VARCHAR(30)    NOT NULL,               -- sourceSystem
    deleted_ind             BOOLEAN        NOT NULL DEFAULT FALSE, -- suppression logique (op = D)
    -- audit
    sys_ins_ts              TIMESTAMPTZ    NOT NULL DEFAULT now(),
    sys_upd_ts              TIMESTAMPTZ    NULL,
    sys_ins_process         VARCHAR(100)   NOT NULL,
    sys_upd_process         VARCHAR(100)   NULL,
    CONSTRAINT pk_agreement PRIMARY KEY (agreement_id),
    CONSTRAINT uk_agreement_agmt_id UNIQUE (agreement_no)
);

CREATE INDEX ix_agreement_facility ON tbl_agreement (credit_facility_no);
CREATE INDEX ix_agreement_sys_upd_ts ON tbl_agreement (sys_upd_ts);


-- ---------------------------------------------------------------------
-- tbl_interest_rate : objet creditInterest (historique par date d'effet)
-- ---------------------------------------------------------------------
CREATE TABLE tbl_interest_rate
(
    interest_rate_id   UUID          NOT NULL,
    agreement_id       UUID          NOT NULL,
    interest_rate_type VARCHAR(10)   NOT NULL, -- FIXED si interestRateFixeInd = true, sinon VARIABLE
    interest_rate      NUMERIC(9, 6) NOT NULL, -- interestRate (AJOUTE : seul taux connu pour un taux fixe)
    base_rate          NUMERIC(9, 6),          -- baseRate (nul pour un taux fixe)
    spread_rate        NUMERIC(9, 6),          -- spreadRate (nul pour un taux fixe)
    effective_dt       DATE          NOT NULL, -- creditInterest.effctvDt
    -- audit
    sys_ins_ts         TIMESTAMPTZ   NOT NULL DEFAULT now(),
    sys_upd_ts         TIMESTAMPTZ   NULL,
    sys_ins_process    VARCHAR(100)  NOT NULL,
    sys_upd_process    VARCHAR(100)  NULL,
    CONSTRAINT pk_interest_rate PRIMARY KEY (interest_rate_id),
    CONSTRAINT fk_interest_rate_agreement FOREIGN KEY (agreement_id) REFERENCES tbl_agreement (agreement_id),
    CONSTRAINT uk_interest_rate UNIQUE (agreement_id, interest_rate_type)
);

CREATE INDEX ix_interest_rate_sys_upd_ts ON tbl_interest_rate (sys_upd_ts);

-- ---------------------------------------------------------------------
-- tbl_balance : objet accrualInterest (une ligne par type et par date)
--   balance_type = ACCRUED_INT_DAILY      -> accrualInterestDailyAmt
--   balance_type = ACCRUED_INT_CUMULATED  -> accrualInterestCumulatedAmt
--   Les interets courus sont dus a la banque : montant en debit_amnt.
-- ---------------------------------------------------------------------
CREATE TABLE tbl_balance
(
    balance_id      UUID           NOT NULL,
    agreement_id    UUID           NOT NULL,
    balance_type    VARCHAR(30)    NOT NULL,
    credit_amnt     NUMERIC(18, 2) NOT NULL DEFAULT 0,
    debit_amnt      NUMERIC(18, 2) NOT NULL DEFAULT 0,
    balance_date    DATE           NOT NULL, -- businessDt
    -- audit
    sys_ins_ts      TIMESTAMPTZ    NOT NULL DEFAULT now(),
    sys_upd_ts      TIMESTAMPTZ    NULL,
    sys_ins_process VARCHAR(100)   NOT NULL,
    sys_upd_process VARCHAR(100)   NULL,
    CONSTRAINT pk_balance PRIMARY KEY (balance_id),
    CONSTRAINT fk_balance_agreement FOREIGN KEY (agreement_id) REFERENCES tbl_agreement (agreement_id),
    CONSTRAINT uk_balance UNIQUE (agreement_id, balance_type, balance_date)
);

CREATE INDEX ix_balance_sys_upd_ts ON tbl_balance (sys_upd_ts);


-- ---------------------------------------------------------------------
-- tbl_party : objet party
-- ---------------------------------------------------------------------
CREATE TABLE tbl_party
(
    party_id        UUID         NOT NULL,
    agreement_id    UUID         NOT NULL,
    unbnk_id        VARCHAR(64),           -- party.unbnkId (chaine vide -> NULL)
    member_id_no    VARCHAR(20)  NOT NULL, -- party.memberIdNo
    -- audit
    sys_ins_ts      TIMESTAMPTZ  NOT NULL DEFAULT now(),
    sys_upd_ts      TIMESTAMPTZ  NULL,
    sys_ins_process VARCHAR(100) NOT NULL,
    sys_upd_process VARCHAR(100) NULL,
    CONSTRAINT pk_party PRIMARY KEY (party_id),
    CONSTRAINT fk_party_agreement FOREIGN KEY (agreement_id) REFERENCES tbl_agreement (agreement_id),
    CONSTRAINT uk_party UNIQUE (agreement_id, member_id_no)
);



-- ---------------------------------------------------------------------
-- tbl_payment_schedule : objet paymentSchedule
-- ---------------------------------------------------------------------
CREATE TABLE tbl_payment_schedule
(
    payment_schedule_id UUID           NOT NULL,
    agreement_id        UUID           NOT NULL,
    payment_date        DATE           NOT NULL,
    payment_amnt        NUMERIC(18, 2) NOT NULL DEFAULT 0,
    schedule_date       DATE           NOT NULL,
    -- audit
    sys_ins_ts          TIMESTAMPTZ    NOT NULL DEFAULT now(),
    sys_upd_ts          TIMESTAMPTZ    NULL,
    sys_ins_process     VARCHAR(100)   NOT NULL,
    sys_upd_process     VARCHAR(100)   NULL,
    CONSTRAINT pk_party PRIMARY KEY (payment_schedule_id),
    CONSTRAINT fk_party_agreement FOREIGN KEY (agreement_id) REFERENCES tbl_agreement (agreement_id)
);
CREATE INDEX ix_party_sys_upd_ts ON tbl_party (sys_upd_ts);
