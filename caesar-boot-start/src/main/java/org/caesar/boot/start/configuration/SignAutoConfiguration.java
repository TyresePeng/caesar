package org.caesar.boot.start.configuration;

import org.caesar.boot.start.properties.SignProperties;
import org.caesar.boot.start.sign.SignAspect;
import org.caesar.boot.start.sign.handler.DefaultSignHandler;
import org.caesar.boot.start.sign.handler.SignHandler;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.annotation.Resource;


/**
 * <p> @author GuoPeng</p>
 * <p> @version 1.0.0</p>
 * <p> @description 签名切面自动装配</p>
 * <p> @createTime 2022-10-31 16:56:00</p>
 */
@Configuration
@EnableConfigurationProperties({SignProperties.class})
@ConditionalOnProperty(prefix = "caesar.sign", value = "enable", matchIfMissing = true)
public class SignAutoConfiguration {

    @Resource
    private SignProperties signProperties;

    private final SignAspect signAspect = new SignAspect();

    @Bean
    @ConditionalOnMissingBean(SignAspect.class)
    public SignAspect signAspect() {
        return signAspect;
    }

    @ConditionalOnMissingBean(SignHandler.class)
    @Bean
    public SignHandler signHandler() {
        return new DefaultSignHandler();
    }


    @Bean
    public Advisor signAspectImpl() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        if (signProperties.getDefaultScanAspect().equals(signProperties.getScanAspect())) {
            pointcut.setExpression(signProperties.getDefaultScanAspect());
        } else {
            pointcut.setExpression(String.format("execution(* %s)", signProperties.getScanAspect()));
        }
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setPointcut(pointcut);
        advisor.setAdvice(signAspect);
        return advisor;
    }

}
