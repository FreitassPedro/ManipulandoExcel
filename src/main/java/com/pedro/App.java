package com.pedro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class App {
    private static final String fileName = "C:\\Users\\CALL1\\Desktop\\projetoExcel\\relatorioOutubro.xlsx";
    // "C:\\Users\\CALL1\\Desktop\\projetoExcel\\contas.xlsx";

    public static void main(String[] args) throws IOException {
        List<Credor> listaCredor = new ArrayList<Credor>();
        try {
            FileInputStream arquivo = new FileInputStream(new File(App.fileName));

            XSSFWorkbook workbook = new XSSFWorkbook(arquivo);
            XSSFSheet sheetFornecedores = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheetFornecedores.iterator();

            // ITERADOR DE LINHAS

                while (rowIterator.hasNext()) {
                    boolean pularLinha = false;
                    Row row = rowIterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();

                    Credor credor = new Credor();
                    listaCredor.add(credor);

                    // LETRA A C D E F N
                    // A-0 C-2 D-3 E-4 N-13
                    // ITERADOR DE COLUNAS
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        CellStyle cellStyle = cell.getCellStyle();
                        if (pularLinha) {
                            pularLinha = false;
                            break;
                        }
                        

                        //Inicilizar Strings
                        String nomeEncontrado = "";
                        String documento = "";
                        switch (cell.getColumnIndex()) {
                            case 0:
                                nomeEncontrado = cell.getStringCellValue();
                                System.out.println(nomeEncontrado);
                                System.out.println("Cor Celula: " + obterCorCelula(cellStyle));
                                String corCelula = obterCorCelula(cellStyle);
                                if (nomeEncontrado.contains("Total por empresa")) {
                                    System.out.println("passou teste");
                                    rowIterator.next();
                                    rowIterator.next();
                                    rowIterator.next();
                                    rowIterator.next();
                                    System.out.println("encerrando...");
                                    pularLinha = true;
                                    break;
                                }
                                if (corCelula.equals("#c0c0c0") || corCelula.equals("#C0C0C0")) {
                                    pularLinha = true;
                                    break;
                                }
                                if (nomeEncontrado.equals("")) {
                                    System.out.println("Nome = aspas");
                                }

                                // PULA CASO A CELULA SEJA VAZIA
                                if (!nomeEncontrado.equals("")) {
                                    nomeEncontrado = nomeEncontrado.trim();
                                    String[] fields = nomeEncontrado.split(" - ");
                                    if (fields[1].endsWith(" ") || fields[1].endsWith(".")) {
                                        fields[1] = fields[1].substring(0, fields[1].length() - 1);
                                    }
                                    removerAcentos(fields[1]);
                                    credor.setNome(removerAcentos(fields[1]).toUpperCase());
                                } else {
                                    pularLinha = true;
                                }

                                break;
                            case 2:
                                System.out.println("Documento: " + cell.getStringCellValue());
                                credor.setDocumento(cell.getStringCellValue());
                                break;
                            case 3:
                                String[] fields = cell.getStringCellValue().split("/");
                                String parcela = fields[1];
                                System.out.println("Título: " + fields[0]);
                                System.out.println("Parcela: " + parcela);
                                credor.setTitulo(fields[0]);
                                credor.setParcela(fields[1]);
                                break;
                            case 4:
                                System.out.println("Parcelas Totais: " + cell.getNumericCellValue());
                                credor.setQuantidade(cell.getNumericCellValue());
                                break;
                            case 13:
                                System.out.println("Valor: " + cell.getNumericCellValue());
                                credor.setValor(cell.getNumericCellValue());
                                System.out.println();
                                break;
                        }
                    }
                
                arquivo.close();
                workbook.close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Arquivo Excel não encontrado!");
        }

        Map<String, Double> valoresPorNome = new HashMap<>();
        double valorDia = 0;
        double valorTeste = 0;
        double valorPassarIf = 0;
        // Itere sobre a lista de credores e agrupe os valores por nome
        for (Credor credor : listaCredor) {
            System.out.println();
            String nomeBeneficiario = credor.getNome();
            double valor = credor.getValor();
            String titulo = credor.getTitulo();
            String parcela = credor.getParcela();
            String valorTotalString = String.format(Locale.forLanguageTag("pt-BR"),
                    "%,.2f", valor);
            if (nomeBeneficiario == null) {
                continue;
            }
            String resposta = LocalizarPdf.localizarArquivosPdf(nomeBeneficiario, valorTotalString, titulo, parcela);
            System.out.println("RESPOSTA----------" + resposta);
            if (!resposta.equals("nomeValor")) {
                valorPassarIf += valor;
                // Verifique se o nome já está no mapa
                if (valoresPorNome.containsKey(nomeBeneficiario)) {
                    // Se o nome já existe no mapa, some o valor existente com o novo valor
                    double valorExistente = valoresPorNome.get(nomeBeneficiario);

                    valoresPorNome.put(nomeBeneficiario, valorExistente + valor);
                } else {
                    // Se o nome não existe no mapa, crie uma nova entrada com o valor
                    valoresPorNome.put(nomeBeneficiario, valor);
                }
            }
        }
        System.out.println("-> TODOS VALROES LIDOS!");
        System.out.println();

        // Imprime mapa com os valores agrupados por nome
        for (Map.Entry<String, Double> entry : valoresPorNome.entrySet()) {
            String nomeBeneficiario = entry.getKey();
            double valorTotal = entry.getValue();
            String valorTotalString = String.format(Locale.forLanguageTag("pt-BR"),
                    "%,.2f", valorTotal);
            System.out.println();
            System.out.println("Nome:" + nomeBeneficiario + ", Valor Total: "
                    + valorTotalString);
            LocalizarPdf.localizarArquivosPdf(nomeBeneficiario, valorTotalString, "null", "null");
            valorDia += valorTotal;
        }
        System.out.println();
        System.out.println("Valor do dia: " + String.format(Locale.forLanguageTag("pt-BR"),
                "%,.2f", valorDia));
        System.out.println("valor teste: " + valorTeste);
        System.out.println("valor passar if " + valorPassarIf);
        System.out.println("valor total: " + (valorPassarIf + valorTeste));
    }

    // CÓDIGO DO STACK OVERFLOW PARA REMOVER ACENTOS DAS PALAVRAS
    public static String removerAcentos(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    public static String obterCorCelula(CellStyle cellStyle) {
        short corIndex = cellStyle.getFillForegroundColor();
        if (corIndex != IndexedColors.AUTOMATIC.getIndex()) {
            // Obtém a cor correspondente ao índice da paleta de cores
            Color color = cellStyle.getFillForegroundColorColor();

            // Verifica se a cor é uma cor padrão
            if (color instanceof XSSFColor) {
                byte[] rgb = ((XSSFColor) color).getRGB();
                return String.format("#%02x%02x%02x", rgb[0], rgb[1], rgb[2]);
            } else {
                return "Cor personalizada (não suportada)";
            }
        } else {
            return "Nenhuma cor de preenchimento definida";
        }
    }
}
