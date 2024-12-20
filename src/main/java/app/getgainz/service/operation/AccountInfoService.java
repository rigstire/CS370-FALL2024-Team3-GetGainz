package app.getgainz.service.operation;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import app.getgainz.dto.accountinfo.AccountInfoResponseDTO;
import app.getgainz.dto.accountinfo.AccountInfoUpdateDTO;
import app.getgainz.dto.appuser.AppUserResponseDTO;
import app.getgainz.dto.appuser.AppUserUpdateDTO;
import app.getgainz.exception.GetGainzException;
import app.getgainz.service.crud.AppUserCrudService;

@Service
public class AccountInfoService {
	
	private final AppUserCrudService appUserCrudService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	public AccountInfoService(AppUserCrudService appUserCrudService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.appUserCrudService = appUserCrudService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	public AccountInfoResponseDTO read(String name) {
		return new AccountInfoResponseDTO(appUserCrudService.readByName(name).getName());
	}
	
	public void update(String name, @Valid AccountInfoUpdateDTO accountInfoUpdateDTO) {
		AppUserResponseDTO appUserResponseDTO = appUserCrudService.readByName(name);
		if (appUserResponseDTO == null) {
			throw new GetGainzException("User not found with name: " + name);
		}
		if (!bCryptPasswordEncoder.matches(accountInfoUpdateDTO.getOldPassword(), appUserResponseDTO.getPassword())) {
			throw new GetGainzException("Old password is not correct.");
		}
		appUserCrudService.update(appUserResponseDTO.getId(), new AppUserUpdateDTO(appUserResponseDTO.getName(), 
				accountInfoUpdateDTO.getNewPassword(), appUserResponseDTO.getRolename()));
	}
}
