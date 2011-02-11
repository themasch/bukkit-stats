/* ----------------------------------------------------------------------------
 * Mark Schmale <ma.schmale@googlemail.com>
 * ----------------------------------------------------------------------------
 * Solange Sie diesen Vermerk nicht entfernen, können Sie mit der Datei machen,
 * was Sie möchten. Wenn wir uns eines Tages treffen und Sie denken, die Datei
 * ist es wert, können Sie mir dafür ein Bier ausgeben.
 * ----------------------------------------------------------------------------
 */

package it.masch.stats;

import java.util.Properties;
import java.io.FileInputStream;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;
/**
 *
 * @author masch
 */
public class statsLogger {

    protected Connection con = null;
    protected PreparedStatement stmt = null;
    protected boolean connected = false;
    protected SimpleDateFormat format = null;

    public statsLogger() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            // load config
            Properties config = new Properties();
            FileInputStream fis = new FileInputStream("stats.properties");
            config.load(fis);
            fis.close();

            this.format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            this.format.setCalendar(GregorianCalendar.getInstance());

            // try to connect
            String url  = config.getProperty("url");
            String user = config.getProperty("user");
            String pass = config.getProperty("password");
            
            if(user == null || pass == null) {
                this.con = DriverManager.getConnection(url);
            } else {
                this.con = DriverManager.getConnection(url, user, pass);
            }
            this.stmt = this.con.prepareStatement("INSERT INTO `log` (`date`, `player`, "
                                                                 + "`material`, `action`) "
                                                  + "VALUES (?, ?, ?, ?) ON DUPLICATE KEY "
                                                  + "UPDATE `count` = `count` + 1");
            this.connected = true;
        }
        catch(java.lang.ClassNotFoundException ex) {
            System.out.println("ERROR: can`t load database driver");
            System.out.println(ex.getMessage());
        }
        catch(java.lang.InstantiationException ex) {
            System.out.println("ERROR: can`t load database driver");
            System.out.println(ex.getMessage());
        }
        catch(java.lang.IllegalAccessException ex) {
            System.out.println("ERROR: can`t load database driver");
            System.out.println(ex.getMessage());
        }
        catch(java.io.FileNotFoundException ex) {
            System.out.println("ERROR: can`t find stats.properties file");
        }
        catch(java.io.IOException ex) {
            System.out.println("ERROR: can`t read stats.properties file");
        }
        catch(java.sql.SQLException sqlex) {
            System.out.println("ERROR: can`t connect to the database");
            System.out.println(sqlex.getMessage());
        }
    }

    public void logBlock(String player, Integer material, boolean destroyed)
    {
        String date = format.format(GregorianCalendar.getInstance().getTime());
        try {
            this.stmt.setString(1, date);
            this.stmt.setString(2, player);
            this.stmt.setInt(3, material);
            if(destroyed) {
                this.stmt.setString(4, "T");
            }  else {
                this.stmt.setString(4, "P");
            }
            this.stmt.execute();
        }
        catch(java.sql.SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
