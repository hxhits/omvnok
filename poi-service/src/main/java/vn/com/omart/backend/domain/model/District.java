package vn.com.omart.backend.domain.model;

import lombok.ToString;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "district")
@ToString
public class District {

    @Id
    @Column(name = "id", columnDefinition = "int")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", columnDefinition = "varchar")
    private String name;

    @Column(name = "province_id", columnDefinition = "int")
    private Long provinceId;

    @OneToMany(mappedBy = "district", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
   	List<BookCar> bookcars;

    // GETTERS
    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Long provinceId() {
        return provinceId;
    }

	public List<BookCar> getBookcars() {
		return bookcars;
	}

	public void setBookcars(List<BookCar> bookcars) {
		this.bookcars = bookcars;
	}
    
}
