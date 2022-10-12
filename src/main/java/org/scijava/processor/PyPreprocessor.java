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
