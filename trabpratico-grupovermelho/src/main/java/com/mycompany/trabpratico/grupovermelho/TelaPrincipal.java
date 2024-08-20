/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.trabpratico.grupovermelho;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author mmanasses
 */
public class TelaPrincipal extends javax.swing.JFrame {

    GerenciadorDAO dao;
    String[] estacoes = new String[]{"Primavera", "Verão", "Outono", "Inverno"};
    List<Cliente> clientes = new ArrayList<>();
    List<Produto> produtos = new ArrayList<>();

    /**
     * Creates new form telaprincipal
     */
    public TelaPrincipal() {
        initComponents();
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(estacoes));
        atualizaTabelaProdutos();
        atualizaTabelaVendas();
        atualizaTabelaCompras();
        atualizaCaixa();
        atualizaTabelaEstoque();
        preencherComboBoxClientes(jComboBox2);
        preencherComboBoxProdutos(jComboBox3);
        preencherComboBoxProdutos(jComboBox4);

        LocalDate dataAtual = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataFormatada = dataAtual.format(formatter);

        dataAtualCaixa.setText(dataFormatada);
        jFormattedTextField1.setText(dataFormatada);
        jFormattedTextField2.setText(dataFormatada);

        verificaCaixa();
    }

    public void verificaCaixa() {
        try {
            GerenciadorDAO dao = new GerenciadorDAO();
            boolean sucesso = dao.verificarStatusCaixa();
            jButton2.setEnabled(!sucesso);
            jButton1.setEnabled(sucesso);
            btVenda.setEnabled(sucesso);
            btCompra.setEnabled(sucesso);
            jButton3.setEnabled(sucesso);
            if (sucesso) {
                jLabel23.setText("Caixa Aberto!");
            } else {
                jLabel23.setText("Caixa Fechado!");
            }
        } catch (SQLException ex) {
            Logger.getLogger(TelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void atualizaTabelaEstoque() {
        dao = new GerenciadorDAO();

        List<String[]> listaProdutos = dao.getListaEstoque();

        DefaultTableModel model = (DefaultTableModel) tabelaProduto1.getModel();

        model.setRowCount(0);

        for (String[] produto : listaProdutos) {
            Object[] rowData = {produto[1], produto[2]};
            model.addRow(rowData);
        }
    }

    public void atualizaCaixa() {
        try {
            dao = new GerenciadorDAO();
            txtValorCaixa.setText((dao.getValorCaixa() + dao.getValorCaixaInicial()) + "");
            jTextField1.setText(dao.getValorCaixaInicial() + "");
            Conexao.fechaConexao();
        } catch (SQLException ex) {
            Logger.getLogger(TelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void preencherComboBoxClientes(JComboBox<String> comboBox) {
        dao = new GerenciadorDAO();
        clientes = dao.listarClientesMatriz();
        Conexao.fechaConexao();

        String[] clientesV = new String[clientes.size()];
        for (int i = 0; i < clientesV.length; i++) {
            clientesV[i] = clientes.get(i).getNome();
        }

        comboBox.removeAllItems();

        for (String cliente : clientesV) {
            comboBox.addItem(cliente);
        }
    }

    public void preencherComboBoxProdutos(JComboBox<String> comboBox) {
        dao = new GerenciadorDAO();
        produtos = dao.listarProdutosMatriz();
        Conexao.fechaConexao();

        String[] produtosV = new String[produtos.size()];
        for (int i = 0; i < produtosV.length; i++) {
            produtosV[i] = produtos.get(i).getNome();
        }

        comboBox.removeAllItems();

        for (String cliente : produtosV) {
            comboBox.addItem(cliente);
        }
    }

    private void atualizaTabelaVendas() {
        dao = new GerenciadorDAO();
        List<String[]> vendas = dao.listarVendasDetalhadas();
        String[] colunas = {"Data", "Valor Total", "Forma Pagamento", "Quantidade", "Valor Unitário", "Produto", "Cliente"};

        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        for (String[] venda : vendas) {
            model.addRow(venda);
        }

        tabelaVenda.setModel(model);
    }

    private void atualizaTabelaCompras() {
        dao = new GerenciadorDAO();
        List<String[]> compras = dao.listarComprasDetalhadas();
        String[] colunas = {"Data", "Valor Total", "Quantidade", "Valor Unitário", "Observação"};

        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        for (String[] compra : compras) {
            model.addRow(compra);
        }

        tabelaCompra.setModel(model);
    }

    private void atualizaTabelaProdutos() {
        dao = new GerenciadorDAO();
        tabelaProduto.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Nome", "Preço", "Tamanho", "Cor", "Estação", "Descrição"
                }
        ));
        DefaultTableModel TabelaProd = (DefaultTableModel) tabelaProduto.getModel();

        ResultSet rs = dao.listarProdutos();
        try {
            while (rs != null && rs.next()) {
                String nome;
                String preco;
                String tamanho;
                String cor;
                String estacao;
                String descricao;

                nome = rs.getString("nome");
                preco = rs.getString("preco");
                tamanho = rs.getString("tamanho");
                cor = rs.getString("cor");
                descricao = rs.getString("descricao");
                estacao = estacoes[rs.getInt("estacao")];

                Object[] novoUsuario = new Object[]{
                    nome,
                    preco,
                    tamanho,
                    cor,
                    estacao,
                    descricao
                };

                TabelaProd.addRow(novoUsuario);
            }
        } catch (SQLException e) {
            Logger.getLogger(GerenciadorDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            Conexao.fechaConexao();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtValorTotalVenda = new javax.swing.JTextField();
        txtQuantidadeVenda = new javax.swing.JTextField();
        txtValorUnitarioVenda = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelaVenda = new javax.swing.JTable();
        btVenda = new javax.swing.JButton();
        txtFormaPagamentoVenda = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        btAdicionarClienteVenda = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBox3 = new javax.swing.JComboBox<>();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaCompra = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtValorTotalCompra = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtQuantidadeCompra = new javax.swing.JTextField();
        txtValorUnitario = new javax.swing.JTextField();
        txtObservacaoCompra = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btCompra = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jFormattedTextField2 = new javax.swing.JFormattedTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtNomeProduto = new javax.swing.JTextField();
        txtPrecoProduto = new javax.swing.JTextField();
        txtxCorProduto = new javax.swing.JTextField();
        txtDescricaoProduto = new javax.swing.JTextField();
        btProduto = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabelaProduto = new javax.swing.JTable();
        txtTamanhoProduto = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tabelaProduto1 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        dataAtualCaixa = new javax.swing.JTextField();
        txtValorCaixa = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Tela Principal Sistema");

        jPanel3.setToolTipText("Principal");

        jLabel8.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        jLabel8.setText("Data");

        jLabel9.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        jLabel9.setText("Valor Total");

        jLabel10.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        jLabel10.setText("Forma de pagamento");

        jLabel11.setText("Produto");

        jLabel12.setText("Valor Unitário");

        txtValorTotalVenda.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        txtQuantidadeVenda.setText("1");
        txtQuantidadeVenda.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtQuantidadeVendaFocusLost(evt);
            }
        });
        txtQuantidadeVenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtQuantidadeVendaActionPerformed(evt);
            }
        });

        txtValorUnitarioVenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtValorUnitarioVendaActionPerformed(evt);
            }
        });

        jLabel13.setText("Quantidade");

        tabelaVenda.setFont(new java.awt.Font("Liberation Sans", 0, 12)); // NOI18N
        tabelaVenda.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Data", "Valor Total", "Forma Pagamento", "Quantidade", "Valor Unitário", "Produto", "Cliente"
            }
        ));
        jScrollPane2.setViewportView(tabelaVenda);

        btVenda.setFont(new java.awt.Font("Liberation Sans", 1, 14)); // NOI18N
        btVenda.setText("Registrar Venda");
        btVenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btVendaActionPerformed(evt);
            }
        });

        jLabel20.setText("Cliente");

        btAdicionarClienteVenda.setFont(new java.awt.Font("Liberation Sans", 1, 14)); // NOI18N
        btAdicionarClienteVenda.setText("Adicionar Cliente");
        btAdicionarClienteVenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAdicionarClienteVendaActionPerformed(evt);
            }
        });

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jComboBox3FocusLost(evt);
            }
        });
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        try {
            jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel9)
                            .addComponent(jLabel8)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12)
                            .addComponent(jLabel13)
                            .addComponent(jLabel20))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtValorUnitarioVenda)
                            .addComponent(txtQuantidadeVenda)
                            .addComponent(txtValorTotalVenda)
                            .addComponent(txtFormaPagamentoVenda)
                            .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jFormattedTextField1)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btAdicionarClienteVenda, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(txtFormaPagamentoVenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(txtValorUnitarioVenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(txtQuantidadeVenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(4, 4, 4)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(txtValorTotalVenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(btAdicionarClienteVenda)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btVenda))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Venda", jPanel3);

        tabelaCompra.setFont(new java.awt.Font("Liberation Sans", 0, 12)); // NOI18N
        tabelaCompra.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Data", "Valor Total", "Quantidade", "Valor Unitário", "Observação"
            }
        ));
        jScrollPane1.setViewportView(tabelaCompra);

        jLabel1.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        jLabel1.setText("Data");

        jLabel2.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        jLabel2.setText("Valor Total");

        txtValorTotalCompra.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jLabel3.setText("Quantidade");

        txtQuantidadeCompra.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtQuantidadeCompraFocusLost(evt);
            }
        });
        txtQuantidadeCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtQuantidadeCompraActionPerformed(evt);
            }
        });

        jLabel4.setText("Valor Unitário");

        jLabel5.setText("Observação");

        btCompra.setFont(new java.awt.Font("Liberation Sans", 1, 14)); // NOI18N
        btCompra.setText("Registrar Compra");
        btCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCompraActionPerformed(evt);
            }
        });

        jLabel21.setText("Produto");

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jComboBox4FocusLost(evt);
            }
        });
        jComboBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox4ActionPerformed(evt);
            }
        });

        try {
            jFormattedTextField2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(26, 26, 26)
                        .addComponent(txtQuantidadeCompra))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel5)
                                    .addGap(26, 26, 26)
                                    .addComponent(txtObservacaoCompra))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGap(88, 88, 88)
                                    .addComponent(jComboBox4, 0, 170, Short.MAX_VALUE))
                                .addComponent(btCompra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel21))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(16, 16, 16)
                        .addComponent(txtValorUnitario))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtValorTotalCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jFormattedTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE)
                .addGap(48, 48, 48))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtValorUnitario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtQuantidadeCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtObservacaoCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtValorTotalCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(btCompra)))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Compra", jPanel2);

        jLabel14.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        jLabel14.setText("Nome");

        jLabel15.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        jLabel15.setText("Preço");

        jLabel16.setText("Tamanho");

        jLabel17.setText("Cor");

        jLabel18.setText("Estação");

        jLabel19.setText("Descrição");

        txtNomeProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeProdutoActionPerformed(evt);
            }
        });

        txtxCorProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtxCorProdutoActionPerformed(evt);
            }
        });

        txtDescricaoProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDescricaoProdutoActionPerformed(evt);
            }
        });

        btProduto.setFont(new java.awt.Font("Liberation Sans", 1, 14)); // NOI18N
        btProduto.setText("Registrar Produto");
        btProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btProdutoActionPerformed(evt);
            }
        });

        tabelaProduto.setFont(new java.awt.Font("Liberation Sans", 1, 12)); // NOI18N
        tabelaProduto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome", "Preço", "Tamanho", "Cor", "Estação", "Descrição"
            }
        ));
        jScrollPane3.setViewportView(tabelaProduto);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addGap(25, 25, 25)
                        .addComponent(txtDescricaoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17)
                            .addComponent(jLabel18))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtxCorProduto)
                            .addComponent(txtTamanhoProduto)
                            .addComponent(jComboBox1, 0, 142, Short.MAX_VALUE)))
                    .addComponent(btProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15))
                        .addGap(34, 34, 34)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtPrecoProduto, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
                            .addComponent(txtNomeProduto))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtNomeProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtPrecoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(txtTamanhoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(txtxCorProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(txtDescricaoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addComponent(btProduto)
                .addContainerGap(234, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 38, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Produto", jPanel1);

        tabelaProduto1.setFont(new java.awt.Font("Liberation Sans", 1, 12)); // NOI18N
        tabelaProduto1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Produto", "Quantidade"
            }
        ));
        jScrollPane4.setViewportView(tabelaProduto1);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 457, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Estoque", jPanel5);

        jLabel6.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        jLabel6.setText("Data");

        jLabel7.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        jLabel7.setText("Valor Caixa");

        dataAtualCaixa.setEditable(false);

        txtValorCaixa.setEditable(false);

        jLabel22.setText("Valor Inicial");

        jButton1.setText("Atualizar Valor Inicial");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Abrir Caixa");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Fechar Caixa");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel23.setText(" ");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel7)
                                .addComponent(jLabel6)
                                .addComponent(jLabel22))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(dataAtualCaixa, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                                .addComponent(txtValorCaixa)
                                .addComponent(jTextField1))))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 505, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(253, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(dataAtualCaixa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtValorCaixa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jButton3))
                    .addComponent(jButton2))
                .addContainerGap(210, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Caixa", jPanel4);

        jMenuBar1.setBackground(new java.awt.Color(0, 0, 0));
        jMenuBar1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));
        jMenuBar1.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N

        jMenu1.setText("LOJA");
        jMenu1.setFont(new java.awt.Font("Liberation Sans", 1, 14)); // NOI18N
        jMenuBar1.add(jMenu1);

        jMenu2.setText("CADASTRO");
        jMenu2.setFont(new java.awt.Font("Liberation Sans", 1, 14)); // NOI18N

        jMenu5.setText("Adicionar Usuario");
        jMenu5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu5ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenu5);

        jMenu6.setText("Adicionar Cliente");
        jMenu6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu6ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenu6);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 504, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtNomeProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeProdutoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeProdutoActionPerformed

    private void txtxCorProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtxCorProdutoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtxCorProdutoActionPerformed

    private void txtDescricaoProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDescricaoProdutoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDescricaoProdutoActionPerformed

    private void jMenu5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu5ActionPerformed
        // TODO add your handling code here:
        //manasses Adicionar Usuario

        AdicionarUsuario usuario = new AdicionarUsuario();
        usuario.setVisible(true);

        AdicionarCliente bttela = new AdicionarCliente(null, true);
        bttela.setVisible(true);


    }//GEN-LAST:event_jMenu5ActionPerformed

    private void jMenu6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu6ActionPerformed
        // TODO add your handling code here:
        //manasses Adicionar Cliente

        Login bttela = new Login();
        bttela.setVisible(true);
    }//GEN-LAST:event_jMenu6ActionPerformed

    private void txtQuantidadeVendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtQuantidadeVendaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQuantidadeVendaActionPerformed

    private void txtValorUnitarioVendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtValorUnitarioVendaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtValorUnitarioVendaActionPerformed

    private void btVendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btVendaActionPerformed

        String dataVenda = jFormattedTextField1.getText().split("/")[2] + "-" + jFormattedTextField1.getText().split("/")[1] + "-" + jFormattedTextField1.getText().split("/")[0];
        String valorTotalVenda = txtValorTotalVenda.getText();
        String formaPagamentoVenda = txtFormaPagamentoVenda.getText();
        int clienteID = clientes.get(jComboBox2.getSelectedIndex()).getIdCliente();

        java.sql.Date sqlDateVenda = java.sql.Date.valueOf(dataVenda);

        dao = new GerenciadorDAO();

