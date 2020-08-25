package com.leyou.auth.test;

import com.leyou.auth.bean.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.auth.utils.RsaUtils;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author Srd
 * @date 2020/8/24  23:59
 */
public class TestJWT {

    private static final String pubKeyPath = "F:/BadCode/rsa/rsa.pub";

    private static final String priKeyPath = "F:/BadCode/rsa/rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    /**
     * 测试生成密钥
     * @throws Exception
     */
    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(pubKeyPath,priKeyPath,"BadCode");
    }

    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    /**
     * 测试生成token
     * @throws Exception
     */
    @Test
    public void testGenerateToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(20L, "Jerry"), privateKey, 5);
        System.out.println("token = " + token);
    }

    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiSmVycnkiLCJleHAiOjE1OTgzNDA0ODJ9.Q7DOmQ_ibbaPwmtvqe3BPUtMux7jypfTWXhfiAhVpIiZv6qIvWpfy8KROMp4HfA22fInsrymkC6qaMtqfAqcA03uQoxTaFUpLGdmBmdxXlohy5jBHLyhfIuKTA7F0PhlX9YG74_mYC_v9QSEDwuWJLYGFLaTOz8706F8vw5v8r8";
        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
}
