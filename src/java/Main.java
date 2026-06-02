import com.template.model.dao.TeamDAO;
import com.template.model.dto.TeamDTO;

private static final Scanner scanner = new Scanner(System.in);
private static final TeamDAO dao = new TeamDAO();

void main() {
    int option;
    do {
        showMenu();
        option = scanner.nextInt();
        scanner.nextLine();
        switch (option) {
            case 1 -> createTeam();
            case 2 -> listTeams();
            case 3 -> updateTeam();
            case 4 -> deleteTeam();
            default -> IO.println("\nInvalid option");
        }
    } while (option != 5);
}

private static void showMenu() {
    IO.println("\n TEAM MANAGEMENT");
    IO.println("1 - Create Team");
    IO.println("2 - List Teams");
    IO.println("3 - Update Team");
    IO.println("4 - Delete Team");
    IO.print("Choose option: ");
}

private static void createTeam() {
    TeamDTO team = new TeamDTO();
    IO.print("Name: ");
    team.setName(scanner.nextLine());
    IO.print("City: ");
    team.setCity(scanner.nextLine());
    IO.print("Stadium: ");
    team.setStadium(scanner.nextLine());
    IO.print("League: ");
    team.setLeague(scanner.nextLine());
    dao.create(team);
    IO.println("\nTeam created");
}

private static void listTeams() {
    List<TeamDTO> teams = dao.findAll();
    if (teams.isEmpty()) {
        IO.println("\n No teams");
        return;
    }
    IO.println("\n TEAM LIST");
    for (TeamDTO t : teams) {
        System.out.printf(
            "ID: %d | Name: %s | City: %s | Stadium: %s | League: %s%n",
            t.getId(),
            t.getName(),
            t.getCity(),
            t.getStadium(),
            t.getLeague()
        );
    }
}

private static void updateTeam() {
    TeamDTO team = new TeamDTO();
    IO.print("Enter ID: ");
    team.setId(scanner.nextInt());
    scanner.nextLine();
    IO.print("New Name: ");
    team.setName(scanner.nextLine());
    IO.print("New City: ");
    team.setCity(scanner.nextLine());
    IO.print("New Stadium: ");
    team.setStadium(scanner.nextLine());
    IO.print("New League: ");
    team.setLeague(scanner.nextLine());
    dao.update(team);
    IO.println("\nTeam updated");
}

private static void deleteTeam() {
    IO.print("Enter ID to delete: ");
    int id = scanner.nextInt();
    dao.delete(id);
    IO.println("\nTeam deleted");
}