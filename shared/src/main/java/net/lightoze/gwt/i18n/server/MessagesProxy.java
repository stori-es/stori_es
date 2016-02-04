package net.lightoze.gwt.i18n.server;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;

import com.google.gwt.i18n.client.LocalizableResource;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.PluralRule;
import com.google.gwt.i18n.client.impl.plurals.DefaultRule;

/**
 * @author Vladimir Kulev
 */
class MessagesProxy extends LocaleProxy {
    private final Map<Object, PluralRule> rules = new HashMap<Object, PluralRule>();
    private final Map<Method, MessageDescriptor> descriptors = new HashMap<Method, MessageDescriptor>();

    protected MessagesProxy(Class<? extends LocalizableResource> cls, Logger log, Locale locale) {
        super(cls, log, locale);
    }

    private synchronized PluralRule getRule(Class<? extends PluralRule> cls) {
        try {
            if (cls.isAssignableFrom(PluralRule.class)) {
                PluralRule rule = rules.get(getCurrentLocale());
                if (rule == null) {
                    rule = (PluralRule) getClassLoader().loadClass(
                            DefaultRule.class.getCanonicalName() + '_' + getCurrentLocale().getLanguage()
                    ).newInstance();
                    rules.put(getCurrentLocale(), rule);
                }
                return rule;
            } else {
                PluralRule rule = rules.get(cls);
                if (rule == null) {
                    rule = cls.newInstance();
                    rules.put(cls, rule);
                }
                return rule;
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static List<String> expand(List<String> list, String... variants) {
        if (list.isEmpty()) {
            return new ArrayList<String>(Arrays.asList(variants));
        }
        List<String> result = new ArrayList<String>(list.size() * variants.length);
        for (String str : list) {
            for (String variant : variants) {
                result.add(str + '|' + variant);
            }
        }
        return result;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MessageDescriptor desc = getDescriptor(method);
        List<String> forms = new ArrayList<String>();
        for (int i = 0; i < desc.args.length; i++) {
            MessageArgument arg = desc.args[i];
            if (arg.pluralCount) {
                PluralRule rule = getRule(arg.pluralRule);
                int n = ((Number) args[i]).intValue();
                forms = expand(forms, "=" + n, rule.pluralForms()[rule.select(n - arg.pluralOffset)].getName());
                args[i] = n - arg.pluralOffset;
            }
            if (arg.select) {
                Object value = args[i];
                String select;
                if (value instanceof Enum) {
                    select = ((Enum) value).name();
                } else {
                    select = String.valueOf(value);
                }
                forms = expand(forms, select, "other");
            }
        }
        String message = null;
        forms.add("");
        Map<String, String> properties = getProperties();
        for (String form : forms) {
            if (desc.defaults.containsKey(form)) {
                message = desc.defaults.get(form);
            }
            String key = desc.key;
            if (form.length() > 0) {
                key += '[' + form + ']';
            }
            if (properties.get(key) != null) {
                message = properties.get(key);
            }
            if (message != null) {
                break;
            }
        }
        if (message == null) {
            log.error(String.format("Unlocalized key '%s(%s)' for locale '%s'", desc.key, forms.get(0),
                    getCurrentLocale()));
            message = '@' + desc.key;
        }
        return new MessageFormat(message, getCurrentLocale()).format(args);
    }

    synchronized MessageDescriptor getDescriptor(Method method) {
        MessageDescriptor desc = descriptors.get(method);
        if (desc == null) {
            desc = new MessageDescriptor();
            {
                Messages.DefaultMessage annotation = method.getAnnotation(Messages.DefaultMessage.class);
                if (annotation != null) {
                    desc.defaults.put("", annotation.value());
                }
            }
            desc.key = getKey(method);
            {
                String[] defaults = null;
                {
                    Messages.PluralText annotation = method.getAnnotation(Messages.PluralText.class);
                    if (annotation != null) {
                        defaults = annotation.value();
                    }
                }
                {
                    Messages.AlternateMessage annotation = method.getAnnotation(Messages.AlternateMessage.class);
                    if (annotation != null) {
                        defaults = annotation.value();
                    }
                }
                if (defaults != null) {
                    for (int i = 0; (i + 1) < defaults.length; i += 2) {
                        desc.defaults.put(defaults[i], defaults[i + 1]);
                    }
                }
            }
            {
                Annotation[][] args = method.getParameterAnnotations();
                desc.args = new MessageArgument[args.length];
                for (int i = 0; i < args.length; i++) {
                    desc.args[i] = new MessageArgument();
                    for (Annotation annotation : args[i]) {
                        if (annotation instanceof Messages.PluralCount) {
                            desc.args[i].pluralCount = true;
                            desc.args[i].pluralRule = ((Messages.PluralCount) annotation).value();
                        }
                        if (annotation instanceof Messages.Offset) {
                            desc.args[i].pluralOffset = ((Messages.Offset) annotation).value();
                        }
                        if (annotation instanceof Messages.Select) {
                            desc.args[i].select = true;
                        }
                    }
                }
            }
            descriptors.put(method, desc);
        }
        return desc;
    }

    class MessageDescriptor {
        String key;
        final Map<String, String> defaults = new HashMap<String, String>();
        MessageArgument[] args;
    }

    class MessageArgument {
        boolean pluralCount;
        int pluralOffset;
        Class<? extends PluralRule> pluralRule;
        boolean select;
    }
}
