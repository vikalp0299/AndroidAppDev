package com.example.connect.model;

public final class Team {

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

    public Team(String teamName, String teamDetail) {
        super();
        this.teamName = teamName;
        this.teamDetail = teamDetail;
    }

    public String toString() {
        return "Team(teamName=" + this.teamName + ", teamDetail=" + this.teamDetail + ")";
    }

    public int hashCode() {
        String var10000 = this.teamName;
        int var1 = (var10000 != null ? var10000.hashCode() : 0) * 31;
        String var10001 = this.teamDetail;
        return var1 + (var10001 != null ? var10001.hashCode() : 0);
    }

    public boolean equals(Object var1) {
        if (this != var1) {
            if (var1 instanceof Team) {
                Team var2 = (Team)var1;
                return true;
            }

            return false;
        } else {
            return true;
        }
    }
}
