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
    @FXML private ComboBox<String> cboLiga;
    @FXML private Button btnSave;
    @FXML private Button btnClear;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private TableView<TeamDTO> tableView;
    @FXML private TableColumn<TeamDTO, String> colNome;
    @FXML private TableColumn<TeamDTO, String> colCidade;
    @FXML private TableColumn<TeamDTO, String> colEstadio;
    @FXML private TableColumn<TeamDTO, String> colLiga;
    @FXML private Label lblStatus;

    private TeamDAO teamDAO = new TeamDAO();
    private ObservableList<TeamDTO> observableTeams;

    @FXML
    private void initialize() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCidade.setCellValueFactory(new PropertyValueFactory<>("city"));
        colEstadio.setCellValueFactory(new PropertyValueFactory<>("stadium"));
        colLiga.setCellValueFactory(new PropertyValueFactory<>("league"));

        cboLiga.setItems(FXCollections.observableArrayList(
            "Brasileirão Série A", "Brasileirão Série B", "Premier League", "La Liga", "Outra"
        ));

        txtNome.setPromptText("Ex: CR Flamengo");
        txtCidade.setPromptText("Ex: Rio de Janeiro");
        txtEstadio.setPromptText("Ex: Maracanã");
        cboLiga.setPromptText("Selecione a Liga");

        loadTableData();
        configurarBotoesIniciais();

        btnSave.setOnAction(event -> saveTeam());
        btnClear.setOnAction(event -> clearFields());
        btnUpdate.setOnAction(event -> updateTeam());
        btnDelete.setOnAction(event -> deleteTeam());

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtNome.setText(newValue.getName());
                txtCidade.setText(newValue.getCity());
                txtEstadio.setText(newValue.getStadium());
                cboLiga.setValue(newValue.getLeague());

                btnUpdate.setDisable(false);
                btnDelete.setDisable(false);
                btnSave.setDisable(true);
            }
        });

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> filterTable(newValue));

        txtNome.requestFocus();
    }

    private void configurarBotoesIniciais() {
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        btnSave.setDisable(false);
    }

    private void loadTableData() {
        try {
            List<TeamDTO> teams = teamDAO.findAll();
            observableTeams = FXCollections.observableArrayList(teams);
            tableView.setItems(observableTeams);
        } catch (Exception e) {
            showStatus("Erro ao carregar dados do banco: " + e.getMessage(), true);
        }
    }

    private void saveTeam() {
        if (txtNome.getText().isEmpty() || txtCidade.getText().isEmpty()) {
            showStatus("Atenção: Preencha os campos obrigatórios (Nome e Cidade)!", true);
            return;
        }

        try {
            TeamDTO team = new TeamDTO();
            team.setName(txtNome.getText());
            team.setCity(txtCidade.getText());
            team.setStadium(txtEstadio.getText());
            team.setLeague(cboLiga.getValue() != null ? cboLiga.getValue() : "");

            teamDAO.create(team);

            clearFields();
            loadTableData();
            showStatus("Equipe cadastrada com sucesso!", false);
        } catch (Exception e) {
            showStatus("Falha ao salvar equipe: " + e.getMessage(), true);
        }
    }

    private void updateTeam() {
        TeamDTO selectedTeam = tableView.getSelectionModel().getSelectedItem();

        if (selectedTeam == null) return;

        try {
            selectedTeam.setName(txtNome.getText());
            selectedTeam.setCity(txtCidade.getText());
            selectedTeam.setStadium(txtEstadio.getText());
            selectedTeam.setLeague(cboLiga.getValue() != null ? cboLiga.getValue() : "");

            teamDAO.update(selectedTeam);

            clearFields();
            loadTableData();
            showStatus("Equipe atualizada com sucesso!", false);
        } catch (Exception e) {
            showStatus("Falha ao atualizar equipe: " + e.getMessage(), true);
        }
    }

    private void deleteTeam() {
        TeamDTO selectedTeam = tableView.getSelectionModel().getSelectedItem();

        if (selectedTeam == null) return;

        try {
            teamDAO.delete(selectedTeam.getId());

            clearFields();
            loadTableData();
            showStatus("Equipe excluída com sucesso!", false);
        } catch (Exception e) {
            showStatus("Falha ao excluir equipe: " + e.getMessage(), true);
        }
    }

    private void clearFields() {
        txtNome.clear();
        txtCidade.clear();
        txtEstadio.clear();
        cboLiga.setValue(null);
        txtSearch.clear();
        tableView.getSelectionModel().clearSelection();

        configurarBotoesIniciais();
        txtNome.requestFocus();
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
                (team.getLeague() != null && team.getLeague().toLowerCase().contains(lowerCaseFilter))) {
                filteredData.add(team);
            }
        }
        tableView.setItems(filteredData);
    }

    private void showStatus(String message, boolean isError) {
        lblStatus.setText(message);
        if (isError) {
            lblStatus.setStyle("-fx-text-fill: #d9534f; -fx-font-weight: bold;");
        } else {
            lblStatus.setStyle("-fx-text-fill: #2b8a3e; -fx-font-weight: bold;");
        }
    }
}