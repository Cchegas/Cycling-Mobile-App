package com.example.cyclingmobileapp.lib.user;

public class Profile {
    private ParticipantAccount[] members ;
    private ClubAccount owner ;

    public Profile(ClubAccount account) {
        this.owner = account ;
    }
}
