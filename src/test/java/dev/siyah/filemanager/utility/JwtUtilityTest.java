package dev.siyah.filemanager.utility;

import dev.siyah.filemanager.model.auth.MemberDetail;
import dev.siyah.filemanager.properties.JWTProperties;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.UUID;

public class JwtUtilityTest {
    private JWTProperties jwtProperties;
    private JwtUtility jwtUtility;
    private MemberDetail memberDetail;

    @BeforeMethod
    public void setUp() {
        this.jwtProperties = JWTProperties.builder()
                .secret("1234567890123456")
                .jwtExp(1000L)
                .refreshExp(10000L)
                .build();

        this.jwtUtility = new JwtUtility(this.jwtProperties);

        this.memberDetail = MemberDetail.builder()
                .id(UUID.randomUUID())
                .fullName("Muhammed Kalender")
                .username("admin")
                .password("123456")
                .build();
    }

    @Test
    public void testGenerateToken() {
        String token = this.jwtUtility.generateToken(this.memberDetail);

        Assert.assertEquals(this.jwtUtility.extractUsername(token), this.memberDetail.getUsername());
    }

    @Test
    public void testCreateToken() {
        String token = this.jwtUtility.generateToken(this.memberDetail);

        Assert.assertEquals(this.jwtUtility.extractUsername(token), this.memberDetail.getUsername());
    }

    @Test
    public void testValidateJwtToken() {
        String token = this.jwtUtility.generateToken(this.memberDetail);

        Assert.assertTrue(this.jwtUtility.validateJwtToken(token));
    }

    @Test
    public void testValidateJwtToken_WhenInvalidToken() {
        Assert.assertFalse(this.jwtUtility.validateJwtToken("test"));
    }

    @Test
    public void testExtractUsername() {
        String token = this.jwtUtility.generateToken(this.memberDetail);

        Assert.assertEquals(this.jwtUtility.extractUsername(token), "admin");
    }

    @Test
    public void testExtractUsername_WhenExpiredToken() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY3ODY3MTQ1NCwiZXhwIjoxNjc4NjcxNDU1fQ.QyolymuqVewPmYDEtegc0eTk1ULIltWlASsWY_VJmrg";

        Assert.assertThrows(() ->
                this.jwtUtility.extractUsername(token)
        );
    }
}