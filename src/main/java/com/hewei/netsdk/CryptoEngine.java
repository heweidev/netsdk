package com.hewei.netsdk;

/**
 * Created by fengyinpeng on 2018/2/11.
 */

public interface CryptoEngine {
    String encrypt(String raw);
    String md5(String raw);
    String decrypt(String raw) throws Exception;
}
