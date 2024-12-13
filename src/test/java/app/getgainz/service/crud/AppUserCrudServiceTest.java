package app.getgainz.service.crud;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import app.getgainz.dto.appuser.AppUserRequestDTO;
import app.getgainz.repository.AppUserRepository;
import app.getgainz.service.mapper.AppUserMapperService;
import app.getgainz.dto.appuser.AppUserUpdateDTO;
import app.getgainz.entity.AppUser;
import app.getgainz.exception.GetGainzException;
import app.getgainz.testhelper.AppUserTestHelper;

@ExtendWith(MockitoExtension.class)
class AppUserCrudServiceTest {
	
	@InjectMocks
	AppUserCrudService appUserCrudService;
	
	@Mock
	AppUserRepository appUserRepository;
	
	@Mock
	AppUserMapperService appUserMapperService;
	
	@Nested
	class Create {
		
		@Test
		void requestDTOIsNull_returnNull() {			
			assertNull(appUserCrudService.create(null));
		}
		
		@Test
		void nameAlreadyExists_throwGetGainzException() {
			AppUserRequestDTO appUserRequestDTO = new AppUserRequestDTO("name", "password", "roleName");
			AppUser appUser = AppUserTestHelper.getMockAppUser();
			
			when(appUserRepository.findByName(anyString())).thenReturn(Optional.of(appUser));
			
			assertThrows(GetGainzException.class, () -> appUserCrudService.create(appUserRequestDTO));
		}
		
		@Test
		void callSave() {
			AppUserRequestDTO appUserRequestDTO = new AppUserRequestDTO("name", "password", "roleName");
			AppUser appUser = AppUserTestHelper.getMockAppUser();
			
			when(appUserRepository.findByName(anyString())).thenReturn(Optional.empty());
			when(appUserMapperService.requestDtoToEntity(appUserRequestDTO)).thenReturn(appUser);
			
			appUserCrudService.create(appUserRequestDTO);
			
			verify(appUserRepository).save(appUser);
		}
	}
	
	@Nested
	class ReadById {

		@Test
		void notFoundById_returnNull() {
			when(appUserRepository.findById(anyInt())).thenReturn(Optional.empty());
			
			assertNull(appUserCrudService.readById(1));			
		}
	}
	
	@Nested
	class ReadByName {

		@Test
		void notFoundByName_returnNull() {
			when(appUserRepository.findByName(anyString())).thenReturn(Optional.empty());

			assertNull(appUserCrudService.readByName("name"));
		}
	}
	
	@Nested
	class ReadAll {
		
		@Test
		void callAppUserRepository() {
			appUserCrudService.readAll();
			
			verify(appUserRepository).findAll();		
		}
	}
	
	@Nested
	class Update {
		
		@Test
		void updateDTOIsNull_returnNull() {			
			assertNull(appUserCrudService.update(1, null));
		}
		
		@Test
		void existingAppUserNotFound_returnNull() {
			AppUserUpdateDTO updateDTO = new AppUserUpdateDTO("name", "roleName");
			
			when(appUserRepository.findById(anyInt())).thenReturn(Optional.empty());
			
			assertNull(appUserCrudService.update(1, updateDTO));
		}
		
		@Test
		void nameAlreadyExists_throwGetGainzException() {
			AppUserUpdateDTO updateDTO = new AppUserUpdateDTO("name", "roleName");			
			AppUser appUser = AppUserTestHelper.getMockAppUser();
			
			when(appUserRepository.findById(anyInt())).thenReturn(Optional.of(appUser));
			when(appUserRepository.findByName(anyString())).thenReturn(Optional.of(appUser));
			
			assertThrows(GetGainzException.class, () -> appUserCrudService.update(1, updateDTO));
		}
		
		@Test
		void applyAndSave() {
			AppUserUpdateDTO updateDTO = new AppUserUpdateDTO("name", "roleName");
			AppUser appUser = AppUserTestHelper.getMockAppUser();
			
			when(appUserRepository.findById(anyInt())).thenReturn(Optional.of(appUser));
			when(appUserRepository.findByName(anyString())).thenReturn(Optional.empty());
			when(appUserMapperService.applyUpdateDtoToEntity(any(AppUser.class), 
					any(AppUserUpdateDTO.class))).thenReturn(appUser);			
			
			appUserCrudService.update(1, updateDTO);
			
			verify(appUserMapperService).applyUpdateDtoToEntity(appUser, updateDTO);
			verify(appUserRepository).save(appUser);
		}
	}
	
	@Nested
	class Delete {
		
		@Test
		void callDeleteById() {
			appUserCrudService.delete(111);
			
			verify(appUserRepository).deleteById(111);
		}
	}
}
