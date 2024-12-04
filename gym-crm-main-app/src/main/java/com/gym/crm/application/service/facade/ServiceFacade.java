package com.gym.crm.application.service.facade;

import com.gym.crm.application.dto.authentication.AuthenticationInfo;
import com.gym.crm.application.dto.request.TraineeTrainingsListRequest;
import com.gym.crm.application.dto.request.TrainerTrainingsListRequest;
import com.gym.crm.application.entity.Trainee;
import com.gym.crm.application.entity.Trainer;
import com.gym.crm.application.entity.Training;
import com.gym.crm.application.entity.User;
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
import com.gym.crm.application.rest.dto.TrainerTrainingsListItem;
import com.gym.crm.application.rest.dto.TrainerUpdateProfileRequest;
import com.gym.crm.application.rest.dto.TrainerProfile;
import com.gym.crm.application.rest.dto.TrainerProfileResponse;
import com.gym.crm.application.rest.dto.TrainingTypeListResponseInner;
import com.gym.crm.application.rest.dto.UpdateTraineeProfile200Response;
import com.gym.crm.application.rest.dto.UpdateTrainerProfile200Response;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Set;

public interface ServiceFacade {

    LoginCredentials createTrainee(TraineeProfile registrationRequest);

    Trainee findTrainee(long id, AuthenticationInfo authenticationInfo);

    TraineeProfileResponse findTrainee(String username);

    UpdateTraineeProfile200Response updateTraineeProfile(String username, TraineeUpdateProfileRequest profileRequest);

    void deleteTrainee(String username);

    List<TrainerItem> getTrainersListThatNotAssigned(String traineeUsername);

    List<TrainerItem> updateTrainersList(String traineeUsername, Set<TraineeTrainerListUpdateRequestInner> trainersList);

    LoginCredentials createTrainer(TrainerProfile registrationRequest);

    Trainer findTrainer(long id, AuthenticationInfo authenticationInfo);

    TrainerProfileResponse findTrainer(String username);

    UpdateTrainerProfile200Response updateTrainerProfile(String username, TrainerUpdateProfileRequest profileRequest);

    void createTraining(AddTrainingRequest addRequest);

    Training findTraining(long id, AuthenticationInfo authenticationInfo);

    List<TraineeTrainingsListItem> getTraineeTrainingsList(TraineeTrainingsListRequest trainingsListRequest);

    List<TrainerTrainingsListItem> getTrainerTrainingsList(TrainerTrainingsListRequest trainingsListRequest);

    List<TrainingTypeListResponseInner> getTrainingTypes();

    User createUser(User user);

    User findUser(long id);

    User findUser(String username);

    void updateUser(User user);

    void login(LoginCredentials loginCredentials, HttpSession session);

    void changeUserLogin(LoginChangeRequest request);

    void activateUser(String username, ActivateDeactivateRequest isActive);

    void deleteUser(User user);

}
