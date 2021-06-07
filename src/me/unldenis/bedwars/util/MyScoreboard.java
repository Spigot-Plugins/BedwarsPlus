package me.unldenis.bedwars.util;

import org.bukkit.scoreboard.Objective;

public class MyScoreboard
{
    public String getEntryFromScore(final Objective o, final int score) {
        if (o == null) {
            return null;
        }
        if (!this.hasScoreTaken(o, score)) {
            return null;
        }
        for (final String s : o.getScoreboard().getEntries()) {
            if (o.getScore(s).getScore() == score) {
                return o.getScore(s).getEntry();
            }
        }
        return null;
    }
    
    public boolean hasScoreTaken(final Objective o, final int score) {
        for (final String s : o.getScoreboard().getEntries()) {
            if (o.getScore(s).getScore() == score) {
                return true;
            }
        }
        return false;
    }
    
    public void replaceScore(final Objective o, final int score, final String name) {
        if (this.hasScoreTaken(o, score)) {
            if (this.getEntryFromScore(o, score).equalsIgnoreCase(name)) {
                return;
            }
            if (!this.getEntryFromScore(o, score).equalsIgnoreCase(name)) {
                o.getScoreboard().resetScores(this.getEntryFromScore(o, score));
            }
        }
        o.getScore(name).setScore(score);
    }
}
