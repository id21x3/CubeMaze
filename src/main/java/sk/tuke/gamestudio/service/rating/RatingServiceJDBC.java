package sk.tuke.gamestudio.service.rating;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.entity.Rating;

import javax.sql.DataSource;
import java.sql.*;

public class RatingServiceJDBC implements RatingService {
    public static final String INSERT_OR_UPDATE = "INSERT INTO rating (player, game, rating, ratedOn) VALUES (?, ?, ?, ?)";
    public static final String SELECT_AVERAGE = "SELECT AVG(rating) FROM rating WHERE game = ?";
    public static final String SELECT_RATING = "SELECT rating FROM rating WHERE game = ? AND player = ?";
    public static final String DELETE = "DELETE FROM rating";

    @Autowired
    private DataSource dataSource;

    @Override
    public void setRating(Rating rating) throws RatingException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_OR_UPDATE)
        ) {
            statement.setString(1, rating.getPlayer());
            statement.setString(2, rating.getGame());
            statement.setInt(3, rating.getRating());
            statement.setTimestamp(4, new Timestamp(rating.getRatedOn().getTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RatingException("Problem setting rating", e);
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_AVERAGE)
        ) {
            statement.setString(1, game);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        } catch (SQLException e) {
            throw new RatingException("Problem getting average rating", e);
        }
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_RATING)
        ) {
            statement.setString(1, game);
            statement.setString(2, player);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        } catch (SQLException e) {
            throw new RatingException("Problem getting rating", e);
        }
    }

    @Override
    public void reset() throws RatingException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(DELETE);
        } catch (SQLException e) {
            throw new RatingException("Problem deleting ratings", e);
        }
    }
}
