package com.reactive.repository.jpa.testdata;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder(toBuilder = true)
public class DomainEntity {
    private final String id;
    private final String name;
    private final Date birthDate;
    private final Long size;
}
