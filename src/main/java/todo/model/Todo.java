package todo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Todo {

    private int id;
    private String name;
    private String createdDate;
    private String deadline;
    private Status status;
    private int userId;


    }





