package de.jlo.talendcomp.json;

import java.util.Collection;
import java.util.Locale;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ValueNode;

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

    public static String buildSQLInClause(Set<? extends Object> keys, String noKeysReplacement) {
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
        } else if (noKeysReplacement != null) {
        	sb.append(noKeysReplacement);
        } else {
            sb.append(" is not null and 1=0 "); // a dummy condition to enforce a
            // no-selection filter
        }
        return sb.toString();
    }

	public static Locale createLocale(String locale) {
		int p = locale.indexOf('_');
		String language = locale;
		String country = "";
		if (p > 0) {
			language = locale.substring(0, p);
			country = locale.substring(p);
		}
		return new Locale(language, country);
	}

	public static boolean isNull(JsonNode node, boolean takeEmptyLikeNull) {
		if (node == null) {
			return true;
		} else if (node.isNull()) {
			return true;
		} else if (node instanceof ValueNode) {
			return false;
		} else {
			if (takeEmptyLikeNull && node.size() == 0) {
				return true;
			}
		}
		return false;
	}
	
	public static String toString(String[] array) {
		if (array == null) {
			return null;
		} else {
			StringBuilder r = new StringBuilder();
			r.append("[");
			for (int i = 0; i < array.length; i++) {
				if (i > 0) {
					r.append(",");
				}
				r.append(array[i]);
			}
			r.append("]");
			return r.toString();
		}
	}

}
