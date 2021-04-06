package com.geeks18.util;

import com.geeks18.controller.PtcFileProcessRestController;
import com.geeks18.exceptions.ErrorConstants;
import com.geeks18.exceptions.JWTTokenValidationException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class ProcessorUtil {
    static Logger logger = LoggerFactory.getLogger(PtcFileProcessRestController.class);

    /**
     * Decode the JWT token and retrieves the header and the payload
     * @param token
     * @return
     */
    public static JSONObject decodeJWTToken(String token){

        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getDecoder();

        if(chunks.length<3){
            logger.error("Error with JWT token validation");
            throw new JWTTokenValidationException(ErrorConstants.ERR_2_CD, ErrorConstants.ERR_2_DESC);
        }
        String header = new String(decoder.decode(chunks[0]));
        String payload = new String(decoder.decode(chunks[1]));

        return new JSONObject(payload);


    }

    /**
     * Validte the incoming md5 hash with the md5 hash of the binary
     * @param base64Data
     * @param md5checkSum
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static boolean isCheckSumValid(String base64Data,String md5checkSum) throws NoSuchAlgorithmException {
        byte[] imageByteArray = Base64.getDecoder().decode(base64Data);
        byte[] digest = MessageDigest.getInstance("MD5").digest(imageByteArray);
        String newHash= DatatypeConverter.printHexBinary(digest).toUpperCase();
        logger.info("New Hash"+newHash);
        return (md5checkSum!=null ? md5checkSum.toUpperCase().equals(newHash) : false );
    }

}
