package com.example.navigationbar.data;


public final class TeamData {

    private String userName;
    private String userMb;


    public final String getTeamName() {
        return this.userName;
    }

    public final void setTeamName(String var1) {
        this.userName = var1;
    }

    public final String getTeamDetail() {
        return this.userMb;
    }

    public final void setTeamDetail(String var1) {
        this.userMb = var1;
    }

    public TeamData(String userName, String userMb) {
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

    public final TeamData copy(String userName, String userMb) {
        return new TeamData(userName, userMb);
    }

    public static TeamData copy$default(TeamData var0, String var1, String var2, int var3, Object var4) {
        if ((var3 & 1) != 0) {
            var1 = var0.userName;
        }

        if ((var3 & 2) != 0) {
            var2 = var0.userMb;
        }

        return var0.copy(var1, var2);
    }

    public String toString() {
        return "TeamData(userName=" + this.userName + ", userMb=" + this.userMb + ")";
    }

    public int hashCode() {
        String var10000 = this.userName;
        int var1 = (var10000 != null ? var10000.hashCode() : 0) * 31;
        String var10001 = this.userMb;
        return var1 + (var10001 != null ? var10001.hashCode() : 0);
    }

    public boolean equals(Object var1) {
        if (this != var1) {
            if (var1 instanceof TeamData) {
                TeamData var2 = (TeamData)var1;
                return true;
            }

            return false;
        } else {
            return true;
        }
    }
}
