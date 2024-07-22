package community.basketballvillage.service;

import static community.basketballvillage.dummy.DummyObject.TEST_EMAIL;
import static community.basketballvillage.dummy.DummyObject.newUserForTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.doReturn;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.doThrow;

import community.basketballvillage.global.exception.ErrorCode;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import community.basketballvillage.domain.User;
import community.basketballvillage.dto.request.JoinDto;
import community.basketballvillage.dto.response.ResUserDto;
import community.basketballvillage.global.constant.Role;
import community.basketballvillage.global.exception.BusinessException;
import community.basketballvillage.repository.UserRepository;
import community.basketballvillage.validation.UserValidation;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final String JOIN_EMAIL = "admin123@naver.com";
    private static final String PASSWORD = "admin123";
    private static final String USERNAME ="adminHong";

    @Mock
    private UserRepository userRepository;

    @Spy
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void 정상_회원가입(){
        JoinDto joinDto = new JoinDto(
            USERNAME,
            JOIN_EMAIL,
            PASSWORD,
            Role.ADMIN
        );

        User buildUser = new User(USERNAME,
            JOIN_EMAIL,
            PASSWORD,
            Role.ADMIN,
            bCryptPasswordEncoder);

        doReturn(buildUser).when(userRepository).save(any(User.class));
        doReturn(Optional.of(buildUser)).when(userRepository).findByEmail(JOIN_EMAIL);

        //when
        ResUserDto resUserDto = userService.join(joinDto);
        Optional<User> byEmail = userRepository.findByEmail(JOIN_EMAIL);
        User foundUser = byEmail.get();

        //then
        assertThat(resUserDto.getEmail()).isEqualTo(JOIN_EMAIL);
        assertThat(resUserDto.getRole()).isEqualTo(Role.ADMIN);
        assertThat(bCryptPasswordEncoder.matches(PASSWORD,foundUser.getPassword())).isTrue();
        //verify
        //메소트 호출 횟수 검증
        verify(userRepository,times(1)).save(any(User.class));
    }

    @Test
    void 비정상_회원가입_중복이메일회원() {

        User firstUser = newUserForTest(TEST_EMAIL);

        JoinDto joinDto = new JoinDto(
            USERNAME,
            JOIN_EMAIL,
            PASSWORD,
            Role.ADMIN
        );

        doReturn(firstUser).when(userRepository).save(firstUser);
        doReturn(true).when(userRepository).existsByEmail(JOIN_EMAIL);

        //when
        userRepository.save(firstUser);

        //then
        assertThatThrownBy(() -> userService.join(joinDto))
            .isInstanceOf(BusinessException.class)
            .hasMessage(ErrorCode.EMAIL_DUPLICATION.getMessage());
    }
}