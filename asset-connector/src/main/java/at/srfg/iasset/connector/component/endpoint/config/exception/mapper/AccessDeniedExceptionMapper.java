package at.srfg.iasset.connector.component.endpoint.config.exception.mapper;

import at.srfg.iasset.connector.component.endpoint.config.exception.AccessDeniedException;
import at.srfg.iasset.connector.component.endpoint.config.exception.ApiErrorDetails;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Exception mapper for {@link AccessDeniedException}s.
 *
 * @author cassiomolin
 */
@Provider
public class AccessDeniedExceptionMapper implements ExceptionMapper<AccessDeniedException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(AccessDeniedException exception) {

        Status status = Status.FORBIDDEN;

        ApiErrorDetails errorDetails = new ApiErrorDetails();
        errorDetails.setStatus(status.getStatusCode());
        errorDetails.setTitle(status.getReasonPhrase());
        errorDetails.setMessage("You don't have enough permissions to perform this action.");
        errorDetails.setPath(uriInfo.getAbsolutePath().getPath());

        return Response.status(status).entity(errorDetails).type(MediaType.APPLICATION_JSON).build();
    }
}