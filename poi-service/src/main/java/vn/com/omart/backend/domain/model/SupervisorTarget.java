package vn.com.omart.backend.domain.model;

import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "omart_sales_target")
@ToString
//@IdClass(SupervisorTarget.class)
public class SupervisorTarget {

//    @Id
    @Column(name = "`month`", columnDefinition = "DATE")
    private Date month;

    @Id
    @Column(name = "user_id", columnDefinition = "varchar")
    private String userId;

//    @Id
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "province_id", columnDefinition = "int")
    private Province province;

    @JoinColumn(name = "quantity", columnDefinition = "bigint")
    private Long quantity;


    public Date month() {
        return month;
    }

    public String userId() {
        return userId;
    }

    public Province province() {
        return province;
    }

    public Long quantity() {
        return quantity;
    }
}
