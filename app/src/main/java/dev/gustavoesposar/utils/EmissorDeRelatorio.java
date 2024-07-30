package dev.gustavoesposar.utils;

import dev.gustavoesposar.database.DatabaseManager;
import javafx.scene.control.Alert;

import javax.swing.JFileChooser;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

public class EmissorDeRelatorio {

    public static void gerarPDF(String dest, String sql) {

        try (ResultSet resultSet = DatabaseManager.executarConsulta(sql)) {

            ResultSetMetaData metaData = resultSet.getMetaData();
            int numColumns = metaData.getColumnCount();

            // Encontre a largura máxima de cada coluna
            float[] columnWidths = new float[numColumns];
            List<String[]> rows = new ArrayList<>();
            while (resultSet.next()) {
                String[] row = new String[numColumns];
                for (int i = 1; i <= numColumns; i++) {
                    String cellValue = resultSet.getString(i);
                    row[i - 1] = cellValue;
                    // Verifica se o tipo de valor da coluna é numérico
                    if (metaData.getColumnType(i) == java.sql.Types.NUMERIC) {
                        // Largura mínima para valores numéricos
                        columnWidths[i - 1] = Math.max(columnWidths[i - 1], Math.max(cellValue.length(), metaData.getColumnName(i).length()) * 7);
                    } else {
                        columnWidths[i - 1] = Math.max(columnWidths[i - 1], Math.max(cellValue.length(), metaData.getColumnName(i).length()) * 7);
                    }
                }
                rows.add(row);
            }

            PDDocument document = new PDDocument();
            PDPage page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(25, 550);

            // Adiciona cabeçalhos da tabela
            for (int i = 1; i <= numColumns; i++) {
                String columnName = metaData.getColumnName(i);
                contentStream.showText(columnName + " ");
                contentStream.newLineAtOffset(columnWidths[i - 1], 0);
            }

            contentStream.newLineAtOffset(-calculateTotalWidth(columnWidths), -14.5f);

            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);

            // Adiciona dados da tabela
            int recordCount = 0;
            for (String[] row : rows) {
                for (int i = 0; i < numColumns; i++) {
                    String cellValue = row[i];
                    contentStream.showText(cellValue + " ");
                    contentStream.newLineAtOffset(columnWidths[i], 0);
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
                        String columnName = metaData.getColumnName(i);
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

            janelaInformativa("PDF criado com sucesso!");

        } catch (IOException e) {
            janelaInformativa("Ocorreu um erro ao salvar o PDF!");
        } catch (SQLException e) {
            janelaInformativa("Ocorreu um erro de comunicação com o banco de dados");
        }
    }

    private static float calculateTotalWidth(float[] widths) {
        float totalWidth = 0;
        for (float width : widths) {
            totalWidth += width;
        }
        return totalWidth;
    }

    public static String selectFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar PDF");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            // Adicionar extensão .pdf se não estiver presente
            if (!fileToSave.getAbsolutePath().endsWith(".pdf")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".pdf");
            }
            return fileToSave.getAbsolutePath();
        }

        return null;
    }

    private static void janelaInformativa(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null); // Sem cabeçalho
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}