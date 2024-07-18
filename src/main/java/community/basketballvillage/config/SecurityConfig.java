package community.basketballvillage.config;

import community.basketballvillage.global.exception.ErrorCode;
import community.basketballvillage.global.exception.ResValidErrorDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import community.basketballvillage.config.jwt.JwtAuthenticationFilter;
import community.basketballvillage.config.jwt.JwtAuthorizationFilter;
import community.basketballvillage.config.jwt.JwtProcess;
import community.basketballvillage.global.constant.Role;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtProcess jwtProcess;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        log.debug("BCryptPasswordEncoder 빈 등록 완료");
        return new BCryptPasswordEncoder();
    }

    // JWT 서버를 만들 예정이므로 Session 사용안함.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.debug("filterChain 빈 등록 완료");

        // iframe 허용안함.
        http.headers(headers -> headers
            .frameOptions((frameOptions -> frameOptions
                .sameOrigin())));

        // enable이면 post맨 작동안함
        http
            .csrf((auth)-> auth.disable());

        //Form login disable
        // react, 앱으로 요청할 예정
        http
            .formLogin((auth)-> auth.disable());

        //http basic disable
        // httpBasic은 브라우저가 팝업창을 이용해서 사용자 인증을 진행한다.
        // 브라우저가 인증 요청 권한을 스스로 행사한다.(스스로 팝업창을 띄운다.)
        http
            .httpBasic((auth)-> auth.disable());

        //security6로 들어오면서 filter들을 세팅한 configurer 등록이 어렵게 되었다(apply가 사장됨)
        http
            .addFilterBefore(new JwtAuthenticationFilter(authenticationManager(authenticationConfiguration), jwtProcess),
                UsernamePasswordAuthenticationFilter.class);

        http
            .addFilter(new JwtAuthorizationFilter(authenticationManager(authenticationConfiguration), jwtProcess));

        //exception exceptionHandler로 책임 보내기
//        http
//            .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
//                httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(jwtAuthenticationEntryPoint);
//            });


        //exception 가로채기
        // 인증 실패

        http.exceptionHandling(e-> e.authenticationEntryPoint((request, response, authException) -> {
            log.info("exceptionHandling authenticationEntryPoint");
            CustomResponseUtil.fail(response, authException, HttpStatus.UNAUTHORIZED);
        }));

        http.exceptionHandling(e-> e.accessDeniedHandler((request, response, accessDeniedException) -> {
            log.info("exceptionHandling accessDeniedHandler");
            CustomResponseUtil.fail(response, accessDeniedException, HttpStatus.FORBIDDEN);
        }));

        http
            .cors(cors -> cors.configurationSource(configurationSource()));

        // jSessionId를 서버쪽에서 관리안하겠다는 뜻
        http
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html
        // 최근 공식문서에서는 ROLE_ 안붙여도 됨
        //	Any URL that starts with "/admin/" will be restricted to users who have the role "ROLE_ADMIN". You will notice that since we are invoking the hasRole method we do not need to specify the "ROLE_" prefix.
        http
            .authorizeHttpRequests((authorize) -> authorize
                .requestMatchers( "/api/swagger-config","/swagger-ui/**").permitAll()
                .requestMatchers("/api/post/**").authenticated()
                .requestMatchers("/api/admin/**").hasRole(Role.ADMIN.getName())
                .anyRequest().permitAll()
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource configurationSource() {
        log.debug("configurationSource cors 설정이 SecurityFilterChain에 등록됨");
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*"); // GET, POST, PUT, DELETE (Javascript 요청 허용)
        configuration.addAllowedOriginPattern("*"); // 모든 IP 주소 허용 (프론트 앤드 IP만 허용 react)
        configuration.setAllowCredentials(true); // 클라이언트에서 쿠키 요청 허용
        configuration.addExposedHeader("Authorization"); // 옛날에는 디폴트 였다. 지금은 ㄴㄴ
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); //모든주소에 CorsConfiguration 세팅
        return source;
    }
}
