package android.portfolio.petshuddle.Entity;

public class Friend {

    private int petId;
    private int friendId;

    public Friend(int petId, int friendId) {
        this.petId = petId;
        this.friendId = friendId;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }
}
