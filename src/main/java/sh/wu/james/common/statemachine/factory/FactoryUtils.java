package sh.wu.james.common.statemachine.factory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

import sh.wu.james.common.exception.StateMachineInitException;
import sh.wu.james.common.statemachine.xml.EventDefinition;
import sh.wu.james.common.utils.Logger;

public class FactoryUtils {
    public static String[] buildEvent(List<EventDefinition> evts) {
        ArrayList<String> stateEvts = new ArrayList<String>();
        if (evts == null || evts.size() == 0) {
            return new String[0];
        }
        for (EventDefinition evtDef : evts) {
            stateEvts.add(evtDef.getType());
        }
        return stateEvts.toArray(new String[stateEvts.size()]);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Object buildStatus(Class statusEntityClass, String nextStatus) throws StateMachineInitException {
        if (statusEntityClass.isEnum()) {
            return Enum.valueOf(statusEntityClass, nextStatus);
        }
        throw new StateMachineInitException("Not support status mark with no enum class.");
    }

    public static <T> T getBeanByType(ApplicationContext ctx, Class<T> clazz) {
        String[] nameByType = ctx.getBeanNamesForType(clazz);
        if (nameByType.length == 0) {
            throw new NoSuchBeanDefinitionException(clazz.getName());
        } else if (nameByType.length > 1) {
            throw new BeanCreationException("Bean found more than by according to the class by bean type, type:"
                    + clazz.getName());
        }
        return ctx.getBean(nameByType[0], clazz);
    }

    @SuppressWarnings("rawtypes")
    public static Class findClassWithException(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            Logger.error(FactoryUtils.class, "BizInterface not found.", e);
            throw new StateMachineInitException(e);
        }
    }

}
