-- Table des deals (source : fichiers UNBNK_DEAL_*.json)
SET search_path TO commercialloan;

CREATE TABLE IF NOT EXISTS tbl_deal
(
    deal_id             uuid         NOT NULL,
    borrowing_agmt_id   VARCHAR(20)  NOT NULL,
    borrowing_agmt_name VARCHAR(255) NOT NULL,
    effctv_dt           DATE,
    classification_cd   VARCHAR(10),
    next_review_dt      DATE,
    deleteIndicator     boolean               default false,

    -- Champs d'audit
    sys_ins_ts          TIMESTAMPTZ  NOT NULL DEFAULT now(),
    sys_upd_ts          TIMESTAMPTZ  NULL,
    sys_ins_process     VARCHAR(100) NOT NULL,
    sys_upd_process     VARCHAR(100) NULL,
    CONSTRAINT pk_tbl_deal PRIMARY KEY (deal_id),
    CONSTRAINT uk_tbl_deal UNIQUE (borrowing_agmt_id)

);

-- Table des facilités de crédit (source : creditFacility[] dans UNBNK_DEAL_*.json)
-- Prérequis : tbl_deal
CREATE TABLE IF NOT EXISTS tbl_credit_facility
(
    credit_facility_id   uuid         not null,
    credit_facility_no   VARCHAR(20)  NOT NULL,
    borrowing_agmt_id    VARCHAR(20)  NOT NULL,
    credit_facility_name VARCHAR(255) NOT NULL,
    status_cd            VARCHAR(20),
    status_cd_src        VARCHAR(20),
    product_cd           VARCHAR(20),
    lender_role_cd       VARCHAR(20),
    effctv_dt            DATE,
    expiry_dt            DATE,
    maturity_dt          DATE,
    currency             CHAR(3),

    -- Champs d'audit
    sys_ins_ts           TIMESTAMPTZ  NOT NULL DEFAULT now(),
    sys_upd_ts           TIMESTAMPTZ  NULL,
    sys_ins_process      VARCHAR(100) NOT NULL,
    sys_upd_process      VARCHAR(100) NULL,

    CONSTRAINT pk_tbl_credit_facility PRIMARY KEY (credit_facility_id),
    CONSTRAINT fk_tbl_credit_facility_deal FOREIGN KEY (borrowing_agmt_id)
        REFERENCES tbl_deal (borrowing_agmt_id),
    CONSTRAINT uk_tbl_credit_facility UNIQUE (credit_facility_no)
);

CREATE INDEX IF NOT EXISTS idx_tbl_credit_facility_borrowing_agmt_id
    ON tbl_credit_facility (borrowing_agmt_id);

-- Table des parties (source : parties[] dans UNBNK_DEAL_*.json)
CREATE TABLE IF NOT EXISTS tbl_cf_party
(
    cf_party_id     uuid         NOT NULL,
    unbnk_id        VARCHAR(20)  NOT NULL,
    member_id_no    VARCHAR(20)  NOT NULL,
    deal_id         uuid         not null,

    sys_ins_ts      TIMESTAMPTZ  NOT NULL DEFAULT now(),
    sys_upd_ts      TIMESTAMPTZ  NULL,
    sys_ins_process VARCHAR(100) NOT NULL,
    sys_upd_process VARCHAR(100) NULL,

    CONSTRAINT pk_tbl_party PRIMARY KEY (cf_party_id),
    CONSTRAINT fk_tbl_credit_facility foreign key (deal_id) REFERENCES tbl_deal (deal_id),
    CONSTRAINT uk_tbl_CF_PARTY UNIQUE (member_id_no)
);

-- Table de relation facilité de crédit <-> agreement
-- (source : creditFacility[].agreements[] dans UNBNK_DEAL_*.json)
-- Prérequis : tbl_credit_facility
CREATE TABLE IF NOT EXISTS tblr_agreement_relation
(
    agreement_relation_id uuid NOT NULL,
    credit_facility_id uuid         NOT NULL,
    agmt_id            VARCHAR(20)  NOT NULL,

    -- Champs d'audit
    sys_ins_ts         TIMESTAMPTZ  NOT NULL DEFAULT now(),
    sys_upd_ts         TIMESTAMPTZ  NULL,
    sys_ins_process    VARCHAR(100) NOT NULL,
    sys_upd_process    VARCHAR(100) NULL,

    CONSTRAINT pk_tblr_agreement_relation PRIMARY KEY (agreement_relation_id),
    CONSTRAINT fk_tblr_agreement_relation_credit_facility FOREIGN KEY (credit_facility_id)
        REFERENCES tbl_credit_facility (credit_facility_id)
);

CREATE INDEX IF NOT EXISTS idx_tblr_agreement_relation_agmt_id
    ON tblr_agreement_relation (agmt_id);



