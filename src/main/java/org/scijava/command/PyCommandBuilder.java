/*-
 * #%L
 * PyImageJ-SciJava-Command
 * %%
 * Copyright (C) 2021 - 2023 EPFL
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

import org.scijava.Context;
import org.scijava.ItemIO;
import org.scijava.ItemVisibility;
import org.scijava.Priority;
import org.scijava.UIDetails;
import org.scijava.plugin.*;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PyCommandBuilder {

    public PyCommandBuilder() {

    }

    String name;
    String menuPath;
    String label;
    String description = "";

    public PyCommandBuilder name(String name) {
        this.name = name;
        return this;
    }

    Map<String, Class<?>> inputsDefinition = new HashMap<>();
    Map<String, Object> inputsDefaultValue = new HashMap<>();
    Map<String, Parameter> inputsParameter = new HashMap<>();
    Map<String, Class<?>> outputsDefinition = new HashMap<>();
    Map<String, Object> outputsDefaultValue = new HashMap<>();
    Map<String, Parameter> outputsParameter = new HashMap<>();
    Function<Map<String, Object>, Map<String, Object>> command;

    public PyCommandBuilder input(String name, Class<?> inputClass) {
        inputsDefinition.put(name, inputClass);
        inputsDefaultValue.put(name, null);
        Parameter p = PyParameterBuilder.defaultInputParameter();
        inputsParameter.put(name, p);
        return this;
    }

    public PyCommandBuilder output(String name, Class<?> outputClass) {
        outputsDefinition.put(name, outputClass);
        outputsDefaultValue.put(name, null);
        Parameter p = PyParameterBuilder.defaultOutputParameter();
        outputsParameter.put(name, p);
        return this;
    }

    public PyCommandBuilder input(String name, Class<?> inputClass, Object defaultValue) {
        inputsDefinition.put(name, inputClass);
        inputsDefaultValue.put(name, defaultValue);
        Parameter p = PyParameterBuilder.defaultInputParameter();
        inputsParameter.put(name, p);
        return this;
    }

    public PyCommandBuilder output(String name, Class<?> outputClass, Object defaultValue) {
        outputsDefinition.put(name, outputClass);
        outputsDefaultValue.put(name, defaultValue);
        Parameter p = PyParameterBuilder.defaultOutputParameter();
        outputsParameter.put(name, p);
        return this;
    }

    public PyCommandBuilder input(String name, Class<?> inputClass, Parameter p) {
        inputsDefinition.put(name, inputClass);
        inputsDefaultValue.put(name, null);
        inputsParameter.put(name, p);
        return this;
    }

    public PyCommandBuilder output(String name, Class<?> outputClass, Parameter p) {
        outputsDefinition.put(name, outputClass);
        outputsDefaultValue.put(name, null);
        outputsParameter.put(name, p);
        return this;
    }

    public PyCommandBuilder input(String name, Class<?> inputClass, Object defaultValue, Parameter p) {
        inputsDefinition.put(name, inputClass);
        inputsDefaultValue.put(name, defaultValue);
        inputsParameter.put(name, p);
        return this;
    }

    public PyCommandBuilder output(String name, Class<?> outputClass, Object defaultValue, Parameter p) {
        outputsDefinition.put(name, outputClass);
        outputsDefaultValue.put(name, defaultValue);
        outputsParameter.put(name, p);
        return this;
    }

    public PyCommandBuilder function(Function<Map<String, Object>, Map<String, Object>> command) {
        this.command = command;
        return this;
    }

    public PyCommandBuilder menuPath(String menuPath) {
        this.menuPath = menuPath;
        return this;
    }

    public PyCommandBuilder label(String label) {
        this.label = label;
        return this;
    }

    public PyCommandBuilder description(String description) {
        this.description = description;
        return this;
    }

    public void create(Context ctx) {
        Plugin pluginAnnotation = new Plugin(){
            @Override
            public Class<? extends Annotation> annotationType() {
                return Plugin.class;
            }

            @Override
            public Class<? extends SciJavaPlugin> type() {
                return Command.class; //commandSupplier.get().getClass(); Can't be more specific
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            public String label() {
                return label;
            }

            @Override
            public String description() {
                return description;
            }

            @Override
            public String menuPath() {
                return menuPath;
            }

            @Override
            public Menu[] menu() {
                return new Menu[0];
            }

            @Override
            public String menuRoot() {
                return UIDetails.APPLICATION_MENU_ROOT;
            }

            @Override
            public String iconPath() {
                return "";
            }

            @Override
            public double priority() {
                return Priority.NORMAL;
            }

            @Override
            public boolean selectable() {
                return false;
            }

            @Override
            public String selectionGroup() {
                return "";
            }

            @Override
            public boolean enabled() {
                return true;
            }

            @Override
            public boolean visible() {
                return true;
            }

            @Override
            public boolean headless() {
                return false;
            }

            @Override
            public String initializer() {
                return "";
            }

            @Override
            public Attr[] attrs() {
                return new Attr[0];
            }
        };
        PyCommandInfo pci =
                new PyCommandInfo(name,
                    pluginAnnotation,
                    inputsDefinition,
                    outputsDefinition,
                    inputsDefaultValue,
                    outputsDefaultValue,
                    inputsParameter,
                    outputsParameter,
                    command);
        ctx.getService(PluginService.class).addPlugin(pci);
    }


}
