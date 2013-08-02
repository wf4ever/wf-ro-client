package org.purl.wf4ever.wf2ro;

/**
 * Exception thrown by the Wf-RO service.
 * 
 * @author piotrekhol
 * 
 */
public class ServiceException extends Exception {

    /** id. */
    private static final long serialVersionUID = 7354632900492357392L;


    /**
     * Constructor.
     * 
     * @param message
     *            message
     */
    public ServiceException(String message) {
        super(message);
    }

}
