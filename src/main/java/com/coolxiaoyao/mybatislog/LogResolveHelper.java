package com.coolxiaoyao.mybatislog;

import com.coolxiaoyao.mybatislog.pair.SqlParamPair;
import com.coolxiaoyao.mybatislog.type.ParamItem;
import com.coolxiaoyao.mybatislog.type.TypeMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author kerryzhang on 2021/10/27
 * <p>
 * 解析Log参数
 */


public class LogResolveHelper {

    public static final String PREPARING = "Preparing: ";
    public static final String PARAMETERS = "Parameters: ";
    public static final String PARAMETERS_SPLIT = ", ";
    public static final Pattern PARAMETERS_TYPE = Pattern.compile("(.*)\\((\\w+)\\)");

    /**
     * 解析SQL,SQL参数
     *
     * @param log
     * @return
     */
    public static List<SqlParamPair> resolveLog(String log) {
        List<SqlParamPair> res = new ArrayList<>();

        final int len = log.length();
        int startPos = 0;

        while (startPos < len) {
            // SQL
            int psIndex = log.indexOf(PREPARING, startPos);
            if (psIndex == -1) {
                break;
            }

            int delta;
            int peIndex = log.indexOf("\n", psIndex);
            if (peIndex == -1) {
                peIndex = log.indexOf("\r\n", psIndex);
                if (peIndex == -1) {
                    break;
                } else {
                    delta = 2;
                }
            } else {
                delta = 1;
            }
            String sql = log.substring(PREPARING.length() + psIndex, peIndex);
            startPos = peIndex + delta;

            // params
            int rsIndex = log.indexOf(PARAMETERS, startPos);
            if (rsIndex == -1) {
                break;
            }

            // reset
            delta = 0;
            int reIndex = log.indexOf("\n", startPos);
            if (reIndex == -1) {
                reIndex = log.indexOf("\r\n", psIndex);
                if (reIndex == -1) {
                    reIndex = len;
                } else {
                    delta = 2;
                }
            } else {
                delta = 1;
            }
            String param = log.substring(PARAMETERS.length() + rsIndex, reIndex);
            startPos = reIndex + delta;

            SqlParamPair pair = new SqlParamPair(sql, param);
            res.add(pair);
        }
        return res;
    }


    /**
     * 解析参数成多个
     *
     * @param paramStr
     * @return
     */
    public static List<ParamItem> resolveParams(String paramStr) {
        List<ParamItem> items = new ArrayList<>();
        String[] params = paramStr.split(PARAMETERS_SPLIT);
        for (String param : params) {
            Matcher matcher = PARAMETERS_TYPE.matcher(param);
            if (!matcher.matches()) {
                break;
            }
            String type = matcher.group(2);
            String value = matcher.group(1);

            Class<?> cls = TypeMatcher.findClass(type);

            ParamItem item = new ParamItem(type, cls, value);
            items.add(item);
        }

        return items;
    }

}
