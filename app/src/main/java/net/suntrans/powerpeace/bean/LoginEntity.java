package net.suntrans.powerpeace.bean;

/**
 * Created by Looney on 2017/8/31.
 */

public class LoginEntity {


    /**
     * token_type : Bearer
     * expires_in : 1296000
     * access_token : eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImI4M2M1Y2Q1ZDc1OGUwMzc3MGFiZTMwNTk3M2MwNzQxZWMyZDc5YzVhNzVlYjUxNThlOTc3MDM2NzI1YmZjYzIyNDc5NDgzMWM5YWM1MDcxIn0.eyJhdWQiOiIxMDAwMDEiLCJqdGkiOiJiODNjNWNkNWQ3NThlMDM3NzBhYmUzMDU5NzNjMDc0MWVjMmQ3OWM1YTc1ZWI1MTU4ZTk3NzAzNjcyNWJmY2MyMjQ3OTQ4MzFjOWFjNTA3MSIsImlhdCI6MTUwNzE5MDY2MCwibmJmIjoxNTA3MTkwNjYwLCJleHAiOjE1MDg0ODY2NjAsInN1YiI6IjQyMTgiLCJzY29wZXMiOltdfQ.hZBvtUfsNUwXDfeY5Rr1j20ti5LPlmiYVetTAEhiQN9mfYMcp_QVMvhnzAUPKfAA7fscZstU75uqyqw_7YQxEgCkxCiH0WjzrOTJxw_dcvdROfZSKI7rQCnFO39vd550EzWsceuf7cds1RqR97aJlFSnsTjvHTWx24SGJ6nyT1Kf4amPu0-oCDry8c9sllPxGQRpR6toQfd2NmjP3Ejjht7XERZotik9u1eOoM0fmFIPCJvz0UDlDp6rQhPnVbF--pDijzJKFGbnc1dR1LSL9Vv0PWYIjMCosjh2W3bCzBB340A9UpLNLm-BEuYVarOLusrqDOnxb7UBTcrH0TAEIiqOgTr_5cqHQBiIbXhMRCJk5iuIU6_zjP1qftJsogbnQyhmjGJKL5cutaQGkHGeH9lm-zbRz4naHp5YozY91joQi6-cBfCdZOP7qjK9VyMpGbCyZN8_mVVEaJCPamkTtGbgOdvb_mOB9Ad8_uFdSvGayp9ynkdg6WKTHoo9-AIfdcEVHlTbypDgVk2SzhSQuej5EaCQgxQvPH4ceL05dhnrk2-6-RdTVFQHXBqO62uglpxy80mtN3hqRlHdy05tfwo3E1QAjys2j9RIntHG2WB1QFzxWBDNYAHkqAtzTjQbenXoTylHCb6XAv_6YiIVnBVkjFSVuF6VygcthRWQf9c
     * refresh_token : def502003c1f9b7eda2787f7aa8ce319681d58e04b114fabe65ec90d8fc8c147b9fb86e3db240653c49627492a5561ea37a8464e2d998266d3d3be8719d7f87e6d100af922eb60481afa4fa59c449728a5cfa14eadd0e12ee10ce6b3e8cb7fb64c285767e8d881d4ea66c8951ba30411c4d80c3cc05a1999c961e4cae714308d9aa480140218243bc5ce1dfa023a97b37ae3d5a826e0866a984f1860280956e50428bbc8ba50d353c8f80f9e2bc56176def08cb4cc5149b6982ccf0f94a5e8343468827a9688891140f79e18b619f7d54ea62862953bf8db1800573069c182bbc4239c285cf5981c49a9fd19a45b4871cb934a9ed4ed20737c7b4e27cb528e86b0d0d23ab40d4c4d38191e12b9c5a67d90d59e2f03bed76f7932f5d9a889911fccace4b90b8dfb9bb6e7713da873bbc175b1f3c4275b4edd0700e6df959cfe0d37977e95f7857eed51fd35ef5c3ecae0f4a626f049549ecd11ec54595a18b5b011af46eb6dac271a65
     */

    private String token_type;
    private int expires_in;
    private String access_token;
    private String refresh_token;

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}
