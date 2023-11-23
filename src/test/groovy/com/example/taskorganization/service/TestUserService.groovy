package com.example.taskorganization.service

import com.example.taskorganization.dao.entity.ChangePasswordEntity
import com.example.taskorganization.dao.entity.UserEntity
import com.example.taskorganization.dao.repository.ChangePasswordRepository
import com.example.taskorganization.dao.repository.UserRepository
import com.example.taskorganization.exception.NotFoundException
import com.example.taskorganization.mapper.UserMapper
import com.example.taskorganization.model.request.ChangePasswordRequest
import com.example.taskorganization.model.request.SignInRequest
import com.example.taskorganization.model.request.SignUpRequest
import com.example.taskorganization.model.response.SignInResponse
import com.example.taskorganization.service.abstraction.UserService
import com.example.taskorganization.service.implementation.UserServiceImpl
import com.example.taskorganization.util.JwtUtil
import com.example.taskorganization.util.MailSenderUtil
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification

import java.time.LocalDateTime

class TestUserService extends Specification {

    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()
    UserService userService
    UserRepository userRepository
    PasswordEncoder passwordEncoder
    AuthenticationManager authenticationManager
    JwtUtil jwtUtil
    MailSenderUtil mailSenderUtil
    ChangePasswordRepository changePasswordRepository

    void setup() {
        userRepository = Mock()
        passwordEncoder = Mock()
        authenticationManager = Mock()
        changePasswordRepository = Mock()
        jwtUtil = Mock()
        mailSenderUtil = Mock()
        userService = new UserServiceImpl(userRepository, passwordEncoder, authenticationManager, jwtUtil, mailSenderUtil, changePasswordRepository)
    }

    def "Test SignIn success"() {
        given:
        def request = random.nextObject(SignInRequest)
        def userEntity = random.nextObject(UserEntity)
        def signInResponse = random.nextObject(SignInResponse)
        when:
        userService.signIn(request)
        then:
        1 * userRepository.findByUsernameOrEmail(request.getUsername(), request.getUsername()) >> Optional.of(userEntity)
        1 * jwtUtil.generateTokens(userEntity) >> signInResponse


    }
    def "Test checkConfirmationCode success"() {

        given:
        def code=random.nextObject(String)
        def user=random.nextObject(UserEntity)
        when:
        userService.checkConfirmationCode(code)
        then:
        1 * userRepository.findByConfirmCode(code) >> Optional.of(user)
        user.isEnabled==true
        1 * userRepository.save(user)

    }
    def "Test checkConfirmationCode code not found"() {

        given:
        def code=random.nextObject(String)
        def user=random.nextObject(UserEntity)
        when:
        userService.checkConfirmationCode(code)
        then:
        1 * userRepository.findByConfirmCode(code) >> Optional.empty()
        0 * userRepository.save(user)
        thrown(NotFoundException)

    }


}
