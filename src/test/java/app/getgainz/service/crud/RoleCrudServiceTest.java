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

import app.getgainz.dto.role.RoleRequestDTO;
import app.getgainz.dto.role.RoleUpdateDTO;
import app.getgainz.entity.Role;
import app.getgainz.exception.GetGainzException;
import app.getgainz.repository.RoleRepository;
import app.getgainz.service.mapper.RoleMapperService;
import app.getgainz.testhelper.RoleTestHelper;

@ExtendWith(MockitoExtension.class)
class RoleCrudServiceTest {
	
	@InjectMocks
	RoleCrudService roleCrudService;
	
	@Mock
	RoleRepository roleRepository;
	
	@Mock
	RoleMapperService roleMapperService;
	
	@Nested
	class Create {
		
		@Test
		void requestDTOIsNull_returnNull() {
			assertNull(roleCrudService.create(null));
		}
		
		@Test
		void nameAlreadyExists_throwGetGainzException() {
			RoleRequestDTO requestDTO = new RoleRequestDTO("roleName");
			Role role = RoleTestHelper.getMockRole();
			
			when(roleRepository.findByName(anyString())).thenReturn(Optional.of(role));
			
			assertThrows(GetGainzException.class, () -> roleCrudService.create(requestDTO));
		}
		
		@Test
		void callSave() {
			RoleRequestDTO requestDTO = new RoleRequestDTO("roleName");
			Role role = RoleTestHelper.getMockRole();
			
			when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());
			when(roleMapperService.requestDtoToEntity(requestDTO)).thenReturn(role);
			
			roleCrudService.create(requestDTO);
			
			verify(roleRepository).save(role);
		}		
	}
	
	@Nested
	class ReadById {
		
		@Test
		void notFoundById_returnNull() {
			when(roleRepository.findById(anyInt())).thenReturn(Optional.empty());
			
			assertNull(roleCrudService.readById(1));			
		}		
	}
	
	@Nested
	class ReadByName {
		
		@Test
		void notFoundByName_returnNull() {
			when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());
			
			assertNull(roleCrudService.readByName("roleName"));
		}		
	}
	
	@Nested
	class ReadAll {
		
		@Test
		void callAppUserRepository() {
			roleCrudService.readAll();
			
			verify(roleRepository).findAll();
		}		
	}
	
	@Nested
	class Update {
		
		@Test
		void updateDTOIsNull_returnNull() {	
			assertNull(roleCrudService.update(1, null));			
		}
		
		@Test
		void existingRoleNotFound_returnNull() {
			RoleUpdateDTO updateDTO = new RoleUpdateDTO("newRoleName");
			
			when(roleRepository.findById(anyInt())).thenReturn(Optional.empty());
			
			assertNull(roleCrudService.update(1, updateDTO));
		}
		
		@Test
		void nameAlreadyExists_throwGetGainzException() {
			RoleUpdateDTO updateDTO = new RoleUpdateDTO("newRoleName");
			Role role = RoleTestHelper.getMockRole();
			
			when(roleRepository.findById(anyInt())).thenReturn(Optional.of(role));
			when(roleRepository.findByName(anyString())).thenReturn(Optional.of(role));
			
			assertThrows(GetGainzException.class, () -> roleCrudService.update(1, updateDTO));
		}
		
		@Test
		void applyAndSave() {
			RoleUpdateDTO updateDTO = new RoleUpdateDTO("newRoleName");
			Role role = RoleTestHelper.getMockRole();
			
			when(roleRepository.findById(anyInt())).thenReturn(Optional.of(role));
			when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());
			when(roleMapperService.applyUpdateDtoToEntity(any(Role.class), any(RoleUpdateDTO.class))).thenReturn(role);
			
			roleCrudService.update(1, updateDTO);
			
			verify(roleMapperService).applyUpdateDtoToEntity(role, updateDTO);
			verify(roleRepository).save(role);						
		}		
	}
	
	@Nested
	class Delete {
		
		@Test
		void callDeleteById() {
			roleCrudService.delete(111);
			
			verify(roleRepository).deleteById(111);
		}
	}
}
