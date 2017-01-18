/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revvster.playright.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import java.util.StringTokenizer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Rahul
 */
public class AuthenticationManager {

    private final static int ITERATION_NUMBER = 1000;

    private static final Logger logger = LogManager.getLogger(AuthenticationManager.class.getName());

    public AuthenticationManager() {
    }

    /**
     * Authenticates the user with a given login and password If password and/or
     * login is null then always returns false. If the user does not exist in
     * the database returns false.
     *
     * @param con Connection An open connection to a databse
     * @param login String The login of the user
     * @param password String The password of the user
     * @return boolean Returns true if the user is authenticated, false
     * otherwise
     * @throws SQLException If the database is inconsistent or unavailable (
     * (Two users with the same login, salt or digested password altered etc.)
     * @throws NoSuchAlgorithmException If the algorithm SHA-1 is not supported
     * by the JVM
     */
    public boolean authenticate(Connection con, String login, String password)
            throws SQLException, NoSuchAlgorithmException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            boolean userExist = true;
            // INPUT VALIDATION
            if (login == null || password == null) {
                // TIME RESISTANT ATTACK
                // Computation time is equal to the time needed by a legitimate user
                userExist = false;
                login = "";
                password = "";
            }
            logger.debug("authenticate::getUserFromDB");
            ps = con.prepareStatement("select password, salt from sb_users where username = ? and active = 1");
            ps.setString(1, login);
            rs = ps.executeQuery();
            String digest, salt;
            if (rs.next()) {
                digest = rs.getString("password");
                salt = rs.getString("salt");
                // DATABASE VALIDATION
                if (digest == null || salt == null) {
                    throw new SQLException("Database inconsistant Salt or Digested Password altered");
                }
                if (rs.next()) { // Should not append, because login is the primary key
                    throw new SQLException("Database inconsistent two CREDENTIALS with the same LOGIN");
                }
            } else { // TIME RESISTANT ATTACK (Even if the user does not exist the
                // Computation time is equal to the time needed for a legitimate user
                digest = "000000000000000000000000000=";
                salt = "00000000000=";
                userExist = false;
            }

            logger.debug("authenticate::userExists::" + userExist);

            byte[] bDigest = base64ToByte(digest);
            byte[] bSalt = base64ToByte(salt);

            // Compute the new DIGEST
            byte[] proposedDigest = getHash(ITERATION_NUMBER, password, bSalt);

            return Arrays.equals(proposedDigest, bDigest) && userExist;
        } catch (IOException ex) {
            throw new SQLException("Database inconsistant Salt or Digested Password altered");
        } finally {
            close(rs);
            close(ps);
        }
    }

    /**
     * Inserts a new user in the database
     *
     * @param con Connection An open connection to a databse
     * @param login String The login of the user
     * @param password String The password of the user
     * @return boolean Returns true if the login and password are ok (not null
     * and length(login)<=100
     * @throws SQLException If the database is unavailable
     * @throws NoSuchAlgorithmException If the algorithm SHA-1 or the
     * SecureRandom is not supported by the JVM
     */
    public boolean updateCredentials(Connection con, String login, String password, Integer resetPwd)
            throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException {
        logger.debug("updateCreds::START");
        PreparedStatement ps = null;
        try {
            if (login != null && password != null && login.length() <= 100) {
                // Uses a secure Random not a simple Random
                SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
                // Salt generation 64 bits long
                byte[] bSalt = new byte[8];
                random.nextBytes(bSalt);
                // Digest computation
                byte[] bDigest = getHash(ITERATION_NUMBER, password, bSalt);
                String sDigest = byteToBase64(bDigest);
                String sSalt = byteToBase64(bSalt);

                logger.debug("updateCreds::EncodedPwd::" + sDigest);
                logger.debug("updateCreds::EncodedSalt::" + sSalt);

                ps = con.prepareStatement("update sb_users \n"
                        + "set password = ?, \n"
                        + "reset_password = ?, \n"
                        + "salt = ? \n"
                        + "where username = ?");
                ps.setString(4, login);
                ps.setString(1, sDigest);
                ps.setString(3, sSalt);
                ps.setInt(2, resetPwd);
                ps.executeUpdate();
                return true;
            } else {
                return false;
            }
        } finally {
            close(ps);
            logger.debug("updateCreds::END");
        }
    }

    /**
     * From a password, a number of iterations and a salt, returns the
     * corresponding digest
     *
     * @param iterationNb int The number of iterations of the algorithm
     * @param password String The password to encrypt
     * @param salt byte[] The salt
     * @return byte[] The digested password
     * @throws NoSuchAlgorithmException If the algorithm doesn't exist
     */
    public byte[] getHash(int iterationNb, String password, byte[] salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.reset();
        digest.update(salt);
        byte[] input = digest.digest(password.getBytes("UTF-8"));
        for (int i = 0; i < iterationNb; i++) {
            digest.reset();
            input = digest.digest(input);
        }
        return input;
    }

