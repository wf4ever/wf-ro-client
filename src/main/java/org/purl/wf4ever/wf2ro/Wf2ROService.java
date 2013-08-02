package org.purl.wf4ever.wf2ro;

import java.io.Serializable;
import java.net.URI;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;

/**
 * Implements the API of a Workflow-RO transformation service. See
 * http://www.wf4ever-project.org/wiki/display/docs/Wf-RO+transformation+service.
 * 
 * @author piotrekhol
 * 
 */
public class Wf2ROService implements Serializable {

    /** id. */
    private static final long serialVersionUID = 6494394874183471515L;

    /** Logger. */
    @SuppressWarnings("unused")
    private static final Logger LOGGER = Logger.getLogger(Wf2ROService.class);

    /** RODL access token. */
    private String token;

    /** web client. */
    private transient Client client;

    /** Base Wf-RO service URI. */
    private URI serviceUri;


    /**
     * Constructor.
     * 
     * @param serviceUri
     *            Wf-RO URI
     * @param token
     *            RODL access token
     */
    public Wf2ROService(URI serviceUri, String token) {
        this.serviceUri = serviceUri;
        this.token = token;
    }


    /**
     * Return an HTTP client, creating it if necessary.
     * 
     * @return an HTTP client
     */
    private Client getClient() {
        if (client == null) {
            client = Client.create();
        }
        return client;
    }


    /**
     * Start a new transformation job.
     * 
     * @param workflowUri
     *            workflow URI
     * @param mimeType
     *            workflow MIME type
     * @param roUri
     *            RO URI
     * @return a status of the job, which can later be refreshed
     * @throws ServiceException
     *             when the service throws an unexpected response
     */
    public JobStatus transform(URI workflowUri, String mimeType, URI roUri)
            throws ServiceException {
        JobConfig config = new JobConfig(workflowUri, mimeType, roUri, token);
        WebResource webResource = getClient().resource(serviceUri);
        ClientResponse response = webResource.type(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class, config);
        if (response.getClientResponseStatus().equals(Status.CREATED)) {
            throw new ServiceException("Service returned response " + response.toString());
        }
        URI job = response.getLocation();
        JobStatus status = webResource.uri(job).get(JobStatus.class);
        status.setUri(job);
        status.setService(this);
        response.close();
        return status;
    }


    /**
     * Get a new status of a snapshotting/archival job.
     * 
     * @param jobUri
     *            URI of an existing job
     * @return the job status
     */
    public JobStatus getStatus(URI jobUri) {
        JobStatus statusOut = getClient().resource(jobUri).accept(MediaType.APPLICATION_JSON_TYPE).get(JobStatus.class);
        statusOut.setUri(jobUri);
        statusOut.setService(this);
        return statusOut;
    }
}
