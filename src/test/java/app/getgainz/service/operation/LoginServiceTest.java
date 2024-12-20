package app.getgainz.service.operation;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import app.getgainz.dto.LoginDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import app.getgainz.entity.AppUser;
import app.getgainz.exception.GetGainzException;
import app.getgainz.repository.AppUserRepository;
import app.getgainz.testhelper.AppUserTestHelper;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {
	
	@InjectMocks	
	LoginService loginService;
	
	@Mock
	AppUserRepository appUserRepository;
	
	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Test
	void usernameNotFound_throwGetGainzException() {
		LoginDTO loginDtoMock = new LoginDTO("name", "password");
		when(appUserRepository.findByName(anyString())).thenReturn(Optional.empty());

		assertThrows(GetGainzException.class, () -> loginService.login(loginDtoMock));
	}
	
	@Test
	void passwordDoesntMatch_throwGetGainzException() {
		AppUser appUser = AppUserTestHelper.getMockAppUser();
		LoginDTO loginDtoMock = new LoginDTO("name", "incorrectPassword");
		when(appUserRepository.findByName(anyString())).thenReturn(Optional.of(appUser));
		when(bCryptPasswordEncoder.matches(anyString(), anyString())).thenReturn(false);
		
		assertThrows(GetGainzException.class, () -> loginService.login(loginDtoMock));
	}	
}
