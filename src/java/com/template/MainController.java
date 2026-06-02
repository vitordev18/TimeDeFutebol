package com.template;

import com.template.model.dao.TeamDAO;
import com.template.model.dto.TeamDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class MainController {
    @FXML private TextField txtSearch;
    @FXML private TextField txtNome;
    @FXML private TextField txtCidade;
    @FXML private TextField txtEstadio;
    @FXML private TextField txtLiga;
    @FXML private Button btnSave;
    @FXML private Button btnClear;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private TableView<TeamDTO> tableView;
    @FXML private TableColumn<TeamDTO, String> colNome;
    @FXML private TableColumn<TeamDTO, String> colCidade;
    @FXML private TableColumn<TeamDTO, String> colEstadio;
    @FXML private TableColumn<TeamDTO, String> colLiga;

    private TeamDAO teamDAO = new TeamDAO();
    private ObservableList<TeamDTO> observableTeams;

    @FXML
    private void initialize() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCidade.setCellValueFactory(new PropertyValueFactory<>("city"));
        colEstadio.setCellValueFactory(new PropertyValueFactory<>("stadium"));
        colLiga.setCellValueFactory(new PropertyValueFactory<>("league"));

        loadTableData();

        btnSave.setOnAction(event -> saveTeam());
        btnClear.setOnAction(event -> clearFields());
        btnUpdate.setOnAction(event -> updateTeam());
        btnDelete.setOnAction(event -> deleteTeam());

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtNome.setText(newValue.getName());
                txtCidade.setText(newValue.getCity());
                txtEstadio.setText(newValue.getStadium());
                txtLiga.setText(newValue.getLeague());
            }
        });

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> filterTable(newValue));
    }

    private void loadTableData() {
        try {
            List<TeamDTO> teams = teamDAO.findAll();
            observableTeams = FXCollections.observableArrayList(teams);
            tableView.setItems(observableTeams);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar dados do banco: " + e.getMessage());
        }
    }

    private void saveTeam() {
        if (txtNome.getText().isEmpty() || txtCidade.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Atenção", "Preencha pelo menos o Nome e a Cidade da equipe.");
            return;
        }

        try {
            TeamDTO team = new TeamDTO();
            team.setName(txtNome.getText());
            team.setCity(txtCidade.getText());
            team.setStadium(txtEstadio.getText());
            team.setLeague(txtLiga.getText());

            teamDAO.create(team);

            clearFields();
            loadTableData();
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Equipe cadastrada com sucesso!");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Falha ao salvar equipe: " + e.getMessage());
        }
    }

    private void updateTeam() {
        TeamDTO selectedTeam = tableView.getSelectionModel().getSelectedItem();

        if (selectedTeam == null) {
            showAlert(Alert.AlertType.WARNING, "Atenção", "Selecione uma equipe na tabela para editar.");
            return;
        }

        try {
            selectedTeam.setName(txtNome.getText());
            selectedTeam.setCity(txtCidade.getText());
            selectedTeam.setStadium(txtEstadio.getText());
            selectedTeam.setLeague(txtLiga.getText());

            teamDAO.update(selectedTeam);

            clearFields();
            loadTableData();
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Equipe atualizada com sucesso!");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Falha ao atualizar equipe: " + e.getMessage());
        }
    }

    private void deleteTeam() {
        TeamDTO selectedTeam = tableView.getSelectionModel().getSelectedItem();

        if (selectedTeam == null) {
            showAlert(Alert.AlertType.WARNING, "Atenção", "Selecione uma equipe na tabela para excluir.");
            return;
        }

        try {
            teamDAO.delete(selectedTeam.getId());

            clearFields();
            loadTableData();
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Equipe excluída com sucesso!");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Falha ao excluir equipe: " + e.getMessage());
        }
    }

    private void clearFields() {
        txtNome.clear();
        txtCidade.clear();
        txtEstadio.clear();
        txtLiga.clear();
        txtSearch.clear();
        tableView.getSelectionModel().clearSelection();
    }

    private void filterTable(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            tableView.setItems(observableTeams);
            return;
        }

        String lowerCaseFilter = keyword.toLowerCase();
        ObservableList<TeamDTO> filteredData = FXCollections.observableArrayList();

        for (TeamDTO team : observableTeams) {
            if (team.getName().toLowerCase().contains(lowerCaseFilter) ||
                team.getCity().toLowerCase().contains(lowerCaseFilter) ||
                team.getLeague().toLowerCase().contains(lowerCaseFilter)) {
                filteredData.add(team);
            }
        }
        tableView.setItems(filteredData);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}