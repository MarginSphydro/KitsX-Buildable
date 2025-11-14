package dev.darkxx.utils.elo;

import java.util.List;
import org.jetbrains.annotations.NotNull;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/elo/ELO.class */
public class ELO {
    @NotNull
    public static List<Integer> elo(@NotNull Ratings<Integer, Integer> ratings, @NotNull Winner winner, int kFactor) {
        int rating1 = ratings.player(1);
        int rating2 = ratings.player(2);
        double expectedScore1 = 1.0d / (1.0d + Math.pow(10.0d, (rating2 - rating1) / 400.0d));
        double expectedScore2 = 1.0d - expectedScore1;
        int score1 = winner.equals(Winner.ONE) ? 1 : 0;
        int score2 = winner.equals(Winner.TWO) ? 1 : 0;
        int newRating1 = (int) Math.round(rating1 + (kFactor * (score1 - expectedScore1)));
        int newRating2 = (int) Math.round(rating2 + (kFactor * (score2 - expectedScore2)));
        if (newRating1 < 0) {
            newRating1 = 0;
        }
        if (newRating2 < 0) {
            newRating2 = 0;
        }
        return List.of(Integer.valueOf(newRating1), Integer.valueOf(newRating2));
    }
}
