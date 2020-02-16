package com.example.urpm.config.shiro.jwt;

import com.alibaba.fastjson.JSON;
import com.example.urpm.common.base.RestResult;
import com.example.urpm.common.exception.BusinessException;
import com.example.urpm.common.exception.JwtTokenException;
import com.example.urpm.config.ConfigProperties;
import com.example.urpm.model.common.Constant;
import com.example.urpm.util.JWTUtil;
import com.example.urpm.util.JedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author dingjinyang
 * @datetime 2020/2/13 12:33
 * @description 重写shiro过滤，采用JWT
 */
@Slf4j
public class JWTFilter extends BasicHttpAuthenticationFilter {


    /**
     * 这里我们详细说明下为什么最终返回的都是true，即允许访问
     * 例如我们提供一个地址 GET /article
     * 登入用户和游客看到的内容是不同的
     * 如果在这里返回了false，请求会被直接拦截，用户看不到任何东西
     * 所以我们在这里返回true，Controller中可以通过 subject.isAuthenticated() 来判断用户是否登入
     * 如果有些资源只有登入用户才能访问，我们只需要在方法上面加上 @RequiresAuthentication 注解即可
     * 但是这样做有一个缺点，就是不能够对GET,POST等请求进行分别过滤鉴权(因为我们重写了官方的方法)，但实际上对应用影响不大
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        String httpMethod = httpServletRequest.getMethod();
        String httpURI = httpServletRequest.getRequestURI();
        String httpHeaderAuthorization = httpServletRequest.getHeader("Authorization");
        log.debug("==> request : {} | method : {} | Authorization:{}", httpURI, httpMethod, httpHeaderAuthorization);
        // 查看当前Header中是否携带Authorization属性(Token)，有 -> 进行登录认证授权 ,没有 -> 401
        if (!this.isLoginAttempt(request, response)) {
            // 没有携带Token
            log.debug("==> request: {} | method {}, Authorization is null", httpURI, httpMethod);
            this.response401(response, "Token为空，请先登录 ");
            return false;
        }
        //  携带Token
        try {
            // 进行Shiro的登录UserRealm
            log.debug("==> request header has Authorization --> userRealm doGetAuthenticationInfo");
            this.executeLogin(request, response);
        } catch (JwtTokenException e) {
            log.debug("==> userRealm doGetAuthenticationInfo failed : JwtToken has expired --> Refresh Token");
            if (!this.refreshToken(request, response)) {
                this.response401(response, "Token已过期，请重新登录 ");
                return false;
            }
        } catch (Exception e) {
            // 认证出现异常，传递错误信息msg
            log.debug("==> userRealm doGetAuthenticationInfo failed Exception : {}", e.getMessage());
        }
        return true;
    }

    /**
     * 这里我们详细说明下为什么重写
     * 可以对比父类方法，只是将executeLogin方法调用去除了
     * 如果没有去除将会循环调用doGetAuthenticationInfo方法
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        this.sendChallenge(request, response);
        return false;
    }

    /**
     * 检测Header里面是否包含Authorization字段，有就进行Token登录认证授权
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        // 拿到当前Header中Authorization的AccessToken(Shiro中getAuthzHeader方法已经实现)
        String token = this.getAuthzHeader(request);
        return token != null;
    }

    /**
     * 进行AccessToken登录认证授权
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        // 拿到当前Header中Authorization的AccessToken(Shiro中getAuthzHeader方法已经实现)
        JWTToken token = new JWTToken(this.getAuthzHeader(request));
        // 提交给UserRealm进行认证，如果错误他会抛出异常并被捕获
        this.getSubject(request, response).login(token);
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    /**
     * 此处为AccessToken刷新，进行判断RefreshToken是否过期，未过期就返回新的AccessToken且继续正常访问
     */
    private boolean refreshToken(ServletRequest request, ServletResponse response) {
        // 拿到当前Header中Authorization的AccessToken(Shiro中getAuthzHeader方法已经实现)
        String token = this.getAuthzHeader(request);
        // 获取当前Token的帐号信息
        String account = JWTUtil.getClaim(token, Constant.ACCOUNT);
        String timestamp = JWTUtil.getClaim(token, Constant.CURRENT_TIME_MILLIS);
        log.debug("==> refreshToken Account: {}, Timestamp :{}", account, timestamp);
        log.debug("Timestamp equals jwt : {}, redis : {}", JWTUtil.getClaim(token, Constant.CURRENT_TIME_MILLIS), JedisUtil.get(Constant.PREFIX_SHIRO_REFRESH_TOKEN + account));
        // 判断Redis中RefreshToken是否存在
        if (!JedisUtil.exists(Constant.PREFIX_SHIRO_REFRESH_TOKEN + account)) {
            return false;
        }// Redis中RefreshToken还存在，获取RefreshToken的时间戳
        String redisTimestamp = JedisUtil.get(Constant.PREFIX_SHIRO_REFRESH_TOKEN + account);
        String jwtTimestamp = JWTUtil.getClaim(token, Constant.CURRENT_TIME_MILLIS);
        // 获取当前AccessToken中的时间戳，与RefreshToken的时间戳对比，如果当前时间戳一致，进行AccessToken刷新
        log.debug("Timestamp equals jwt : {}, redis : {}", jwtTimestamp, redisTimestamp);
        if (!jwtTimestamp.equals(redisTimestamp)) {
            return false;
        }
        // 获取当前最新时间戳
        String currentTimeMillis = String.valueOf(System.currentTimeMillis());
        // 读取配置文件，获取refreshTokenExpireTime属性
        int refreshTokenExpireTime = ConfigProperties.refreshTokenExpireTime;
        String refreshed_token = JWTUtil.sign(account, currentTimeMillis);
        // 刷新AccessToken，设置时间戳为当前最新时间戳,将新刷新的AccessToken再次进行Shiro的登录
        JWTToken jwtToken = new JWTToken(refreshed_token);
        // 设置RefreshToken中的时间戳为当前最新时间戳，且刷新过期时间重新为30分钟过期(配置文件可配置refreshTokenExpireTime属性)
        JedisUtil.set(Constant.PREFIX_SHIRO_REFRESH_TOKEN + account, currentTimeMillis, refreshTokenExpireTime);
        // 提交给UserRealm进行认证，如果错误他会抛出异常并被捕获，如果没有抛出异常则代表登入成功，返回true
        log.debug("refreshToken over , --> userRealm again  ");
        this.getSubject(request, response).login(jwtToken);
        // 最后将刷新的AccessToken存放在Response的Header中的Authorization字段返回
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setHeader("Authorization", refreshed_token);
        httpServletResponse.setHeader("Access-Control-Expose-Headers", "Authorization");
        return true;
    }

    /**
     * 无需转发，直接返回Response信息
     */
    private void response401(ServletResponse response, String msg) {
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        try (PrintWriter out = httpServletResponse.getWriter()) {
            String data = JSON.toJSONString(new RestResult<>(HttpStatus.UNAUTHORIZED.value(), "无权访问(Unauthorized):" + msg, null));
            out.append(data);
        } catch (IOException e) {
            log.error("response401 has IOException:{}", e.getMessage());
            throw new BusinessException("直接返回Response信息出现IOException异常:" + e.getMessage());
        }
    }

}
