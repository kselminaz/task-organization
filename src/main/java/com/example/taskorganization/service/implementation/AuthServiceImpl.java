package com.example.taskorganization.service.implementation;

import com.example.taskorganization.config.security.SignedUserDetails;
import com.example.taskorganization.service.abstraction.AuthService;
import com.example.taskorganization.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

import static com.example.taskorganization.model.constants.AuthConstants.AUTH_HEADER;
import static com.example.taskorganization.model.constants.AuthConstants.BEARER_AUTH_HEADER;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Override
    public Optional<Authentication> getAuthentication(HttpServletRequest httpServletRequest) {
        return Optional.ofNullable(httpServletRequest.getHeader(AUTH_HEADER))
                .filter(this::isBearerAuth)
                .flatMap(this::getAuthenticationBearer);
    }

    private boolean isBearerAuth(String header) {
        return header.toLowerCase().startsWith(BEARER_AUTH_HEADER.toLowerCase());
    }

    private Optional<Authentication> getAuthenticationBearer(String header) {
        String token = header.substring(BEARER_AUTH_HEADER.length()).trim();
        Claims claims = jwtUtil.parseToken(token);
        if (claims.getExpiration().before(new Date())) {
            return Optional.empty();
        }
        return Optional.of(getAuthentication(claims));
    }

    private Authentication getAuthentication(Claims claims) {

        SignedUserDetails signedUserDetails = new SignedUserDetails(
                Long.valueOf(claims.get("userId").toString()),
                claims.get("username").toString(),
                claims.get("username").toString()
        );

        return new UsernamePasswordAuthenticationToken(signedUserDetails, "", null);
    }

}
