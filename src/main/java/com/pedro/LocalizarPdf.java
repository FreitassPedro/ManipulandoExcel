package com.pedro;

import org.apache.commons.text.similarity.JaroWinklerSimilarity;

import java.io.File;

public class LocalizarPdf {
    public String nomeProcurar(String nome) {
        return nome;
    }

    public static String localizarArquivosPdf(String nomeDestinatario, String valorTotal, String titulo) {
        JaroWinklerSimilarity similarity = new JaroWinklerSimilarity();

        String diretorio = "C:\\Users\\CALL1\\Desktop\\projetoExcel\\comprovantes";
        File diretorioArquivos = new File(diretorio);

        // Verifica se o diretório existe
        if (diretorioArquivos.exists() && diretorioArquivos.isDirectory()) {
            File[] arquivos = diretorioArquivos.listFiles();

            // Itera pelos arquivos no diretório

            for (File arquivo : arquivos) {
                String nomeDestinatarioArquivo = "";
                try {
                    String[] partes = arquivo.getName().split(" -- ");
                    nomeDestinatarioArquivo = partes[1];
                } catch (ArrayIndexOutOfBoundsException e) {
                    nomeDestinatarioArquivo = arquivo.getName();
                }

                double score = similarity.apply(nomeDestinatarioArquivo, nomeDestinatario);

                double limiteSimilaridade = 0.70;

                if (score >= limiteSimilaridade) {
                    System.out.println("Comprovante: " + nomeDestinatarioArquivo);
                    System.out.println("Similaridade: " + score);
                }
                if (arquivo.isFile() && score >= limiteSimilaridade && arquivo.getName().contains(valorTotal)) {
                    System.out.println("PDF ENCONTRADO: NOME E VALOR -------- lançar!");
                    adicionarTitulo(arquivo, titulo);
                    
                    return "nomeValor"; // Arquivo encontrado, não é necessário continuar a busca
                } else if (arquivo.isFile() && score >= limiteSimilaridade) {
                    System.out.println("PDF ENCONTRADO: NOME");
                    return "nome";
                } else if (arquivo.isFile() && arquivo.getName().contains(valorTotal)) {
                    System.out.println("PDF ENCONTRADO: VALOR");
                    return "valor";
                }
            }

            // Se chegou até aqui, o arquivo não foi encontrado
            System.out.println("COMPROVANTE: Não encontrado");
            return "Não encontrado";
        } else {
            System.out.println("O diretório especificado não existe ou não é um diretório válido.");
        }
        return "Não encontrado 2";
    }

    public static String directory() {
        String username = System.getProperty("user.name");
        return "C:\\Users\\" + username + "\\Desktop\\projetoExcel\\comprovantes\\";
    }
       public static void adicionarTitulo(File arquivo, String titulo) {
        String[] nomeTratar = arquivo.getName().split(".pdf");
        nomeTratar[0] = nomeTratar[0].replace("--", "-");
        if(!titulo.equals("null")) {
        String nomeFinal = nomeTratar[0] + " - " + titulo + ".pdf";
        
        File arquivoFinal = new File(directory(), nomeFinal);

        arquivo.renameTo(arquivoFinal);
       }


    }
}