package com.tajr.tajr.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserResponse {

    @SerializedName("response")
    @Expose
    private String response;

    @SerializedName("data")
    @Expose
    private UserData data;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("info")
    @Expose
    private String info;

    @SerializedName("token")
    @Expose
    private String token;


    @SerializedName("clients")
    @Expose
    private List<Clients> clients;

    public UserResponse() {
    }

    public List<Clients> getClients() {
        return clients;
    }

    public void setClients(List<Clients> clients) {
        this.clients = clients;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getCode() {
        return code;
    }


    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public UserData getData() {
        return data;
    }

    public void setData(UserData data) {
        this.data = data;
    }

    public class UserData {

        @SerializedName("login_msg")
        @Expose
        private String login_msg;

        @SerializedName("login_data")
        @Expose
        private LoginData login_data;

        public UserData() {
        }

        public String getLogin_msg() {
            return login_msg;
        }

        public void setLogin_msg(String login_msg) {
            this.login_msg = login_msg;
        }

        public LoginData getLogin_data() {
            return login_data;
        }

        public void setLogin_data(LoginData login_data) {
            this.login_data = login_data;
        }
    }

    public class LoginData {

        @SerializedName("username")
        @Expose
        private String username;

        @SerializedName("token")
        @Expose
        private String token;

        @SerializedName("user_type")
        @Expose
        private String user_type;

        @SerializedName("user_id")
        @Expose
        private String user_id;

        @SerializedName("is_tajr")
        @Expose
        private String is_tajr;

        @SerializedName("parent_tajr_id")
        @Expose
        private String parent_tajr_id;

        public LoginData() {
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getUser_type() {
            return user_type;
        }

        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getIs_tajr() {
            return is_tajr;
        }

        public void setIs_tajr(String is_tajr) {
            this.is_tajr = is_tajr;
        }

        public String getParent_tajr_id() {
            return parent_tajr_id;
        }

        public void setParent_tajr_id(String parent_tajr_id) {
            this.parent_tajr_id = parent_tajr_id;
        }
    }

    public class Clients {

        @SerializedName("id")
        @Expose
        private String id;

        @SerializedName("name")
        @Expose
        private String name;

        public Clients() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }



//    {
//        "data": {
//        "login_msg": "login successful",
//                "login_data": {
//                    "username": "Mohamed Gamal Mahmoud",
//                    "user_type": "Member",
//                    "user_id": "15",
//                    "is_tajr": "tajr_client",
//                    "parent_tajr_id": "0",
//                    "token": "hiDjfqg8SloeksRrQPt2kTyR+PxYNtFfKCIBxXAt4zX+Y5N9LE44Y5rgUWElqIecRnBdZAaDDGDkWNGnyJSPOA=="
//        }
//       }
//    }


//
//    {
//        "response": "Error",
//            "data": {
//            "login_msg": "invalid email or password"
//            }
//    }
}
