package org.purl.wf4ever.wf2ro;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.sun.xml.txw2.annotation.XmlElement;

/**
 * Job status as JSON.
 * 
 * @author piotrekhol
 * 
 */
@XmlRootElement
public class JobStatus implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6321661813309298438L;


    public enum State {
        /** The job has started and is running. */
        RUNNING,
        /** The job has finished succesfully. */
        DONE,
        /** The job has been cancelled by the user. */
        CANCELLED,
        /** The resource to be formated is invalid. */
        INVALID_RESOURCE,
        /** There has been an unexpected error during conversion. */
        RUNTIME_ERROR;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        };
    }
    
    /** workflow URI. */
    private URI resource;

    /** workflow format MIME type. */
    private String format;

    /** RO URI. */
    private URI ro;

    /** job state. */
    private State state;

    /** resources already uploaded. */
    private List<URI> added;

    /** reason for the status, i.e. exception message. */
    private String reason;

    /** Which folders to extract to */
    private JobExtractFolders extract;


    /** job status URI. */
    private URI uri;

    /** service. */
    private Wf2ROService service;
    

    /**
     * Default empty constructor.
     */
    public JobStatus() {

    }


    /**
     * Constructor.
     * 
     * @param resource
     *            workflow URI
     * @param format
     *            workflow format URI
     * @param extract
     *            Which resources to extract to which folders
     * @param ro
     *            RO URI
     * @param state
     *            job state
     * @param added
     *            resources added
     * @param reason
     *            reason for the status, i.e. exception message
     */
    public JobStatus(URI resource, String format, JobExtractFolders extract, URI ro, State state, List<URI> added, String reason) {
        super();
        this.resource = resource;
        this.format = format;
        this.setExtract(extract);
        this.ro = ro;
        this.state = state;
        this.added = added;
        this.reason = reason;
    }


    public URI getResource() {
        return resource;
    }


    public void setResource(URI resource) {
        this.resource = resource;
    }


    public String getFormat() {
        return format;
    }


    public void setFormat(String format) {
        this.format = format;
    }


    public URI getRo() {
        return ro;
    }


    public void setRo(URI ro) {
        this.ro = ro;
    }


    @XmlElement("status")
    public State getState() {
        return state;
    }


    public void setState(State state) {
        this.state = state;
    }


    public List<URI> getAdded() {
        if (added == null) { 
            added = new ArrayList<>();
        }
        return added; 
    }


    public void setAdded(List<URI> added) {
        this.added = added;
    }


    public String getReason() {
        return reason;
    }


    public void setReason(String reason) {
        this.reason = reason;
    }


    public JobExtractFolders getExtract() {
        return extract;
    }


    public void setExtract(JobExtractFolders extract) {
        this.extract = extract;
    }


    public URI getUri() {
        return uri;
    }


    public void setUri(URI uri) {
        this.uri = uri;
    }


    @XmlTransient
    public Wf2ROService getService() {
        return service;
    }


    public void setService(Wf2ROService service) {
        this.service = service;
    }
    

    /**
     * Reload the properties from the Wf-RO service.
     */
    public void refresh() {
        JobStatus status = service.getStatus(uri);
        this.added = status.getAdded();
        this.format = status.getFormat();
        this.reason = status.getReason();
        this.resource = status.getResource();
        this.ro = status.getRo();
        this.state = status.getState();
        this.extract = status.getExtract();
    }

}
