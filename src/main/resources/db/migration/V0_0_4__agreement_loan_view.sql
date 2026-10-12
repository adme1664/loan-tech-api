-- =====================================================================
-- View for Kafka Publishing - Agreement Bundle with JSON
-- Aggregates agreement with related entities (balance, party, interest rate, payment schedule)
-- =====================================================================

CREATE OR REPLACE VIEW commercialloan.loan_view AS
SELECT
    agreement.agreement_no,
    agreement.agreement_id,
    -- Calculate latest timestamp across all related entities
    GREATEST(agreement.sys_ins_ts, COALESCE(agreement.sys_upd_ts, agreement.sys_ins_ts)) AS latest_sys_ts,
    -- Build complete JSON object with nested related entities
    json_build_object(
        'agreementNo', agreement.agreement_no,
        'agreementType',agreement.agreement_type,
        'agreement', json_build_object(
            'agreementId', agreement.agreement_id,
            'agreementNo', agreement.agreement_no,
            'creditFacilityNo', agreement.credit_facility_no,
            'agreementType', agreement.agreement_type,
            'productCode', agreement.product_cd,
            'statusCodeSoure', agreement.status_cd_src,
            'statusCd', agreement.status_cd,
            'unbnkLetterIssuerInd', agreement.unbnk_letter_issuer_ind,
            'originalAmt', agreement.original_amt,
            'effctvDt', agreement.effctv_dt,
            'expiryDt', agreement.expiry_dt,
            'maturityDt', agreement.maturity_dt,
            'currency', agreement.currency,
            'sourceSystem', agreement.source_system,
            'deletedInd', agreement.deleted_ind
        ),
        'balances', COALESCE(
            (SELECT json_agg(
                json_build_object(
                    'balanceId', balance.balance_id,
                    'agreementId', balance.agreement_id,
                    'balanceType', balance.balance_type,
                    'creditAmnt', balance.credit_amnt,
                    'debitAmnt', balance.debit_amnt,
                    'balanceDate', balance.balance_date
                )
            )
            FROM commercialloan.tbl_balance balance
            WHERE balance.agreement_id = agreement.agreement_id),
            '[]'::json
        ),
        'interestRates', COALESCE(
            (SELECT json_agg(
                json_build_object(
                    'interestRateId', interest_rate.interest_rate_id,
                    'agreementId', interest_rate.agreement_id,
                    'interestRateType', interest_rate.interest_rate_type,
                    'interestRate', interest_rate.interest_rate,
                    'baseRate', interest_rate.base_rate,
                    'spreadRate', interest_rate.spread_rate,
                    'effectiveDate', interest_rate.effective_dt
                )
            )
            FROM commercialloan.tbl_interest_rate interest_rate
            WHERE interest_rate.agreement_id = agreement.agreement_id),
            '[]'::json
        ),
        'paymentSchedules', COALESCE(
            (SELECT json_agg(
                json_build_object(
                    'paymentScheduleId', payment_schedule.payment_schedule_id,
                    'agreementId', payment_schedule.agreement_id,
                    'paymentDate', payment_schedule.payment_date,
                    'paymentAmnt', payment_schedule.payment_amnt,
                    'businessDate', payment_schedule.schedule_date
                )
            )
            FROM commercialloan.tbl_payment_schedule payment_schedule
            WHERE payment_schedule.agreement_id = agreement.agreement_id),
            '[]'::json
        ),
        'party', (
            SELECT json_build_object(
                'partyId', party.party_id,
                'agreementId', party.agreement_id,
                'unbnkId', party.unbnk_id,
                'memberIdNo', party.member_id_no
            )
            FROM commercialloan.tbl_party party
            WHERE party.agreement_id = agreement.agreement_id
            LIMIT 1
        )
    ) AS agreement_json
FROM commercialloan.tbl_agreement agreement where agreement.agreement_type='LOAN';
COMMENT ON VIEW commercialloan.loan_view IS 'Agreement bundle view for Kafka publishing with complete JSON payload';
