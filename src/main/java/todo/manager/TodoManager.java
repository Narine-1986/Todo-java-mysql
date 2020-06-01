package todo.manager;

import todo.db.DBConnectionProvider;
import todo.model.Status;
import todo.model.Todo;

import java.sql.*;

import java.util.LinkedList;
import java.util.List;

public class TodoManager {

    private Connection connection;

    public TodoManager() {

        connection = DBConnectionProvider.getInstance().getConnection();
    }
    public void addTodo(Todo todo) throws SQLException {


        PreparedStatement preparedStatement = connection.prepareStatement("Insert into todos (name,created_date,deadline,status,user_id) Values(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, todo.getName());
        preparedStatement.setString(2, todo.getCreatedDate());
        preparedStatement.setString(3, todo.getDeadline());
        preparedStatement.setString(4, (todo.getStatus().name()));
        preparedStatement.setInt(5, todo.getUserId());
        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        if (resultSet.next()) {
            int id = resultSet.getInt(1);
            todo.setId(id);
        }

    }


    List<Todo> todos = new LinkedList<>();

    public List<Todo> myList() throws SQLException {
       select("SELECT * FROM todos ");
        return todos;
    }


    public List<Todo> myInProgressList() {
        try {
            select("SELECT * FROM todos WHERE status = 'IN_PROGRESS'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return todos;

    }
    public List<Todo> myInFinishedList() {
        try {
            select("SELECT * FROM todos WHERE status = 'FINISHED'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return todos;

    }


    public void select(String query) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            Todo todo = new Todo();
            todo.setId(resultSet.getInt("id"));
            todo.setName(resultSet.getString("name"));
            todo.setCreatedDate(resultSet.getString("created_date"));
            todo.setDeadline(resultSet.getString("deadline"));
            todo.setStatus(Enum.valueOf(Status.class, resultSet.getString("status")));
            todo.setUserId(resultSet.getInt("user_id"));
            todos.add(todo);

        }

    }

    public void changeTodoStatus(int id, String status) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE todos SET status=?   WHERE id = ? ");
        preparedStatement.setString(1, String.valueOf(status));
        preparedStatement.setInt(2, id);
        preparedStatement.executeUpdate();
    }


    public void deleteTodoById(int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM todos WHERE `id`=?");
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }

}

