package com.example.navigationbar.model;


public final class UserData {

    private String userName;
    private String userMb;


    public final String getUserName() {
        return this.userName;
    }

    public final void setUserName(String var1) {
        this.userName = var1;
    }

    public final String getUserMb() {
        return this.userMb;
    }

    public final void setUserMb(String var1) {
        this.userMb = var1;
    }

    public UserData(String userName,String userMb) {
        super();
        this.userName = userName;
        this.userMb = userMb;
    }

    public final String component1() {
        return this.userName;
    }

    public final String component2() {
        return this.userMb;
    }

    public final UserData copy(String userName, String userMb) {
        return new UserData(userName, userMb);
    }

    public static UserData copy$default(UserData var0, String var1, String var2, int var3, Object var4) {
        if ((var3 & 1) != 0) {
            var1 = var0.userName;
        }

        if ((var3 & 2) != 0) {
            var2 = var0.userMb;
        }

        return var0.copy(var1, var2);
    }

    public String toString() {
        return "UserData(userName=" + this.userName + ", userMb=" + this.userMb + ")";
    }

    public int hashCode() {
        String var10000 = this.userName;
        int var1 = (var10000 != null ? var10000.hashCode() : 0) * 31;
        String var10001 = this.userMb;
        return var1 + (var10001 != null ? var10001.hashCode() : 0);
    }

    public boolean equals(Object var1) {
        if (this != var1) {
            if (var1 instanceof UserData) {
                UserData var2 = (UserData)var1;
                return true;
            }

            return false;
        } else {
            return true;
        }
    }
}
