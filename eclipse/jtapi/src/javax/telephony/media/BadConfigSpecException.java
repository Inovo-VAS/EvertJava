/*
 * BadConfigSpecException.java Version-1.4, 2002/11/22 09:26:10 -0800 (Fri)
 * ECTF S.410-R2 Source code distribution.
 *
 * Copyright (c) 2002, Enterprise Computer Telephony Forum (ECTF),
 * All Rights Reserved.
 *
 * Use and redistribution of this file is subject to a License.
 * For terms and conditions see: javax/telephony/media/LICENSE.HTML
 * 
 * In short, you can use this source code if you keep and display
 * the ECTF Copyright and the License conditions. The code is supplied
 * "AS IS" and ECTF disclaims all warranties and liability.
 */

package javax.telephony.media;

/**
 * Configuration failed because the ConfigSpec is somehow bad.
 *
 * @author  Jeff Peck
 * @since   JTAPI-1.3
 */
public
class BadConfigSpecException extends MediaConfigException {
    /**
     * Constructs a <code>BadConfigSpecException</code> 
     * with no specified detail message. 
     */
    public BadConfigSpecException() {super();}

    /**
     * Constructs a <code>BadConfigSpecException</code> 
     * with the specified detail message. 
     *
     * @param   s   the detail message.
     */
    public BadConfigSpecException(String s) {super(s);}

    /**
     * Construct a <code>BadConfigSpecException</code>
     * with the specified detail message and Event.
     * 
     * @param s  the detail message describing the error in the event.
     * @param event a MediaEvent 
     */
    public BadConfigSpecException(String s, MediaEvent event) {
	super(s, event);
    }
}
