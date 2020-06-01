package todo;

import todo.interfaces.Commands;
import todo.manager.TodoManager;
import todo.manager.UserManager;
import todo.model.Gender;
import todo.model.Status;
import todo.model.Todo;
import todo.model.User;

import java.sql.SQLException;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main implements Commands {

    public static Scanner scanner = new Scanner(System.in);
    public static UserManager userManager = new UserManager();
    public static TodoManager todoManager = new TodoManager();
    private static User currentUser = null;

    public static void main(String[] args) {
        boolean isRun = true;
        while (isRun) {
            Commands.printMainCommands();
            int command;
            try {
                command = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                command = -1;
            }
            switch (command) {
                case EXIT:
                    isRun = false;
                    break;
                case LOGIN:
                    loginUser();
                    break;
                case REGISTER:
                    registerUser();
                    break;
                default:
                    System.out.println("Wrong command!");
            }
        }
    }

    private static void registerUser() {
        System.out.println("Please input user data " +
                "name,surname,age,gender(MALE,FEMALE),email,password");
        try {
            String userDataStr = scanner.nextLine();
            String[] userDataArr = userDataStr.split(",");

            User userFromStorage = userManager.getUserByEmailAndPassword(userDataArr[4], userDataArr[5]);
            if (userFromStorage == null) {
                User user = new User();
                user.setName(userDataArr[0]);
                user.setSurname(userDataArr[1]);
                user.setAge(Integer.parseInt(userDataArr[2]));
                user.setGender(Gender.valueOf(userDataArr[3].toUpperCase()));
                user.setEmail(userDataArr[4]);
                user.setPassword(userDataArr[5]);
                userManager.addUser(user);
                System.out.println("User was successfully added");
            } else {
                System.out.println("User already exists!");
            }
        } catch (ArrayIndexOutOfBoundsException | SQLException e) {
            System.out.println("Wrong Data!");
        }

    }

    private static void loginUser() {

        System.out.println("Please input email,password");
        try {
            String loginStr = scanner.nextLine();
            String[] loginArr = loginStr.split(",");
            User user = userManager.getUserByEmailAndPassword(loginArr[0], loginArr[1]);
            if (user != null && user.getEmail().equals(loginArr[0]) && user.getPassword().equals(loginArr[1])) {
                currentUser = user;
                loginSuccess();
            } else {
                System.out.println("Wrong email or password");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Wrong Data!");

        }
    }

    private static void loginSuccess() {
        System.out.println("Welcome " + currentUser.getName() + "!");
        boolean isRun = true;
        while (isRun) {
            Commands.printUserCommands();
            int command;
            try {
                command = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                command = -1;
            }
            switch (command) {
                case LOGOUT:
                    isRun = false;
                    break;
                case ADD_TODO:
                    addNewTodo();
                    break;
                case MY_LIST:
                    myList();
                    break;
                case MY_IN_PROGRESS_LIST:
                    myInProgress();
                    break;
                case MY_FINISHED_LIST:
                    myInFinished();
                    break;
                case CHANGE_TODO_STATUS:
                    changeTodoStatus();
                    break;
                case DELETE_TODO_BY_ID:
                    deleteById();
                    break;
                default:
                    System.out.println("Wrong command!");
            }
        }
    }

    private static void deleteById() {
        myList();
        System.out.println("Please input id");
        String changeStr = scanner.nextLine();
        try {
            todoManager.deleteTodoById(Integer.parseInt(changeStr));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void changeTodoStatus() {
        myList();
        System.out.println("Please input change todo data status,id");
        try {
            String changeStr = scanner.nextLine();
            String[] changenArr = changeStr.split(",");
            todoManager.changeTodoStatus(Integer.parseInt(changenArr[1]), changenArr[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Wrong Data!");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void myInFinished() {
        List<Todo> todos = todoManager.myInFinishedList();
        for (Todo todo : todos) {
            System.out.println(todo);
        }
    }


    public static void myList() {
        try {
            List<Todo> todos = todoManager.myList();
            for (Todo todo : todos) {
                System.out.println(todo);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void myInProgress() {
        List<Todo> todos = todoManager.myInProgressList();
        for (Todo todo : todos) {
            System.out.println(todo);
        }
    }

    private static void addNewTodo() {
        System.out.println("Please input todo data name, createdDate, deadline, status, userId");
        System.out.println("Please choose status name from list: " + Arrays.toString(Status.values()));
        try {
            String todoDataStr = scanner.nextLine();
            String[] todoDataArr = todoDataStr.split(",");
            Todo todo = new Todo();
            todo.setName(todoDataArr[0]);
            todo.setCreatedDate(todoDataArr[1]);
            todo.setDeadline(todoDataArr[2]);
            todo.setStatus(Status.valueOf(todoDataArr[3].toUpperCase()));
            todo.setUserId(Integer.parseInt(todoDataArr[4]));

            todoManager.addTodo(todo);
            System.out.println("Todo was successfully added");
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Wrong Data!");
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
     
    