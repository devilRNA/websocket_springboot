package DB;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.List;


// 实现类
public class UserDaoImpl implements UserDao {

    public User findUserById(int id) throws Exception {
        String resource = "SqlMapConfig.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = factory.openSession();
        //---------------
        User user = session.selectOne("user.findUserById",id); //参数一：namespace.id
        //--------------
        session.close();
        return user;
    }

    public List<User> findAllUsers() {
        SqlSession session = new Session().getSession();
        List<User> users = session.selectList("user.findUserAll");
        //----------------------
        session.close();
        return users;
    }
    public boolean verify(int id,String password){
        Tools tools=new Tools();
        boolean result=false;
        SqlSession session = new Session().getSession();
        User user = session.selectOne("user.findUserById",id);
        String real_password=user.getPassword();
        if(tools.verifySHA(password,real_password)){
            result=true;
        }

        session.close();
        return result;
    }




}
