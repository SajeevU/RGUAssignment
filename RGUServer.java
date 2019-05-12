import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.sql.*;  

public class RGUServer {
    public static void main(String[] args) throws IOException 
    {

        try (ServerSocket  listener = new ServerSocket(59090)) {
          //Creating the Server 
            System.out.println("The RGU Server is running...");
            while (true) {
                try (Socket  socket = listener.accept()) {
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.println(new Date().toString());
                    //Parts Related to Create Database Connection and retrieve Data
                    Connection con=DriverManager.getConnection(  
                                  "jdbc:mysql://localhost:3306/cashout","root","paycollect");  
                    //here sonoo is database name, root is username and password  
                    Statement stmt=con.createStatement();  
                    ResultSet rs=stmt.executeQuery("select * from h5_txn");  
                
                    StringBuffer xmlArray = new StringBuffer("<results>");
                    while (rs.next()) {
                      int total_rows = rs.getMetaData().getColumnCount();
                      xmlArray.append("<result ");
                      for (int i = 0; i < total_rows; i++) {
                          xmlArray.append(" " + rs.getMetaData().getColumnLabel(i + 1)
                                        .toLowerCase() + "='" + rs.getObject(i + 1) + "'"); }
                      xmlArray.append(" />");
                    }

                    xmlArray.append("</results>");

                out.println(xmlArray.toString());
		System.out.println(xmlArray.toString());
                con.close(); 
                }catch(Exception ex)
                {
                  System.out.println(ex);
                }
            }
                
   }
    }

}
