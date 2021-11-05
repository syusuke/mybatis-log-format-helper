package com.coolxiaoyao.mybatislog;

import com.coolxiaoyao.mybatislog.druid.LogDruidParser;
import com.coolxiaoyao.mybatislog.pair.SqlParamPair;
import com.coolxiaoyao.mybatislog.parser.LogParser;
import com.coolxiaoyao.mybatislog.type.ParamItem;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * @author kerryzhang on 2021/10/27
 */


public class MybatisLogHelper {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(MybatisLogHelper.class);

    /**
     * default mysql
     *
     * @param log
     * @return
     */
    public static List<String> formatLog(String log) {
        return formatLog(log, DbType.DEFAULT);
    }

    /**
     * 返回完整SQL列表
     *
     * @param log
     * @param dbType
     * @return
     */
    public static List<String> formatLog(String log, DbType dbType) {
        List<SqlParamPair> sqlParamPairs = LogResolveHelper.resolveLog(log);
        if (sqlParamPairs.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> sqls = new ArrayList<>(sqlParamPairs.size());
        for (SqlParamPair pair : sqlParamPairs) {
            List<ParamItem> params = LogResolveHelper.resolveParams(pair.getParam());
            // dbType 这里一定使用小写
            String newSql = formatSql(pair.getSql(), params, dbType.name().toLowerCase(Locale.ROOT));
            sqls.add(newSql);
        }
        return sqls;
    }


    public static String formatSql(String sql, List<ParamItem> params) {
        return formatSql(sql, params, null);
    }

    public static String formatSql(String sql, List<ParamItem> params, String type) {
        LogParser logParser = getLogParser();
        // dbType 这里一定使用小写
        return logParser.parser(sql, params, type);
    }


    private static LogParser getLogParser() {
        return new LogDruidParser();
    }

}
