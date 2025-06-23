package in.codifi.ambalal.rest.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import in.codifi.ambalal.rms.model.AccessTokenRequest;
import in.codifi.ambalal.rms.model.AccessTokenResponse;
import in.codifi.ambalal.rms.model.RmsUpdateRequest;
import in.codifi.ambalal.rms.model.RmsUpdateResponse;

@RegisterRestClient(configKey = "config-aecor")
@RegisterClientHeaders
public interface IAecorRestService {

    @POST
    @Path("/backofficeapi/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    AccessTokenResponse login(AccessTokenRequest request);

    @POST
    @Path("/backofficeapi/updatermslimit/rmsfields")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    RmsUpdateResponse updateRmsLimitFields(
        @HeaderParam("Authorization") String token,
        RmsUpdateRequest request
    );
}