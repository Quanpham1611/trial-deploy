package quan.dodomilktea.config.paypal;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PaypalConfig {
    @Value("${paypal.client.app}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientScrect;

    @Value("${paypal.mode}")
    private String mode;

    @Bean
    Map<String, String>paypalSdkConfig(){
        Map<String, String> sdkConfig = new HashMap<>();
        sdkConfig.put("mode", mode);
        return sdkConfig;
    }

    @Bean
    OAuthTokenCredential authTokenCredential(){
        return new OAuthTokenCredential(clientId, clientScrect, paypalSdkConfig());
    }

    @Bean
    APIContext apiContext() throws PayPalRESTException {
        @SuppressWarnings("deprecation") APIContext apiContext = new APIContext(
                authTokenCredential().getAccessToken()
        );
        apiContext.setConfigurationMap(paypalSdkConfig());
        return apiContext;
    }
}
