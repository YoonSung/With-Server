package gaongil.domain;

import gaongil.dto.UserDTO;

import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

//TODO Seperate to ConvertableToDomain, domain
@Entity
@Table(name="tbl_user")
public class User implements ConvertableToDto<UserDTO> {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String phoneNumber;
	
	@Column
	private String nickname;
	
	@Column
	private String imagePath;
	
	@Column()
	private String regId;

	@Column()
	private String uuid;

	@OneToOne(fetch=FetchType.LAZY, mappedBy="user")
	private Member member;
	
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	private Collection<Safezone> safezones;
	
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	private List<LocationLog> locationLogs;
	
	@ManyToMany(fetch=FetchType.LAZY, mappedBy="users")
	private List<ChatRoom> groups;

	public User(){}

	public User(Long id, String phoneNumber, String nickname, String imagePath, String regId, String uuid) {
		this.id = id;
		this.phoneNumber = phoneNumber;
		this.nickname = nickname;
		this.imagePath = imagePath;
		this.regId = regId;
		this.uuid = uuid;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getNickname() {
		return nickname;
	}

	public String getImagePath() {
		return imagePath;
	}

	public String getRegId() {
		return regId;
	}

	public String getUuid() {
		return this.uuid;
	}

	//Type이 정확하게 일치해야 한다. return type이 long일경우 에러발생
	public Long getId() {
		return id;
	}

	/*
	    T toDTOExcludeReferenceData();
    T toDTOWithReferenceData();
	 */

	public UserDTO toDTOExcludeReferenceData() {
		UserDTO dto = new UserDTO();
		dto.setId(this.id);
		dto.setPhoneNumber(this.phoneNumber);
		dto.setNickname(this.nickname);

		return dto;
	}

	@Override
	public UserDTO toDTOWithReferenceData() {
		return this.toDTOExcludeReferenceData();
	}

	public boolean isSameUserForm(UserDTO userForm) {
		if (this.regId.equalsIgnoreCase(userForm.getRegId()) == false)
			return false;

		if (this.uuid.equalsIgnoreCase(userForm.getUuid()) == false)
			return false;

		return true;
	}

	public boolean isInvitedUser() {
		if (this.regId != null)
			return false;

		if (this.uuid != null)
			return false;

		if (this.id == null)
			return false;

		if (this.phoneNumber == null)
			return false;

		return true;
	}
}
