/* ============
 * FXGraphics2D
 * ============
 * 
 * (C)opyright 2014-2022, by David Gilbert.
 * 
 * https://github.com/jfree/fxgraphics2d
 *
 * The FXGraphics2D class has been developed by David Gilbert for
 * use in Orson Charts (https://github.com/jfree/orson-charts) and
 * JFreeChart (http://www.jfree.org/jfreechart).  It may be useful for other
 * code that uses the Graphics2D API provided by Java2D.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *   - Neither the name of JFree.org nor the names of its contributors may
 *     be used to endorse or promote products derived from this software
 *     without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */

package org.jfree.fx;

import java.awt.RenderingHints;

/**
 * Defines the rendering hints that can be used with the {@link FXGraphics2D} 
 * class.  There is just one hint defined at present:<br>
 * <ul>
 * <li>{@link #KEY_USE_FX_FONT_METRICS} that controls whether JavaFX font
 * metrics or Java2D font metrics are used.</li>
 * </ul>
 * 
 * @since 1.5
 */
public final class FXHints {

    private FXHints() {
        // no need to instantiate this    
    }
    
    /**
     * The key for the hint that controls whether JavaFX font metrics are
     * used (better matching to rendering engine) or Java2D font metrics.  
     * A {@code Boolean} value (or {@code null}) can be assigned as the value 
     * for this key.
     */
    public static final Key KEY_USE_FX_FONT_METRICS
            = new Key(0);
    
    /**
     * A key for hints used by the {@link FXGraphics2D} class.
     */
    public static class Key extends RenderingHints.Key {

        /**
         * Creates a new instance with the specified private key.
         * 
         * @param privateKey  the private key. 
         */
        public Key(int privateKey) {
            super(privateKey);    
        }
    
        /**
         * Returns {@code true} if {@code val} is a value that is
         * compatible with this key, and {@code false} otherwise.
         * 
         * @param val  the value.
         * 
         * @return A boolean. 
         */
        @Override
        public boolean isCompatibleValue(Object val) {
            if (intKey() == 0) {
                return val == null
                        || val instanceof Boolean;
            }
            throw new RuntimeException("Not expected!");
        }
    }
    
}
