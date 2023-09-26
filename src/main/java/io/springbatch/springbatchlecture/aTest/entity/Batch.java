package io.springbatch.springbatchlecture.aTest.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
@Table(name = "batch")
@Builder
public class Batch {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String jobName;
    private String cron;

}
