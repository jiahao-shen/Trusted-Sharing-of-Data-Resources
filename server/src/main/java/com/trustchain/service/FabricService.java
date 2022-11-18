package com.trustchain.service;

import com.trustchain.fabric.FabricGateway;
import com.trustchain.model.API;
import com.trustchain.model.Organization;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FabricService {

    @Async
    public void saveOrganization(Organization organization) {
        FabricGateway fg = new FabricGateway();

        fg.invoke("createOrganization",
                organization.getId().toString(),
                organization.getName(),
                organization.getType().toString(),
                organization.getIntroduction(),
                organization.getSuperior().toString(),
                organization.getCreatedTime().toString());
    }

    @Async
    public void saveAPI(API api) {
        FabricGateway fg = new FabricGateway();

        fg.invoke("createAPI",
                api.getId().toString(),
                api.getName(),
                api.getIntroducation(),
                api.getOrganization().toString(),
                api.getUrl(),
                api.getMethod().toString(),
                api.getHeader(),
                api.getHeaderType().toString(),
                api.getRequest(),
                api.getResponseType().toString(),
                api.getResponse(),
                api.getResponseType().toString(),
                api.getVersion(),
                api.getCreatedTime().toString());
    }

    public Object invokeAPI(String applicant, String apiID, String header, String request) {
        FabricGateway fg = new FabricGateway();

        return fg.invoke("requestAPI",
                UUID.randomUUID().toString(),
                applicant,
                apiID,
                request,
                LocalDateTime.now().toString());
    }
}
