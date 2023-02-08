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
package org.scijava.processor;

import org.scijava.Priority;
import org.scijava.module.Module;
import org.scijava.module.process.AbstractPreprocessorPlugin;
import org.scijava.module.process.PreprocessorPlugin;
import org.scijava.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Adds a way to statically add preprocessor from python into the Scijava framework via JPype
 * Holding references to suppliers should limit the memory footprint, even if held through static field
 */
@SuppressWarnings("unused")
@Plugin(type = PreprocessorPlugin.class, priority = Priority.VERY_LOW)
public class PyPreprocessor extends AbstractPreprocessorPlugin {

    static List<Supplier<Consumer<Module>>> pyPreProcessors = new ArrayList<>();

    @Override
    public void process(Module module) {
        for (Supplier<Consumer<Module>> processor : pyPreProcessors) {
            processor.get().accept(module);
        }
    }

    static public void register(Supplier<Consumer<Module>> processor) {
        pyPreProcessors.add(processor);
    }

}
