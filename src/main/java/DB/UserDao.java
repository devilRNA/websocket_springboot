package DB;

import java.util.List;


// 接口
public interface UserDao {
    public User findUserById(int id) throws Exception ;
    public List<User> findAllUsers() throws Exception;
    public boolean verify(int id ,String password)throws Exception;


}
