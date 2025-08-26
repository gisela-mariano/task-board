package br.com.ui;

import br.com.persistence.entity.BoardEntity;
import br.com.service.BoardQueryService;
import lombok.AllArgsConstructor;

import java.sql.SQLException;
import java.util.Scanner;

import static br.com.persistence.config.ConnectionConfig.getConnection;

@AllArgsConstructor
public class BoardMenu {
    private final Scanner scanner = new Scanner(System.in).useDelimiter("\n");

    private final BoardEntity entity;

    public void execute() {
        try {

            System.out.printf("Você está acessando o board %s, selecione a opção desejada:", entity.getName());

            var option = -1;

            while (option != 9) {
                System.out.println("1- Criar um novo card.");
                System.out.println("2- Mover um card.");
                System.out.println("3- Bloquear um card.");
                System.out.println("4- Desbloquear um card.");
                System.out.println("5- Cancelar um card.");
                System.out.println("6- Visualizar board.");
                System.out.println("7- Visualizar coluna com cards.");
                System.out.println("8- Visualizar card.");
                System.out.println("9- Voltar para o menu anterior.");
                System.out.println("10- Sair.");

                option = scanner.nextInt();

                switch (option) {
                    case 1 -> createCard();
                    case 2 -> moveCardToNextColumn();
                    case 3 -> blockCard();
                    case 4 -> unblockCard();
                    case 5 -> cancelCard();
                    case 6 -> viewBoard();
                    case 7 -> showColumn();
                    case 8 -> showCard();
                    case 9 -> System.out.println("Voltando para o menu anterior.");
                    case 10 -> System.exit(0);
                    default -> System.out.println("Opção inválida, informe uma opção do menu.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private void createCard() {
    }

    private void moveCardToNextColumn() {
    }

    private void blockCard() {
    }

    private void unblockCard() {
    }

    private void cancelCard() {
    }

    private void viewBoard() throws SQLException {
        try (var connection = getConnection()) {
            var optional = new BoardQueryService(connection).showBoardDetails(entity.getId());

            optional.ifPresent(board -> {
                System.out.printf("Board [%s: %s]\n", board.id(), board.name());

                board.columns().forEach(column -> {
                    System.out.printf(
                            "Coluna [%s] tipo: [%s] tem %s cards\n",
                            column.name(),
                            column.type(),
                            column.cardsAmount()
                    );
                });
            });
        }
    }

    private void showColumn() {
    }

    private void showCard() {
    }
}
