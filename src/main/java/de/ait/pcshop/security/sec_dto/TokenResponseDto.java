package de.ait.pcshop.security.sec_dto;

import de.ait.pcshop.model.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
@EqualsAndHashCode
@ToString
public class TokenResponseDto {

    private String accessToken;
    private String refreshToken;
    private User user;


    public TokenResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public TokenResponseDto(String accessToken, String refreshToken, User user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.user = user;
    }
}