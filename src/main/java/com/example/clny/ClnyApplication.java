package com.example.clny;

import com.example.clny.dto.CredentialsDTO;
import com.example.clny.dto.UserDTO;
import com.example.clny.mapper.CredentialsMapper;
import com.example.clny.mapper.UserMapper;
import com.example.clny.model.Credentials;
import com.example.clny.model.Profile;
import com.example.clny.model.User;
import com.example.clny.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClnyApplication implements CommandLineRunner {

	@Autowired
	private UserService userService;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private CredentialsMapper credentialsMapper;

	public static void main(String[] args) {
		SpringApplication.run(ClnyApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Credentials credentials = new Credentials("alibaba@gmail.com","password");
		Profile profile = new Profile();
		User user = new User("Ali","Baba",credentials,profile);

		UserDTO userDTO = userMapper.userToUserDTO(user);

		UserDTO registeredUser = userService.register(userDTO);
		System.out.println(registeredUser);

		CredentialsDTO credentialsDTO = credentialsMapper.credentialsToCredentialsDTO(credentials);

		UserDTO authenticatedUser = userService.login(credentialsDTO);
		System.out.println(authenticatedUser);

		// email already in use
		// userService.register(userDTO);

		// no account
		// Credentials credentials2 = new Credentials("noaccount@gmail.com","password");
		// CredentialsDTO credentialsDTO2 = credentialsMapper.credentialsToCredentialsDTO(credentials2);
		// userService.login(credentialsDTO2);

		// wrong password
		// Credentials credentials3 = new Credentials("alibaba@gmail.com","wrongpassword");
		// CredentialsDTO credentialsDTO3 = credentialsMapper.credentialsToCredentialsDTO(credentials3);
		// userService.login(credentialsDTO3);
	}

}
