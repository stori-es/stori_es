package net.lightoze.gwt.i18n.server;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.LocaleUtils;
import org.slf4j.LoggerFactory;

/**
 * @author Vladimir Kulev
 */
public class MessagesExporter {
    public static void main(String[] args) throws Exception {
        Class cls = Class.forName(args[0]);
        Locale locale = null;
        if (args.length > 1) {
            locale = LocaleUtils.toLocale(args[1]);
        }
        MessagesProxy proxy = new MessagesProxy(cls, LoggerFactory.getLogger(MessagesExporter.class), null);
        Map<String, String> properties = proxy.loadBundles(locale, true, false);
        HashSet<String> seenMethods = new HashSet<String>();
        HashSet<String> seenKeys = new HashSet<String>();

        LinkedHashMap<String, String> output = new LinkedHashMap<String, String>();
        for (Method method : cls.getDeclaredMethods()) {
            seenMethods.add(method.getName());
            MessagesProxy.MessageDescriptor descriptor = proxy.getDescriptor(method);
            if (descriptor.defaults.isEmpty()) {
                descriptor.defaults.put("", "");
            }
            for (Map.Entry<String, String> entry : descriptor.defaults.entrySet()) {
                StringBuilder builder = new StringBuilder();
                if (!entry.getValue().isEmpty()) {
                    builder.append("# ").append(escape(entry.getValue())).append('\n');
                }

                String key = descriptor.key;
                if (!entry.getKey().isEmpty()) {
                    key += '[' + entry.getKey() + ']';
                }
                seenKeys.add(key);

                String value = properties.get(key);
                if (value == null) {
                    System.err.println("Key not localized: " + key);
                    value = "";
                    builder.append('#');
                }
                builder.append(key).append('=').append(escape(value)).append("\n\n");
                write(output, method.getName(), builder.toString());
            }
        }
        for (String key : properties.keySet()) {
            if (seenKeys.contains(key)) {
                continue;
            }
            String method = key.replaceFirst("\\[.+\\]", "");
            if (seenMethods.contains(method)) {
                System.err.println("Unused key: " + key);
            } else {
                System.err.println("Unused method: " + key);
            }
            write(output, method, String.format("%s=%s\n\n", key, escape(properties.get(key))));
        }
        Thread.sleep(500); // let stderr to flush
        for (String str : output.values()) {
            System.out.print(str);
        }
    }

    private static void write(LinkedHashMap<String, String> output, String key, String str) {
        output.put(key, output.containsKey(key) ? output.get(key) + str : str);
    }

    private static String escape(String s) {
        return s.replaceAll("\\\\", "\\\\\\\\")
                .replaceAll("\\n", "\\\\n")
                .replaceAll("\\r", "\\\\r")
                .replaceAll("\\t", "\\\\t");
    }
}
