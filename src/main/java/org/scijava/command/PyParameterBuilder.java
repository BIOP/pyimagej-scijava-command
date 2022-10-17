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

import org.scijava.ItemIO;
import org.scijava.ItemVisibility;
import org.scijava.plugin.Attr;
import org.scijava.plugin.Parameter;

import java.lang.annotation.Annotation;
public class PyParameterBuilder {

    String label="";
    String description="";
    ItemIO type=ItemIO.INPUT;
    ItemVisibility visibility=ItemVisibility.NORMAL;
    Boolean autoFill= true;
    Boolean required= true;
    Boolean persist= true;
    String persistKey= "";
    String initializer= "";
    String validater= "";
    String callback= "";
    String style= "";
    String min= "";
    String max= "";
    String stepSize= "";
    String[] choices = new String[0];
    Attr[] attrs = new Attr[0];
    int columns = 0;

    public static PyParameterBuilder Builder() {
        return new PyParameterBuilder();
    }

    public static PyParameterBuilder input() {
        return new PyParameterBuilder().type(ItemIO.INPUT);
    }

    public static PyParameterBuilder output() {
        return new PyParameterBuilder().type(ItemIO.OUTPUT);
    }

    public PyParameterBuilder type(ItemIO type) {
        this.type = type;
        return this;
    }

    public PyParameterBuilder label(String label) {
        this.label = label;
        return this;
    }

    public PyParameterBuilder description(String description) {
        this.description = description;
        return this;
    }

    public PyParameterBuilder visibility(ItemVisibility visibility) {
        this.visibility = visibility;
        return this;
    }

    public PyParameterBuilder autoFill(Boolean autoFill) {
        this.autoFill = autoFill;
        return this;
    }

    public PyParameterBuilder required(Boolean required) {
        this.required = required;
        return this;
    }

    public PyParameterBuilder persist(Boolean persist) {
        this.persist = persist;
        return this;
    }

    public PyParameterBuilder persistKey(String persistKey) {
        this.persistKey = persistKey;
        return this;
    }

    public PyParameterBuilder initializer(String initializer) {
        this.initializer = initializer;
        return this;
    }

    public PyParameterBuilder validater(String validater) {
        this.validater = validater;
        return this;
    }

    public PyParameterBuilder callback(String callback) {
        this.callback = callback;
        return this;
    }

    public PyParameterBuilder style(String style) {
        this.style = style;
        return this;
    }

    public PyParameterBuilder min(String min) {
        this.min = min;
        return this;
    }

    public PyParameterBuilder max(String max) {
        this.max = max;
        return this;
    }

    public PyParameterBuilder stepSize(String stepSize) {
        this.stepSize = stepSize;
        return this;
    }

    public PyParameterBuilder choices(String[] choices) {
        this.choices = choices;
        return this;
    }

    public PyParameterBuilder attrs(Attr[] attrs) {
        this.attrs = attrs;
        return this;
    }

    public PyParameterBuilder columns(Integer columns) {
        this.columns = columns;
        return this;
    }

    public static Parameter defaultOutputParameter() {
        return Builder().type(ItemIO.OUTPUT).get();
    }

    public static Parameter defaultInputParameter() {
        return Builder().type(ItemIO.INPUT).get();
    }

    public Parameter get() {
        return new Parameter() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return Parameter.class;
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
            public ItemIO type() {
                return type;
            }

            @Override
            public ItemVisibility visibility() {
                return visibility;
            }

            @Override
            public boolean autoFill() {
                return autoFill;
            }

            @Override
            public boolean required() {
                return required;
            }

            @Override
            public boolean persist() {
                return persist;
            }

            @Override
            public String persistKey() {
                return persistKey;
            }

            @Override
            public String initializer() {
                return  initializer;
            }

            @Override
            public String validater() {
                return validater;
            }

            @Override
            public String callback() {
                return callback;
            }

            @Override
            public String style() {
                return style;
            }

            @Override
            public String min() {
                return min;
            }

            @Override
            public String max() {
                return max;
            }

            @Override
            public String stepSize() {
                return stepSize;
            }

            @Override
            public String[] choices() {
                return choices;
            }

            @Override
            public Attr[] attrs() {
                return attrs;
            }

            @Override
            public int columns() {
                return columns;
            }
        };
    }

}
