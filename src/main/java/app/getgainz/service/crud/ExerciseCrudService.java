package app.getgainz.service.crud;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import app.getgainz.dto.exercise.ExerciseRequestDTO;
import app.getgainz.dto.exercise.ExerciseResponseDTO;
import app.getgainz.dto.exercise.ExerciseUpdateDTO;
import app.getgainz.entity.Exercise;
import app.getgainz.exception.GetGainzException;
import app.getgainz.repository.ExerciseRepository;
import app.getgainz.service.mapper.ExerciseMapperService;

@Service
public class ExerciseCrudService implements CrudService<ExerciseRequestDTO, ExerciseResponseDTO, ExerciseUpdateDTO> {
	
	private final ExerciseRepository exerciseRepository;
	private final ExerciseMapperService exerciseMapperService;
	
	@Autowired
	public ExerciseCrudService(ExerciseRepository exerciseRepository,
								ExerciseMapperService exerciseMapperService) {
		this.exerciseRepository = exerciseRepository;
		this.exerciseMapperService = exerciseMapperService;
	}
	
	@Override
	public ExerciseResponseDTO create(ExerciseRequestDTO requestDTO) {
		if (requestDTO == null) {
			return null;
		}
		if (exerciseRepository.findByNameAndUserId(requestDTO.getName(), requestDTO.getAppUserId()).isPresent()) {
			throw new GetGainzException("Exercise name already exists.");
			
		}
		Exercise savedExercise = exerciseRepository.save(exerciseMapperService.requestDtoToEntity(requestDTO));
		return exerciseMapperService.entityToResponseDto(savedExercise);
	}
	
	@Override
	public ExerciseResponseDTO readById(Integer id) {
		Optional<Exercise> optionalExercise = exerciseRepository.findById(id);
		if (optionalExercise.isEmpty()) {
			return null;
		}
		return exerciseMapperService.entityToResponseDto(optionalExercise.get());		
	}
	
	@NotNull	
	public List<ExerciseResponseDTO> readMany(Integer appUserId) {		 
		return exerciseMapperService.entitiesToResponseDtos(exerciseRepository.findAllByUserId(appUserId));
	}
	
	@Override
	public ExerciseResponseDTO update(Integer id, ExerciseUpdateDTO updateDTO) {
		if (updateDTO == null) {
			return null;
		}
		Optional<Exercise> optionalExistingExercise = exerciseRepository.findById(id);
		if (optionalExistingExercise.isEmpty()) {
			return null;
		}
		if (!optionalExistingExercise.get().getName().equals(updateDTO.getName()) &&
			exerciseRepository.findByName(updateDTO.getName()).isPresent()) {
				throw new GetGainzException("Exercise name already exists.");
		}		
		Exercise savedExercise = exerciseRepository.save(
				exerciseMapperService.applyUpdateDtoToEntity(optionalExistingExercise.get(), updateDTO));
		return exerciseMapperService.entityToResponseDto(savedExercise);
	}
	
	@Override
	public void delete(Integer id) {
		try {
			exerciseRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new GetGainzException("Exercise cannot be deleted, it's in use.");
		}
	}	
	
}
