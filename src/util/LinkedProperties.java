package util;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

/**
 * Extends functionality of Properties to use keys in insertion order.
 *
 * Pulled from:
 * @see <a href="https://stackoverflow.com/questions/1312383/pulling-values-from-a-java-properties-file-in-order"></a>
 *
 * @author Ben Schwennesen
 */
public class LinkedProperties extends Properties {

    private final HashSet<Object> keys = new LinkedHashSet<>();

    @Override
    public Set<String> stringPropertyNames() {
        Set<String> set = new LinkedHashSet<String>();

        for (Object key : this.keys) {
            set.add((String)key);
        }

        return set;
    }

    @Override
    public Enumeration<Object> keys() {
        return Collections.<Object>enumeration(keys);
    }

    @Override
    public Object put(Object key, Object value) {
        keys.add(key);
        return super.put(key, value);
    }
}