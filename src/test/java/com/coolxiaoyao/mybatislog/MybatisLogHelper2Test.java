package com.coolxiaoyao.mybatislog;

import com.coolxiaoyao.mybatislog.pair.SqlParamPair;
import com.coolxiaoyao.mybatislog.type.ParamItem;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kerryzhang on 2021/10/27
 */

class MybatisLogHelper2Test {

    private String resolveSql;

    @Test
    void testFormatSQL_simple_select() {
        resolveSql = "Preparing: select P.*, O.* from t_patient P,      t_outpat_register O where P.org_name = ?   and O.org_name = ?\n" +
                "Parameters: LOVE(String), FUCE(String)\n";
        List<SqlParamPair> sqlParamPairs = LogResolveHelper.resolveLog(resolveSql);
        List<String> sqls = new ArrayList<>(sqlParamPairs.size());
        for (SqlParamPair sqlParamPair : sqlParamPairs) {
            List<ParamItem> paramItems = LogResolveHelper.resolveParams(sqlParamPair.getParam());
            String s = MybatisLogHelper.formatSql(sqlParamPair.getSql(), paramItems, null);
            sqls.add(s);
        }
        for (String sql : sqls) {
            System.out.println("sql: " + sql);
            System.out.println("-------------------");
        }
    }


