package io.springbatch.springbatchlecture.bTest.entity;

import io.springbatch.springbatchlecture.aTest.entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@Table(name = "tm_mbr_mst")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class TestEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer mberSn;
    private String mberId;
    private String mberNm;
    private String password;
    @Column(name = "telno_1")
    private String telno1;
    @Column(name = "telno_2")
    private String telno2;
    @Column(name = "telno_3")
    private String telno3;
    @Column(name = "hp_no_1")
    private String hpNo1;
    @Column(name = "hp_no_2")
    private String hpNo2;
    @Column(name = "hp_no_3")
    private String hpNo3;
    private String email;
    private String brthdy;
    private LocalDateTime passwordChangeDt;
    private String postNo;
    private String adres;
    private String adresDetail;
    private String rdnmadr;
    private String rdnmadrDetail;
    private String rdnmadrBuldNm;

    public Product toProduct() {
        return Product.builder()
                .id(mberSn.longValue())
                .name(mberNm)
                .price(mberSn)
                .type(password)
                .build();
    }

}
