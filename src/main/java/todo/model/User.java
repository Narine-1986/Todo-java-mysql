package todo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int id;
    private String name;
    private String surname;
    private int age;
    private Gender gender;
    private String email;
    private String password;

    public User(String name, String surname, int age, Gender gender, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.password = password;
    }
}
