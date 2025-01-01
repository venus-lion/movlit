package movlit.be.auth.presentation;

import lombok.RequiredArgsConstructor;
import movlit.be.auth.application.service.MyMemberDetailsService;
import movlit.be.auth.presentation.dto.RefreshTokenRequest;
import movlit.be.common.filter.dto.AuthenticationRequest;
import movlit.be.common.filter.dto.AuthenticationResponse;
import movlit.be.common.util.JwtTokenUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final MyMemberDetailsService myMemberDetailsService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
            throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                            authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect email or password", e);
        }

        final UserDetails userDetails = myMemberDetailsService
                .loadUserByUsername(authenticationRequest.getEmail());

        final String accessToken = jwtTokenUtil.generateAccessToken(userDetails.getUsername()); // email
        final String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails.getUsername()); // email

        return ResponseEntity.ok(new AuthenticationResponse(accessToken, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        // Refresh Token 검증
        String memberName = jwtTokenUtil.extractEmail(refreshToken);
        if (jwtTokenUtil.validateToken(refreshToken, memberName)) {
            // 새로운 Acces Token 생성
            String newAccessToken = jwtTokenUtil.generateAccessToken(memberName);
            return ResponseEntity.ok(new AuthenticationResponse(newAccessToken, refreshToken));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh Token"); // TODO: 리팩토링 필요
    }

}