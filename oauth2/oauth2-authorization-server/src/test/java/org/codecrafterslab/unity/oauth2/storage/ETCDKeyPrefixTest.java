package org.codecrafterslab.unity.oauth2.storage;

import org.codecrafterslab.unity.oauth2.storage.etcd.ETCDKeyPrefix;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ETCDKeyPrefixTest {

    @Test
    void getKey() {
        assertEquals("/oauth2", ETCDKeyPrefix.OAUTH2_PREFIX.toString());
        assertEquals("/oauth2/client", ETCDKeyPrefix.OAUTH2_CLIENT_PREFIX.toString());
        assertEquals("/oauth2/authorize", ETCDKeyPrefix.OAUTH2_AUTHORIZE_PREFIX.toString());
        assertEquals("/oauth2/authorize/state", ETCDKeyPrefix.getKey("oauth2", "authorize", "state").toString());
        assertEquals("/oauth2/authorize/state", ETCDKeyPrefix.getKey("oauth2", null, "authorize", "state").toString());
        assertEquals("/oauth2/authorize/state",
                ETCDKeyPrefix.getKey("oauth2", null, "authorize", "", "state").toString());
    }
}