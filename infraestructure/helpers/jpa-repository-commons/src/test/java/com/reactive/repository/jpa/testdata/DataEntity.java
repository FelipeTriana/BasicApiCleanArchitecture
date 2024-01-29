package com.reactive.repository.jpa.testdata;

import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.Date;

@Data
@Entity
class DataEntity {
    @Id
    private String id;
    private String name;
    private Date birthDate;
    private Long size;
}
