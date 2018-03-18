package it.fanciullini.response;

import it.fanciullini.data.entity.User;
import lombok.Data;

@Data
public class UsersResponse extends User {

private User user;
private Double importTotal;


    public UsersResponse(User user){
        super(user);
    }

}
