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

import org.scijava.Context;
import org.scijava.Priority;
import org.scijava.UIDetails;
import org.scijava.plugin.*;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PyCommandBuilder {

    public PyCommandBuilder() {
        DynamicCommand dc;
    }

    String name;
    String menuPath;

    public PyCommandBuilder name(String name) {
        this.name = name;
        return this;
    }

    Map<String, Class<?>> inputsDefinition = new HashMap<>();
    Map<String, Object> inputsDefaultValue = new HashMap<>();
    Map<String, Class<?>> outputsDefinition = new HashMap<>();
    Map<String, Object> outputsDefaultValue = new HashMap<>();
    Function<Map<String, Object>, Map<String, Object>> command;

    public PyCommandBuilder input(String name, Class<?> inputClass) {
        inputsDefinition.put(name, inputClass);
        inputsDefaultValue.put(name, null);
        return this;
    }

    public PyCommandBuilder output(String name, Class<?> outputClass) {
        outputsDefinition.put(name, outputClass);
        outputsDefaultValue.put(name, null);
        return this;
    }

    public PyCommandBuilder input(String name, Class<?> inputClass, Object defaultValue) {
        inputsDefinition.put(name, inputClass);
        inputsDefaultValue.put(name, defaultValue);
        return this;
    }

    public PyCommandBuilder output(String name, Class<?> outputClass, Object defaultValue) {
        outputsDefinition.put(name, outputClass);
        outputsDefaultValue.put(name, defaultValue);
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
                return name;
            }

            @Override
            public String description() {
                return "";
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
                    command);
        ctx.getService(PluginService.class).addPlugin(pci);
    }

}
