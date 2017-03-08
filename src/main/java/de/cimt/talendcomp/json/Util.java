package de.cimt.talendcomp.json;

import java.util.Collection;

public class Util {

    public static String buildListAsString(Collection<? extends Object> keys, boolean sql) {
        StringBuilder sb = new StringBuilder();
        boolean firstLoop = true;
        for (Object key : keys) {
            if (key instanceof String) {
                if (firstLoop) {
                    firstLoop = false;
                } else {
                    sb.append(",");
                }
                if (sql) {
                    sb.append("'");
                }
                sb.append(((String) key).trim());
                if (sql) {
                    sb.append("'");
                }
            } else if (key != null) {
                if (firstLoop) {
                    firstLoop = false;
                } else {
                    sb.append(",");
                }
                sb.append(String.valueOf(key));
            }
        }
        return sb.toString();
    }

}
