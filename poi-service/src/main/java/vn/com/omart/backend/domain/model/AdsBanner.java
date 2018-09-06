package vn.com.omart.backend.domain.model;

import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "omart_ads_banner")
@ToString
public class AdsBanner {

    @Id
    @Column(name = "`id`", columnDefinition = "int")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cat_id", columnDefinition = "int")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "poi_id", columnDefinition = "int")
    private PointOfInterest poi;

    @Column(name = "`image`", columnDefinition = "text")
    private String image;

    @Column(name = "`order`", columnDefinition = "int")
    private Long order;

    public Long id() {
        return id;
    }

    public Category category() {
        return category;
    }

    public PointOfInterest poi() {
        return poi;
    }

    public String image() {
        return image;
    }

    public Long order() {
        return order;
    }
}
