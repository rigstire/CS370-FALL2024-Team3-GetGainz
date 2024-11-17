package app.getgainz.controller.crud;

import app.getgainz.dto.exercise.ExerciseRequestDTO;
import app.getgainz.dto.exercise.ExerciseResponseDTO;
import app.getgainz.dto.exercise.ExerciseUpdateDTO;
import app.getgainz.exception.GetGainzException;
import app.getgainz.security.AppUserPrincipal;
import app.getgainz.service.crud.ExerciseCrudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/user/exercises")
@PreAuthorize("authenticated")
public class ExerciseController {
	
	private final Logger logger;
	private final ExerciseCrudService exerciseCrudService;
	
	@Autowired
	public ExerciseController(ExerciseCrudService exerciseCrudService) {
		this.exerciseCrudService = exerciseCrudService;
		this.logger = LoggerFactory.getLogger(ExerciseController.class);
	}
	
	@PostMapping
	public void create(@RequestBody @Valid ExerciseRequestDTO exerciseRequestDTO,
			@AuthenticationPrincipal AppUserPrincipal appUserPrincipal) {
		exerciseRequestDTO.setAppUserId(appUserPrincipal.getId());
		exerciseCrudService.create(exerciseRequestDTO);
		logger.info("Creating new exercise: {}", exerciseRequestDTO);
	}

	@GetMapping
	public List<ExerciseResponseDTO> readAll(@AuthenticationPrincipal AppUserPrincipal appUserPrincipal) {		
		List<ExerciseResponseDTO> responseDTOs = exerciseCrudService.readMany(appUserPrincipal.getId());
		logger.info("Sending a list of exercises: {}", responseDTOs);
		return responseDTOs;
	}
	
	@PutMapping("{id}")
	public void update(@PathVariable("id") @NotNull Integer exerciseId, 
			@Valid @RequestBody ExerciseUpdateDTO exerciseUpdateDTO,
			@AuthenticationPrincipal AppUserPrincipal appUserPrincipal) {		
		ExerciseResponseDTO exerciseResponseDTO = exerciseCrudService.readById(exerciseId);
		if (exerciseResponseDTO != null && exerciseResponseDTO.getAppUserId().equals(appUserPrincipal.getId())) {
			exerciseCrudService.update(exerciseId, exerciseUpdateDTO);
			logger.info("Updating the exercise: {}", exerciseUpdateDTO);
		} else {
			throw new GetGainzException("UserIds doesn't match. Cannot update others Exercise.");
		}
	}
	
	@DeleteMapping("{id}")
	public void delete(@PathVariable("id") @NotNull Integer exerciseId,
			@AuthenticationPrincipal AppUserPrincipal appUserPrincipal) {
		ExerciseResponseDTO exerciseResponseDTO = exerciseCrudService.readById(exerciseId);
		if (exerciseResponseDTO != null && exerciseResponseDTO.getAppUserId().equals(appUserPrincipal.getId())) {
			exerciseCrudService.delete(exerciseId);
			logger.info("Deleting exercise: {}", exerciseResponseDTO);
		} else {
			throw new GetGainzException("UserIds doesn't match. Cannot delete others Exercise.");
		}
	}

}
