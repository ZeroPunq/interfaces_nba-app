package org.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
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
    private Label label;
    @FXML
    private Hyperlink userGuideLink;

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


        // Listener para ocultar el campo de temporada si se selecciona "Jugadores en equipo"
        reportComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ("Jugadores en equipo".equals(newValue)) {
                seasonTextField.setVisible(false);
                label.setVisible(false);
            } else {
                seasonTextField.setVisible(true);
            }
        });

    }



    @FXML
    public void generateReport() {
        try {
            // Cargar el driver de MariaDB
            Class.forName("org.mariadb.jdbc.Driver");
            Connection conexion = DriverManager.getConnection("jdbc:mariadb://localhost:3306/nba", "root", "");

            // Crear parámetros para el informe
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("temporada", seasonTextField.getText());
            parametros.put("nombre_equipo", teamComboBox.getSelectionModel().getSelectedItem());

            // Seleccionar el archivo del informe con rutas absolutas corregidas
            String reportFile = "";
            String defaultFileName = "reporte.pdf";

            if (reportComboBox.getSelectionModel().getSelectedItem().equals("Estadísticas jugadores por temporada")) {
                reportFile = "Informes/nba_estadisticas_jugadores_def_2.jasper";
                defaultFileName = "nba_estadisticas_jugadores.pdf";
            } else if (reportComboBox.getSelectionModel().getSelectedItem().equals("Jugadores en equipo")) {
                reportFile = "Informes/nba_jugadores_equipos.jasper";
                defaultFileName = "nba_jugadores_equipos.pdf";
            }

            // Verificar si la ruta del informe es válida
            if (reportFile.isEmpty()) {
                System.out.println("No se seleccionó un informe válido.");
                return;
            }

            // Llenar el informe con los datos de la base de datos
            JasperPrint print = JasperFillManager.fillReport(reportFile, parametros, conexion);

            // Usar FileChooser para seleccionar la ubicación donde se guardará el PDF
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar Informe PDF");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));
            fileChooser.setInitialFileName(defaultFileName);

            File selectedFile = fileChooser.showSaveDialog(new Stage());

            if (selectedFile != null) {
                // Exportar el informe al PDF en la ubicación seleccionada
                JasperExportManager.exportReportToPdfFile(print, selectedFile.getAbsolutePath());
                System.out.println("Informe guardado en: " + selectedFile.getAbsolutePath());
            } else {
                System.out.println("Guardado cancelado.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openUserGuide() {
        try {
            File userGuideFile = new File("user_guide.html");
            if (userGuideFile.exists()) {
                Desktop.getDesktop().browse(userGuideFile.toURI());
            } else {
                System.out.println("No se encontró el archivo de la guía de usuario.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
