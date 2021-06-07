package me.unldenis.bedwars.object.clans;

public enum ClanState
{
    INLOBBY("INLOBBY", 0), 
    MATCHMAKING("MATCHMAKING", 1), 
    PLAYING("PLAYING", 2);
    
    private ClanState(final String name, final int ordinal) {
    }
}
