package gaongil.repository;

import static org.junit.Assert.*;

import javax.transaction.Transactional;

import gaongil.config.AppCoinfig;
import gaongil.domain.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={AppCoinfig.class})
@Transactional
public class UserRepositoryTest {

	@Autowired
	UserRepository userRepository;
	
	User user1;
	
	@Before
	public void setUp() {
		user1 = new User("01099258547", "testNickname", "testRegistrationId");
	}
	
	@Test
	public void userDao() throws Exception {
		assertNotNull(userRepository);
	}
	
	@Test
	@Rollback(true)
	public void create() throws Exception {
		userRepository.save(user1);
	}

	@Test
	@Rollback(true)
	public void select() throws Exception {
		userRepository.save(user1);
		
		User selectedUser = userRepository.findByRegId(user1.getRegId());
		System.out.println("selectedUser : "+selectedUser.toString());
		assertEquals(user1.getNickname(), selectedUser.getNickname());
	}
}
