package app.getgainz.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import app.getgainz.dto.appuser.AppUserRequestDTO;
import app.getgainz.dto.appuser.AppUserResponseDTO;
import app.getgainz.dto.appuser.AppUserUpdateDTO;
import app.getgainz.entity.AppUser;
import app.getgainz.entity.Role;
import app.getgainz.exception.GetGainzException;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import app.getgainz.repository.RoleRepository;
import app.getgainz.testhelper.AppUserTestHelper;
import app.getgainz.testhelper.RoleTestHelper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AppUserMapperServiceTest {
	
	@InjectMocks
	AppUserMapperService appUserMapperService;
	
	@Mock
	RoleRepository roleRepository;
	
	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Nested
	class RequestDtoToEntity {
		
		@Test
		void requestDTOIsNull_returnNull() {
			AppUser actualAppUser = appUserMapperService.requestDtoToEntity(null);

			assertNull(actualAppUser);
		}

		@Test
		void roleNotFound_throwGetGainzException() {
			AppUserRequestDTO requestDTO = new AppUserRequestDTO("name", "password", "roleName");
					
			when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());

			assertThrows(GetGainzException.class, () -> appUserMapperService.requestDtoToEntity(requestDTO));
		}
		
		@Test
		void returnAppUser() {
			AppUserRequestDTO requestDTO = new AppUserRequestDTO("name", "password", "roleName");
			Role role = RoleTestHelper.getMockRole(1, "roleName");
			
			when(roleRepository.findByName(anyString())).thenReturn(Optional.of(role));
			when(bCryptPasswordEncoder.encode(anyString())).thenReturn("encodedPassword");
			
			AppUser actualAppUser = appUserMapperService.requestDtoToEntity(requestDTO);
			
			assertEquals(requestDTO.getName(), actualAppUser.getName());
			assertEquals("encodedPassword", actualAppUser.getPassword());
			assertEquals(requestDTO.getRolename(), actualAppUser.getRole().getName());
			verify(bCryptPasswordEncoder).encode(requestDTO.getPassword());
		}
	}
	
	@Nested
	class EntityToResponseDto {
		
		@Test
		void entityIsNull_returnNull() {
			AppUserResponseDTO actualResponseDTO = appUserMapperService.entityToResponseDto(null);
			
			assertNull(actualResponseDTO);
		}
		
		@Test
		void returnResponseDTO() {
			AppUser appUser = AppUserTestHelper.getMockAppUser(1, "name", "password");
			
			AppUserResponseDTO actualResponseDTO = appUserMapperService.entityToResponseDto(appUser);
			
			assertThat(AppUserTestHelper.isEqual(appUser, actualResponseDTO)).isTrue();
		}		
	}
	
	@Nested
	class EntitiesToResponseDtos {
		
		@Test
		void entitiesIsNull_returnEmptyList() {
			List<AppUserResponseDTO> actualResponseDTOs = appUserMapperService.entitiesToResponseDtos(null);
			
			assertEquals(Collections.emptyList(), actualResponseDTOs);
		}
		
		@Test
		void entitiesIsEmpty_returnEmptyList() {
			List<AppUserResponseDTO> actualResponseDTOs = appUserMapperService.entitiesToResponseDtos(
					Collections.emptyList());
			
			assertEquals(Collections.emptyList(), actualResponseDTOs);
		}
		
		@Test
		void returnResponseDTOs() {
			AppUser appUser1 = AppUserTestHelper.getMockAppUser(1, "name1", "password1");
			AppUser appUser2 = AppUserTestHelper.getMockAppUser(2, "name2", "password2");
			List<AppUser> appUsers = List.of(appUser1, appUser2);
			
			List<AppUserResponseDTO> actualResponseDTOs = appUserMapperService.entitiesToResponseDtos(appUsers);
			
			assertEquals(appUsers.size(), actualResponseDTOs.size());
			assertThat(AppUserTestHelper.isEqual(appUsers.get(0), actualResponseDTOs.get(0))).isTrue();
			assertThat(AppUserTestHelper.isEqual(appUsers.get(1), actualResponseDTOs.get(1))).isTrue();			
		}
	}
	
	@Nested
	class ApplyUpdateDtoToEntity {

		@Test
		void entityIsNull_returnNull() {
			AppUserUpdateDTO updateDTO = new AppUserUpdateDTO("name", "roleName");

			AppUser actualAppUser = appUserMapperService.applyUpdateDtoToEntity(null, updateDTO);

			assertNull(actualAppUser);
		}

		@Test
		void updateDTOIsNull_returnNull() {
			AppUser appUser = AppUserTestHelper.getMockAppUser(1, "name", "password");

			AppUser actualAppUser = appUserMapperService.applyUpdateDtoToEntity(appUser, null);

			assertNull(actualAppUser);
		}

		@Test
		void roleNotFound_throwGetGainzException() {
			AppUser appUser = AppUserTestHelper.getMockAppUser(1, "name", "password");
			AppUserUpdateDTO updateDTO = new AppUserUpdateDTO("name", "newRoleName");

			when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());

			assertThrows(GetGainzException.class, () ->
					appUserMapperService.applyUpdateDtoToEntity(appUser, updateDTO));
		}

		@Test
		void returnUpdatedAppUser() {
			Role role = RoleTestHelper.getMockRole(1, "roleName");
			AppUser appUser = AppUserTestHelper.getMockAppUser(1, "name", "password", role);
			AppUserUpdateDTO updateDTO = new AppUserUpdateDTO("name", "newRoleName");

			when(roleRepository.findByName(anyString())).thenReturn(Optional.of(role));

			AppUser actualAppUser = appUserMapperService.applyUpdateDtoToEntity(appUser, updateDTO);

			assertEquals(appUser.getName(), actualAppUser.getName());
			assertEquals(appUser.getRole().getName(), actualAppUser.getRole().getName());
		}
	}
}
