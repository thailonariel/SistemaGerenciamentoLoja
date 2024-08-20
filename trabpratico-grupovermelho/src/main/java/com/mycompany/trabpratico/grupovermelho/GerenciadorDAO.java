package com.mycompany.trabpratico.grupovermelho;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GerenciadorDAO {

    private Connection conn;

    public GerenciadorDAO() {
        try {
            this.conn = Conexao.getConexao();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(GerenciadorDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Métodos para a tabela Usuario
    public boolean inserirUsuario(String nome, String login, String senha) {
        String sql = "INSERT INTO Usuario (nome, login, senha) VALUES (?, ?, ?)";
        return executeUpdate(sql, nome, login, senha);
    }

    public boolean atualizarUsuario(int codigo, String nome, String login, String senha) {
        String sql = "UPDATE Usuario SET nome = ?, login = ?, senha = ? WHERE codigo = ?";
        return executeUpdate(sql, nome, login, senha, codigo);
    }

    public boolean excluirUsuario(int codigo) {
        String sql = "DELETE FROM Usuario WHERE codigo = ?";
        return executeUpdate(sql, codigo);
    }

    public List<String[]> listarComprasDetalhadas() {
        String sql = "SELECT c.data, c.valor_total, rc.valor_unitario, rc.observacao "
                + "FROM Compra c "
                + "JOIN Registro_Compra rc ON c.codigo = rc.COD_Compra";

        ResultSet rs = executeQuery(sql);
        List<String[]> compras = new ArrayList<>();

        try {
            while (rs != null && rs.next()) {
                String[] compra = new String[5];
                compra[0] = rs.getString("data");
                compra[1] = rs.getString("valor_total");
                compra[2] = "1";
                compra[3] = rs.getString("valor_unitario");
                compra[4] = rs.getString("observacao");
                compras.add(compra);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return compras;
    }

    public Usuario buscarUsuario(String login) {
        String sql = "SELECT * FROM Usuario WHERE login = ?";
        ResultSet rs = executeQuery(sql, login);
        try {
            if (rs != null && rs.next()) {
                return new Usuario(
                        rs.getInt("codigo"),
                        rs.getString("nome"),
                        rs.getString("login"),
                        rs.getString("senha")
                );
            }
        } catch (SQLException e) {
            Logger.getLogger(GerenciadorDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public ResultSet listarUsuarios() {
        String sql = "SELECT * FROM Usuario";
        return executeQuery(sql);
    }

    // Métodos para a tabela Caixa
    public boolean inserirCaixa(double valorCaixa) {
        String sql = "INSERT INTO Caixa (valor_caixa) VALUES (?)";
        return executeUpdate(sql, valorCaixa);
    }

    public boolean atualizarCaixaInicial(double valorCaixa) {
        String sql = "UPDATE Caixa SET valor_inicial = ?";
        return executeUpdate(sql, valorCaixa);
    }

    public boolean excluirCaixa(int codigo) {
        String sql = "DELETE FROM Caixa WHERE codigo = ?";
        return executeUpdate(sql, codigo);
    }

    public ResultSet listarCaixas() {
        String sql = "SELECT * FROM Caixa";
        return executeQuery(sql);
    }

    public double getValorCaixa() throws SQLException {
        String sql = "SELECT SUM(valor_caixa) FROM Caixa";
        double somaValorCaixa = 0;

        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                somaValorCaixa = rs.getDouble(1);
            }
        }

        return somaValorCaixa;
    }

    public double getValorCaixaInicial() throws SQLException {
        String sql = "SELECT MAX(valor_inicial) FROM Caixa";
        double somaValorCaixa = 0;

        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                somaValorCaixa = rs.getDouble(1);
            }
        }

        return somaValorCaixa;
    }

    public boolean inserirProduto(String nome, double preco, String tamanho, String cor, String descricao, int estacao) {
        String sql = "INSERT INTO Produto (nome, preco, tamanho, cor, descricao, estacao) VALUES (?, ?, ?, ?, ?, ?)";
        return executeUpdate(sql, nome, preco, tamanho, cor, descricao, estacao);
    }

    public boolean atualizarProduto(int idProduto, String nome, double preco, String tamanho, String cor, String descricao, short estacao) {
        String sql = "UPDATE Produto SET nome = ?, preco = ?, tamanho = ?, cor = ?, descricao = ?, estacao = ? WHERE ID_Produto = ?";
        return executeUpdate(sql, nome, preco, tamanho, cor, descricao, estacao, idProduto);
    }

    public boolean excluirProduto(int idProduto) {
        String sql = "DELETE FROM Produto WHERE ID_Produto = ?";
        return executeUpdate(sql, idProduto);
    }

    public ResultSet listarProdutos() {
        String sql = "SELECT * FROM Produto";
        return executeQuery(sql);
    }

    public List<Produto> listarProdutosMatriz() {
        String sql = "SELECT * FROM Produto";
        ResultSet rs = executeQuery(sql);
        List<Produto> produtos = new ArrayList<>();
        try {
            while (rs != null && rs.next()) {
                Produto produto = new Produto(rs.getInt("id_produto"), rs.getString("nome"), rs.getDouble("preco"), rs.getString("tamanho"), rs.getString("cor"), rs.getString("descricao"), rs.getInt("estacao")); // Supondo que a tabela Produto tem 7 colunas              
                produtos.add(produto);
            }
        } catch (SQLException e) {
            Logger.getLogger(GerenciadorDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return produtos;
    }

    public int inserirCompra(String dataCompra, Double valorTotalCompra) {
        String sql = "INSERT INTO Compra (data, valor_total) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, java.sql.Date.valueOf(dataCompra)); // Converte a data para o formato SQL
            stmt.setDouble(2, valorTotalCompra);
            stmt.executeUpdate();

            // Captura o código gerado para a compra
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean atualizarCompra(int codigo, java.sql.Date data, double valorTotal) {
        String sql = "UPDATE Compra SET data = ?, valor_total = ? WHERE codigo = ?";
        return executeUpdate(sql, data, valorTotal, codigo);
    }

    public boolean excluirCompra(int codigo) {
        String sql = "DELETE FROM Compra WHERE codigo = ?";
        return executeUpdate(sql, codigo);
    }

    public ResultSet listarCompras() {
        String sql = "SELECT * FROM Compra";
        return executeQuery(sql);
    }

    public boolean inserirRegistroCompra(int codCompra, int idProduto, double valorUnitarioCompra, int quantidadeCompra, String observacaoCompra) {
        String sql = "INSERT INTO Registro_Compra (COD_Compra, ID_Produto, valor_unitario, observacao) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codCompra);
            stmt.setInt(2, idProduto);
            stmt.setDouble(3, valorUnitarioCompra);
            //stmt.setInt(4, quantidadeCompra);
            stmt.setString(4, observacaoCompra);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ResultSet listarComprasComProdutos() {
        String sql = "SELECT c.data, c.valor_total, rc.quantidade, rc.valor_unitario, p.nome AS produto, c.codigo "
                + "FROM Compra c "
                + "JOIN Registro_Compra rc ON c.codigo = rc.COD_Compra "
                + "JOIN Produto p ON rc.ID_Produto = p.ID_Produto";
        return executeQuery(sql);
    }

    public boolean atualizarRegistroCompra(int codCompra, int idProduto, double valorUnitario, String observacao) {
        String sql = "UPDATE Registro_Compra SET valor_unitario = ?, observacao = ? WHERE COD_Compra = ? AND ID_Produto = ?";
        return executeUpdate(sql, valorUnitario, observacao, codCompra, idProduto);
    }

    public boolean excluirRegistroCompra(int codCompra, int idProduto) {
        String sql = "DELETE FROM Registro_Compra WHERE COD_Compra = ? AND ID_Produto = ?";
        return executeUpdate(sql, codCompra, idProduto);
    }

    public ResultSet listarRegistroCompras() {
        String sql = "SELECT * FROM Registro_Compra";
        return executeQuery(sql);
    }

    // Métodos para a tabela Cliente
    public boolean inserirCliente(String nome, String contato, String endereco) {
        String sql = "INSERT INTO Cliente (nome, contato, endereco) VALUES (?, ?, ?)";
        return executeUpdate(sql, nome, contato, endereco);
    }

    public boolean atualizarCliente(int idCliente, String nome, String contato, String endereco) {
        String sql = "UPDATE Cliente SET nome = ?, contato = ?, endereco = ? WHERE ID_Cliente = ?";
        return executeUpdate(sql, nome, contato, endereco, idCliente);
    }

    public boolean excluirCliente(int idCliente) {
        String sql = "DELETE FROM Cliente WHERE ID_Cliente = ?";
        return executeUpdate(sql, idCliente);
    }

    public ResultSet listarClientes() {
        String sql = "SELECT * FROM Cliente";
        return executeQuery(sql);
    }

    public List<Cliente> listarClientesMatriz() {
        String sql = "SELECT * FROM Cliente";
        ResultSet rs = executeQuery(sql);
        List<Cliente> clientes = new ArrayList<>();
        try {
            while (rs != null && rs.next()) {
                Cliente cliente = new Cliente(rs.getInt("id_cliente"), rs.getString("nome"), rs.getString("contato"), rs.getString("endereco"));

                clientes.add(cliente);
            }
        } catch (SQLException e) {
            Logger.getLogger(GerenciadorDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return clientes;
    }

    public int inserirVenda(java.sql.Date data, double valorTotal, String formaPagamento, int idCliente) {
        String sql = "INSERT INTO Venda (data, valor_total, forma_pagamento, ID_Cliente) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, data);
            stmt.setBigDecimal(2, BigDecimal.valueOf(valorTotal));
            stmt.setString(3, formaPagamento);
            stmt.setInt(4, idCliente);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);  // Retorna o código da venda inserida
                    }
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(GerenciadorDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return -1;  // Retorna -1 se houve um erro
    }

    public boolean atualizarVenda(int codigo, java.sql.Date data, double valorTotal, String formaPagamento, int idCliente) {
        String sql = "UPDATE Venda SET data = ?, valor_total = ?, forma_pagamento = ?, ID_Cliente = ? WHERE codigo = ?";
        return executeUpdate(sql, data, valorTotal, formaPagamento, idCliente, codigo);
    }

    public boolean excluirVenda(int codigo) {
        String sql = "DELETE FROM Venda WHERE codigo = ?";
        return executeUpdate(sql, codigo);
    }

    public ResultSet listarVendas() {
        String sql = "SELECT * FROM Venda";
        return executeQuery(sql);
    }

    // Métodos para a tabela RegistroVenda
    public boolean inserirRegistroVenda(int codVenda, int idProduto, int quantidade, double valorUnitario, String observacao) {
        String sql = "INSERT INTO Registro_Venda (COD_Venda, ID_Produto, quantidade, valor_unitario, observacao) VALUES (?, ?, ?, ?, ?)";
        return executeUpdate(sql, codVenda, idProduto, quantidade, valorUnitario, observacao);
    }

    public boolean atualizarRegistroVenda(int codVenda, int idProduto, int quantidade, double valorUnitario, String observacao) {
        String sql = "UPDATE Registro_Venda SET quantidade = ?, valor_unitario = ?, observacao = ? WHERE COD_Venda = ? AND ID_Produto = ?";
        return executeUpdate(sql, quantidade, valorUnitario, observacao, codVenda, idProduto);
    }

    public boolean excluirRegistroVenda(int codVenda, int idProduto) {
        String sql = "DELETE FROM Registro_Venda WHERE COD_Venda = ? AND ID_Produto = ?";
        return executeUpdate(sql, codVenda, idProduto);
    }

    public ResultSet listarRegistroVendas() {
        String sql = "SELECT * FROM Registro_Venda";
        return executeQuery(sql);
    }

    public List<String[]> listarVendasDetalhadas() {
        String sql = "SELECT v.data, v.valor_total, v.forma_pagamento, rv.quantidade, rv.valor_unitario, p.nome AS produto, c.nome AS cliente "
                + "FROM Venda v "
                + "JOIN Registro_Venda rv ON v.codigo = rv.COD_Venda "
                + "JOIN Produto p ON rv.ID_Produto = p.ID_Produto "
                + "JOIN Cliente c ON v.ID_Cliente = c.ID_Cliente";

        List<String[]> vendasDetalhadas = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String[] venda = new String[7];
                venda[0] = rs.getString("data");
                venda[1] = rs.getString("valor_total");
                venda[2] = rs.getString("forma_pagamento");
                venda[3] = rs.getString("quantidade");
                venda[4] = rs.getString("valor_unitario");
                venda[5] = rs.getString("produto");
                venda[6] = rs.getString("cliente");
                vendasDetalhadas.add(venda);
            }
        } catch (SQLException e) {
            Logger.getLogger(GerenciadorDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return vendasDetalhadas;
    }

    // Métodos auxiliares para executar as operações no banco de dados
    private boolean executeUpdate(String sql, Object... params) {
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            Logger.getLogger(GerenciadorDAO.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    private ResultSet executeQuery(String sql, Object... params) {
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            return stmt.executeQuery();
        } catch (SQLException e) {
            Logger.getLogger(GerenciadorDAO.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public boolean updateEstoque(int idProduto, int quantidade) {
        try {
            String sqlSelect = "SELECT quantidade FROM Estoque WHERE ID_Produto = ?";
            PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect);
            stmtSelect.setInt(1, idProduto);
            ResultSet rs = stmtSelect.executeQuery();

            if (rs.next()) {
                int quantidadeAtual = rs.getInt("quantidade");
                String sqlUpdate = "UPDATE Estoque SET quantidade = ? WHERE ID_Produto = ?";
                PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate);
                stmtUpdate.setInt(1, quantidadeAtual + quantidade);
                stmtUpdate.setInt(2, idProduto);
                stmtUpdate.executeUpdate();
            } else {
                String sqlInsert = "INSERT INTO Estoque (ID_Produto, quantidade) VALUES (?, ?)";
                PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert);
                stmtInsert.setInt(1, idProduto);
                stmtInsert.setInt(2, quantidade);
                stmtInsert.executeUpdate();
            }

            return true;
        } catch (SQLException e) {
            Logger.getLogger(GerenciadorDAO.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    public List<String[]> getListaEstoque() {
        String sql = "SELECT p.id_produto, p.nome, COALESCE(e.quantidade, 0) AS quantidade "
                + "FROM Produto p "
                + "LEFT JOIN Estoque e ON p.id_produto = e.id_produto";

        List<String[]> listaEstoque = new ArrayList<>();
        ResultSet rs = executeQuery(sql);

        try {
            while (rs != null && rs.next()) {
                String[] produtoEstoque = new String[3];
                produtoEstoque[0] = String.valueOf(rs.getInt("id_produto"));
                produtoEstoque[1] = rs.getString("nome");
                produtoEstoque[2] = String.valueOf(rs.getInt("quantidade"));
                listaEstoque.add(produtoEstoque);
            }
        } catch (SQLException e) {
            Logger.getLogger(GerenciadorDAO.class.getName()).log(Level.SEVERE, null, e);
        }

        return listaEstoque;
    }

    public int getQuantidadeEstoque(int idProduto) {
        try {
            String sql = "SELECT quantidade FROM Estoque WHERE ID_Produto = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idProduto);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("quantidade");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            Logger.getLogger(GerenciadorDAO.class.getName()).log(Level.SEVERE, null, e);
            return 0;
        }
    }
    
    public void abrirCaixa() throws SQLException {
        String sql = "UPDATE caixa SET status = TRUE WHERE codigo = (SELECT MAX(codigo) FROM caixa)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {            
            stmt.executeUpdate();
        }
    }
    
    public void fecharCaixa() throws SQLException {
        String sql = "UPDATE caixa SET status = FALSE WHERE codigo = (SELECT MAX(codigo) FROM caixa)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {            
            stmt.executeUpdate();
        }
    }
    
    public boolean verificarStatusCaixa() throws SQLException {
        String sql = "SELECT status FROM caixa WHERE codigo = (SELECT MAX(codigo) FROM caixa)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("status");
                } else {
                    throw new SQLException("Caixa não encontrado.");
                }
            }
        }
    }
//    public static void main(String[] args) {
//        GerenciadorDAO dao = new GerenciadorDAO();
//
//        // Exemplo de uso para listar usuários
//        ResultSet rs = dao.listarUsuarios();
//        try {
//            while (rs != null && rs.next()) {
//                System.out.println("Código: " + rs.getInt("codigo") + " Nome: " + rs.getString("nome"));
//            }
//        } catch (SQLException e) {
//            Logger.getLogger(GerenciadorDAO.class.getName()).log(Level.SEVERE, null, e);
//        } finally {
//            Conexao.fechaConexao();
//        }
//    }

}