//   public void createTable(Connection con) throws SQLException{
//       Statement st = null;
//       try {
//           st = con.createStatement();
//           st.execute("CREATE TABLE CREDENTIAL (LOGIN VARCHAR(100) PRIMARY KEY, PASSWORD VARCHAR(32) NOT NULL, SALT VARCHAR(32) NOT NULL)");
//       } finally {
//           close(st);
//       }
//   }
    /**
     * Closes the current statement
     *
     * @param ps Statement
     */
    public void close(Statement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException ignore) {
            }
        }
    }

    /**
     * Closes the current resultset
     *
     * @param ps Statement
     */
    public void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ignore) {
            }
        }
    }

    /**
     * From a base 64 representation, returns the corresponding byte[]
     *
     * @param data String The base64 representation
     * @return byte[]
     * @throws IOException
     */
    public static byte[] base64ToByte(String data) throws IOException {
        Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(data);
    }

    /**
     * From a byte[] returns a base 64 representation
     *
     * @param data byte[]
     * @return String
     * @throws IOException
     */
    public static String byteToBase64(byte[] data) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(data);
    }

    public String authenticateApi(Connection con, String authCredentials)
            throws SQLException, NoSuchAlgorithmException {
        if (null == authCredentials) {
            logger.info("authenticateApi::Credentials not provided");
            return "";
        }
        // header value format will be "Basic encodedstring" for Basic
        // authentication. Example "Basic YWRtaW46YWRtaW4="
        logger.debug("authenticateApi::START");
        final String encodedUserPassword = authCredentials.replaceFirst("Basic" + " ", "");
        String usernameAndPassword = null;
        try {
            byte[] decodedBytes = base64ToByte(encodedUserPassword);
            usernameAndPassword = new String(decodedBytes, "UTF-8");
        } catch (IOException e) {
            logger.error("authenticateApi::Unable to decode username and password", e);
        }
        final StringTokenizer tokenizer = new StringTokenizer(
                usernameAndPassword, ":");
        String login = tokenizer.nextToken();
        String password = tokenizer.nextToken();

        boolean authenticated = authenticate(con, login, password);

        logger.info("authenticateApi::Authenticated::" + authenticated);
        logger.debug("authenticateApi::END");
        if (authenticated) {
            return login;
        } else {
            return "";
        }
    }

    public static String generatePassword() {
        Random ran = new Random();
        char[] val1 = {'w', 'e', 'l', 'c', 's', 'p', 'a', 'm', 'S', 'M', 'A', 'H', 'E', 'V'};
        char[] val2 = {'@', '&', '$', '#', '%', '*'};
        char[] val3 = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        String output1 = "";
        String output2 = "";
        String output3 = "";
        String password = "";

        for (int i = 0; i < 3; i++) {
            int idx = ran.nextInt(val1.length);
            output1 += val1[idx];
        }

        for (int i = 0; i < 3; i++) {
            int idx = ran.nextInt(val3.length);
            output2 += val3[idx];
        }

        for (int i = 0; i < 1; i++) {
            int idx = ran.nextInt(val2.length);
            output3 += val2[idx];
        }

        password = output1.concat(output3).concat(output2);
        return password;
    }

}
