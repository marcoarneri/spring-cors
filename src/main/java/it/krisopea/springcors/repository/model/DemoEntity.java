package it.krisopea.springcors.repository.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document
@NoArgsConstructor
public class DemoEntity {

    @Id
    private String id;

    private String iuv;

    private String location;

    private String noticeId;

    private Instant insertedTimestamp;
}

