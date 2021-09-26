/*-
 * #%L
 * PyImageJ-SciJava-Command
 * %%
 * Copyright (C) 2021 EPFL
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

import ij.ImagePlus;
import net.imagej.ImageJ;
import org.junit.After;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

public class SimpleJavaDemo {

    static ImageJ ij;

    static public void main(String... args) throws Exception {
        // create the ImageJ application context with all available services
        ij = new ImageJ();

        new PyCommandBuilder()
                .name("pyimagej.MyCommand")
                .input("name", String.class)
                .input("repeat", Integer.class)
                //.input("image", ImagePlus.class)
                .output("greetings", String.class)
                .menuPath("Plugins>DemoPyCommand")
                .function(inputs -> {
                    Map<String, Object> out = new HashMap<>();
                    for (int i = 0;i<(int)inputs.get("repeat");i++) {
                        System.out.println("Repeat!");
                    }
                    String greetings = "Hello "+inputs.get("name")+"!";
                    //((ImagePlus)inputs.get("image")).close();
                    out.put("greetings", greetings);
                    return out;
                })
                .create(ij.context());

        ij.ui().showUI();

        Future<CommandModule> module = ij.command().run("pyimagej.MyCommand", true);

        DefaultCommandService dcs;

        System.out.println(module);

    }

    @Test
    public void demoRunOk() {
        //main(new String[]{""});
    }

    @After
    public void closeFiji() {
        TestHelper.closeFiji(ij);
    }
}
