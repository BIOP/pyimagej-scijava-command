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
