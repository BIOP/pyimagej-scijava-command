/*-
 * #%L
 * PyImageJ-SciJava-Command
 * %%
 * Copyright (C) 2021 - 2022 EPFL
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
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
import java.lang.reflect.Constructor;
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
                         Map<String, Parameter> inputsParameters,
                         Map<String, Parameter> outputsParameters,
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

                final CommandModuleItem<Object> item =
                        new PyCommandModuleItem(this, f, clazz, inputsDefaults.get(inputName),
                                inputsParameters.get(inputName), inputName);

                inputMap.put(inputName, item);
                inputList.add(item);
            });
            // add item to the relevant list (inputs or outputs)

            outputsDefinition.forEach((outputName, clazz) -> {
                final CommandModuleItem<Object> item =
                        new PyCommandModuleItem(this, f, clazz, outputsDefaults.get(outputName),
                                outputsParameters.get(outputName), outputName);
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
                if (objectClass.equals(int.class)) {
                    return (T) Integer.valueOf(0);
                }
                if (objectClass.equals(double.class)) {
                    return (T) Double.valueOf(0);
                }
                if (objectClass.equals(float.class)) {
                    return (T) Float.valueOf(0);
                }
                if (objectClass.equals(boolean.class)) {
                    return (T) Boolean.FALSE;
                }
                if (objectClass.equals(Integer.class)) {
                    return (T) Integer.valueOf(0);
                }
                if (objectClass.equals(Double.class)) {
                    return (T) Double.valueOf(0);
                }
                if (objectClass.equals(Float.class)) {
                    return (T) Float.valueOf(0);
                }
                if (objectClass.equals(Boolean.class)) {
                    return (T) Boolean.FALSE;
                }
                if (hasParameterlessPublicConstructor(objectClass)) {
                    final Object dummy = objectClass.newInstance();
                    @SuppressWarnings("unchecked") final T value = (T) dummy;
                    return value;
                } else {
                    return null;
                }
            }
            catch (final InstantiationException | IllegalAccessException exc)
            {
                throw new IllegalStateException(exc);
            }
        }

    }

    private static boolean hasParameterlessPublicConstructor(Class<?> clazz) {
        for (Constructor<?> constructor : clazz.getConstructors()) {
            // In Java 7-, use getParameterTypes and check the length of the array returned
            if (constructor.getParameterCount() == 0) {
                return true;
            }
        }
        return false;
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

    @Override
    public String toString(){


        /*if (objectClass.equals(int.class)) {
        }
        if (objectClass.equals(double.class)) {
            return (T) Double.valueOf(0);
        }
        if (objectClass.equals(float.class)) {
            return (T) Float.valueOf(0);
        }
        if (objectClass.equals(boolean.class)) {
            return (T) Boolean.FALSE;
        }
        if (objectClass.equals(Integer.class)) {
            return (T) Integer.valueOf(0);
        }
        if (objectClass.equals(Double.class)) {
            return (T) Double.valueOf(0);
        }
        if (objectClass.equals(Float.class)) {
            return (T) Float.valueOf(0);
        }
        if (objectClass.equals(Boolean.class)) {
            return (T) Boolean.FALSE;
        }*/
        return super.toString();

        //else return "Undefined"
    }

}
