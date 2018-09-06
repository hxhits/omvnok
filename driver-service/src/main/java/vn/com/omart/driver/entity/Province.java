package vn.com.omart.driver.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "province")
public class Province implements Serializable {

	private static final long serialVersionUID = -2893261371841961982L;

	@Id
	@GeneratedValue
	@Column(name = "id", columnDefinition = "int")
	private Long id;

	@Column(name = "name", columnDefinition = "varchar")
	private String name;

	@OneToMany(mappedBy = "province", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	private List<DriverInfo> driverInfos;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DriverInfo> getDriverInfos() {
		return driverInfos;
	}

	public void setDriverInfos(List<DriverInfo> driverInfos) {
		this.driverInfos = driverInfos;
	}

}
