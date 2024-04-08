//package it.krisopea.springcors.repository.model;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.hibernate.annotations.CreationTimestamp;
//
//import java.time.Instant;
//
//@Data
//@Entity
//@Table(name = "DEMO")
//@NoArgsConstructor
//public class DemoEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "ID")
//    private Long id;
//
//    @Column(name = "IUV", unique = true, nullable = false)
//    private String iuv;
//
//    @Column(name = "LOCATION")
//    private String location;
//
//    @Column(name = "NOTICE_ID",nullable = false)
//    private String noticeId;
//
//    @CreationTimestamp
//    @Column(name = "INSERTED_TIMESTAMP")
//    private Instant insertedTimestamp;
//}
//
