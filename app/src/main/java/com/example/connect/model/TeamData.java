package com.example.navigationbar.model;


public final class TeamData {

    private String teamName;
    private String teamDetail;


    public final String getTeamName() {
        return this.teamName;
    }

    public final void setTeamName(String var1) {
        this.teamName = var1;
    }

    public final String getTeamDetail() {
        return this.teamDetail;
    }

    public final void setTeamDetail(String var1) {
        this.teamDetail = var1;
    }

    public TeamData(String teamName, String teamDetail) {
        super();
        this.teamName = teamName;
        this.teamDetail = teamDetail;
    }

    public final String component1() {
        return this.teamName;
    }

    public final String component2() {
        return this.teamDetail;
    }

    public final TeamData copy(String userName, String userMb) {
        return new TeamData(userName, userMb);
    }

    public static TeamData copy$default(TeamData var0, String var1, String var2, int var3, Object var4) {
        if ((var3 & 1) != 0) {
            var1 = var0.teamName;
        }

        if ((var3 & 2) != 0) {
            var2 = var0.teamDetail;
        }

        return var0.copy(var1, var2);
    }

    public String toString() {
        return "TeamData(teamName=" + this.teamName + ", teamDetail=" + this.teamDetail + ")";
    }

    public int hashCode() {
        String var10000 = this.teamName;
        int var1 = (var10000 != null ? var10000.hashCode() : 0) * 31;
        String var10001 = this.teamDetail;
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
