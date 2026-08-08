package net.swofty.type.skyblockgeneric.resourcepack;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class HypixelSkyblockPackApiTest {
    @Test
    void selectsTheOfficialPackForEachSupportedProtocol() {
        HypixelSkyblockPackApi.Catalog catalog = HypixelSkyblockPackApi.parse("""
                {
                  "success": true,
                  "packs": [{
                    "id": "SkyBlock",
                    "versions": [
                      {"packFormat": 84, "hash": "hash-84", "url": "https://example.test/84.zip"},
                      {"packFormat": 88, "hash": "hash-88", "url": "https://example.test/88.zip"},
                      {"packFormat": 75, "hash": "hash-75", "url": "https://example.test/75.zip"}
                    ]
                  }]
                }
                """);

        assertEquals("hash-75", catalog.forProtocol(774).hash());
        assertEquals("hash-84", catalog.forProtocol(775).hash());
        assertEquals("hash-88", catalog.forProtocol(776).hash());
        assertNull(catalog.forProtocol(773));
    }
}
