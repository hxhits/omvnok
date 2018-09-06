package vn.com.omart.backend.domain.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "omart_user_profile")
public class UserProfile {

	@Id
	@GeneratedValue
	@Column(name = "id", columnDefinition = "int")
	private Long id;

	@Column(name = "user_id", columnDefinition = "varchar", unique = true, nullable = false)
	private String userId;

	@Column(name = "name", columnDefinition = "varchar", nullable = false)
	private String name;

	@Column(name = "avatar", columnDefinition = "varchar")
	private String avatar;

	@Column(name = "cover", columnDefinition = "varchar")
	private String cover;

	@Column(name = "date_of_birth", columnDefinition = "date")
	private Date dateOfBirth;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lives_in", referencedColumnName = "id", columnDefinition = "int", nullable = true)
	private Province province;

	@Column(name = "phone", columnDefinition = "varchar")
	private String phone;

	@Column(name = "sex", columnDefinition = "int")
	private int sex;
	
	@Column(name = "coin", columnDefinition = "int")
	private int coin;
	
	@OneToOne(mappedBy = "userProfile", cascade = CascadeType.DETACH, fetch = FetchType.LAZY, optional = false)
	private  OmartCoin omartCoin;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	List<UserFriend> users;
	
	@OneToMany(mappedBy = "friend", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
	List<UserFriend> friends;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getCoin() {
		return coin;
	}

	public void setCoin(int coin) {
		this.coin = coin;
	}

	public OmartCoin getOmartCoin() {
		return omartCoin;
	}

	public void setOmartCoin(OmartCoin omartCoin) {
		this.omartCoin = omartCoin;
	}

	public List<UserFriend> getUsers() {
		return users;
	}

	public void setUsers(List<UserFriend> users) {
		this.users = users;
	}

	public List<UserFriend> getFriends() {
		return friends;
	}

	public void setFriends(List<UserFriend> friends) {
		this.friends = friends;
	}

}