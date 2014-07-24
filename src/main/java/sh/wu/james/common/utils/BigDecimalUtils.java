package sh.wu.james.common.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class BigDecimalUtils {
    private static Map<Key, BigDecimal> lruMap = new HashMap<Key, BigDecimal>(5000);
    public static final MathContext _SCALE = new MathContext(100, RoundingMode.HALF_UP);

    public static BigDecimal divide(BigDecimal one, BigDecimal another) {
        Key key = new Key(one, another, Type.DIVIDE);
        BigDecimal result = lruMap.get(key);
        if (result == null) {
            result = one.divide(another, _SCALE);
            lruMap.put(key, result);
        }

        return result;
    }

    public static BigDecimal multiply(BigDecimal one, BigDecimal another) {
        Key key = new Key(one, another, Type.MULTIPLY);
        BigDecimal result = lruMap.get(key);
        if (result == null) {
            result = one.multiply(another, _SCALE);
            lruMap.put(key, result);
        }

        return result;
    }

    public static BigDecimal pow(BigDecimal one, int another) {
        Key key = new Key(one, another, Type.POW);
        BigDecimal result = lruMap.get(key);
        if (result == null) {
            result = one.pow(another, _SCALE);
            lruMap.put(key, result);
        }

        return result;
    }

    public static BigDecimal subtract(BigDecimal one, BigDecimal another) {
        Key key = new Key(one, another, Type.SUBTRACT);
        BigDecimal result = lruMap.get(key);
        if (result == null) {
            result = one.subtract(another);
            lruMap.put(key, result);
        }

        return result;
    }

    public static BigDecimal add(BigDecimal one, BigDecimal another) {
        Key key = new Key(one, another, Type.ADD);
        BigDecimal result = lruMap.get(key);
        if (result == null) {
            result = one.add(another);
            lruMap.put(key, result);
        }

        return result;
    }

    private static class Key {
        public BigDecimal one;
        public Object another;
        public Type type;

        public Key(BigDecimal one, Object another, Type type) {
            this.one = one;
            this.another = another;
            this.type = type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Key key = (Key) o;

            if (!another.equals(key.another)) return false;
            if (!one.equals(key.one)) return false;
            if (type != key.type) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = one.hashCode();
            result = 31 * result + another.hashCode();
            result = 31 * result + type.hashCode();
            return result;
        }
    }

    private static enum Type {
        ADD, SUBTRACT, MULTIPLY, DIVIDE, POW
    }
}
