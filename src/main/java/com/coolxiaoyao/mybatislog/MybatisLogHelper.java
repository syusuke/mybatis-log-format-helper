package com.coolxiaoyao.mybatislog;

import com.coolxiaoyao.mybatislog.log.LogResolve;
import com.coolxiaoyao.mybatislog.pair.SqlParamPair;
import com.coolxiaoyao.mybatislog.parser.LogDruidParser;
import com.coolxiaoyao.mybatislog.parser.LogParser;
import com.coolxiaoyao.mybatislog.type.ParamItem;
import org.slf4j.Logger;

import java.util.ArrayList;
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
        return formatLog(log, DbType.MYSQL);
    }

    /**
     * 返回完整SQL列表
     *
     * @param log
     * @param dbType
     * @return
     */
    public static List<String> formatLog(String log, DbType dbType) {
        List<SqlParamPair> sqlParamPairs = LogResolve.resolveLog(log);

        List<String> sqls = new ArrayList<>(sqlParamPairs.size());
        for (SqlParamPair sqlParamPair : sqlParamPairs) {
            List<ParamItem> params = LogResolve.resolveParams(sqlParamPair.getParam());
            LogParser logParser = getLogParser();
            // dbType 这里一定使用小写
            String newSql = logParser.parser(sqlParamPair.getSql(), params, dbType.name().toLowerCase(Locale.ROOT));
            sqls.add(newSql);
        }
        return sqls;
    }


    private static LogParser getLogParser() {
        return new LogDruidParser();
    }


}
