import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        SerialPort comPort = SerialPort.getCommPort("COM6");
        comPort.setComPortParameters(115200, 8, 1, 0);

        comPort.openPort();
        Scanner scanner = new Scanner(comPort.getInputStream());
        comPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_RECEIVED; }
            @Override
            public void serialEvent(SerialPortEvent event)
            {
                String longitude = scanner.nextLine();
                String latitude = scanner.nextLine();
                System.out.println("Longitude: " + longitude + " Latitude: " + latitude);
                double longi = Double.parseDouble(longitude);
                double lat = Double.parseDouble(latitude);
                DatabaseConnection connectNow = new DatabaseConnection();
                Connection connectDB = connectNow.getConnection();
                try {

                    if (connectDB != null) {
                        PreparedStatement statement = connectDB.prepareStatement("insert into locatie values (?,?)");
                        statement.setDouble(1, longi);
                        statement.setDouble(2, lat);
                        statement.executeUpdate();

                    } else {
                        System.out.println("Could not establish a connection to the database.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
