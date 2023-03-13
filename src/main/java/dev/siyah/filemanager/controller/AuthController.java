package dev.siyah.filemanager.controller;

import dev.siyah.filemanager.entity.Member;
import dev.siyah.filemanager.entity.RefreshToken;
import dev.siyah.filemanager.model.auth.MemberDetail;
import dev.siyah.filemanager.model.request.auth.JwtRefreshTokenRequest;
import dev.siyah.filemanager.model.request.auth.LoginRequest;
import dev.siyah.filemanager.model.response.auth.JwtTokenResponse;
import dev.siyah.filemanager.model.response.auth.RefreshTokenResponse;
import dev.siyah.filemanager.model.response.common.ExceptionResponse;
import dev.siyah.filemanager.service.auth.token.RefreshTokenService;
import dev.siyah.filemanager.utility.JwtUtility;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtility jwtUtility;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    @RequestBody(
            description = "User credentials for login",
            required = true,
            content = @Content(
                    schema = @Schema(implementation = LoginRequest.class),
                    examples = {
                            @ExampleObject(
                                    name = "Base Account",
                                    value = "{\"username\":\"admin\",\"password\":\"123456\"}",
                                    description = "Prefilled Login Request"
                            )
                    }
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(
                    schema = @Schema(implementation = JwtTokenResponse.class)
            )),
            @ApiResponse(responseCode = "401",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ExceptionResponse.class
                            ),
                            examples = @ExampleObject(
                                    name = "Invalid Credentials",
                                    value = "{\"status\": 401, \"error\": \"UNAUTHORIZED\", \"message\": \"Invalid Credentials\" }"
                            )
                    )
            ),
    })
    public JwtTokenResponse login(@org.springframework.web.bind.annotation.RequestBody LoginRequest loginRequest) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        MemberDetail memberDetail = (MemberDetail) auth.getPrincipal();
        final String jwt = this.jwtUtility.generateToken(memberDetail);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(memberDetail.getId());

        return JwtTokenResponse.builder().type("Bearer").token(jwt).refreshToken(refreshToken.getRefreshToken()).username(memberDetail.getUsername()).fullName(memberDetail.getFullName()).memberId(memberDetail.getId()).build();
    }

    @PostMapping("/logout")
    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.isAuthenticated()) {
            try {
                MemberDetail memberDetail = (MemberDetail) authentication.getPrincipal();

                refreshTokenService.deleteByMemberId(memberDetail.getId());
            } catch (Exception ignored) {

            }
        }
    }

    @PostMapping("/refresh-token")
    public RefreshTokenResponse refreshToken(@org.springframework.web.bind.annotation.RequestBody JwtRefreshTokenRequest jwtRefreshTokenRequest) {
        RefreshToken token = refreshTokenService.findByRefreshToken(jwtRefreshTokenRequest.getRefreshToken());

        if (token != null && refreshTokenService.verifyExpiration(token) != null) {
            Member member = token.getMember();

            String jwt = this.jwtUtility.createToken(member.getUsername());

            return RefreshTokenResponse.builder().tokenType("Bearer").accessToken(jwt).refreshToken(jwtRefreshTokenRequest.getRefreshToken()).build();
        } else {
            throw new JwtException("Refresh token expired");
        }
    }

}
