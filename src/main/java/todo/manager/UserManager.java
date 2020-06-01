package todo.manager;
import todo.db.DBConnectionProvider;
import todo.model.Gender;
import todo.model.User;

import java.sql.*;

public class UserManager {

    private Connection connection;

    public UserManager() {

        connection = DBConnectionProvider.getInstance().getConnection();
    }

    public void addUser(User user) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement("Insert into user (name,surname,age,gender,email,password) Values(?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, user.getName());
        preparedStatement.setString(2, user.getSurname());
        preparedStatement.setInt(3, user.getAge());
        preparedStatement.setString(4,user.getGender().name());
        preparedStatement.setString(5, user.getEmail());
        preparedStatement.setString(6, user.getPassword());

        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        if (resultSet.next()) {
            int id = resultSet.getInt(1);
            user.setId(id);
        }


    }

    public User getUserByEmailAndPassword(String email,String password) {

        try{
            PreparedStatement preparedStatement=connection.prepareStatement("SELECT * FROM user WHERE `email` = ? AND `password`=?");
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt(1));
                user.setName(resultSet.getString(2));
                user.setSurname(resultSet.getString(3));
                user.setAge(resultSet.getInt(4));
                user.setGender(Gender.valueOf(resultSet.getString(5)));
                user.setEmail(resultSet.getString(6));
                user.setPassword(resultSet.getString(7));
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

       return null;
    }

}
