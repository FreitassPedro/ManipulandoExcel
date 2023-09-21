package com.pedro;

import java.io.File;

public class LocalizarPdf {
    public String nomeProcurar(String nome) {
        return nome;
    }
    public static void localizarArquivosPdf(String nomeDestinatario, String valorTotal) {

        String diretorio = "C:\\Users\\CALL1\\Desktop\\teste";
        File diretorioArquivos = new File(diretorio);

        // Verifica se o diretório existe
        if (diretorioArquivos.exists() && diretorioArquivos.isDirectory()) {
            File[] arquivos = diretorioArquivos.listFiles();

            // Itera pelos arquivos no diretório
            for (File arquivo : arquivos) {
                if (arquivo.isFile() && arquivo.getName().contains(nomeDestinatario) && arquivo.getName().contains(valorTotal)) {
                    System.out.println("Encontrado NOME e VALORES iguais! Aguardando lançamento");
                    return; // Arquivo encontrado, não é necessário continuar a busca
                }
                else if(arquivo.isFile() && arquivo.getName().contains(nomeDestinatario)) {
                    System.out.println("Encontrado apenas FORNECEDOR! Aguardando lançamento");
                    return;
                }
                else if (arquivo.isFile() && arquivo.getName().contains(valorTotal)) {
                    System.out.println("Encontrado apenas VALOR! Aguardando lançamento");
                    return;
                }
            }

            // Se chegou até aqui, o arquivo não foi encontrado
            System.out.println("Não encontrado: " + nomeDestinatario);
        } else {
            System.out.println("O diretório especificado não existe ou não é um diretório válido.");
        }
    }
}