    @Test
    void fillLogSelect() {
        String log = ":851843552075776- ==>  Preparing: SELECT count(0) FROM (SELECT pmProjectInfo.*, pmUseScope.agency_code authAgencyCode, pmUseScope.dominate_amt authDominateAmt, CASE WHEN pmAnnualExpplanInfo.check_amt > 0 AND pmAnnualExpplanInfo.check_amt IS NOT NULL THEN pmAnnualExpplanInfo.check_amt WHEN pmAnnualExpplanInfo.mof_dep_check_amt > 0 AND pmAnnualExpplanInfo.mof_dep_check_amt IS NOT NULL THEN pmAnnualExpplanInfo.mof_dep_check_amt WHEN pmAnnualExpplanInfo.dept_check_amt > 0 AND pmAnnualExpplanInfo.dept_check_amt IS NOT NULL THEN pmAnnualExpplanInfo.dept_check_amt ELSE pmAnnualExpplanInfo.declaration_amt END effectiveAmt, pmAnnualExpplanInfo.declaration_amt declarationAmt, pmAnnualExpplanInfo.dept_check_amt deptCheckAmt, pmAnnualExpplanInfo.mof_dep_check_amt mofDepCheckAmt, pmAnnualExpplanInfo.check_amt checkAmt FROM pm_project_info pmProjectInfo INNER JOIN pm_use_scope pmUseScope ON pmUseScope.mof_div_code = '440000000' AND pmUseScope.is_deleted = '0' AND pmProjectInfo.pro_id = pmUseScope.pro_id AND pmUseScope.is_deleted = 0 LEFT JOIN pm_annual_expplan_info pmAnnualExpplanInfo ON pmAnnualExpplanInfo.mof_div_code = '440000000' AND pmAnnualExpplanInfo.is_deleted = '0' AND pmAnnualExpplanInfo.pro_id = pmProjectInfo.pro_id AND pmAnnualExpplanInfo.plan_year = ? AND pmAnnualExpplanInfo.is_deleted = 0 WHERE pmProjectInfo.mof_div_code = '440000000' AND pmProjectInfo.is_deleted = '0' AND pmProjectInfo.pro_start_year = ? AND pmProjectInfo.authorise_way = ? AND pmUseScope.is_deleted = 0 AND pmProjectInfo.is_temp_storage = 0 AND pmProjectInfo.is_deleted = 0 AND (pmProjectInfo.publish_status = '1' OR (pmProjectInfo.publish_status = '0' AND pmProjectInfo.in_lib_status = '1')) UNION ALL SELECT pmProjectInfo.update_time, pmProjectInfo.order_bid, pmProjectInfo.pro_id, pmProjectInfo.setup_year, pmProjectInfo.pro_code, pmProjectInfo.pro_name, pmProjectInfo.pro_cat_code, pmProjectInfo.pro_detail_cat_code, pmProjectInfo.pro_level, pmProjectInfo.pro_catalog_bid, pmProjectInfo.pro_catalog_code, pmProjectInfo.agency_code, pmProjectInfo.mgt_dept_code, pmProjectInfo.mgt_mof_dep_code, pmProjectInfo.dept_code, pmProjectInfo.mof_dep_code, pmProjectInfo.use_money_agency_code, pmProjectInfo.exploit_agency, pmProjectInfo.distri_type_code, pmProjectInfo.traobj_agency_code, pmProjectInfo.traobj_agency_id, pmProjectInfo.traobj_agency_name, pmProjectInfo.cen_tra_pro_code, pmProjectInfo.transfer_status, pmProjectInfo.lock_transfer_report, pmProjectInfo.pro_source_code, pmProjectInfo.pro_term, pmProjectInfo.pro_apply_time, pmProjectInfo.pro_start_year, pmProjectInfo.pro_end_date, pmProjectInfo.is_end, pmProjectInfo.end_year, pmProjectInfo.pro_lv2_sum_declara_amt, pmProjectInfo.pro_lv2_sum_amt, pmProjectInfo.dominate_amt, pmProjectInfo.pro_total_amt, pmProjectInfo.pro_total_sum_amt, pmProjectInfo.is_amt_visible, pmProjectInfo.scoi_invest_fund, pmProjectInfo.strategic_areas_code, pmProjectInfo.fiscal_authority_code, pmProjectInfo.fincal_level_code, pmProjectInfo.pro_nature_code, pmProjectInfo.exist_statue, pmProjectInfo.his_bgt, pmProjectInfo.doc_vode, pmProjectInfo.tra_doc_no, pmProjectInfo.approval, pmProjectInfo.is_secret_pro, pmProjectInfo.is_pro_agriculture_fund, pmProjectInfo.apply_link_man, pmProjectInfo.apply_link_tel, pmProjectInfo.build_pro_type_code, pmProjectInfo.is_pro_approval, pmProjectInfo.is_other_dep_exp, pmProjectInfo.is_captial_cons_pro, pmProjectInfo.capital_pro_approval, pmProjectInfo.authorise_way, pmProjectInfo.pro_lib_status, pmProjectInfo.publish_status, pmProjectInfo.in_lib_status, pmProjectInfo.change_before_after_flag, pmProjectInfo.bond_status, pmProjectInfo.data_source, pmProjectInfo.is_construction, pmProjectInfo.is_major_pro, pmProjectInfo.is_basenum_pro, pmProjectInfo.menu_code, pmProjectInfo.construction_agency, '' AS pro_usescope_id, '' AS authFiscalYear, '' AS authMofDepCode, '' AS proDepartment, '' AS proDepartmentCode, '' proDepartmentId, '' proDepartmentName, '' AS authMgtDeptCode, '' AS authMgtMofDepCode, '' AS authProDivCode, '' AS authAgencyCode, '' AS authDominateAmt, CASE WHEN pmAnnualExpplanInfo.check_amt > 0 AND pmAnnualExpplanInfo.check_amt IS NOT NULL THEN pmAnnualExpplanInfo.check_amt WHEN pmAnnualExpplanInfo.mof_dep_check_amt > 0 AND pmAnnualExpplanInfo.mof_dep_check_amt IS NOT NULL THEN pmAnnualExpplanInfo.mof_dep_check_amt WHEN pmAnnualExpplanInfo.dept_check_amt > 0 AND pmAnnualExpplanInfo.dept_check_amt IS NOT NULL THEN pmAnnualExpplanInfo.dept_check_amt ELSE pmAnnualExpplanInfo.declaration_amt END effectiveAmt, pmAnnualExpplanInfo.declaration_amt declarationAmt, pmAnnualExpplanInfo.dept_check_amt deptCheckAmt, pmAnnualExpplanInfo.mof_dep_check_amt mofDepCheckAmt, pmAnnualExpplanInfo.check_amt checkAmt FROM pm_project_info pmProjectInfo LEFT JOIN pm_annual_expplan_info pmAnnualExpplanInfo ON pmAnnualExpplanInfo.mof_div_code = '440000000' AND pmAnnualExpplanInfo.is_deleted = '0' AND pmAnnualExpplanInfo.pro_id = pmProjectInfo.pro_id AND pmAnnualExpplanInfo.plan_year = ? AND pmAnnualExpplanInfo.is_deleted = 0 AND pmProjectInfo.is_deleted = 0 LEFT JOIN pm_use_scope pmUseScope ON pmUseScope.mof_div_code = '440000000' AND pmUseScope.is_deleted = '0' AND pmProjectInfo.pro_id = pmUseScope.pro_id AND pmUseScope.is_deleted = 0 AND pmUseScope.is_deleted = 0 WHERE pmProjectInfo.mof_div_code = '440000000' AND pmProjectInfo.is_deleted = '0' AND pmProjectInfo.pro_start_year = ? AND pmProjectInfo.authorise_way = ? AND pmProjectInfo.is_temp_storage = 0 AND (pmProjectInfo.publish_status = '1' OR (pmProjectInfo.publish_status = '0' AND pmProjectInfo.in_lib_status = '1')) AND ((pmProjectInfo.authorise_way = '1' AND pmProjectInfo.dept_code IN (?)) OR (pmProjectInfo.authorise_way = '2'))) table_count \n" + "2021-10-09 11:02:14.002 DEBUG [http-nio-8082-exec-3] agencyControlAmtSearchCount:143-024120211009110212914ogt000000n33ao:851843552075776- ==> Parameters: 2021(String), 2021(String), 1(String), 2021(String), 2021(String), 1(String), 150(String)";

        String dlej = "Preparing: select P.*, O.* from t_patient P,      t_outpat_register O where P.org_name = ?   and O.org_name = ?\n" +
                "Parameters: LOVE(String), FUCE(String)\n";

        String subQuery = "Preparing: select P.*, O.* from (select * from t_patient where org_name = ?) P,      t_outpat_register O where P.patient_name = ?   and O.patient_name = ?\n" +
                "Parameters: 玉(String), orgName1(String), orgName2(String)";


        String complex = "Preparing: select P.*, O.* from (select * from t_patient where org_name = ?) P          inner join          (select * from t_outpat_register where org_name = ?) O          ON P.org_name = O.org_name AND P.update_time = ? AND O.update_time = ? where P.patient_name = ?   and O.patient_name = ?\n" +
                "Parameters: orgNameP(String), orgNameO(String), 2020-01-01(String), 2020-01-02(String), LOVE(String), YOU(String)\n";


        String tableNameParams = "Preparing: select P.*, O.* from (select * from ? where org_name = ?) P          inner join          (select * from t_outpat_register where org_name = ?) O          ON P.org_name = O.org_name AND P.update_time = ? AND O.update_time = ? where P.patient_name = ?   and O.patient_name = ? and O.pn = ? AND P.pn = ?\n" +
                "Parameters: t_patient(String), orgNameP(String), orgNameO(String), 2020-01-01(String), 2020-01-02(String), LOVE(String), YOU(String), O.name(String), O_NAME(String)\n";


        List<String> sqls = MybatisLogHelper.formatLog(dlej, DbType.MYSQL);

        for (String sql : sqls) {
            System.out.println("sql: " + sql);
            System.out.println("-------------------");
        }

    }

    @Test
    public void delete() {
        String log = "Preparing: delete from table where id = ?\n" +
                "Parameters: 1(Long)";

        // System.out.println(log);

        List<String> sqls = MybatisLogHelper.formatLog(log, DbType.MYSQL);
        for (String sql : sqls) {
            System.out.println("sql: " + sql);
            System.out.println("-------------------");
        }
    }

    @Test
    public void update() {
        String log = "Preparing: update table SET age = 1,name = ? where id = ?\n" +
                "Parameters: 玉(String), 1(Long)";
        List<String> sqls = MybatisLogHelper.formatLog(log, DbType.MYSQL);
        for (String sql : sqls) {
            System.out.println("sql: " + sql);
            System.out.println("-------------------");
        }
    }

    @Test
    public void insert() {
        String log = "Preparing: insert into TA(id,name, pname ) values (?,'1',?),(?,'2',?)\n" +
                "Parameters: name(String), 玉(String), 1(Long), 玉2(String), 2(Long)";
        List<String> sqls = MybatisLogHelper.formatLog(log, DbType.MYSQL);
        for (String sql : sqls) {
            System.out.println("sql: " + sql);
            System.out.println("-------------------");
        }
    }
}