// Recupera a quantidade de venda e o ID do produto
        int idProduto = produtos.get(jComboBox3.getSelectedIndex()).getIdProduto();
        int quantidadeVenda = Integer.parseInt(txtQuantidadeVenda.getText());
        double valorUnitarioVenda = Double.parseDouble(txtValorUnitarioVenda.getText());

// Verifica o estoque atual
        int quantidadeAtualEstoque = dao.getQuantidadeEstoque(idProduto);

        if (quantidadeAtualEstoque >= quantidadeVenda) {
            // Se houver estoque suficiente, insere a venda
            int codVenda = dao.inserirVenda(sqlDateVenda, Double.parseDouble(valorTotalVenda), formaPagamentoVenda, clienteID);

            if (codVenda != -1) {
                String observacaoVenda = "";

                boolean sucesso = dao.inserirRegistroVenda(codVenda, idProduto, quantidadeVenda, valorUnitarioVenda, observacaoVenda);
                dao.inserirCaixa(Double.parseDouble(valorTotalVenda));

                if (sucesso) {
                    // Atualiza o estoque após a venda
                    int novaQuantidadeEstoque = -1 * quantidadeVenda;
                    dao.updateEstoque(idProduto, novaQuantidadeEstoque);

                    // Atualiza a tabela de vendas na interface gráfica
                    atualizaTabelaVendas();
                    atualizaTabelaEstoque();
                } else {
                    System.out.println("Erro ao inserir registro de venda.");
                }
            } else {
                System.out.println("Erro ao inserir venda.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Estoque insuficiente para realizar a venda.", "Falha na Venda", JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_btVendaActionPerformed

    private void txtQuantidadeCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtQuantidadeCompraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQuantidadeCompraActionPerformed

    private void btCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCompraActionPerformed
        String dataCompra = jFormattedTextField2.getText().split("/")[2] + "-" + jFormattedTextField2.getText().split("/")[1] + "-" + jFormattedTextField2.getText().split("/")[0];
        Double valorTotalCompra = Double.parseDouble(txtValorTotalCompra.getText());
        dao = new GerenciadorDAO();
// Insere na tabela Compra
        int codCompra = dao.inserirCompra(dataCompra, valorTotalCompra);

        if (codCompra > -1) {
            // Insere os registros na tabela Registro_Compra
            int idProduto = produtos.get(jComboBox4.getSelectedIndex()).getIdProduto();
            double valorUnitarioCompra = Double.parseDouble(txtValorUnitario.getText());
            int quantidadeCompra = Integer.parseInt(txtQuantidadeCompra.getText());
            String observacaoCompra = txtObservacaoCompra.getText();

            dao.inserirRegistroCompra(codCompra, idProduto, valorUnitarioCompra, quantidadeCompra, observacaoCompra);
            dao.inserirCaixa(Double.parseDouble(txtValorTotalCompra.getText()) * -1);
            dao.updateEstoque(idProduto, quantidadeCompra);
            atualizaTabelaEstoque();
        }
        atualizaTabelaCompras();
        atualizaCaixa();
        txtValorTotalCompra.setText("");
        txtQuantidadeCompra.setText("1");
        txtValorUnitario.setText("");
        txtObservacaoCompra.setText("");

    }//GEN-LAST:event_btCompraActionPerformed

    private void btProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btProdutoActionPerformed
        String nome = txtNomeProduto.getText();
        String preco = txtPrecoProduto.getText();
        String tamanho = txtTamanhoProduto.getText();
        String cor = txtxCorProduto.getText();
        int estacao = jComboBox1.getSelectedIndex();
        String descricao = txtDescricaoProduto.getText();

        dao = new GerenciadorDAO();
        dao.inserirProduto(nome, Double.parseDouble(preco), tamanho, cor, descricao, estacao);

        txtNomeProduto.setText("");
        txtPrecoProduto.setText("");
        txtTamanhoProduto.setText("");
        txtxCorProduto.setText("");
        jComboBox1.setSelectedIndex(0);
        txtDescricaoProduto.setText("");

        atualizaTabelaProdutos();
        preencherComboBoxProdutos(jComboBox3);
        preencherComboBoxProdutos(jComboBox4);
    }//GEN-LAST:event_btProdutoActionPerformed

    private void btAdicionarClienteVendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAdicionarClienteVendaActionPerformed
        AdicionarCliente addclie = new AdicionarCliente(null, true);
        addclie.setVisible(true);
        preencherComboBoxClientes(jComboBox2);
    }//GEN-LAST:event_btAdicionarClienteVendaActionPerformed

    private void jComboBox3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jComboBox3FocusLost
        Produto produto = produtos.get(jComboBox3.getSelectedIndex());
        txtValorUnitarioVenda.setText(produto.getPreco() + "");
        txtValorTotalVenda.setText((produto.getPreco() * Integer.parseInt(txtQuantidadeVenda.getText())) + "");
    }//GEN-LAST:event_jComboBox3FocusLost

    private void txtQuantidadeVendaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQuantidadeVendaFocusLost
        Produto produto = produtos.get(jComboBox3.getSelectedIndex());
        txtValorUnitarioVenda.setText(produto.getPreco() + "");
        txtValorTotalVenda.setText((produto.getPreco() * Integer.parseInt(txtQuantidadeVenda.getText())) + "");
    }//GEN-LAST:event_txtQuantidadeVendaFocusLost

    private void jComboBox4FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jComboBox4FocusLost
        if (jComboBox4.getSelectedIndex() >= 0) {
            dao = new GerenciadorDAO();
            Produto produto = produtos.get(jComboBox4.getSelectedIndex());
            txtValorUnitario.setText(produto.getPreco() + "");
            Conexao.fechaConexao();
        }
    }//GEN-LAST:event_jComboBox4FocusLost

    private void txtQuantidadeCompraFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtQuantidadeCompraFocusLost
        txtValorTotalCompra.setText((Double.parseDouble(txtValorUnitario.getText()) * Integer.parseInt(txtQuantidadeCompra.getText())) + "");
    }//GEN-LAST:event_txtQuantidadeCompraFocusLost

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dao = new GerenciadorDAO();
        dao.atualizarCaixaInicial(Double.parseDouble(jTextField1.getText()));
        Conexao.fechaConexao();
        atualizaCaixa();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox4ActionPerformed
        if (jComboBox4.getSelectedIndex() >= 0) {
            dao = new GerenciadorDAO();
            Produto produto = produtos.get(jComboBox4.getSelectedIndex());
            txtValorUnitario.setText(produto.getPreco() + "");
            Conexao.fechaConexao();
        }
    }//GEN-LAST:event_jComboBox4ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        if (jComboBox3.getSelectedIndex() >= 0) {
            Produto produto = produtos.get(jComboBox3.getSelectedIndex());
            txtValorUnitarioVenda.setText(produto.getPreco() + "");
            txtValorTotalVenda.setText((produto.getPreco() * Integer.parseInt(txtQuantidadeVenda.getText())) + "");
        }
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            dao = new GerenciadorDAO();
            dao.abrirCaixa();
            verificaCaixa();            
        } catch (SQLException ex) {
            Logger.getLogger(TelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            dao = new GerenciadorDAO();
            dao.fecharCaixa();
            verificaCaixa();         
        } catch (SQLException ex) {
            Logger.getLogger(TelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAdicionarClienteVenda;
    private javax.swing.JButton btCompra;
    private javax.swing.JButton btProduto;
    private javax.swing.JButton btVenda;
    private javax.swing.JTextField dataAtualCaixa;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JFormattedTextField jFormattedTextField2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTable tabelaCompra;
    private javax.swing.JTable tabelaProduto;
    private javax.swing.JTable tabelaProduto1;
    private javax.swing.JTable tabelaVenda;
    private javax.swing.JTextField txtDescricaoProduto;
    private javax.swing.JTextField txtFormaPagamentoVenda;
    private javax.swing.JTextField txtNomeProduto;
    private javax.swing.JTextField txtObservacaoCompra;
    private javax.swing.JTextField txtPrecoProduto;
    private javax.swing.JTextField txtQuantidadeCompra;
    private javax.swing.JTextField txtQuantidadeVenda;
    private javax.swing.JTextField txtTamanhoProduto;
    private javax.swing.JTextField txtValorCaixa;
    private javax.swing.JTextField txtValorTotalCompra;
    private javax.swing.JTextField txtValorTotalVenda;
    private javax.swing.JTextField txtValorUnitario;
    private javax.swing.JTextField txtValorUnitarioVenda;
    private javax.swing.JTextField txtxCorProduto;
    // End of variables declaration//GEN-END:variables

    void setVisiblre(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
