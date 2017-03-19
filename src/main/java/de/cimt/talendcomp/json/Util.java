package de.cimt.talendcomp.json;

import java.util.Collection;
import java.util.Set;

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
                if (sql) {
                    sb.append(((String) key).trim().replace("'", "''"));
                } else {
                    sb.append(((String) key).trim());
                }
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

    public static String buildSQLInClause(Set<? extends Object> keys) {
        StringBuilder sb = new StringBuilder();
        boolean firstLoop = true;
        for (Object key : keys) {
            if (key instanceof String) {
                if (firstLoop) {
                    firstLoop = false;
                    sb.append(" in (");
                } else {
                    sb.append(",");
                }
                sb.append("'");
                sb.append(((String) key).trim().replace("'", "''"));
                sb.append("'");
            } else if (key != null) {
                if (firstLoop) {
                    firstLoop = false;
                    sb.append(" in (");
                } else {
                    sb.append(",");
                }
                sb.append(String.valueOf(key));
            }
        }
        if (firstLoop == false) {
            sb.append(") ");
        } else {
            sb.append(" is not null "); // a dummy condition to enforce a
            // reasonable filter
        }
        return sb.toString();
    }

}
