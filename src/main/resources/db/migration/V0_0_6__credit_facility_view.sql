CREATE OR REPLACE VIEW commercialloan.credit_facility_view AS
SELECT
    deal.borrowing_agmt_id AS deal_no,
    deal.deal_id,
    -- Calculate latest timestamp across all related entities
    GREATEST(deal.sys_ins_ts, COALESCE(deal.sys_upd_ts, deal.sys_ins_ts)) AS latest_sys_ts,
    -- Build complete JSON object with nested related entities
    json_build_object(
            'dealNo', deal.borrowing_agmt_id,
            'deal', json_build_object(
                    'dealId', deal.deal_id,
                    'borrowingAgreementId', deal.borrowing_agmt_id,
                    'borrowingAgreementName', deal.borrowing_agmt_name,
                    'effectiveDate', deal.effctv_dt,
                    'classificationCode', deal.classification_cd,
                    'nextReviewDate', deal.next_review_dt
                    ),
            'creditFacilities', COALESCE(
                    (SELECT json_agg(
                                    json_build_object(
                                            'creditFacilityId', credit_facility.credit_facility_id,
                                            'creditFacilityNo', credit_facility.credit_facility_no,
                                            'borrowingAgreementId', credit_facility.borrowing_agmt_id,
                                            'creditFacilityName', credit_facility.credit_facility_name,
                                            'statusCode', credit_facility.status_cd,
                                            'statusCodeSource', credit_facility.status_cd_src,
                                            'productCode', credit_facility.product_cd,
                                            'lenderRoleCode', credit_facility.lender_role_cd,
                                            'effectiveDate', credit_facility.effctv_dt,
                                            'expiryDate', credit_facility.expiry_dt,
                                            'maturityDate', credit_facility.maturity_dt,
                                            'currency', credit_facility.currency,
                                            'agreementRelations', COALESCE(
                                                    (SELECT json_agg(
                                                                    json_build_object(
                                                                            'agreementRelationId', agreement_relation.agreement_relation_id,
                                                                            'creditFacilityId', agreement_relation.credit_facility_id,
                                                                            'agreementId', agreement_relation.agmt_id
                                                                    )
                                                            )
                                                     FROM commercialloan.tblr_agreement_relation agreement_relation
                                                     WHERE agreement_relation.credit_facility_id = credit_facility.credit_facility_id),
                                                    '[]'::json
                                                                  )
                                    )
                            )
                     FROM commercialloan.tbl_credit_facility credit_facility
                     WHERE credit_facility.borrowing_agmt_id = deal.borrowing_agmt_id),
                    '[]'::json
                                ),
            'parties', COALESCE(
                    (SELECT json_agg(
                                    json_build_object(
                                            'partyId', party.cf_party_id,
                                            'dealId', party.deal_id,
                                            'unbankId', party.unbnk_id,
                                            'memberIdNo', party.member_id_no
                                    )
                            )
                     FROM commercialloan.tbl_cf_party party
                     WHERE party.deal_id = deal.deal_id),
                    '[]'::json
                       )
    ) AS deal_json
FROM commercialloan.tbl_deal deal;