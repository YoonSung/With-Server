package gaongil.service;

import gaongil.domain.User;
import gaongil.dto.UserDTO;
import gaongil.repository.UserRepository;
import gaongil.security.LoginRequiredException;
import gaongil.support.exception.WrongParameterException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	public User create(UserDTO userDTO) {
		if (userDTO == null || userDTO.canRegistable() == false)
			throw new WrongParameterException();

		return userRepository.save(userDTO.toDomain());
	}

	//TODO Batch, delete long period member (Invited Long time ago)
	public User createTemporally(UserDTO userDTO) {
		if (StringUtils.isEmpty(userDTO.getPhoneNumber()) || !userDTO.isValidPhoneNumber())
			throw new WrongParameterException();

		return userRepository.save(userDTO.toDomain());
	}

	public User createByInvitation(User selectedUser, UserDTO userForm) {
		if (StringUtils.isEmpty(userForm.getUuid()) || StringUtils.isEmpty(userForm.getRegId()))
			throw new WrongParameterException();

		// public User(Long id, String phoneNumber, String nickname, String imagePath, String regId, String uuid) {
		User newUser = new User(
				selectedUser.getId(),
				selectedUser.getPhoneNumber(),
				selectedUser.getNickname(),
				selectedUser.getImagePath(),
				userForm.getRegId(),
				userForm.getUuid()
		);

		return userRepository.save(newUser);
	}

	public User findById(Long id) {
		if (id == null)
			throw new WrongParameterException();

		return userRepository.findOne(id);
	}

	public User findByPhoneNumber(String phoneNumber) {
		if (phoneNumber == null)
			throw new WrongParameterException();

		return userRepository.findByPhoneNumber(phoneNumber);
	}

	public User getCurrentLoginUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null)
			throw new LoginRequiredException();

		String id = authentication.getName();
		return findById(Long.parseLong(id));
	}

	public void delete(User selectedUser) {
		userRepository.delete(selectedUser);
	}
}
