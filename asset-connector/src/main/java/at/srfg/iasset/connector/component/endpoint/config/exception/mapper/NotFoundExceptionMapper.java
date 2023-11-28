package at.srfg.iasset.connector.component.endpoint.config.exception.mapper;

import at.srfg.iasset.connector.component.endpoint.config.exception.AccessDeniedException;
import at.srfg.iasset.connector.component.endpoint.config.exception.ApiErrorDetails;
import at.srfg.iasset.repository.api.exception.NotFoundException;
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
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(NotFoundException exception) {

        Status status = Status.NOT_FOUND;

        ApiErrorDetails errorDetails = new ApiErrorDetails();
        errorDetails.setStatus(status.getStatusCode());
        errorDetails.setTitle(status.getReasonPhrase());
        errorDetails.setMessage(exception.getMessage());
        errorDetails.setPath(uriInfo.getAbsolutePath().getPath());

        return Response.status(status).entity(errorDetails).type(MediaType.APPLICATION_JSON).build();
    }
}