package net.swofty.type.hub.npcs;

import net.minestom.server.coordinate.Pos;
import net.swofty.type.generic.user.HypixelPlayer;
import net.swofty.type.generic.entity.npc.HypixelNPC;
import net.swofty.type.generic.entity.npc.configuration.HumanConfiguration;
import net.swofty.type.hub.gui.GUIShopMineMerchant;
import net.swofty.type.hub.gui.GUIShopPat;

import net.swofty.type.generic.event.custom.NPCInteractEvent;

public class NPCPat extends HypixelNPC {

    public NPCPat() {
        super(new HumanConfiguration() {
            @Override
            public String[] holograms(HypixelPlayer player) {
                return new String[]{"Pat", "§e§lCLICK"};
            }

            @Override
            public String signature(HypixelPlayer player) {
                return "nbzDsyU+fe3zv9+9hCem2l8pxaJdgtNC2wx81F5XyhkDJnIh9TYsPrA1u8AJIEIGkZ0FTI6A9dF49IAISHpwy9psBRoh0oGD1y2MFTxL+8vPAbBh6tkFfbeiVAnZua04v3hvcXMZf0zv77gyH7EK/ml0YTG/lJenOrseze9n61r69vFZcygV0OSfIC6Iy0cYyMAc3I4d+lMHDIQceJ6IC+MXwtOQK2pHloPANVDpFMkv1W2Kczwzm0K/2/GS8C2eCLV0IySPnhUlqj/gR1Qdm16VodXQ62mum5Cul5MQzK/Bh0PTMk4OiLWT3Mz3Ok4NWL0H4dtk0O8kCjcYV7xvBR6mKqdhhTerGbNqrx2FxV3XJIKROS436m4zvmqSEX1KkrMKqGFQFlV5Xm8gmqeiBC48FvNOOvSKlhQg6vQuKTVWRqsF+hzvTdTDeKftv4MZnXIY/E5Upqq8ke9/ShboWTxVqCv7ZHTNDoe0c1yIhWPgpkkHmnIfdb6+Xr4VWy/d0KnOR/6xxbL5OGzJbAOo09C5oittKabclmeAZJwg1tdwVg+2fRxuyW/H6uAPfxNeiM/uGZN9QzKPb8JMD3HCRO/XoJU5VD18WVcm6TTEshMPPSERJfWs/QdrkvuQ35RP3VjIF/B7QXOIEnlsL59bsPi0PFArqRIFHw4qpmzXeKM=";
            }

            @Override
            public String texture(HypixelPlayer player) {
                return "eyJ0aW1lc3RhbXAiOjE0OTI5MzE3ODA5ODksInByb2ZpbGVJZCI6IjdkYTJhYjNhOTNjYTQ4ZWU4MzA0OGFmYzNiODBlNjhlIiwicHJvZmlsZU5hbWUiOiJHb2xkYXBmZWwiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJtZXRhZGF0YSI6eyJtb2RlbCI6InNsaW0ifSwidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9lMjYyOTQ2N2NmNTQ0ZTc5ZWQxNDhkM2QzODQyODc2ZjJkODg0MTg5OGI0YjJlNmY2MGRmYzFlMDJmMTE3OWYzIn19fQ==";
            }

            @Override
            public Pos position(HypixelPlayer player) {
                return new Pos(-134.5, 73, -97.5, 0, 0);
            }

            @Override
            public boolean looking(HypixelPlayer player) {
                return true;
            }
        });
    }

    @Override
    public void onClick(NPCInteractEvent e) {
        e.player().openView(new GUIShopPat());
    }

}
