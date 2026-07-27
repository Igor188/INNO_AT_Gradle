import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;



public class GenerateSQL {
    public static void main(String[] args) {
        // Шаблон одной записи (все поля кроме id)
        String template = "('%d','ba33b931-21f2-4cd7-bc11-b122c350ca23',NULL,NULL,'2026-07-27',NULL,'SINGLE',NULL,'ba33b931-21f2-4cd7-bc11-b122c350ca23',NULL,NULL,NULL,NULL,NULL,'PICKUP_ON_MOVE','2026-07-27T10:37:31.027',NULL,NULL,'2026-07-27T10:37:31.027','1','1','0','0',NULL,'51.76544116666667','46.342329333333325',NULL)";

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO activity_result (id,activityId,bookingId,bookingModifiedTime,bookingDate,bookingStatus,processCode,contactCount,visitId,canceledReason,verification,transferResultReason,transferResultReasonCode,transferResultReasonComment,visitStatus,visitModifiedTime,artifacts,targetPerson,createTimeStamp,attachmentSent,encDpanSent,skipped,tries,lastErrorMessage,latitude,longitude,returnConnection) VALUES \n");

        // Генерируем 1000 записей
        for (int i = 1; i <= 1000; i++) {
            sb.append(String.format(template, i));
            if (i < 1000) {
                sb.append(",\n");
            } else {
                sb.append(";");
            }
        }

        // Записываем в файл
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("insert_1000.sql"))) {
            writer.write(sb.toString());
            System.out.println("Файл insert_1000.sql создан! Содержит 1000 записей.");
        } catch (IOException e) {
            System.err.println("Ошибка при записи файла: " + e.getMessage());
            e.printStackTrace();
        }
    }
}