package org.scijava.command;

import org.scijava.InstantiableException;
import org.scijava.ItemIO;
import org.scijava.ItemVisibility;
import org.scijava.module.Module;
import org.scijava.module.ModuleException;
import org.scijava.module.ModuleInfo;
import org.scijava.module.ModuleItem;
import org.scijava.plugin.Attr;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class PyCommandInfo extends CommandInfo {

    final Supplier<? extends Command> creator;

    public PyCommandInfo(String name,
                         final Plugin annotation,
                         Map<String, Class<?>> inputsDefinition,
                         Map<String, Class<?>> outputsDefinition,
                         Map<String, Object> inputsDefaults,
                         Map<String, Object> outputsDefaults,
                         Function<Map<String, Object>, Map<String, Object>> command)
    {
        super(null, name, null, annotation);

        this.creator = () -> {
            PyModuleCommand c = new PyModuleCommand() {

                @Override
                public CommandInfo getInfo() {
                    return PyCommandInfo.this;
                }

                @Override
                public Object getInput(String name) {
                    return inputs.get(name);
                }

                @Override
                public Object getOutput(String name) {
                    return outputs.get(name);
                }

                @Override
                public void setInput(String name, Object value) {
                    inputs.put(name, value);
                }

                @Override
                public void setOutput(String name, Object value) {
                    outputs.put(name, value);
                }

                @Override
                public void run() {
                    outputs =  command.apply(inputs);
                }

            };
            c.iniInputs(PyCommandInfo.this);
            return c;
        };

        try {

            // Gives a dummy field to build a module
            Field f = creator.get().getClass().getField("dummyContext");

            inputsDefinition.forEach((inputName, clazz) -> {
                Parameter p = new Parameter() {

                    @Override
                    public Class<? extends Annotation> annotationType() {
                        return Parameter.class;
                    }

                    @Override
                    public String label() {
                        return inputName;
                    }

                    @Override
                    public String description() {
                        return "";
                    }

                    @Override
                    public ItemIO type() {
                        return ItemIO.INPUT;
                    }

                    @Override
                    public ItemVisibility visibility() {
                        return ItemVisibility.NORMAL;
                    }

                    @Override
                    public boolean autoFill() {
                        return true;
                    }

                    @Override
                    public boolean required() {
                        return true;
                    }

                    @Override
                    public boolean persist() {
                        return true;
                    }

                    @Override
                    public String persistKey() {
                        return "";
                    }

                    @Override
                    public String initializer() {
                        return "";
                    }

                    @Override
                    public String validater() {
                        return "";
                    }

                    @Override
                    public String callback() {
                        return "";
                    }

                    @Override
                    public String style() {
                        return "";
                    }

                    @Override
                    public String min() {
                        return "";
                    }

                    @Override
                    public String max() {
                        return "";
                    }

                    @Override
                    public String stepSize() {
                        return "";
                    }

                    @Override
                    public String[] choices() {
                        return new String[0];
                    }

                    @Override
                    public Attr[] attrs() {
                        return new Attr[0];
                    }

                    @Override
                    public int columns() {
                        return 0;
                    }
                };
                final CommandModuleItem<Object> item =
                        new PyCommandModuleItem(this, f, clazz, inputsDefaults.get(inputName), p, inputName);

                inputMap.put(inputName, item);
                inputList.add(item);
            });
            // add item to the relevant list (inputs or outputs)

            outputsDefinition.forEach((outputName, clazz) -> {
                Parameter p = new Parameter() {

                    @Override
                    public Class<? extends Annotation> annotationType() {
                        return Parameter.class;
                    }

                    @Override
                    public String label() {
                        return outputName;
                    }

                    @Override
                    public String description() {
                        return "";
                    }

                    @Override
                    public ItemIO type() {
                        return ItemIO.OUTPUT;
                    }

                    @Override
                    public ItemVisibility visibility() {
                        return ItemVisibility.NORMAL;
                    }

                    @Override
                    public boolean autoFill() {
                        return true;
                    }

                    @Override
                    public boolean required() {
                        return true;
                    }

                    @Override
                    public boolean persist() {
                        return true;
                    }

                    @Override
                    public String persistKey() {
                        return "";
                    }

                    @Override
                    public String initializer() {
                        return "";
                    }

                    @Override
                    public String validater() {
                        return "";
                    }

                    @Override
                    public String callback() {
                        return "";
                    }

                    @Override
                    public String style() {
                        return "";
                    }

                    @Override
                    public String min() {
                        return "";
                    }

                    @Override
                    public String max() {
                        return "";
                    }

                    @Override
                    public String stepSize() {
                        return "";
                    }

                    @Override
                    public String[] choices() {
                        return new String[0];
                    }

                    @Override
                    public Attr[] attrs() {
                        return new Attr[0];
                    }

                    @Override
                    public int columns() {
                        return 0;
                    }
                };
                final CommandModuleItem<Object> item =
                        new PyCommandModuleItem(this, f, clazz, outputsDefaults.get(outputName), p, outputName);
                outputMap.put(outputName, item);
                outputList.add(item);
            });
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }

    public static class PyCommandModuleItem<T> extends CommandModuleItem<T> {

        final Parameter parameter;
        final Class<T> objectClass;
        final T defaultValue;
        final String name;

        public PyCommandModuleItem(ModuleInfo info,
                                   Field field,
                                   Class<T> objectClass,
                                   T defaultValue,
                                   Parameter parameter,
                                   String name) {
            super(info, field);
            this.parameter = parameter;
            this.objectClass = objectClass;
            this.name = name;
            this.defaultValue = defaultValue;
        }

        public Parameter getParameter() {
            return parameter;
        }

        @Override
        public Class<T> getType() {
            return objectClass;
        }

        @Override
        public Type getGenericType() {
            return objectClass;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public T getDefaultValue() {
            if (defaultValue!=null) {
                return defaultValue;
            }
            try {
                if (objectClass.equals(Integer.class)) {
                    return (T) Integer.valueOf(0);
                }
                if (objectClass.equals(Double.class)) {
                    return (T) Double.valueOf(0);
                }
                if (objectClass.equals(Float.class)) {
                    return (T) Float.valueOf(0);
                }
                final Object dummy = objectClass.newInstance();
                @SuppressWarnings("unchecked")
                final T value = (T) dummy;
                return value;
            }
            catch (final InstantiationException | IllegalAccessException exc)
            {
                throw new IllegalStateException(exc);
            }
        }

    }




    @Override
    public Class<? extends Command> loadClass() throws InstantiableException {
        return creator.get().getClass();
    }

    @Override
    public Command createInstance() throws InstantiableException {
        return creator.get();
    }

    @Override
    public Module createModule() throws ModuleException {
        return (Module) creator.get();
    }

    /** Table of inputs, keyed on name. */
    private final Map<String, ModuleItem<?>> inputMap =
            new HashMap<>();

    /** Table of outputs, keyed on name. */
    private final Map<String, ModuleItem<?>> outputMap =
            new HashMap<>();

    /** Ordered list of input items. */
    private final List<ModuleItem<?>> inputList = new ArrayList<>();

    /** Ordered list of output items. */
    private final List<ModuleItem<?>> outputList = new ArrayList<>();

    @Override
    public Iterable<ModuleItem<?>> inputs() {
        return Collections.unmodifiableList(inputList);
    }

    @Override
    public Iterable<ModuleItem<?>> outputs() {
        return Collections.unmodifiableList(outputList);
    }

    @Override
    public CommandModuleItem<?> getInput(final String name) {
        return (CommandModuleItem<?>) inputMap.get(name);
    }

}
