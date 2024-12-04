package com.gym.crm.application.service.facade;

import com.gym.crm.application.dto.authentication.AuthenticationInfo;
import com.gym.crm.application.dto.request.TraineeTrainingsListRequest;
import com.gym.crm.application.dto.request.TrainerTrainingsListRequest;
import com.gym.crm.application.dto.request.TrainingAddRequest;
import com.gym.crm.application.entity.Trainee;
import com.gym.crm.application.entity.Trainer;
import com.gym.crm.application.entity.Training;
import com.gym.crm.application.entity.User;
import com.gym.crm.application.exception.AuthenticationException;
import com.gym.crm.application.exception.NotFoundException;
import com.gym.crm.application.mapper.TraineeMapper;
import com.gym.crm.application.mapper.TrainerMapper;
import com.gym.crm.application.mapper.TrainingTypeMapper;
import com.gym.crm.application.mapper.TrainingsListMapper;
import com.gym.crm.application.rest.dto.ActivateDeactivateRequest;
import com.gym.crm.application.rest.dto.AddTrainingRequest;
import com.gym.crm.application.rest.dto.LoginChangeRequest;
import com.gym.crm.application.rest.dto.LoginCredentials;
import com.gym.crm.application.rest.dto.TraineeProfile;
import com.gym.crm.application.rest.dto.TraineeProfileResponse;
import com.gym.crm.application.rest.dto.TraineeTrainerListUpdateRequestInner;
import com.gym.crm.application.rest.dto.TraineeTrainingsListItem;
import com.gym.crm.application.rest.dto.TraineeUpdateProfileRequest;
import com.gym.crm.application.rest.dto.TrainerItem;
import com.gym.crm.application.rest.dto.TrainerProfile;
import com.gym.crm.application.rest.dto.TrainerProfileResponse;
import com.gym.crm.application.rest.dto.TrainerTrainingsListItem;
import com.gym.crm.application.rest.dto.TrainerUpdateProfileRequest;
import com.gym.crm.application.rest.dto.TrainingTypeListResponseInner;
import com.gym.crm.application.rest.dto.UpdateTraineeProfile200Response;
import com.gym.crm.application.rest.dto.UpdateTrainerProfile200Response;
import com.gym.crm.application.service.TraineeService;
import com.gym.crm.application.service.TrainerService;
import com.gym.crm.application.service.TrainingService;
import com.gym.crm.application.service.TrainingTypeService;
import com.gym.crm.application.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceFacadeImpl implements ServiceFacade {

    private final UserService userService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;
    private final TrainingTypeService trainingTypeService;

    @Override
    public LoginCredentials createTrainee(TraineeProfile registrationRequest) {
        Trainee traineeRequest = TraineeMapper.INSTANCE.toEntity(registrationRequest);

        AuthenticationInfo newAuth = traineeService.createTrainee(traineeRequest);

        return new LoginCredentials(newAuth.getUsername(), newAuth.getPassword());
    }

    @Override
    public Trainee findTrainee(long id, AuthenticationInfo authenticationInfo) {
        return traineeService.findById(id);
    }

    @Override
    public TraineeProfileResponse findTrainee(String username) {
        Trainee foundTrainee = traineeService.findByUsername(username);

        return TraineeMapper.INSTANCE.toTraineeProfileResponse(foundTrainee);
    }

    @Override
    public UpdateTraineeProfile200Response updateTraineeProfile(String username,
                                                                TraineeUpdateProfileRequest profileRequest) {
        Trainee traineeRequest = TraineeMapper.INSTANCE.fromTraineeUpdateProfileRequest(profileRequest);

        Trainee updatedTrainee = traineeService.updateTraineeProfile(username, traineeRequest);

        return TraineeMapper.INSTANCE.toUpdateTraineeProfile200Response(updatedTrainee);
    }

    @Override
    public void deleteTrainee(String username) {
        traineeService.delete(Optional.ofNullable(traineeService.findByUsername(username))
                .orElseThrow(() -> new NotFoundException("Trainee not found")));
    }

    @Override
    public List<TrainerItem> getTrainersListThatNotAssigned(String traineeUsername) {
        List<Trainer> trainersThatNotAssigned = traineeService.getTrainersListThatNotAssigned(traineeUsername);

        return trainersThatNotAssigned.stream().map(TraineeMapper.INSTANCE::toTrainerItem).toList();
    }

    @Override
    public List<TrainerItem> updateTrainersList(String traineeUsername,
                                                Set<TraineeTrainerListUpdateRequestInner> trainersList) {
        Set<String> trainersUsernamesList = trainersList.stream()
                .map(TraineeTrainerListUpdateRequestInner::getUsername).collect(Collectors.toSet());

        List<Trainer> updatedTrainersList = traineeService.updateTrainersList(traineeUsername, trainersUsernamesList);

        return updatedTrainersList.stream().map(TraineeMapper.INSTANCE::toTrainerItem).toList();
    }

    @Override
    public LoginCredentials createTrainer(TrainerProfile registrationRequest) {
        Trainer trainerRequest = TrainerMapper.INSTANCE.fromTrainerProfile(registrationRequest);

        AuthenticationInfo newAuth = trainerService.createTrainer(trainerRequest);

        return new LoginCredentials(newAuth.getUsername(), newAuth.getPassword());
    }

    @Override
    public Trainer findTrainer(long id, AuthenticationInfo authenticationInfo) {
        return trainerService.findById(id);
    }

    @Override
    public TrainerProfileResponse findTrainer(String username) {
        Trainer foundTrainer = trainerService.findByUsername(username);

        return TrainerMapper.INSTANCE.toTrainerProfileResponse(foundTrainer);
    }

    @Override
    public UpdateTrainerProfile200Response updateTrainerProfile(String username,
                                                                TrainerUpdateProfileRequest profileRequest) {
        Trainer trainerRequest = TrainerMapper.INSTANCE.fromTrainerUpdateProfileRequest(profileRequest);

        Trainer updatedTrainer = trainerService.updateTrainerProfile(username, trainerRequest);

        return TrainerMapper.INSTANCE.toUpdateTrainerProfile200Response(updatedTrainer);
    }

    @Override
    public void createTraining(AddTrainingRequest addRequest) {
        TrainingAddRequest trainingAddRequest = new TrainingAddRequest(
                addRequest.getTraineeUsername(),
                addRequest.getTrainerUsername(),
                addRequest.getTrainingName(),
                addRequest.getTrainingDate(),
                addRequest.getTrainingDuration()
        );

        trainingService.createTraining(trainingAddRequest);
    }

    @Override
    public Training findTraining(long id, AuthenticationInfo authenticationInfo) {
        return trainingService.findById(id);
    }

    @Override
    public List<TraineeTrainingsListItem> getTraineeTrainingsList(TraineeTrainingsListRequest trainingsListRequest) {
        return trainingService.findByCriteria(TrainingsListMapper.INSTANCE
                .fromTraineeRequest(trainingsListRequest)).stream()
                .map(TrainingsListMapper.INSTANCE::toTraineeTrainingsListItem).toList();
    }

    @Override
    public List<TrainerTrainingsListItem> getTrainerTrainingsList(TrainerTrainingsListRequest trainingsListRequest) {
        return trainingService.findByCriteria(TrainingsListMapper.INSTANCE.fromTrainerRequest(trainingsListRequest)).stream().map(TrainingsListMapper.INSTANCE::toTrainerTrainingsListItem).toList();
    }

    @Override
    public List<TrainingTypeListResponseInner> getTrainingTypes() {
        return trainingTypeService.getAll().stream().map(TrainingTypeMapper.INSTANCE::from).collect(Collectors.toList());
    }

    @Override
    public User createUser(User user) {
        return userService.save(user);
    }

    @Override
    public User findUser(long id) {
        return userService.findById(id);
    }

    @Override
    public User findUser(String username) {
        User foundUser = userService.findByUsername(username);

        if (foundUser == null) {
            log.warn("User not found");

            return null;
        }

        return foundUser;
    }

    @Override
    public void updateUser(User user) {
        userService.update(user.toBuilder().username(userService.findById(user.getId()).getUsername()).build());
    }

    @Override
    public void login(LoginCredentials loginCredentials, HttpSession session) {
        Optional.ofNullable(userService.findByUsername(loginCredentials.getUsername())).orElseThrow(() -> new AuthenticationException("User not found"));

        AuthenticationInfo auth = new AuthenticationInfo(loginCredentials.getUsername(), loginCredentials.getPassword());

        session.setAttribute("userAuth", auth);
    }

    @Override
    public void changeUserLogin(LoginChangeRequest request) {
        if (request.getOldPassword().equals(request.getNewPassword())) {
            throw new AuthenticationException("New password cannot be equal old password");
        }

        if (!userService.isAuthenticate(request.getUsername(), request.getOldPassword())) {
            throw new AuthenticationException("Password not match");
        }

        User user = findUser(request.getUsername());

        userService.changePassword(user, request.getNewPassword());

        // TODO Logout
    }

    @Override
    public void activateUser(String username, ActivateDeactivateRequest isActive) {
        userService.activateUser(username, isActive.getIsActive());
    }

    @Override
    public void deleteUser(User user) {
        userService.delete(user);
    }

}
