package org.scijava.command;

import ij.ImagePlus;
import net.imagej.ImageJ;
import org.junit.After;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class SimpleJavaDemo {

    static ImageJ ij;

    static public void main(String... args) throws Exception {
        // create the ImageJ application context with all available services
        ij = new ImageJ();

        new PyCommandBuilder()
                .name("pyimagej.MyCommand")
                .input("name", String.class)
                .input("repeat", Integer.class)
                .input("image", ImagePlus.class)
                .output("greetings", String.class)
                .menuPath("Plugins>DemoPyCommand")
                .function(inputs -> {
                    Map<String, Object> out = new HashMap<>();
                    for (int i = 0;i<(int)inputs.get("repeat");i++) {
                        System.out.println("Repeat!");
                    }
                    String greetings = "Hello "+inputs.get("name")+"!";
                    ((ImagePlus)inputs.get("image")).close();
                    out.put("greetings", greetings);
                    return out;
                })
                .create(ij.context());

        ij.ui().showUI();

        ij.command().run("pyimagej.MyCommand", true);

    }

    @Test
    public void demoRunOk() {
        //main(new String[]{""});
    }

    @After
    public void closeFiji() {
        TestHelper.closeFijiAndBdvs(ij);
    }
}
