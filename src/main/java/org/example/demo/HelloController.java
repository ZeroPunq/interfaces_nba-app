package org.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

public class HelloController {
    @FXML
    private ComboBox<String> reportComboBox;
    @FXML
    private Button generateReportButton;
    @FXML
    private TextField seasonTextField;
    @FXML
    public void initialize() {
        // Opciones del ComboBox
        reportComboBox.getItems().addAll("Estadísticas jugadores por temporada", "Jugadores en equipo");
        reportComboBox.getSelectionModel().selectFirst(); // Selección predeterminada

    }
    @FXML
    public void generateReport() {
        try{
            Class.forName("org.mariadb.jdbc.Driver");
            Connection conexion = DriverManager.getConnection("jdbc:mariadb://localhost:3306/nba","root","");

            Map parametros = new HashMap();
            parametros.put("temporada", seasonTextField.getText());
            if(reportComboBox.getSelectionModel().getSelectedItem().equals("Estadísticas jugadores por temporada")) {

                JasperPrint print = JasperFillManager.fillReport("/media/alumno/3EF5-1E86/DAM/Segundo/Interfaces/UT5/Informe2Def/demo/src/Informes/nba_estadisticas_jugadores_def.jasper", parametros, conexion);
                JasperExportManager.exportReportToPdfFile(print, "/media/alumno/3EF5-1E86/DAM/Segundo/Interfaces/UT5/Informe2Def/demo/src/Informes/nba_estadisticas_jugadores.pdf");
            }else if (reportComboBox.getSelectionModel().getSelectedItem().equals("Jugadores en equipo")) {
                JasperPrint print = JasperFillManager.fillReport("/media/alumno/3EF5-1E86/DAM/Segundo/Interfaces/UT5/Informe2Def/demo/src/Informes/nba_jugadores_equipos.jasper", parametros, conexion);
                JasperExportManager.exportReportToPdfFile(print, "/media/alumno/3EF5-1E86/DAM/Segundo/Interfaces/UT5/Informe2Def/demo/src/Informes/nba_jugadores_equipos.pdf");
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
    }
}