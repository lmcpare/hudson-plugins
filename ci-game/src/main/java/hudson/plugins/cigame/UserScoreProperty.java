package hudson.plugins.cigame;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

import hudson.model.User;
import hudson.model.UserProperty;

/**
 * 
 * @author Erik Ramfelt
 */
@ExportedBean(defaultVisibility = 999)
public class UserScoreProperty extends UserProperty {

    private double score;
    
    /** Inversed name as default value is false when serializing from data that
     * has doesnt have the value. */
    private boolean isNotParticipatingInGame;

    public UserScoreProperty() {
        score = 0;
        isNotParticipatingInGame = false;
    }
    
    @DataBoundConstructor
    public UserScoreProperty(double score, boolean participatingInGame) {
        this.score = score;
        this.isNotParticipatingInGame = !participatingInGame;
    }

    @Exported
    public User getUser() {
        return user;
    }

    @Exported
    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Exported
    public boolean isParticipatingInGame() {
        return !isNotParticipatingInGame;
    }
    
    @Override
    public String toString() {
        return String.format("UserScoreProperty [isNotParticipatingInGame=%s, score=%s, user=%s]", isNotParticipatingInGame, score, user);
    }
}
