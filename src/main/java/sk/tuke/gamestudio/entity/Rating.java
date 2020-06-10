package sk.tuke.gamestudio.entity;

import java.sql.Timestamp;
import javax.persistence.*;

@Entity
@NamedQueries({
        @NamedQuery(name="Rating.getAverageRating",
                query="SELECT AVG(rating) FROM Rating"),
        @NamedQuery(name="Rating.getRating",
                query="SELECT rating FROM Rating r WHERE r.player = :name and r.game =:game"),
        @NamedQuery(name="Rating.deleteRating",
                query="DELETE FROM Rating WHERE player = :name and game =:game"),
        @NamedQuery(name="Rating.getAllRatings",
                query="SELECT r FROM Rating r WHERE r.game =:game ORDER BY r.ratedon DESC"),
})

public class Rating {
    @Id
    @GeneratedValue
    private int ident;
    private String player;
    private String game;
    private int rating;
    private Timestamp ratedon;

    public Rating(String player, String game, int rating, Timestamp ratedon) {
        this.player = player;
        this.game = game;
        this.rating = rating;
        this.ratedon = ratedon;
    }

    public Rating() {
        this.rating = 0;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Timestamp getRatedon() {
        return ratedon;
    }

    public void setRatedon(Timestamp ratedon) {
        this.ratedon = ratedon;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Rating{");
        sb.append("player='").append(player).append('\'');
        sb.append("ident='").append(ident).append('\'');
        sb.append(", game='").append(game).append('\'');
        sb.append(", rating=").append(rating);
        sb.append(", ratedon=").append(ratedon);
        sb.append('}');
        return sb.toString();
    }

    public int getIdent() { return ident; }
    public void setIdent(int ident) { this.ident = ident; }
}
