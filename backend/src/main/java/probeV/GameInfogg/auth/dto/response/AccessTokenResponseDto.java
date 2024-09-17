package probeV.GameInfogg.auth.dto.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccessTokenResponseDto {
    private String accessToken;
    private Date accessTokenExpiresDate;
}
