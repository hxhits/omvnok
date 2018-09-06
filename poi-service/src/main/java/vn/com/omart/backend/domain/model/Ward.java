package vn.com.omart.backend.domain.model;

import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ward")
@ToString
public class Ward {

    @Id
    @Column(name = "id", columnDefinition = "int")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", columnDefinition = "varchar")
    private String name;

    @Column(name = "province_id", columnDefinition = "int")
    private Long provinceId;

    @Column(name = "district_id", columnDefinition = "int")
    private Long districtId;


    //GETTERS


    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Long provinceId() {
        return provinceId;
    }

    public Long districtId() {
        return districtId;
    }
}
