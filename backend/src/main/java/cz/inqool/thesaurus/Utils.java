package cz.inqool.thesaurus;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;

import java.util.Objects;

@SuppressWarnings({"unchecked", "unused"})
public class Utils {

    /**
     * Check whether the given object is a JDK dynamic proxy or a CGLIB proxy.
     *
     * @see AopUtils#isAopProxy(Object)
     */
    public static boolean isProxy(Object a) {
        return (AopUtils.isAopProxy(a) && a instanceof Advised);
    }

    /**
     * Extracts exposed instance from a given proxy object or the object in case of already exposed instance.
     */
    public static <T> T unwrap(T a) {
        if (isProxy(a)) {
            try {
                Object target = ((Advised) a).getTargetSource().getTarget();

                if (!Objects.equals(target, null)) {
                    return (T) target;
                } else {
                    return null;
                }
            } catch (Exception ignored) {
                return null; // return null if not in scope
            }
        } else {
            return a;
        }
    }
}
