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
    private ComboBox<String> teamComboBox;
    @FXML
    private Button generateReportButton;
    @FXML
    private TextField seasonTextField;

    @FXML
    public void initialize() {
        // Opciones del ComboBox de Reportes
        reportComboBox.getItems().addAll(
                "Estadísticas jugadores por temporada",
                "Jugadores en equipo"
        );
        reportComboBox.getSelectionModel().selectFirst();

        // Opciones del ComboBox de Equipos
        teamComboBox.getItems().addAll(
                "76ers", "Bobcats", "Bucks", "Bulls", "Cavaliers", "Celtics",
                "Clippers", "Grizzlies", "Hawks", "Heat", "Hornets", "Jazz",
                "Kings", "Knicks", "Lakers", "Magic", "Mavericks", "Nets",
                "Nuggets", "Pacers", "Pistons", "Raptors", "Rockets", "Spurs", "Suns"
        );
        teamComboBox.getSelectionModel().selectFirst();

        // Listener para ocultar el campo de temporada si se selecciona "Jugadores en equipo"
        reportComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ("Jugadores en equipo".equals(newValue)) {
                seasonTextField.setVisible(false);
            } else {
                seasonTextField.setVisible(true);
            }
        });
    }

    @FXML
    public void generateReport() {
        try{
            Class.forName("org.mariadb.jdbc.Driver");
            Connection conexion = DriverManager.getConnection("jdbc:mariadb://localhost:3306/nba","root","");

            Map parametros = new HashMap();
            parametros.put("temporada", seasonTextField.getText());
            parametros.put("nombre_equipo",teamComboBox.getSelectionModel().getSelectedItem());
            if(reportComboBox.getSelectionModel().getSelectedItem().equals("Estadísticas jugadores por temporada")) {

                JasperPrint print = JasperFillManager.fillReport("/media/alumno/3EF5-1E86/DAM/Segundo/Interfaces/UT5/Informe2Def/demo/src/Informes/nba_estadisticas_jugadores_def_2.jasper", parametros, conexion);
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