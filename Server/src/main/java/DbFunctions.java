
import java.sql.*;

public class DbFunctions {
    public Connection connectToDb(String dbName, String user, String pass)
    {
    Connection connection = null;
    try{
        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+ dbName, user, pass);
        if(connection != null){
            System.out.println("Connection to DB established!");
        }
        else{
            System.out.println("Connection to DB failed!");
        }
    }
    catch (Exception e){
        System.out.println(e);
    }
    return connection;
    }

    public  boolean search_by_name(Connection connection, String tableName,String field, String name){
        Statement statement;
        ResultSet resultSet = null ;
        try {
            String query = String.format("select username from public.\"%s\" where %s = '%s'", tableName,field, name);
            statement = connection.createStatement();
            resultSet=statement.executeQuery(query);
          if(resultSet.next() == false)
              return  false;
          return true;

        }catch (Exception e){
            System.out.println(e);
        }

        return false;
    }
    public void insert_user(Connection connection, String tableName, User user)
    {
        Statement statement;
        try{
            String query = String.format("INSERT INTO public.\"%s\"(username, password, question, answer) " +
                    "VALUES('%s','%s','%s','%s')", tableName, user.getUsername(), user.getPassword(), user.getQuestion(), user.getAnswer());
            statement = connection.createStatement();
            statement.executeQuery(query);
        }catch (Exception e){
            System.out.println(e);
        }
    }

}
