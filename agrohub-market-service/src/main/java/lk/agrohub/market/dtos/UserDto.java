package lk.agrohub.market.dtos;

import lk.agrohub.market.model.User;

public class UserDto {
    private User user;
    private Integer avgRating;
    private String imagePath;

    public UserDto(User user, Integer avgRating, String imagePath) {
        super();
        this.user = user;
        this.avgRating = avgRating;
        this.imagePath = imagePath;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Integer avgRating) {
        this.avgRating = avgRating;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

}
