package com.pedro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class App {
    private static final String fileName = "C:\\Users\\CALL1\\Desktop\\cpgContas2.xls";

    public static void main(String[] args) throws IOException {
        List<Credor> listaCredor = new ArrayList<Credor>();
        try {
            FileInputStream arquivo = new FileInputStream(new File(App.fileName));

            HSSFWorkbook workbook = new HSSFWorkbook(arquivo);
            HSSFSheet sheetFornecedores = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheetFornecedores.iterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();

                Credor credor = new Credor();
                listaCredor.add(credor);

                String nomeEncontrado = "";

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    switch (cell.getColumnIndex()) {
                        case 0:
                            nomeEncontrado = cell.getStringCellValue();
                            String[] fields = nomeEncontrado.split("- ");
                            if (fields[1].endsWith(" ")) {
                                fields[1] = fields[1].substring(0, fields[1].length() - 1);
                            }
                            credor.setNome(fields[1].toUpperCase());
                            break;
                        case 1:
                            credor.setDocumento(cell.getStringCellValue());
                            break;
                        case 2:
                            credor.setTitulo(cell.getStringCellValue());
                            break;
                        case 3:
                            credor.setQuantidade(cell.getNumericCellValue());
                            break;
                        case 4:
                            credor.setValor(cell.getNumericCellValue());
                            break;
                    }
                }
            }
            arquivo.close();
            workbook.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Arquivo Excel não encontrado!");
        }

        Map<String, Double> valoresPorNome = new HashMap<>();

        // Itere sobre a lista de credores e agrupe os valores por nome
        for (Credor aluno : listaCredor) {
            String nomeBeneficiario = aluno.getNome();
            double valor = aluno.getValor();

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
        // Mapa com os valores agrupados por nome
        for (Map.Entry<String, Double> entry : valoresPorNome.entrySet()) {
            String nomeBeneficiario = entry.getKey();
            double valorTotal = entry.getValue();
            String valorTotalString = String.format(Locale.forLanguageTag("pt-BR"), "%,.2f", valorTotal);
            System.out.println();
            System.out.println("Nome:" + nomeBeneficiario + ", Valor Total: "
                    + String.format(Locale.forLanguageTag("pt-BR"), "%,.2f", valorTotal));
            LocalizarPdf.localizarArquivosPdf(nomeBeneficiario, valorTotalString);
        }
    }
}