package org.example.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

// Определяем что это конфигурация
@Configuration
// Аннотация Spring Security активирует фреймворк
@EnableWebSecurity
public class AppSecurityConfig {
    private static final Logger logger = LoggerFactory.getLogger(AppSecurityConfig.class);

    @Bean(name = "mvcHandlerMappingIntrospector")
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
        return new HandlerMappingIntrospector();
    }


    // [ In-Memory Authentication ]
    // Создает бин userDetailsService, который использует класс InMemoryUserDetailsManager
    // из Spring Security для хранения пользователей в памяти
    @Bean
    public UserDetailsService users() {
        logger.info("populate in memory auth user");
        UserDetails user = User.builder()
                .username("root")
                // пароль кодируется с passwordEncoder(), который должен быть зарегистрирован в контейнере Spring
                .password(passwordEncoder().encode("123"))
                .roles("USER").build();
        return new InMemoryUserDetailsManager(user);
    }

    // код создает бин PasswordEncoder, который использует BCryptPasswordEncoder для шифрования паролей.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // [ Configuring HttpSecurity ]
    // Создает SecurityFilterChain, который представляет собой цепочку фильтров,
    // обрабатывающих запросы в соответствии с настройками Spring Security.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        logger.info("config http security");
        // отключения защиты от кликджекинга (clickjacking protection) в заголовке ответа HTTP
        // необходима для того что бы веб интерфейс базы данных смог отобразится
        http
                // Метод http.headers() возвращает объект HeadersConfigurer,
                // который позволяет настраивать заголовки HTTP в ответах, отправляемых приложением.
                .headers()
                // Метод frameOptions() указывает, что настройки безопасности кадров (frame options)
                // должны быть настроены.
                .frameOptions()
                // Метод disable() отключает защиту от кликджекинга, устанавливая значение
                // "DENY" для заголовка "X-Frame-Options".
                .disable();

        http
                // отключение Cross-Site Request Forgery (CSRF)
                .csrf().disable()
                // начало настройки авторизации запросов
                .authorizeHttpRequests(authorize -> authorize
                        // разрешает все запросы, начинающиеся с /login*.
                        .requestMatchers("/login**").permitAll()
                        // требует аутентификации для всех остальных запросов
                        .anyRequest().authenticated()
                )
                // // настройка формы логина
                .formLogin(form -> form
                        // указывает, что страница логина находится по адресу /login
                        .loginPage("/login")
                        // указывает URL, который обрабатывает запрос на аутентификацию
                        .loginProcessingUrl("/login/auth")
                        // указывает URL для перенаправления пользователя после успешной аутентификации
                        .defaultSuccessUrl("/books/shelf", true)
                        // указывает URL для перенаправления пользователя после неудачной попытки аутентификации
                        .failureUrl("/login")
                );
        // создает и возвращает SecurityFilterChain
        return http.build();
    }

    // [ Configuring WebSecurity ]
    // создает бин webSecurityCustomizer, который используется для настройки объекта WebSecurity
    // WebSecurity используется для настройки базовой защиты на уровне веб-запросов,
    // таких как запросы на статические ресурсы (например, изображения, CSS-файлы и т.д.).
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        logger.info("config web security");
        return web -> web
                // игнорирование определенных запросов
                // т.е. Spring Security не будет выполнять на них проверки авторизации и аутентификации
                .ignoring()
                // запросов, которые начинаются с /images/**
                .requestMatchers("/images/**");
    }
}