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

import net.imagej.ImageJ;

public class TestHelper {

    public static void closeFiji(ImageJ ij) {
        try {

            // Closes ij context
            ij.context().close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
