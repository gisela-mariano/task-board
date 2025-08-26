package br.com.ui;

import br.com.persistence.entity.BoardColumnEntity;
import br.com.persistence.entity.BoardColumnTypeEnum;
import br.com.persistence.entity.BoardEntity;
import br.com.service.BoardQueryService;
import br.com.service.BoardService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static br.com.persistence.config.ConnectionConfig.getConnection;

public class MainMenu {
    private final Scanner scanner = new Scanner(System.in).useDelimiter("\n");;

    public void execute() throws SQLException {
        System.out.println("Gerenciador de boards, escolha a opção desejada:");

        var option = -1;

        while (true) {
            System.out.println("1- Criar um novo board.");
            System.out.println("2- Selecionar um board existente.");
            System.out.println("3- Excluir um board.");
            System.out.println("4- Sair.");

            option = scanner.nextInt();

            switch (option) {
                case 1 -> createBoard();
                case 2 -> selectBoard();
                case 3 -> deleteBoard();
                case 4 -> System.exit(0);
                default -> System.out.println("Opção inválida, informe uma opção do menu.");
            }
        }
    }

    private void createBoard() throws SQLException {
        var entity = new BoardEntity();

        System.out.println("Informe o nome do seu board");
        entity.setName(scanner.next());

        System.out.println("Você deseja inserir colunas adicionais além das 3 padrões? Se sim informe a quantidade, " +
                           "se não digite 0");
        var additionalColumnsQuantity = scanner.nextInt();

        List<BoardColumnEntity> columns = new ArrayList<>();

        System.out.println("Informe o nome da coluna inicial do board");
        var initialColumnName = scanner.next();
        var initialColumn = createColumn(initialColumnName, BoardColumnTypeEnum.INITIAL, 0);
        columns.add(initialColumn);

        for (int i = 0; i < additionalColumnsQuantity; i++) {
            System.out.println("Informe o nome da coluna de tarefa pendente do board");
            var pendingColumnName = scanner.next();
            var pendingColumn = createColumn(pendingColumnName, BoardColumnTypeEnum.PENDING, i + 1);
            columns.add(pendingColumn);
        }

        System.out.println("Informe o nome da coluna final do board");
        var finalColumnName = scanner.next();
        var finalColumn = createColumn(finalColumnName, BoardColumnTypeEnum.FINAL, additionalColumnsQuantity + 1);
        columns.add(finalColumn);

        System.out.println("Informe o nome da coluna de cancelamento do board");
        var cancelColumnName = scanner.next();
        var cancelColumn = createColumn(cancelColumnName, BoardColumnTypeEnum.CANCEL, additionalColumnsQuantity + 2);
        columns.add(cancelColumn);

        entity.setBoardColumns(columns);

        try (var connection = getConnection()) {
            var service = new BoardService(connection);
            service.create(entity);
        }
    }

    private void selectBoard() throws SQLException {
        System.out.println("Informe o id do board que deseja selecionar.");

        var id = scanner.nextLong();

        try (var connection = getConnection()) {
            var queryService = new BoardQueryService(connection);
            var optional = queryService.findById(id);

            optional.ifPresentOrElse(
                    board -> new BoardMenu(board).execute(),
                    () -> System.out.printf("Não foi encontrado um board com od id %s\n", id)
            );
        }
    }

    private void deleteBoard() throws SQLException {
        System.out.println("Informe o id do board que será excluido.");

        var id = scanner.nextLong();

        try (var connection = getConnection()) {
            var service = new BoardService(connection);

            if (service.delete(id)) {
                System.out.printf("O board %s foi excluido com sucesso!\n", id);
                return;
            }

            System.out.printf("Não foi encontrado um board com od id %s\n", id);
        }
    }

    private BoardColumnEntity createColumn(final String name, final BoardColumnTypeEnum type, final int order) {
        var boardColumn = new BoardColumnEntity();

        boardColumn.setName(name);
        boardColumn.setType(type);
        boardColumn.setOrder(order);

        return boardColumn;
    }
}
