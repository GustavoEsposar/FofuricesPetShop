package dev.gustavoesposar.utils;

import dev.gustavoesposar.database.DatabaseManager;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

public class GeradorPDF {
    private static String sql = "Select * from Fornecedor;";

    public static void generatePDF(String dest) {

        try (ResultSet resultSet = DatabaseManager.executarConsulta(sql)) {

            PDDocument document = new PDDocument();
            PDPage page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth())); 
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(25, 550);

             // Define larguras das colunas com base no tamanho máximo dos dados
             float[] columnWidths = new float[]{
                70,  // idFornecedor (11 caracteres + padding)
                170, // nomeFantasia (45 caracteres + padding)
                170, // razaoSocial (45 caracteres + padding)
                200, // email (45 caracteres + padding)
                70, // telefone (12 caracteres + padding)
                150  // cnpj (14 caracteres + padding)
            };

            // Adiciona cabeçalhos da tabela
            int numColumns = resultSet.getMetaData().getColumnCount();
            for (int i = 1; i <= numColumns; i++) {
                String columnName = resultSet.getMetaData().getColumnName(i);
                contentStream.showText(columnName + " ");
                contentStream.newLineAtOffset(columnWidths[i - 1], 0);
            }

            contentStream.newLineAtOffset(-calculateTotalWidth(columnWidths), -14.5f);

            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);

            // Adiciona dados da tabela
            int recordCount = 0;
            while (resultSet.next()) {
                for (int i = 1; i <= numColumns; i++) {
                    String cellValue = resultSet.getString(i);
                    contentStream.showText(cellValue + " ");
                    contentStream.newLineAtOffset(columnWidths[i - 1], 0);
                }
                contentStream.newLineAtOffset(-calculateTotalWidth(columnWidths), -14.5f);
                recordCount++;

                // Verifica se o limite de 10 registros por página foi atingido
                if (recordCount % 10 == 0) {
                    contentStream.endText();
                    contentStream.close();
                    page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.beginText();
                    contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
                    contentStream.setLeading(14.5f);
                    contentStream.newLineAtOffset(25, 550);

                    // Reescreve os cabeçalhos da tabela na nova página
                    for (int i = 1; i <= numColumns; i++) {
                        String columnName = resultSet.getMetaData().getColumnName(i);
                        contentStream.showText(columnName + " ");
                        contentStream.newLineAtOffset(columnWidths[i - 1], 0);
                    }
                    contentStream.newLineAtOffset(-calculateTotalWidth(columnWidths), -14.5f);
                }
            }

            contentStream.endText();
            contentStream.close();
            document.save(dest);
            document.close();

            System.out.println("PDF criado com sucesso!");

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private static float calculateTotalWidth(float[] widths) {
        float totalWidth = 0;
        for (float width : widths) {
            totalWidth += width;
        }
        return totalWidth;
    }
}
