package app.getgainz.service.crud;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.getgainz.dto.role.RoleRequestDTO;
import app.getgainz.dto.role.RoleResponseDTO;
import app.getgainz.dto.role.RoleUpdateDTO;
import app.getgainz.entity.Role;
import app.getgainz.exception.GetGainzException;
import app.getgainz.repository.RoleRepository;
import app.getgainz.service.mapper.RoleMapperService;

@Service
public class RoleCrudService implements CrudService<RoleRequestDTO, RoleResponseDTO, RoleUpdateDTO> {
	
	private final RoleRepository roleRepository;
	private final RoleMapperService roleMapperService;
	
	@Autowired
	public RoleCrudService(RoleRepository roleRepository, RoleMapperService roleMapperService) {		
		this.roleRepository = roleRepository;
		this.roleMapperService = roleMapperService;
	}
	
	@Override
	public RoleResponseDTO create(RoleRequestDTO requestDTO) {
		if (requestDTO == null) {
			return null;
		}
		if (roleRepository.findByName(requestDTO.getName()).isPresent()) {
			throw new GetGainzException("Role name already exists.");
		}
		Role savedRole = roleRepository.save(roleMapperService.requestDtoToEntity(requestDTO));
		return roleMapperService.entityToResponseDto(savedRole);
	}
	
	@Override
	public RoleResponseDTO readById(Integer id) {
		Optional<Role> optionalRole = roleRepository.findById(id);
		if (optionalRole.isEmpty()) {
			return null;
		}
		return roleMapperService.entityToResponseDto(optionalRole.get());
	}
	
	public RoleResponseDTO readByName(String name) {
		Optional<Role> optionalRole = roleRepository.findByName(name);
		if (optionalRole.isEmpty()) {
			return null;
		}
		return roleMapperService.entityToResponseDto(optionalRole.get());
	}
	
	@NotNull
	public List<RoleResponseDTO> readAll() {
		return roleMapperService.entitiesToResponseDtos((List<Role>) roleRepository.findAll());
	}
	
	@Override
	public RoleResponseDTO update(Integer id, RoleUpdateDTO updateDTO) {
		if (updateDTO == null) {
			return null;
		}
		Optional<Role> optionalExistingRole = roleRepository.findById(id);
		if (optionalExistingRole.isEmpty()) {
			return null;
		}
		if (!optionalExistingRole.get().getName().equals(updateDTO.getName()) &&
			roleRepository.findByName(updateDTO.getName()).isPresent()) {
				throw new GetGainzException("Role name already exists.");
		}
		Role savedRole = roleRepository.save(
				roleMapperService.applyUpdateDtoToEntity(optionalExistingRole.get(), updateDTO));
		return roleMapperService.entityToResponseDto(savedRole);
	}
	
	@Override
	public void delete(Integer id) {
		roleRepository.deleteById(id);		
	}

}
