package com.trustchain.sdkjava.model;

import com.trustchain.sdkjava.enums.OrganizationType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class APIInfo extends API {
    private String organizationName;
    private OrganizationType organizationType;
}
