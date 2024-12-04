package com.gym.crm.application.service.facade;

import com.gym.crm.application.dto.authentication.AuthenticationInfo;
import com.gym.crm.application.dto.request.TraineeTrainingsListRequest;
import com.gym.crm.application.dto.request.TrainerTrainingsListRequest;
import com.gym.crm.application.dto.request.TrainingAddRequest;
import com.gym.crm.application.entity.Trainee;
import com.gym.crm.application.entity.Trainer;
import com.gym.crm.application.entity.Training;
import com.gym.crm.application.entity.TrainingType;
import com.gym.crm.application.entity.User;
import com.gym.crm.application.exception.AuthenticationException;
import com.gym.crm.application.mapper.TraineeMapper;
import com.gym.crm.application.rest.dto.AddTrainingRequest;
import com.gym.crm.application.rest.dto.LoginChangeRequest;
import com.gym.crm.application.rest.dto.LoginCredentials;
import com.gym.crm.application.rest.dto.TraineeProfileResponse;
import com.gym.crm.application.rest.dto.TraineeTrainerListUpdateRequestInner;
import com.gym.crm.application.rest.dto.TraineeTrainingsListItem;
import com.gym.crm.application.rest.dto.TraineeUpdateProfileRequest;
import com.gym.crm.application.rest.dto.TrainerItem;
import com.gym.crm.application.rest.dto.TrainerTrainingsListItem;
import com.gym.crm.application.rest.dto.TrainingTypeListResponseInner;
import com.gym.crm.application.rest.dto.UpdateTraineeProfile200Response;
import com.gym.crm.application.service.TraineeService;
import com.gym.crm.application.service.TrainerService;
import com.gym.crm.application.service.TrainingService;
import com.gym.crm.application.service.TrainingTypeService;
import com.gym.crm.application.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.gym.crm.application.testdata.EntityTestData.TRAINEE;
import static com.gym.crm.application.testdata.EntityTestData.TRAINER;
import static com.gym.crm.application.testdata.EntityTestData.TRAINING;
import static com.gym.crm.application.testdata.EntityTestData.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServiceFacadeTest {

    @Mock
    private UserService userService;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingService trainingService;

    @Mock
    private TrainingTypeService trainingTypeService;

    @InjectMocks
    private ServiceFacadeImpl serviceFacade;

    @Test
    @DisplayName("Should select trainee")
    void shouldSelectTrainee() {
        AuthenticationInfo auth = new AuthenticationInfo("username", "password");
        when(traineeService.findById(1L)).thenReturn(TRAINEE);

        Trainee actualTrainee = serviceFacade.findTrainee(1L, auth);

        verify(traineeService).findById(1L);
        assertEquals(TRAINEE, actualTrainee);
    }

    @Test
    void shouldFindTraineeSuccess() {
        String username = "testUser";

        when(traineeService.findByUsername(username)).thenReturn(TRAINEE);

        TraineeProfileResponse actualTrainee = serviceFacade.findTrainee(username);

        assertNotNull(actualTrainee);
        verify(traineeService).findByUsername(username);
    }

    @Test
    void shouldFindTrainerByAuthenticationInfo() {
        when(trainerService.findByUsername(TRAINER.getUser().getUsername())).thenReturn(TRAINER);

        serviceFacade.findTrainer(TRAINER.getUser().getUsername());

        verify(trainerService).findByUsername(TRAINER.getUser().getUsername());
    }

    @Test
    @DisplayName("Should delete trainee")
    void shouldDeleteTrainee() {
        when(traineeService.findByUsername(anyString())).thenReturn(TRAINEE);

        serviceFacade.deleteTrainee("username");

        verify(traineeService).delete(TRAINEE);
    }

    @Test
    @DisplayName("Should select trainer")
    void shouldSelectTrainer() {
        AuthenticationInfo auth = new AuthenticationInfo("username", "password");

        Trainer expectedTrainer = TRAINER;
        when(trainerService.findById(1L)).thenReturn(expectedTrainer);

        Trainer actualTrainer = serviceFacade.findTrainer(1L, auth);

        verify(trainerService).findById(1L);
        assertEquals(expectedTrainer, actualTrainer);
    }

    @Test
    void shouldCreateTrainingSuccess() {
        AddTrainingRequest addRequest = new AddTrainingRequest();
        addRequest.setTraineeUsername(TRAINEE.getUser().getUsername());
        addRequest.setTrainerUsername(TRAINER.getUser().getUsername());
        addRequest.setTrainingDate(LocalDate.now());
        addRequest.setTrainingName("Yoga Session");
        addRequest.setTrainingDuration(60);

        serviceFacade.createTraining(addRequest);

        verify(trainingService).createTraining(any(TrainingAddRequest.class));
    }

    @Test
    @DisplayName("Should select training")
    void shouldSelectTraining() {
        AuthenticationInfo auth = new AuthenticationInfo("username", "password");
        when(trainingService.findById(1L)).thenReturn(TRAINING);

        Training actualTraining = serviceFacade.findTraining(1L, auth);

        verify(trainingService).findById(1L);
        assertEquals(TRAINING, actualTraining);
    }

    @Test
    @DisplayName("Should create user")
    void shouldCreateUser() {
        User expectedUser = USER;
        when(userService.save(expectedUser)).thenReturn(expectedUser);

        User actualTrainee = serviceFacade.createUser(expectedUser);

        verify(userService).save(expectedUser);
        assertEquals(expectedUser, actualTrainee);
    }

    @Test
    @DisplayName("Should select user")
    void shouldSelectUser() {
        when(userService.findById(1L)).thenReturn(USER);

        User actualUser = serviceFacade.findUser(1L);

        verify(userService).findById(1L);
        assertEquals(USER, actualUser);
    }

    @Test
    @DisplayName("Should update user")
    void shouldUpdateUser() {
        when(userService.findById(any())).thenReturn(USER);

        serviceFacade.updateUser(USER);

        verify(userService).update(any());
    }

    @Test
    @DisplayName("Should delete user")
    void shouldDeleteUser() {
        serviceFacade.deleteUser(USER);

        verify(userService).delete(USER);
    }

    @Test
    void shouldGetTraineeTrainingsList() {
        TraineeTrainingsListRequest request = new TraineeTrainingsListRequest("trainee", null, null, null, null);
        Training training1 = new Training().toBuilder().trainingName("training1").build();
        Training training2 = new Training().toBuilder().trainingName("training2").build();
        int expectedListSize = 2;
        when(trainingService.findByCriteria(any())).thenReturn(List.of(training1, training2));

        List<TraineeTrainingsListItem> actualTraineeTrainingsList = serviceFacade.getTraineeTrainingsList(request);

        assertEquals(expectedListSize, actualTraineeTrainingsList.size());
        assertTrue(actualTraineeTrainingsList.stream()
                .anyMatch(training -> training1.getTrainingName().equals(training.getTrainingName())));
        assertTrue(actualTraineeTrainingsList.stream()
                .anyMatch(training -> training2.getTrainingName().equals(training.getTrainingName())));
        verify(trainingService).findByCriteria(any());
    }

    @Test
    void shouldGetTrainerTrainingsList() {
        TrainerTrainingsListRequest request = new TrainerTrainingsListRequest("trainer", null, null, null);
        Training training1 = new Training().toBuilder().trainingName("training1").build();
        Training training2 = new Training().toBuilder().trainingName("training2").build();
        int expectedListSize = 2;
        when(trainingService.findByCriteria(any())).thenReturn(List.of(training1, training2));

        List<TrainerTrainingsListItem> actualTrainerTrainingsList = serviceFacade.getTrainerTrainingsList(request);

        assertEquals(expectedListSize, actualTrainerTrainingsList.size());
        assertTrue(actualTrainerTrainingsList.stream()
                .anyMatch(training -> training1.getTrainingName().equals(training.getTrainingName())));
        assertTrue(actualTrainerTrainingsList.stream()
                .anyMatch(training -> training2.getTrainingName().equals(training.getTrainingName())));
        verify(trainingService).findByCriteria(any());
    }

    @Test
    void shouldGetTrainingTypes() {
        TrainingType trainingType1 = new TrainingType().toBuilder().trainingTypeName("Zumba").build();
        TrainingType trainingType2 = new TrainingType().toBuilder().trainingTypeName("Samba").build();
        int expectedTrainingTypeListSize = 2;
        when(trainingTypeService.getAll()).thenReturn(List.of(trainingType1, trainingType2));

        List<TrainingTypeListResponseInner> actualTrainingTypeList = serviceFacade.getTrainingTypes();

        assertEquals(expectedTrainingTypeListSize, actualTrainingTypeList.size());
        assertTrue(actualTrainingTypeList.stream()
                .anyMatch(tt -> trainingType1.getTrainingTypeName().equals(tt.getType())));
        assertTrue(actualTrainingTypeList.stream()
                .anyMatch(tt -> trainingType2.getTrainingTypeName().equals(tt.getType())));
        verify(trainingTypeService).getAll();
    }

    @Test
    void shouldFindUserByUsername() {
        AuthenticationInfo auth = new AuthenticationInfo("username", "password");
        User mockUser = new User();
        when(userService.findByUsername(auth.getUsername())).thenReturn(mockUser);

        User actualUser = serviceFacade.findUser(auth.getUsername());

        assertEquals(mockUser, actualUser);
        verify(userService).findByUsername(auth.getUsername());
    }

    @Test
    void shouldFindUserNotFound() {
        AuthenticationInfo auth = new AuthenticationInfo("username", "password");
        when(userService.findByUsername(auth.getUsername())).thenReturn(null);

        User actualUser = serviceFacade.findUser(auth.getUsername());

        assertNull(actualUser);
        verify(userService).findByUsername(auth.getUsername());
    }

    @Test
    void shouldLoginSuccessful() {
        LoginCredentials credentials = new LoginCredentials("username", "password");
        HttpSession mockSession = mock(HttpSession.class);
        User mockUser = new User();
        when(userService.findByUsername(credentials.getUsername())).thenReturn(mockUser);

        serviceFacade.login(credentials, mockSession);

        verify(mockSession).setAttribute(anyString(), any());
    }

    @Test
    void shouldLoginUserNotFound() {
        LoginCredentials credentials = new LoginCredentials("username", "password");
        HttpSession mockSession = mock(HttpSession.class);
        when(userService.findByUsername(credentials.getUsername())).thenReturn(null);

        assertThrows(AuthenticationException.class, () -> serviceFacade.login(credentials, mockSession));
    }

    @Test
    void shouldChangeUserLoginSuccess() {
        LoginChangeRequest loginChangeRequest = new LoginChangeRequest()
                .username("Ivan.Ivanoff")
                .oldPassword("oldPassword")
                .newPassword("newPassword");
        User mockUser = new User();
        when(userService.findByUsername(loginChangeRequest.getUsername())).thenReturn(mockUser);
        when(userService.isAuthenticate(loginChangeRequest.getUsername(), loginChangeRequest.getOldPassword()))
                .thenReturn(true);

        serviceFacade.changeUserLogin(loginChangeRequest);

        verify(userService).changePassword(mockUser, loginChangeRequest.getNewPassword());
    }

    @Test
    void shouldNotChangeUserLoginWhenSamePassword() {
        LoginChangeRequest loginChangeRequest = new LoginChangeRequest()
                .username("Ivan.Ivanoff")
                .oldPassword("password")
                .newPassword("password");

        assertThrows(AuthenticationException.class, () -> serviceFacade.changeUserLogin(loginChangeRequest));
    }

    @Test
    void shouldNotChangeUserLoginWhenIncorrectOldPassword() {
        LoginChangeRequest loginChangeRequest = new LoginChangeRequest()
                .username("Ivan.Ivanoff")
                .oldPassword("wrongPassword")
                .newPassword("newPassword");

        assertThrows(AuthenticationException.class, () -> serviceFacade.changeUserLogin(loginChangeRequest));
    }

    @Test
    void shouldReturnTrainerItemsWhenTrainersListIsValid() {
        String traineeUsername = "testTrainee";
        Set<TraineeTrainerListUpdateRequestInner> trainersList = Set.of(
                new TraineeTrainerListUpdateRequestInner().username("trainer.1"),
                new TraineeTrainerListUpdateRequestInner().username("trainer.2")
        );
        Set<String> trainersUsernamesList = trainersList.stream()
                .map(TraineeTrainerListUpdateRequestInner::getUsername).collect(Collectors.toSet());
        User user1 = User.builder().username("trainer.1").build();
        User user2 = User.builder().username("trainer.2").build();
        List<Trainer> trainers = List.of(
                Trainer.builder().user(user1).build(),
                Trainer.builder().user(user2).build()
        );
        int expectedTrainersListSize = 2;
        when(traineeService.updateTrainersList(traineeUsername, trainersUsernamesList)).thenReturn(trainers);

        List<TrainerItem> actualTrainersList = serviceFacade.updateTrainersList(traineeUsername, trainersList);

        assertEquals(expectedTrainersListSize, actualTrainersList.size());
        verify(traineeService).updateTrainersList(traineeUsername, trainersUsernamesList);
    }

    @Test
    void shouldReturnUpdatedProfileResponseWhenValidRequest() {
        String username = "testUser";
        TraineeUpdateProfileRequest profileRequest = new TraineeUpdateProfileRequest()
                .firstName(TRAINEE.getUser().getFirstName())
                .lastName(TRAINEE.getUser().getLastName())
                .dateOfBirth(TRAINEE.getDateOfBirth())
                .address(TRAINEE.getAddress());
        Trainee traineeRequest = TraineeMapper.INSTANCE.fromTraineeUpdateProfileRequest(profileRequest);
        Trainee updatedTrainee = TRAINEE;
        UpdateTraineeProfile200Response expectedResponse = TraineeMapper.INSTANCE.toUpdateTraineeProfile200Response(updatedTrainee);

        when(traineeService.updateTraineeProfile(username, traineeRequest)).thenReturn(updatedTrainee);

        UpdateTraineeProfile200Response actualResponse = serviceFacade.updateTraineeProfile(username, profileRequest);

        assertEquals(expectedResponse, actualResponse);
        verify(traineeService).updateTraineeProfile(username, traineeRequest);
    }

}