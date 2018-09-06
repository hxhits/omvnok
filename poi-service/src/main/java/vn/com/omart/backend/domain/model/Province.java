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
@Table(name = "province")
@ToString
public class Province {

    @Id
    @Column(name = "id", columnDefinition = "int")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", columnDefinition = "varchar")
    private String name;

    @OneToMany(mappedBy = "province", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	List<BookCar> bookcars;
    
//    @OneToMany(mappedBy = "province", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
//   	List<UserProfile> userProfiles;

    // GETTERS
    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

	public List<BookCar> getBookcars() {
		return bookcars;
	}

	public void setBookcars(List<BookCar> bookcars) {
		this.bookcars = bookcars;
	}
    
}
