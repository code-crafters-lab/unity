package org.codecrafterslab.unity.oauth2.authentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;


/**
 * 绑定的第三方 OAuth2 客户端，临时授权码登录
 *
 * @author Wu Yujie
 * @email coffee377@dingtalk.com
 * @time 2021/06/23 14:13
 */
@Slf4j
public class ThirdCodeLoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter implements ApplicationContextAware {
    public static final String PROVIDER_KEY = "provider";
    public static final String CLIENT_ID_KEY = "client_id";
    public static final String TEMP_AUTH_CODE_KEY = "code";
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/login" +
            "/oauth2/code/*",
            "POST");
    private final ThirdCodeAuthenticationConverter authenticationConverter =
            new ThirdCodeAuthenticationConverter();
    private ApplicationContext applicationContext;
    private Converter<ThirdCodeAuthenticationToken, OAuth2AuthenticationToken> authenticationResultConverter;

    public ThirdCodeLoginAuthenticationFilter(String processesUrl) {
        super(new AndRequestMatcher(new AntPathRequestMatcher(processesUrl, "POST")));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, ServletException {
        ThirdCodeAuthenticationToken authRequest =
                (ThirdCodeAuthenticationToken) this.authenticationConverter.convert(request);
        Object authenticationDetails = this.authenticationDetailsSource.buildDetails(request);
        assert authRequest != null;
        authRequest.setDetails(authenticationDetails);
        ThirdCodeAuthenticationToken authenticationResult =
                (ThirdCodeAuthenticationToken) this.getAuthenticationManager().authenticate(authRequest);
        OAuth2AuthenticationToken oauth2Authentication =
                this.authenticationResultConverter.convert(authenticationResult);
        Assert.notNull(oauth2Authentication, "authentication result cannot be null");
        oauth2Authentication.setDetails(authenticationDetails);
//        OAuth2AuthorizedClient authorizedClient = new OAuth2AuthorizedClient(authenticationResult
//        .getClientRegistration(), oauth2Authentication.getName(), authenticationResult.getAccessToken(),
//        authenticationResult.getRefreshToken());
//        this.authorizedClientRepository.saveAuthorizedClient(authorizedClient, oauth2Authentication, request,
//        response);
        return oauth2Authentication;
    }

    @Override
    public void afterPropertiesSet() {
        if (this.getAuthenticationManager() == null) {
            AuthenticationManager authenticationManager = applicationContext.getBean(AuthenticationManager.class);
            this.setAuthenticationManager(authenticationManager);
        }
        super.afterPropertiesSet();
    }

}
