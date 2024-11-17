package app.getgainz.controller.operation;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.getgainz.dto.accountinfo.AccountInfoResponseDTO;
import app.getgainz.dto.accountinfo.AccountInfoUpdateDTO;
import app.getgainz.security.AppUserPrincipal;
import app.getgainz.service.operation.AccountInfoService;

@RestController
@RequestMapping("/user/account")
@PreAuthorize("authenticated")
public class AccountInfoController {
	
	private final AccountInfoService accountInfoService;
	
	@Autowired
	public AccountInfoController(AccountInfoService accountInfoService) {
		this.accountInfoService = accountInfoService;
	}
	
	@GetMapping
	public AccountInfoResponseDTO read(@AuthenticationPrincipal AppUserPrincipal appUserPrincipal) {		
		return accountInfoService.read(appUserPrincipal.getUsername());
	}
	
	@PutMapping
	public void update(@RequestBody @Valid AccountInfoUpdateDTO accountInfoUpdateDTO,
			@AuthenticationPrincipal AppUserPrincipal appUserPrincipal) {		
		accountInfoService.update(appUserPrincipal.getUsername(), accountInfoUpdateDTO);				
	}

}
