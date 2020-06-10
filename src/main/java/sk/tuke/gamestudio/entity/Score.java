package sk.tuke.gamestudio.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;

@Entity
@NamedQueries({
        @NamedQuery( name = "Score.getBestScores",
                query = "SELECT s FROM Score s WHERE s.game=:game ORDER BY s.points DESC"),
        @NamedQuery( name = "Score.getScore",
                query = "SELECT s FROM Score s WHERE s.game=:game and s.player=:player ORDER BY s.points DESC"),
        @NamedQuery(name="Score.deleteScore",
                query="DELETE FROM Score WHERE player = :name and game =:game"),
})

public class Score implements Comparable<Score>, Serializable {
    @Id
    @GeneratedValue
    private int ident;
    private String game;
    private String player;
    private int points;
    private Timestamp playedOn;
    private int actualSteps;
    private int actualDeaths;
    private int totalDeaths;
    private int totalSteps;

    public Score(String game, String player, int points, Timestamp playedOn) {
        this.game = game;
        this.player = player;
        this.points = points;
        this.playedOn = playedOn;
        this.actualDeaths = 0;
        this.actualSteps = 0;
        this.totalSteps = 0;
        this.actualDeaths = 0;
        this.totalDeaths = 0;
    }

    public Score(){
        this.actualDeaths = 0;
        this.actualSteps = 0;
        this.totalSteps = 0;
        this.actualDeaths = 0;
        this.totalDeaths = 0;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Timestamp getPlayedOn() {
        return playedOn;
    }

    public void setPlayedOn(Timestamp playedOn) {
        this.playedOn = playedOn;
    }

    @Override
    public String toString() {
        return "Score{" +
                "game='" + game + '\'' +
                ", player='" + player + '\'' +
                ", ident='" + ident + '\'' +
                ", points=" + points +
                ", playedOn=" + playedOn +
                '}';
    }

    @Override
    public int compareTo(Score o) {
        if(o == null) return -1;
        return this.getPoints() - o.getPoints();
    }

    public int getSteps() {
        return totalSteps;
    }

    public void increaseSteps() {
        this.actualSteps++;
        this.totalSteps++;
    }

    public int getDeaths() {
        return actualDeaths;
    }

    public void increaseDeaths() {
        this.actualDeaths++;
        this.totalDeaths++;
    }

    public void resetSteps(){
        this.totalSteps -= this.actualSteps;
        this.actualSteps = 0;
    }

    public void resetDeaths(){
        this.actualDeaths = 0;
    }

    public int getActualDeaths() {
        return actualDeaths;
    }

    public int getActualSteps() {
        return actualSteps;
    }

    public void updatePoints(){
        if(totalSteps > 0)
        points = Math.round (10000/((float)totalSteps + (float)totalDeaths));
        this.actualSteps = 0;
    }

    public void resetAll(){
        this.actualDeaths = 0;
        this.actualSteps = 0;
        this.totalSteps = 0;
        this.totalDeaths = 0;
    }

    public int getIdent() { return ident; }

    public void setIdent(int ident) { this.ident = ident; }
}
