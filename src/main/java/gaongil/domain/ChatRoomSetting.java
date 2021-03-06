package gaongil.domain;

import gaongil.dto.ChatRoomSettingDTO;

import javax.persistence.*;

@Entity
@Table(name="tbl_chat_room_setting")
public class ChatRoomSetting implements ConvertableToDto<ChatRoomSettingDTO> {

	@EmbeddedId
	ChatRoomSettingPK id;

	//TODO H2database not yet support, 1.5 Roadmap will support enum type
	//@Column(name="status", columnDefinition = InvitationStatus.COLUMN_DEFINITION)
	@Column(name="status", columnDefinition = "varchar(20) default 'NOT_REGISTRATION'")
	@Enumerated(EnumType.STRING)
	private InvitationStatus status;

	@Column(name="alarm_on")
	private boolean alarmOn;

	public ChatRoomSetting(){}

	public ChatRoomSetting(ChatRoomSettingPK chatRoomSettingPK, InvitationStatus status) {
		this.id = chatRoomSettingPK;
		this.status = status;
	}

	@Override
	public ChatRoomSettingDTO toDTOExcludeReferenceData() {
		ChatRoomSettingDTO dto = new ChatRoomSettingDTO();
		dto.setAlarmOn(alarmOn);
		dto.setStatus(status);

		return dto;
	}

	@Override
	public ChatRoomSettingDTO toDTOWithReferenceData() {

		ChatRoomSettingDTO dto  = new ChatRoomSettingDTO();
		dto.setStatus(this.status);
		dto.setUser(this.id.getUser().toDTOExcludeReferenceData());

		return dto;
	}

	public boolean isAlarmOn() {
		return alarmOn;
	}

	public InvitationStatus getStatus() {
		return status;
	}

	public ChatRoomSettingPK getId() {
		return id;
	}

	public void setId(ChatRoomSettingPK id) {
		this.id = id;
	}

	public void setStatus(InvitationStatus status) {
		this.status = status;
	}

	public void setAlarmOn(boolean alarmOn) {
		this.alarmOn = alarmOn;
	}
}
