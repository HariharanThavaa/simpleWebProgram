package microservices.book.multiplication.service;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.User;
import microservices.book.multiplication.repository.MultiplicationResultAttemptRepository;
import microservices.book.multiplication.repository.UserRepository;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class MultiplicationServiceImplTest {

    private MultiplicationServiceImpl multiplicationService;

    @Mock
    private RandomGeneratorServiceImpl randomGeneratorService;

    @Mock
    private MultiplicationResultAttemptRepository attemptRepository;

    @Mock
    private UserRepository userRepository;

    @Before
    public void setup() {
        // With this call to initMocks we tell Mockito to process the annotation
        initMocks(this);
        multiplicationService =
                new MultiplicationServiceImpl(randomGeneratorService, attemptRepository, userRepository);
    }

    @Test
    public void createRandomMultiplication() {

        // given (our mocked Random Generator service will return first 50 then 30)
        given(randomGeneratorService.generateRandomFactor()).willReturn(50, 30);

        // when
        Multiplication multiplication = multiplicationService.createRandomMultiplication();

        // then
        assertThat(multiplication.getFactorA()).isEqualTo(50);
        assertThat(multiplication.getFactorB()).isEqualTo(30);
    }

    @Test
    public void checkCorrectAttemptTest() {
        // given
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("Hariharan");
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication, 3000, false);
        MultiplicationResultAttempt verifiedAttempt = new MultiplicationResultAttempt(user, multiplication, 3000, true);
        given(userRepository.findByAlias("Hariharan")).willReturn(Optional.empty());

        // when
        boolean attemptResult = multiplicationService.checkAttempt(attempt);

        // assert
        assertThat(attemptResult).isTrue();
        verify(attemptRepository).save(verifiedAttempt);
    }

    @Test
    public void checkWrongAttemptTest() {
        // given
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("Hariharan");
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication, 3010, false);
        given(userRepository.findByAlias("Hariharan")).willReturn(Optional.empty());

        // when
        boolean attemptResult = multiplicationService.checkAttempt(attempt);

        // assert
        assertThat(attemptResult).isFalse();
        verify(attemptRepository).save(attempt);
    }

    @Test
    public void retreiveStatsTest() {
        // given
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("Hariharan");
        MultiplicationResultAttempt attempt1 = new MultiplicationResultAttempt(user, multiplication, 3010, false);
        MultiplicationResultAttempt attempt2 = new MultiplicationResultAttempt(user, multiplication, 3051, false);
        List<MultiplicationResultAttempt> latestAttempts = Lists.newArrayList(attempt1, attempt2);
        given(userRepository.findByAlias("Hariharan")).willReturn(Optional.empty());
        given(attemptRepository.findTop5ByUserAliasOrderByIdDesc("Hariharan")).willReturn(latestAttempts);

        // When
        List<MultiplicationResultAttempt> latestAttemptsResult = multiplicationService.getStatsForUser("Hariharan");

        // then
        assertThat(latestAttemptsResult).isEqualTo(latestAttemptsResult);
    }
}
