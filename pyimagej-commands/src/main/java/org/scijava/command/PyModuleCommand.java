package org.scijava.command;

import org.scijava.Cancelable;
import org.scijava.Context;
import org.scijava.Contextual;
import org.scijava.module.AbstractModule;
import org.scijava.plugin.Parameter;

import java.util.HashMap;
import java.util.Map;

public abstract class PyModuleCommand extends AbstractModule implements
        Cancelable, Command, Contextual
{


    @Override
    public Context context() {
        return getContext();
    }

    @Parameter
    public Context dummyContext;

    Map<String, Object> inputs = new HashMap<>();
    Map<String, Object> outputs = new HashMap<>();


    @Parameter
    private Context context;

    @Parameter
    private CommandService commandService;

    private CommandInfo info;

    /** Reason for cancelation, or null if not canceled. */
    private String cancelReason;

    // -- Module methods --

    @Override
    public CommandInfo getInfo() {
        if (info == null) {
            // NB: Obtain metadata lazily.
            info = commandService.getCommand(getClass());
        }
        return info;
    }

    // -- Contextual methods --

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void setContext(final Context context) {
        context.inject(this);
    }

    // -- Cancelable methods --

    @Override
    public boolean isCanceled() {
        return cancelReason != null;
    }

    @Override
    public void cancel(final String reason) {
        cancelReason = reason == null ? "" : reason;
    }

    @Override
    public String getCancelReason() {
        return cancelReason;
    }

    public void iniInputs(PyCommandInfo pyCommandInfo) {
        getInfo().inputs().forEach(input -> {
            inputs.put(input.getName(), null);
        });
    }
}
